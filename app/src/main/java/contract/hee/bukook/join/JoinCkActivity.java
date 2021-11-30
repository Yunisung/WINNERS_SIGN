package contract.hee.bukook.join;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import contract.hee.bukook.R;
import contract.hee.bukook.bean.Admin;
import contract.hee.bukook.contract.CancelPopupActivity;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class JoinCkActivity extends AppCompatActivity {
   private TextView cc1;
   private TextView cc2;
   private TextView cc3;
   private WebView webview1;
   private WebView webview2;
   private WebView webview3;
   private CheckBox ckAll;
   private CheckBox ck1;
   private CheckBox ck2;
   private CheckBox ck3;
   private  CheckBox [] ckarr;
   private int result =1;
   private String  beforeCk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_ck);
        //부분색변경
        colorChange();

        ckAll = findViewById(R.id.ckAll);
        ck1 = findViewById(R.id.cc1);
        ck2 = findViewById(R.id.cc2);
        ck3 = findViewById(R.id.cc3);
        ckarr = new CheckBox[]{ck1,ck2,ck3};

        webview1 = findViewById(R.id.joinWeb1);
        webview2 = findViewById(R.id.joinWeb2);
        webview3 = findViewById(R.id.joinWeb3);
        showWebView();

        beforeCk=getIntent().getStringExtra("ck3");
        Log.d("TAG", "beforeCk: "+beforeCk);
        if(beforeCk!=null){
            ck1.setChecked(true);
            ck2.setChecked(true);
            if(beforeCk.equals("Y")){
                ck3.setChecked(true);
                ckAll.setChecked(true);
            }
        }
    }

    public void showWebView() {
        webview1.setWebViewClient(new WebViewClient());
        webview1.getSettings().setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        webview1.getSettings().setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webview1.getSettings().setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webview1.getSettings().setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webview1.getSettings().setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webview1.getSettings().setSupportZoom(true); // 화면 줌 허용 여부
        webview1.getSettings().setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        webview1.getSettings().setDisplayZoomControls(false);
        webview1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 컨텐츠 사이즈 맞추기
        webview1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webview1.getSettings().setDomStorageEnabled(true); // 로컬저장소 허용 여부
        webview1.loadUrl(Admin.sic_url + "/_files/agree1.html");

        webview2.setWebViewClient(new WebViewClient());
        webview2.getSettings().setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        webview2.getSettings().setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webview2.getSettings().setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webview2.getSettings().setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webview2.getSettings().setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webview2.getSettings().setSupportZoom(true); // 화면 줌 허용 여부
        webview2.getSettings().setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        webview2.getSettings().setDisplayZoomControls(false);
        webview2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 컨텐츠 사이즈 맞추기
        webview2.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webview2.getSettings().setDomStorageEnabled(true); // 로컬저장소 허용 여부
        webview2.loadUrl(Admin.sic_url + "/_files/agree2.html");

        webview3.setWebViewClient(new WebViewClient());
        webview3.getSettings().setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        webview3.getSettings().setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webview3.getSettings().setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webview3.getSettings().setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webview3.getSettings().setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webview3.getSettings().setSupportZoom(true); // 화면 줌 허용 여부
        webview3.getSettings().setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        webview3.getSettings().setDisplayZoomControls(false);
        webview3.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 컨텐츠 사이즈 맞추기
        webview3.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webview3.getSettings().setDomStorageEnabled(true); // 로컬저장소 허용 여부
        webview3.loadUrl( Admin.sic_url + "/_files/agree3.html");
    }

    public void goPopup(View v){
        goPop(v.getId());
    }

    public void goPop(int id){
        Intent intent= new Intent(getApplication(), AgreeJoinActivity.class);
        if(id==R.id.a1 || id==R.id.cc1){
            intent.putExtra("agree",1);
        }else if(id==R.id.a2 ||id==R.id.cc2){
            intent.putExtra("agree",2);
        }else if(id==R.id.a3 ||id==R.id.cc3){
            intent.putExtra("agree",3);
        }else{
            Toast.makeText(this, "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(intent,result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: "+resultCode);
        if (requestCode == result &&resultCode == RESULT_OK) {
               switch (data.getIntExtra("result",0)) {
                   case 1:
                       ck1.setChecked(true);
                       break;
                   case 2:
                       ck2.setChecked(true);
                       break;
                   case 3:
                       ck3.setChecked(true);
                       break;
           }
        }
    }

    //다음btn
    public void goJoinInputFrm(View v){
        Intent intent = new Intent(getApplicationContext() , JoinInputActivity.class);
        if(ck1.isChecked() && ck2.isChecked()){
            if(ck3.isChecked()){
                intent.putExtra("ck3","Y");
            }else{
                intent.putExtra("ck3","N");
            }
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "필수항목을 체크해주세요", Toast.LENGTH_LONG).show();
        }
    }

    //(필수)색변경
    public void colorChange() {
        cc1 = findViewById(R.id.cc1);
        cc2 = findViewById(R.id.cc2);
        cc3 = findViewById(R.id.cc3);
        List<String> arr = new ArrayList<>();
        arr.add(cc1.getText().toString());
        arr.add(cc2.getText().toString());
        for(int i=0; i< arr.size(); i++){
            SpannableStringBuilder st = new SpannableStringBuilder(arr.get(i));
            int s = arr.get(i).indexOf("[필수]");
            int e = s+4;
            st.setSpan(new ForegroundColorSpan(Color.parseColor("#495EFA")),s,e, SPAN_EXCLUSIVE_EXCLUSIVE);
            switch (i){
                case 0:
                cc1.setText(st);
                break;
                case 1:
                cc2.setText(st);
                break;
                case 2:
                cc3.setText(st);
                break;
                default:
                    break;
            }
        }
    }
    //모두동의
    public void ckAll(View v) {
        for (int i = 0; i < ckarr.length; i++) {
            if (ckAll.isChecked()) {
                ckarr[i].setChecked(true);
            } else {
                ckarr[i].setChecked(false);
            }

        }
    }
    //필수 항목 동의
    public void clickCk(View v){
        int num=0;
        for(int i=0; i<ckarr.length; i++) {
            if (ckarr[i].isChecked() == false) {
                ckAll.setChecked(false);
            }else{
                num+=1;
            }
            if(num==3){
                ckAll.setChecked(true);
            }
        }
        CheckBox cb = findViewById(v.getId());
        Log.d("TAG", "clickCk: "+cb.isChecked());
        if(cb.isChecked()==true){
            goPop(v.getId());
        }
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/748cb2b7c4274bca968ed04dd6399840";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Log.d("TAG", "JoinCkActivity--CancelPopupActivity: ");
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("where","main");
        intent.putExtra("main","회원가입");
        intent.putExtra("text","작성중인 회원가입이 취소됩니다.");
        startActivity(intent);
    }
}