package contract.hee.bukook.contract;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import contract.hee.bukook.R;

public class AlreadyPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_already_popup);
    }
    //닫기btn
    public void closePopup(View v){
        finish();
    }
    @Override
    public void onBackPressed() {
        return;
    }

}

