package in.edureal.billsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText billAmount;
    private EditText totalPeople;
    private EditText taxPercentage;
    private EditText tipPercentage;

    private AdView adView;

    private SwitchCompat taxSwitcher;
    private SwitchCompat tipSwitcher;

    private boolean useDefaultTax;
    private boolean useDefaultTip;

    private void showError(String str){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(str)
                .show();
    }

    public void calculate(View view){

        String enteredAmt=billAmount.getText().toString().trim();
        int amtLen=enteredAmt.length();
        if(amtLen==0){
            showError("Please enter the bill amount.");
        }else{
            if(amtLen>9){
                showError("Bill Amount max length - 9");
            }else{
                if(enteredAmt.matches("[+-]?([0-9]*[.])?[0-9]+")){

                    String enteredPeople=totalPeople.getText().toString().trim();
                    int peopleLen=enteredPeople.length();
                    if(peopleLen==0){
                        showError("Please enter the total number of people at the table.");
                    }else{
                        if(peopleLen>6){
                            showError("Total people max length - 6");
                        }else{
                            if(enteredPeople.matches("[0-9]+")){

                                boolean further=true;
                                float taxRate=0.0f;
                                if(useDefaultTax){
                                    taxRate=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("tax",0.0f);
                                }else{

                                    String enteredTax=taxPercentage.getText().toString().trim();
                                    int taxLen=enteredTax.length();
                                    if(taxLen==0){
                                        further=false;
                                        showError("Please enter tax percentage.");
                                    }else{
                                        if(taxLen>6){
                                            further=false;
                                            showError("Tax max length - 6");
                                        }else{
                                            if(enteredTax.matches("[+-]?([0-9]*[.])?[0-9]+")){
                                                taxRate=Float.parseFloat(enteredTax);
                                                further=true;
                                            }else{
                                                further=false;
                                                showError("Enter a valid tax percentage.");
                                            }
                                        }
                                    }

                                }

                                if(further){

                                    further=true;
                                    float tipRate=0.0f;
                                    if(useDefaultTip){
                                        tipRate=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("tip",0.0f);
                                    }else{

                                        String enteredTip=tipPercentage.getText().toString().trim();
                                        int tipLen=enteredTip.length();
                                        if(tipLen==0){
                                            further=false;
                                            showError("Please enter tip percentage.");
                                        }else{
                                            if(tipLen>6){
                                                further=false;
                                                showError("Tip max length - 6");
                                            }else{
                                                if(enteredTip.matches("[+-]?([0-9]*[.])?[0-9]+")){
                                                    tipRate=Float.parseFloat(enteredTip);
                                                    further=true;
                                                }else{
                                                    further=false;
                                                    showError("Enter a valid tip percentage.");
                                                }
                                            }
                                        }

                                    }

                                    if(further){

                                        float amt=Float.parseFloat(enteredAmt);
                                        int people=Integer.parseInt(enteredPeople);
                                        if(Float.compare(amt,0.0f)>0){

                                            if(people>0){

                                                if(Float.compare(taxRate,0.0f)>=0){

                                                    if(Float.compare(tipRate,0.0f)>=0){
                                                        //billAmount.setText("");
                                                        //totalPeople.setText("");
                                                        //taxPercentage.setText("");
                                                        //tipPercentage.setText("");
                                                        //useDefaultTip=true;
                                                        //useDefaultTax=true;
                                                        //taxSwitcher.setChecked(true);
                                                        //tipSwitcher.setChecked(true);
                                                        SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putFloat("requestAmount",amt);
                                                        SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putInt("requestPeople",people);
                                                        SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putFloat("requestTax",taxRate);
                                                        SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().putFloat("requestTip",tipRate);
                                                        SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSpEditor().commit();

                                                        Intent intent=new Intent(MainActivity.this, CalculationActivity.class);
                                                        startActivity(intent);

                                                    }else{
                                                        showError("Tip rate cannot be negative.");
                                                    }

                                                }else{
                                                    showError("Tax rate cannot be negative.");
                                                }

                                            }else{
                                                showError("Number of people cannot be zero or negative.");
                                            }

                                        }else{
                                            showError("Amount cannot be zero or negative.");
                                        }

                                    }

                                }

                            }else{
                                showError("Please enter the total number of people at the table.");
                            }
                        }
                    }

                }else{
                    showError("Please enter a valid bill amount.");
                }
            }
        }

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }

    public void githubSources(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_openGitHub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vishu103/"));
                startActivity(browserIntent);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        billAmount=(EditText) findViewById(R.id.billAmount);
        totalPeople=(EditText) findViewById(R.id.totalPeople);
        taxPercentage=(EditText) findViewById(R.id.taxPercentage);
        tipPercentage=(EditText) findViewById(R.id.tipPercentage);

        taxSwitcher=(SwitchCompat) findViewById(R.id.taxSwitcher);
        tipSwitcher=(SwitchCompat) findViewById(R.id.tipSwitcher);
        useDefaultTax=true;
        useDefaultTip=true;

        taxSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    useDefaultTax=true;
                    taxPercentage.setVisibility(View.GONE);
                }else{
                    useDefaultTax=false;
                    taxPercentage.setVisibility(View.VISIBLE);
                }
            }
        });

        tipSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    useDefaultTip=true;
                    tipPercentage.setVisibility(View.GONE);
                }else{
                    useDefaultTip=false;
                    tipPercentage.setVisibility(View.VISIBLE);
                }
            }
        });

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713"); // Sample App ID
        adView=(AdView) findViewById(R.id.adView);
        AdRequest request=new AdRequest.Builder().build();
        adView.loadAd(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Tools.changeMenuIconColor(menu, Color.BLACK);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else {
            githubSources();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Exit")
                .setContentText("Are you sure you want to close Bill Split?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

}
