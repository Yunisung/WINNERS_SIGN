package contract.hee.bukook.find;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.R;

public class FindIdFinishPopup extends Activity {
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_id_finish_popup);


        id=findViewById(R.id.id);
        id.setText(getIntent().getStringExtra("mb_id"));


    }

    public void goLoginFrm(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void findPwFrm(View v){
        Intent intent = new Intent(getApplication(),FindPwActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}