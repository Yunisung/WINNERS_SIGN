package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import contract.hee.bukook.ContractFinishPopup;
import contract.hee.bukook.HomeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.CommonFile;
import contract.hee.bukook.FilesTask;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class Li_Before_FinishActivity extends HomeActivity {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ContractDB contractDB = ContractDB.getInstance();
    private CommonFile comm = new CommonFile();
    private Licensee li = new Licensee();
    private TextView li_sv_divide;
    private TextView li_sv_charge;
    private TextView li_sv_calculate;
    private TextView li_terminal;
    private TextView li_company;
    private TextView li_bs_divide;
    private TextView li_admin;
    private TextView li_tel;
    private TextView li_bs_num;
    private TextView li_cindition;
    private TextView li_event;
    private TextView li_zipcode;
    private TextView li_addr1;
    private TextView li_addr2;
    private TextView li_re_name;
    private TextView li_re_birth;
    private TextView li_re_phone;
    private TextView li_re_email;
    private TextView li_bank_name;
    private TextView li_bank_num;
    private TextView li_depositor;
    private TextView wdate;
    private TextView signName;
    private TextView li_agent;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li__before__finish);
        li_sv_divide = findViewById(R.id.li_sv_divide);
        li_sv_charge = findViewById(R.id.li_sv_charge);
        li_sv_calculate = findViewById(R.id.li_sv_calculate);
        li_terminal = findViewById(R.id.li_terminal);
        li_company = findViewById(R.id.li_company);
        li_bs_divide = findViewById(R.id.li_bs_divide);
        li_admin = findViewById(R.id.li_admin);
        li_tel = findViewById(R.id.li_tel);
        li_bs_num = findViewById(R.id.li_bs_num);
        li_cindition = findViewById(R.id.li_cindition);
        li_event = findViewById(R.id.li_event);
        li_zipcode = findViewById(R.id.li_zipcode);
        li_addr1 = findViewById(R.id.li_addr1);
        li_addr2 = findViewById(R.id.li_addr2);
        li_re_name = findViewById(R.id.li_re_name);
        li_re_birth = findViewById(R.id.li_re_birth);
        li_re_phone = findViewById(R.id.li_re_phone);
        li_re_email = findViewById(R.id.li_re_email);
        li_bank_name = findViewById(R.id.li_bank_name);
        li_bank_num = findViewById(R.id.li_bank_num);
        li_depositor = findViewById(R.id.li_depositor);
        wdate = findViewById(R.id.wdate);
        signName = findViewById(R.id.signName);
        li_agent = findViewById(R.id.li_agent);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        goMyContract();

    }

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/6e480593f7d0476cb042ac0bbde65ae4";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void goHome(View v){
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","계약서 작성취소");
        intent.putExtra("text","작성중인 계약서가 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }
    //계약서 보여주기
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
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){

                            String li_sv_divide_val =null;
                            if("1".equals(li.getLi_sv_divide()) )    li_sv_divide_val = "신용카드";
                            else if("2".equals(li.getLi_sv_divide()) )     li_sv_divide_val = "가상계좌";
                            else if("3".equals(li.getLi_sv_divide()) )     li_sv_divide_val = "계좌이체";
                            else     li_sv_divide_val = "휴대폰결제";

                            li_sv_divide.setText(li_sv_divide_val);
                            //li_sv_charge.setText(li.getLi_sv_charge());
                            li_sv_charge.setText(R.string.chargeString);
                            li_sv_calculate.setText(li.getLi_sv_calculate());

                            //단말기
                            //test  StringJoiner js = new StringJoiner(", ");
                            StringBuffer sb = new StringBuffer();
                            List<String> arr = new ArrayList<>();
                            arr.add(li.getLi_terminal_1());
                            arr.add(li.getLi_terminal_2());
                            arr.add(li.getLi_terminal_3());
                            for(int z=0; z<arr.size(); z++){
                                if(arr.get(z).equals("Y")){
                                    switch (z){
                                        case 0:
                                            sb.append("수기결제, ");
                                            break;
                                        case 1:
                                            sb.append("스와이프, ");
                                            break;
                                        case 2:
                                            sb.append("무선단말기, ");
                                            break;
                                    }
                                }
                            }
                            li_terminal.setText(sb.toString().substring(0,sb.toString().length()-2));
                            li_company.setText(li.getLi_company());

                            String li_bs_divide_val =null;
                            if("1".equals(li.getLi_bs_divide()) )    li_bs_divide_val = "개인";
                            else     li_bs_divide_val = "법인";

                            li_bs_divide.setText(li_bs_divide_val);
                            li_admin.setText(li.getLi_admin());
                            li_tel.setText(comm.maskingFunc("tel",li.getLi_tel())); //마스킹
                            li_bs_num.setText(comm.maskingFunc("bsnum",li.getLi_bs_num())); //마스킹
                            li_cindition.setText(li.getLi_cindition());
                            li_event.setText(li.getLi_event());
                            li_zipcode.setText(li.getLi_zipcode());
                            li_addr1.setText(li.getLi_addr1());
                            li_addr2.setText(li.getLi_addr2());
                            li_re_name.setText(li.getLi_re_name());
                            li_re_birth.setText(comm.maskingFunc("birth",li.getLi_re_birth())); //마스킹
                            li_re_phone.setText(comm.maskingFunc("tel",li.getLi_re_phone())); //마스킹
                            li_re_email.setText(li.getLi_re_email());
                            li_bank_name.setText(li.getLi_bank_name());
                            li_bank_num.setText(li.getLi_bank_num());
                            li_depositor.setText(li.getLi_depositor());
                            wdate.setText(comm.maskingFunc("date",li.getLi_wdate()));
                            signName.setText(li.getLi_name());
                            //li_agent.setText(li.getLi_agent());
                            fileDown(li.getPath3(),img1);//신분증
                            fileDown(li.getPath4(),img2);//통장
                            fileDown(li.getPath5(),img3);//사업자
                            fileDown(li.getPath7(),img4);//서명
                        }else{
                            Toast.makeText(Li_Before_FinishActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_Before_FinishActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

    public void fileDown(String url, ImageView img){
        try {
            Bitmap result = new FilesTask().execute(url).get();
            Log.d("TAG", "file: "+result);
            img.setImageBitmap(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void goFinishContractFrm(View v){
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("li_idnum",SessionMb.mb_idnum);
        map.put("li_seq", li.getLi_seq());
        map.put("li_finish","Y");
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
            Li_Before_FinishActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){
                            Log.d("TAG", " ## Licensee basic update ok");
                            Intent intent = new Intent(getApplication(), ContractFinishPopup.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Li_Before_FinishActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_Before_FinishActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };
    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(),Li_WriteBankActivity.class);
        startActivity(intent);
        finish();
    }
}