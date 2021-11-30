package contract.hee.bukook.contract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.CommonFile;
import contract.hee.bukook.FilesTask;
import contract.hee.bukook.LoginMenuActivity;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class Li_MycontractActivity extends AppCompatActivity {
    private ContractDB contractDB = ContractDB.getInstance();
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
    private TextView li_addr1;
    private TextView li_re_name;
    private TextView li_re_birth;
    private TextView li_re_phone;
    private TextView li_re_email;
    private TextView li_bank_name;
    private TextView li_bank_num;
    private TextView li_depositor;
    private TextView li_wdate;
    private TextView li_name;
    private TextView li_agent;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ProgressDialog progressDialog;
    private Licensee li = new Licensee();
    private CommonFile comm = new CommonFile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li_mycontract);
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
        li_addr1 = findViewById(R.id.li_addr1);
        li_re_name = findViewById(R.id.li_re_name);
        li_re_birth = findViewById(R.id.li_re_birth);
        li_re_phone = findViewById(R.id.li_re_phone);
        li_re_email = findViewById(R.id.li_re_email);
        li_bank_name = findViewById(R.id.li_bank_name);
        li_bank_num = findViewById(R.id.li_bank_num);
        li_depositor = findViewById(R.id.li_depositor);
        li_wdate = findViewById(R.id.li_wdate);
        li_name = findViewById(R.id.li_name2);
        li_agent = findViewById(R.id.li_agent);
        img1 =findViewById(R.id.img1);
        img2 =findViewById(R.id.img2);
        img3 =findViewById(R.id.img3);
        img4 =findViewById(R.id.img4);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        goMyContract();

    }
    public void goHome(View v){
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    //계약서 보여주기
    public void goMyContract() {
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("li_idnum", SessionMb.mb_idnum);
        contractDB.selectContract_li(map, callbackselectContract_li);
    }
    Callback callbackselectContract_li = new Callback() {
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
                    li = gson.fromJson(body.trim(), li.getClass());

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
                    li_bs_num.setText(comm.maskingFunc("bsnum",li.getLi_bs_num()));//마스킹
                    li_cindition.setText(li.getLi_cindition());
                    li_event.setText(li.getLi_event());
                    String addr = "("+li.getLi_zipcode()+")  "+li.getLi_addr1()+" "+li.getLi_addr2();
                    li_addr1.setText(addr);
                    li_re_name.setText(li.getLi_re_name());
                    li_re_birth.setText(comm.maskingFunc("birth",li.getLi_re_birth())); //마스킹
                    li_re_phone.setText(comm.maskingFunc("tel",li.getLi_re_phone())); //마스킹
                    li_re_email.setText(li.getLi_re_email());
                    li_bank_name.setText(li.getLi_bank_name());
                    li_bank_num.setText(li.getLi_bank_num());
                    li_depositor.setText(li.getLi_depositor());
                    li_wdate.setText(comm.maskingFunc("date",li.getLi_wdate())); //마스킹
                    li_name.setText(li.getLi_name());
                    //li_agent.setText(li.getLi_agent());
                    fileDown(li.getPath3(),img1);//신분증
                    fileDown(li.getPath4(),img2);//통장
                    fileDown(li.getPath5(),img3);//사업자
                    fileDown(li.getPath7(),img4);//서명
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
    public void goLoginMenu(View v){
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }
}