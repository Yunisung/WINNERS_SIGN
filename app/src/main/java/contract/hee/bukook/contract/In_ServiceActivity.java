package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import contract.hee.bukook.HomeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class In_ServiceActivity extends HomeActivity {
    private TextView company;
    private TextView charge;
    private TextView calculate;
    private TextView bs_num;
    private CheckBox[] ckArr;
    private CheckBox in_terminal_1;
    private CheckBox in_terminal_2;
    private CheckBox in_terminal_3;

    private LinearLayout in_sv_cost_layout;
    private RadioButton in_sv_cost_lumpsum;
    private RadioButton in_sv_cost_monthly;


    private ProgressDialog progressDialog;
    private Individual in = new Individual();
    private ContractDB contractDB = ContractDB.getInstance();

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/93269af6c85c43eb823b764f2ba2b5ba";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_service);
        company =findViewById(R.id.company); //회사명
        charge =findViewById(R.id.charge);  //서비스이용 수수료
        calculate =findViewById(R.id.calculate); //정산주기
        bs_num =findViewById(R.id.bs_num); // 사업자등록번호
        in_terminal_1=findViewById(R.id.in_terminal_1);
        //in_terminal_2=findViewById(R.id.in_terminal_2);
        in_terminal_3=findViewById(R.id.in_terminal_3);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        //ckArr = new CheckBox[]{in_terminal_1,in_terminal_2,in_terminal_3};
        ckArr = new CheckBox[]{in_terminal_1,in_terminal_3};

        //통신비 추가
        in_sv_cost_layout = findViewById(R.id.in_sv_cost_Layout);
        in_sv_cost_lumpsum = findViewById(R.id.in_sv_cost_lumpsum);
        in_sv_cost_monthly = findViewById(R.id.in_sv_cost_monthly);

        in_sv_cost_layout.setVisibility(View.GONE);
        in_sv_cost_lumpsum.setChecked(false);
        in_sv_cost_monthly.setChecked(false);

        in_terminal_1.setChecked(false);
        //in_terminal_2.setChecked(false);
        in_terminal_3.setChecked(false);
        goMyContract();
    }
    public void goHome(View v){
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","계약서 작성취소");
        intent.putExtra("text","작성중인 계약서가 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }
    //작성중인 계약서 가져오기
    public void goMyContract() {
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("in_idnum",SessionMb.mb_idnum);
        contractDB.selectMyIndividual(map, callbackSelectMyIndividual);
    }
    Callback callbackSelectMyIndividual = new Callback() {
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
                    Gson gson = new Gson();
                    try {
                        in = gson.fromJson(body.trim(), in.getClass());
                        if("true".equals(in.getResult())){
                            company.setText(in.getIn_sv_company());
                            //charge.setText(in.getIn_sv_charge());
                            calculate.setText(in.getIn_sv_calculate());
                            bs_num.setText(in.getIn_sv_bs_num());
                            if("Y".equals(in.getIn_terminal_1()))   in_terminal_1.setChecked(true);
                            //if("Y".equals(in.getIn_terminal_2()))   in_terminal_2.setChecked(true);
                            if("Y".equals(in.getIn_terminal_3())) {
                                in_terminal_3.setChecked(true);

                                in_sv_cost_layout.setVisibility(View.VISIBLE);

                                if("lumpsum".equals(in.getIn_sv_cost())) {
                                    in_sv_cost_lumpsum.setChecked(true);
                                } else if("monthly".equals(in.getIn_sv_cost())) {
                                    in_sv_cost_monthly.setChecked(true);
                                }

                            }
                        }else{
                            Toast.makeText(In_ServiceActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_ServiceActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public void onClickTerminal2(View v) {
        in_terminal_2.setChecked(false);
    }

    public void onClickTerminal3(View v) {
        if(in_terminal_3.isChecked() == true) {
            in_sv_cost_layout.setVisibility(View.VISIBLE);
        } else {
            in_sv_cost_layout.setVisibility(View.GONE);
        }
    }

    //다음 btn
    public void goInBasicFrm(View v){
        Log.d("TAG", "in_terminal_1: "+in_terminal_1.isChecked());
        //Log.d("TAG", "in_terminal_2: "+in_terminal_2.isChecked());
        Log.d("TAG", "in_terminal_3: "+in_terminal_3.isChecked());
        if(ckBox()) {
            HashMap<String, String> map = new HashMap<>();
            if (in_terminal_1.isChecked())    map.put("in_terminal_1", "Y");
            else                              map.put("in_terminal_1", "N");

            //if (in_terminal_2.isChecked())    map.put("in_terminal_2", "Y");
            //else                              map.put("in_terminal_2", "N");
            map.put("in_terminal_2", "N");

            if (in_terminal_3.isChecked()) {
                map.put("in_terminal_3", "Y");

                if(in_sv_cost_monthly.isChecked()) {
                    map.put("in_sv_cost", "monthly");
                } else {
                    map.put("in_sv_cost", "lumpsum");
                }
            } else {
                map.put("in_terminal_3", "N");
            }

            map.put("in_idnum", SessionMb.mb_idnum);
            map.put("in_seq", in.getIn_seq());
            map.put("in_sv_company", company.getText().toString());
            map.put("in_sv_charge", getString(R.string.chargeValue));
            //map.put("in_sv_charge", charge.getText().toString());
            map.put("in_sv_calculate", calculate.getText().toString());
            map.put("in_sv_bs_num", bs_num.getText().toString());
            map.put("in_admin", SessionMb.mb_id);
            progressDialog.show();
            contractDB.updateIndividual(map, callbackUpdateIndividual);
        }
    }
    Callback callbackUpdateIndividual = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            In_ServiceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        in = gson.fromJson(body.trim(), in.getClass());
                        if("true".equals(in.getResult())){
                            Intent intent = new Intent(getApplicationContext(), In_WriteBasicActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(In_ServiceActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_ServiceActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public boolean ckBox(){
        int num=0;
        for (int i=0; i<ckArr.length; i++){
            if(ckArr[i].isChecked()){
                num+=1;
            }
        }
        Log.d("TAG", "ckBox: "+num);
        if(num==0){
            Toast.makeText(this, "결제수단은 반드시 하나이상을 선택해야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(in_terminal_3.isChecked()) {
            if(in_sv_cost_monthly.isChecked() == false && in_sv_cost_lumpsum.isChecked() == false) {
                Toast.makeText(this, "통신비 청구비용을 선택해야합니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplication(), In_AgreeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}