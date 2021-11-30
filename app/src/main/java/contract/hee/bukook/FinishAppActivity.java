package contract.hee.bukook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.core.app.ActivityCompat;

public class FinishAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finish_app);
    }
    //닫기btn
    public void closePopup(View v){
        finish();
    }

    //앱종료btn
    public void finishApp(View v){
        ActivityCompat.finishAffinity(this);
        System.exit(0);
        finish();
    }
    @Override
    public void onBackPressed() {
        return;
    }

}