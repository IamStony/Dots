package game.dots;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "HighScoresManager";
    private static final String TABLE_HIGHSCORES = "HighScores";

    private static final String NAME = "name";
    private static final String SCORE = "score";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + TABLE_HIGHSCORES + "(" +
                                        NAME + " TEXT, " + SCORE + " INTEGER)";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);

        // Create tables again
        onCreate(db);
    }

    public void addScore(HighScore score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, score._name);
        values.put(SCORE, score._score);

        db.insert(TABLE_HIGHSCORES, null, values);
        db.close();
    }

    public ArrayList<HighScore> getAllScores() {
        ArrayList<HighScore> scoreList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_HIGHSCORES +
                                " ORDER BY " + SCORE +
                                " DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                HighScore score = new HighScore();
                score._name = cursor.getString(0);
                score._score = cursor.getInt(1);
                scoreList.add(score);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return scoreList;
    }

    public void clearDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_HIGHSCORES);
    }
}
