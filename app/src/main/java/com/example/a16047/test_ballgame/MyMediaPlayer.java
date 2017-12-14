package com.example.a16047.test_ballgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by 16047 on 2017/8/23.
 */

public class MyMediaPlayer {
    private MediaPlayer bgm=new MediaPlayer();
    private MediaPlayer mainBgm=new MediaPlayer();
    private MediaPlayer victorySound=new MediaPlayer();
    private MediaPlayer falseSound=new MediaPlayer();
    private SoundPool soundPool=new SoundPool(4, AudioManager.STREAM_MUSIC,0 );
    private int knockBrickId,knockWallId,knockRacketId,magic,liveup,coins,change,zidan,fire;
    public  MyMediaPlayer(Context context1){
        bgm=MediaPlayer.create(context1,R.raw.bgm3);
        mainBgm=MediaPlayer.create(context1,R.raw.bgm2);
        victorySound=MediaPlayer.create(context1,R.raw.victory);
        falseSound=MediaPlayer.create(context1,R.raw.defeat);
        knockBrickId=soundPool.load(context1,R.raw.knock_brick,1);
        knockRacketId=soundPool.load(context1,R.raw.knock_racket2,1);
        knockWallId=soundPool.load(context1,R.raw.knock_wall3,1);
        magic=soundPool.load(context1,R.raw.magic,1);
        liveup=soundPool.load(context1,R.raw.liveup,1);
        coins=soundPool.load(context1,R.raw.coins2,1);
        change=soundPool.load(context1,R.raw.change,1);
        zidan=soundPool.load(context1,R.raw.zidan2,1);
        fire=soundPool.load(context1,R.raw.fire,1);
    }
    public void startMainBgm(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        mainBgm=MediaPlayer.create(context1,R.raw.bgm2);
        mainBgm.setVolume(volume,volume);
        mainBgm.start();
        mainBgm.setLooping(true);
    }
    public void stopMainBgm(){
        mainBgm.stop();
    }
    public void startBgm(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        bgm=MediaPlayer.create(context1,R.raw.bgm3);
        bgm.setVolume(volume,volume);
        bgm.start();
        bgm.setLooping(true);
    }
    public void stopBgm(){
        bgm.stop();
    }
    public void startMagicSound(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(magic,volume,volume,1,0,1.0f);
    }
    public void stopMagicSound(Context context1){
        soundPool.stop(magic);
    }
    public void startVictorySound(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        victorySound=MediaPlayer.create(context1,R.raw.victory);
        victorySound.setVolume(volume,volume);
        victorySound.start();
    }
    public void stopVictorySound(){
        victorySound.stop();
    }
    public void startDefeatSound(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        falseSound=MediaPlayer.create(context1,R.raw.defeat);
        falseSound.setVolume(volume,volume);
        falseSound.start();
    }
    public void stopDefeatSound(){
        falseSound.stop();
    }
    public void startKnockBrickSound(Context context1) {
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(knockBrickId,volume,volume,1,0,1.5f);
    }
    public void stopKnockBrickSound(){
        soundPool.stop(knockBrickId);
    }
    public void startLiveupSound(Context context1) {
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(liveup,volume,volume,1,0,1.0f);
    }
    public void stopLiveupSound(){
        soundPool.stop(liveup);
    }
    public void startCoinsSound(Context context1) {
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(coins,volume,volume,1,0,1.0f);
    }
    public void stopCoinsSound(){
        soundPool.stop(coins);
    }
    public void startChangeSound(Context context1) {
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(change,volume,volume,1,0,1.5f);
    }
    public void stopChangeSound(){
        soundPool.stop(change);
    }
    public void startFireSound(Context context1) {
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(fire,volume,volume,1,0,1.5f);
    }
    public void stopFireSound(){
        soundPool.stop(fire);
    }
    public void startZidanSound(Context context1) {
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(zidan,volume,volume,1,0,1.5f);
    }
    public void stopZidanSound(){
        soundPool.stop(zidan);
    }
    public void startKnockWallSound(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(knockWallId,volume,volume,1,0,2.0f);
    }
    public void stopKnockWallSound(){
        soundPool.stop(knockWallId);
    }
    public void startKnockRacketSound(Context context1){
        AudioManager audioManager = (AudioManager) context1.getSystemService(Context.AUDIO_SERVICE);
        float currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=currVolume/maxVolume;
        soundPool.play(knockRacketId,volume,volume,1,0,2.0f);
    }
    public void stopKnockRacketSound(){
        soundPool.stop(knockRacketId);
    }
}
