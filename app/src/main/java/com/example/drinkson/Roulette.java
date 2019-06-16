package com.example.drinkson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class Roulette extends AppCompatActivity {

    private TextView beerText;
    private ImageView iv_wheel;

    private Random randomInt;
    private Random randomPeter;

    int degree = 0, degree_old = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        Button spinButton = findViewById(R.id.button2);
        beerText = findViewById(R.id.textView);
        iv_wheel = findViewById(R.id.ic_wheel);

        randomInt = new Random();
        randomPeter = new Random();

        spinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                degree_old = degree % 360;
                degree = randomInt.nextInt(3600) + 720;
                if ((CurrentUser.getCurrentUser().toLowerCase().contains("peter"))){
                    itsPeter();
                }
                System.out.println(degree);
                RotateAnimation rotate = new RotateAnimation(degree_old, degree,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(3600);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new DecelerateInterpolator());
                rotate.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        beerText.setText("");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        beerText.setText(currentNumber(360 - (degree % 360)));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                iv_wheel.startAnimation(rotate);
            }
        });

    }

    private String currentNumber(int degrees){
        String beer = "";

        if(degrees >= 0 && degrees < (45)){
            beer = "Classic";
        }

        if(degrees >= (45) && degrees < (90)){
            beer = "Pilsner";
        }

        if(degrees >= (90) && degrees < (135)){
            beer = "Classic";
        }

        if(degrees >= (135) && degrees < (180)){
            beer = "Pilsner";
        }

        if(degrees >= (180) && degrees < (225)){
            beer = "Classic";
        }

        if(degrees >= (225) && degrees < (270)){
            beer = "Pilsner";
        }

        if(degrees >= (270) && degrees < (315)){
            beer = "Classic";
        }

        if(degrees >= (315) && degrees < (360)){
            beer = "Pilsner";
        }

        return beer;

    }

    // Your welcome Peter, we know your love for Plisner ;)
    private void itsPeter(){
        int xD = randomPeter.nextInt(6);
        if (xD == 1) {
            degree = 1545;
        }
        if (xD == 2) {
            degree = 3618;
        }
        if (xD == 3) {
            degree = 840;
        }
        if (xD == 4) {
            degree = 1545;
        }
        if (xD == 5) {
            degree = 1725;
        }
        if (xD == 6) {
            degree = 2460;

        }
    }
}
