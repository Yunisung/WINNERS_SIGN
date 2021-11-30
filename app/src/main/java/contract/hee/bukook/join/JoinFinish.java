package contract.hee.bukook.join;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.R;

public class JoinFinish extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_finish);
    }
    //back
    public void back(View v){
        onBackPressed();
    }
    //로그인btn
    public void goLoginFrm(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}