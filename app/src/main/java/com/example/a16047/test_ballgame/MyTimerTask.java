package com.example.a16047.test_ballgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.WindowManager;

import java.util.List;
import java.util.TimerTask;

/*
    自定义时间任务列表
     */
public class MyTimerTask extends TimerTask {
    private GameView gameView=GameActivity.gameView;
    private MyMediaPlayer myMediaPlayer=MainActivity.myMediaPlayer;
    private float xSpeed=GameActivity.xSpeed,ySpeed=GameActivity.ySpeed;
    private float speed=GameActivity.speed;
    private float fallSpeed=GameActivity.fallSpeed;
    private int sum=1;
    private boolean isfireball=false;
    private boolean start_jishi=false;
    private int timeNum=0;
    @Override
    public void run() {
        isfireball=false;
        if(start_jishi)timeNum++;
        else timeNum=0;
        for (int i = 0; i < gameView.toolList.size(); ) {
            gameView.toolList.get(i).setToolY(gameView.toolList.get(i).getToolY() + fallSpeed);
            float x = gameView.toolList.get(i).getToolX();
            float y = gameView.toolList.get(i).getToolY();
            float height = gameView.toolList.get(i).getBitmap().getHeight();
            float width = gameView.toolList.get(i).getBitmap().getWidth();
            if (y >= gameView.racket.getRacketY() && y <= gameView.racket.getRacketY() + gameView.racket.getRacketHeight()
                    && x <= gameView.racket.getRacketX() + gameView.racket.getRacketWidth()*gameView.racket.getWidthNum()
                    && x + width >= gameView.racket.getRacketX()) {
                int num = gameView.toolList.get(i).getBitmapId();
                switch (num) {
                    case 0:
                        int size=gameView.ballList.size();
                        for(int j=0;j<size;j++){
                            float ballX=gameView.ballList.get(j).getBallX();
                            float ballY=gameView.ballList.get(j).getBallY();
                            float ballSize=gameView.ballList.get(j).getBallSize();
                            float sizeNum=gameView.ballList.get(j).getSizeNum();
                            float xSpeed=gameView.ballList.get(j).getxSpeed();
                            float ySpeed=gameView.ballList.get(j).getySpeed();
                            float speedNum=gameView.ballList.get(j).getSpeedNum();
                            Bitmap bitmap=gameView.ballList.get(j).getBitmap();
                            double bili=xSpeed/GameActivity.speed;
                            double jiao=Math.asin(bili);
                            double sin1=Math.sin(jiao+Math.PI/6);
                            double cos1=Math.cos(jiao+Math.PI/6);
                            cos1=cos1>0.1?cos1:0.1;
                            double sin2=Math.sin(jiao-Math.PI/6);
                            double cos2=Math.cos(jiao-Math.PI/6);
                            cos2=cos2>0.1?cos2:0.1;
                            int zheng=(ySpeed>0)?1:-1;
                            GameView.Ball ball1=gameView.creatBall(ballX,ballY,ballSize,
                                    GameActivity.speed*(float)sin1,zheng*GameActivity.speed*(float)cos1);
                            ball1.setSizeNum(sizeNum);
                            ball1.setSpeedNum(speedNum);
                            ball1.setBig(gameView.ballList.get(j).isBig());
                            ball1.setSmall(gameView.ballList.get(j).isSmall());
                            ball1.setFast(gameView.ballList.get(j).isFast());
                            ball1.setSlow(gameView.ballList.get(j).isSlow());
                            ball1.setBitmap(bitmap);
                            gameView.ballList.add(ball1);
                            GameView.Ball ball2=gameView.creatBall(ballX,ballY,ballSize,
                                    GameActivity.speed*(float)sin2,zheng*GameActivity.speed*(float)cos2);
                            ball2.setSizeNum(sizeNum);
                            ball2.setSpeedNum(speedNum);
                            ball2.setBig(gameView.ballList.get(j).isBig());
                            ball2.setSmall(gameView.ballList.get(j).isSmall());
                            ball2.setFast(gameView.ballList.get(j).isFast());
                            ball2.setSlow(gameView.ballList.get(j).isSlow());
                            ball2.setBitmap(bitmap);
                            gameView.ballList.add(ball2);
                        }
                        if(MainActivity.allowSound){
                            myMediaPlayer.startMagicSound(gameView.getContext());
                        }
                        break;
                    case 1:
                        gameView.moneyNum++;
                        if(MainActivity.allowSound){
                            myMediaPlayer.startCoinsSound(gameView.getContext());
                        }
                        break;
                    case 2:
                        gameView.lives++;
                        if(MainActivity.allowSound){
                            myMediaPlayer.startLiveupSound(gameView.getContext());
                        }
                        break;
                    case 3:
                        gameView.hasGun=true;
                        gameView.gunNum+=3;
                        gameView.hasDaodan=false;
                        gameView.daodanNum=0;
                        gameView.hasFireball=false;
                        gameView.fireballNum=0;
                        if(MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 4:
                        gameView.hasGun=false;
                        gameView.gunNum=0;
                        gameView.hasDaodan=true;
                        gameView.daodanNum+=1;
                        gameView.hasFireball=false;
                        gameView.fireballNum=0;

                        if(MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 5:
//                        for(int j=0;j<gameView.ballList.size();j++){
//                            Bitmap ballBitmap=BitmapFactory.decodeResource(gameView.getResources(),R.mipmap.fireball1_small);
//                            gameView.ballList.get(j).setBitmap(ballBitmap);
//                        }
                        gameView.hasGun=false;
                        gameView.gunNum=0;
                        gameView.hasDaodan=false;
                        gameView.daodanNum=0;
                        gameView.hasFireball=true;
                        gameView.fireballNum+=1;
                        if(MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 6:
                        boolean bian6=false;
                        for(int j=0;j<gameView.ballList.size();j++){
                            if(!gameView.ballList.get(j).isBig()) {
                                gameView.ballList.get(j).setSizeNum(1.5f);
                                gameView.ballList.get(j).setBig(true);
                                gameView.ballList.get(j).setSmall(false);
                                bian6=true;
                            }
                        }
                        if(bian6 && MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 7:
                        boolean bian7=false;
                        for(int j=0;j<gameView.ballList.size();j++){
                            if(!gameView.ballList.get(j).isFast()) {
                                gameView.ballList.get(j).setSpeedNum(1.4f);
                                gameView.ballList.get(j).setFast(true);
                                gameView.ballList.get(j).setSlow(false);
                                bian7=true;
                            }
                        }
                        if(bian7 && MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 8:
                        boolean bian8=false;
                        for(int j=0;j<gameView.ballList.size();j++){
                            if(!gameView.ballList.get(j).isSmall()) {
                                gameView.ballList.get(j).setSizeNum(0.7f);
                                gameView.ballList.get(j).setSmall(true);
                                gameView.ballList.get(j).setBig(false);
                                bian8=true;
                            }
                        }
                        if(bian8 && MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 9:
                        boolean bian9=false;
                        if(!gameView.racket.isShort()){
                            gameView.racket.setWidthNum(0.7f);
                            gameView.racket.setShort(true);
                            gameView.racket.setLong(false);
                            bian9=true;
                        }
                        if(bian9 && MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 10:
                        boolean bian10=false;
                        if(!gameView.racket.isLong()){
                            gameView.racket.setWidthNum(1.3f);
                            gameView.racket.setShort(false);
                            gameView.racket.setLong(true);
                            bian10=true;
                        }
                        if(bian10 && MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                    case 11:
                        boolean bian11=false;
                        for(int j=0;j<gameView.ballList.size();j++){
                            if(!gameView.ballList.get(j).isSlow()) {
                                gameView.ballList.get(j).setSpeedNum(0.7f);
                                gameView.ballList.get(j).setSlow(true);
                                gameView.ballList.get(j).setFast(false);
                                bian11=true;
                            }
                        }
                        if(bian11 && MainActivity.allowSound){
                            myMediaPlayer.startChangeSound(gameView.getContext());
                        }
                        break;
                }
                gameView.toolList.remove(i);
            } else if (y >= GameActivity.screenHeight) {
                gameView.toolList.remove(i);
            } else {
                i++;
            }
        }
        for(int i=0;i<gameView.gunList.size();){
            float gunX=gameView.gunList.get(i).getGunX();
            float gunY=gameView.gunList.get(i).getGunY();
            float gunWidth=gameView.gunList.get(i).getGun().getWidth();
            float gunHeight=gameView.gunList.get(i).getGun().getHeight();
            float gunSpeed=gameView.gunList.get(i).getUpSpeed();
            gameView.gunList.get(i).setGunY(gunY-gunSpeed);
            if(isKnock(gunX,gunY-gunSpeed)||isKnock(gunX+gunWidth,gunY-gunSpeed)){
                gameView.gunList.remove(i);
                if (MainActivity.allowSound) {
                    myMediaPlayer.startKnockBrickSound(gameView.getContext());
                }
            }else if(gunY-gunSpeed<gameView.stop.getHeight()-gunHeight){
                gameView.gunList.remove(i);
            }else{
                i++;
            }
        }
        for (int i=0;i<gameView.daodanList.size();i++){
            float daodanX=gameView.daodanList.get(i).getDaodanX();
            float daodanY=gameView.daodanList.get(i).getDaodanY();
            float daodanWidth=gameView.daodanList.get(i).getDaodan().getWidth();
            float daodanHeight=gameView.daodanList.get(i).getDaodan().getHeight();
            float daodanSpeed=gameView.daodanList.get(i).getUpSpeed();
            gameView.daodanList.get(i).setDaodanY(daodanY-daodanSpeed);
            if(isKnock(daodanX,daodanY-daodanSpeed)||isKnock(daodanX+daodanWidth,daodanY-daodanSpeed)){
                if (MainActivity.allowSound) {
                    myMediaPlayer.startKnockBrickSound(gameView.getContext());
                }
            }else if(daodanY-daodanSpeed<gameView.stop.getHeight()-daodanHeight){
                gameView.daodanList.remove(i);
            }else{
                i++;
            }
        }
        for (int i=0;i<gameView.fireballList.size();i++){
            float fireballX=gameView.fireballList.get(i).getFireballX();
            float fireballY=gameView.fireballList.get(i).getFireballY();
            float fireballWidth=gameView.fireballList.get(i).getFireball().getWidth();
            float fireballHeight=gameView.fireballList.get(i).getFireball().getHeight();
            float fireballSpeed=gameView.fireballList.get(i).getUpSpeed();
            gameView.fireballList.get(i).setFireballY(fireballY-fireballSpeed);
            isfireball=true;
            if(isKnock(fireballX,fireballY-fireballSpeed)||
                    isKnock(fireballX+fireballWidth,fireballY-fireballSpeed)){
                gameView.fireballList.remove(i);
            }else if(fireballY-fireballSpeed<gameView.stop.getHeight()-fireballHeight){
                gameView.fireballList.remove(i);
            }else{
                i++;
            }
            isfireball=false;
        }
        int count=0;
        for (int i = 0; i < gameView.ballList.size(); i++) {
            float ballX=gameView.ballList.get(i).getBallX();
            float ballY=gameView.ballList.get(i).getBallY();
            float ballSize=gameView.ballList.get(i).getBallSize();
            float xSpeed=gameView.ballList.get(i).getxSpeed();
            float ySpeed=gameView.ballList.get(i).getySpeed();
            float sizeNum= gameView.ballList.get(i).getSizeNum();
            float speedNum=gameView.ballList.get(i).getSpeedNum();
            gameView.ballList.get(i).setBallX(ballX+xSpeed*speedNum);
            gameView.ballList.get(i).setBallY(ballY+ySpeed*speedNum);
            ballX=gameView.ballList.get(i).getBallX();
            ballY=gameView.ballList.get(i).getBallY();
            if (ballX - ballSize*sizeNum < 0) { //左碰壁
                gameView.ballList.get(i).setBallX(ballSize*sizeNum);
                gameView.ballList.get(i).setxSpeed(-xSpeed);
                if (MainActivity.allowSound) {
                    myMediaPlayer.startKnockWallSound(gameView.getContext());
                }
            } else if (ballX + ballSize*sizeNum > gameView.saveAreaX) {//右碰壁
                gameView.ballList.get(i).setBallX(gameView.saveAreaX -ballSize*sizeNum);
                gameView.ballList.get(i).setxSpeed(-xSpeed);
                if (MainActivity.allowSound) {
                    myMediaPlayer.startKnockWallSound(gameView.getContext());
                }
            }

            if (ballY - ballSize*sizeNum <= gameView.stop.getHeight()) {//顶部碰壁
                gameView.ballList.get(i).setBallY(gameView.stop.getHeight() + ballSize*sizeNum);
                gameView.ballList.get(i).setySpeed(-ySpeed);
                if (MainActivity.allowSound) {
                    myMediaPlayer.startKnockWallSound(gameView.getContext());
                }
            } else if (ballY - ballSize*sizeNum < gameView.saveAreaY) {//碰砖块
                //                上碰
                if (isKnock(ballX, ballY - ballSize*sizeNum)) {
                    gameView.ballList.get(i).setySpeed(-ySpeed);
                    gameView.score = gameView.score + 50 * sum;
                    sum++;
                }
                //                左碰
                if (isKnock(ballX - ballSize*sizeNum, ballY)) {
                    gameView.ballList.get(i).setxSpeed(-xSpeed);
                    gameView.score = gameView.score + 50 * sum;
                    sum++;
                }
                //                下碰
                if (isKnock(ballX, ballY + ballSize*sizeNum)) {
                    gameView.ballList.get(i).setySpeed(-ySpeed);
                    gameView.score = gameView.score + 50 * sum;
                    sum++;
                }
                //                右碰
                if (isKnock(ballX + ballSize*sizeNum, ballY)) {
                    gameView.ballList.get(i).setxSpeed(-xSpeed);
                    gameView.score = gameView.score + 50 * sum;
                    sum++;
                }
            } else if (ballY + ballSize*sizeNum >= gameView.racket.getRacketY()) {
                if (ballY + ballSize*sizeNum <= gameView.racket.getRacketY() + gameView.racket.getRacketHeight()
                        && ballX <= gameView.racket.getRacketX() + gameView.racket.getRacketWidth()*gameView.racket.getWidthNum()
                        && ballX >= gameView.racket.getRacketX()
                        && ySpeed > 0) {
                    gameView.ballList.get(i).setBallY(gameView.racket.getRacketY() - ballSize*sizeNum);
                    float fall = ballX - gameView.racket.getRacketX() - gameView.racket.getRacketWidth()*gameView.racket.getWidthNum() / 2;
                    float sin = Math.abs(fall) / (1 + gameView.racket.getRacketWidth()*gameView.racket.getWidthNum() / 2);
                    float cos = (float) Math.sqrt(1 - Math.pow(sin, 2));
                    xSpeed = speed * sin;
                    ySpeed = -speed * cos;
                    if (fall < 0) {
                        xSpeed = -xSpeed;
                    }
                    gameView.ballList.get(i).setxSpeed(xSpeed);
                    gameView.ballList.get(i).setySpeed(ySpeed);
                    sum = 1;
                    if (MainActivity.allowSound) {
                        myMediaPlayer.startKnockRacketSound(gameView.getContext());
                    }
                } else if (ballY - ballSize*sizeNum >= GameActivity.screenHeight) {
                    if(gameView.ballList.size()==1) {
                        gameView.isGameOver = true;
                        gameView.hasGun=false;
                        gameView.hasDaodan=false;
                        gameView.hasFireball=false;
                        gameView.fireballNum=0;
                        gameView.daodanNum=0;
                        gameView.gunNum=0;
                    }
//                    gameView.ballList.get(i).setBallY(GameActivity.screenHeight + ballSize*sizeNum);
//                    xSpeed = 0;
//                    ySpeed = 0;
//                    gameView.ballList.get(i).setxSpeed(xSpeed);
//                    gameView.ballList.get(i).setySpeed(ySpeed);
                    gameView.ballList.remove(i);
                    i--;
                    sum = 1;
                }
            }
            GameActivity.xSpeed = xSpeed;
            GameActivity.ySpeed = ySpeed;
            GameActivity.myHandler.sendEmptyMessage(100);
        }
    }
    public boolean isKnock(float pointX, float pointY) {
        boolean is = false;
        boolean delete=false;
        for (Float key:gameView.bricks.keySet()) {
            if(pointY >= key && pointY <= key + gameView.brickHeight) {
                List<GameView.Brick> list=gameView.bricks.get(key);
                for (int i = 0; i < list.size();i++ ) {
                    GameView.Brick brick = list.get(i);
                    if (pointX >= brick.getBrickX() && pointX <= brick.getBrickX() + gameView.brickWidth) {
                        if(brick.getToolNum()!=-1){
                            Bitmap bitmap=BitmapFactory.decodeResource(GameActivity.context.getResources(),gameView.toolBitmaps[brick.getToolNum()]);
                            gameView.toolList.add(gameView.creatTool(bitmap,brick.getToolNum(),brick.getBrickX(),brick.getBrickY()));
                        }
                        if(isfireball) delete=true;
                        list.remove(i);
                        if (MainActivity.allowSound) {
                            myMediaPlayer.startKnockBrickSound(gameView.getContext());
                        }
                        is = true;
                    }
                }
                if(delete){
                    for(int i=0;i<list.size();){
                        GameView.Brick brick = list.get(i);
                        if(brick.getToolNum()!=-1){
                            Bitmap bitmap=BitmapFactory.decodeResource(GameActivity.context.getResources(),gameView.toolBitmaps[brick.getToolNum()]);
                            gameView.toolList.add(gameView.creatTool(bitmap,brick.getToolNum(),brick.getBrickX(),brick.getBrickY()));
                        }
                        list.remove(i);
                        if (MainActivity.allowSound) {
                            myMediaPlayer.startKnockBrickSound(gameView.getContext());
                        }
                    }
                }
            }
        }
        return is;
    }
    public void startJishi(){
        start_jishi=true;
    }
    public void stopJishi(){
        start_jishi=false;
    }
    public int getTimeNum(){
        return timeNum;
    }
}