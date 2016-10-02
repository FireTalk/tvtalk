package kr.co.tvtalk.activitySupport.catting;



public class ChattingData extends Data{


    public String anotherTextMessage;


    public ChattingData(String anotherProfileImage, String anotherName,String anotherTextMessage ,AskPersonInfo personInfo ){
        super(anotherProfileImage,anotherName,personInfo);
        this.anotherTextMessage=anotherTextMessage;

    }
    @Override
    public String getAnotherTextMessage(){
        return anotherTextMessage;
    }

}