package kr.co.tvtalk.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kr.co.tvtalk.activitySupport.main.MainData;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private Context context;
    public static final String DATABASE_NAME="FRIETALK.db";
    public static final int DATABASE_VERSION=1;
    public static final String DRAMA_TABLE = "drama_table"; //
    public static final String CHATTING_TABLE = "chatting_table";

    public static final String DRAMA_TABLE_CREATE ="create table "+DRAMA_TABLE+" (dt_name text primary key , image_path text , broadcast_brand text , broadcast_time text , isbookmark integer )";
    /*
    CHATTING_TABLE을 만들면서 생긴 이슈.
    각 컬럼마다 말한 사람의 이름을 저장할 것인가?? - 정규화가 되지 않아서 데이터의 중복이 생김.
    정규화를 하면서 DB를 잘게 쪼개서 만들었다고 치자. 서비스가 오래 지속될 것인가?
    -> 일단은 정규홧 생각하지 않고 DB를 만들어서 쓰자. 나중일은 나중에 생각하는게 좋다.
    서비스가 성공적으로 규모가 점점 커진다면 DB모델링은 다시 해야할것이다.
     */
    public static final String CHATTING_TABLE_CREATE = "create table "+CHATTING_TABLE+" (ct_id integer primary key autoincrement , dt_name text , profile_imaage text , name text , text_message ,foreign key(dt_id) references drama_table(dt_id)";
    private DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if( instance == null )
            instance = new DatabaseHelper(context);
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DRAMA_TABLE_CREATE);
        db.execSQL(CHATTING_TABLE_CREATE);
        db.execSQL("insert into "+DRAMA_TABLE+" VALUES(null,'구르미 그린 달빛','SBS,월·화 오후 11:00',0)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists "+DRAMA_TABLE);
        db.execSQL("drop table if exists "+CHATTING_TABLE);
    }
    public static class DramaDAO {
        /*
        쓰고나보니 MainData가 VO역할을 해주네..
         */
        public int insert(MainData data) {
            SQLiteDatabase db = instance.getWritableDatabase();
            db.execSQL("insert into "+DRAMA_TABLE+" (dt_name,image_path,broadcast_brand,broadcast_time,isbookmark) values("+data.broadcastName+ "," + "-" + "," + data.broadcastKindOf+","+data.broadcastDescription+","+data.isBookmark+")");
            return 0;
        }


    }

}
