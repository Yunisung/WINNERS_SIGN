package contract.hee.bukook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.contract.AlreadyPopupActivity;
import contract.hee.bukook.contract.ContractActivity;
import contract.hee.bukook.contract.In_AgreeActivity;
import contract.hee.bukook.contract.In_MycontractActivity;
import contract.hee.bukook.contract.Li_AgreeActivity;
import contract.hee.bukook.contract.Li_MycontractActivity;
import contract.hee.bukook.contract.MyContractCkActivity;
import contract.hee.bukook.contract.NotConstractActivity;
import contract.hee.bukook.contract.RestartPopupActivity;
import contract.hee.bukook.db.ContractDB;
import contract.hee.bukook.mbinfo.MyInfoPwCkActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginMenuActivity extends AppCompatActivity {
    private TextView name;
    private ContractDB contractDB = ContractDB.getInstance();
    private String selectWriteContract;
    private String selectReadContract;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        name = findViewById(R.id.name);
        name.setText(SessionMb.mb_name + "님");
    }
    public boolean net(){
        if(!CommonFile.isConnect(getBaseContext())){
            Toast.makeText(this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //사업계약서 쓰기
    public void writeLiContract(View v) {
        if(!net())return;
        selectWriteContract = "Li";
        HashMap<String, String> map = new HashMap<>();
        map.put("li_idnum", SessionMb.mb_idnum);
        contractDB.countYLicensee(map, callbackContractck); //db cnt

    }

    //개인계약서 쓰기
    public void writeInContract(View v) {
        if(!net())return;
        selectWriteContract = "In";
        HashMap<String, String> map = new HashMap<>();
        map.put("in_idnum", SessionMb.mb_idnum);
        contractDB.countYIndividual(map, callbackContractck); //db cnt
    }

    //사업계약서 보기
    public void goMyLiContractFrm(View v) {
        if(!net())return;
        selectReadContract = "li";
        HashMap<String, String> map = new HashMap<>();
        map.put("mb_idnum", SessionMb.mb_idnum);
        map.put("contract", selectReadContract);
        contractDB.selectMyContract(map, callbackSelectMyContract); //db cnt
    }

    //개인계약서 보기
    public void goMyInContractFrm(View v) {
        if(!net())return;
        selectReadContract = "in";
        HashMap<String, String> map = new HashMap<>();
        map.put("mb_idnum", SessionMb.mb_idnum);
        map.put("contract", selectReadContract);
        contractDB.selectMyContract(map, callbackSelectMyContract); //db cnt
    }

    //마루페이
    public void goMaruPay(View v) {
        String url = "https://sugi.ghpayments.kr";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //나의정보btn
    public void goMyInfoFrm(View v){
        if(!net())return;
        Intent intent = new Intent(getApplicationContext(), MyInfoPwCkActivity.class);
        startActivity(intent);
        finish();
    }
    //계약서작성btn
    public void goContractFrm(View v){
        if(!net())return;
        Intent intent = new Intent(getApplicationContext(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    //나의 계약서
    public void goMyContractFrm(View v){
        if(!net())return;
        Intent intent = new Intent(getApplicationContext(), MyContractCkActivity.class);
        startActivity(intent);
        finish();
    }
    public void logout(View v){
       Intent intent = new Intent(getApplication(), LogoutPopupActivity.class);
       startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(), FinishAppActivity.class);
        startActivity(intent);
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
                            if (selectWriteContract.equals("Li")) {
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
                            if (selectWriteContract.equals("Li")) {
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
                            Toast.makeText(LoginMenuActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };


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
                        if(selectReadContract.equals("li")){
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
}