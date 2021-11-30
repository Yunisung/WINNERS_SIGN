package contract.hee.bukook.contract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;

public class ContractFinishActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contract_finish);

    }
//    public void goMyconCk(View v){
//        Intent intent = new Intent(getApplicationContext(), MyContractCkActivity.class);
//        startActivity(intent);
//        finish();
//    }
//    public void goLoginMenu(View v){
//        onBackPressed();
//    }
//    @Override
//    public void onBackPressed() {
//        Intent intent= new Intent(getApplicationContext(), LoginMenuActivity.class);
//        startActivity(intent);
//        finish();
//    }


}