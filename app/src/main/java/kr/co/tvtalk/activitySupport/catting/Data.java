package kr.co.tvtalk.activitySupport.catting;

/**
 * Created by kwongyo on 2016-10-03.
 */

public abstract class Data {
    public int anotherProfileImage;
    public String anotherName;

    public AskPersonInfo personInfo;

    public Data(int anotherProfileImage, String anotherName, AskPersonInfo personInfo) {
        this.anotherProfileImage = anotherProfileImage;
        this.anotherName = anotherName;
        this.personInfo = personInfo;
    }

    public int getEmotion(){
        return 0;
    }
    public String getAnotherTextMessage(){
        return "";
    }

    public enum AskPersonInfo{
        SAME ,
        ANOTHER,
        ME,
        ME_EMOTION
    }
}