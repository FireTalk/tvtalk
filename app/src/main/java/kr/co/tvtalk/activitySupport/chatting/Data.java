package kr.co.tvtalk.activitySupport.chatting;

/**
 * Created by kwongyo on 2016-10-03.
 */

public abstract class Data {
    public String anotherProfileImage;
    public String anotherName;
    public AskPersonInfo personInfo;
//    public String emoticon;
    public String key;

    public Data(String anotherProfileImage, String anotherName, AskPersonInfo personInfo, String key) {
        this.anotherProfileImage = anotherProfileImage;
        this.anotherName = anotherName;
        this.personInfo = personInfo;
        this.key = key;
//        this.emoticon = emoticon;
    }

    public int getEmotion(){
        return 0;
    }
    public String getAnotherTextMessage(){
        return "";
    }
    public boolean isLike(){
        return true;
    }
    public int getLikeNo(){
        return 0;
    }

    public String getKey() {
        return key;
    }

    public enum AskPersonInfo{
        SAME ,
        SAME_EMOTION,
        ANOTHER,
        ANOTHER_EMOTION,
        ME,
        ME_EMOTION,
        ME_TEXT_WHIT_EMOTION,
        ANOTHER_TEXT_WHIT_EMOTION,
        ANOTHER_TEXT_WHIT_EMOTION_CONTINUE

    }
}