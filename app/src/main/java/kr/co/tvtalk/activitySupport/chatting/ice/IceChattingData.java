package kr.co.tvtalk.activitySupport.chatting.ice;

import kr.co.tvtalk.activitySupport.chatting.Data;

/**
 * Created by kwongiho on 2016. 10. 10..
 */

public class IceChattingData extends Data{


    public String anotherTextMessage;
    public boolean isLike;
    public int likeNo;


    public IceChattingData(String anotherProfileImage, String anotherName,String anotherTextMessage ,AskPersonInfo personInfo ,String key, boolean isLike , int likeNo ){
        super(anotherProfileImage,anotherName,personInfo, key);
        this.anotherTextMessage=anotherTextMessage;
        this.isLike = isLike;
        this.likeNo = likeNo;
    }
    @Override
    public String getAnotherTextMessage(){
        return anotherTextMessage;
    }
    @Override
    public boolean isLike(){
        return this.isLike;
    }
    @Override
    public int getLikeNo(){
        return this.likeNo;
    }

}