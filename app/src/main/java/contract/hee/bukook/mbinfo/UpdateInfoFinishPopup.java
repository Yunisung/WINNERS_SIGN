package contract.hee.bukook.mbinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;

public class UpdateInfoFinishPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_info_finish_popup);

    }

    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    public void goMyInfoFrm(View v){
        Intent intent = new Intent(getApplicationContext(), MyInfoPwCkActivity.class);
        startActivity(intent);
        finish();
    }
    public void goLoginAfterMain(View v){
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
}