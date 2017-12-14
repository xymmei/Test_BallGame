package com.example.a16047.test_ballgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

/**
 * Created by 16047 on 2017/8/17.
 */

public class GameView extends View {
    //砖块宽高，以及左上角坐标
    static float brickWidth, brickHeight;
    static Racket racket;
    //    关卡砖块排列
    static Map<Float,List<Brick>> bricks,bricks1, bricks2, bricks3, bricks4,bricks5,bricks6,bricks7,bricks8,bricks9,bricks10;
    //    安全区
    static float saveAreaX = 0, saveAreaY = 0;
    //道具
    static List<Tool> toolList;
    //小球
    static List<Ball> ballList;
    //子弹
    static List<Gun> gunList;
    //导弹
    static List<Daodan> daodanList;
    //火球
    static List<Fireball> fireballList;
    //分数
    static int presentBallId;
    static int score=0;
    static int lives=4;
    static int moneyNum=150;
    //道具的图片
    static int[] toolBitmaps=new int[]
            {
                    R.mipmap.ballto3,R.mipmap.money2,R.mipmap.heart,
                    R.mipmap.qiang,R.mipmap.daodan_fall,R.mipmap.fireball1_small,
                    R.mipmap.big,R.mipmap.fast,R.mipmap.small,
                    R.mipmap.short2,R.mipmap.long2,R.mipmap.slow
            };
    //砖块的颜色
    private int[] brickBitmaps = new int[]
            {
                    R.mipmap.brick1,R.mipmap.brick2,R.mipmap.brick3,
                    R.mipmap.brick4,R.mipmap.brick5,R.mipmap.brick6
            };
    static Bitmap stop,set,money;
    private MyMediaPlayer myMediaPlayer=MainActivity.myMediaPlayer;
    private int screenWidth, screenHeight;
    private float beginX, beginY;
    private float lastX, lastY;
    private boolean isMove=false;
    private Context context1;
    static AlertDialog gameover_dialog,victory_dialog;
    //游戏结束
    static boolean isGameOver = false;
    static boolean allowClickBall=false;
    static boolean hasGun=false;
    static int gunNum=0;
    static boolean hasDaodan=false;
    static int daodanNum=0;
    static boolean hasFireball=false;
    static int fireballNum=0;
    static int whichPressed=0;
    static int guanqiaNum=1;
    public void initialize(Context context){
        context1 = context;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        System.out.println("---->" + screenWidth + "," + screenHeight);
        stop = BitmapFactory.decodeResource(context.getResources(), R.mipmap.stop);
        set = BitmapFactory.decodeResource(context.getResources(), R.mipmap.help);
        money=BitmapFactory.decodeResource(context.getResources(), R.mipmap.money1);
        brickWidth = screenWidth / 9;//76
        brickHeight = brickWidth/2;//38
        float ballSize = brickHeight / 3;//12
        float racketHeight = ballSize+GameActivity.speed+5;//25
        float racketWidth=ballSize*10;
        Random rand = new Random();
        float racketX = rand.nextInt((int)(screenWidth - racketWidth));
        float racketY = (int) (screenHeight - 6 * racketHeight);
        float ballX = racketX + racketWidth / 2;
        float ballY = racketY - ballSize;
        racket=new Racket(racketX,racketY,racketWidth,racketHeight);
        Ball ball=new Ball(ballX,ballY,ballSize,0,0);
        Bitmap ballBitmap=BitmapFactory.decodeResource(context1.getResources(),presentBallId);
        ball.setBitmap(ballBitmap);
        System.out.println("--presentBallId-->"+presentBallId);
        allowClickBall=true;
        bricksInitialize();
        guanqiaNum=guanqiaNum%11==0?1:guanqiaNum;
        if(guanqiaNum==1){
            bricks=bricks1;
            saveAreaX = screenWidth;
            saveAreaY = brickHeight*2  + brickHeight * 9 + stop.getHeight();
        }else if(guanqiaNum==2){
            bricks=bricks2;
            saveAreaX = screenWidth;
            saveAreaY = brickHeight*2  + brickHeight * 13 + stop.getHeight();
        }else if(guanqiaNum==3){
            bricks=bricks3;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight*2  + brickHeight * 12 + stop.getHeight();
        }else if(guanqiaNum==4){
            bricks=bricks4;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 14 + stop.getHeight();
        }else if(guanqiaNum==5){
            bricks=bricks5;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 14 + stop.getHeight();
        }else if(guanqiaNum==6){
            bricks=bricks6;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 15 + stop.getHeight();
        }else if(guanqiaNum==7){
            bricks=bricks7;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 14 + stop.getHeight();
        }else if(guanqiaNum==8){
            bricks=bricks8;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 13 + stop.getHeight();
        }else if(guanqiaNum==9){
            bricks=bricks9;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 16 + stop.getHeight();
        }else if(guanqiaNum==10){
            bricks=bricks10;
            saveAreaX=screenWidth;
            saveAreaY=brickHeight * 15 + stop.getHeight();
        }
        toolList=new ArrayList<Tool>();
        ballList=new ArrayList<Ball>();
        gunList=new ArrayList<Gun>();
        daodanList=new ArrayList<Daodan>();
        fireballList=new ArrayList<Fireball>();
        hasDaodan=false;
        daodanNum=0;
        hasGun=false;
        gunNum=0;
        hasFireball=false;
        fireballNum=0;
        ballList.add(ball);

    }

