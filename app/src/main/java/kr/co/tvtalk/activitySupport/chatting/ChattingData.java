package kr.co.tvtalk.activitySupport.chatting;



public class ChattingData extends Data{


    public String anotherTextMessage;
    public int anotherEmoticon;
    public String key;


    public ChattingData(String anotherProfileImage, String anotherName,String anotherTextMessage ,AskPersonInfo personInfo, int anotherEmoticon, String key ){
        super(anotherProfileImage,anotherName,personInfo, key);
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