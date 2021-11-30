package contract.hee.bukook.contract;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class ContractActivity extends AppCompatActivity {
    private ToggleButton li_contract;
    private ToggleButton in_contract;
    private View goContractFrm;
    private TextView name;
    private ContractDB contractDB = ContractDB.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
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
        goContractFrm = findViewById(R.id.goContractFrm);
        goContractFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean li = li_contract.isChecked();
                Boolean in = in_contract.isChecked();
                HashMap<String, String> map = new HashMap<>();
                if (li == true) {
                    map.put("li_idnum", SessionMb.mb_idnum);
                    contractDB.countYLicensee(map, callbackContractck); //db cnt
                } else if (in == true) {
                    map.put("in_idnum", SessionMb.mb_idnum);
                    contractDB.countYIndividual(map, callbackContractck); //db cnt
                } else {
                    Toast.makeText(ContractActivity.this, "작성하실 계약서를 선택해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    Callback callbackContractck = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Log.d("TAG", "onResponse() returned body.trim(): " + body.trim());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(body.trim());
                        String val = jsonObject.getString("result");
                        if ("Y".equals(val)) {
                            Log.d("TAG", " ## 작성완료 계약서 있음. 관리자에게 문의하세요. "+body.trim());
                            Intent intent = new Intent(getApplicationContext(), AlreadyPopupActivity.class);
                            startActivity(intent);
                            return;
                        }else if ("N".equals(val)) {
                            Log.d("TAG", " ## 신규작성 "+body.trim());
                            if (li_contract.isChecked()) {
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

                        }else if("write".equals(val)){
                            Log.d("TAG", " ## 작성완료 X, 작성중 O -> 팝업띄움 "+body.trim());

                            Intent intent = new Intent(getApplicationContext(), RestartPopupActivity.class);
                            if (li_contract.isChecked()) {
                                Licensee.seq = jsonObject.getString("li_seq");
                                intent.putExtra("activity","li");
                            }else{
                                Individual.seq = jsonObject.getString("in_seq");
                                intent.putExtra("activity","in");
                            }
                            startActivity(intent);
                            return;
                        }else{
                            Log.d("TAG", " ## callbackContractck failed"+body.trim());
                            Toast.makeText(ContractActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };
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