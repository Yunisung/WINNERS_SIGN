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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
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

public class In_AgreeActivity extends HomeActivity {
    private ImageView signPop1;
    private ImageView signPop2;
    private int id;
    private String[] fileName = new String[2] ;
    private String[] fi_group = new String[2] ;
    private ContractDB contractDB = ContractDB.getInstance();
    private  String sign1;
    private  String sign2;
    private Boolean[] isSignLoaded = new Boolean[2];

    private EditText signName1;
    private  EditText signName2;
    private TextView text1;
    private TextView text2;
    private ProgressDialog progressDialog;
    private Individual in = new Individual();
    private InputMethodManager manager;
    private WebView web1;
    private WebView web2;

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/0d457ea7317e458e85e5a33764b486d5";
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
        setContentView(R.layout.activity_in__agree);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        signPop1 = findViewById(R.id.signPop1);
        signPop2 = findViewById(R.id.signPop2);
        signName1 = findViewById(R.id.signName1);
        signName2 = findViewById(R.id.signName2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        web1 =findViewById(R.id.web1);
        web2 =findViewById(R.id.web2);
        isSignLoaded[0] = false;
        isSignLoaded[1] = false;
        showWebview();
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        Log.d("TAG", "onCreate() returned Individual.db_div : " + Individual.db_div);
        if("U".equals(Individual.db_div)){
            goMyContract();
        }
    }

    public void showWebview() {
        web1.setWebViewClient(new WebViewClient());
        WebSettings mWebSettings = web1.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(true); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        web1.loadUrl( Admin.sic_url + "/_files/in_agree1.html");

        web2.setWebViewClient(new WebViewClient());
        WebSettings mWebSetting = web2.getSettings(); //세부 세팅 등록
        mWebSetting.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSetting.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSetting.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSetting.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSetting.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSetting.setSupportZoom(true); // 화면 줌 허용 여부
        mWebSetting.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        mWebSetting.setDisplayZoomControls(false);
        mWebSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSetting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSetting.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        web2.loadUrl( Admin.sic_url + "/_files/in_agree2.html");
    }


