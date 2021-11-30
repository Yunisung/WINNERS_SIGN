package contract.hee.bukook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import contract.hee.bukook.bean.SessionMb;

public class LogoutPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logout_popup);
    }
    public void goLogout(View v){
        ActivityCompat.finishAffinity(this);
        SharedPreferences preferences =getSharedPreferences("mb", Context.MODE_PRIVATE);
        SharedPreferences.Editor e =preferences.edit();
        e.clear().commit();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("KILL",true);
        SessionMb.mb_id=null;
        SessionMb.mb_idnum=null;
        SessionMb.mb_name=null;
        Toast.makeText(this, "로그아웃이 되었습니다.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    public void noLogout(View v){
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}

