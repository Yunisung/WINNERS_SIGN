package contract.hee.bukook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.contract.In_AddFilesActivity;
import contract.hee.bukook.contract.Li_AddFilesActivity;
import contract.hee.bukook.db.ContractDB;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PhotoViewActivity extends Activity {

    private PhotoView photoview;
    private String contractType;
    private String contract;
    private ProgressDialog progressDialog;
    private ContractDB contractDB = ContractDB.getInstance();
    private Licensee li = new Licensee();
    private Individual in = new Individual();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        photoview = findViewById(R.id.photo_view);

        //Bitmap bitmap = (Bitmap)getIntent().getExtras().get("img");
        //byte[] arr = getIntent().getByteArrayExtra("img");
        //Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        //photoview.setImageBitmap(bitmap);

        contractType = getIntent().getStringExtra("ContractType");
        contract = getIntent().getStringExtra("img");
        getIntent().getExtras().clear();

        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");

        if(contractType.equals("li")) {
            goMyContract();
        } else {
            goMyInContract();
        }
    }
    public void goMyContract() {
        progressDialog.show();
        HashMap<String,String> map=new HashMap<>();
        map.put("li_idnum", SessionMb.mb_idnum);
        contractDB.selectMyLicensee(map, callbackSelectMyLicensee);
    }

    public void goMyInContract() {
        HashMap<String,String> map=new HashMap<>();
        map.put("in_idnum",SessionMb.mb_idnum);
        progressDialog.show();
        contractDB.selectMyIndividual(map, callbackSelectMyIndividual);
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
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){
                            if(contract.equals("3")) {
                                fileDown(li.getPath3(),photoview);//신분증
                            } else if (contract.equals("4")) {
                                fileDown(li.getPath4(),photoview);//통장사본
                            } else if (contract.equals("5")) {
                                fileDown(li.getPath5(),photoview);//사업자등록증
                            }
                        }else{
                            Toast.makeText(PhotoViewActivity.this, "계약서 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(PhotoViewActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };

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
                        in= gson.fromJson(body.trim(), in.getClass());

                        if("true".equals(in.getResult())){
                            Individual.seq = in.getIn_seq();
                            if(contract.equals("3")) {
                                fileDown(in.getPath3(),photoview);//신분증
                            } else if (contract.equals("4")) {
                                fileDown(in.getPath4(),photoview);//통장사본
                            }

                        }else{
                            Log.d("goMyContract: ", "empty: ");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(PhotoViewActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };


    public void fileDown(String url, PhotoView img){
        Log.d("TAG", "fileDown: "+url);
        if(url!=null){
            try {
                Bitmap result = new FilesTask().execute(url).get();
                Log.d("TAG", "file: "+result);
                img.setImageBitmap(result);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            return;
        }
    }

    public void onBack() {
        finish();
    }

    public void goBack(View v) {
        onBack();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }
}