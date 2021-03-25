package sudokugame.example.sudoku.control_sudoku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sudoku_data extends SQLiteOpenHelper {
    public sudoku_data(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_str_easy="create table easy_level(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100), hole_sudoku varchar(100))";
        String sql_str_medium="create table medium_level(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100), hole_sudoku varchar(100))";
        String sql_str_hard="create table hard_level(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100), hole_sudoku varchar(100))";
        String time_str_easy="create table easy_time(id INTEGER PRIMARY KEY AUTOINCREMENT, time varchar(100), time_already_use varchar(100))";
        String time_str_medium="create table medium_time(id INTEGER PRIMARY KEY AUTOINCREMENT, time varchar(100), time_already_use varchar(100))";
        String time_str_hard="create table hard_time(id INTEGER PRIMARY KEY AUTOINCREMENT, time varchar(100), time_already_use varchar(100))";
        String backup_easy="create table backup_easy(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100))";
        String backup_medium="create table backup_medium(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100))";
        String backup_hard="create table backup_hard(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100))";
        String music="create table music(click_puzzle INT, theme INT, hint INT)";
        String music_ini="INSERT INTO music (click_puzzle, theme, hint) VALUES (1,1,1)";
        String record="create table record(id INTEGER PRIMARY KEY AUTOINCREMENT,date date, level INT, all_sudoku varchar(100), half_sudoku varchar(100))";
        String mark="create table mark(mark INT)";
        String mark_ini="INSERT INTO mark (mark) VALUES (0)";

        db.execSQL(sql_str_easy);
        db.execSQL(sql_str_medium);
        db.execSQL(sql_str_hard);
        db.execSQL(time_str_easy);
        db.execSQL(time_str_medium);
        db.execSQL(time_str_hard);
        db.execSQL(backup_easy);
        db.execSQL(backup_medium);
        db.execSQL(backup_hard);
        db.execSQL(music);
        db.execSQL(music_ini);
        db.execSQL(record);
        db.execSQL(mark);
        db.execSQL(mark_ini);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
