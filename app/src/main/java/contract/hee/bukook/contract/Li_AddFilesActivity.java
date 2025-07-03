package contract.hee.bukook.contract;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
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
import contract.hee.bukook.bean.Licensee;
import contract.hee.bukook.bean.SessionMb;
import contract.hee.bukook.db.ContractDB;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class Li_AddFilesActivity extends HomeActivity {
    private TextView cc1;
    private ImageView imgIdentification;
    private ImageView imgBankbook;
    private ImageView imgLicense;
    private final static int TAKE_PICTURE = 1;
    private String currentPhotoPath;
    private String currentFileName;
    private int IMG_CODE = 200;
    private int imgId;
    private int GRANT_CODE = 200;
//    private RadioGroup rg1;
//    private RadioGroup rg2;
//    private RadioGroup rg3;
//    private TextView open1;
//    private TextView open2;
//    private TextView open3;
    private String[] fileName = new String[3];
    private String[] fi_group = new String[3];
    private String sign1;
    private String sign2;
    private String sign3;
    private String IMG_PATH1;
    private String IMG_PATH2;
    private String IMG_PATH3;
    private String fileNamepng1;
    private String fileNamepng2;
    private String fileNamepng3;
    private boolean isCamera1;
    private boolean isCamera2;
    private boolean isCamera3;
    private ContractDB contractDB = ContractDB.getInstance();
    private boolean setImg =false;
    private ProgressDialog progressDialog;
    private Licensee li = new Licensee();
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    private CommonFile commonFile= new CommonFile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li__addfiles);
        colorChange();
        imgIdentification = findViewById(R.id.imgIdentification);
        imgBankbook = findViewById(R.id.imgBankbook);
        imgLicense =findViewById(R.id.imgLicense);
