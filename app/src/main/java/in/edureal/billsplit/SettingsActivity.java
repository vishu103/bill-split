package in.edureal.billsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsActivity extends AppCompatActivity {

    private TextView currency;
    private TextView taxRate;
    private TextView tipRate;

    private RewardedVideoAd rewardVideo;

    private SharedPreferences sp;

    private static String rewardVideoAdId="ca-app-pub-3940256099942544/5224354917"; // Test Reward Video Ad
    private String currentFeature;

    public void checkChangeCurrency(View view){
        if(sp.contains("currencyFeature")){
            changeCurrency();
        }else{
            currentFeature="currencyFeature";
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Unlock Feature")
                    .setContentText("You can unlock this feature by just watching a video.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // Show Ad
                            sDialog.dismissWithAnimation();

                            if(rewardVideo.isLoaded()){
                                rewardVideo.show();
                            }else{
                                rewardVideo.loadAd(rewardVideoAdId, new AdRequest.Builder().build());
                            }

                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void changeCurrency(){
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                if(code.equals("INR")){
                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("currency","INR");
                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("symbol","₹");
                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                    currency.setText("Current: INR\nClick to change the default currency.");
                    Toast.makeText(SettingsActivity.this, "Currency changed - ₹", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("currency",code);
                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putString("symbol",symbol);
                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                    currency.setText("Current: "+code+"\nClick to change the default currency.");
                    Toast.makeText(SettingsActivity.this, "Currency changed - "+symbol, Toast.LENGTH_SHORT).show();
                }
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }

    public void checkChangeTax(View view){
        if(sp.contains("taxFeature")){
            changeTax();
        }else{
            currentFeature="taxFeature";
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Unlock Feature")
                    .setContentText("You can unlock this feature by just watching a video.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // Show Ad
                            sDialog.dismissWithAnimation();

                            if(rewardVideo.isLoaded()){
                                rewardVideo.show();
                            }else{
                                rewardVideo.loadAd(rewardVideoAdId, new AdRequest.Builder().build());
                            }

                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void changeTax(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.success)
                .setTitle("Please enter the tax rate")
                .setIcon(R.drawable.ic_pie)
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        String original=text.trim();
                        int len=original.length();
                        if(len==0){
                            Toast.makeText(SettingsActivity.this, "You have to enter a rate.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(len>6){
                                Toast.makeText(SettingsActivity.this, "Max length - 6", Toast.LENGTH_SHORT).show();
                            }else{
                                // Check if valid float
                                if(original.matches("[+-]?([0-9]*[.])?[0-9]+")){
                                    float numb=Float.parseFloat(original);
                                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putFloat("tax",numb);
                                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                                    taxRate.setText("Current: "+numb+" %\nClick to change the default tax rate.");
                                    Toast.makeText(SettingsActivity.this, "Tax Rate changed - "+numb, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SettingsActivity.this, "Enter a valid value.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                })
                .show();
    }

    public void checkChangeTip(View view){
        if(sp.contains("tipFeature")){
            changeTip();
        }else{
            currentFeature="tipFeature";
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Unlock Feature")
                    .setContentText("You can unlock this feature by just watching a video.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // Show Ad
                            sDialog.dismissWithAnimation();

                            if(rewardVideo.isLoaded()){
                                rewardVideo.show();
                            }else{
                                rewardVideo.loadAd(rewardVideoAdId, new AdRequest.Builder().build());
                            }

                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    public void changeTip(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.success)
                .setTitle("Please enter the tip rate")
                .setIcon(R.drawable.ic_pie)
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        String original=text.trim();
                        int len=original.length();
                        if(len==0){
                            Toast.makeText(SettingsActivity.this, "You have to enter a rate.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(len>6){
                                Toast.makeText(SettingsActivity.this, "Max length - 6", Toast.LENGTH_SHORT).show();
                            }else{
                                // Check if valid float
                                if(original.matches("[+-]?([0-9]*[.])?[0-9]+")){
                                    float numb=Float.parseFloat(original);
                                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().putFloat("tip",numb);
                                    SharedPreferenceSingleton.getInstance(SettingsActivity.this).getSpEditor().commit();
                                    tipRate.setText("Current: "+numb+" %\nClick to change the default tip rate.");
                                    Toast.makeText(SettingsActivity.this, "Tip Rate changed - "+numb, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SettingsActivity.this, "Enter a valid value.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currency=(TextView) findViewById(R.id.defaultCurrency);
        taxRate=(TextView) findViewById(R.id.taxRate);
        tipRate=(TextView) findViewById(R.id.tipRate);

        currency.setText("Current: "+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("currency","INR")+"\nClick to change the default currency.");
        taxRate.setText("Current: "+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("tax",0.0f)+" %\nClick to change the default tax rate.");
        tipRate.setText("Current: "+SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("tip",0.0f)+" %\nClick to change the default tip rate.");

        currentFeature="";
        sp=getSharedPreferences("in.edureal.billsplit",MODE_PRIVATE);
        final SharedPreferences.Editor spEditor=sp.edit();

        rewardVideo=MobileAds.getRewardedVideoAdInstance(this);
        if(!sp.contains("currencyFeature") || !sp.contains("taxFeature") || !sp.contains("tipFeature")){
            rewardVideo.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {

                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {

                }

                @Override
                public void onRewardedVideoAdClosed() {
                    rewardVideo.loadAd(rewardVideoAdId, new AdRequest.Builder().build());
                    new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Good job!")
                            .setContentText("Features unlocked...")
                            .show();
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    spEditor.putInt(currentFeature,1).commit();
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {

                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    Toast.makeText(SettingsActivity.this, "There is no ad for you to watch right now. Please try again later...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoCompleted() {

                }
            });
            rewardVideo.loadAd(rewardVideoAdId, new AdRequest.Builder().build());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        rewardVideo.pause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        rewardVideo.resume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        rewardVideo.destroy(this);
        super.onDestroy();
    }
}
