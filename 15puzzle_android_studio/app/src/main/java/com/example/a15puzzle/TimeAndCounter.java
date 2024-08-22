package com.example.a15puzzle;

import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;

public class TimeAndCounter {
    static int moves;
    static boolean running;
    static long pauseOffset;
    public static void counterInit(TextView view){
        moves = 0;
        CharSequence a = "" + moves;
        view.setText(a);
    }

    public static void counterPlus(TextView view){
        moves++;
        CharSequence a =  "" + moves;
        view.setText(a);
    }

    public static void startChronometer(Chronometer timer){
        if (!running){
            timer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            timer.start();
            running = true;
        }
    }

    public static void pauseChronometer(Chronometer timer){
        if (running){
            timer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - timer.getBase();
            running = false;
        }
    }

    public static void resetChronometer(Chronometer timer){
        timer.setBase(SystemClock.elapsedRealtime());
        timer.stop();
        running = false;
        pauseOffset = 0;
    }
}
