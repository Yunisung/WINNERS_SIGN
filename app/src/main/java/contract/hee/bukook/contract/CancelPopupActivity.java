package contract.hee.bukook.contract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;

public class CancelPopupActivity extends Activity {
    private TextView mainText;
    private TextView text;
    private String mainStr;
    private String textStr;
    private String whereStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cancel_popup);
        mainText = findViewById(R.id.mainText);
        text = findViewById(R.id.text);
        mainStr = getIntent().getStringExtra("main");
        textStr = getIntent().getStringExtra("text");
        whereStr =getIntent().getStringExtra("where");
        mainText.setText(mainStr);
        text.setText(textStr);
    }

    public void goMain(View view) {
        if(whereStr.equals("main")){
            ActivityCompat.finishAffinity(this);
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }else if(whereStr.equals("login")) {
            ActivityCompat.finishAffinity(this);
            Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
            startActivity(intent);
            finish();
        }else{
            ActivityCompat.finishAffinity(this);
            Toast.makeText(this, "다시시도 하십시오.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void cancel(View view) {
        finish();
    }
}