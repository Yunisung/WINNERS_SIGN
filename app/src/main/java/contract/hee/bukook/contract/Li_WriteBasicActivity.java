package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class Li_WriteBasicActivity extends HomeActivity {
    private Licensee li = new Licensee();
    private ContractDB contractDB = ContractDB.getInstance();
    private TextView li_name;
    private EditText li_company;
    private TextView li_admin;
    private EditText li_bs_num;
    private EditText li_tel;
    private EditText li_cindition;
    private EditText li_event;
    private TextView li_zipcode;
    private TextView li_addr1;
    private EditText li_addr2;
    private EditText li_re_name;
    private EditText li_re_birth;
    private EditText li_re_phone;
    private EditText li_re_email;
    private final static int TAKE_ADDR = 1;
    private RadioGroup li_bs_divide;
    private int radioBtnId;
    private ProgressDialog progressDialog;
    private InputMethodManager manager ;

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li__write_basic);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        li_company =findViewById(R.id.li_company);
        li_name = findViewById(R.id.li_name);
        li_admin =findViewById(R.id.li_admin);
        li_bs_num = findViewById(R.id.li_bs_num);
        li_tel =findViewById(R.id.li_tel);
        li_cindition =findViewById(R.id.li_cindition);
        li_event =findViewById(R.id.li_event);
        li_zipcode = findViewById(R.id.li_zipcode);
        li_addr1 =findViewById(R.id.li_addr1);
        li_addr2 =findViewById(R.id.li_addr2);
        li_re_name =findViewById(R.id.li_re_name);
        li_re_birth =findViewById(R.id.li_re_birth);
        li_re_email =findViewById(R.id.li_re_email);
        li_re_phone =findViewById(R.id.li_re_phone);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        Log.d("TAG", "SessionMb.mb_id: "+SessionMb.mb_id);
        goMyContract();
        //엔터키 방어
        li_event.setOnKeyListener(new View.OnKeyListener() {
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

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/d46d3f86ee0d49e5afc37a65f72603a4";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void goLiWriteHome(View v){
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
                            li_name.setText(SessionMb.mb_name);
                            li_company.setText(li.getLi_company());
                            li_admin.setText(li.getLi_admin());
                            li_bs_num.setText(li.getLi_bs_num());
                            li_tel.setText(li.getLi_tel());
                            li_cindition.setText(li.getLi_cindition());
                            li_event.setText(li.getLi_event());
                            li_zipcode.setText(li.getLi_zipcode());
                            li_addr1.setText(li.getLi_addr1());
                            li_addr2.setText(li.getLi_addr2());
                            li_re_name.setText(li.getLi_re_name());
                            li_re_birth.setText(li.getLi_re_birth());
                            li_re_phone.setText(li.getLi_re_phone());
                            li_re_email.setText(li.getLi_re_email());

                            //사업자 구분
                            String li_bs_divide=(li.getLi_bs_divide());
                            RadioButton radioButton;
                            switch (li_bs_divide){
                                case "1":
                                    radioButton = findViewById(R.id.li_bs_divide_1);
                                    radioButton.setChecked(true);
                                    break;
                                case "2":
                                    radioButton = findViewById(R.id.li_bs_divide_2);
                                    radioButton.setChecked(true);
                                    break;
                                default:
                                    break;
                            }
                        }else{
                            Toast.makeText(Li_WriteBasicActivity.this, "계약서 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_WriteBasicActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };
    //다음 btn
    public void goLifilesFrm(View v){
       String li_bs_divide="";
        if(nullCk()) {
            HashMap<String, String> map = new HashMap<>();
            if(radioBtnId == R.id.li_bs_divide_1) {
                li_bs_divide="1";
            } else if(radioBtnId == R.id.li_bs_divide_2) {
                li_bs_divide="2";
            }

            progressDialog.show();
            map.put("li_idnum", SessionMb.mb_idnum);
            map.put("li_seq", li.getLi_seq());
            map.put("li_company", li_company.getText().toString());
            map.put("li_admin", li_admin.getText().toString());
            map.put("li_bs_num", li_bs_num.getText().toString());
            map.put("li_tel", li_tel.getText().toString());
            map.put("li_cindition", li_cindition.getText().toString());
            map.put("li_event", li_event.getText().toString());
            map.put("li_zipcode", li_zipcode.getText().toString());
            map.put("li_addr1", li_addr1.getText().toString());
            map.put("li_addr2", li_addr2.getText().toString());
            map.put("li_re_name", li_re_name.getText().toString());
            map.put("li_re_birth", li_re_birth.getText().toString());
            map.put("li_re_phone", li_re_phone.getText().toString());
            map.put("li_re_email", li_re_email.getText().toString());
            map.put("li_bs_divide", li_bs_divide);
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
            Li_WriteBasicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){
                            Intent intent = new Intent(getApplicationContext(), Li_AddFilesActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Li_WriteBasicActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_WriteBasicActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public boolean nullCk(){
        String nameResult =PatternAdd.validateName(li_re_name.getText().toString().trim());
        String companyResult =PatternAdd.validateText(li_company.getText().toString().trim());
        String cinditionResult=PatternAdd.validateText(li_cindition.getText().toString().trim());
        String eventResult=PatternAdd.validateText(li_event.getText().toString().trim());
        li_bs_divide=findViewById(R.id.li_bs_divide);
        int rId=li_bs_divide.getCheckedRadioButtonId();
        radioBtnId = rId;
        //널체크
        if (li_company.getText().toString().length()==0) {
            Toast.makeText(this, "상호명을 입력해주세요", Toast.LENGTH_SHORT).show();
            li_company.requestFocus();
            manager.showSoftInput(li_company, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!companyResult.equals("ok")){
            companyResult="상호명은"+companyResult;
            Toast.makeText(this, companyResult, Toast.LENGTH_SHORT).show();
            li_company.requestFocus();
            manager.showSoftInput(li_company, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_bs_num.getText().toString().length()==0) {
            Toast.makeText(this, "사업자 등록번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_bs_num.requestFocus();
            manager.showSoftInput(li_bs_num, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }if(!bsNumCk(li_bs_num.getText().toString().trim())){
            Log.d("TAG", "bsNumCk: "+bsNumCk(li_bs_num.getText().toString().trim()));
            li_bs_num.requestFocus();
            manager.showSoftInput(li_bs_num, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(rId<0){
            Toast.makeText(this, "사업자 구분을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (li_tel.getText().toString().length()==0) {
            Toast.makeText(this, "대표번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_tel.requestFocus();
            manager.showSoftInput(li_tel, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }if(!PatternAdd.validateTel(li_tel.getText().toString().trim())){
            Toast.makeText(this, "올바른 대표번호의 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            li_tel.requestFocus();
            manager.showSoftInput(li_tel, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_cindition.getText().toString().length()==0) {
            Toast.makeText(this, "업태를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_cindition.requestFocus();
            manager.showSoftInput(li_cindition, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!cinditionResult.equals("ok")){
            cinditionResult="업태는"+cinditionResult;
            Toast.makeText(this, cinditionResult, Toast.LENGTH_SHORT).show();
            li_cindition.requestFocus();
            manager.showSoftInput(li_cindition, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_event.getText().toString().length()==0) {
            Toast.makeText(this, "종목을 입력해주세요", Toast.LENGTH_SHORT).show();
            li_event.requestFocus();
            manager.showSoftInput(li_event, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!eventResult.equals("ok")){
            eventResult="종목은"+eventResult;
            Toast.makeText(this, eventResult, Toast.LENGTH_SHORT).show();
            li_event.requestFocus();
            manager.showSoftInput(li_event, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_zipcode.getText().toString().length()==0) {
            Toast.makeText(this, "우편번호를 검색해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), DaumWebViewActivity.class);
            startActivityForResult(intent,TAKE_ADDR);
            return false;
        }
        if (li_addr1.getText().toString().length()==0) {
            Toast.makeText(this, "기본주소를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_addr1.requestFocus();
            return false;
        }
        if (li_addr2.getText().toString().length()==0) {
            Toast.makeText(this, "상세주소를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_addr2.requestFocus();
            manager.showSoftInput(li_addr2, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_re_name.getText().toString().length()==0) {
            Toast.makeText(this, "대표자 성명을 입력해주세요", Toast.LENGTH_SHORT).show();
            li_re_name.requestFocus();
            manager.showSoftInput(li_re_name, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }if(!nameResult.equals("ok")) {
            Toast.makeText(this, nameResult, Toast.LENGTH_SHORT).show();
            li_re_name.requestFocus();
            manager.showSoftInput(li_re_name, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_re_birth.getText().toString().length()==0) {
            Toast.makeText(this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
            li_re_birth.requestFocus();
            manager.showSoftInput(li_re_birth, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (!PatternAdd.validateBirth(li_re_birth.getText().toString().trim())) {
            Toast.makeText(this, "생년월일을 다시한번 확인해 주세요", Toast.LENGTH_SHORT).show();
            li_re_birth.requestFocus();
            manager.showSoftInput(li_re_birth, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (li_re_phone.getText().toString().length()==0) {
            Toast.makeText(this, "연락처를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_re_phone.requestFocus();
            manager.showSoftInput(li_re_phone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!PatternAdd.validatePhone(li_re_phone.getText().toString().trim())){
            Toast.makeText(this, "올바른 연락처의 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            li_re_phone.requestFocus();
            manager.showSoftInput(li_re_phone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }

        return true;
    }
    public boolean bsNumCk(String bsNum){
        Log.d("TAG", "bsNumCk: "+bsNum);
        Log.d("TAG", "bsNum.length(): "+bsNum.length());
        if(bsNum.length()!=10){
            Toast.makeText(this, "사업자등록번호는 10자리입니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void findAddr(View v){
        Intent intent = new Intent(getApplication(), DaumWebViewActivity.class);
        startActivityForResult(intent,TAKE_ADDR);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            li_zipcode.setText(data.getBundleExtra("addr").get("zipcode").toString());
            li_addr1.setText(data.getBundleExtra("addr").get("addr1").toString());
            li_addr2.requestFocus();
            manager.showSoftInput(li_addr2, InputMethodManager.SHOW_IMPLICIT);
        }else {
            Toast.makeText(this, "주소를 불러올 수 없습니다. 다시시도해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Li_WriteServiceActivity.class);
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


    public void clickView(View v) {

        if(v.getId() == R.id.company) {
            li_company.requestFocus();
            manager.showSoftInput(li_company, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.bsnum) {
            li_bs_num.requestFocus();
            manager.showSoftInput(li_bs_num, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.tel) {
            li_tel.requestFocus();
            manager.showSoftInput(li_tel, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.cindition) {
            li_cindition.requestFocus();
            manager.showSoftInput(li_cindition, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.event) {
            li_event.requestFocus();
            manager.showSoftInput(li_event, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.addr2) {
            li_addr2.requestFocus();
            manager.showSoftInput(li_addr2, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.rename) {
            li_re_name.requestFocus();
            manager.showSoftInput(li_re_name, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.rebirth) {
            li_re_birth.requestFocus();
            manager.showSoftInput(li_re_birth, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.rephone) {
            li_re_phone.requestFocus();
            manager.showSoftInput(li_re_phone, InputMethodManager.SHOW_IMPLICIT);
        } else if(v.getId() == R.id.reemail) {
            li_re_email.requestFocus();
            manager.showSoftInput(li_re_email, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}