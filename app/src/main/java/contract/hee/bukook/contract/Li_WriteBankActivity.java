package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class Li_WriteBankActivity extends HomeActivity {
    private Spinner li_bank_name_text;
    private EditText li_bank_num_text;
    private EditText li_depositor_text;
    private EditText li_agent_text;
    private Licensee li = new Licensee();
    private ContractDB contractDB = ContractDB.getInstance();
    private String fileName ;
    private String fi_group ="0";
    private TextView signName1;
    private TextView underName;
    private EditText li_agent;
    private ImageView signPop1;
    private int id;
    private ProgressDialog progressDialog;
    private InputMethodManager manager;
    private Boolean isFileLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li_bank);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        signPop1 = findViewById(R.id.signPop1);
        signName1 = findViewById(R.id.signName1);
        underName = findViewById(R.id.underName);
        li_agent = findViewById(R.id.li_agent);
        li_bank_name_text = findViewById(R.id.li_bank_name);
        String[] bankNames = getResources().getStringArray(R.array.bank);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, bankNames);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        li_bank_name_text.setAdapter(adapter);


        li_bank_num_text =findViewById(R.id.li_bank_num);
        li_depositor_text =findViewById(R.id.li_depositor);
        li_agent_text =findViewById(R.id.li_agent);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        isFileLoaded = false;
        showToday();
        goMyContract();

    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/b8581f81919f46daa9fc0dbf9d9d4ed5";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }

    //작성중인 계약서 가져오기
    public void goMyContract() {
        progressDialog.show();
        Log.d("TAG", "goMyContract() SessionMb.mb_idnum returned: " + SessionMb.mb_idnum);
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
                            String li_bank_name_val = li.getLi_bank_name();
                            String[] bankNames = getResources().getStringArray(R.array.bank);

                            int spinnerIndex = 0;
                            for(int j =0; j < bankNames.length;j++) {
                                if(li_bank_name_val.equals(bankNames[j]))
                                    spinnerIndex = j;
                            }
                            li_bank_name_text.setSelection(spinnerIndex);
                            li_bank_num_text.setText(li.getLi_bank_num());
                            li_depositor_text.setText(li.getLi_depositor());
                            signName1.setText(li.getLi_name());
                            underName.setText(li.getLi_name());
                            //li_agent.setText(li.getLi_agent());

                            if (li.getPath7() != "" && li.getPath7() != null) {
                                fileDown(li.getPath7(), signPop1);
                                findViewById(R.id.text1).setVisibility(View.GONE);
                                signPop1.setVisibility(View.VISIBLE);
                                fi_group = "7";
                                isFileLoaded = true;
                            }

                        }else{
                            Toast.makeText(Li_WriteBankActivity.this, "계약서 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_WriteBankActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
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

    //완료 btn
    public void goBeforeContractFrm(View v) {
        String selectedItem = li_bank_name_text.getSelectedItem().toString();
        String li_bank_num = li_bank_num_text.getText().toString();
        String li_depositor = li_depositor_text.getText().toString();
        String li_agent = li_agent_text.getText().toString();
        String depositorResult = PatternAdd.validateName(li_depositor.trim());
        String signNameResult = PatternAdd.validateName(signName1.getText().toString().trim());
        //널체크
        if (selectedItem.equals("은행명 선택")) {
            Toast.makeText(this, "은행을 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (li_bank_num.trim().length()==0) {
            Toast.makeText(this, "계좌번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_bank_num_text.requestFocus();
            manager.showSoftInput(li_bank_num_text, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if(!PatternAdd.validateBankNum(li_bank_num.trim())){
            Toast.makeText(this, "올바른 계좌번호가 아닙니다.", Toast.LENGTH_SHORT).show();
            li_bank_num_text.requestFocus();
            manager.showSoftInput(li_bank_num_text, InputMethodManager.SHOW_IMPLICIT);
            return;
    }
        if (li_depositor.trim().length()==0) {
            Toast.makeText(this, "예금주를 입력해주세요", Toast.LENGTH_SHORT).show();
            li_depositor_text.requestFocus();
            manager.showSoftInput(li_depositor_text, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if(!depositorResult.equals("ok")){
            Toast.makeText(this, depositorResult, Toast.LENGTH_SHORT).show();
            li_depositor_text.requestFocus();
            manager.showSoftInput(li_depositor_text, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if(signName1.getText().toString().trim().length()==0) {
            Toast.makeText(this, "서명이 필요합니다.", Toast.LENGTH_SHORT).show();
            signName1.requestFocus();
            manager.showSoftInput(signName1, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if(!signNameResult.equals("ok")){
            Toast.makeText(this, signNameResult, Toast.LENGTH_SHORT).show();
            signName1.requestFocus();
            manager.showSoftInput(signName1, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if(underName.getText().toString().trim().length()==0){
            Toast.makeText(this, "신청인을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            underName.requestFocus();
            manager.showSoftInput(underName, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        if(!signName1.getText().toString().trim().equals(underName.getText().toString().trim())){
            Toast.makeText(this, "서명과 신청인이 일치해야합니다.", Toast.LENGTH_SHORT).show();
            underName.requestFocus();
            manager.showSoftInput(underName, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
//        if(li_agent.trim().length()==0){
//            Toast.makeText(this, "담당자를 입력해 주세요.", Toast.LENGTH_SHORT).show();
//            li_agent_text.requestFocus();
//            manager.showSoftInput(li_agent_text, InputMethodManager.SHOW_IMPLICIT);
//            return;
//        }

        if (!fi_group.equals("7")) {
            Toast.makeText(this, "전자 서명을 완료해 주세요", Toast.LENGTH_SHORT).show();
            id = R.id.signPop1;
            signPop();
            return;
        }
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("li_idnum", SessionMb.mb_idnum);
        map.put("li_seq", li.getLi_seq());
        map.put("li_bank_name",selectedItem);
        map.put("li_bank_num",li_bank_num);
        map.put("li_depositor",li_depositor);
        //map.put("li_agent",li_agent);
        contractDB.updateLicensee(map, callbackUpdateLicensee);
    }
    Callback callbackUpdateLicensee = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Li_WriteBankActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){
                            Log.d("TAG", "fi_group: "+fi_group);
                            String imagePath = getCacheDir().toString()+"/"+fileName;

                            //사진로드완성일때는 파일업로드 안함
                            if (isFileLoaded == true) {
                                Intent intent = new Intent(getApplicationContext(), Li_Before_FinishActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                if(fileUp(imagePath, fi_group, li.getLi_seq())){
                                    Intent intent = new Intent(getApplicationContext(), Li_Before_FinishActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(Li_WriteBankActivity.this, "오류가 발생했습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                        }else{
                            Toast.makeText(Li_WriteBankActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_WriteBankActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
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
    public void sign1(View v){
        id =R.id.signPop1;
        signPop();
    }
    //서명클릭시->팝업창
    public void signPop() {
        Intent intent = new Intent(getApplication(), SignPopupActivity.class);
        startActivityForResult(intent, 1);
    }
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
    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Li_AddFilesActivity.class);
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
        switch (v.getId()) {
            case R.id.banknum:
                li_bank_num_text.requestFocus();
                manager.showSoftInput(li_bank_num_text, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.depositor:
                li_depositor_text.requestFocus();
                manager.showSoftInput(li_depositor_text, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }
}