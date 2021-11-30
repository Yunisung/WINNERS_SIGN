package contract.hee.bukook.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.R;

public class FindPwFinishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_finish);
    }
    public void back(View v){
        Intent intent = new Intent(getApplication(),MainActivity.class);
        startActivity(intent);
        finish();
    }
    //로그인 버튼
    //public void goLoginFrm(View v){
//        onBackPressed();
//    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}