package kr.ac.jbnu.se.mobile.game2048;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainView extends View {

    //Internal Constants
    private static final String TAG = GameView.class.getSimpleName();
    //Internal variables
    private final Paint paint = new Paint();
    public int startingX;
    public int startingY;
    public int endingX;
    public int endingY;

    Rectangle rStart;
    Rectangle rHighscore;
    Rectangle rSettings;
    //Text
    private float bodyTextSize;
    //Layout variables
    private int cellSize = 0;
    private float textSize = 0;
    private int gridWidth = 0;
    //Assets
    private Drawable backgroundRectangle;
    private Bitmap background = null;

    Context context;

    private final int numSquaresX = 4;
    private final int numSquaresY = 4;

    public MainView(Context context) {
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
        drawCell(canvas, rStart, getResources().getDrawable(R.drawable.cell_rectangle_4), "Start Game", getResources().getColor(R.color.black));
        drawCell(canvas, rHighscore, getResources().getDrawable(R.drawable.cell_rectangle_8), "Highscore", getResources().getColor(R.color.text_white));
        drawCell(canvas, rSettings, getResources().getDrawable(R.drawable.cell_rectangle_1024), "", -1);
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

        rStart = calculateRectangle(0, 0, 3, 2);
        rHighscore = calculateRectangle(0, 2, 2, 1);
        rSettings = calculateRectangle(3, 3, 1, 1);

        InputListener inputListener = new InputListener(this);
        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rStart)){
                    Intent intent = new Intent(context, GameActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rHighscore)){
                    Intent intent = new Intent(context, HighscoreActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rSettings)){
                    Intent intent = new Intent(context, SettingsActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        setOnTouchListener(inputListener);
    }

    private boolean isInRange(float posX, float posY, Rectangle range){
        return (range.getStartX() <= posX && posX <= range.getEndX()) && (range.getStartY() <= posY && posY <= range.getEndY());
    }

}
