package contract.hee.bukook.join;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import contract.hee.bukook.R;
import contract.hee.bukook.bean.Admin;

public class AgreeJoinActivity extends AppCompatActivity {
    private WebView webView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_join);
        webView = findViewById(R.id.webView);
        // WebView 초기화
        init_webView();
    }
    public void init_webView() {
        intent = new Intent();
        webView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        WebSettings mWebSettings = webView.getSettings(); //세부 세팅 등록
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
        int val = getIntent().getIntExtra("agree", 0);
        Log.d("TAG", "init_webView---val: " + val);
        switch (val) {
            case 1:
                webView.loadUrl( Admin.sic_url + "/_files/agree1.html");
                intent.putExtra("result",1);
                break;
            case 2:
                webView.loadUrl( Admin.sic_url + "/_files/agree2.html");
                intent.putExtra("result",2);
                break;
            case 3:
                webView.loadUrl( Admin.sic_url + "/_files/agree3.html");
                intent.putExtra("result",3);
                break;
        }
    }
    public void back(View view) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,intent);
        finish();
    }
}