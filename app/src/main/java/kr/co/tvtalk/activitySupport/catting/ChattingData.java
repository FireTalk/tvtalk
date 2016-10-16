package kr.co.tvtalk.activitySupport.catting;



public class ChattingData extends Data{


    public String anotherTextMessage;
    public int anotherEmoticon;


    public ChattingData(String anotherProfileImage, String anotherName,String anotherTextMessage ,AskPersonInfo personInfo, int anotherEmoticon ){
        super(anotherProfileImage,anotherName,personInfo);
        this.anotherTextMessage=anotherTextMessage;
        this.anotherEmoticon = anotherEmoticon;

    }
    @Override
    public String getAnotherTextMessage(){
        return anotherTextMessage;
    }

    @Override
    public int getEmotion() {
        return anotherEmoticon;
    }
}