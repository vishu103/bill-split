package in.edureal.billsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

public class SettingsActivity extends AppCompatActivity {

    private TextView currency;
    private TextView taxRate;
    private TextView tipRate;

    public void changeCurrency(View view){
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

    public void changeTax(View view){
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

    public void changeTip(View view){
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
