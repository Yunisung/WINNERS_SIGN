package contract.hee.bukook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import contract.hee.bukook.bean.SessionMb;

public class CommonFile {

    public String maskingFunc(String div, String val){
        String num1;
        String num2;
        String num3;
        if(div == "tel"){
            if(val.length() == 11){ // 11자리 휴대폰 번호 마스킹 처리
                num1 = val.substring(0, 3);
                num2 = val.substring(3, 7);
                num3 = val.substring(7, 11);
                val = num1 + "-" + num2 + "-" + num3;
            }else if(val.length() == 10){ // 10자리 휴대폰 번호 마스킹 처리
                num1 = val.substring(0, 3);
                num2 = val.substring(3, 6);
                num3 = val.substring(6, 10);
                val = num1 + "-" + num2 + "-" + num3;
            }else if(val.length()==8){
                num1= val.substring(0,4);
                num2= val.substring(4,8);
                val = num1 + "-" + num2;
            }
        }else if(div == "birth"){
            num1 = val.substring(0, 4);
            num2 = val.substring(4, 6);
            num3 = val.substring(6, 8);
            val = num1 + "-" + num2 + "-" + num3;
        }else if(div == "date"){
            num1 = val.substring(0, 4);
            num2 = val.substring(4, 6);
            num3 = val.substring(6, 8);
            val = num1 + "년 " + num2 + "월 " + num3 + "일";
        }else if(div == "rrn"){
            num1 = val.substring(0, 6);
            num2 = val.substring(6, 13);
            val = num1 + "-" + num2;
        }else if(div == "bsnum"){
            num1 = val.substring(0, 3);
            num2 = val.substring(3, 5);
            num3 = val.substring(5, 10);
            val = num1 + "-" + num2 + "-" + num3;
        }
        return val;
    }


    public Bitmap resizeBitmap(Bitmap bitmap){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bitmap src = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        return src;

        //Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * 300) / bitmap.getHeight() , 300, true);
        //Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 320 , 240, true);
        //Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        //return resizeBitmap;

    }

    public String makeFilename(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String today = simpleDate.format(date);
        String mbId = SessionMb.mb_id;
        String fileNamepng = mbId + "_" + today + ".png";   //저장할 파일 이름(아이디_현재날짜시간.png)
        return fileNamepng;
    }

    public Bitmap makeTempFile(File tempFile, Bitmap bitmap){
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성합니다.
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비합니다.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            out.close();
        } catch (IOException e) {
            Log.d("error", "makeTempFile");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Boolean isConnect(Context context){
        ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net =cm.getActiveNetworkInfo();
        return net !=null && net.isConnected();

    }
}
