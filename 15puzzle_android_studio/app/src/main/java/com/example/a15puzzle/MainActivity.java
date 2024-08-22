package com.example.a15puzzle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper db;
    TextView bestScoreAndTimeTextView;
    int bestScore;
    int bestTime;
    TextView[] textBoxes;
    Chronometer timer;
    TextView moves, solvedText;
    View solve;
    int displayWidth;
    int emptyBoxLeftMargin, emptyBoxTopMargin;
    int BOX_SIZE, n = 4, emptyIndex;
    RelativeLayout.LayoutParams lp;
    ViewGroup tahta;
    ObjectAnimator anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tahta = findViewById(R.id.root);
        tahta.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        tahta.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        tahta.getLayoutTransition().setDuration(LayoutTransition.APPEARING,300);
        tahta.getLayoutTransition().setDuration(LayoutTransition.CHANGING,100);
        displayWidth = getResources().getDisplayMetrics().widthPixels;
        tahta.getLayoutParams().height = displayWidth - 40;
        moves = findViewById(R.id.counter);
        timer = findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());
        solvedText = findViewById(R.id.solved);
        solve = findViewById(R.id.solveButton);
    }

    @Override
    protected void onStart(){
        super.onStart();

        db = new DatabaseHelper(this);
        bestScore = db.getBestScore(n);
        bestTime = db.getBestTime(n);
        String bestTimeString;
        if(bestTime >= 60){
            long minutes = TimeUnit.SECONDS.toMinutes(bestTime);
            long seconds = bestTime - TimeUnit.MINUTES.toSeconds(minutes);
            bestTimeString = String.format("%02d:%02d", minutes, seconds);
        } else {
            bestTimeString = String.format("00:%02d", bestTime);
        }
        bestScoreAndTimeTextView = findViewById(R.id.bestScoreAndTime);
        bestScoreAndTimeTextView.setText("En iyi skor: " + bestScore + "\nEn iyi süre: " + bestTimeString);

        TimeAndCounter.counterInit(moves);
        textBoxes = new TextView[n*n];
        Combination.newCombination(n);
        emptyIndex = Combination.emptyBox;
        BOX_SIZE = (displayWidth-88)/n;
        emptyBoxTopMargin = BOX_SIZE * (Combination.emptyBox / n);
        emptyBoxLeftMargin = BOX_SIZE * (Combination.emptyBox % n);
        solve.setClickable(true);

        int textSize = ((n/3)*60) - ((n/4)*20) - ((n/5)*10);
        for (int i = 0; i < n*n; i++){
            if(Combination.gameNums[i] == n*n)
                continue;

            textBoxes[i] = new TextView(this, null, 0, R.style.n4Style);
            textBoxes[i].setOnClickListener(this);
            lp = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textBoxes[i].setWidth(BOX_SIZE+8);
            textBoxes[i].setHeight(BOX_SIZE+8);
            lp.topMargin = (i/n) * BOX_SIZE;
            lp.setMarginStart((i%n) * BOX_SIZE);
            CharSequence j = Combination.gameNums[i] + "";
            if((((Combination.gameNums[i]-(n%2))%n)+1)%2 == (n%2))
                textBoxes[i].setBackground(ContextCompat.getDrawable(this, R.drawable.hucreler2));
            textBoxes[i].setText(j);
            textBoxes[i].setTextSize(textSize);
            textBoxes[i].setLayoutParams(lp);
            tahta.addView(textBoxes[i]);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        for (TextView textBox : textBoxes) tahta.removeView(textBox);
        TimeAndCounter.resetChronometer(timer);
        solvedText.setVisibility(View.INVISIBLE);
    }

    public void onRadioButtonClicked(View view){
        for (TextView textBox : textBoxes) tahta.removeView(textBox);
        solvedText.setVisibility(View.INVISIBLE);
        TimeAndCounter.resetChronometer(timer);
        switch (view.getId()){
            case (R.id.rb3):
                n = 3;
                onStart();
                break;

            case (R.id.rb4):
                n = 4;
                onStart();
                break;

            case (R.id.rb5):
                n = 5;
                onStart();
                break;
        }
    }

    public void onClick(View v){
        TimeAndCounter.startChronometer(timer);
        lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        int clickedBoxLeftMargin = lp.getMarginStart();
        int clickedBoxTopMargin = lp.topMargin;
        //swap
        if((Math.abs(clickedBoxLeftMargin - emptyBoxLeftMargin) == BOX_SIZE)
                && clickedBoxTopMargin == emptyBoxTopMargin){
            if(clickedBoxLeftMargin - emptyBoxLeftMargin < 0){
                textBoxes[emptyIndex] = textBoxes[emptyIndex-1];
                textBoxes[emptyIndex-1] = null;
                Combination.gameNums[emptyIndex] = Combination.gameNums[emptyIndex-1];
                Combination.gameNums[(emptyIndex--)-1] = 0;
                Combination.path[Combination.a++] = 0;
            }
            else {
                textBoxes[emptyIndex] = textBoxes[emptyIndex+1];
                textBoxes[emptyIndex+1] = null;
                Combination.gameNums[emptyIndex] = Combination.gameNums[emptyIndex+1];
                Combination.gameNums[(emptyIndex++)+1] = 0;
                Combination.path[Combination.a++] = 2;
            }
            lp.setMarginStart(emptyBoxLeftMargin);
            emptyBoxLeftMargin = clickedBoxLeftMargin;
            TimeAndCounter.counterPlus(moves);
        }
        else if (clickedBoxLeftMargin == emptyBoxLeftMargin && (Math.abs(clickedBoxTopMargin - emptyBoxTopMargin) == BOX_SIZE)){
            if(clickedBoxTopMargin - emptyBoxTopMargin < 0){
                textBoxes[emptyIndex] = textBoxes[emptyIndex-n];
                textBoxes[emptyIndex-n] = null;
                Combination.gameNums[emptyIndex] = Combination.gameNums[emptyIndex-n];
                Combination.gameNums[emptyIndex-n] = 0;
                emptyIndex = emptyIndex-n;
                Combination.path[Combination.a++] = 1;
            }
            else {
                textBoxes[emptyIndex] = textBoxes[emptyIndex+n];
                textBoxes[emptyIndex+n] = null;
                Combination.gameNums[emptyIndex] = Combination.gameNums[emptyIndex+n];
                Combination.gameNums[emptyIndex+n] = 0;
                emptyIndex = emptyIndex+n;
                Combination.path[Combination.a++] = 3;
            }
            lp.topMargin = emptyBoxTopMargin;
            emptyBoxTopMargin = clickedBoxTopMargin;
            TimeAndCounter.counterPlus(moves);
        }
        v.setLayoutParams(lp);

        byte i;
        for (i = 0; i<Combination.gameNums.length-1;){
            if(Combination.gameNums[i] == i+1)
                i++;
            else
                break;
        }

        byte finalI = i;
        new Handler().postDelayed(() -> {
            if(finalI == Combination.gameNums.length-1) {
                db.saveBestScoreAndTime(n, TimeAndCounter.moves, (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000));
                for (TextView textBox : textBoxes) {
                    anim = ObjectAnimator.ofFloat(textBox, "translationY", 1000);
                    anim.setDuration(3000);
                    anim.start();
                }
                solvedText.setVisibility(View.VISIBLE);
                int bestScore = db.getBestScore(n);
                int bestTime = db.getBestTime(n);

                if (bestScoreAndTimeTextView != null) {
                    String bestTimeString;

                    if(bestTime >= 60){
                        long minutes = TimeUnit.SECONDS.toMinutes(bestTime);
                        long seconds = bestTime - TimeUnit.MINUTES.toSeconds(minutes);
                        bestTimeString = String.format("%02d:%02d", minutes, seconds);
                    } else {
                        bestTimeString = String.format("00:%02d", bestTime);
                    }


                    bestScoreAndTimeTextView = findViewById(R.id.bestScoreAndTime);
                    bestScoreAndTimeTextView.setText("En iyi skor: " + bestScore + "\nEn iyi süre: " + bestTimeString);
                }
                TimeAndCounter.pauseChronometer(timer);
            }
        },110);
    }

    public void solve(View v) {
        findViewById(R.id.rb3).setClickable(false);
        findViewById(R.id.rb4).setClickable(false);
        findViewById(R.id.rb5).setClickable(false);
        for(int i = 0; i<textBoxes.length; i++)
            if (textBoxes[i] != null)
                textBoxes[i].setClickable(false);
        TimeAndCounter.pauseChronometer(timer);
        solve.setClickable(false);
        int delay, j=0;
        for (int i = Combination.a-1; i > -1; i--){
            j++;
            delay = j*150;
            int finalI1 = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Combination.path[finalI1] == 0){
                        lp = (RelativeLayout.LayoutParams) textBoxes[emptyIndex+1].getLayoutParams();
                        lp.setMarginStart(lp.getMarginStart()-BOX_SIZE);
                        textBoxes[emptyIndex+1].setLayoutParams(lp);
                        textBoxes[emptyIndex] = textBoxes[++emptyIndex];
                        textBoxes[emptyIndex] = null;
                    }
                    else if(Combination.path[finalI1] == 2){
                        lp = (RelativeLayout.LayoutParams) textBoxes[emptyIndex-1].getLayoutParams();
                        lp.setMarginStart(lp.getMarginStart()+BOX_SIZE);
                        textBoxes[emptyIndex-1].setLayoutParams(lp);
                        textBoxes[emptyIndex] = textBoxes[--emptyIndex];
                        textBoxes[emptyIndex] = null;
                    }
                    else if(Combination.path[finalI1] == 1){
                        lp = (RelativeLayout.LayoutParams) textBoxes[emptyIndex+n].getLayoutParams();
                        lp.topMargin -= BOX_SIZE;
                        textBoxes[emptyIndex+n].setLayoutParams(lp);
                        textBoxes[emptyIndex] = textBoxes[emptyIndex+n];
                        emptyIndex += n;
                        textBoxes[emptyIndex] = null;
                    }
                    else {
                        lp = (RelativeLayout.LayoutParams) textBoxes[emptyIndex-n].getLayoutParams();
                        lp.topMargin += BOX_SIZE;
                        textBoxes[emptyIndex-n].setLayoutParams(lp);
                        textBoxes[emptyIndex] = textBoxes[emptyIndex-n];
                        emptyIndex -= n;
                        textBoxes[emptyIndex] = null;
                    }
                }
            },delay);
        }
        int delay2 = 150*Combination.a + 150;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (TextView textBox : textBoxes) {
                    anim = ObjectAnimator.ofFloat(textBox, "translationY", 1000);
                    anim.setDuration(3000);
                    anim.start();
                }
                solvedText.setVisibility(View.VISIBLE);
                findViewById(R.id.rb3).setClickable(true);
                findViewById(R.id.rb4).setClickable(true);
                findViewById(R.id.rb5).setClickable(true);
            }
        },delay2);
    }
}