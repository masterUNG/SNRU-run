package appewtc.masterung.snrurun;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 5/17/16 AD.
 */
public class MyManage {

    //Explicit
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static final String user_table = "userTABLE";
    public static final String column_id = "_id";
    public static final String column_name = "Name";
    public static final String column_user = "User";
    public static final String column_password = "Password";
    public static final String column_avata = "Avata";

    public MyManage(Context context) {

        myOpenHelper = new MyOpenHelper(context);
        sqLiteDatabase = myOpenHelper.getWritableDatabase();

    }   // Constructor

    public long addUser(String strName,
                        String strUser,
                        String strPassword,
                        String strAvata) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(column_name, strName);
        contentValues.put(column_user, strUser);
        contentValues.put(column_password, strPassword);
        contentValues.put(column_avata, strAvata);

        return sqLiteDatabase.insert(user_table, null, contentValues);
    }


}   // Main Class
