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

    Button button;
    TextView textView;
    ImageView iv_wheel;


    Random r;
    Random n;

    int degree = 0, degree_old = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        button = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        iv_wheel = (ImageView) findViewById(R.id.ic_wheel);

        r = new Random();
        n = new Random();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                degree_old = degree % 360;
                degree = r.nextInt(3600) + 720;
                if ((currentuser.getCurrentUser().toLowerCase().contains("peter"))){
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
                        textView.setText("");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.setText(currentNumber(360 - (degree % 360)));
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
        String text = "";

        if(degrees >= 0 && degrees < (45)){
            text = "Classic";
        }

        if(degrees >= (45) && degrees < (90)){
            text = "Pilsner";
        }

        if(degrees >= (90) && degrees < (135)){
            text = "Classic";
        }

        if(degrees >= (135) && degrees < (180)){
            text = "Pilsner";
        }

        if(degrees >= (180) && degrees < (225)){
            text = "Classic";
        }

        if(degrees >= (225) && degrees < (270)){
            text = "Pilsner";
        }

        if(degrees >= (270) && degrees < (315)){
            text = "Classic";
        }

        if(degrees >= (315) && degrees < (360)){
            text = "Pilsner";
        }

        return text;

    }

    // Your welcome Peter, we know your love for Plisner ;)
    private void itsPeter(){
        int xD = n.nextInt(6);
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