    public void goMyContract(){
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
                            signName1.setText(in.getIn_name());
                            signName2.setText(in.getIn_name());

                            if(in.getPath1() != "" && in.getPath1() != null) {
                                fileDown(in.getPath1(), signPop1);
                                findViewById(R.id.text1).setVisibility(View.GONE);
                                signPop1.setVisibility(View.VISIBLE);
                                sign1 = "1";
                                isSignLoaded[0] = true;
                            }

                            if (in.getPath2() != "" && in.getPath2() != null) {
                                fileDown(in.getPath2(), signPop2);
                                findViewById(R.id.text2).setVisibility(View.GONE);
                                signPop2.setVisibility(View.VISIBLE);
                                sign2 = "1";
                                isSignLoaded[1] = true;
                            }

                        }else{
                            Log.d("goMyContract: ", "empty: ");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_AgreeActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
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

    public void goHome(View v){
        onBackPressed();
    }
    //다음 btn
    public void goInServiceFrm(View v){
        if (!nameCk()) {
            return;
        }
            if(sign1==null || sign2==null){
                Toast.makeText(this, "전자 서명을 완료 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                signName1.clearFocus();
                signName2.clearFocus();
                if(sign1==null){
                    id =R.id.signPop1;
                    signPop();
                    return;
                }else if(sign2==null){
                    id =R.id.signPop2;
                    signPop();
                    return;
                }
            }
        progressDialog.show();
        Log.d("TAG", " ## goInServiceFrm DB save start");
        HashMap<String, String> map = new HashMap<>();
        map.put("in_idnum", SessionMb.mb_idnum);
        map.put("in_name", signName1.getText().toString());
        Log.d("TAG", "goInServiceFrm() Individual.db_div: " + Individual.db_div);
        Log.d("TAG", "goInServiceFrm() Individual.db_div: " + Individual.seq);
        if("U".equals(Individual.db_div)){
            map.put("in_seq", in.getIn_seq());
            contractDB.updateIndividual(map, callbackIndividual); //db update
        }else{
            contractDB.insertIndividual(map, callbackIndividual); //db insert
        }
    }
    Callback callbackIndividual = new Callback() {
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
                            int num=0;
                            for (int i=0; i<2;i++){
                                String imagePath = getCacheDir().toString()+"/"+fileName[i];
                                Log.d("TAG", "goInServiceFrm imagePath: "+imagePath);

                                if (isSignLoaded[i] == true) {
                                    num+=1;
                                }
                                else {
                                    if(!fileUp(imagePath, fi_group[i], in.getIn_seq())){
                                        progressDialog.dismiss();
                                        Toast.makeText(In_AgreeActivity.this, "서명 저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    num+=1;
                                }

                            }
                            if(num==2){
                                Individual.db_div = "U";
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), In_ServiceActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(In_AgreeActivity.this, "계약서 저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_AgreeActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }
    };
    public boolean fileUp(String imagePath,String fileGroup,String seq){
        boolean result=false;
        String ImageUploadURL = Admin.sic_url+"/app/Appdbconn?page=imgUpload";

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
    public boolean nameCk() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        String nameResult = PatternAdd.validateName(signName1.getText().toString().trim());
        if(signName1.getText().toString().trim().length() == 0 || signName2.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "약관동의 이름이 필요합니다.", Toast.LENGTH_SHORT).show();
            if(signName1.getText().toString().trim().length() == 0 ){
                signName1.requestFocus();
                manager.showSoftInput(signName1, InputMethodManager.SHOW_IMPLICIT);
            }else if(signName2.getText().toString().trim().length()==0){
                signName2.requestFocus();
                manager.showSoftInput(signName2, InputMethodManager.SHOW_IMPLICIT);
            }
            return false;
        }else if(!nameResult.equals("ok")){
            Toast.makeText(this, nameResult, Toast.LENGTH_SHORT).show();
            signName1.requestFocus();
            manager.showSoftInput(signName1, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }else if(!signName1.getText().toString().equals(signName2.getText().toString())){
            Toast.makeText(this, "약관동의 이름이 일치해야 합니다.", Toast.LENGTH_SHORT).show();
            signName2.requestFocus();
            manager.showSoftInput(signName2, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        return true;
    }
    public void sign1(View v){
        signPop();
        id =R.id.signPop1;
    }
    public void sign2(View v){
        signPop();
        id =R.id.signPop2;
    }

    //서명클릭시->팝업창
    public void signPop() {
        Intent intent = new Intent(getApplication(), SignPopupActivity.class);
        startActivityForResult(intent, 1);
    }
    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","계약서 작성취소");
        intent.putExtra("text","작성중인 계약서가 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                byte[] bytes = data.getByteArrayExtra("sign");
                String getfileName = data.getExtras().get("fileName").toString();
                Bitmap sign = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if(SignDrow.isDrow()!=1){
                    switch (id){
                        case R.id.signPop1:
                            text1.setVisibility(View.VISIBLE);
                            signPop1.setVisibility(View.GONE);
                            sign1=null;
                            break;
                        case R.id.signPop2:
                            text2.setVisibility(View.VISIBLE);
                            signPop2.setVisibility(View.GONE);
                            sign2=null;
                            break;
                    }
                    return;
                }
                switch (id) {
                    case R.id.signPop1:
                        signPop1 = findViewById(R.id.signPop1);
                        signPop1.setImageBitmap(sign);
                        fileName[0] = getfileName;
                        fi_group[0] = "1";
                        sign1=sign.toString();
                        //성공시
                        text1.setVisibility(View.GONE);
                        signPop1.setVisibility(View.VISIBLE);
                        signName2.requestFocus();
                        break;
                    case R.id.signPop2:
                        signPop2 = findViewById(R.id.signPop2);
                        signPop2.setImageBitmap(sign);
                        fileName[1] = getfileName;
                        fi_group[1] = "2";
                        sign2=sign.toString();
                        //성공시
                        text2.setVisibility(View.GONE);
                        signPop2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
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

