package contract.hee.bukook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.LoginDB;
import contract.hee.bukook.find.FindIdActivity;
import contract.hee.bukook.find.FindPwActivity;
import contract.hee.bukook.join.JoinCkActivity;

public class MainActivity extends AppCompatActivity {
    private EditText id;
    private EditText pw;
    private LoginDB loginDB = LoginDB.getInstance();
    private Member mb = new Member();
    private CheckBox loginSession;
    private InputMethodManager manager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_main);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Log.d("TAG", " getTheme: "+ getTheme());


        //자동 로그인
        SharedPreferences preferences =getSharedPreferences("mb", Context.MODE_PRIVATE);
        String session_idnum = preferences.getString("mb_idnum",null);
        String session_id = preferences.getString("mb_id",null);
        String session_name = preferences.getString("mb_name",null);
        Log.d("TAG", "## session_idnum: "+session_idnum+", session_id: "+session_id+", session_name: "+session_name);

        if(session_id!=null && session_idnum!=null){
            SessionMb.mb_idnum=session_idnum;
            SessionMb.mb_id =session_id;
            SessionMb.mb_name=session_name;
            Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
            startActivity(intent);
           finish();
        }
        id=findViewById(R.id.id);
        pw = findViewById(R.id.pw);

        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
    }

    public void onClickManual(View v) {
        String url = "https://www.notion.so/PDF-0e3e5df643694f98bb80e3521d3073e0";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //회원가입btn
    public void goJoinFrm(View v){
        Intent intent = new Intent(getApplicationContext(), JoinCkActivity.class);
        startActivity(intent);
        finish();
    }

    //아이디찾기btn
    public void goFindIdFrm(View v){

        Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
        startActivity(intent);
        finish();
    }

    //비밀번호 찾기btn
    public void goFindPwFrm(View v){
        Intent intent = new Intent(getApplicationContext(), FindPwActivity.class);
        startActivity(intent);
        finish();
    }
    //로그인 btn
    public void goLogin(View v){
        String mb_id=id.getText().toString().trim();
        String mb_pw=pw.getText().toString().trim();

        if(mb_id.length()==0 || mb_pw.length()==0){
            Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            if(mb_id.length()==0){
                id.requestFocus();
                manager.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);
            }else if(mb_pw.length()==0){
                pw.requestFocus();
                manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
            }
        }else{
           if(!CommonFile.isConnect(getBaseContext())){
                Toast.makeText(this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();
            mb.setMb_id(mb_id);
            mb.setMb_pw(mb_pw);
            loginDB.goLogin(mb, callbackGoLogin);
        }
    }
    Callback callbackGoLogin = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            progressDialog.dismiss();
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            progressDialog.dismiss();

            String body = response.body().string();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());

                    if ("null".equals(body.trim())) {
                        Toast.makeText(MainActivity.this, "아이디와 패스워드가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        try {
                            Gson gson =new Gson();
                            mb =gson.fromJson(body.trim(), mb.getClass());

                            loginSession = findViewById(R.id.loginSession);
                            if(loginSession.isChecked()){
                                // session저장
                                SharedPreferences preferences = getSharedPreferences("mb", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor =preferences.edit();
                                editor.putString("mb_idnum",mb.getMb_idnum()).commit();
                                editor.putString("mb_id",mb.getMb_id()).commit();
                                editor.putString("mb_name",mb.getMb_name()).commit();
                            }
                            SessionMb.mb_id=mb.getMb_id();
                            SessionMb.mb_idnum=mb.getMb_idnum();
                            SessionMb.mb_name=mb.getMb_name();

                            Intent intent =new Intent(getApplicationContext(), LoginMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            });
        }
    };
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
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(), FinishAppActivity.class);
        startActivity(intent);
    }

}