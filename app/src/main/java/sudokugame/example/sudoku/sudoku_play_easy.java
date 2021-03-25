package sudokugame.example.sudoku;

import android.annotation.SuppressLint;
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
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku.R;

import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.graphical_user_interface.draw_sudoku_grid_easy;
import sudokugame.example.sudoku.graphical_user_interface.pause_pop_window;


public class sudoku_play_easy extends AppCompatActivity {

    private static Chronometer temp_timer_for_submit;
    private long timer_for_pause=0;
    long time;

    private sudoku_data easy_database;
    private SQLiteDatabase easy_db;
    public static Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme_id;
        int theme_mode=1;
        sudoku_data easy_database=new sudoku_data(sudoku_play_easy.this,"easy.db",null,1);
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

        //Can not screenshot
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_sudoku_play_easy);
        getSupportActionBar().hide();
        final ImageButton settings_play = findViewById(R.id.setting_play_icon);

        final ImageButton pause_play = findViewById(R.id.pause_play_icon);
        timer=findViewById(R.id.play_timer);
        timer_for_pause=SystemClock.elapsedRealtime();
        timer.setBase(timer_for_pause);
        timer.start();

        settings_play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Cursor cursor_for_music_puzzle=easy_db.rawQuery("SELECT * FROM music LIMIT 1",null);
                int music_puzzle_mode = 0;
                while (cursor_for_music_puzzle.moveToNext()) {
                    music_puzzle_mode = cursor_for_music_puzzle.getInt(0);
                }
                cursor_for_music_puzzle.close();

                @SuppressLint("WrongConstant") Toast toast=Toast.makeText(sudoku_play_easy.this, "", 1000);
                LinearLayout layout = (LinearLayout) toast.getView();
                TextView str = (TextView) layout.getChildAt(0);
                str.setTextSize(30);
                str.setTextColor(R.color.white);
                toast.setGravity(Gravity.CENTER, 0, 0);
                ImageView imageView= new ImageView(sudoku_play_easy.this);
                if(music_puzzle_mode==1){
                    easy_db.execSQL("update music set click_puzzle =0");
                    toast.setText("Music off");
                    imageView.setImageResource(R.drawable.music_off);
                }
                else {
                    easy_db.execSQL("update music set click_puzzle =1");
                    toast.setText("Music on");
                    imageView.setImageResource(R.drawable.music_on);
                }
                LinearLayout toastView = (LinearLayout) toast.getView();
                toastView.setOrientation(LinearLayout.VERTICAL);
                toastView.addView(imageView, 0);
                toast.show();
            }
        });

        pause_play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final pause_pop_window popupWindow = new pause_pop_window(sudoku_play_easy.this);
                popupWindow.displayPop(v);
                popupWindow.setFocusable(true);
                time = SystemClock.elapsedRealtime();

                popupWindow.control_timer(timer,time);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(false);
                popupWindow.setWindowLayoutType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
                popupWindow.setOnPathItemClickListener(new pause_pop_window.OnPathItemClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onItemClick(pause_pop_window.four_lists item) {
                        if(item.list_name.equals("Home")){
                            Intent to_index=new Intent(sudoku_play_easy.this , home_page.class);
                            startActivity(to_index);
                        }
                        else if(item.list_name.equals("Share")){
                            String share_data=get_share_data();

                            //get clipboardmanager
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // create char clipdata
                            ClipData mClipData = ClipData.newPlainText("Label", share_data);
                            // copy clipdata content to system clipdata
                            cm.setPrimaryClip(mClipData);

                            @SuppressLint("WrongConstant")
                            Toast toast_for_share=Toast.makeText(getApplicationContext(), "", 3000);
                            LinearLayout layout = (LinearLayout) toast_for_share.getView();
                            TextView str_for_share = (TextView) layout.getChildAt(0);
                            str_for_share.setTextSize(14);
                            str_for_share.setTextColor(R.color.white);
                            toast_for_share.setText("Please send clipboard contents to friends");
                            toast_for_share.setGravity(Gravity.CENTER, 0, 0);
                            toast_for_share.show();
                        }
                        else if(item.list_name.equals("Help")){
                            Intent to_help=new Intent(sudoku_play_easy.this,teaching_sudoku.class);
                            startActivity(to_help);
                        }
                        else if(item.list_name.equals("Save")){
                            save_data();

                            @SuppressLint("WrongConstant")
                            Toast toast_for_save=Toast.makeText(getApplicationContext(), "", 3000);
                            LinearLayout layout = (LinearLayout) toast_for_save.getView();
                            TextView str_for_save = (TextView) layout.getChildAt(0);
                            str_for_save.setTextSize(20);
                            str_for_save.setTextColor(R.color.white);
                            toast_for_save.setText("Save successfully");
                            toast_for_save.setGravity(Gravity.CENTER, 0, 0);
                            toast_for_save.show();
                        }
                        else {Toast.makeText(sudoku_play_easy.this,"点击了--->"+item.list_name,Toast.LENGTH_LONG).show();}
                    }
                });
            }
        });
    }
    public static void set_timer_stop(){
        temp_timer_for_submit =timer;
        temp_timer_for_submit.stop();
    }

    public String get_share_data(){

        sudoku_data easy_database_for_share;
        SQLiteDatabase easy_db_for_share;
        String all_puzzle = null;
        String half_puzzle = null;
        String hole_puzzle=null;
        easy_database_for_share=new sudoku_data(this,"easy.db",null,1);
        easy_db_for_share=easy_database_for_share.getWritableDatabase();
        Cursor cursor=easy_db_for_share.rawQuery("SELECT * FROM easy_level ORDER BY id DESC LIMIT 1",null);
        while (cursor.moveToNext()) {
            int cursor_int=cursor.getInt(0);
            all_puzzle=cursor.getString(1);
            half_puzzle=cursor.getString(2);
            hole_puzzle=cursor.getString(3);
        }

        return "&"+hole_puzzle+"&";
    }

    public void save_data(){
        String all_puzzle=null;
        String half_puzzle=null;
        Cursor cursor=easy_db.rawQuery("SELECT * FROM easy_level ORDER BY id DESC LIMIT 1",null);
        while (cursor.moveToNext()) {
            int cursor_int=cursor.getInt(0);
            all_puzzle=cursor.getString(1);
            half_puzzle=cursor.getString(2);
        }
        easy_db.execSQL("INSERT INTO backup_easy (all_sudoku, half_sudoku) VALUES (?,?)", new Object[]{all_puzzle, half_puzzle});

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
            if (item.getText() != null && !item.getText().toString().equals("")) {
                str_from_clipboard = item.getText().toString();
            }
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
                            Intent to_share_puzzle = new Intent(sudoku_play_easy.this, sudoku_play_easy.class);
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
