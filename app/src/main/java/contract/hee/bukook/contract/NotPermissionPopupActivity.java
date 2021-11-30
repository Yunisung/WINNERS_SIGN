package contract.hee.bukook.contract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;

public class NotPermissionPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_not_permission_popup);
    }
    public void goPermission(View v){
        String permission= getIntent().getStringExtra("permission");
        if(permission.equals("li")){
            Intent intent = new Intent(getApplication(),Li_AddFilesActivity.class);
            startActivity(intent);
            finish();
        }else if(permission.equals("in")){
            Intent intent = new Intent(getApplication(),In_AddFilesActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "다시시도 해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void noPermission(View v){
        Toast.makeText(this, "계약서 작성이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplication(),LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}