    public void bricksInitialize(){
        bricks1 = new HashMap<Float, List<Brick>>();
        int col=9,bitmapPosition;
        float x=brickWidth/2,y=brickHeight*2;
        for (int j = 0; j < col; j++) {
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition = j%brickBitmaps.length;
            for (int i = 0; i < col-j; i++) {

                Brick brick = new Brick(x * j + i * brickWidth, y+ brickHeight * j + stop.getHeight(), bitmapPosition);
//                Brick brick = new Brick(i * brickWidth, y * j + brickHeight * j + bitmap.getHeight(), bitmapPosition);
                if(j==col-3&&i==1)brick.setToolNum(0);
                if(j==col-9&&i==3)brick.setToolNum(2);
                if(j==col-2&&i==0||j==col-6&&i==5||j==col-8&&i==4||j==col-4&&i==1)brick.setToolNum(1);
                if(j==col-4&&i==3)brick.setToolNum(3);
                if(j==col-5&&i==3)brick.setToolNum(4);
                if(j==col-6&&i==0)brick.setToolNum(5);
                if(j==col-7&&i==2)brick.setToolNum(6);
                if(j==col-7&&i==5)brick.setToolNum(7);
                if(j==col-8&&i==4) brick.setToolNum(8);
                if(j==col-8&&i==6) brick.setToolNum(9);
                if(j==col-8&&i==1)brick.setToolNum(10);
                if(j==col-9&&i==3)brick.setToolNum(11);
                brickList.add(brick);
            }
            bricks1.put(y + brickHeight * j + stop.getHeight(),brickList);
        }
        bricks2=new HashMap<Float, List<Brick>>();
        col=13;
        int lie=8;
        x=brickWidth;
        for(int i=0;i<col;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            for(int j=1;j<lie;j++){
                if(i==0||i==1||i==col-1||i==col-2||j==1||j==lie-1) {
                    bitmapPosition = 0;
                }else if(i==2||i==3||i==col-3||i==col-4||j==2||j==lie-2){
                    bitmapPosition=1;
                }else if(i==4||i==5||i==col-5||i==col-6||j==3||j==lie-3){
                    bitmapPosition=2;
                }else {
                    bitmapPosition=3;
                }
                Brick brick=new Brick(j*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
                if(i==0&&j==3||i==2&&j==7||i==6&&j==4)brick.setToolNum(0);
                if(i==0&&j==6||i==4&&j==2||i==5&&j==5||i==8&&j==6||i==10&&j==6)brick.setToolNum(1);
                if(i==1&&j==5||i==9&&j==5)brick.setToolNum(2);
                if(i==3&&j==2||i==10&&j==5)brick.setToolNum(3);
                if(i==3&&j==7||i==11&&j==4)brick.setToolNum(4);
                if(i==4&&j==6||i==7&&j==1) brick.setToolNum(5);
                if(i==7&&j==4)brick.setToolNum(6);
                if(i==8&&j==2)brick.setToolNum(7);
                if(i==9&&j==3)brick.setToolNum(8);
                if(i==2&&j==3)brick.setToolNum(9);
                if(i==1&&j==1)brick.setToolNum(10);
                if(i==11&&j==1)brick.setToolNum(11);
                brickList.add(brick);
            }
            bricks2.put(y+stop.getHeight()+i*brickHeight,brickList);
        }
        bricks3=new HashMap<Float, List<Brick>>();
        col=13;
        lie=9;
        x=brickWidth;
        for(int i=0;i<2;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=4;
            for(int j=0;j<lie;j++){
                Brick brick=new Brick(j*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
                if(i==0&&j==3||i==1&&j==5) brick.setToolNum(1);
                if(i==1&&j==7) brick.setToolNum(0);
                if(i==0&&j==8) brick.setToolNum(5);
                brickList.add(brick);
            }
            bricks3.put(y+stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=2;i<3;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=4;
            Brick brick=new Brick(4*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
            brick.setToolNum(4);
            brickList.add(brick);
            bricks3.put(y+stop.getHeight()+i*brickHeight,brickList);
        }
        x=brickWidth/2;
        for(int i=3;i<9;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=5;
            for(int j=0;j<3;j++){
                Brick brick=new Brick((2*j+1)*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
                if(i==4&&j==2)brick.setToolNum(2);
                if(i==3&&j==0)brick.setToolNum(1);
                if(i==7&&j==1)brick.setToolNum(9);
                if(i==8&&j==1)brick.setToolNum(11);
                brickList.add(brick);
            }
            bitmapPosition=4;
            Brick brick1=new Brick(8*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
            if(i==5)brick1.setToolNum(3);
            if(i==8)brick1.setToolNum(4);
            brickList.add(brick1);
            bitmapPosition=5;
            for(int j=5;j<8;j++){
                Brick brick=new Brick((2*j+1)*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
                if(i==3&&j==5)brick.setToolNum(8);
                if(i==4&&j==7)brick.setToolNum(10);
                if(i==7&&j==6)brick.setToolNum(6);
                if(i==5&&j==5)brick.setToolNum(1);
                brickList.add(brick);
            }
            bricks3.put(y+stop.getHeight()+i*brickHeight,brickList);
        }
        x=brickWidth;
        for(int i=9;i<10;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=4;
            Brick brick=new Brick(4*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
            brick.setToolNum(1);
            brickList.add(brick);
            bricks3.put(y+stop.getHeight()+i*brickHeight,brickList);
        }
        x=brickWidth;
        for(int i=10;i<12;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=4;
            for(int j=0;j<lie;j++){
                Brick brick=new Brick(j*x,y+stop.getHeight()+i*brickHeight,bitmapPosition);
                if(i==10&&j==2)brick.setToolNum(0);
                if(i==10&&j==6)brick.setToolNum(1);
                if(i==11&&j==1)brick.setToolNum(7);
                if(i==11&&j==4)brick.setToolNum(3);
                if(i==11&&j==7)brick.setToolNum(9);
                brickList.add(brick);
            }
            bricks3.put(y+stop.getHeight()+i*brickHeight,brickList);
        }
        bricks4 = new HashMap<Float, List<Brick>>();
        col=14;
        x=brickWidth;
        y=brickHeight;
        for(int i=1;i<col;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            if(i%4==1){
                bitmapPosition=5;
                for(int j=0;j<9;j++){
                    Brick brick=new Brick(j*x,stop.getHeight()+i*brickHeight,bitmapPosition);
                    if(i==1&&j==0||i==1&&j==6||i==5&&j==2||i==5&&j==7||i==9&&j==3||i==13&&j==5)brick.setToolNum(1);
                    if(i==1&&j==4)brick.setToolNum(10);
                    if(i==1&&j==8)brick.setToolNum(8);
                    if(i==5&&j==4||i==9&&j==7)brick.setToolNum(0);
                    if(i==5&&j==8)brick.setToolNum(3);
                    if(i==9&&j==0)brick.setToolNum(5);
                    if(i==9&&j==5)brick.setToolNum(4);
                    if(i==13&&j==2)brick.setToolNum(9);
                    brickList.add(brick);
                }
            }else{
                bitmapPosition=2;
                for(int j=0;j<9;j++){
                    if(j%2==1){
                        Brick brick=new Brick(j*x,stop.getHeight()+i*brickHeight,bitmapPosition);
                        if(i==3&&j==3)brick.setToolNum(11);
                        if(i==3&&j==5||i==12&&j==1)brick.setToolNum(1);
                        if(i==7&&j==1||i==11&&j==5)brick.setToolNum(3);
                        if(i==7&&j==5)brick.setToolNum(6);
                        if(i==11&&j==7)brick.setToolNum(7);
                        brickList.add(brick);
                    }
                }
            }
            bricks4.put(stop.getHeight()+i*brickHeight,brickList);
        }
        bricks5 = new HashMap<Float, List<Brick>>();
        col=14;
        x=brickWidth;
        y=brickHeight;
        for(int i=1;i<2;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                Brick brick=new Brick(j*x,stop.getHeight()+i*brickHeight,bitmapPosition);
                if(j==1)brick.setToolNum(1);
                else if(j==4)brick.setToolNum(9);
                else if(j==7)brick.setToolNum(4);
                brickList.add(brick);
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);

        }
        for(int i=2;i<4;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j==0||j==7) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    if(i==2&&j==0)brick.setToolNum(8);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=4;i<5;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j!=1&&j!=6) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    if(j==4)brick.setToolNum(1);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=5;i<7;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j==0||j==7||j==2||j==5) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    if(i==6&&j==2)brick.setToolNum(3);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=7;i<8;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j!=1&&j!=6&&j!=3) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    if(j==0)brick.setToolNum(5);
                    else if(j==5)brick.setToolNum(6);
                    else if(j==7)brick.setToolNum(3);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=8;i<10;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j==0||j==7||j==2) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=10;i<11;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j!=1) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    if(j==0)brick.setToolNum(7);
                    else if(j==2)brick.setToolNum(0);
                    else if(j==4)brick.setToolNum(1);
                    else if(j==7)brick.setToolNum(10);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=11;i<13;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<8;j++){
                if(j==0) {
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    brickList.add(brick);
                }
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=13;i<col;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<9;j++){
                    Brick brick = new Brick(j * x, stop.getHeight() + i * brickHeight, bitmapPosition);
                    if(j==2)brick.setToolNum(11);
                    else if(j==4)brick.setToolNum(1);
                    brickList.add(brick);
            }
            bricks5.put(stop.getHeight()+i*brickHeight,brickList);
        }
        bricks6= new HashMap<Float, List<Brick>>();
        col=14;
        x=brickWidth/2;
        y=brickHeight;
        for(int i=1;i<5;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            for(int j=0;j<i;j++){
                Brick brick = new Brick(4*brickWidth-(i-1)*x+j * brickWidth, stop.getHeight() + i * brickHeight, bitmapPosition);
                if(i==1)brick.setToolNum(8);
                else if(i==3&&j==2)brick.setToolNum(3);
                else if(i==4&&(j==1||j==2))brick.setToolNum(1);
                brickList.add(brick);
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=5;i<10;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            for(int j=0;j<9+5-i;j++){
                Brick brick = new Brick(x * (i-5) + j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==5&&j==0)brick.setToolNum(6);
                else if(i==5&&j==2||i==7&&j==6)brick.setToolNum(4);
                else if(i==5&&j==6||i==7&&j==2)brick.setToolNum(5);
                else if(i==6&&j==0||i==9&&j==2)brick.setToolNum(0);
                else if(i==6&&j==2)brick.setToolNum(7);
                else if(i==6&&j==7)brick.setToolNum(2);
                else if(i==8&&j==0)brick.setToolNum(11);
                else if(i==8&&j!=0&&j!=5)brick.setToolNum(1);
                else if(i==9&&j==4)brick.setToolNum(10);
                brickList.add(brick);
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=10;i<11;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            for(int j=1;j<8;j++){
                if(j!=4) {
                    Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(j==2)brick.setToolNum(10);
                    brickList.add(brick);
                }
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=11;i<12;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            for(int j=0;j<8;j++){
                if(j!=4&&j!=3) {
                    Brick brick = new Brick(x+j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(j==2)brick.setToolNum(3);
                    else if(j==5)brick.setToolNum(11);
                    else if(j==7)brick.setToolNum(9);
                    brickList.add(brick);
                }
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=12;i<13;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            for(int j=0;j<8;j++){
                if(j!=4&&j!=3&&j!=2&&j!=5) {
                    Brick brick = new Brick(x+j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(j==6)brick.setToolNum(0);
                    brickList.add(brick);
                }
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=13;i<14;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            for(int j=0;j<9;j++){
                if(j==0||j==8) {
                    Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    brickList.add(brick);
                }
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        for(int i=14;i<15;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=2;
            for(int j=0;j<9;j++){
                    Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(j==3||j==6)brick.setToolNum(1);
                    brickList.add(brick);
            }
            bricks6.put(stop.getHeight()+i*brickHeight,brickList);
        }
        bricks7=new HashMap<Float, List<Brick>>();
        col=14;
        x=brickWidth/2;
        y=brickHeight;
        for(int i=0;i<1;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=4;
            for(int j=0;j<9;j++){
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(j==1||j==4)brick.setToolNum(1);
                else if(j==3)brick.setToolNum(3);
                brickList.add(brick);
            }
            bricks7.put(brickHeight * i + stop.getHeight(),brickList);
        }
        for(int i=1;i<10;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=4;
            for(int j=0;j<10-i;j++){
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==1&&j==7||i==7&&j==1)brick.setToolNum(4);
                else if(i==2&&j==3)brick.setToolNum(0);
                else if(i==2&&j==6)brick.setToolNum(7);
                else if(i==3&&j==4||i==8&&j==0)brick.setToolNum(5);
                else if(i==3&&j==6||i==5&&j==3||i==7&&j==2)brick.setToolNum(1);
                else if(i==4&&j==0)brick.setToolNum(9);
                else if(i==5&&j==4)brick.setToolNum(11);
                else if(i==6&&j==2)brick.setToolNum(6);
                brickList.add(brick);
            }
            if(i>=4){
                bitmapPosition=5;
                for(int j=12-i;j<9;j++){
                    Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(i==4&&j==8)brick.setToolNum(2);
                    else if(i==5&&j==8)brick.setToolNum(9);
                    else if(i==7&&j==5)brick.setToolNum(0);
                    else if(i==7&&j==7||i==9&&j==4)brick.setToolNum(1);
                    else if(i==9&&j==3)brick.setToolNum(7);
                    else if(i==9&&j==6)brick.setToolNum(10);
                    brickList.add(brick);
                }
            }
            bricks7.put(brickHeight * i + stop.getHeight(),brickList);
        }
        for(int i=10;i<13;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=5;
            for(int j=12-i;j<9;j++){
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==10&&j==3)brick.setToolNum(8);
                else if(i==10&&j==7)brick.setToolNum(3);
                else if(i==12&&j==1)brick.setToolNum(0);
                else if(i==12&&j==3)brick.setToolNum(6);
                brickList.add(brick);
            }
            bricks7.put(brickHeight * i + stop.getHeight(),brickList);
        }
        for(int i=13;i<14;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=5;
            for(int j=0;j<9;j++){
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(j==2||j==7)brick.setToolNum(1);
                brickList.add(brick);
            }
            bricks7.put(brickHeight * i + stop.getHeight(),brickList);
        }
        bricks8=new HashMap<Float,List<Brick>>();
        x=brickWidth/2;
        for(int i=0;i<5;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=3;
            for(int j=0;j<=4-i;j++){
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==0&&j==4)brick.setToolNum(9);
                else if(i==1&&j==1)brick.setToolNum(1);
                else if(i==3&&j==1)brick.setToolNum(3);
                brickList.add(brick);
            }
            bitmapPosition=1;
            if(i>2){
                for(int j=4;j<2+i;j++){
                    Brick brick = new Brick(j * brickWidth-(i-3)*x, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(i==3&&j==4)brick.setToolNum(0);
                    brickList.add(brick);
                }
            }
            bitmapPosition=3;
            for(int j=4+i;j<9;j++){
                if(j==4-i)j++;
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==2&&j==7)brick.setToolNum(6);
                else if(i==4&&j==8)brick.setToolNum(1);
                brickList.add(brick);
            }
            bricks8.put(brickHeight * i + stop.getHeight(),brickList);
        }
        for(int i=5;i<8;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=1;
            if(i!=6) {
                for (int j = 3; j < 6; j++) {
                    Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(i==5&&j==5)brick.setToolNum(1);
                    brickList.add(brick);
                }
            }else {
                for (int j = 3; j < 7; j++) {
                    Brick brick = new Brick(j * brickWidth-x, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(j==4)brick.setToolNum(5);
                    else if(j==6)brick.setToolNum(4);
                    brickList.add(brick);
                }
            }
            bricks8.put(brickHeight * i + stop.getHeight(),brickList);
        }
        for(int i=8;i<13;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=3;
            for(int j=0;j<=i-8;j++){
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==9&&j==1)brick.setToolNum(10);
                else if(i==12&&j==0)brick.setToolNum(11);
                else if(i==12&&j==4)brick.setToolNum(1);
                brickList.add(brick);
            }
            bitmapPosition=1;
            if(i<=10){
                for(int j=4;j<14-i;j++){
                    Brick brick = new Brick(j * brickWidth-(9-i)*x, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(i==8&&j==5)brick.setToolNum(8);
                    brickList.add(brick);
                }
            }
            bitmapPosition=3;
            for(int j=16-i;j<9;j++){
                if(j==i-8)j++;
                Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==8&&j==8)brick.setToolNum(1);
                else if(i==10&&j==7)brick.setToolNum(7);
                brickList.add(brick);
            }
            bricks8.put(brickHeight * i + stop.getHeight(),brickList);
        }
        bricks9=new HashMap<Float,List<Brick>>();
        for(int i=1;i<16;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            if(i!=8) {
                for (int j = 0; j < 4; j++) {
                    if (i < 8) bitmapPosition = 0;
                    else bitmapPosition = 2;
                    Brick brick = new Brick(j * brickWidth, brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(i==1&&j==3)brick.setToolNum(10);
                    else if(i==3&&j==2)brick.setToolNum(7);
                    else if(i==4&&j==1||i==7&&j==0)brick.setToolNum(1);
                    else if(i==5&&j==2)brick.setToolNum(3);
                    else if(i==7&&j==3)brick.setToolNum(0);
                    else if(i==9&&j==1)brick.setToolNum(4);
                    else if(i==10&&j==2||i==14&&j==2)brick.setToolNum(1);
                    else if(i==12&&j==3)brick.setToolNum(9);
                    else if(i==13&&j==0)brick.setToolNum(10);
                    else if(i==15&&j==1)brick.setToolNum(3);
                    brickList.add(brick);
                }
                for (int j = 5; j < 9; j++) {
                    if (i < 8) bitmapPosition = 4;
                    else bitmapPosition = 5;
                    Brick brick = new Brick(j * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
                    if(i==1&&j==5||i==2&&j==7)brick.setToolNum(1);
                    else if(i==1&&j==8)brick.setToolNum(2);
                    else if(i==4&&j==6)brick.setToolNum(6);
                    else if(i==6&&j==5)brick.setToolNum(8);
                    else if(i==7&&j==7)brick.setToolNum(7);
                    else if(i==9&&j==7||i==15&&j==6)brick.setToolNum(1);
                    else if(i==10&&j==5)brick.setToolNum(11);
                    else if(i==11&&j==6)brick.setToolNum(0);
                    else if(i==12&&j==8)brick.setToolNum(8);
                    else if(i==14&&j==7)brick.setToolNum(5);
                    brickList.add(brick);
                }
                bricks9.put(brickHeight * i + stop.getHeight(), brickList);
            }
        }
        bricks10=new HashMap<Float, List<Brick>>();
        x=brickWidth/2;
        for(int i=0;i<1;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<9;j++){
                Brick brick = new Brick(j * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
                if(j==3)brick.setToolNum(3);
                else if(j==7)brick.setToolNum(1);
                brickList.add(brick);
            }
            bricks10.put(brickHeight * i + stop.getHeight(), brickList);
        }
        for(int i=1;i<2;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            Brick brick1 = new Brick(0 * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
            brick1.setToolNum(9);
            Brick brick2 = new Brick(8 * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
            brickList.add(brick1);
            brickList.add(brick2);
            bricks10.put(brickHeight * i + stop.getHeight(), brickList);
        }
        for(int i=2;i<13;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            Brick brick1 = new Brick(0 * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
            if(i==5)brick1.setToolNum(1);
            else if(i==10)brick1.setToolNum(8);
            Brick brick2 = new Brick(8 * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
            if(i==5)brick2.setToolNum(7);
            else if(i==9)brick2.setToolNum(1);
            brickList.add(brick1);
            brickList.add(brick2);
            lie=8-i>0?8-i:i-6;
            int m=i>7?13-i:i-1;
            bitmapPosition=4;
            for(int j=0;j<lie;j++){
                Brick brick = new Brick((j+1) * brickWidth +m*x, brickHeight * i + stop.getHeight(), bitmapPosition);
                if(i==2&&j==1||i==12&&j==2)brick.setToolNum(3);
                else if(i==2&&j==5)brick.setToolNum(4);
                else if(i==3&&j==3)brick.setToolNum(11);
                else if(i==4&&j==0)brick.setToolNum(7);
                else if(i==5&&j==2)brick.setToolNum(9);
                else if(i==7&&j==0)brick.setToolNum(0);
                else if(i==9&&j==0)brick.setToolNum(5);
                else if(i==10&&j==2)brick.setToolNum(10);
                else if(i==11&&j==4)brick.setToolNum(1);
                else if(i==12&&j==2)brick.setToolNum(3);
                brickList.add(brick);
            }
            bricks10.put(brickHeight * i + stop.getHeight(), brickList);
        }
        for(int i=13;i<14;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            Brick brick1 = new Brick(0 * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
            Brick brick2 = new Brick(8 * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
            brickList.add(brick1);
            brickList.add(brick2);
            bricks10.put(brickHeight * i + stop.getHeight(), brickList);
        }
        for(int i=14;i<15;i++){
            List<Brick> brickList=new ArrayList<Brick>();
            bitmapPosition=0;
            for(int j=0;j<9;j++){
                Brick brick = new Brick(j * brickWidth , brickHeight * i + stop.getHeight(), bitmapPosition);
                if(j==3)brick.setToolNum(1);
                brickList.add(brick);
            }
            bricks10.put(brickHeight * i + stop.getHeight(), brickList);
        }
    }
    public GameView(Context context) {
        super(context);
        setFocusable(true);
        SharedPreferences sp = context.getSharedPreferences("username", Context.MODE_WORLD_READABLE);
        //.getString("savename","没有保存数据")第一个参数为文件内的name，方法的第二个参数为缺省值，如果SharedPreferences没有该参数，将返回缺省值
        moneyNum=sp.getInt("money",150);
        presentBallId=sp.getInt("presentball",R.mipmap.iconball);
        guanqiaNum=1;
        gameover_dialog=new AlertDialog.Builder(context,R.style.alert_dialog).create();
        View gameover_view= LayoutInflater.from(context).inflate(R.layout.over_dialog,null);
        gameover_dialog.setView(gameover_view);
        gameover_dialog.setCancelable(false);
        gameover_dialog.setOwnerActivity((Activity)context);

        victory_dialog=new AlertDialog.Builder(context,R.style.alert_dialog).create();
        View victory_view=LayoutInflater.from(context).inflate(R.layout.victory_dialog,null);
        victory_dialog.setView(victory_view);
        victory_dialog.setCancelable(false);
        victory_dialog.setOwnerActivity((Activity)context);
        initialize(context);
        score=0;
        lives=4;

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
//        背景颜色
        canvas.drawRGB(245,245,245);
        drawBrick(bricks,canvas);
        for(int i=0;i<gunList.size();i++){
            Gun gun=gunList.get(i);
            canvas.drawBitmap(gun.getGun(),gun.getGunX(),gun.getGunY(),new Paint());
        }
        for(int i=0;i<daodanList.size();i++){
            Daodan daodan=daodanList.get(i);
            canvas.drawBitmap(daodan.getDaodan(),daodan.getDaodanX(),daodan.getDaodanY(),new Paint());
        }
        for(int i=0;i<fireballList.size();i++){
            Fireball fireball=fireballList.get(i);
            canvas.drawBitmap(fireball.getFireball(),fireball.getFireballX(),fireball.getFireballY(),new Paint());
        }
//        用来显示分数的背景
        paint.setColor(Color.rgb(251,114,153));
        canvas.drawRect(stop.getWidth(),0,screenWidth-stop.getWidth(),stop.getHeight(),paint);
//        分数
        Paint textPaint = new Paint();
        // 设置颜色
        textPaint.setColor(Color.WHITE);
        // 设置样式
        textPaint.setStyle(Paint.Style.FILL);
        String familyName="sans";
        Typeface font=Typeface.create(familyName,Typeface.NORMAL);
        textPaint.setTypeface(font);
        textPaint.setTextSize((float)(stop.getHeight()*0.4));
        String string="分数："+score;
        canvas.drawText(string,(float) (stop.getWidth()*1.5),(float)(stop.getHeight()*0.7),textPaint);
        string="生命："+lives;
        canvas.drawText(string,screenWidth-set.getWidth()*3.0f,stop.getHeight()*0.7f,textPaint);
        textPaint.setColor(Color.RED);
        string="第十五届齐鲁软件大赛参赛作品";
        canvas.drawText(string,screenWidth*0.1f,screenHeight*0.95f,textPaint);
        for(int i=0;i<toolList.size();i++){
            Tool tool=toolList.get(i);
            canvas.drawBitmap(tool.bitmap,tool.toolX,tool.toolY,new Paint());
        }
        canvas.drawBitmap(stop, 0, 0, new Paint());
        canvas.drawBitmap(set,screenWidth-set.getWidth(),0,new Paint());
        canvas.drawBitmap(money,0,screenHeight-money.getHeight(),new Paint());
        textPaint.setColor(Color.rgb(255,215,0));
        string=""+moneyNum;
        canvas.drawText(string,money.getWidth()*1.2f,screenHeight-money.getHeight()*0.3f,textPaint);
        if(hasFireball){
            Bitmap drawfire=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.fireball1_small);
            canvas.drawBitmap(drawfire,screenWidth-drawfire.getWidth()*2,screenHeight-drawfire.getHeight(),new Paint());
            string=""+fireballNum;
            canvas.drawText(string,screenWidth-drawfire.getWidth()*0.8f,screenHeight-drawfire.getHeight()*0.3f,textPaint);
        }
        if(hasDaodan){
            Bitmap drawDaodan=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.daodan_fall);
            canvas.drawBitmap(drawDaodan,screenWidth-drawDaodan.getWidth()*2,screenHeight-drawDaodan.getHeight(),new Paint());
            string=""+daodanNum;
            canvas.drawText(string,screenWidth-drawDaodan.getWidth()*0.8f,screenHeight-drawDaodan.getHeight()*0.3f,textPaint);
        }
        if(hasGun){
            Bitmap drawGun=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.zidan);
            canvas.drawBitmap(drawGun,screenWidth-drawGun.getWidth()*2,screenHeight-drawGun.getHeight(),new Paint());
            string=""+gunNum;
            canvas.drawText(string,screenWidth-drawGun.getWidth()*0.8f,screenHeight-drawGun.getHeight()*0.3f,textPaint);
        }
        paint.setColor(Color.rgb(255, 102, 11));

        RectF rectF = new RectF(racket.getRacketX(), racket.getRacketY(), racket.getRacketX()
                + racket.getRacketWidth()*racket.getWidthNum(), racket.getRacketY() + racket.getRacketHeight());
        canvas.drawRoundRect(rectF, 5, 5, paint);
        paint.setColor(Color.rgb(237,28,36));
        for(int i=0;i<ballList.size();i++) {
//            canvas.drawCircle(ballList.get(i).getBallX(), ballList.get(i).getBallY(),
//                    ballList.get(i).getBallSize()*ballList.get(i).getSizeNum(), paint);
            Bitmap ballBitmap=ballList.get(i).getBitmap();
            RectF dst = new RectF(ballList.get(i).getBallX()-ballList.get(i).getBallSize()*ballList.get(i).getSizeNum(),
                    ballList.get(i).getBallY()-ballList.get(i).getBallSize()*ballList.get(i).getSizeNum(),
                    ballList.get(i).getBallX()+ballList.get(i).getBallSize()*ballList.get(i).getSizeNum(),
                    ballList.get(i).getBallY()+ballList.get(i).getBallSize()*ballList.get(i).getSizeNum());
            Rect src = new Rect(0, 0, ballBitmap.getWidth(), ballBitmap.getHeight());
            canvas.drawBitmap(ballBitmap, src, dst, null);
        }
//           画一个橘色圆角矩形球拍

        if (isGameOver&&lives==0) {
            isGameOver = false;
            stopTimer();
            if(!gameover_dialog.isShowing()) {
                gameover_dialog.show();
                if (MainActivity.allowMusic) myMediaPlayer.stopBgm();
                if (MainActivity.allowSound) {
                    myMediaPlayer.startDefeatSound(context1);
                }
                Button confirmButton = (Button) gameover_dialog.findViewById(R.id.confirm_button);
                final Button cancelButton = (Button) gameover_dialog.findViewById(R.id.cancel_button);
                confirmButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameover_dialog.dismiss();
                        if (MainActivity.allowSound) myMediaPlayer.stopDefeatSound();
                        score=0;
                        lives=4;
                        guanqiaNum=1;
                        initialize(context1);
                        invalidate();
                        if (MainActivity.allowMusic) myMediaPlayer.startBgm(context1);
                        startTimer(0);
                    }
                });
                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameover_dialog.dismiss();
                        if (MainActivity.allowSound) myMediaPlayer.stopDefeatSound();
                        Intent intent = new Intent(context1, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context1.startActivity(intent);
                        gameover_dialog.getOwnerActivity().finish();
                    }
                });
            }
        }else if(isGameOver&&lives>0){
            lives--;
            isGameOver=false;
            stopTimer();
            racket.setWidthNum(1.0f);
            float ballSize = brickHeight / 3;
            float ballX = racket.getRacketX() + racket.getRacketWidth()*racket.getWidthNum() / 2;
            float ballY=racket.getRacketY()-ballSize;
            Ball ball=new Ball(ballX,ballY,ballSize,0,0);
            Bitmap ballBitmap=BitmapFactory.decodeResource(context1.getResources(),presentBallId);
            ball.setBitmap(ballBitmap);
            allowClickBall=true;
            ballList.clear();
            ballList.add(ball);
            startTimer(0);
        }
    }

    public void drawBrick(Map<Float,List<Brick>> bricks, Canvas canvas) {
        int num=0;
        for (Float key : bricks.keySet()) {
            List<Brick> list = bricks.get(key);
            for (int i = 0; i < list.size(); i++) {
                Brick brick = list.get(i);
                Bitmap bp = BitmapFactory.decodeResource(context1.getResources(), brickBitmaps[brick.bitmapPosition]);
                RectF dst = new RectF(brick.brickX, brick.brickY, brick.brickX + brickWidth, brick.brickY + brickHeight);
                Rect src = new Rect(0, 0, bp.getWidth(), bp.getHeight());
                canvas.drawBitmap(bp, src, dst, null);
                num++;
            }
        }
        if(num==0){
            stopTimer();
            if(!victory_dialog.isShowing()) {
                victory_dialog.show();
                moneyNum += 20;
                if (MainActivity.allowMusic) myMediaPlayer.stopBgm();
                if (MainActivity.allowSound) {
                    myMediaPlayer.startVictorySound(context1);
                }
                final Button confirmButton = (Button) victory_dialog.findViewById(R.id.confirm_button);
                final Button cancelButton = (Button) victory_dialog.findViewById(R.id.cancel_button);
                TextView title = (TextView) victory_dialog.findViewById(R.id.title_text);
                if (guanqiaNum == 10) {
                    title.setText("恭喜通关！");
                    confirmButton.setText("重新挑战");
                    cancelButton.setText("返回菜单");
                }

                confirmButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        victory_dialog.dismiss();
                        if (MainActivity.allowSound) myMediaPlayer.stopVictorySound();
                        guanqiaNum++;
                        initialize(context1);
                        if (MainActivity.allowMusic) myMediaPlayer.startBgm(context1);
                        startTimer(0);
                    }
                });
                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        victory_dialog.dismiss();
                        if (MainActivity.allowSound) myMediaPlayer.stopVictorySound();
                        Intent intent = new Intent(context1, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context1.startActivity(intent);
                        victory_dialog.getOwnerActivity().finish();
                    }
                });
            }
        }
    }

    /*
    游戏开始
     */
    public void startTimer(int delay) {
        if (GameActivity.timer == null) {
            GameActivity.timer = new Timer();
        }
        if (GameActivity.timerTask == null) {
            GameActivity.timerTask = new MyTimerTask();
        }
        GameActivity.timer.schedule(GameActivity.timerTask, delay, 30);
    }

    /*
    游戏暂停
     */
    public void stopTimer() {
        if (GameActivity.timer != null) {
            GameActivity.timer.cancel();
            GameActivity.timer = null;
        }
        if (GameActivity.timerTask != null) {
            GameActivity.timerTask.cancel();
            GameActivity.timerTask = null;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x, y;
        setClickable(true);
        whichPressed=0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(GameActivity.timerTask!=null)
                GameActivity.timerTask.startJishi();
                x = (float) event.getX();
                y = (float) event.getY();
                beginX = x;
                beginY = y;
                lastX = x;
                lastY = y;
                if (beginY <= stop.getHeight() && beginX<=stop.getWidth()) {
                    stop=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.stop_press);
                }else if (beginY <= set.getHeight() && beginX>=screenWidth-set.getWidth()) {
                    set=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.help_press);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                    if ( beginY > stop.getHeight()) {
                    racket.racketX = racket.racketX + (float) event.getX() - lastX;
                    if (racket.racketX < 0) racket.racketX = 0;
                    if (racket.racketX > screenWidth - racket.racketWidth*racket.getWidthNum())
                        racket.racketX = screenWidth - racket.racketWidth*racket.getWidthNum();
                    if(allowClickBall){
                        for(int i=0;i<ballList.size();i++){
                            ballList.get(i).setBallX(racket.racketX+racket.racketWidth*racket.getWidthNum()/2);
                        }
                    }
                    lastX = (float) event.getX();
                }
                if (event.getY()<= stop.getHeight() && event.getX()<=stop.getWidth()) {
                    stop=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.stop_press);
                }else{
                    stop=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.stop);
                }
                if (event.getY()<= set.getHeight() && event.getX()>=screenWidth-set.getWidth()) {
                    set=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.help_press);
                }else{
                    set=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.help);
                }
                break;
            case MotionEvent.ACTION_UP:
                int timeNum=200;
                if(GameActivity.timerTask!=null) {
                    GameActivity.timerTask.stopJishi();
                    timeNum = GameActivity.timerTask.getTimeNum() * 30;
                }
                x = (float) event.getX();
                y = (float) event.getY();
                if (beginY <= stop.getHeight() && beginX<=stop.getWidth() && y <= stop.getHeight() && x<=stop.getWidth()) {
                    stop=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.stop);
                    whichPressed=1;
                } else {
                    if (beginY <= set.getHeight() && beginX>=screenWidth-set.getWidth() && y <= set.getHeight() && x>=screenWidth-set.getWidth()) {
                        set=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.help);
                        whichPressed=2;
                    } else {
                        set=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.help);
                        if(timeNum<=100&&allowClickBall && beginY>set.getHeight() && y>set.getHeight() ){
                            whichPressed=3;
                        }else {
                            if(timeNum<=100&&hasGun && gunNum!=0){
                                Gun gun=new Gun(racket.racketX+racket.racketWidth*racket.widthNum/2,racket.racketY);
                                gunNum--;
                                gunList.add(gun);
                                if(MainActivity.allowSound){
                                    myMediaPlayer.startZidanSound(context1);
                                }
                                if(gunNum==0) hasGun=false;
                            }else if(timeNum<=100&&hasDaodan && daodanNum!=0){
                                Daodan daodan=new Daodan(racket.racketX+racket.racketWidth*racket.widthNum/2,racket.racketY);
                                daodanNum--;
                                daodanList.add(daodan);
                                if(MainActivity.allowSound){
                                    myMediaPlayer.startZidanSound(context1);
                                }
                                if(daodanNum==0) hasDaodan=false;
                            }else if(timeNum<=100&&hasFireball &&fireballNum!=0){
                                Fireball fireball=new Fireball(racket.racketX+racket.racketWidth*racket.widthNum/2,racket.racketY);
                                fireballNum--;
                                fireballList.add(fireball);
                                if(MainActivity.allowSound){
                                    myMediaPlayer.startZidanSound(context1);
                                }
                                if(fireballNum==0) hasFireball=false;
                            }else {
                                setClickable(false);
                            }
                        }
                    }
                    stop=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.stop);
                }
                System.out.println("---"+"whichPressed="+whichPressed);
                break;
        }
        return super.onTouchEvent(event);
    }


    public class Brick {
        private float brickX, brickY;
        private int bitmapPosition;
        private int toolNum;

        public int getToolNum() {
            return toolNum;
        }

        public void setToolNum(int toolNum) {
            this.toolNum = toolNum;
        }

        public Brick(float x, float y, int bitmapPosition) {
            brickX = x;
            brickY = y;
            this.bitmapPosition = bitmapPosition;
            toolNum=-1;
        }

        public float getBrickX() {
            return brickX;
        }

        public void setBrickX(float brickX) {
            this.brickX = brickX;
        }

        public float getBrickY() {
            return brickY;
        }

        public void setBrickY(float brickY) {
            this.brickY = brickY;
        }

    }

    public class Tool{
        private int bitmapId;
        private Bitmap bitmap;
        private float toolX,toolY;
        public Tool(Bitmap bitmap,int id,float x,float y){
            this.bitmap=bitmap;
            toolX=x;
            toolY=y;
            bitmapId=id;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public float getToolX() {
            return toolX;
        }

        public void setToolX(float toolX) {
            this.toolX = toolX;
        }

        public float getToolY() {
            return toolY;
        }

        public void setToolY(float toolY) {
            this.toolY = toolY;
        }

        public int getBitmapId() {
            return bitmapId;
        }

        public void setBitmapId(int bitmapId) {
            this.bitmapId = bitmapId;
        }
    }

    public Tool creatTool(Bitmap bitmap,int id,float x,float y){
        Tool tool=new Tool(bitmap,id,x,y);
        return tool;
    }

    public class Ball {
        //球心坐标，半径
        private float ballX;

        public float getBallX() {
            return ballX;
        }

        private float ballY;

        public void setBallX(float ballX) {
            this.ballX = ballX;
        }

        public void setBallY(float ballY) {
            this.ballY = ballY;
        }

        public float getBallSize() {
            return ballSize;
        }

        public void setBallSize(float ballSize) {
            this.ballSize = ballSize;
        }

        private float ballSize,sizeNum;
        private float xSpeed,ySpeed,speedNum;
        private boolean isBig,isFast,isSmall,isSlow;
        private Bitmap bitmap;

        public boolean isBig() {
            return isBig;
        }

        public void setBig(boolean big) {
            isBig = big;
        }

        public boolean isFast() {
            return isFast;
        }

        public void setFast(boolean fast) {
            isFast = fast;
        }

        public boolean isSmall() {
            return isSmall;
        }

        public void setSmall(boolean small) {
            isSmall = small;
        }

        public boolean isSlow() {
            return isSlow;
        }

        public void setSlow(boolean slow) {
            isSlow = slow;
        }

        public float getSpeedNum() {
            return speedNum;
        }

        public void setSpeedNum(float speedNum) {
            this.speedNum = speedNum;
        }

        public float getSizeNum() {
            return sizeNum;
        }

        public void setSizeNum(float sizeNum) {
            this.sizeNum = sizeNum;
        }

        public Ball(float x, float y, float size, float xS, float yS){
            ballX=x;
            ballY=y;
            ballSize=size;
            xSpeed=xS;
            ySpeed=yS;
            isBig=false;
            isFast=false;
            isSmall=false;
            isSlow=false;
            speedNum=1.0f;
            sizeNum=1.0f;
            bitmap=BitmapFactory.decodeResource(context1.getResources(), R.mipmap.iconball);
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public float getBallY() {
            return ballY;
        }

        public float getxSpeed() {
            return xSpeed;
        }

        public void setxSpeed(float xSpeed) {
            this.xSpeed = xSpeed;
        }

        public float getySpeed() {
            return ySpeed;
        }

        public void setySpeed(float ySpeed) {
            this.ySpeed = ySpeed;
        }
    }

    public Ball creatBall(float x,float y,float size,float xS,float yS){
        Ball ball=new Ball(x,y,size,xS,yS);
        return ball;
    }

    public class Racket {
        //球拍坐标，宽高
        private float racketX,racketY,racketWidth,racketHeight;
        private float widthNum;
        boolean isShort,isLong;

        public boolean isShort() {
            return isShort;
        }

        public void setShort(boolean aShort) {
            isShort = aShort;
        }

        public boolean isLong() {
            return isLong;
        }

        public void setLong(boolean aLong) {
            isLong = aLong;
        }

        public Racket(float x, float y, float width, float height){
            racketX=x;
            racketY=y;
            racketWidth=width;
            racketHeight=height;
            widthNum=1.0f;
            isShort=false;
            isLong=false;

        }

        public float getRacketX() {
            return racketX;
        }

        public void setRacketX(float racketX) {
            this.racketX = racketX;
        }

        public float getRacketY() {
            return racketY;
        }

        public void setRacketY(float racketY) {
            this.racketY = racketY;
        }

        public float getRacketWidth() {
            return racketWidth;
        }

        public void setRacketWidth(float racketWidth) {
            this.racketWidth = racketWidth;
        }

        public float getRacketHeight() {
            return racketHeight;
        }

        public void setRacketHeight(float racketHeight) {
            this.racketHeight = racketHeight;
        }

        public float getWidthNum() {
            return widthNum;
        }

        public void setWidthNum(float widthNum) {
            this.widthNum = widthNum;
        }
    }

    public class Gun{
        private Bitmap gun;
        private float gunX,gunY;
        private float upSpeed;

        public Bitmap getGun() {
            return gun;
        }

        public void setGun(Bitmap gun) {
            this.gun = gun;
        }

        public float getGunX() {
            return gunX;
        }

        public void setGunX(float gunX) {
            this.gunX = gunX;
        }

        public float getGunY() {
            return gunY;
        }

        public void setGunY(float gunY) {
            this.gunY = gunY;
        }

        public float getUpSpeed() {
            return upSpeed;
        }

        public void setUpSpeed(float upSpeed) {
            this.upSpeed = upSpeed;
        }

        public Gun(float x, float y){
            gunX=x;
            gunY=y;
            gun=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.zidan_single);
            upSpeed=10;
        }
    }

    public class Daodan{
        private Bitmap daodan;
        private float daodanX,daodanY;
        private float upSpeed;

        public Bitmap getDaodan() {
            return daodan;
        }

        public void setDaodan(Bitmap daodan) {
            this.daodan = daodan;
        }

        public float getDaodanX() {
            return daodanX;
        }

        public void setDaodanX(float daodanX) {
            this.daodanX = daodanX;
        }

        public float getDaodanY() {
            return daodanY;
        }

        public void setDaodanY(float daodanY) {
            this.daodanY = daodanY;
        }

        public float getUpSpeed() {
            return upSpeed;
        }

        public void setUpSpeed(float upSpeed) {
            this.upSpeed = upSpeed;
        }

        public Daodan(float x, float y){
            daodanX=x;
            daodanY=y;
            daodan=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.daodan);
            upSpeed=10;
        }
    }

    public class Fireball{
        private Bitmap fireball;
        private float fireballX,fireballY;
        private float upSpeed;

        public Fireball(float x, float y){
            fireballX=x;
            fireballY=y;
            fireball=BitmapFactory.decodeResource(context1.getResources(),R.mipmap.fireball1_small);
            upSpeed=10;
        }

        public Bitmap getFireball() {
            return fireball;
        }

        public void setFireball(Bitmap fireball) {
            this.fireball = fireball;
        }

        public float getFireballX() {
            return fireballX;
        }

        public void setFireballX(float fireballX) {
            this.fireballX = fireballX;
        }

        public float getFireballY() {
            return fireballY;
        }

        public void setFireballY(float fireballY) {
            this.fireballY = fireballY;
        }

        public float getUpSpeed() {
            return upSpeed;
        }

        public void setUpSpeed(float upSpeed) {
            this.upSpeed = upSpeed;
        }
    }

}
