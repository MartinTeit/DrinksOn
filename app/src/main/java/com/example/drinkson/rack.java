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

public class rack extends AppCompatActivity {

    Button button;
    TextView textView;
    ImageView iv_wheel;
    //hej

    Random r;

    int degree = 0, degree_old = 0;

    private static final float FACTOR = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rack);

        button = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        iv_wheel = (ImageView) findViewById(R.id.ic_wheel);

        r = new Random();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                degree_old = degree % 360;
                degree = r.nextInt(3600) + 720;
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

        if(degrees >= (FACTOR *1) && degrees < (FACTOR * 3)){
            text = "Classic";
        }
        if(degrees >= (FACTOR *3) && degrees < (FACTOR * 5)){
            text = "Pilsner";
        }
        if(degrees >= (FACTOR *5) && degrees < (FACTOR * 7)){
            text = "Classic";
        }
        if(degrees >= (FACTOR *7) && degrees < (FACTOR * 9)){
            text = "Pilsner";
        }
        if(degrees >= (FACTOR *9) && degrees < (FACTOR * 11)){
            text = "Classic";
        }
        if(degrees >= (FACTOR *11) && degrees < (FACTOR * 13)){
            text = "Pilsner";
        }
        if(degrees >= (FACTOR *13) && degrees < (FACTOR * 15)){
            text = "Classic";
        }
        if(degrees >= (FACTOR *15) && degrees < (FACTOR * 17)){
            text = "Pilsner";
        }
        if(degrees >= (FACTOR *17) && degrees < (FACTOR * 19)){
            text = "Classic";
        }

        return text;
    }
}
