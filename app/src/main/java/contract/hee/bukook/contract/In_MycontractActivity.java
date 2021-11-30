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
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class In_MycontractActivity extends AppCompatActivity {
    private ContractDB contractDB = ContractDB.getInstance();
    private TextView in_name;
    private TextView in_company;
    private TextView in_admin;
    private TextView in_phone;
    private TextView in_rrn;
    private TextView in_item;
    private TextView addr;
    private TextView in_tel;
    private TextView in_bank_name;
    private TextView in_bank_num;
    private TextView in_depositor;
    private TextView in_wdate;
    private TextView signName;
    private TextView in_sv_company;
    private TextView in_sv_charge;
    private TextView in_sv_calculate;
    private TextView in_sv_bs_num;
    private TextView in_terminal;
    private TextView in_agent;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ProgressDialog progressDialog;
    private Individual in = new Individual();
    private CommonFile comm = new CommonFile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_mycontract);
        in_name = findViewById(R.id.in_name);
        in_company = findViewById(R.id.in_company);
        in_admin = findViewById(R.id.in_admin);
        in_phone = findViewById(R.id.in_phone);
        in_rrn = findViewById(R.id.in_rrn);
        in_item = findViewById(R.id.in_item);
        addr = findViewById(R.id.addr1);
        in_tel = findViewById(R.id.in_tel);
        in_bank_name = findViewById(R.id.in_bank_name);
        in_bank_num = findViewById(R.id.in_bank_num);
        in_depositor = findViewById(R.id.in_depositor);
        in_wdate = findViewById(R.id.in_wdate);
        signName = findViewById(R.id.signName);
        in_sv_company = findViewById(R.id.in_sv_company);
        in_sv_charge = findViewById(R.id.in_sv_charge);
        in_sv_calculate = findViewById(R.id.in_sv_calculate);
        in_sv_bs_num = findViewById(R.id.in_sv_bs_num);
        in_terminal = findViewById(R.id.in_terminal);
        in_agent = findViewById(R.id.in_agent);
        img1 =findViewById(R.id.img1);
        img2 =findViewById(R.id.img2);
        img3 =findViewById(R.id.img3);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        goMyContract();

    }
    //계약서 가져오기
    public void goMyContract() {
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("in_idnum",SessionMb.mb_idnum);
        contractDB.selectContract_in(map, callbackselectContract_in);
    }
    public void goHome(View v){
        Intent intent = new Intent(getApplication(), LoginMenuActivity.class);
        startActivity(intent);
        finish();
    }

    Callback callbackselectContract_in = new Callback() {
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
                        in = gson.fromJson(body.trim(), in.getClass());
                        in_name.setText(in.getIn_name());
                        in_company.setText(in.getIn_company());
                        in_admin.setText(in.getIn_admin());
                        in_phone.setText(comm.maskingFunc("tel",in.getIn_phone())); //마스킹
                        in_rrn.setText(comm.maskingFunc("rrn",in.getIn_rrn())); //마스킹
                        in_item.setText(in.getIn_item());
                        String addr_val = "("+in.getIn_zipcode()+") "+in.getIn_addr1()+" "+in.getIn_addr2();
                        addr.setText(addr_val);
                        in_tel.setText(comm.maskingFunc("tel", in.getIn_tel())); //마스킹
                        in_bank_name.setText(in.getIn_bank_name());
                        in_bank_num.setText(in.getIn_bank_num());
                        in_depositor.setText(in.getIn_depositor());
                        in_wdate.setText(comm.maskingFunc("date",in.getIn_wdate()));
                        signName.setText(in.getIn_name());
                        in_sv_company.setText(in.getIn_sv_company());
                        //in_sv_charge.setText(in.getIn_sv_charge());
                        in_sv_charge.setText(R.string.chargeString);
                        in_sv_calculate.setText(in.getIn_sv_calculate());
                        in_sv_bs_num.setText(in.getIn_sv_bs_num());
                        //단말기
                        StringBuffer sb = new StringBuffer();
                        List<String> arr = new ArrayList<>();
                        arr.add(in.getIn_terminal_1());
                        arr.add(in.getIn_terminal_2());
                        arr.add(in.getIn_terminal_3());
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
                        in_terminal.setText(sb.toString().substring(0,sb.toString().length()-2));
                        //in_agent.setText(in.getIn_agent());
                        fileDown(in.getPath3(),img1);//신분증
                        fileDown(in.getPath4(),img2);//통장사본
                        fileDown(in.getPath7(),img3);//서명
                    }catch (Exception e){
                        e.printStackTrace();
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