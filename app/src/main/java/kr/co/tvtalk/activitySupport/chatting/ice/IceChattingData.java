package kr.co.tvtalk.activitySupport.chatting.ice;

import kr.co.tvtalk.activitySupport.chatting.Data;

/**
 * Created by kwongiho on 2016. 10. 10..
 */

public class IceChattingData extends Data{


    public String anotherTextMessage;
    public int anotherEmoticon;
    public boolean isLike;
    public int likeNo;


    public IceChattingData(String anotherProfileImage, String anotherName,String anotherTextMessage ,AskPersonInfo personInfo, int anotherEmoticon ,String key, boolean isLike , int likeNo ){
        super(anotherProfileImage,anotherName,personInfo, key);
        this.anotherTextMessage=anotherTextMessage;
        this.anotherEmoticon = anotherEmoticon;
        this.isLike = isLike;
        this.likeNo = likeNo;
    }
    @Override
    public String getAnotherTextMessage(){
        return anotherTextMessage;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public void setLikeNo(int likeNo) {
        this.likeNo = likeNo;
    }

    public boolean isLike(){
        return isLike;
    }


    public int getLikeNo(){
        return likeNo;
    }
    @Override
    public int getEmotion() {
        return anotherEmoticon;
    }

}