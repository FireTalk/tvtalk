package kr.co.tvtalk.activitySupport;

import android.content.Context;
import android.graphics.Typeface;

public class FontFactory {
    private static final FontFactory instance;
    static {
        instance = new FontFactory();
    }

    /**
     * 지금은 안쓴다.
     * @return class FontFactoy instance.
     */
    private static FontFactory getInstance() {
        return instance;
    }
    private FontFactory(){}

    /**
     * what kind of do you want font?
     * call this method.
     * Good.
     * @param context when you call the method, live the activty of context
     * @param font ecum of Font list
     * @return Typeface object.
     */
    public static Typeface getFont(Context context , Font font){
        switch (font) {
            case NOTOSANS_BOLD :
                return Typeface.createFromAsset(context.getAssets() , "NotoSans-Bold.ttf" );
            case NOTOSANS_REGULAR:
                return Typeface.createFromAsset(context.getAssets() , "NotoSans-Regular.ttf");
            case ROBOTO_BOLD :
                return Typeface.createFromAsset(context.getAssets() , "Roboto-Bold.ttf");
            case ROBOTO_REQULAR:
                return Typeface.createFromAsset(context.getAssets() , "Roboto-Regular.ttf");
        }
        return null;

    }
    public enum Font{
        NOTOSANS_BOLD,
        NOTOSANS_REGULAR,
        ROBOTO_BOLD,
        ROBOTO_REQULAR
    }
}
