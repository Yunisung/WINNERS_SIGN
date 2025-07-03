package contract.hee.bukook.contract;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import contract.hee.bukook.HomeActivity;
import contract.hee.bukook.PhotoViewActivity;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import contract.hee.bukook.CommonFile;
import contract.hee.bukook.FilesTask;
import contract.hee.bukook.ImgUploadTask;
import contract.hee.bukook.R;
import contract.hee.bukook.bean.Admin;
import contract.hee.bukook.bean.Individual;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class In_AddFilesActivity extends HomeActivity {
    private TextView cc1;
    private ImageView imgIdentification;
    private ImageView imgBankbook;
    private final static int TAKE_PICTURE = 1;
    private String currentPhotoPath;
    private String currentFileName;
    private int IMG_CODE = 200;
    private int imgId;
    private int GRANT_CODE = 200;
    //private RadioGroup rg1;
    //private RadioGroup rg2;
    //private TextView open1;
    //private TextView open2;
    private ContractDB contractDB = ContractDB.getInstance();
    private  String sign1 =null;
    private String sign2 =null;
    private boolean isSetImg = false;
    private boolean isLoadImg = false;
    private String IMG_PATH1;
    private String IMG_PATH2;
    private boolean isCamera1;
    private boolean isCamera2;
    private String fileNamepng1;
    private String fileNamepng2;
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    private ProgressDialog progressDialog;
    private Individual in = new Individual();
    private CommonFile commonFile = new CommonFile();

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/22110d92f03a469d9f76cb959fbe8701";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void onClickImgIdentification(View v) {

        Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
        intent.putExtra("ContractType", "in");
        intent.putExtra("img", "3");
        startActivity(intent);


    }

    public void onClickImgBankBook(View v) {

        Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
        intent.putExtra("ContractType", "in");
        intent.putExtra("img", "4");
        startActivity(intent);
    }


    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_addfiles);
        colorChange();
        imgIdentification = findViewById(R.id.imgIdentification);
        imgBankbook = findViewById(R.id.imgBankbook);
        //open1 = findViewById(R.id.open1);
        //open2 = findViewById(R.id.open2);
        //rg1= findViewById(R.id.rg1);
        //rg2= findViewById(R.id.rg2);
        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        IMG_PATH1 = getCacheDir().toString() + "/";
        IMG_PATH2 = getCacheDir().toString() + "/";

        goMyContract();
    }
    public void goHome(View v){
        Intent intent = new Intent(getApplication(), CancelPopupActivity.class);
        intent.putExtra("main","계약서 작성취소");
        intent.putExtra("text","작성중인 계약서가 취소됩니다.");
        intent.putExtra("where","login");
        startActivity(intent);
    }
    public void addGrantCamera(){
        int permssionCheckCAMERA = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        if (permssionCheckCAMERA!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //거부
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                //처음
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }else{
            addGrantGallery();
        }
    }
    public void addGrantGallery(){
        boolean permssionCheckGallery = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if(!permssionCheckGallery){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GRANT_CODE);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GRANT_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //승인허가
                    addGrantGallery();
                } else {
                    //popup
                    Intent intent= new Intent(getApplication(),NotPermissionPopupActivity.class);
                    intent.putExtra("permission","in");
                    startActivity(intent);
                }
                return;
        }
        if(requestCode==GRANT_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //승인허가
                Log.d("TAG", "onRequestPermissionsResult: 승인허가");
            }else{
                Log.d("TAG", "onRequestPermissionsResult: 승인거부");
                //popup
                Intent intent= new Intent(getApplication(),NotPermissionPopupActivity.class);
                intent.putExtra("permission","in");
                startActivity(intent);
            }
        }
    }
    //계약서 보여주기
    public void goMyContract() {
        HashMap<String,String> map=new HashMap<>();
        map.put("in_idnum",SessionMb.mb_idnum);
        progressDialog.show();
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
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        in= gson.fromJson(body.trim(), in.getClass());

                        if("true".equals(in.getResult())){
                            Individual.seq = in.getIn_seq();
                            fileDown(in.getPath3(),imgIdentification);//신분증
                            fileDown(in.getPath4(),imgBankbook);//통장사본

                            addGrantCamera();
                        }else{
                            Log.d("goMyContract: ", "empty: ");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(In_AddFilesActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };
    public void fileDown(String url, ImageView img){
        if(url!=null){
            try {
                Bitmap result = new FilesTask().execute(url).get();
                img.setImageBitmap(result);
                isLoadImg=true;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            isLoadImg=false;
            return;
        }
    }

    public void OpenIdentificationPhoto(View v) {
        imgId = R.id.btnIdentification_ga;
        openGallery(v);
    }

    public void OpenIdentificationCamera(View v) {
        imgId = R.id.btnIdentification_ca;
        openCamera(v);
    }

    public void OpenBankbookPhoto(View v) {
        imgId = R.id.btnBankbook_ga;
        openGallery(v);
    }

    public void OpenBankbookCamera(View v) {
        imgId = R.id.btnBankbook_ca;
        openCamera(v);
    }
//
//    public void open(View v){
//        TextView tv=findViewById(v.getId());
//        if(tv.equals(open1)){
//            int id = rg1.getCheckedRadioButtonId();
//            imgId = id;
//            if(id<0){
//                Toast.makeText(this, "선택 후 이용해 주세요", Toast.LENGTH_SHORT).show();
//            }else{
//                if(id==R.id.btnIdentification_ga){
//                    openGallery(v);
//                }else if(id==R.id.btnIdentification_ca){
//                    openCamera(v);
//                }
//            }
//        }else if(tv.equals(open2)){
//            int id = rg2.getCheckedRadioButtonId();
//            imgId = id;
//            if(id<0){
//                Toast.makeText(this, "선택 후 이용해 주세요", Toast.LENGTH_SHORT).show();
//            }else{
//                if(id==R.id.btnBankbook_ga){
//                    openGallery(v);
//                }else if(id==R.id.btnBankbook_ca){
//                    openCamera(v);
//                }
//            }
//        }
//    }

    //다음 버튼
    public void goInBankFrm(View v){
        if (imgIdentification.getDrawable() == null || imgBankbook.getDrawable() == null) {
            Toast.makeText(this, "파일을 첨부해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isLoadImg == true && isSetImg == false){
            Intent intent = new Intent(getApplicationContext(), In_WriteBankActivity.class);
            startActivity(intent);
            finish();

        }else {
            progressDialog.show();
            Log.d("TAG", " ## goInServiceFrm addfile DB save start");
            HashMap<String, String> map = new HashMap<>();
            map.put("in_idnum", SessionMb.mb_idnum);
            if(sign1!=null && sign2!=null){
                //2개다
                if(fileUp(fileNamepng1,"3") && fileUp(fileNamepng2,"4"))insertSuccess();
                else    faileFile();
            }else if(sign1!=null){
                if(fileUp(fileNamepng1,"3"))insertSuccess();
                else    faileFile();
            }else if(sign2!=null){
               if(fileUp(fileNamepng2,"4"))insertSuccess();
               else    faileFile();
            }
        }
    }
    public void insertSuccess(){
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), In_WriteBankActivity.class);
            startActivity(intent);
            finish();
    }
    public void faileFile(){
        progressDialog.dismiss();
        Toast.makeText(In_AddFilesActivity.this, "파일 저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
        return;
    }

    public boolean fileUp(String fileName,String fileGroup){
        boolean result=false;
        String imgUploadURL = Admin.sic_url+"/app/Appdbconn?page=imgUpload";
        String path = "";

        if(fileGroup == "3") {

            if (isCamera1 == true)
                path = IMG_PATH1;
            else
                path = IMG_PATH1 + fileName;
        }
        else if(fileGroup == "4") {
            if (isCamera2 == true)
                path = IMG_PATH2;
            else
                path = IMG_PATH2 + fileName;
        }

        try {
            result =new ImgUploadTask().execute(imgUploadURL,path,fileGroup, in.getIn_seq()).get();
            Log.d("TAG", "ImgUploadTask||result: "+result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     return result;
    }
    //back
    public void back(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), In_WriteBasicActivity.class);
        startActivity(intent);
        finish();
    }
    //(필수)색상변경
    public void colorChange() {
        cc1 = findViewById(R.id.cc1);
        String str = cc1.getText().toString();
        SpannableString st = new SpannableString(str);
        //5--"[필수]"전까지의 수
        int s = str.indexOf("[");
        int e = str.length();
        st.setSpan(new ForegroundColorSpan(Color.parseColor("#495EFA")), s, e, SPAN_EXCLUSIVE_EXCLUSIVE);
        cc1.setText(st);
    }
    public void openGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_CODE);
    }
    //camera
    public void openCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(cameraIntent, TAKE_PICTURE);
            File photoFile = null;

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
            String today = simpleDate.format(date);
            String mbId = SessionMb.mb_id;
            String fileNamepng = mbId + "_" + today;   //저장할 파일 이름(아이디_현재날짜시간.png)

            currentFileName = fileNamepng + ".png";

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                photoFile = File.createTempFile(fileNamepng, ".png", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }

            currentPhotoPath = photoFile.getAbsolutePath();

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "contract.hee.bukook.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, TAKE_PICTURE);
            }

        }
    }
    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            Bitmap bitmap = null;

            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                    bitmap = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                Bitmap resizeBitmap = commonFile.resizeBitmap(bitmap);
                resizeBitmap = commonFile.makeTempFile(file, resizeBitmap);

                if (imgId == R.id.btnIdentification_ca) {
                    //신분증
                    isCamera1 = true;
                    IMG_PATH1 = currentPhotoPath;
                    imgIdentification = findViewById(R.id.imgIdentification);
                    imgIdentification.setImageBitmap(resizeBitmap);
                    sign1="Y";
                    fileNamepng1 = currentFileName;
                    isSetImg=true;
                    fileUp(fileNamepng1, "3");
                } else if(imgId == R.id.btnBankbook_ca) {
                    //통장사본
                    isCamera2 = true;
                    IMG_PATH2 = currentPhotoPath;
                    imgBankbook = findViewById(R.id.imgBankbook);
                    imgBankbook.setImageBitmap(resizeBitmap);
                    sign2="Y";
                    fileNamepng2= currentFileName;
                    isSetImg=true;
                    fileUp(fileNamepng2, "4");
                }
            }
        }
        if (requestCode == IMG_CODE) {
            if (resultCode == RESULT_OK) {
                File storage = getCacheDir();
                String fileNamepng = commonFile.makeFilename();
                //storage 에 파일 인스턴스를 생성합니다.
                File tempFile = new File(storage, fileNamepng);
                Bitmap bitmap = null;

                if (Build.VERSION.SDK_INT >= 29) {
                    try {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                if (bitmap != null) {
                    //비트맵 리사이징
                    Bitmap resizeBitmap =commonFile.resizeBitmap(bitmap);
                    //1. 내부저장소(캐시)에 먼저 저장한다.
                    resizeBitmap = commonFile.makeTempFile(tempFile,resizeBitmap);
                    if(resizeBitmap!=null) {
                        if(imgId == R.id.btnIdentification_ga) {
                            imgIdentification = findViewById(R.id.imgIdentification);
                            imgIdentification.setImageBitmap(resizeBitmap);
                            sign1="Y";
                            fileNamepng1 = fileNamepng;
                            isSetImg=true;
                            fileUp(fileNamepng1, "3");
                        } else if(imgId == R.id.btnBankbook_ga) {
                            imgBankbook = findViewById(R.id.imgBankbook);
                            imgBankbook.setImageBitmap(resizeBitmap);
                            sign2="Y";
                            fileNamepng2 = fileNamepng;
                            isSetImg=true;
                            fileUp(fileNamepng2, "4");
                        }
                    }
                }

            }//RESULT_OK if
        }//IMG_CODE
    }//onActivityResult
}