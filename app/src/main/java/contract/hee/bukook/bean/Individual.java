package contract.hee.bukook.bean;

import lombok.Data;

@Data
public class Individual {

    private String in_idnum;
    private String in_seq;
    private String in_name;
    private String in_rrn;
    private String in_phone;
    private String in_company;
    private String in_admin;
    private String in_zipcode;
    private String in_addr1;
    private String in_addr2;
    private String in_item; //판매품목
    private String in_tel;
    private String in_bank_name;
    private String in_bank_num;
    private String in_depositor;
    private String in_state;
    private String in_wdate;
    private String in_wtime;
    private String in_mdate;
    private String in_mtime;
    private String in_note;
    private String in_sv_company;
    private String in_sv_charge;
    private String in_sv_calculate;
    private String in_sv_cost;
    private String in_sv_bs_num;
    private String in_terminal_1="N";//결제수단
    private String in_terminal_2="N";//결제수단
    private String in_terminal_3="N";//결제수단
    private String in_agent;//담당자
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

    public String getIn_item() {
        return in_item;
    }
    public String getIn_sv_cost() { return in_sv_cost;}

}
