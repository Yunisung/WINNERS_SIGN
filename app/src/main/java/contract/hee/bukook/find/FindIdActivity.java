package contract.hee.bukook.find;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.contract.CancelPopupActivity;
import contract.hee.bukook.db.MemberDAO;

public class FindIdActivity extends AppCompatActivity {
    private EditText name;
    private EditText phone;
    private EditText phone_num;
    private String mb_id;
    private MemberDAO md = MemberDAO.getInstance();
    private String sendNum;
    private int phoneck;
    private ProgressDialog progressDialog;
    private InputMethodManager manager;
    private MemberDAO memberDAO = MemberDAO.getInstance();
    private TextView sendBtn;
    //타이머
    private static final int MILLISINFUTURE = 118*1000;
    private Long mLastClickTime = 0L;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private int mm=1;
    private int ss=59;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        name = findViewById(R.id.name);
        phone =findViewById(R.id.phone);
        phone_num = findViewById(R.id.phone_num);
        sendBtn = findViewById(R.id.sendBtn);
    }
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Log.d("TAG", "JoinCkActivity--FindIdActivity: ");
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("where","main");
        intent.putExtra("main","아이디 찾기");
        intent.putExtra("text","아이디 찾기가 취소됩니다.");
        startActivity(intent);
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/a87d75c0dae245d7a5eb1b8e1430dfb8";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void goFindIdFinishFrm(View v){
        String mb_name= name.getText().toString().trim();
        String mb_phone =phone.getText().toString().trim();
        if(mb_name.length()==0 || mb_phone.length()==0){
            Toast.makeText(this, "이름과 핸드폰 번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            if(mb_name.length()==0){
                name.requestFocus();
                manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            }else if(mb_phone.length()==0){
                phone.requestFocus();
                manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            }
            return;
        }
        if(!PatternAdd.validatePhone(mb_phone)){
            Toast.makeText(this, "올바른 핸드폰 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
        }
       if(phoneck!=1) {
           Toast.makeText(this, "핸드폰 인증을 진행해 주세요.", Toast.LENGTH_SHORT).show();
           return;
       }else if(!sendPhoneNum()){
           Toast.makeText(this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
           phone_num.requestFocus();
           manager.showSoftInput(phone_num, InputMethodManager.SHOW_IMPLICIT);
           return;
       }else{
           Intent intent = new Intent(getApplication(), FindIdFinishPopup.class);
           intent.putExtra("mb_id", mb_id);
           startActivity(intent);
        }
    }

    public void sendNum(View v){
        String mb_name= name.getText().toString();
        String mb_phone =phone.getText().toString().trim();
        if(mb_name.length()==0 || mb_phone.length()==0){
            Toast.makeText(this, "이름과 핸드폰 번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            if(!sendBtn.getText().toString().trim().equals("인증번호 재전송")&&!sendBtn.getText().toString().trim().equals("인증번호 전송")){
                Toast.makeText(this, "잠시만 기다려 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
                HashMap<String,String>map =new HashMap<>();
                map.put("mb_name",mb_name);
                map.put("mb_phone", mb_phone);
                progressDialog= new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("회원정보를 확인중입니다.");
                progressDialog.show();
                md.selectFindId(map,callbackSelectFindId); //아이디찾기

        }//1else
    }//sendEmailNum

    Callback callbackSelectFindId = new Callback() {
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
                    if(body.trim().equals("null")){
                        Toast.makeText(FindIdActivity.this, "존재하지 않는 회원입니다", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else{
                        mb_id=body.trim();
                        sendPhone();
                        Log.d("TAG", "mb_id: " + mb_id);
                        progressDialog.dismiss();
                    }
                }
            });
        }
    };
    public void sendPhone(){
        String phNum = phone.getText().toString().trim();
        if(!PatternAdd.validatePhone(phNum)){
            Toast.makeText(this, "올바른 휴대폰 번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sendBtn.getText().toString().trim().equals("인증번호 전송")){
            sendNum= PatternAdd.createPhoneCode();
            HashMap<String, String> map = new HashMap<>();
            map.put("phNum",phNum);
            map.put("num",sendNum);
            Log.d("TAG", "sendNum: "+sendNum);
            memberDAO.sendPhone(map, callbackPhone);
            countDownTimer();
            countDownTimer.start();
            return;
        }
        if(SystemClock.elapsedRealtime() - mLastClickTime > 10000) {
            if(!sendBtn.getText().toString().trim().equals("인증번호")){
                Toast.makeText(this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                sendNum=PatternAdd.createPhoneCode();
                HashMap<String, String> map = new HashMap<>();
                map.put("phNum",phNum);
                map.put("num",sendNum);
               memberDAO.sendPhone(map, callbackPhone);
                Log.d("TAG", "sendNum: "+sendNum);
                countDownTimer();
                countDownTimer.start();
                return;
            }
        }
    }
    private void countDownTimer() {
        Log.d("TAG", "---------countDownTimer---------");
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                sendBtn.setTextColor(Color.parseColor("#555555"));
                sendBtn.setBackground(getResources().getDrawable(R.drawable.line_all_btn_before));
                if(mm==1 && ss==0){
                    ss=59;
                    mm=0;
                }else if(mm==0 && ss==0){
                    mm=1;
                    ss=59;
                    sendBtn.setText("인증번호 재전송");
                }
                String ssStr="";
                if(ss<10){
                    ssStr ="0"+ss;
                }else{
                    ssStr=ss+"";
                }
                String time = mm+":"+ssStr;
                sendBtn.setText(time);
                ss --;
            }
            public void onFinish() {
                sendBtn.setTextColor(Color.parseColor("#12BABB"));
                sendBtn.setBackground(getResources().getDrawable(R.drawable.line_all_btn));
                sendBtn.setText("인증번호 재전송");
            }
        };
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }

    Callback callbackPhone = new Callback() {
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
                    if ("1".equals(body.trim())) {
                        phoneck =1;
                        Toast.makeText(FindIdActivity.this, "인증번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if("4".equals(body.trim())){
                        Toast.makeText(FindIdActivity.this, "휴대폰 번호를 다시한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(FindIdActivity.this, "다시시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public boolean sendPhoneNum(){
        String num = phone_num.getText().toString().trim();
        if(sendNum.equals(num)){
            return true;
        }else{
            return false;
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
    public void clickView(View v) {
        switch (v.getId()) {
            case R.id.nameLy:
                name.requestFocus();
                manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.emailLy:
                phone.requestFocus();
                manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.numLy:
                phone_num.requestFocus();
                manager.showSoftInput(phone_num, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }
}