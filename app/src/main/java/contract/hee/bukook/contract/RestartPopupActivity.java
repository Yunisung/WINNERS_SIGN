package contract.hee.bukook.contract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import contract.hee.bukook.R;
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.Licensee;

public class RestartPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_restart_popup);
    }
    //이어서 작성
    public void goNextCon(View v){
        Intent intent = getIntent();
        String activity = intent.getExtras().getString("activity");
        if("li".equals(activity)){
            Licensee.db_div = "U";
            Intent intent_li = new Intent(getApplicationContext(), Li_AgreeActivity.class);
            startActivity(intent_li);
            finish();
        }else{
            Individual.db_div = "U";
            Intent intent_in = new Intent(getApplicationContext(), In_AgreeActivity.class);
            startActivity(intent_in);
            finish();
        }
    }
    //신규작성
    public void goNewCon(View v){
        Intent intent = getIntent();
        String activity = intent.getExtras().getString("activity");

        if("li".equals(activity)){
            Licensee.db_div = "I";
            Intent intent_li = new Intent(getApplicationContext(), Li_AgreeActivity.class);
            startActivity(intent_li);
            finish();
        }else{
            Individual.db_div = "I";
            Intent intent_in = new Intent(getApplicationContext(), In_AgreeActivity.class);
            startActivity(intent_in);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }

}




