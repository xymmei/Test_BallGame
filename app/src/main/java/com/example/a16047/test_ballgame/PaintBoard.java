package com.example.a16047.test_ballgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;

/**
 * Created by 16047 on 2017/8/16.
 */

public class PaintBoard extends View {
    private float brickWidth, brickHeight, brickX, brickY;
    private int racketX, racketY, racketHeight, racketWidth;
    private int ballX, ballY, ballSize;
    private int[] colors = new int[]
            {
                    Color.RED, Color.MAGENTA, Color.BLUE,
                    Color.GREEN, Color.YELLOW, Color.CYAN,
                    Color.GRAY, Color.DKGRAY, Color.TRANSPARENT
            };
    private int screenWidth, screenHeight;

    public PaintBoard(Context context) {
        super(context);
    }

    public PaintBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        brickWidth = dm.widthPixels / 10;
        brickHeight = brickWidth / 2;
        System.out.println("-->" + dm.widthPixels + "," + dm.heightPixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //paint a circle
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        for (int j = 0; j < 4; j++) {

            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                int position = random.nextInt(8);
                paint.setColor(colors[position]);
                RectF rectF = new RectF(brickWidth * i, brickHeight * j + 1, brickWidth * i + brickWidth, brickHeight * j + 1 + brickHeight);
                canvas.drawRoundRect(rectF, 10, 10, paint);
            }
        }

        paint.setColor(Color.WHITE);
        canvas.drawCircle(384, 975, brickHeight / 2, paint);
        paint.setColor(Color.YELLOW);
        RectF rectF = new RectF(314, 995, 454, 1020);
        canvas.drawRoundRect(rectF, 5, 5, paint);
    }
}