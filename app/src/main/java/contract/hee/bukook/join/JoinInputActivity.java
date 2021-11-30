package contract.hee.bukook.join;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import contract.hee.bukook.MainActivity;
import contract.hee.bukook.contract.CancelPopupActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Member;
import contract.hee.bukook.bean.PatternAdd;
import contract.hee.bukook.db.MemberDAO;

public class JoinInputActivity extends AppCompatActivity {
    private EditText name;
    private EditText id;
    private EditText pw;
    private EditText repw;
    private EditText phone;
    private EditText email;
    private EditText phone_Num;
    private MemberDAO memberDAO = MemberDAO.getInstance();
    private int idck;
    private String sendNum;
    private int phoneck;
    private ProgressDialog progressDialog;
    private InputMethodManager manager;
    private String ck3;
    private TextView sendBtn;
    private Long mLastClickTime = 0L;

    //타이머
    private static final int MILLISINFUTURE = 118*1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private int mm=1;
    private int ss=59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_input);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        name = findViewById(R.id.mb_name);
        id = findViewById(R.id.mb_id);
        pw = findViewById(R.id.mb_pw);
        repw = findViewById(R.id.repw);
        phone = findViewById(R.id.mb_phone);
        email = findViewById(R.id.mb_email);
        phone_Num = findViewById(R.id.phone_Num);
        ck3 = getIntent().getStringExtra("ck3");
        sendBtn = findViewById(R.id.sendBtn);
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/87287ac6c980443895e374bf19b26af3";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //가입완료 btn
    public void goJoin(View v) {
        //널체크
        if (!nullck()) {
            return;
        }else if (idck == 0) {
            Toast.makeText(this, "아이디 중복확인을 진행해주세요", Toast.LENGTH_SHORT).show();
            return;
            //비밀번호 확인
        } else if (!pwck()) {
            return;
        }else if(phoneck!=1) {
            Toast.makeText(this, "핸드폰 인증을 진행해 주세요.", Toast.LENGTH_SHORT).show();
            return;
            //인증번호 일치 불일치
        }else if(!sendPhoneNum()){
            Toast.makeText(this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            phone_Num.requestFocus();
            manager.showSoftInput(phone_Num, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        insertJoin();
    }
    public void insertJoin() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat HHmmss = new SimpleDateFormat("HHmmss");
        String mdate = yyyymmdd.format(date);
        String mtime = HHmmss.format(date);

        Member mb =new Member();
        mb.setMb_id(id.getText().toString());
        mb.setMb_name(name.getText().toString());
        mb.setMb_pw(pw.getText().toString());
        mb.setMb_phone(phone.getText().toString());
        mb.setMb_email(email.getText().toString());
        mb.setMb_wdate(mdate);
        mb.setMb_wtime(mtime);
        mb.setMb_ck3(ck3);
        memberDAO.insertJoin(mb, callbackInsertJoin);
    }

    Callback callbackInsertJoin = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    if("true".equals(body.trim())) { //member insert 성공 true
//                        Intent intent = new Intent(getApplicationContext(), JoinFinish.class);
//                        startActivity(intent);
//                        finish();
                        Intent intent = new Intent(getApplicationContext(), JoinFinishPopup.class);
                        startActivity(intent);


                    } else { //member insert 실패 false
                        Toast.makeText(JoinInputActivity.this, "회원가입에 실패하셨습니다. 다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    public boolean nullck() {
        String nameResult =PatternAdd.validateName(name.getText().toString().trim());
        if(name.getText().toString().trim().length()==0){
            Toast.makeText(this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!nameResult.equals("ok")) {
            Toast.makeText(this, nameResult, Toast.LENGTH_SHORT).show();
            name.requestFocus();
            manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(id.getText().toString().trim().length()==0){
            Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show();
            id.requestFocus();
            manager.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!PatternAdd.validateId(id.getText().toString().trim())) {
            Toast.makeText(this, "아이디는 4~12자의 영문, 숫자만 사용 가능합니다.", Toast.LENGTH_SHORT).show();
            id.requestFocus();
            manager.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(pw.getText().toString().trim().length()==0){
            Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
            pw.requestFocus();
            manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!PatternAdd.validatePw(pw.getText().toString().trim())){
            Toast.makeText(this, "비밀번호는 4~12자 영문과 숫자를 사용하세요.", Toast.LENGTH_SHORT).show();
            pw.requestFocus();
            manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(repw.getText().toString().trim().length()==0){
            Toast.makeText(this, "비밀번호 확인란을 입력해 주세요", Toast.LENGTH_SHORT).show();
            repw.requestFocus();
            manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
//        if(email.getText().toString().trim().length()==0){
//            Toast.makeText(this, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
//            email.requestFocus();
//            manager.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
//            return false;
//        }
//        if(!PatternAdd.validateEmail(email.getText().toString().trim())){
//            Toast.makeText(this, "올바른 형식의 이메일이 아닙니다.", Toast.LENGTH_SHORT).show();
//            email.requestFocus();
//            manager.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
//            return false;
//        }
        if(phone.getText().toString().trim().length()==0){
            Toast.makeText(this, "휴대폰번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if(!PatternAdd.validatePhone(phone.getText().toString())) {
            Toast.makeText(this, "올바른 형식의 휴대폰번호가 아닙니다.", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        return true;
    }
    public boolean pwck() {
        String mb_pw = pw.getText().toString().trim();
        String mb_repw = repw.getText().toString().trim();
        if (mb_pw.equals(mb_repw)) {
            return true;
        }
        Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        repw.requestFocus();
        manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
        return false;
    }

    public void id_ck(View v) {
        id = findViewById(R.id.mb_id);
        String mb_id = id.getText().toString().trim();
        if(mb_id.length()==0){
            Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show();
            id.requestFocus();
            manager.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);
            return;
        }else if(!PatternAdd.validateId(mb_id.trim())) {
            Toast.makeText(this, "아이디는 4~12자의 영문, 숫자만 사용 가능합니다.", Toast.LENGTH_SHORT).show();
            id.requestFocus();
            manager.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);
            return ;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("mb_id", mb_id);
        memberDAO.join_idCK(map, callbackIdCk);

    }

    Callback callbackIdCk = new Callback() {
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
                    if ("false".equals(body.trim())){
                        Toast.makeText(JoinInputActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        idck = 1;
                    } else {
                        Toast.makeText(JoinInputActivity.this, "이미 등록되어있는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        idck = 0;
                    }
                }
            });
        }
    };

    public void sendNum(View v){
        String phNum = phone.getText().toString().trim();
        if(!PatternAdd.validatePhone(phNum)){
            Toast.makeText(this, "올바른 휴대폰 번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sendBtn.getText().toString().trim().equals("인증번호 전송")){
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
        if(SystemClock.elapsedRealtime() - mLastClickTime > 10000) {
            if(!sendBtn.getText().toString().trim().equals("인증번호 재전송")){
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
                        Toast.makeText(JoinInputActivity.this, "인증번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if("4".equals(body.trim())){
                        Toast.makeText(JoinInputActivity.this, "휴대폰 번호를 다시한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(JoinInputActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        //인증실패
                    }
                }
            });
        }
    };

    public boolean sendPhoneNum(){
        String num = phone_Num.getText().toString().trim();
        if(sendNum.equals(num)){
            return true;
        }else{
            return false;
        }
    }

    public void back(View v){
        onBackPressed();
    }

    public void goDirectMain(View v) {
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","회원가입 작성취소");
        intent.putExtra("text","작성중인 회원가입이 취소됩니다.");
        intent.putExtra("where","main");
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplication(), JoinCkActivity.class);
        intent.putExtra("ck3", ck3);
        Log.d("TAG", "ck3: "+ck3);
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


    public void clickView(View v){
        switch (v.getId()){
            case R.id.name:
                name.requestFocus();
                manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.id:
                id.requestFocus();
                manager.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.pw:
                pw.requestFocus();
                manager.showSoftInput(pw, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.repwLy:
                repw.requestFocus();
                manager.showSoftInput(repw, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.phone:
                phone.requestFocus();
                manager.showSoftInput(phone, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.email:
                email.requestFocus();
                manager.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.num:
                phone_Num.requestFocus();
                manager.showSoftInput(phone_Num, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
    }

}




