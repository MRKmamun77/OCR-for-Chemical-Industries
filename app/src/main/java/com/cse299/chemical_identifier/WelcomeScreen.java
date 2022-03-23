package com.cse299.chemical_identifier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse299.chemical_identifier.activities.MainActivity;

public class WelcomeScreen extends AppCompatActivity {

    ImageView imageView;
    TextView text_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        imageView = (ImageView)findViewById(R.id.imageView);
        text_animation = (TextView)findViewById(R.id.text_animation);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.welcomescreen);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.textanimation);
        imageView.setAnimation(animation);
        text_animation.setAnimation(animation1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
