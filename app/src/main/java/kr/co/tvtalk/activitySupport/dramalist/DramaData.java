package kr.co.tvtalk.activitySupport.dramalist;
/**
 * Created by kwongyo on 2016-09-16.
 */
public class DramaData {
    public String dramaImage;
    public String dramaCountInfomation;
    public String dramaBroadcastDay;
    public String infomationEnterChattingRoom;
    public int iconEnterChattingRoom;
    public String key;

    public DramaData(String dramaImage, String dramaCountInfomation, String dramaBroadcastDay, String infomationEnterChattingRoom, int iconEnterChattingRoom, String key) {
        this.dramaImage = dramaImage;
        this.dramaCountInfomation = dramaCountInfomation;
        this.dramaBroadcastDay = dramaBroadcastDay;
        this.infomationEnterChattingRoom = infomationEnterChattingRoom;
        this.iconEnterChattingRoom = iconEnterChattingRoom;
        this.key = key;
    }
}

