package kr.ac.jbnu.se.mobile.game2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class HighscoreView extends View {

    //Internal Constants
    private static final String TAG = HighscoreView.class.getSimpleName();
    public static final String DB_PATH = "score.db";
    //Internal variables
    private final Paint paint = new Paint();
    public int startingX;
    public int startingY;
    public int endingX;
    public int endingY;

    Rectangle rBack;
    Rectangle[] rScores;
    //Text
    private float bodyTextSize;
    //Layout variables
    private int cellSize = 0;
    private float textSize = 0;
    private int gridWidth = 0;
    //Assets
    private Drawable backgroundRectangle;
    private Bitmap background = null;

    DBHandler mHandler = null;
    Cursor mCursor = null;
    int scores[];

    Context context;

    private final int numSquaresX = 4;
    private final int numSquaresY = 4;

    public HighscoreView(Context context) {
        super(context);

        this.context = context;

        Resources resources = context.getResources();
        try {
            //Getting assets
            backgroundRectangle = resources.getDrawable(R.drawable.background_rectangle);
            this.setBackgroundColor(resources.getColor(R.color.background));
            Typeface font = Typeface.createFromAsset(resources.getAssets(), "ClearSans-Bold.ttf");
            paint.setTypeface(font);
            paint.setAntiAlias(true);

        } catch (Exception e) {
            Log.e(TAG, "Error getting assets?", e);
        }
        initScores();

    }

    private void initScores() {
        mHandler = DBHandler.open(context, DB_PATH);
        mCursor = mHandler.select();
        scores = new int[numSquaresX*numSquaresY];
        mCursor.moveToFirst();

        for(int i = 0; i < mCursor.getCount(); i++){
            scores[i] = mCursor.getInt(1);
            mCursor.moveToNext();
        }
    }

    private static int log2(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Reset the transparency of the screen

        canvas.drawBitmap(background, 0, 0, paint);
        drawCells(canvas);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldW, int oldH) {
        super.onSizeChanged(width, height, oldW, oldH);
        getLayout(width, height);
        createBackgroundBitmap(width, height);
    }

    private void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        drawDrawable(canvas, backgroundRectangle, startingX, startingY, endingX, endingY);
    }

    //Renders the set of 16 background squares.
    private void drawBackgroundGrid(Canvas canvas) {
        Resources resources = getResources();
        Drawable backgroundCell = resources.getDrawable(R.drawable.cell_rectangle);
        // Outputting the game grid
        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                drawDrawable(canvas, backgroundCell, sX, sY, eX, eY);
            }
        }
    }

    private void drawCells(Canvas canvas) {
        for(int i = 0; i < scores.length; i++){
            String scoreText = "";
            if(scores[i] != 0)
                scoreText = "" + scores[i];
            drawCell(canvas, rScores[i], calculateScoreDrawable(i), scoreText, getResources().getColor(R.color.black));
        }
        drawCell(canvas, rBack, getResources().getDrawable(R.drawable.cell_rectangle_4), "Back", getResources().getColor(R.color.black));
    }

    private Rectangle calculateRectangle(int posX, int posY, int sizeX, int sizeY){
        int startX, startY, endX, endY;

        startX = startingX + gridWidth + (cellSize + gridWidth) * posX;
        startY = startingY + gridWidth + (cellSize + gridWidth) * posY;
        endX =  startX + cellSize + (gridWidth + cellSize) * (sizeX - 1);
        endY = startY + cellSize + (gridWidth + cellSize) * (sizeY - 1);

        return new Rectangle(startX, startY, endX, endY);
    }

    private void drawCell(Canvas canvas, Rectangle rectangle, Drawable drawable, String text, int textColor) {

        int startX, startY, endX, endY;

        startX = rectangle.getStartX();
        startY = rectangle.getStartY();
        endX =  rectangle.getEndX();
        endY =  rectangle.getEndY();

        drawable.setBounds(startX, startY, endX, endY);
        drawable.draw(canvas);
        if(textColor != -1)
            paint.setColor(textColor);
        paint.setTextSize(bodyTextSize);
        canvas.drawText(text, (startX + endX) / 2f, (startY + endY - ((paint.descent() + paint.ascent()) / 2)) / 2f, paint);
    }

    private void createBackgroundBitmap(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);
    }

    private void getLayout(int width, int height) {
        cellSize = Math.min(width / (numSquaresX + 1), height / (numSquaresY + 3));
        gridWidth = cellSize / 7;
        int screenMiddleX = width / 2;
        int screenMiddleY = height / 2;
        int boardMiddleY = screenMiddleY;

        //Grid Dimensions
        double halfNumSquaresX = numSquaresX / 2d;
        double halfNumSquaresY = numSquaresY / 2d;
        startingX = (int) (screenMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2);
        endingX = (int) (screenMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2);
        startingY = (int) (boardMiddleY - (cellSize + gridWidth) * halfNumSquaresY - gridWidth / 2 );
        endingY = (int) (boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2);

        // Text Dimensions
        paint.setTextSize(cellSize);
        textSize = cellSize * cellSize / Math.max(cellSize, paint.measureText("0000"));

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(1000);

        paint.setTextSize(cellSize);
        bodyTextSize = (int) (textSize / 1.5);

        rBack = calculateRectangle(3, 3, 1, 1);
        makeScoresRectangle();

        InputListener inputListener = new InputListener(this);

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rBack)){
                    ((Activity)context).finish();
                }
            }
        });

        setOnTouchListener(inputListener);
    }

    private boolean isInRange(float posX, float posY, Rectangle range){
        return (range.getStartX() <= posX && posX <= range.getEndX()) && (range.getStartY() <= posY && posY <= range.getEndY());
    }

    private Drawable calculateScoreDrawable(int rank){
        if(rank > 11)
            return getResources().getDrawable(R.drawable.cell_rectangle_2);
        else if(rank > 7)
            return getResources().getDrawable(R.drawable.cell_rectangle_4);
        else if(rank > 5)
            return getResources().getDrawable(R.drawable.cell_rectangle_8);
        else if(rank > 3)
            return getResources().getDrawable(R.drawable.cell_rectangle_16);
        else if(rank > 0)
            return getResources().getDrawable(R.drawable.cell_rectangle_32);
        else if(rank == 0)
            return getResources().getDrawable(R.drawable.cell_rectangle_64);

        return getResources().getDrawable(R.drawable.cell_rectangle_4096);
    }

    private void makeScoresRectangle(){
        rScores = new Rectangle[numSquaresX * numSquaresY];
        for(int i = 0; i < rScores.length; i++){
            int row = i / 4;
            int column = i - row * 4;
            if(row % 2 == 1){
                rScores[i] = calculateRectangle(3 - column, row, 1, 1);
            }
            else {
                rScores[i] = calculateRectangle(column, row, 1, 1);
            }
        }
    }

}