//        open1 = findViewById(R.id.open1);
//        open2 = findViewById(R.id.open2);
//        open3 = findViewById(R.id.open3);
//        rg1= findViewById(R.id.rg1);
//        rg2= findViewById(R.id.rg2);
//        rg3= findViewById(R.id.rg3);
        IMG_PATH1= getCacheDir().toString() + "/";
        IMG_PATH2= getCacheDir().toString() + "/";
        IMG_PATH3= getCacheDir().toString() + "/";

        progressDialog= new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요.");
        goMyContract();



    }

    public void onClickHelp(View v) {
        String url = "https://www.notion.so/6147151867c949199a35ffe20368eac2";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public byte[] ConvertImgToBytes(ImageView view) {
        if (view.getDrawable() == null) {
            return null;
        }

        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    public void onClickImgIdentification(View v) {

        Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
        intent.putExtra("ContractType", "li");
        intent.putExtra("img", "3");
        startActivity(intent);

//        byte[] byteArray = ConvertImgToBytes(imgIdentification);
//
//        if (byteArray != null){
//            Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
//            intent.putExtra("ContractType", "li");
//            intent.putExtra("img", byteArray);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "사진을 불러올수 없습니다. 다시 첨부해주세요.", Toast.LENGTH_SHORT).show();
//        }

    }

    public void onClickImgBankBook(View v) {

        Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
        intent.putExtra("ContractType", "li");
        intent.putExtra("img", "4");
        startActivity(intent);

//        byte[] byteArray = ConvertImgToBytes(imgBankbook);
//
//        if (byteArray != null){
//            Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
//            intent.putExtra("ContractType", "li");
//            intent.putExtra("img", byteArray);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "사진을 불러올수 없습니다. 다시 첨부해주세요.", Toast.LENGTH_SHORT).show();
//        }

    }

    public void onClickImgLicense(View v) {

        Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
        intent.putExtra("ContractType", "li");
        intent.putExtra("img", "5");
        startActivity(intent);

//        byte[] byteArray = ConvertImgToBytes(imgLicense);
//
//        if (byteArray != null){
//            Intent intent = new Intent(getApplication(), PhotoViewActivity.class);
//            intent.putExtra("ContractType", "li");
//            intent.putExtra("img", byteArray);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "사진을 불러올수 없습니다. 다시 첨부해주세요.", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void didTapContractHome(View v) {
        super.didTapContractHome(v);
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
                Log.d("TAG", "1---카메라 권한ADD");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                //처음
                Log.d("TAG", "2---카메라 권한ADD");
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
                    intent.putExtra("permission","li");
                    startActivity(intent);
                }
                return;
        }
        if(requestCode==GRANT_CODE){
            Log.d("TAG", "GRANT_CODE: "+grantResults.length);
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //승인허가
                Log.d("TAG", "onRequestPermissionsResult: 승인허가");
            }else{
                Log.d("TAG", "onRequestPermissionsResult: 승인거부");
                //popup
                Intent intent= new Intent(getApplication(),NotPermissionPopupActivity.class);
                intent.putExtra("permission","li");
                startActivity(intent);
            }
        }
    }
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
                @Override
                public void run() {
                    Log.d("TAG", "run: " + body.trim());
                    Gson gson = new Gson();
                    try {
                        li = gson.fromJson(body.trim(), li.getClass());
                        if("true".equals(li.getResult())){
                            fileDown(li.getPath3(),imgIdentification);//신분증
                            fileDown(li.getPath4(),imgBankbook);//통장사본
                            fileDown(li.getPath5(),imgLicense);//사업자등록증

                            addGrantCamera();
                        }else{
                            Toast.makeText(Li_AddFilesActivity.this, "계약서 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Li_AddFilesActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            progressDialog.dismiss();
        }
    };
    public void fileDown(String url, ImageView img){
        Log.d("TAG", "fileDown: "+url);
       if(url!=null){
           try {
               Bitmap result = new FilesTask().execute(url).get();
               Log.d("TAG", "file: "+result);
               img.setImageBitmap(result);
               setImg=true;
           } catch (ExecutionException e) {
               e.printStackTrace();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }else{
           setImg=false;
           return;
       }
    }

    public void OpenLiIdentificationPhoto(View v) {
        imgId = R.id.li_btnIdentification_ga;
        openGallery(v);
    }

    public void OpenLiIdentificationCamera(View v) {
        imgId = R.id.li_btnIdentification_ca;
        openCamera(v);
    }

    public void OpenLiBankbookPhoto(View v) {
        imgId = R.id.li_btnBankbook_ga;
        openGallery(v);
    }

    public void OpenLiBankbookCamera(View v) {
        imgId = R.id.li_btnBankbook_ca;
        openCamera(v);
    }

    public void OpenBsNumPhoto(View v) {
        imgId = R.id.bs_num_ga;
        openGallery(v);
    }

    public void OpenBsNumCamera(View v) {
        imgId = R.id.bs_num_ca;
        openCamera(v);
    }

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
//        }else if(tv.equals(open3)){
//            int id = rg3.getCheckedRadioButtonId();
//            imgId = id;
//            if(id<0){
//                Toast.makeText(this, "선택 후 이용해 주세요", Toast.LENGTH_SHORT).show();
//            }else{
//                if(id==R.id.bs_num_ga){
//                    openGallery(v);
//                }else if(id==R.id.bs_num_ca){
//                    openCamera(v);
//                }
//            }
//        }
//    }

    //다음 버튼
    public void goLiBankFrm(View v){
        if (imgIdentification.getDrawable() == null || imgBankbook.getDrawable() == null ||  imgLicense.getDrawable() == null) {
            Toast.makeText(this, "파일을 첨부해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(setImg){
            Intent intent = new Intent(getApplicationContext(), Li_WriteBankActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        progressDialog.show();

        Log.d("TAG", " ## goInServiceFrm addfile DB save start");
        HashMap<String, String> map = new HashMap<>();
        map.put("li_idnum", SessionMb.mb_idnum);
        if(sign1!=null && sign2!=null && sign3!=null){
            if(fileUp(fileNamepng1,"3") && fileUp(fileNamepng2,"4") &&fileUp(fileNamepng3,"5")) insertSuccess();
            else    faileFile();
        }else if(sign1!=null && sign2!=null){
            if(fileUp(fileNamepng1,"3") && fileUp(fileNamepng2,"4"))insertSuccess();
            else    faileFile();
        }else if(sign2!=null && sign3!=null){
            if(fileUp(fileNamepng2,"4") &&fileUp(fileNamepng3,"5"))insertSuccess();
            else    faileFile();
        }else if(sign1!=null &&sign3!=null){
            if(fileUp(fileNamepng1,"3") &&fileUp(fileNamepng3,"5"))insertSuccess();
            else    faileFile();
        }else if(sign1!=null){
            if(fileUp(fileNamepng1,"3"))insertSuccess();
            else    faileFile();
        }else if(sign2!=null){
            if(fileUp(fileNamepng2,"4"))insertSuccess();
            else    faileFile();
        }else if(sign3!=null){
            if(fileUp(fileNamepng3,"5"))insertSuccess();
            else    faileFile();
        }
    }
    public void insertSuccess(){
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), Li_WriteBankActivity.class);
        startActivity(intent);
        finish();
    }
    public void faileFile(){
        progressDialog.dismiss();
        Toast.makeText(Li_AddFilesActivity.this, "파일 저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
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
        else if(fileGroup == "5") {
            if (isCamera3 == true)
                path = IMG_PATH3;
            else
                path = IMG_PATH3 + fileName;
        }


        try {
            result =new ImgUploadTask().execute(imgUploadURL,path,fileGroup,li.getLi_seq()).get();
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
        Intent intent = new Intent(getApplication(), Li_WriteBasicActivity.class);
        startActivity(intent);
        finish();
    }
    //(필수)색상변경
    public void colorChange() {
        cc1 = findViewById(R.id.cc1);
        String str = cc1.getText().toString();
        SpannableString st = new SpannableString(str);
        //5("(필수)"전까지의 수)
        int s = str.indexOf("[");
        int e = str.length();
        st.setSpan(new ForegroundColorSpan(Color.parseColor("#495EFA")), s, e, SPAN_EXCLUSIVE_EXCLUSIVE);
        cc1.setText(st);
    }
    public void openGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_CODE);
    }
    //camera
    public void openCamera(View v)  {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null) {

            //temp파일 생성
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

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }

        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // 카메라로 촬영 가져오는 부분
    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //카메라로 찍기
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            Bitmap bitmap = null;
            ExifInterface exif = null;


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

                if(imgId == R.id.li_btnIdentification_ca) {
                    isCamera1 = true;
                    IMG_PATH1 = currentPhotoPath;
                    imgIdentification = findViewById(R.id.imgIdentification);
                    imgIdentification.setImageBitmap(resizeBitmap);
                    sign1 = "Y";
                    fileNamepng1 = currentFileName;
                    setImg = false;
                    fileUp(fileNamepng1, "3");
                } else if(imgId == R.id.li_btnBankbook_ca) {
                    isCamera2 = true;
                    IMG_PATH2 = currentPhotoPath;
                    imgBankbook = findViewById(R.id.imgBankbook);
                    imgBankbook.setImageBitmap(resizeBitmap);
                    sign2 = "Y";
                    fileNamepng2 = currentFileName;
                    setImg = false;
                    fileUp(fileNamepng2, "4");
                } else if(imgId == R.id.bs_num_ca) {
                    isCamera3 = true;
                    IMG_PATH3 = currentPhotoPath;
                    imgLicense = findViewById(R.id.imgLicense);
                    imgLicense.setImageBitmap(resizeBitmap);
                    sign3 = "Y";
                    fileNamepng3 = currentFileName;
                    setImg = false;
                    fileUp(fileNamepng3, "5");
                }
            }
        }

        //갤러리에서 가져오기
        if (requestCode == IMG_CODE) {
            if (resultCode == RESULT_OK) {
                File storage = getCacheDir();
                String fileNamepng = commonFile.makeFilename();
                File tempFile = new File(storage, fileNamepng);
                Bitmap bitmap = null;

                if (Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                }
                //비트맵 리사이징
                Bitmap resizeBitmap =commonFile.resizeBitmap(bitmap);
        /*
            1. 내부저장소(캐시)에 먼저 저장한다.
        */
                resizeBitmap = commonFile.makeTempFile(tempFile,resizeBitmap);

                if(bitmap!=null) {
                    if(imgId == R.id.li_btnIdentification_ga) {
                        imgIdentification = findViewById(R.id.imgIdentification);
                        imgIdentification.setImageBitmap(resizeBitmap);
                        sign1="Y";
                        fileNamepng1 = fileNamepng;
                        setImg=false;
                        fileUp(fileNamepng1, "3");
                    } else if(imgId == R.id.li_btnBankbook_ga) {
                        imgBankbook = findViewById(R.id.imgBankbook);
                        imgBankbook.setImageBitmap(resizeBitmap);
                        sign2="Y";
                        fileNamepng2 = fileNamepng;
                        setImg=false;
                        fileUp(fileNamepng2, "4");
                    } else if(imgId == R.id.bs_num_ga) {
                        imgLicense = findViewById(R.id.imgLicense);
                        imgLicense.setImageBitmap(resizeBitmap);
                        sign3="Y";
                        fileNamepng3= fileNamepng;
                        setImg=false;
                        fileUp(fileNamepng3, "5");
                    }
                }
            }//RESULT_OK if
        }//IMG_CODE
    }//onActivityResult
}