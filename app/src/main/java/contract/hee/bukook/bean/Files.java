package contract.hee.bukook.bean;

import lombok.Data;

@Data
public class Files {

    private String fi_idnum;
    private String fi_seq;
    private String fi_group;//파일그룹(서명1:1, 서명2:2, 신분증:3, 통장사본:4, 사업자등록증:5, 서비스 대행사:6 )
    private String fi_id;
    private String fi_oriname;
    private String fi_sysname;
    private String fi_oripath;
    private String fi_syspath;
    private String fi_fullpath;
    private String fi_dir1;
    private String fi_dir2;
    private String fi_size;//파일크기
    private String fi_type;//확장자
    private String fi_width;//이미지 가로
    private String fi_height;//이미지 세로
    private String fi_wdate;
    private String fi_wtime;
    private String fi_mdate;
    private String fi_mtime;
    private String fi_note;//수정이유
}
