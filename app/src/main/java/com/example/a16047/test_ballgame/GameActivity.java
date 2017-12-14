package com.example.a16047.test_ballgame;

/**
 * Created by 16047 on 2017/8/23.
 */
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
public class GameActivity extends AppCompatActivity{
    //x轴球速，y轴
    static float xSpeed, ySpeed, speed = 12,fallSpeed= 10;
    //    游戏关卡
    private int level = 0;
    //屏幕宽度，高度
    static int screenWidth, screenHeight;
    //自定义适配器
    static MyHandler myHandler;
    //自定义游戏视图
    static GameView gameView;
    //定时器
    static Timer timer;
    static MyTimerTask timerTask;
    static Context context;
    private MyMediaPlayer myMediaPlayer=MainActivity.myMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无标题窗口
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //        显示游戏视图
        gameView = new GameView(this);
        setContentView(gameView);
        context = GameActivity.this;
        //        初始化球速
        xSpeed = (int) (speed * Math.sin(2 * Math.PI / 360 * 30));
        ySpeed = -(int) (speed * Math.cos(2 * Math.PI / 360 * 30));
        //        获取屏幕宽高
        final Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
//        初始化适配器
        myHandler = new MyHandler();
//        定时刷新界面
        gameView.setClickable(true);
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        gameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gameView.whichPressed==1){
                    gameView.stopTimer();
                    final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this, R.style.alert_dialog).create();
                    View view1 = LayoutInflater.from(GameActivity.this).inflate(
                            R.layout.parse_dialog, null);
                    dialog.setView(view1);
                    dialog.setCancelable(false);
                    dialog.show();
                    Button continueButton = (Button) dialog.findViewById(R.id.btn_continue);
                    Button restartButton = (Button) dialog.findViewById(R.id.btn_restart);
                    final Button exitButton = (Button) dialog.findViewById(R.id.btn_exit);
                    continueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            gameView.startTimer(0);
                        }
                    });
                    restartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            gameView.score=0;
                            gameView.lives=4;
                            gameView.guanqiaNum=1;
                            gameView.initialize(GameActivity.this);
                            gameView.invalidate();

                            if(MainActivity.allowMusic) {
                                myMediaPlayer.stopBgm();
                                myMediaPlayer.startBgm(GameActivity.this);
                            }
                            gameView.startTimer(0);
                        }
                    });
                    exitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if(MainActivity.allowMusic) myMediaPlayer.stopBgm();
                            Intent intent=new Intent(GameActivity.this,MainActivity.class);
                            startActivity(intent);
                            GameActivity.this.finish();
                        }
                    });
                }else if(gameView.whichPressed==2){
                    gameView.stopTimer();
                    final AlertDialog helpDialog = new AlertDialog.Builder(GameActivity.this, R.style.alert_dialog).create();
                    View helpView = LayoutInflater.from(GameActivity.this).inflate(
                            R.layout.help_dialog, null);
                    helpDialog.setView(helpView);
                    helpDialog.setCancelable(false);
                    helpDialog.show();
                    Button backButton=(Button) helpDialog.findViewById(R.id.button5);
                    backButton.setText("返回游戏");
                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            helpDialog.dismiss();
                            gameView.startTimer(0);
                        }
                    });
                }else if(gameView.whichPressed==3){
                    gameView.allowClickBall=false;
                    for(int i=0;i<gameView.ballList.size();i++){
                        gameView.ballList.get(i).setxSpeed((float)(speed*Math.sin(2*Math.PI/360*30)));
                        gameView.ballList.get(i).setySpeed(-(float)(speed*Math.cos(2*Math.PI/360*30)));
                    }
                }

            }
        });
    }

    /*
    自定义适配器
     */
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
//                通知画面重绘
                gameView.invalidate();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = GameActivity.this.getSharedPreferences("username", Context.MODE_WORLD_READABLE);
        //通过Editor对象以键值对<String Key,String Value>存储数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("money", GameView.moneyNum);
        //通过.commit()方法保存数据
        editor.commit();
        if(MainActivity.allowMusic)myMediaPlayer.stopBgm();
        gameView.stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = context.getSharedPreferences("username", Context.MODE_WORLD_READABLE);
        //.getString("savename","没有保存数据")第一个参数为文件内的name，方法的第二个参数为缺省值，如果SharedPreferences没有该参数，将返回缺省值
        GameView.moneyNum=sp.getInt("money",150);
        GameView.presentBallId=sp.getInt("presentball",R.mipmap.icon);
        if((!gameView.victory_dialog.isShowing())&&(!gameView.gameover_dialog.isShowing())&&MainActivity.allowMusic) {
            myMediaPlayer.startBgm(GameActivity.this);
            gameView.startTimer(0);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(MainActivity.allowMusic) myMediaPlayer.stopBgm();
        Intent intent=new Intent(GameActivity.this,MainActivity.class);
        startActivity(intent);
        GameActivity.this.finish();
    }
}
