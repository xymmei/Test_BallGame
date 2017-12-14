package com.example.a16047.test_ballgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    static boolean allowSound=true;
    static boolean allowMusic=true;
    static MyMediaPlayer myMediaPlayer;
    static int []ballList={
            R.mipmap.iconball,
            R.mipmap.ball1,R.mipmap.ball2,R.mipmap.ball4,
            R.mipmap.ball5,R.mipmap.ball7,R.mipmap.ball8,
            R.mipmap.ball9
    };
    private int presentBallId;
    private int money;
    private int []statusBall={1,0,0,0,0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMediaPlayer=new MyMediaPlayer(MainActivity.this);
        Button button=(Button) this.findViewById(R.id.button);
        Button button1=(Button) this.findViewById(R.id.button2);
        Button button2=(Button) this.findViewById(R.id.button3);
        Button button3=(Button) this.findViewById(R.id.button4);
        final SharedPreferences sp = MainActivity.this.getSharedPreferences("username", Context.MODE_WORLD_READABLE);
        //.getString("savename","没有保存数据")第一个参数为文件内的name，方法的第二个参数为缺省值，如果SharedPreferences没有该参数，将返回缺省值
        presentBallId=sp.getInt("presentball",R.mipmap.iconball);
        System.out.println("--presentBallId-->"+presentBallId);
        money=sp.getInt("money",150);
        System.out.println("--money-->"+money);
        for(int i=1;i<8;i++){
            statusBall[i]=sp.getInt("status_ball"+i,0);
            System.out.println("--statusBall[i]-->"+statusBall[i]);
        }
        final AlertDialog setDialog = new AlertDialog.Builder(MainActivity.this, R.style.alert_dialog).create();
        View view1 = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.set_dialog, null);
        setDialog.setView(view1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,GameActivity.class);
                myMediaPlayer.stopMainBgm();
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog helpDialog = new AlertDialog.Builder(MainActivity.this, R.style.alert_dialog).create();
                View helpView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.help_dialog, null);
                helpDialog.setView(helpView);
                helpDialog.setCancelable(false);
                helpDialog.show();
                Button backButton=(Button) helpDialog.findViewById(R.id.button5);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        helpDialog.dismiss();
                    }
                });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog screenDialog = new AlertDialog.Builder(MainActivity.this, R.style.alert_dialog).create();
                View screenView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.screen_dialog, null);
                screenDialog.setView(screenView);
                screenDialog.setCancelable(false);
                screenDialog.show();
                presentBallId=sp.getInt("presentball",R.mipmap.iconball);
                System.out.println("--presentBallId-->"+presentBallId);
                money=sp.getInt("money",150);
                System.out.println("--money-->"+money);
                for(int i=1;i<8;i++){
                    statusBall[i]=sp.getInt("status_ball"+i,0);
                    System.out.println("--statusBall[i]-->"+statusBall[i]);
                }
                final TextView moneyNum=(TextView)screenDialog.findViewById(R.id.moneyNum);
                moneyNum.setText(" "+money);
                final ImageView presentBall=(ImageView)screenDialog.findViewById(R.id.presentball);
                presentBall.setImageResource(presentBallId);
                TextView moneyball0=(TextView)screenDialog.findViewById(R.id.money_ball0);
                TextView moneyball1=(TextView)screenDialog.findViewById(R.id.money_ball1);
                TextView moneyball2=(TextView)screenDialog.findViewById(R.id.money_ball2);
                TextView moneyball3=(TextView)screenDialog.findViewById(R.id.money_ball3);
                TextView moneyball4=(TextView)screenDialog.findViewById(R.id.money_ball4);
                TextView moneyball5=(TextView)screenDialog.findViewById(R.id.money_ball5);
                TextView moneyball6=(TextView)screenDialog.findViewById(R.id.money_ball6);
                TextView moneyball7=(TextView)screenDialog.findViewById(R.id.money_ball7);
                Button buyButton0=(Button)screenDialog.findViewById(R.id.btn_buy0);
                Button buyButton1=(Button)screenDialog.findViewById(R.id.btn_buy1);
                Button buyButton2=(Button)screenDialog.findViewById(R.id.btn_buy2);
                Button buyButton3=(Button)screenDialog.findViewById(R.id.btn_buy3);
                Button buyButton4=(Button)screenDialog.findViewById(R.id.btn_buy4);
                Button buyButton5=(Button)screenDialog.findViewById(R.id.btn_buy5);
                Button buyButton6=(Button)screenDialog.findViewById(R.id.btn_buy6);
                Button buyButton7=(Button)screenDialog.findViewById(R.id.btn_buy7);
                final List<TextView> prices=new ArrayList<TextView>();
                prices.add(moneyball0);
                prices.add(moneyball1);
                prices.add(moneyball2);
                prices.add(moneyball3);
                prices.add(moneyball4);
                prices.add(moneyball5);
                prices.add(moneyball6);
                prices.add(moneyball7);
                List<Button> btns=new ArrayList<Button>();
                btns.add(buyButton0);
                btns.add(buyButton1);
                btns.add(buyButton2);
                btns.add(buyButton3);
                btns.add(buyButton4);
                btns.add(buyButton5);
                btns.add(buyButton6);
                btns.add(buyButton7);
                for(int i=0;i<btns.size();i++){
                    Button buyButton=btns.get(i);
                    if(statusBall[i]==0){
                        buyButton.setText("购买");
                    }else{
                        buyButton.setText("更换");
                    }
                }
                for(int i=0;i<btns.size();i++){
                    final Button button=btns.get(i);
                    final TextView textView=prices.get(i);
                    final int position=i;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String string=(String)button.getText();
                            System.out.println("--string-->"+string);
                            if(string.equals("购买")){
                                int price= Integer.parseInt(textView.getText().toString());
                                if(price>money){
                                    Toast.makeText(MainActivity.this,"金币不足",Toast.LENGTH_SHORT).show();
                                }else {
                                    money=money-price;
                                    moneyNum.setText(" "+money);
                                    button.setText("更换");
                                    statusBall[position]=1;
                                    Toast.makeText(MainActivity.this,"购买成功",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                presentBall.setImageResource(ballList[position]);
                                presentBallId=ballList[position];
                                Toast.makeText(MainActivity.this,"更换成功",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                Button backButton=(Button)screenDialog.findViewById(R.id.btn_back);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        screenDialog.dismiss();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("money",money);
                        editor.putInt("presentball",presentBallId);
                        for(int i=0;i<statusBall.length;i++){
                            editor.putInt("status_ball"+i,statusBall[i]);
                        }
                        //通过.commit()方法保存数据
                        editor.commit();
                    }
                });
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog.setCancelable(false);
                setDialog.show();
                Switch music=(Switch)setDialog.findViewById(R.id.switch1);
                Switch sound=(Switch)setDialog.findViewById(R.id.switch2);
                music.setChecked(allowMusic);
                sound.setChecked(allowSound);
                music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            allowMusic=true;
                            myMediaPlayer.startMainBgm(MainActivity.this);
                        }else{
                            allowMusic=false;
                            myMediaPlayer.stopMainBgm();
                        }
                    }
                });
                sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            allowSound=true;
                        }else{
                            allowSound=false;
                        }
                    }
                });
                Button cancelButton=(Button)setDialog.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(MainActivity.allowMusic)myMediaPlayer.stopMainBgm();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(MainActivity.allowMusic)myMediaPlayer.stopMainBgm();
        if(MainActivity.allowMusic)myMediaPlayer.startMainBgm(MainActivity.this);
    }

}
