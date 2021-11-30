package contract.hee.bukook.mbinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;

public class UpdateInfoFinishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_finish);
    }
//    public void back(View v){
//        onBackPressed();
//    }
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
//        startActivity(intent);
//        finish();
//    }
//    public void goMyInfoFrm(View v){
//        Intent intent = new Intent(getApplicationContext(), MyInfoPwCkActivity.class);
//        startActivity(intent);
//        finish();
//    }
//    public void goLoginAfterMain(View v){
//        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
//        startActivity(intent);
//        finish();
//    }
}