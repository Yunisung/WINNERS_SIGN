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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class Li_WriteServiceActivity extends HomeActivity {
    //private Spinner li_sv_divide;
    private CheckBox li_service_1;
    private CheckBox li_service_2;
    private CheckBox li_service_3;
    private CheckBox li_service_4;
    private TextView li_sv_charge;
    private TextView li_sv_calculate;

    private LinearLayout li_sv_cost_Layout;
    private RadioButton li_sv_cost_lumpsum;
    private RadioButton li_sv_cost_monthly;

    private ContractDB contractDB = ContractDB.getInstance();
    private CheckBox li_terminal_1;
    private CheckBox li_terminal_2;
    private CheckBox li_terminal_3;
    private CheckBox[] ckArr;
    private ProgressDialog progressDialog;
    private Licensee li = new Licensee();

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li__write_service);
        //li_sv_divide = (Spinner)findViewById(R.id.li_sv_divide);
        li_service_1 = findViewById(R.id.toggle_service_1);
        //li_service_2 = findViewById(R.id.toggle_service_2);
        //li_service_3 = findViewById(R.id.toggle_service_3);
        //li_service_4 = findViewById(R.id.toggle_service_4);

        li_sv_charge = findViewById(R.id.li_sv_charge);
        li_sv_calculate =findViewById(R.id.li_sv_calculate);
        li_terminal_1=findViewById(R.id.li_terminal_1);
        //li_terminal_2=findViewById(R.id.li_terminal_2);
        li_terminal_3=findViewById(R.id.li_terminal_3);
        //ckArr= new CheckBox[]{li_terminal_1,li_terminal_2,li_terminal_3};
        ckArr= new CheckBox[]{li_terminal_1,li_terminal_3};

        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");

        li_terminal_1.setChecked(false);
        //li_terminal_2.setChecked(false);
        li_terminal_3.setChecked(false);

        //통신비추가
        li_sv_cost_lumpsum = findViewById(R.id.li_sv_cost_lumpsum);
        li_sv_cost_monthly = findViewById(R.id.li_sv_cost_monthly);
        li_sv_cost_Layout = findViewById(R.id.li_sv_cost_Layout);

        li_sv_cost_lumpsum.setChecked(false);
        li_sv_cost_monthly.setChecked(false);
        li_sv_cost_Layout.setVisibility(View.GONE);

        goMyContract();
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/ebaedf3ae73546c1abaa39040110b1f3";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    //작성중인 계약서 가져오기
    public void goMyContract() {
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("li_idnum",SessionMb.mb_idnum);
        contractDB.selectMyLicensee(map, callbackSelectMyLicensee);
    }
    Callback callbackSelectMyLicensee = new Callback() {
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
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){

                            String li_sv_divide_val = li.getLi_sv_divide();
                            String[] li_sv_divideNames = getResources().getStringArray(R.array.li_sv_divide);

                            int spinnerIndex = 0;

                            if(li_sv_divide_val.equals("1")) {
                                li_sv_divide_val = "신용카드";
                                li_service_1.setChecked(true);
                                //li_service_2.setChecked(false);
                                //li_service_3.setChecked(false);
                                //li_service_4.setChecked(false);
                            }
//                            else if(li_sv_divide_val.equals("2"))   {
//                                li_sv_divide_val = "가상계좌";
//                                li_service_1.setChecked(false);
//                                li_service_2.setChecked(true);
//                                li_service_3.setChecked(false);
//                                li_service_4.setChecked(false);
//                            }
//                            else if(li_sv_divide_val.equals("3"))    {
//                                li_sv_divide_val = "계좌이체";
//                                li_service_1.setChecked(false);
//                                li_service_2.setChecked(false);
//                                li_service_3.setChecked(true);
//                                li_service_4.setChecked(false);
//                            }
//                            else  if(li_sv_divide_val.equals("4")) {
//                                li_sv_divide_val = "휴대폰결제";
//                                li_service_1.setChecked(false);
//                                li_service_2.setChecked(false);
//                                li_service_3.setChecked(false);
//                                li_service_4.setChecked(true);
//                            }
                            else   li_sv_divide_val = "선택";

                            for(int j =0; j < li_sv_divideNames.length;j++) {
                                if(li_sv_divide_val.equals(li_sv_divideNames[j])) {
                                    spinnerIndex = j;

                                }
                            }
                            //li_sv_divide.setSelection(spinnerIndex);
                            //li_sv_charge.setText(li.getLi_sv_charge());
                            li_sv_calculate.setText(li.getLi_sv_calculate());

                            if("Y".equals(li.getLi_terminal_1()))   li_terminal_1.setChecked(true);
                            //if("Y".equals(li.getLi_terminal_2()))   li_terminal_2.setChecked(true);
                            if("Y".equals(li.getLi_terminal_3())) {
                                li_terminal_3.setChecked(true);

                                li_sv_cost_Layout.setVisibility(View.VISIBLE);

                                if("lumpsum".equals(li.getLi_sv_cost())) {
                                    li_sv_cost_lumpsum.setChecked(true);
                                } else if("monthly".equals(li.getLi_sv_cost())) {
                                    li_sv_cost_monthly.setChecked(true);
                                }
                            }
                        }else{
                            Toast.makeText(Li_WriteServiceActivity.this, "계약서 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_WriteServiceActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public int CheckService() {
        if (li_service_1.isChecked())
            return 1;
//        else if (li_service_2.isChecked())
//            return 2;
//        else if (li_service_3.isChecked())
//            return 3;
//        else if (li_service_4.isChecked())
//            return 4;

        return 0;
    }

    public void onClickService1(View v) {
        Log.d("ddd", "service1");
        //li_service_2.setChecked(false);
        //li_service_3.setChecked(false);
        //li_service_4.setChecked(false);
    }

    public void onClickService2(View v) {
        Log.d("ddd", "service2");
        //li_service_1.setChecked(false);
        //li_service_2.setChecked(false);
        //li_service_3.setChecked(false);
        //li_service_4.setChecked(false);
    }

    public void onClickService3(View v) {
        Log.d("ddd", "service3");
        //li_service_1.setChecked(false);
        //li_service_2.setChecked(false);
        //li_service_3.setChecked(false);
        //li_service_4.setChecked(false);
    }

    public void onClickService4(View v) {
        Log.d("ddd", "service4");
        //li_service_1.setChecked(false);
        //li_service_2.setChecked(false);
        //li_service_3.setChecked(false);
        //li_service_4.setChecked(false);
    }

    public void onClickTerminal2(View v) {
        li_terminal_2.setChecked(false);
    }

    public void onClickTerminal3(View v) {
        if(li_terminal_3.isChecked() == true) {
            li_sv_cost_Layout.setVisibility(View.VISIBLE);
        } else {
            li_sv_cost_Layout.setVisibility(View.GONE);
        }
    }

    //다음 btn
    public void goLiBasicFrm(View v){
        String selectedItem = String.format("%d", CheckService());
        //널체크
        if (CheckService() == 0) {
            Toast.makeText(this, "서비스구분을 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ckBox()) {
            HashMap<String, String> map = new HashMap<>();
            if (li_terminal_1.isChecked())     map.put("li_terminal_1", "Y");
            else                               map.put("li_terminal_1", "N");

//            if (li_terminal_2.isChecked())     map.put("li_terminal_2", "Y");
//            else                               map.put("li_terminal_2", "N");
            map.put("li_terminal_2", "N");

            if (li_terminal_3.isChecked()) {
                map.put("li_terminal_3", "Y");

                if(li_sv_cost_monthly.isChecked()) {
                    map.put("li_sv_cost", "monthly");
                } else if(li_sv_cost_lumpsum.isChecked()) {
                    map.put("li_sv_cost", "lumpsum");
                }

            } else {
                map.put("li_terminal_3", "N");
            }

            if(CheckService() == 1)           selectedItem = "1";
//            else if(CheckService() == 2)      selectedItem = "2";
//            else if(CheckService() == 3)      selectedItem = "3";
//            else                                         selectedItem = "4";

            map.put("li_idnum", SessionMb.mb_idnum);
            map.put("li_seq", li.getLi_seq());
            map.put("li_sv_divide",selectedItem);
            //map.put("li_sv_charge",li_sv_charge.getText().toString());
            map.put("li_sv_charge",getString(R.string.chargeValue));
            map.put("li_sv_calculate",li_sv_calculate.getText().toString());
            map.put("li_admin",SessionMb.mb_id);
            Log.d("TAG", "map: "+map.toString());
            progressDialog.show();
            contractDB.updateLicensee(map, callbackUpdateLicensee);
        }
    }
    Callback callbackUpdateLicensee = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Li_WriteServiceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){
                            Intent intent = new Intent(getApplicationContext(), Li_WriteBasicActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Li_WriteServiceActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_WriteServiceActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
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

        if(li_terminal_3.isChecked()) {
            if(li_sv_cost_lumpsum.isChecked() == false && li_sv_cost_monthly.isChecked() == false) {
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
        Intent intent = new Intent(getApplicationContext(), Li_AgreeActivity.class);
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

    public void goHome(View view) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","계약서 작성취소");
        intent.putExtra("text","작성중인 계약서가 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }
}