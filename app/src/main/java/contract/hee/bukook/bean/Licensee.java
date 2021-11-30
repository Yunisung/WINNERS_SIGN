package contract.hee.bukook.bean;

import lombok.Data;

@Data
public class Licensee {

    private String li_idnum;
    private String li_seq;
    private String li_sv_divide;//서비스 구분(신용카드:1, 가상계좌:2, 계좌이체:3, 휴대폰결제4)
    private String li_company;//상호명
    private String li_bs_divide;//사업자구분(개인:1, 법인:2)
    private String li_admin;
    private String li_bs_num;
    private String li_tel;
    private String li_zipcode;//우편번호
    private String li_addr1;
    private String li_addr2;
    private String li_cindition;//업태
    private String li_event;//종목
    private String li_re_name;
    private String li_re_birth;
    private String li_re_phone;
    private String li_re_email;
    private String li_bank_name;
    private String li_bank_num;
    private String li_depositor;//예금주
    private String li_state;
    private String li_wdate;//등록일
    private String li_wtime;//등록시간
    private String li_mdate;//수정일
    private String li_mtime;//수정시간
    private String li_note;//수정이유
    private String li_sv_charge;//
    private String li_sv_calculate;//
    private String li_sv_cost; //통신비
    private String li_terminal_1="N";//결제수단
    private String li_terminal_2="N";//결제수단
    private String li_terminal_3="N";//결제수단
    private String li_agent;//담당자
    private String li_name;//신청인
    private String path1;//약관동의서명1
    private String path2;//약관동의서명2
    private String path3;//신분증
    private String path4;//통장사본
    private String path5;//사업자등록증
    private String path6;//서비스대행사
    private String path7;//완료서명
    private String result;//db결과값 true/false

    public static String db_div; //db구분(작성중or신규)
    public static String seq;
}
