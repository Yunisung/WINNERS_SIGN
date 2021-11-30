package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import contract.hee.bukook.FilesTask;
import contract.hee.bukook.HomeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.ImgUploadTask;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Admin;
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class In_WriteBankActivity extends HomeActivity {
    private int id;
    private ImageView signPop1;
    private Spinner bank_name;
    private EditText bank_num;
    private EditText depositor;
    private ContractDB contractDB = ContractDB.getInstance();
    private String fileName;
    private String fi_group="0" ;
    private TextView signName1;
    private TextView underName;
    private EditText in_agent;
    private ProgressDialog progressDialog;
    private InputMethodManager manager;
    private Individual in = new Individual();
    private Boolean isFileLoaded;

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/afd0c58478854127b3ed5b65b1380b25";
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
        setContentView(R.layout.activity_in_bank);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        signPop1 = findViewById(R.id.signPop1);
        signName1 = findViewById(R.id.signName1);
        underName = findViewById(R.id.underName);
        bank_num = findViewById(R.id.bank_num);
        depositor = findViewById(R.id.depositor);
        bank_name = findViewById(R.id.bank_name);
        String[] bankNames = getResources().getStringArray(R.array.bank);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, bankNames);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bank_name.setAdapter(adapter);

        in_agent = findViewById(R.id.in_agent);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        isFileLoaded = false;
        showToday(); //오늘날짜 세팅
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
                            String bank_name_val = in.getIn_bank_name();
                            String[] bankNames = getResources().getStringArray(R.array.bank);

                            int spinnerIndex = 0;
                            for(int j =0; j < bankNames.length;j++) {
                                if(bank_name_val.equals(bankNames[j]))
                                    spinnerIndex = j;
                            }
                            bank_name.setSelection(spinnerIndex);
                            bank_num.setText(in.getIn_bank_num());
                            depositor.setText(in.getIn_depositor());
                            signName1.setText(in.getIn_name());
                            underName.setText(in.getIn_name());
                            //in_agent.setText(in.getIn_agent());

                            if (in.getPath7() != "" && in.getPath7() != null) {
                                fileDown(in.getPath7(), signPop1);
                                findViewById(R.id.text1).setVisibility(View.GONE);
                                signPop1.setVisibility(View.VISIBLE);
                                fi_group = "7";
                                isFileLoaded = true;
                            }

                        }else{
                            Toast.makeText(In_WriteBankActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_WriteBankActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    //이미지 다운
    public void fileDown(String url, ImageView img){
        try {
            Bitmap result = new FilesTask().execute(url).get();
            img.setImageBitmap(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sign1(View v){
        id =R.id.signPop1;
        signPop();
    }
    //back
    public void back(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), In_AddFilesActivity.class);
        startActivity(intent);
        finish();
    }
    //완료 btn
    public void goFinishContractFrm(View v){
        String selectedItem = bank_name.getSelectedItem().toString();
        String in_bank_num = bank_num.getText().toString();
        String in_depositor = depositor.getText().toString();
        //널체크
        if (selectedItem.equals("은행명 선택")) {
            Toast.makeText(this, "은행을 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!nullCk()){
            return;
        }
        if(!fi_group.equals("7")){
            Toast.makeText(this, "약관동의에 전자 서명을 하십시오", Toast.LENGTH_SHORT).show();
            id =R.id.signPop1;
            signPop();
            return;
        }
        progressDialog.show();
        Log.d("TAG", " ## goInServiceFrm DB save start");
        HashMap<String,String> map=new HashMap<>();
        map.put("in_idnum", SessionMb.mb_idnum);
        map.put("in_seq", in.getIn_seq());
        map.put("in_bank_name",selectedItem);
        map.put("in_bank_num",in_bank_num);
        map.put("in_depositor",in_depositor);
        map.put("in_name", signName1.getText().toString());
        //map.put("in_agent",in_agent.getText().toString());
        contractDB.updateIndividual(map, callbackUpdateIndividual);
    }

    public boolean nullCk(){
        String depositorResult = PatternAdd.validateName(depositor.getText().toString().trim());
        String signNameResult = PatternAdd.validateName(signName1.getText().toString().trim());
        //정산계좌정보 null
        if(bank_num.getText().toString().trim().length()==0){
            Toast.makeText(this, "계좌번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            bank_num.requestFocus();
            manager.showSoftInput(bank_num, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!PatternAdd.validateBankNum(bank_num.getText().toString().trim())){
            Toast.makeText(this, "올바른 계좌번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            bank_num.requestFocus();
            manager.showSoftInput(bank_num, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(depositor.getText().toString().trim().length()== 0){
            Toast.makeText(this, "예금주를 입력해 주세요", Toast.LENGTH_SHORT).show();
            depositor.requestFocus();
            manager.showSoftInput(depositor, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!depositorResult.equals("ok")){
            Toast.makeText(this, depositorResult, Toast.LENGTH_SHORT).show();
            depositor.requestFocus();
            manager.showSoftInput(depositor, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(signName1.getText().toString().trim().length()==0) {
            Toast.makeText(this, "서명이 필요합니다.", Toast.LENGTH_SHORT).show();
            signName1.requestFocus();
            manager.showSoftInput(signName1, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } if(!signNameResult.equals("ok")){
                Toast.makeText(this, signNameResult, Toast.LENGTH_SHORT).show();
                signName1.requestFocus();
            manager.showSoftInput(signName1, InputMethodManager.SHOW_IMPLICIT);
                return false;
        }if(underName.getText().toString().trim().length()==0){
            Toast.makeText(this, "신청인을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            underName.requestFocus();
            manager.showSoftInput(underName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }if(!signName1.getText().toString().trim().equals(underName.getText().toString().trim())){
            Toast.makeText(this, "서명과 신청인이 일치해야합니다.", Toast.LENGTH_SHORT).show();
            underName.requestFocus();
            manager.showSoftInput(underName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
//        if(in_agent.getText().toString().trim().length()==0 ) {
//            Toast.makeText(this, "담당자를 입력해주세요.", Toast.LENGTH_SHORT).show();
//            in_agent.requestFocus();
//            manager.showSoftInput(in_agent, InputMethodManager.SHOW_IMPLICIT);
//            return false;
//        }
        return true;
    }

    Callback callbackUpdateIndividual = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            In_WriteBankActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        in = gson.fromJson(body.trim(), in.getClass());
                        if("true".equals(in.getResult())){
                            String imagePath = getCacheDir().toString()+"/"+fileName;

                            if (isFileLoaded == true) {
                                Intent intent = new Intent(getApplicationContext(), In_Before_FinishActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                if(fileUp(imagePath,fi_group, in.getIn_seq())){
                                    Intent intent = new Intent(getApplicationContext(), In_Before_FinishActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(In_WriteBankActivity.this, "오류가 발생했습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                        }else{
                            Toast.makeText(In_WriteBankActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_WriteBankActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public boolean fileUp(String imagePath,String fileGroup,String seq){
        boolean result=false;
        String  ImageUploadURL = Admin.sic_url+"/app/Appdbconn?page=imgUpload";

        try {
            result =new ImgUploadTask().execute(ImageUploadURL,imagePath,fileGroup,seq).get();
            Log.d("TAG", "ImgUploadTask||result: "+result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


    //서명클릭시->팝업창
    public void signPop() {
        Intent intent = new Intent(getApplication(), SignPopupActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                byte[] bytes = data.getByteArrayExtra("sign");
                String getfileName = data.getExtras().get("fileName").toString();
                Bitmap sign = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (SignDrow.isDrow() != 1) {
                    fi_group = "0";
                    findViewById(R.id.text1).setVisibility(View.VISIBLE);
                    signPop1.setVisibility(View.GONE);
                    return;
                }
                switch (id) {
                    case R.id.signPop1:
                        signPop1.setImageBitmap(sign);
                        fileName = getfileName;
                        fi_group = "7";
                        //성공시
                        findViewById(R.id.text1).setVisibility(View.GONE);
                        signPop1.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    public void showToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date date = new Date();
        String strDate = simpleDateFormat.format(date);
        ((TextView) findViewById(R.id.today)).setText(strDate);
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
        switch (v.getId()) {
            case R.id.banknum:
                bank_num.requestFocus();
                manager.showSoftInput(bank_num, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.depositorLy:
                depositor.requestFocus();
                manager.showSoftInput(depositor, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }
}