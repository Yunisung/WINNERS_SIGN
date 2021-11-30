package contract.hee.bukook.find;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.R;

public class FindPwFinishPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_pw_finish_popup);

    }

    public void back(View v){
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    //로그인 버튼
    public void goLoginFrm(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}