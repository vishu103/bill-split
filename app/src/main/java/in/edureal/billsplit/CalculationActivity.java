package in.edureal.billsplit;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CalculationActivity extends AppCompatActivity {

    private TextView billAmount;
    private TextView peopleAtTable;
    private TextView taxAndTip;
    private TextView payableAmount;
    private TextView equalSplit;
    private Button unequalSplit;

    private float amount;
    private int people;
    private float tax;
    private float taxAmount;
    private float tip;
    private float tipAmount;
    private float pay;
    private float equal;
    private String symbol;

    private AdView adView;


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }

    private void showError(String str){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(str)
                .show();
    }

    public void splitUnequally(View view){

        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.success)
                .setTitle("Please enter some of the lowest amounts individuals on your table are willing to pay separated by commas. (Ex: 10,20,30)")
                .setIcon(R.drawable.ic_split)
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        String original=text.trim();
                        int strLne=original.length();
                        if(strLne==0){
                            showError("You cannot leave the field empty.");
                        }else{
                            StringTokenizer tokenizer=new StringTokenizer(original,",");
                            int numToken=tokenizer.countTokens();
                            if(numToken>0){
                                if(numToken>=people){
                                    showError("Number of amounts have to be less than total number of people at the table.");
                                }else{

                                    boolean further=true;
                                    float sums=0.0f;

                                    for(int i=0;i<numToken;i++){
                                        String temp=tokenizer.nextToken().trim();
                                        if(temp.length()>9){
                                            further=false;
                                            showError("Amount max length - 9.");
                                            break;
                                        }else{
                                            if(temp.matches("[+-]?([0-9]*[.])?[0-9]+")){
                                                sums+=Float.parseFloat(temp);
                                            }else{
                                                further=false;
                                                showError("Please enter valid amounts.");
                                                break;
                                            }
                                        }
                                    }

                                    if(further){
                                        if(Float.compare(sums, pay) >= 0){
                                            showError("Sum of the entered amounts cannot be greater than or equal to payable amount.");
                                        }else{
                                            float diff=pay-sums;
                                            int peopleLeft=people-numToken;
                                            new LovelyStandardDialog(CalculationActivity.this)
                                                    .setTopColorRes(R.color.success)
                                                    .setIcon(R.drawable.ic_info)
                                                    .setTitle("Rest pay "+symbol+" "+(diff/peopleLeft)+" per person")
                                                    .setMessage("If some individuals pays the entered low amounts, the rest of the people have to pay "+symbol+" "+(diff/peopleLeft)+" per person")
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .show();
                                        }
                                    }

                                }
                            }else{
                                showError("You cannot leave the field empty.");
                            }
                        }
                    }
                })
                .show();

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
        setContentView(R.layout.activity_calculation);
        initToolbar();

        billAmount=(TextView) findViewById(R.id.billAmount);
        peopleAtTable=(TextView)findViewById(R.id.peopleAtTable);
        taxAndTip=(TextView) findViewById(R.id.taxAndTip);
        payableAmount=(TextView) findViewById(R.id.payableAmount);
        equalSplit=(TextView) findViewById(R.id.equalSplit);
        unequalSplit=(Button) findViewById(R.id.unequalSplit);

        amount=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("requestAmount",0.0f);
        people=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getInt("requestPeople",0);
        tax=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("requestTax",0.0f);
        tip=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getFloat("requestTip",0.0f);
        symbol=SharedPreferenceSingleton.getInstance(this.getApplicationContext()).getSp().getString("symbol","");

        taxAmount=(tax/100)*amount;
        tipAmount=(tip/100)*amount;
        pay=amount+taxAmount+tipAmount;
        equal=pay/people;

        billAmount.setText(symbol+" "+amount);
        peopleAtTable.setText(people+" ");
        taxAndTip.setText(tax+"% (i.e. "+symbol+" "+taxAmount+")\n"+tip+"% (i.e. "+symbol+" "+tipAmount+")");
        payableAmount.setText(symbol+" "+pay);
        equalSplit.setText(symbol+" "+equal+" per person");

        if(people>1){
            unequalSplit.setVisibility(View.VISIBLE);
        }

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
        if(item.getItemId() == android.R.id.home){
            Intent intent=new Intent(CalculationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            githubSources();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(CalculationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
