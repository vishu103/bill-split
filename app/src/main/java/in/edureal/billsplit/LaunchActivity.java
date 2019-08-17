package in.edureal.billsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        Glide.with(LaunchActivity.this).load(R.drawable.icon_500).into((ImageView) findViewById(R.id.logo));

        // SharedPreference currency, tax rate and tip percentage
        if(!SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().contains("first")){
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("currency","INR");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putString("symbol","₹");
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putFloat("tax",0.0f);
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putFloat("tip",0.0f);
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putInt("first",1);
            SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().commit();
        }

        new CountDownTimer(3000,3000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }
}
