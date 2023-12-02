package com.example.colormatchgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView iv_button;
    ImageView iv_arrow;
    TextView tv_points;
    ProgressBar progressBar;

    Handler handler;
    Runnable runnable;
    Random r;

    private final static int STATIC_BLUE = 1;
    private final static int STATIC_RED = 2;
    private final static int STATIC_YELLOW = 3;
    private final static int STATIC_GREEN = 4;

    int buttonState = STATIC_BLUE;
    int arrowState = STATIC_BLUE;

    int originalStartTime = 4000;
    int currentTime = 4000;
    int startTime = 4000;
    int currentPoints = 0;

    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_button = findViewById(R.id.iv_button);
        iv_arrow = findViewById(R.id.iv_arrow);
        tv_points = findViewById(R.id.tv_points);
        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);

        //set the initial progressbar time to 4 seconds
        progressBar.setMax(startTime);
        progressBar.setProgress(startTime);

        originalStartTime = startTime;

        //retry button if game over
        Button btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setVisibility(View.GONE);

        //display the starting points
        tv_points.setText("Points: " + currentPoints);

        //generate random arrow color at the start of the game
        r = new Random();
        arrowState = r.nextInt(4) + 1;
        setArrowImage(arrowState);

        iv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rotate the button with the colors
                setButtonImage(setButtonPosition(buttonState));
                updateBackgroundColor();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the resetGame method when the "Retry" button is clicked
                resetGame();
            }
        });

        //main game loop
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //show progress
                Button btnRetry = findViewById(R.id.btnRetry);
                btnRetry.setVisibility(View.GONE);
                currentTime = currentTime - 100;
                progressBar.setProgress(currentTime);
                //check of there is still some time left in the progressbar
                if(currentTime > 0){
                    handler.postDelayed(runnable, 100);
                } else{
                    //check if the colors of the arrow and the button are the same
                    if(buttonState == arrowState){
                        //increase points and show them
                        currentPoints = currentPoints + 1;
                        tv_points.setText("Points: " + currentPoints);
                        //make the speed higher after every turn/if the speed is 1 second make it again 2 seconds
                        if (currentPoints % 5 == 0) {
                            // Increase the speed after every 10 points
                            startTime -= 2000; // or any value you desire
                            if (startTime < 1000) {
                                startTime = 2000;
                            }
                            progressBar.setMax(startTime);
                        }
                        progressBar.setMax(startTime);
                        currentTime = startTime;
                        progressBar.setProgress(currentTime);
                        //generate new color of the arrow
                        arrowState = r.nextInt(4) + 1;
                        setArrowImage(arrowState);

                        handler.postDelayed(runnable, 100);
                    } else{
                        iv_button.setEnabled(false);
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                        btnRetry.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        //start the game loop
        handler.postDelayed(runnable, 100);
    }
    //display the arrow color according to the generated number
    private void setArrowImage(int state){
        switch(state){
            case STATIC_BLUE:
                iv_arrow.setImageResource(R.drawable.ic_blue);
                arrowState = STATIC_BLUE;
                break;
            case STATIC_RED:
                iv_arrow.setImageResource(R.drawable.ic_red);
                arrowState = STATIC_RED;
                break;
            case STATIC_YELLOW:
                iv_arrow.setImageResource(R.drawable.ic_yellow);
                arrowState = STATIC_YELLOW;
                break;
            case STATIC_GREEN:
                iv_arrow.setImageResource(R.drawable.ic_green);
                arrowState = STATIC_GREEN;
                break;
        }
    }
    //rotate animation of the button when clicked
    private void setRotation(final ImageView image, final int drawable){
        //rotate 90 degrees
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(100);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                image.setImageResource(drawable);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        image.startAnimation(rotateAnimation);
    }
    //set button colors position 1-4
    private int setButtonPosition(int position){
        position = position + 1;
        if(position == 5){
            position = 1;
        }
        return position;
    }
    private void setButtonImage(int state){
        switch(state){
            case STATIC_BLUE:
                setRotation(iv_button, R.drawable.ic_button_blue);
                buttonState = STATIC_BLUE;
                break;
            case STATIC_RED:
                setRotation(iv_button, R.drawable.ic_button_red);
                buttonState = STATIC_RED;
                break;
            case STATIC_YELLOW:
                setRotation(iv_button, R.drawable.ic_button_yellow);
                buttonState = STATIC_YELLOW;
                break;
            case STATIC_GREEN:
                setRotation(iv_button, R.drawable.ic_button_green);
                buttonState = STATIC_GREEN;
                break;
        }
    }
    //reset the game w/ retry button if game over
    private void resetGame() {
        // Reset all game variables to their initial values
        currentTime = originalStartTime; // Reset to the original start time
        currentPoints = 0;
        buttonState = r.nextInt(4) + 1; // Set buttonState to a random value
        arrowState = r.nextInt(4) + 1; // Set arrowState to a random value

        // Reset UI elements
        progressBar.setMax(originalStartTime);
        progressBar.setProgress(originalStartTime);
        tv_points.setText("Points: " + currentPoints);
        setArrowImage(arrowState);
        setButtonImage(buttonState);
        iv_button.setEnabled(true);

        // Make the "Retry" button visible after the game is over
        Button btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setVisibility(View.VISIBLE);

        // Restart the game loop with the new time values
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 100);
    }

    private void updateBackgroundColor() {
        // Generate a random color
        int color = Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        // Set the background color of the layout container
        mainLayout.setBackgroundColor(color);
    }
}
