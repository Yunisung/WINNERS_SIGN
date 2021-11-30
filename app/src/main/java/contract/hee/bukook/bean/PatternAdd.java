package contract.hee.bukook.bean;

import android.util.Log;

import java.util.Random;
import java.util.regex.Pattern;

public class PatternAdd {
    //이메일 정규식
    public static final Pattern VALID_EMAIL = java.util.regex.Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", java.util.regex.Pattern.CASE_INSENSITIVE);
    //이름-한글
    public static final Pattern VALID_KOREAN = java.util.regex.Pattern.compile("^[가-힣]*$");
    //이름-영어
    public static final Pattern VALID_ENGLISH = java.util.regex.Pattern.compile("^[a-zA-Z]*$");
    //전화번호
    public static final Pattern VALID_PHONE = java.util.regex.Pattern.compile("^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$");
    //비밀번호
    public static final Pattern VALID_PW = java.util.regex.Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$");
    //아이디
    public static final Pattern VALID_ID = java.util.regex.Pattern.compile("^[a-zA-Z0-9]{4,12}$");

    //주민등록번호
    public static final Pattern VALID_RRN = java.util.regex.Pattern.compile("\\d{2}([0]\\d|[1][0-2])([0][1-9]|[1-2]\\d|[3][0-1])[-]*[1-8]\\d{6}");
    //생년월일
    public static final Pattern VALID_BIRTH = java.util.regex.Pattern.compile("^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$");

    //대표번호
    public static final Pattern VALID_TEL = java.util.regex.Pattern.compile("^(15|16|18)[0-9]{2}-?[0-9]{4}$");

    //관리자 아이디(2~10자이내의 특수문자 불포함)
    public static final Pattern VALID_TEXT =  java.util.regex.Pattern.compile("^[가-힣a-zA-Z0-9]+$");

    public static boolean validateEmail(String emailStr) {
        return VALID_EMAIL.matcher(emailStr).matches();
    }

    public static boolean validatePhone(String phoneStr) {
        return VALID_PHONE.matcher(phoneStr).matches();
    }
    
    public static boolean validateRrn(String rrnStr) {
        return VALID_RRN.matcher(rrnStr).matches();
    }

    public static String validateName(String nameStr) {
        if (!VALID_KOREAN.matcher(nameStr).matches() && !VALID_ENGLISH.matcher(nameStr).matches()) {
            return "이름을 다시 한번 확인해 주세요";
        } else {
            if (VALID_KOREAN.matcher(nameStr).matches()) {
                if (nameStr.length() < 2) {
                    return "이름을 2글자 이상 적어주세요.";
                } else {
                    return "ok";
                }
            } else if (VALID_ENGLISH.matcher(nameStr).matches()) {
                if (nameStr.length() < 5) {
                    return "영문 이름은 5자 이상이어야 합니다.";
                } else {
                    return "ok";
                }
            }
        }
        return "";
    }

    public static boolean validatePw(String pwStr) {
        return VALID_PW.matcher(pwStr).matches();
    }
    public static boolean validateId(String idStr) {
        return VALID_ID.matcher(idStr).matches();
    }


    //계좌번호 조건(11~16)
    public static boolean validateBankNum(String bankNumStr){
        if(bankNumStr.length()>10 && bankNumStr.length()<17){
            return true;
        }
        return false;
    }

    public static boolean validateBirth(String birthStr){
        Log.d("TAG", "validateBirth----birthStr: "+birthStr);
        return VALID_BIRTH.matcher(birthStr).matches();
    }

    public static String validateText(String textStr){
      if(textStr.length()>1 && textStr.length()<11){
          if(VALID_TEXT.matcher(textStr).matches()) {
              return "ok";
          }
          return " 한글, 영문, 숫자만 사용이 가능합니다.";
      }
      return " 2~10자이내 입니다.";
    }

    public static boolean validateTel(String telStr){
        if(VALID_TEL.matcher(telStr).matches() || VALID_PHONE.matcher(telStr).matches()){
            return true;
        }
        return false;
    }


    public static String createPhoneCode() {
        Random rand = new Random();
        String num = "";
        for (int i = 0; i < 6; i++) {
            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            num += ran;
        }
        return num;
    }//랜덤 인증숫자



}//PatternAdd
