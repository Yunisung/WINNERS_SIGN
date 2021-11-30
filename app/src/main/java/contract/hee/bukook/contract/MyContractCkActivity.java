package contract.hee.bukook.contract;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class MyContractCkActivity extends AppCompatActivity {
    private ToggleButton li_contract;
    private ToggleButton in_contract;
    private TextView name;
    private ContractDB contractDB = ContractDB.getInstance();
    private  String con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contract_ck);
        name = findViewById(R.id.mb_name);
        name.setText(SessionMb.mb_name);
        li_contract = findViewById(R.id.li_contract);
        in_contract = findViewById(R.id.in_contract);
        li_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_contract.setChecked(false);
            }
        });
        in_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                li_contract.setChecked(false);
            }
        });
    }
    public void goHome(View v){
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication() ,LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    public void goMyContractFrm(View v){
        if (li_contract.isChecked()) {
            con="li";
        } else if (in_contract.isChecked()) {
            con="in";
        } else {
            Toast.makeText(MyContractCkActivity.this, "계약서를 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        goMyContract(con);
    }
    //계약서 보여주기
    public void goMyContract(String con) {
        String idnum = SessionMb.mb_idnum;
        if(!idnum.equals(null)){
            HashMap<String,String> map=new HashMap<>();
            map.put("mb_idnum",idnum);
            map.put("contract", con);
            contractDB.selectMyContract(map, callbackSelectMyContract);
        }
    }
    Callback callbackSelectMyContract = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());

                    if("true".equals(body.trim())){
                        if(con.equals("li")){
                            Intent intent = new Intent(getApplicationContext(), Li_MycontractActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), In_MycontractActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Intent intent = new Intent(getApplicationContext(), NotConstractActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    };
    public void goLoginMenu(View v){
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }

}