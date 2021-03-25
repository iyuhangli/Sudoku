package sudokugame.example.sudoku;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sudoku.R;

import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.graphical_user_interface.*;

public class home_page extends AppCompatActivity {
    private sudoku_data easy_database;
    private SQLiteDatabase easy_db;
    private int theme_mode=1;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme_id;
        sudoku_data easy_database=new sudoku_data(home_page.this,"easy.db",null,1);
        easy_db= easy_database.getWritableDatabase();
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

        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();
        final Button new_game = findViewById(R.id.new_game_button);
        final ImageButton settings_index = findViewById(R.id.setting_icon);
        final ImageButton teaching_index = findViewById(R.id.teaching_icon);
        final ImageButton about_index= findViewById(R.id.about_icon);
        final ImageButton record_index=findViewById(R.id.record_icon);

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_select_level = new Intent(home_page.this, select_level.class);
                startActivity(to_select_level);
            }
        });

        settings_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_setting_play = new Intent(home_page.this, settings_sudoku.class);
                startActivity(to_setting_play);
            }
        });

        record_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_record = new Intent(home_page.this, record_of_sudoku.class);
                startActivity(to_record);
            }
        });

        teaching_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_teaching = new Intent(home_page.this, teaching_sudoku.class);
                startActivity(to_teaching);
            }
        });

        about_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(home_page.this);
                builder.setIcon(R.drawable.about_icon);
                builder.setTitle("About");
                builder.setMessage("Author: Yuhang Li\n\nVersion: v1.1.2\n\nhttps://github.com/iyuhangli");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                builder.show();
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

            // do operations
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                            Intent to_share_puzzle = new Intent(home_page.this, sudoku_play_easy.class);
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

