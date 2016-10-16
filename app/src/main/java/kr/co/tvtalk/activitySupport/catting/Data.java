package kr.co.tvtalk.activitySupport.catting;

/**
 * Created by kwongyo on 2016-10-03.
 */

public abstract class Data {
    public String anotherProfileImage;
    public String anotherName;
    public AskPersonInfo personInfo;
//    public String emoticon;

    public Data(String anotherProfileImage, String anotherName, AskPersonInfo personInfo) {
        this.anotherProfileImage = anotherProfileImage;
        this.anotherName = anotherName;
        this.personInfo = personInfo;
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

    public enum AskPersonInfo{
        SAME ,
        SAME_EMOTION,
        ANOTHER,
        ANOTHER_EMOTION,
        ME,
        ME_EMOTION
    }
}