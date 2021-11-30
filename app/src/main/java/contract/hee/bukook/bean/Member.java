package contract.hee.bukook.bean;

import lombok.Data;

@Data
public class Member {

    private String mb_idnum;
    private String mb_id;
    private String mb_pw;
    private String mb_name;
    private String mb_phone;
    private String mb_email;
    private String mb_wdate;//등록일
    private String mb_wtime;//등록시간
    private String mb_mdate;//수정일
    private String mb_mtime;//수정시간
    private String mb_lv ="1";
    private String mb_state ="1";
    private String mb_ck3; //마케팅이용약관(선택)
}
