package kr.co.tvtalk.activitySupport.chatting;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class EmotionData extends Data{

    int emotion;
    public EmotionData(String anotherProfileImage, String anotherName, AskPersonInfo personInfo,int emotion) {
        super(anotherProfileImage,anotherName,personInfo);
        this.anotherProfileImage = anotherProfileImage;
        this.anotherName = anotherName;
        this.personInfo = personInfo;
        this.emotion = emotion;
    }
    @Override
    public int getEmotion(){
        return emotion;
    }
}
