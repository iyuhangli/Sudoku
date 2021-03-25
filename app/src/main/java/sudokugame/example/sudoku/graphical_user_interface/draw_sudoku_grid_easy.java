package sudokugame.example.sudoku.graphical_user_interface;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;

import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudoku.R;
import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.control_sudoku.three_levels;
import sudokugame.example.sudoku.sudoku_play_easy;

import java.lang.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class draw_sudoku_grid_easy extends View {
    private int screen_width=0;
    private int screen_height=0;
    private int each_grid=0;
    private int each_space=0;
    private float digits_x=0;//it is to calculate gap and layout
    private float digits_y=0;
    private float digits_size=0;
    private float mark_digit_size=0;
    private float half_grid=0;
    private int digit_x=-1;//it is to mark touch attributes
    private int digit_y=-1;
    private int[] now_select_puzzle= new int[2];
    private int flag_for_keyboard_puzzle=0;
    private int now_select_keyboard;
    private int t0=-1;//now selected board x
    private int t1=-1;//now selected board y
    private int keypad_number =-1;
    private int selected_number_of_puzzle =-1;//click digit of puzzle grid
    private int temp_keypad_number =-1; //"temp" is to check couble click
    private int temp_board_number=-1;//check board double check
    private String temp_string="";
    private int [][] half_data_puzzle;
    private int [][] temp_data_puzzle;
    private int[][] all_data_puzzle={{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
    private int[][] share_data_puzzle={{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
    private float input_digits_size=0;
    private float each_digits=0;
    private float internal_gap=0;
    private sudoku_data easy_database;
    public SQLiteDatabase easy_db;
    private String all_string;
    private String half_string;
    private String time;
    private int flag_for_set_temp_value=0;
    private int flag_for_first_boot=0;
    private static int flag_for_first_boot_static=0;
    private int flag_for_replay=0;
    private int flag_for_last_step=0;
    private String flag_for_share=null;
    private static String flag_for_share_static=null;
    private static String flag_for_save_all_static=null;
    private static String flag_for_save_half_static=null;
    private int mark_mode=0;
    private int mark_puzzle[][];
    private int mark_x=-1;
    private int mark_y=-1;
    private String hole_sudoku;
    private int check_finished=1;
    private int flag_for_first_board_or_first_keypad=0;

    Paint draw_digits_circle=new Paint();
    Paint draw_digits=new Paint();
    Paint draw_mark_digits=new Paint();
    Paint draw_new_circle=new Paint();
    Paint draw_new_digits=new Paint();
    Paint draw_small_line=new Paint();
    Paint draw_big_line=new Paint();
    Paint draw_digits_of_board=new Paint();//input way digits
    Paint draw_background=new Paint();
    Paint draw_new_digits_of_board=new Paint();
    Paint draw_old_new_background=new Paint();
    Paint draw_new_background=new Paint();
    Paint draw_null_background=new Paint();
    Bitmap b_submit;
    Bitmap b_replay;
    Bitmap b_last_step;
    Bitmap b_mark;
    Bitmap b_mark_selected;
    three_levels sd=new three_levels(1);

    SoundPool sound_pool;
    HashMap<Integer, Integer> hash_map;
    int curr_stream_number;
    int music_puzzle_mode=0;
    int hint_mode=0;

    public draw_sudoku_grid_easy(sudoku_play_easy sudoku_play_easy){
        super(sudoku_play_easy);
        do_before_onDraw();
    }

    public draw_sudoku_grid_easy(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        do_before_onDraw();
    }

    public draw_sudoku_grid_easy(Context context){
        super(context);
    }

    public draw_sudoku_grid_easy(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    public static void set_flag_for_share(String flag_for_share_pass){
        flag_for_share_static=flag_for_share_pass;
    }

    public static void set_flag_for_save(String flag_for_save_all, String flag_for_save_half){
        flag_for_save_all_static=flag_for_save_all;
        flag_for_save_half_static=flag_for_save_half;
    }

    public void do_before_onDraw(){
        mark_puzzle=new int[81][9];
        flag_for_share=flag_for_share_static;

        if(!isInEditMode()) {
            easy_database=new sudoku_data(this.getContext(),"easy.db",null,1);
            easy_db = easy_database.getWritableDatabase();
        }

        //each time play, mark-1
        int mark=0;
        Cursor cursor_score = easy_db.rawQuery("SELECT * FROM mark ORDER BY mark DESC LIMIT 1", null);
        while (cursor_score.moveToNext()) {
            mark = cursor_score.getInt(0);
        }
        cursor_score.close();

        Cursor cursor_hint = easy_db.rawQuery("SELECT * FROM music LIMIT 1", null);
        while (cursor_hint.moveToNext()) {
            cursor_hint.getInt(0);
            cursor_hint.getInt(1);
            hint_mode=cursor_hint.getInt(2);
        }
        cursor_hint.close();

        int mark_after=mark-1;
        ContentValues cv = new ContentValues();
        cv.put("mark", mark_after);
        String[] args = {String.valueOf(mark)};
        easy_db.update("mark", cv, "mark=?",args);

        if(flag_for_share==null) {
            if(flag_for_save_all_static==null) {
                sd = new three_levels(1);
                all_data_puzzle = three_levels.get_all_puzzle();
                half_data_puzzle = three_levels.get_half_puzzle();
            }
            else{
                all_data_puzzle=string2two_dim_share(flag_for_save_all_static);
                half_data_puzzle=string2two_dim_share(flag_for_save_half_static);
                flag_for_save_all_static=null;
                flag_for_save_half_static=null;
            }
        }
        else{
            half_data_puzzle=string2two_dim_share(flag_for_share);
            copy_2dim_array(share_data_puzzle,half_data_puzzle);
            back_trace(0,0);
            flag_for_share=null;
            flag_for_share_static=null;
            share_data_puzzle= new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}};
        }

        all_string=two_dim2string(all_data_puzzle);
        half_string=two_dim2string(half_data_puzzle);
        time="0";

        int is_table_exist=1;
        if(!isInEditMode()) {
            Cursor c = easy_db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='easy_level'", null);
            while (c.moveToNext()) {
                is_table_exist = c.getInt(0);
            }
        }
        //if not first boot;
        if(is_table_exist==1) {
            String drop_level = "DROP TABLE easy_level;";
            String drop_time = "DROP TABLE easy_time;";
            String delete_seq = "DELETE FROM sqlite_sequence WHERE name=\"easy_level\";";
            String time_str_easy = "create table easy_time(id INTEGER PRIMARY KEY AUTOINCREMENT, time varchar(100), time_already_use varchar(100))";
            String sql_str_easy = "create table easy_level(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100),hole_sudoku varchar(100))";

            //add if(!isInEditMode()) to ensure preview in xml
            if (!isInEditMode()) {
                easy_db.execSQL(drop_level);
                easy_db.execSQL(drop_time);
                easy_db.execSQL(delete_seq);
                easy_db.execSQL(time_str_easy);
                easy_db.execSQL(sql_str_easy);
            }
        }

            hole_sudoku=new String(half_string);
        //add if(!isInEditMode()) to ensure preview in xml
        if(!isInEditMode()) {
            easy_db.execSQL("INSERT INTO easy_level (all_sudoku, half_sudoku,hole_sudoku) VALUES (?,?,?)", new Object[]{all_string, half_string,half_string});
            //easy_db.insert("easy_level",null,values);
        }

        temp_data_puzzle=new int[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                temp_data_puzzle[i][j]= half_data_puzzle[i][j];
            }
        }
        //not temp_data_puzzle=half_data_puzzle, because reference address, must copy value
        screen_height=getResources().getDisplayMetrics().heightPixels;
        screen_width=screen_height/2;//very important!!!!
        each_grid=screen_width/9;
        each_space=each_grid/6;
        digits_size=each_grid*3/4;
        mark_digit_size=digits_size/3;
        half_grid=each_grid/2;
        digits_x=each_grid/2;
        digits_y=each_grid*3/4;
        each_digits=screen_width/7;
        internal_gap=each_digits/4;
        input_digits_size=each_digits*3/4;

        now_select_puzzle[0]=-1;
        now_select_puzzle[1]=-1;
        now_select_keyboard=-1;

        draw_digits.setTextAlign(Paint.Align.CENTER);
        draw_digits.setTextSize(digits_size);
        draw_digits.setColor(getResources().getColor(R.color.black));

        draw_mark_digits.setTextAlign(Paint.Align.CENTER);
        draw_mark_digits.setTextSize(mark_digit_size);
        draw_mark_digits.setColor(getResources().getColor(R.color.red));

        draw_new_digits.setTextAlign(Paint.Align.CENTER);
        draw_new_digits.setTextSize(digits_size);
        draw_new_digits.setColor(getResources().getColor(R.color.white));
        draw_new_digits.setStrokeWidth(4);

        draw_digits_of_board.setTextAlign(Paint.Align.CENTER);
        draw_digits_of_board.setTextSize(input_digits_size);
        draw_digits_of_board.setColor(getResources().getColor(R.color.black));
        draw_digits_of_board.setStrokeWidth(4);

        draw_new_digits_of_board.setTextAlign(Paint.Align.CENTER);
        draw_new_digits_of_board.setTextSize(input_digits_size);
        draw_new_digits_of_board.setColor(getResources().getColor(R.color.white));
        draw_new_digits_of_board.setStrokeWidth(4);

        draw_digits_circle.setStyle(Paint.Style.STROKE);
        draw_digits_circle.setStrokeWidth(6);
        draw_digits_circle.setColor(getResources().getColor(R.color.gray));

        draw_new_circle.setStyle(Paint.Style.FILL_AND_STROKE);
        draw_new_circle.setStrokeWidth(6);
        draw_new_circle.setColor(getResources().getColor(R.color.darkviolet));

        draw_small_line.setColor(getResources().getColor(R.color.gray));
        draw_small_line.setStyle(Paint.Style.FILL);

        draw_big_line.setColor((getResources().getColor(R.color.darkviolet)));
        draw_big_line.setStyle(Paint.Style.FILL_AND_STROKE);
        draw_big_line.setStrokeWidth(3);


        draw_background.setColor(getResources().getColor(R.color.gentlegray));
        draw_background.setTextAlign(Paint.Align.CENTER);

        draw_old_new_background.setColor(getResources().getColor(R.color.gentleoldnewdarkviolet));
        draw_old_new_background.setTextAlign(Paint.Align.CENTER);

        draw_new_background.setColor(getResources().getColor(R.color.gentledarkviolet));
        draw_new_background.setTextAlign(Paint.Align.CENTER);

        draw_null_background.setColor(getResources().getColor(R.color.gentlenulldarkviolet));

        draw_null_background.setTextAlign(Paint.Align.CENTER);

        b_submit=BitmapFactory.decodeResource(getResources(), R.drawable.submit_icon);
        b_replay=BitmapFactory.decodeResource(getResources(), R.drawable.replay_icon);
        b_last_step=BitmapFactory.decodeResource(getResources(), R.drawable.laststep_icon);
        b_mark=BitmapFactory.decodeResource(getResources(), R.drawable.mark_icon);
        b_mark_selected=BitmapFactory.decodeResource(getResources(), R.drawable.mark_select_icon);

        if(!isInEditMode()) {
            String drop_backup_easy = "DROP TABLE backup_easy;";
            String delete_seq_backup_easy = "DELETE FROM sqlite_sequence WHERE name=\"backup_easy\";";
            String create_backup_easy = "create table backup_easy(id INTEGER PRIMARY KEY AUTOINCREMENT, all_sudoku varchar(100), half_sudoku varchar(100))";
            easy_db.execSQL(drop_backup_easy);
            easy_db.execSQL(delete_seq_backup_easy);
            easy_db.execSQL(create_backup_easy);
            sound_pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
            hash_map = new HashMap<Integer, Integer>();
            hash_map.put(1, sound_pool.load(getContext(), R.raw.click, 1));
        }


    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas){
        draw_puzzle_background(canvas);
        draw_ini_digits(canvas);
        draw_digits_and_circles(canvas);
        draw_operations(canvas);
        draw_mark(canvas);
    }

    //draw grid line
    public void draw_puzzle_background(Canvas canvas){
        for(int temp=0;temp<9;temp++){
            for(int temp2=1;temp2<9;temp2++){
                canvas.drawLine(each_space+each_grid*temp,each_grid*temp2,each_grid*(temp+1)-each_space,each_grid*temp2,draw_small_line);
                canvas.drawLine(each_grid*temp2,each_grid*temp+each_space,each_grid*temp2,each_grid*(temp+1)-each_space,draw_small_line);
            }
        }
        for(int temp3=1;temp3<3;temp3++){
            canvas.drawLine(0,each_grid*temp3*3,screen_width,each_grid*temp3*3,draw_big_line);
            canvas.drawLine(each_grid*temp3*3,0,each_grid*temp3*3,0+screen_width,draw_big_line);
        }
    }

    //draw puzzle digits
    public void draw_ini_digits(Canvas canvas){
        for(int temp4=0;temp4<9;temp4++){
            for (int temp5=0;temp5<9;temp5++){
                temp_string=""+temp_data_puzzle[temp4][temp5];
                if(!temp_string.equals("0")){
                    if((temp_string.equals(""+ selected_number_of_puzzle)&&keypad_number==-1)||temp_string.equals(""+keypad_number)){
                        if(temp5==t0&&temp4==t1){
                            canvas.drawCircle(temp5 * each_grid + half_grid, temp4 * each_grid + half_grid, half_grid, draw_null_background);
                        }
                        else {
                            if(half_data_puzzle[temp4][temp5]!=0){
                                canvas.drawCircle(temp5 * each_grid + half_grid, temp4 * each_grid + half_grid, half_grid, draw_old_new_background);
                            }
                            else {
                                canvas.drawCircle(temp5 * each_grid + half_grid, temp4 * each_grid + half_grid, half_grid, draw_new_background);
                            }
                        }
                        canvas.drawText(temp_string,temp5*each_grid+digits_x,temp4*each_grid+digits_y,draw_new_digits);
                    }
                    else{
                        if(half_data_puzzle[temp4][temp5]!=0) {
                            canvas.drawCircle(temp5 * each_grid + half_grid, temp4 * each_grid + half_grid, half_grid, draw_background);
                        }
                        canvas.drawText(temp_string,temp5*each_grid+digits_x,temp4*each_grid+digits_y,draw_digits);
                    }
                }
                else{
                    if((digit_x==temp5&&digit_y==temp4&&t0!=-1)&&keypad_number!=0){
                        canvas.drawCircle(temp5*each_grid+half_grid,temp4*each_grid+half_grid,half_grid,draw_null_background);
                       // digit_x=-1;
                        //digit_y=-1;
                    }
                }
            }
        }
    }

    //draw keyboard
    private void draw_digits_and_circles(Canvas canvas) {
        if(flag_for_first_board_or_first_keypad==1){
            keypad_number=-1;
            temp_board_number=-1;
            selected_number_of_puzzle=-1;
            t0=-1;
            t1=-1;
            flag_for_first_board_or_first_keypad=0;
        }
        for(int temp=0;temp<5;temp++){
            for(int temp2=0;temp2<2;temp2++){
                if(temp==4&temp2==1) {
                    if (keypad_number!=0) {
                        canvas.drawCircle(each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits+(each_digits + internal_gap) * temp2, each_digits / 2, draw_digits_circle);
                        canvas.drawText("D", each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits * 5 / 4 + (each_digits + internal_gap) * temp2, draw_digits_of_board);
                    }
                    else{
                        canvas.drawCircle(each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits + (each_digits + internal_gap) * temp2, each_digits / 2, draw_new_circle);
                        canvas.drawText("D", each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits * 5 / 4 + (each_digits + internal_gap) * temp2, draw_new_digits_of_board);
                    }
                }
                else {
                    if(keypad_number!=(temp+1)+(temp2*5)){
                        canvas.drawCircle(each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits + (each_digits + internal_gap) * temp2, each_digits / 2, draw_digits_circle);
                        canvas.drawText("" + ((temp + 1) + 5 * temp2), each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits * 5 / 4 + (each_digits + internal_gap) * temp2, draw_digits_of_board);
                    }
                    else{
                        canvas.drawCircle(each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits + (each_digits + internal_gap) * temp2, each_digits / 2, draw_new_circle);
                        canvas.drawText("" + ((temp + 1) + 5 * temp2), each_digits + (internal_gap + each_digits) * temp, screen_width+each_digits * 5 / 4 + (each_digits + internal_gap) * temp2, draw_new_digits_of_board);
                    }
                }
            }
        }
    }

    public void draw_operations(Canvas canvas){
        Rect src_submit = new Rect(0,0,b_submit.getWidth(),b_submit.getHeight());
        Rect dst_submit = new Rect(screen_width/16, (int) (screen_width+each_digits+(each_digits + internal_gap)*2),+screen_width*3/16,(int)(screen_width*18/16+each_digits+(each_digits + internal_gap)*2));
        Rect src_replay = new Rect(0,0,b_submit.getWidth(),b_submit.getHeight());
        Rect dst_replay = new Rect(screen_width*5/16, (int) (screen_width+each_digits+(each_digits + internal_gap)*2),screen_width*7/16,(int)(screen_width*18/16+each_digits+(each_digits + internal_gap)*2));
        Rect src_last_step = new Rect(0,0,b_submit.getWidth(),b_submit.getHeight());
        Rect dst_last_step = new Rect(screen_width*9/16, (int) (screen_width+each_digits+(each_digits + internal_gap)*2),screen_width*11/16,(int)(screen_width*18/16+each_digits+(each_digits + internal_gap)*2));
        Rect src_mark = new Rect(0,0,b_submit.getWidth(),b_submit.getHeight());
        Rect dst_mark = new Rect(screen_width*13/16, (int) (screen_width+each_digits+(each_digits + internal_gap)*2),screen_width*15/16,(int)(screen_width*18/16+each_digits+(each_digits + internal_gap)*2));
        canvas.drawBitmap(b_submit, src_submit,dst_submit,null);
        canvas.drawBitmap(b_replay, src_replay,dst_replay,null);
        canvas.drawBitmap(b_last_step, src_last_step,dst_last_step,null);
        if(mark_mode==0){
            canvas.drawBitmap(b_mark, src_mark,dst_mark,null);
        }
        else {
            canvas.drawBitmap(b_mark_selected, src_mark, dst_mark, null);
        }
    }

    public void draw_mark(Canvas canvas){
        if(mark_mode==1&&mark_x!=-1&&mark_y!=-1&&keypad_number!=-1){
            int number_of_mark=mark_x+9*mark_y;
            int mark_number=keypad_number-1;
            if(keypad_number!=0) {
                if (half_data_puzzle[mark_y][mark_x] == 0) {
                    mark_puzzle[number_of_mark][mark_number] = 1;
                }
            }
            else{
                for(int i=0;i<9;i++){
                    mark_puzzle[number_of_mark][i]=0;
                }
            }
        }
        int no_of_grid=-1;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                no_of_grid=i*9+j;
                for(int k=0;k<9;k++){
                    if(mark_puzzle[no_of_grid][k]==1){
                        if(temp_data_puzzle[i][j]==0){
                            canvas.drawText("" + (k+1),j * each_grid + (each_grid / 3) * (k%3) + each_grid/6, (i * each_grid )+ (each_grid / 3) * (k/3) + (digits_y / 3), draw_mark_digits);
                        }
                    }
                }
            }
        }
    }

    /*
    Use 'if and else' to check the digits
    To do this, reference activity_sudoku_play_easy.xmll.xml and find each condition
    2.4 is because h,2.4:1 in activity_sudoku_play_easy.
     */
    int board_event_y=0;
    int board_event_x=0;
    int first_event_x=0;
    int first_event_y=0;
    int board_event_xx=0;
    int board_event_yy=0;
    int move_length_max=0;
    long first_time=0;
    long last_time=0;
    @SuppressLint({"ResourceAsColor", "WrongConstant", "ClickableViewAccessibility"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        move_length_max=getWidth()/12;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                board_event_x = (int) event.getX();
                board_event_y = (int) event.getY();
                first_time= System.currentTimeMillis();
                first_event_x=board_event_x;
                first_event_y=board_event_y;
                System.out.println(board_event_x+"++++++key_down"+board_event_y+"   "+first_time);
                break;
            case MotionEvent.ACTION_UP:
                board_event_xx = (int) event.getX();
                board_event_yy = (int) event.getY();
                last_time=System.currentTimeMillis();
                if(Math.abs(first_event_x-board_event_xx)<move_length_max&&Math.abs(first_event_y-board_event_yy)<move_length_max){
                    if(last_time-first_time<1500||hint_mode==0) {
                        do_sth(board_event_xx, board_event_yy,0);
                    }
                    else{
                        @SuppressLint("WrongConstant")
                        Toast toast=Toast.makeText(getContext(), "", 800);
                        LinearLayout layout = (LinearLayout) toast.getView();
                        TextView str = (TextView) layout.getChildAt(0);
                        str.setTextSize(30);
                        str.setTextColor(R.color.white);
                        toast.setText("Hint added!");
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        ImageView imageView= new ImageView(getContext());
                        imageView.setImageResource(R.drawable.about_icon);
                        LinearLayout toastView = (LinearLayout) toast.getView();
                        toastView.setOrientation(LinearLayout.VERTICAL);
                        toastView.addView(imageView, 0);
                        toast.show();
                        do_sth(board_event_xx, board_event_yy,1);
                    }
                }
                System.out.println(board_event_xx+"++++++key_up"+board_event_yy);
                break;
            case MotionEvent.ACTION_CANCEL:
                System.out.println("++++++key_c");
                break;
        }

        return true;
    }

    @SuppressLint("ResourceAsColor")
    private void do_sth(int board_event_x, int board_event_y, int flag_for_long_time_press) {

        Cursor cursor_for_music_puzzle=easy_db.rawQuery("SELECT * FROM music LIMIT 1",null);
        int music_puzzle_mode = 0;
        while (cursor_for_music_puzzle.moveToNext()) {
            music_puzzle_mode=cursor_for_music_puzzle.getInt(0);
        }
        cursor_for_music_puzzle.close();
        if(music_puzzle_mode==1) {
            play_click_sound(1, 0);
        }
        temp_keypad_number =-1;
        //to check grid

        if(mark_mode==0) {
            digit_x=-1;
            digit_y=-1;
            if (a_c_b(board_event_x, 0) && a_c_b(board_event_y, 0) && a_c_b(9 * each_grid, board_event_x) && a_c_b(9 * each_grid, board_event_y)) {
                digit_x = board_event_x / each_grid;
                digit_y = board_event_y / each_grid;

                //flag is 0 means short time press, and 1 means long time press
                if(flag_for_long_time_press==0){
                    temp_board_number = temp_data_puzzle[digit_y][digit_x];
                    //double click
                    if (digit_x == t0 && digit_y == t1 && keypad_number != temp_board_number) {
                        selected_number_of_puzzle = -1;
                        t0 = -1;
                        t1 = -1;
                    }
                    else if (digit_x == t0 && digit_y == t1 && keypad_number == temp_keypad_number) {
                        temp_data_puzzle[digit_y][digit_x] = 0;
                        flag_for_keyboard_puzzle = 1;
                    }
                    else {
                        selected_number_of_puzzle = temp_board_number;
                        t0 = (int) digit_x;
                        t1 = (int) digit_y;
                    }
                    if (half_data_puzzle[digit_y][digit_x] != 0) {
                        keypad_number = -1;
                    }
                    if (keypad_number == temp_board_number) {
                        temp_data_puzzle[digit_y][digit_x] = 0;
                        flag_for_keyboard_puzzle = 1;
                        digit_x = -1;
                        digit_y = -1;
                        selected_number_of_puzzle = -1;
                        t0 = -1;
                        t1 = -1;
                    }
                }
                else{
                    temp_board_number=all_data_puzzle[digit_y][digit_x];
                    temp_data_puzzle[digit_y][digit_x]=temp_board_number;
                    selected_number_of_puzzle = temp_board_number;
                    t0 = (int) digit_x;
                    t1 = (int) digit_y;
                }


            }
        }
        else{
            if (a_c_b(board_event_x, 0) && a_c_b(board_event_y, 0) && a_c_b(9 * each_grid, board_event_x) && a_c_b(9 * each_grid, board_event_y)) {
                mark_x=board_event_x/each_grid;
                mark_y=board_event_y/each_grid;
            }

        }
        //to check board
        if(a_c_b(board_event_y,screen_width+0.5*each_digits)&&a_c_b(screen_width+2.5*each_digits,board_event_y)){
            if (a_c_b(1.5 * each_digits, board_event_x) && a_c_b(board_event_x, 0.5 * each_digits)) {
                if (a_c_b(board_event_y, 0.5 * each_digits + screen_width) && a_c_b(1.5 * each_digits + screen_width, board_event_y)) {
                    temp_keypad_number = 1;
                } else if (a_c_b(board_event_y, 1.5 * each_digits + internal_gap + screen_width) && a_c_b(2.5 * each_digits + internal_gap + screen_width, board_event_y)) {
                    temp_keypad_number = 6;
                }
            }
            else if (a_c_b(2.5 * each_digits + internal_gap, board_event_x) && a_c_b(board_event_x, 1.5 * each_digits + internal_gap)) {
                if (a_c_b(board_event_y, 0.5 * each_digits + screen_width) && a_c_b(1.5 * each_digits + screen_width, board_event_y)) {
                    temp_keypad_number = 2;
                } else if (a_c_b(board_event_y, 1.5 * each_digits + internal_gap + screen_width) && a_c_b(2.5 * each_digits + internal_gap + screen_width, board_event_y)) {
                    temp_keypad_number = 7;
                }
            }
            else if (a_c_b(3.5 * each_digits + 2 * internal_gap, board_event_x) && a_c_b(board_event_x, 2.5 * each_digits + 2 * internal_gap)) {
                if (a_c_b(board_event_y, 0.5 * each_digits + screen_width) && a_c_b(1.5 * each_digits + screen_width, board_event_y)) {
                    temp_keypad_number = 3;
                } else if (a_c_b(board_event_y, 1.5 * each_digits + internal_gap + screen_width) && a_c_b(2.5 * each_digits + internal_gap + screen_width, board_event_y)) {
                    temp_keypad_number = 8;
                }
            }
            else if (a_c_b(4.5 * each_digits + 3 * internal_gap, board_event_x) && a_c_b(board_event_x, 3.5 * each_digits + 3 * internal_gap)) {
                if (a_c_b(board_event_y, 0.5 * each_digits + screen_width) && a_c_b(1.5 * each_digits + screen_width, board_event_y)) {
                    temp_keypad_number = 4;
                } else if (a_c_b(board_event_y, 1.5 * each_digits + internal_gap + screen_width) && a_c_b(2.5 * each_digits + internal_gap + screen_width, board_event_y)) {
                    temp_keypad_number = 9;
                }
            }
            else if (a_c_b(5.5 * each_digits + 4 * internal_gap, board_event_x) && a_c_b(board_event_x, 4.5 * each_digits + 4 * internal_gap)) {
                if (a_c_b(board_event_y, 0.5 * each_digits + screen_width) && a_c_b(1.5 * each_digits + screen_width, board_event_y)) {
                    temp_keypad_number = 5;
                } else if (a_c_b(board_event_y, 1.5 * each_digits + internal_gap + screen_width) && a_c_b(2.5 * each_digits + internal_gap + screen_width, board_event_y)) {
                    temp_keypad_number = 0;
                }
            }
            //to check double click
            if (temp_keypad_number == keypad_number) {
                keypad_number = -1;
                selected_number_of_puzzle =-1;
                t0=-1;
                t1=-1;
            }

            else {
                keypad_number = temp_keypad_number;
            }
            if(selected_number_of_puzzle!=-1){
                flag_for_first_board_or_first_keypad=1;
            }
        }
        else{
            if(a_c_b(board_event_y,(int) (screen_width+each_digits+(each_digits + internal_gap)*2))&&a_c_b((int)(screen_width*18/16+each_digits+(each_digits + internal_gap)*2),board_event_y)){
                //submit
                if(a_c_b(board_event_x,screen_width/16)&&a_c_b(screen_width*3/16,board_event_x)){
                    String g1="";
                    String g2="";
                    String g3="";
                    String g4="";
                    String g5="";
                    String g6="";
                    String g7="";
                    String g8="";
                    String g9="";
                    check_finished=1;
                    for(int i=0;i<9;i++){
                        for(int j=0;j<9;j++){
                            if(!(temp_data_puzzle[i][j] ==all_data_puzzle[i][j])){
                                check_finished=0;
                                if(i<3){
                                    if(j<3) {
                                        g1="1st ";
                                    }
                                    else if(j<6){
                                        g2="2nd ";
                                    }
                                    else if(j<9){
                                        g3="3rd ";
                                    }
                                }
                                else if(i<6){
                                    if(j<3) {
                                        g4="4th ";
                                    }
                                    else if(j<6){
                                        g5="5th ";
                                    }
                                    else if(j<9){
                                        g6="6th ";
                                    }
                                }
                                else{
                                    if(j<3) {
                                        g7="7th ";
                                    }
                                    else if(j<6){
                                        g8="8th ";
                                    }
                                    else if(j<9){
                                        g9="9th ";
                                    }
                                }
                            }
                        }
                    }

                    //0 is not finished and 1 is finished
                    if(check_finished==0){
                        @SuppressLint("WrongConstant")
                        Toast toast=Toast.makeText(getContext(), "", 6000);
                        LinearLayout layout = (LinearLayout) toast.getView();
                        TextView str = (TextView) layout.getChildAt(0);
                        str.setTextSize(30);
                        str.setTextColor(R.color.white);

                        toast.setText("Unsolved puzzle"+"\n"+"Error:"+"\n"+"at the "+g1+g2+g3+g4+g5+g6+g7+g8+g9+"block");
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        ImageView imageView= new ImageView(getContext());
                        imageView.setImageResource(R.drawable.about_icon);
                        LinearLayout toastView = (LinearLayout) toast.getView();
                        toastView.setOrientation(LinearLayout.VERTICAL);
                        toastView.addView(imageView, 0);
                        toast.show();
                    }
                    else{
                        String all = null;
                        String half = null;
                        int mark=0;
                        Cursor cursor=easy_db.rawQuery("SELECT * FROM easy_level ORDER BY id LIMIT 1",null);
                        while (cursor.moveToNext()) {
                            cursor.getInt(0);
                            all=cursor.getString(1);
                            half=cursor.getString(2);
                        }
                        cursor.close();
                        Cursor cursor_mark=easy_db.rawQuery("SELECT * FROM mark LIMIT 1",null);
                        while (cursor.moveToNext()) {
                            mark=cursor_mark.getInt(0);
                        }
                        cursor_mark.close();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                        Date date = new Date(System.currentTimeMillis());
                        String now_date=simpleDateFormat.format(date);
                        easy_db.execSQL("INSERT INTO record (date, level, all_sudoku, half_sudoku) VALUES (?,?,?,?)", new Object[]{now_date,3,all, half});
                        int sum_mark=mark+11;
                        easy_db.execSQL("INSERT INTO mark (mark) VALUES (?)", new Object[]{sum_mark});

                        PopupWindow true_submit = new PopupWindow(getContext());
                        true_submit.setWidth(getWidth());
                        true_submit.setHeight(getHeight());
                        submit_pop_window_true_easy submit_pop_window_true_easy =new submit_pop_window_true_easy(getContext());
                        true_submit.setContentView(submit_pop_window_true_easy);
                        true_submit.setOutsideTouchable(false);
                        true_submit.setFocusable(true);
                        true_submit.setTouchable(true);
                        //set no background
                        true_submit.setBackgroundDrawable(new ColorDrawable());
                        View set_alpha=findViewById(R.id.draw_puzzle_grid);
                        set_alpha.setAlpha((float) 0.05);
                        true_submit.showAtLocation(findViewById(R.id.draw_puzzle_grid), Gravity.NO_GRAVITY, 0, (int) (0.175*getHeight()));
                        sudoku_play_easy.set_timer_stop();
                    }
                }
                //replay
                else if(a_c_b(board_event_x,screen_width*5/16)&&a_c_b(screen_width*7/16,board_event_x)){
                    String delete_half="DELETE FROM easy_level WHERE id>1;";
                    String delete_half_seq="UPDATE sqlite_sequence SET seq=1 WHERE name=\"easy_level\";";
                    easy_db.execSQL(delete_half);
                    easy_db.execSQL(delete_half_seq);
                    String all_from_db = null;
                    String half_from_db = null;
                    Cursor cursor=easy_db.rawQuery("SELECT * FROM easy_level ORDER BY id DESC LIMIT 1",null);
                    while (cursor.moveToNext()) {
                        int cursor_int=cursor.getInt(0);
                        all_from_db=cursor.getString(1);
                        half_from_db=cursor.getString(2);
                    }
                    cursor.close();
                    string2two_dim_and_set_value(half_from_db);
                    flag_for_replay=1;

                }
                //last step
                else if(a_c_b(board_event_x,screen_width*9/16)&&a_c_b(screen_width*11/16,board_event_x)){
                    int cursor_int = -1;
                    Cursor cursor=easy_db.rawQuery("SELECT * FROM easy_level ORDER BY id DESC LIMIT 1",null);
                    while (cursor.moveToNext()) {
                        cursor_int=cursor.getInt(0);
                        String all_from_db=cursor.getString(1);
                        String half_from_db=cursor.getString(2);
                    }
                    cursor.close();
                    if(cursor_int!=1){
                        easy_db.execSQL("DELETE FROM easy_level WHERE id=?", new Object[]{cursor_int});
                        Cursor cursor2=easy_db.rawQuery("SELECT * FROM easy_level ORDER BY id DESC LIMIT 1",null);
                        String half_from_db = null;
                        while (cursor2.moveToNext()) {
                            cursor_int=cursor2.getInt(0);
                            String all_from_db=cursor2.getString(1);
                            half_from_db=cursor2.getString(2);
                        }
                        string2two_dim_and_set_value(half_from_db);
                        cursor2.close();

                    }
                    flag_for_last_step=1;
                }
                else if(a_c_b(board_event_x,screen_width*13/16)&&a_c_b(screen_width*15/16,board_event_x)){
                    //mark
                    if(mark_mode==0) {
                        mark_mode = 1;
                    }
                    else{
                        mark_mode=0;
                    }
                }
            }
        }

        if(t0!=-1&&t1!=-1&&flag_for_replay==0&&flag_for_last_step==0){
            if(keypad_number!=-1&& half_data_puzzle[t1][t0]==0&&flag_for_keyboard_puzzle==0){
                temp_data_puzzle[t1][t0]=keypad_number;
                selected_number_of_puzzle=keypad_number;//If select 1 and input to, then select 6, if no this, 6 and 1 will be highlight.

                String temp_str=two_dim2string(temp_data_puzzle);
                ContentValues contentValues=new ContentValues();
                contentValues.put("all_sudoku",all_string);
                contentValues.put("half_sudoku",temp_str);
                contentValues.put("hole_sudoku",hole_sudoku);
                easy_db.insert("easy_level",null,contentValues);
            }
        }
        if(keypad_number==0){
            t0=-1;
            t1=-1;
        }

        flag_for_replay=0;
        flag_for_last_step=0;
        flag_for_keyboard_puzzle=0;

        invalidate();
    }


    //how to compare two double? if return 1, bigger, 0 equal, -1 smaller
    public boolean a_c_b (double a, double b){
        BigDecimal aa = new BigDecimal(a);
        BigDecimal bb = new BigDecimal(b);
        if(aa.compareTo(bb)==1){
            return true;
        }
        else return false;
    }

    public String two_dim2string(int [][] array){
        StringBuilder temp_str= new StringBuilder();
        for(int temp=0;temp<9;temp++){
            for(int temp1=0;temp1<9;temp1++){
                temp_str.append(array[temp][temp1]);
            }
        }
        return temp_str.toString();
    }

    public void string2two_dim_and_set_value(String str){
        int index=0;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                temp_data_puzzle[i][j]=str.charAt(index)-'0';
                index++;
            }
        }
    }

    public int [][] string2two_dim_share(String str){
        int index=0;
        int[][]a = {{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                a[i][j]=str.charAt(index)-'0';
                index++;
            }
        }
        return a;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=screen_height/2;
        int height=screen_height/2;

        setMeasuredDimension(width, height);
    }

    private void copy_2dim_array(int[][]a, int[][]b) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                a[i][j] = b[i][j];
            }
        }
    }

    private void back_trace(int i, int j) {
        if (i == 8 && j == 9) {
            set_all_from_share();
            return;
        }
        if (j == 9) {
            i++;
            j = 0;
        }
        if (share_data_puzzle[i][j] == 0) {
            for (int k = 1; k <= 9; k++) {
                //check
                if (check(i, j, k)) {
                    share_data_puzzle[i][j] = k;
                    back_trace(i, j + 1);
                    share_data_puzzle[i][j] = 0;
                }
            }
        } else {
            //if this grid has value, next
            back_trace(i, j + 1);
        }
    }

    //Check is ok?
    private boolean check(int row, int line, int number) {
        //check numbers repeat;
        for (int i = 0; i < 9; i++) {
            if (share_data_puzzle[row][i] == number || share_data_puzzle[i][line] == number) {
                return false;
            }
        }
        //check is repeat
        int tempRow = row / 3;
        int tempLine = line / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (share_data_puzzle[tempRow * 3 + i][tempLine * 3 + j] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    public void set_all_from_share() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                all_data_puzzle[i][j]=share_data_puzzle[i][j];
            }
        }
    }

    private void play_click_sound(int sound, int loop) {
        AudioManager audio_manager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        float now_volume = audio_manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float max_volume = audio_manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = now_volume / max_volume;
        curr_stream_number = sound_pool.play(hash_map.get(sound), volume, volume, 1, loop, 1.0f);
        System.out.println(curr_stream_number);
    }



}


