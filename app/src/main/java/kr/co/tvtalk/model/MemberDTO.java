package kr.co.tvtalk.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 병윤 on 2016-09-29.
 */

public class MemberDTO {
    String nickname;
    String profile;
    String email;
    boolean facebook;

    public MemberDTO(){

    }

    public MemberDTO(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile() {
        return profile;
    }






}
