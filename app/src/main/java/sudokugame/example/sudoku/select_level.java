package sudokugame.example.sudoku;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku.R;

import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.graphical_user_interface.draw_sudoku_grid_easy;
import sudokugame.example.sudoku.graphical_user_interface.draw_sudoku_grid_hard;
import sudokugame.example.sudoku.graphical_user_interface.draw_sudoku_grid_medium;

public class select_level extends AppCompatActivity {

    private sudoku_data easy_database;
    private SQLiteDatabase easy_db;
    private int theme_mode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme_id;
        easy_database =new sudoku_data(getApplicationContext(),"easy.db",null,1);
        easy_db = easy_database.getWritableDatabase();
        sudoku_data easy_database=new sudoku_data(select_level.this,"easy.db",null,1);
        easy_db= easy_database.getWritableDatabase();
        //easy_db= getApplicationContext().openOrCreateDatabase("easy.db", Context.MODE_PRIVATE, null);
        Cursor cursor_for_settings = easy_db.rawQuery("SELECT * FROM music LIMIT 1", null);
        while (cursor_for_settings.moveToNext()) {
            cursor_for_settings.getInt(0);
            theme_mode = cursor_for_settings.getInt(1);
        }
        cursor_for_settings.close();
        switch (theme_mode){
            case 0:
                theme_id= R.style.night_mode;
                break;
            case 1:
                theme_id=R.style.day_mode;
                break;
            default:
                theme_id=R.style.day_mode;
        }
        setTheme(theme_id);
        setContentView(R.layout.activity_select_level);
        getSupportActionBar().hide();
        final Button easy_level = findViewById(R.id.easy_level_button);
        final Button medium_level = findViewById(R.id.medium_level_button);
        final Button hard_level = findViewById(R.id.hard_level_button);

        easy_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoku_data easy_database_for_share;
                SQLiteDatabase easy_db_for_share;
                String all_puzzle = null;
                String half_puzzle = null;
                easy_database_for_share=new sudoku_data(getApplicationContext(),"easy.db",null,1);
                easy_db_for_share=easy_database_for_share.getWritableDatabase();
                Cursor cursor=easy_db_for_share.rawQuery("SELECT * FROM backup_easy ORDER BY id DESC LIMIT 1",null);
                while (cursor.moveToNext()) {
                    int cursor_int=cursor.getInt(0);
                    all_puzzle=cursor.getString(1);
                    half_puzzle=cursor.getString(2);
                }
                if(all_puzzle==null){
                    Intent easy=new Intent(select_level.this , sudoku_play_easy.class);
                    startActivity(easy);
                }
                else{
                    final String all_final=all_puzzle;
                    final String half_final=half_puzzle;
                    AlertDialog.Builder builder = new AlertDialog.Builder(select_level.this);
                    builder.setIcon(R.drawable.about_icon);
                    builder.setTitle("Do you want to continue you puzzle?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            draw_sudoku_grid_easy.set_flag_for_save(all_final,half_final);
                            Intent easy=new Intent(select_level.this , sudoku_play_easy.class);
                            startActivity(easy);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent easy=new Intent(select_level.this , sudoku_play_easy.class);
                            startActivity(easy);
                        }
                    });

                    builder.show();
                }
            }
        });

        medium_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String all_puzzle = null;
                String half_puzzle = null;
                select_level.this.easy_database =new sudoku_data(getApplicationContext(),"easy.db",null,1);
                easy_db = select_level.this.easy_database.getWritableDatabase();
                Cursor cursor= easy_db.rawQuery("SELECT * FROM backup_medium ORDER BY id DESC LIMIT 1",null);
                while (cursor.moveToNext()) {
                    int cursor_int=cursor.getInt(0);
                    all_puzzle=cursor.getString(1);
                    half_puzzle=cursor.getString(2);
                }
                if(all_puzzle==null){
                    Intent medium=new Intent(select_level.this , sudoku_play_medium.class);
                    startActivity(medium);
                }
                else{
                    final String all_final=all_puzzle;
                    final String half_final=half_puzzle;
                    AlertDialog.Builder builder = new AlertDialog.Builder(select_level.this);
                    builder.setIcon(R.drawable.about_icon);
                    builder.setTitle("Do you want to continue you puzzle?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            draw_sudoku_grid_medium.set_flag_for_save(all_final,half_final);
                            Intent medium=new Intent(select_level.this , sudoku_play_medium.class);
                            startActivity(medium);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent medium=new Intent(select_level.this , sudoku_play_medium.class);
                            startActivity(medium);
                        }
                    });

                    builder.show();
                }
            }
        });

        hard_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String all_puzzle = null;
                String half_puzzle = null;

                Cursor cursor=easy_db.rawQuery("SELECT * FROM backup_hard ORDER BY id DESC LIMIT 1",null);
                while (cursor.moveToNext()) {
                    int cursor_int=cursor.getInt(0);
                    all_puzzle=cursor.getString(1);
                    half_puzzle=cursor.getString(2);
                }
                if(all_puzzle==null){
                    Intent medium=new Intent(select_level.this , sudoku_play_hard.class);
                    startActivity(medium);
                }
                else{
                    final String all_final=all_puzzle;
                    final String half_final=half_puzzle;
                    AlertDialog.Builder builder = new AlertDialog.Builder(select_level.this);
                    builder.setIcon(R.drawable.about_icon);
                    builder.setTitle("Do you want to continue you puzzle?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            draw_sudoku_grid_hard.set_flag_for_save(all_final,half_final);
                            Intent hard=new Intent(select_level.this , sudoku_play_hard.class);
                            startActivity(hard);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent hard=new Intent(select_level.this , sudoku_play_hard.class);
                            startActivity(hard);
                        }
                    });

                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                copydata(getApplicationContext());
            }
        },1000);
    }

    private void copydata(final Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        if (!clipboard.hasPrimaryClip()) {
            return;
        }
        //if scan text
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            final String[] value_content = {null};
            ClipData cdText = clipboard.getPrimaryClip();
            ClipData.Item item = cdText.getItemAt(0);
            String str_from_clipboard = null;
            //text info
            if (item.getText() != null && !item.getText().toString().equals("")) {
                str_from_clipboard = item.getText().toString();
            }
            // sth will do
            if(str_from_clipboard!=null&&str_from_clipboard!="") {
                for (int i = 0; i < str_from_clipboard.length(); i++) {
                    if (str_from_clipboard.charAt(i) == '&') {
                        if (i + 82 <= str_from_clipboard.length()) {
                            if (str_from_clipboard.charAt(i + 82) == '&') {
                                value_content[0] = str_from_clipboard.substring(i + 1, i + 82);
                                break;
                            }
                        }
                    }
                }
                System.out.println(value_content[0]);
                if (value_content[0] != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(select_level.this);
                    builder.setIcon(R.drawable.about_icon);
                    builder.setTitle("Do you want to start puzzle you copied?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            draw_sudoku_grid_easy.set_flag_for_share(value_content[0]);
                            value_content[0] = null;
                            ClipboardManager clip_manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", "");
                            clip_manager.setPrimaryClip(clip);
                            Intent to_share_puzzle = new Intent(select_level.this, sudoku_play_easy.class);
                            startActivity(to_share_puzzle);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ClipboardManager clip_manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", "");
                            clip_manager.setPrimaryClip(clip);
                        }
                    });
                    builder.show();

                }
            }

        }
    }
}
