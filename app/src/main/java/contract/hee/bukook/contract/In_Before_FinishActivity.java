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
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

public class In_Before_FinishActivity extends HomeActivity {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ContractDB contractDB = ContractDB.getInstance();
    private CommonFile comm = new CommonFile();
    private TextView sv_company;
    private TextView sv_charge;
    private TextView sv_bs_num;
    private TextView sv_calculate;
    private TextView in_name;
    private TextView in_company;
    private TextView in_admin;
    private TextView in_phone;
    private TextView in_rrn;
    private TextView in_item;
    private TextView zipcode;
    private TextView addr1;
    private TextView addr2;
    private TextView in_tel;
    private TextView in_bank_name;
    private TextView in_bank_num;
    private TextView in_depositor;
    private TextView signName;
    private TextView in_wdate;
    private TextView in_terminal;
    private TextView in_agent;
    private ProgressDialog progressDialog;
    private Individual in = new Individual();

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_before_finish);
        sv_company = findViewById(R.id.sv_company);
        sv_charge = findViewById(R.id.sv_charge);
        sv_bs_num = findViewById(R.id.sv_bs_num);
        sv_calculate = findViewById(R.id.sv_calculate);
        in_name = findViewById(R.id.in_name);
        in_company = findViewById(R.id.in_company);
        in_admin = findViewById(R.id.in_admin);
        in_phone = findViewById(R.id.in_phone);
        in_item = findViewById(R.id.in_item);
        in_rrn = findViewById(R.id.in_rrn);
        zipcode = findViewById(R.id.zipcode);
        addr1 = findViewById(R.id.addr1);
        addr2 = findViewById(R.id.addr2);
        in_tel = findViewById(R.id.in_tel);
        in_bank_name = findViewById(R.id.in_bank_name);
        in_bank_num = findViewById(R.id.in_bank_num);
        in_depositor = findViewById(R.id.in_depositor);
        signName = findViewById(R.id.signName);
        in_wdate = findViewById(R.id.in_wdate);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        in_terminal = findViewById(R.id.in_terminal);
        in_agent = findViewById(R.id.in_agent);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        goMyContract();

    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/3bebc81941ff4f76ad3a78f12fa59eee";
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
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        in = gson.fromJson(body.trim(), in.getClass());
                        if("true".equals(in.getResult())){
                            in_name.setText(in.getIn_name());
                            in_company.setText(in.getIn_company());
                            in_admin.setText(in.getIn_admin());
                            in_phone.setText(comm.maskingFunc("tel",in.getIn_phone()));
                            in_rrn.setText(comm.maskingFunc("rrn",in.getIn_rrn())); //마스킹
                            in_item.setText(in.getIn_item());
                            zipcode.setText(in.getIn_zipcode());
                            addr1.setText(in.getIn_addr1());
                            addr2.setText(in.getIn_addr2());
                            in_tel.setText(comm.maskingFunc("tel",in.getIn_tel())); //마스킹
                            in_bank_name.setText(in.getIn_bank_name());
                            in_bank_num.setText(in.getIn_bank_num());
                            in_depositor.setText(in.getIn_depositor());
                            in_wdate.setText(comm.maskingFunc("date",in.getIn_wdate()));
                            signName.setText(in.getIn_name());
                            sv_company.setText(in.getIn_sv_company());
                            //sv_charge.setText(in.getIn_sv_charge());
                            sv_charge.setText(R.string.chargeString);
                            sv_bs_num.setText(in.getIn_sv_bs_num());
                            sv_calculate.setText(in.getIn_sv_calculate());
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
                        }else{
                            Toast.makeText(In_Before_FinishActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_Before_FinishActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
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
            img.setImageBitmap(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //back
    public void back(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(),In_WriteBankActivity.class);
        startActivity(intent);
        finish();
    }
    public  void goFinishFrm(View v){
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("in_idnum",SessionMb.mb_idnum);
        map.put("in_seq", in.getIn_seq());
        map.put("in_finish","Y");
        contractDB.updateIndividual(map, callbackUpdateIndividual);
    }
    Callback callbackUpdateIndividual = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TAG", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            In_Before_FinishActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        in = gson.fromJson(body.trim(), in.getClass());
                        if("true".equals(in.getResult())){
                            Intent intent = new Intent(getApplication(), ContractFinishPopup.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(In_Before_FinishActivity.this, "다시 시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_Before_FinishActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

}