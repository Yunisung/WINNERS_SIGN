package contract.hee.bukook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import contract.hee.bukook.contract.MyContractCkActivity;

public class ContractFinishPopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contract_finish_popup);

    }

    public void goMyconCk(View v){
        Intent intent = new Intent(getApplicationContext(), MyContractCkActivity.class);
        startActivity(intent);
        finish();
    }
    public void goLoginMenu(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
}