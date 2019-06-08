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

    Random r;

    int degree = 0, degree_old = 0;

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

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iv_wheel.startAnimation(rotate);

            }
        });

    }
}
