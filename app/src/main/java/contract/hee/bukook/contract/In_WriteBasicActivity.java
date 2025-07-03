package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import contract.hee.bukook.HomeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.DaumWebViewActivity;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class In_WriteBasicActivity extends HomeActivity {
    private TextView name;
    private EditText company;
    private TextView admin;
    private EditText item; //판매품목 추가
    private EditText phone;
    private EditText rrn;
    private TextView zipcode;
    private TextView addr1;
    private EditText addr2;
    private EditText tel;
    private final static int TAKE_ADDR = 1;
    private InputMethodManager manager;
    private ContractDB contractDB = ContractDB.getInstance();
    private ProgressDialog progressDialog;
    private Individual in = new Individual();

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/fe96466eb20b4d75be1a163b66b86678";
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
        setContentView(R.layout.activity_in_write_basic);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        name = findViewById(R.id.name);
        company =findViewById(R.id.company);
        admin =findViewById(R.id.admin);
        item = findViewById(R.id.item);
        phone =findViewById(R.id.phone);
        rrn =findViewById(R.id.rrn);
        zipcode =findViewById(R.id.zipcode);
        addr1 =findViewById(R.id.addr1);
        addr2 =findViewById(R.id.addr2);
        tel =findViewById(R.id.tel);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        goMyContract();
        item.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(getApplication(), DaumWebViewActivity.class);
                    startActivityForResult(intent,TAKE_ADDR);
                    return true;
                }
                return false;
            }
        });
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
                            name.setText(in.getIn_name());
                            company.setText(in.getIn_company());
                            admin.setText(in.getIn_admin());
                            item.setText(in.getIn_item());
                            phone.setText(in.getIn_phone());
                            rrn.setText(in.getIn_rrn());
                            zipcode.setText(in.getIn_zipcode());
                            addr1.setText(in.getIn_addr1());
                            addr2.setText(in.getIn_addr2());
                            tel.setText(in.getIn_tel());
                        }else{
                            Toast.makeText(In_WriteBasicActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_WriteBasicActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progressDialog.dismiss();
        }
    };
    //다음 btn
    public void goInfilesFrm(View v){
        if(nullCk()) {
            progressDialog.show();
            HashMap<String,String> map=new HashMap<>();
            map.put("in_idnum"  , SessionMb.mb_idnum);
            map.put("in_seq", in.getIn_seq());
            map.put("in_name",name.getText().toString());
            map.put("in_company",company.getText().toString());
            map.put("in_admin",admin.getText().toString());
            map.put("in_item", item.getText().toString());
            map.put("in_phone",phone.getText().toString());
            map.put("in_rrn",rrn.getText().toString());
            map.put("in_zipcode",zipcode.getText().toString());
            map.put("in_addr1",addr1.getText().toString());
            map.put("in_addr2",addr2.getText().toString());
            map.put("in_tel",tel.getText().toString());
            Log.d("IN_WriteBasic", "Data : " + map.toString());
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
            In_WriteBasicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        in = gson.fromJson(body.trim(), in.getClass());
                        if("true".equals(in.getResult())){
                            Intent intent = new Intent(getApplicationContext(), In_AddFilesActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(In_WriteBasicActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_WriteBasicActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progressDialog.dismiss();
        }
    };
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), In_ServiceActivity.class);
        startActivity(intent);
        finish();
    }
    //우편번호 검색
    public void findAddr(View v){
       Intent intent = new Intent(getApplication(), DaumWebViewActivity.class);
        startActivityForResult(intent,TAKE_ADDR);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                zipcode.setText(data.getBundleExtra("addr").get("zipcode").toString());
                addr1.setText(data.getBundleExtra("addr").get("addr1").toString());
                addr2.requestFocus();
                manager.showSoftInput(addr2, InputMethodManager.SHOW_IMPLICIT);
        }else {
            Toast.makeText(this, "주소를 불러올 수 없습니다. 다시시도해주세요.", Toast.LENGTH_SHORT).show();
           return;
        }
    }
    public boolean nullCk() {
        String nameResult = PatternAdd.validateName(name.getText().toString().trim());
        String companyResult =PatternAdd.validateText(company.getText().toString().trim());

        if(name.getText().toString().trim().length() == 0){
            Toast.makeText(this, "성명을 입력해 주세요", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (!nameResult.equals("ok")) {
            Toast.makeText(this, nameResult, Toast.LENGTH_SHORT).show();
            name.requestFocus();
            manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(company.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "상호명을 입력해 주세요", Toast.LENGTH_SHORT).show();
            company.requestFocus();
            manager.showSoftInput(company, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!companyResult.equals("ok")){
            companyResult="상호명은"+companyResult;
            Toast.makeText(this, companyResult, Toast.LENGTH_SHORT).show();
            company.requestFocus();
            manager.showSoftInput(company, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(phone.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "연락처를 입력해 주세요", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (!PatternAdd.validatePhone(phone.getText().toString().trim())) {
            Toast.makeText(this, "올바른 전화번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(rrn.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "주민등록번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
            rrn.requestFocus();
            manager.showSoftInput(rrn, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (!PatternAdd.validateRrn(rrn.getText().toString())) {
            Toast.makeText(this, "올바른 주민등록번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            rrn.requestFocus();
            manager.showSoftInput(rrn, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }

        if (item.getText().toString().length() == 0 ) {
            Toast.makeText(this, "판매품목을 입력해주세요", Toast.LENGTH_SHORT).show();
            item.requestFocus();
            manager.showSoftInput(item, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }

        if(zipcode.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "우편번호를 검색해 주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), DaumWebViewActivity.class);
            startActivityForResult(intent,TAKE_ADDR);
            return false;
        }
        if(addr2.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "상세주소를 입력해 주세요", Toast.LENGTH_SHORT).show();
            addr2.requestFocus();
            manager.showSoftInput(addr2, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(tel.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "사업장 연락처를 입력해 주세요", Toast.LENGTH_SHORT).show();
            tel.requestFocus();
            manager.showSoftInput(tel, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (!PatternAdd.validateTel(tel.getText().toString().trim())) {
            Toast.makeText(this, "올바른 연락처 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            tel.requestFocus();
            manager.showSoftInput(tel, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        return true;
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
    public void clickView(View v) {
        if(v.getId() == R.id.nameLy) {
            name.requestFocus();
            manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.companyLy) {
            company.requestFocus();
            manager.showSoftInput(company, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.phoneLy) {
            phone.requestFocus();
            manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.rrnLy) {
            rrn.requestFocus();
            manager.showSoftInput(rrn, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.itemLy) {
            item.requestFocus();
            manager.showSoftInput(item, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.addr2Ly) {
            addr2.requestFocus();
            manager.showSoftInput(addr2, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.telLy) {
            tel.requestFocus();
            manager.showSoftInput(tel, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
