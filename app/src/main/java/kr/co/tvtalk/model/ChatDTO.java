package kr.co.tvtalk.model;

import kr.co.tvtalk.activitySupport.chatting.ChattingData;

/**
 * Created by 병윤 on 2016-10-04.
 */

public class ChatDTO {
    String msg, emoticon, nickname, photo, key, uid;
    ChattingData.AskPersonInfo isSamePerson;

    public ChatDTO(){

    }

    public ChatDTO(String msg, String emoticon, String nickname, String photo, String key, String uid, ChattingData.AskPersonInfo isSamePerson) {
        this.msg = msg;
        this.emoticon = emoticon;
        this.nickname = nickname;
        this.photo = photo;
        this.key = key;
        this.uid = uid;
        this.isSamePerson = isSamePerson;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public String getEmoticon() {
        return emoticon;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public String getKey() {
        return key;
    }

    public ChattingData.AskPersonInfo getIsSamePerson() {
        return isSamePerson;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setEmoticon(String emoticon) {
        this.emoticon = emoticon;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setIsSamePerson(ChattingData.AskPersonInfo isSamePerson) {
        this.isSamePerson = isSamePerson;
    }
}
