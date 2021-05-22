package kr.ac.jbnu.se.mobile.game2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;

public class SettingsView extends View {

    //Internal Constants
    private static final String TAG = GameView.class.getSimpleName();
    //Internal variables
    private final Paint paint = new Paint();
    public int startingX;
    public int startingY;
    public int endingX;
    public int endingY;

    Rectangle rBackgroundMusic;
    Rectangle rNotification;
    Rectangle rBackgroundMusicCheck;
    Rectangle rNotificationCheck;
    Rectangle rTimer;
    Rectangle rTimerNumber;
    Rectangle rTimerUp;
    Rectangle rTimerDown;
    Rectangle rBack;
    //Text
    private float bodyTextSize;
    //Layout variables
    private int cellSize = 0;
    private float textSize = 0;
    private int gridWidth = 0;
    //Assets
    private Drawable backgroundRectangle;
    private Bitmap background = null;

    private Drawable dBgmCheck, dNotificationCheck, dCheckTrue, dCheckFalse, dUp, dDown;

    //Preferences
    private SharedPreferences appData;
    private boolean isBgmChecked;
    private boolean isNotificationChecked;
    private int timerNum;

    Context context;

    private final int numSquaresX = 4;
    private final int numSquaresY = 4;

    public SettingsView(Context context) {
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
        appData = context.getSharedPreferences("appData", context.MODE_PRIVATE);
        loadSp();


        Drawable d;
        d = getResources().getDrawable(R.drawable.ic_check).getConstantState().newDrawable();
        int padding = 25;
        LayerDrawable ld;
        ld = new LayerDrawable(new Drawable[]{d});
        ld.setLayerInset(0, padding, padding, padding, padding);
        ld.setAlpha(255);
        dCheckTrue = ld.getConstantState().newDrawable().mutate();
        ld.setAlpha(100);
        dCheckFalse = ld.getConstantState().newDrawable().mutate();

        padding = 80;
        d = getResources().getDrawable(R.drawable.ic_up).getConstantState().newDrawable();
        ld = new LayerDrawable(new Drawable[]{d});
        ld.setLayerInset(0, padding, padding, padding, padding);
        dUp = ld;

        d = getResources().getDrawable(R.drawable.ic_down).getConstantState().newDrawable();
        ld = new LayerDrawable(new Drawable[]{d});
        ld.setLayerInset(0, padding, padding, padding, padding);
        dDown = ld;
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
        if(isBgmChecked)
            dBgmCheck = dCheckTrue;
        else
            dBgmCheck = dCheckFalse;

        if(isNotificationChecked)
            dNotificationCheck = dCheckTrue;
        else
            dNotificationCheck = dCheckFalse;



        drawCell(canvas, rBackgroundMusic, getResources().getDrawable(R.drawable.cell_rectangle_4), "Background Music", getResources().getColor(R.color.black));
        drawCell(canvas, rBackgroundMusicCheck, dBgmCheck, "", -1);
        drawCell(canvas, rNotification, getResources().getDrawable(R.drawable.cell_rectangle_4), "Notification", getResources().getColor(R.color.black));
        drawCell(canvas, rNotificationCheck, dNotificationCheck, "", -1);
        drawCell(canvas, rTimer, getResources().getDrawable(R.drawable.cell_rectangle_4), "Timer", getResources().getColor(R.color.black));
        drawCell(canvas, rTimerNumber, getResources().getDrawable(R.drawable.cell_rectangle_2), "" + timerNum, getResources().getColor(R.color.white));
        drawCell(canvas, rTimerUp, dUp, "", -1);
        drawCell(canvas, rTimerDown, dDown, "", -1);
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

        rBackgroundMusic = calculateRectangle(0, 0, 3, 1);
        rNotification = calculateRectangle(0, 1, 3, 1);
        rBackgroundMusicCheck = calculateRectangle(3, 0, 1, 1);
        rNotificationCheck = calculateRectangle(3, 1, 1, 1);
        rTimer = calculateRectangle(0, 2, 1, 1);
        rTimerNumber = calculateRectangle(1, 2, 1, 1);
        rTimerUp = calculateRectangle(2, 2, 1, 1);
        rTimerDown = calculateRectangle(3, 2, 1, 1);
        rBack = calculateRectangle(3, 3, 1, 1);

        InputListener inputListener = new InputListener(this);

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rBackgroundMusicCheck)) {
                    isBgmChecked = !isBgmChecked;
                    saveMusicSp(isBgmChecked);

                    Intent intent = new Intent(context, MusicService.class);
                    intent.putExtra(MusicService.MESSEAGE_KEY, isBgmChecked);
                    context.startService(intent);

                    invalidate();
                }
            }
        });

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rNotificationCheck)) {
                    isNotificationChecked = !isNotificationChecked;
                    saveNotificationSp(isNotificationChecked);
                    invalidate();
                }
            }
        });

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rTimerUp)) {
                    if(timerNum < 60)
                        timerNum++;
                    saveTimerSp(timerNum);
                    invalidate();
                }
            }
        });

        inputListener.addClickInputHandler(new IClickInputHandler() {
            @Override
            public void handle(float touchPosX, float touchPosY) {
                if(isInRange(touchPosX, touchPosY, rTimerDown)) {
                    if(timerNum > 10)
                        timerNum--;
                    saveTimerSp(timerNum);
                    invalidate();
                }
            }
        });

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

    private void loadSp(){//설정 값 불러오기, 존재하지 않을 시 기본값
        isBgmChecked = appData.getBoolean("SAVE_MUSIC_DATA", false);
        isNotificationChecked = appData.getBoolean("SAVE_NOTIFICATION_DATA", false);
        timerNum = appData.getInt("SAVE_TIMER_DATA",30);
    }

    public void saveMusicSp(boolean value){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_MUSIC_DATA", value);
        editor.apply();
    }

    public void saveNotificationSp(boolean value){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_NOTIFICATION_DATA", value);
        editor.apply();
    }

    public void saveTimerSp(int time){
        SharedPreferences.Editor editor = appData.edit();
        editor.putInt("SAVE_TIMER_DATA", time);
        editor.apply();
    }

    public int getTimerSp(){
        timerNum = appData.getInt("SAVE_TIMER_DATA", 30);
        return timerNum;
    }

}
