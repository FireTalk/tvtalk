package kr.co.tvtalk.model;

/**
 * Created by 병윤 on 2016-09-26.
 */

public class MemberDTO {
    public String email;
    public String nickname;
    public String profile;
    public boolean facebook;

    public MemberDTO(){

    }

    public MemberDTO(String email, String nickname, String profile, boolean facebook) {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.facebook = facebook;
    }
}
