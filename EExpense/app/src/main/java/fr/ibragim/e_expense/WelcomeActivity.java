package fr.ibragim.e_expense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        logo = findViewById(R.id.splashLogo);
        name = findViewById(R.id.splashName);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splashtransition);

        logo.startAnimation(anim);
        name.startAnimation(anim);
        final Intent i = new Intent(this, LoginActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
