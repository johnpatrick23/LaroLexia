package com.oneclique.larolexia.helper;

import android.os.CountDownTimer;

public class CountUpTimer extends CountDownTimer {

    private int second;
    private GameTimerListener gameTimerListener;

    public interface GameTimerListener {
        void onCancelTime(int time);
    }

    public CountUpTimer() {
        super(Integer.MAX_VALUE, (1000));
        second = 0;
        gameTimerListener = null;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        second++;
    }

    @Override
    public void onFinish() {
        this.cancel();
        if(gameTimerListener != null){
            gameTimerListener.onCancelTime(second);
        }
    }
    public void setGameTimerListener(GameTimerListener gameTimerListener) {
        this.gameTimerListener = gameTimerListener;
    }
}
