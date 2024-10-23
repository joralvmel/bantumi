package es.upm.miw.bantumi.dominio.logica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bantumi.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_SEEDS_PLAYER1 = "seeds_player1";
    public static final String COLUMN_SEEDS_PLAYER2 = "seeds_player2";
    public static final String COLUMN_ELAPSED_TIME = "elapsed_time";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SCORES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYER_NAME + " TEXT, " +
                    COLUMN_DATE_TIME + " TEXT, " +
                    COLUMN_SEEDS_PLAYER1 + " INTEGER, " +
                    COLUMN_SEEDS_PLAYER2 + " INTEGER, " +
                    COLUMN_ELAPSED_TIME + " TEXT);";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_SCORES + " ADD COLUMN " + COLUMN_ELAPSED_TIME + " TEXT;");
        }
    }
}