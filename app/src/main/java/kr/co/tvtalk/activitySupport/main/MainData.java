package kr.co.tvtalk.activitySupport.main;

/**
 * Created by kwongyo on 2016-09-11.
 * CardView for main Activity
 * MainData class access modifier is all public for perfomance.
 */
public class MainData {
    public String broadcastImage;
    public String broadcastName;
    public int isBookmark;
    public int broadcastKindOf;
    public String broadcastDescription;
    public String key;

    public MainData(String broadcastImage, String broadcastName, int isBookmark, int broadcastKindOf, String broadcastDescription, String key) {
        this.broadcastImage = broadcastImage;
        this.broadcastName = broadcastName;
        this.isBookmark = isBookmark;
        this.broadcastKindOf = broadcastKindOf;
        this.broadcastDescription = broadcastDescription;
        this.key = key;

    }

    @Override
    public String toString() {
        return "MainData{" +
                "broadcastImage='" + broadcastImage + '\'' +
                ", broadcastName='" + broadcastName + '\'' +
                ", isBookmark=" + isBookmark +
                ", broadcastKindOf=" + broadcastKindOf +
                ", broadcastDescription='" + broadcastDescription + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
