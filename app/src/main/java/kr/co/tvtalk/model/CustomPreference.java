package kr.co.tvtalk.model;


import android.content.Context;
import android.content.SharedPreferences;


public class CustomPreference {

    public static CustomPreference getInstance(Context context) {
        mContext = context;
        return customPreference;
    }
    static {
        customPreference = new CustomPreference();
    }
    private static CustomPreference customPreference;
    private final String PREF_NAME = "com.example.kwongyo.firetalk";
    public final static String PREF_INTRO_USER_AGREEMENT = "PREF_USER_AGREEMENT";
    public final static String PREF_MAIN_VALUE = "PREF_MAIN_VALUE";

    static Context mContext;

    private CustomPreference() {}

    public void remove(String key) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.clear();
        editor.commit();
    }

    //여기서 put메서드 하나만 설명하면 어떤 키값을 가진 벨류를 사용할거다 이거야. 그러니깐
    //CustomPreference를 멤버변수로 가진 엑티비티에서는 어떤 키값으로 저장된 데이터를
    //put메서드를 통해서 저장할 수 있는거지

    public void put(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        //안을 보면 딱봐도 클래스 이름부터가 뭔가를 공유할거다 이말이잖아. 그러니깐 어떤 공유하기위한 데이터를
        //만든다 이런식으로 생각하고 사용은 이런 방식으로 하면된다정도만 알아둬

        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, Boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, long value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }
    //그리고 put 메서드를 이용해서 데이터가 저장되면 getValue 메서드를 호출해서 그 값을 엑티비티 내에서 쓸수 가 있어.

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public long getValue(String key, long dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }


    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                mContext.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }
}