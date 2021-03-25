package sudokugame.example.sudoku.graphical_user_interface;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import android.view.*;

import com.example.sudoku.R;
import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.settings_sudoku;
import sudokugame.example.sudoku.sudoku_play_easy;

import java.math.BigDecimal;

public class settings_sudoku_lists extends View {

    private int screen_width=0;
    private int screen_height=0;
    private int each_list=0;

    Paint draw_list_line=new Paint();
    Paint draw_each_sets=new Paint();
    Paint draw_each_sets_selected=new Paint();

    Bitmap b_selected;
    Bitmap b_not_selected;
    private sudoku_data easy_database;
    private SQLiteDatabase easy_db;
    Paint.FontMetrics fontMetrics;

    public settings_sudoku_lists (sudoku_play_easy sudoku_play_easy){
        super(sudoku_play_easy);
        do_before_onDraw();
    }

    public settings_sudoku_lists (Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        do_before_onDraw();
    }

    public void do_before_onDraw(){
        screen_height=getResources().getDisplayMetrics().heightPixels;
        screen_width=getResources().getDisplayMetrics().widthPixels;
        each_list=screen_width/6;

        draw_list_line.setColor(getResources().getColor(R.color.gray));
        draw_list_line.setStyle(Paint.Style.FILL);

        draw_each_sets.setStyle(Paint.Style.FILL);
        draw_each_sets.setStrokeWidth(3);
        draw_each_sets.setTextSize(each_list/3);
        draw_each_sets.setColor(getResources().getColor(R.color.black));

        draw_each_sets_selected.setStyle(Paint.Style.FILL);
        draw_each_sets_selected.setFakeBoldText(true);
        draw_each_sets_selected.setStrokeWidth(5);
        draw_each_sets_selected.setTextSize(each_list/3);
        draw_each_sets_selected.setColor(getResources().getColor(R.color.black));

        b_selected = BitmapFactory.decodeResource(getResources(), R.drawable.selected_icon);
        b_not_selected = BitmapFactory.decodeResource(getResources(), R.drawable.not_selected_icon);

        fontMetrics = draw_each_sets.getFontMetrics();


        if(!isInEditMode()) {
            easy_database = new sudoku_data(getContext(), "easy.db", null, 1);
            easy_db = getContext().openOrCreateDatabase("easy.db", Context.MODE_PRIVATE, null);
        }

    }

    public void onDraw(Canvas canvas){
        draw_list_line(canvas);
        draw_select_district(canvas);

    }

    //draw grid line
    public void draw_list_line(Canvas canvas) {
        canvas.drawText("Click music",(float)0.06*screen_width,each_list*(1),draw_each_sets);
        canvas.drawText("Theme",(float)0.06*screen_width,each_list*(2),draw_each_sets);
        canvas.drawText("Hint",(float)0.06*screen_width,each_list*(3),draw_each_sets);
    }

    private void draw_select_district(Canvas canvas) {
        Rect src_selected = new Rect(0,0,b_selected.getWidth(),b_selected.getHeight());
        Rect dst_selected = new Rect(screen_width-2*b_selected.getWidth(), (int)(each_list+fontMetrics.top),(int)(screen_width-2*b_selected.getWidth()+fontMetrics.bottom-fontMetrics.top),(int)(each_list+fontMetrics.bottom));
        Rect src_not_selected = new Rect(0,0,b_selected.getWidth(),b_selected.getHeight());
        Rect dst_not_selected = new Rect(screen_width-2*b_selected.getWidth(), (int)(each_list+fontMetrics.top),(int)(screen_width-2*b_selected.getWidth()+fontMetrics.bottom-fontMetrics.top),(int)(each_list+fontMetrics.bottom));
        Rect dst_hint_selected = new Rect(screen_width-2*b_selected.getWidth(), (int)(3*each_list+fontMetrics.top),(int)(screen_width-2*b_selected.getWidth()+fontMetrics.bottom-fontMetrics.top),(int)(3*each_list+fontMetrics.bottom));
        Rect dst_hint_not_selected = new Rect(screen_width-2*b_selected.getWidth(), (int)(3*each_list+fontMetrics.top),(int)(screen_width-2*b_selected.getWidth()+fontMetrics.bottom-fontMetrics.top),(int)(3*each_list+fontMetrics.bottom));

        int music_puzzle_mode = 0;
        int theme_mode=0;
        int hint_mode=0;
        if(!isInEditMode()) {
            Cursor cursor_for_settings = easy_db.rawQuery("SELECT * FROM music LIMIT 1", null);

            while (cursor_for_settings.moveToNext()) {
                music_puzzle_mode = cursor_for_settings.getInt(0);
                theme_mode=cursor_for_settings.getInt(1);
                hint_mode=cursor_for_settings.getInt(2);
            }
            cursor_for_settings.close();
        }
        if(music_puzzle_mode==1){
            canvas.drawBitmap(b_selected,src_selected,dst_selected,null);
        }
        else{
            canvas.drawBitmap(b_not_selected,src_not_selected,dst_not_selected,null);
        }
        if(theme_mode==0){
            canvas.drawText("Day",(float)0.6*screen_width,each_list*2,draw_each_sets);
            canvas.drawText("Night",(float)0.8*screen_width,each_list*2,draw_each_sets_selected);
        }
        else if(theme_mode==1){
            canvas.drawText("Day",(float)0.6*screen_width,each_list*2,draw_each_sets_selected);
            canvas.drawText("Night",(float)0.8*screen_width,each_list*2,draw_each_sets);
        }
        if(hint_mode==1){
            canvas.drawBitmap(b_selected,src_selected,dst_hint_selected,null);
        }
        else{
            canvas.drawBitmap(b_not_selected,src_not_selected,dst_hint_not_selected,null);
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        int event_x=(int) event.getX();
        int event_y=(int) event.getY();
        int music_puzzle_mode=0;
        int hint_mode=0;
        float textLength_day = draw_each_sets.measureText("Day");
        float textLength_night = draw_each_sets.measureText("Night");
        if(a_c_b(event_x,screen_width-2*b_selected.getWidth())&&a_c_b(screen_width-b_selected.getWidth(),event_x)&&a_c_b(event_y,each_list+fontMetrics.top)&&a_c_b(each_list+fontMetrics.bottom,event_y)){
            if(!isInEditMode()) {
                Cursor cursor_for_settings = easy_db.rawQuery("SELECT * FROM music LIMIT 1", null);
                while (cursor_for_settings.moveToNext()) {
                    music_puzzle_mode = cursor_for_settings.getInt(0);
                }
                cursor_for_settings.close();
            }
            if(music_puzzle_mode==0){
                easy_db.execSQL("update music set click_puzzle=1");
            }
            else{
                easy_db.execSQL("update music set click_puzzle=0");
            }
        }
        else if(a_c_b(each_list*2+fontMetrics.bottom,event_y)&&a_c_b(event_y,each_list*2+fontMetrics.top)){
            if(a_c_b(event_x,0.6*screen_width)&&a_c_b(0.6*screen_width+textLength_day,event_x)){
                easy_db.execSQL("update music set theme=1");
            }
            else if(a_c_b(event_x,0.8*screen_width)&&a_c_b(0.8*screen_width+textLength_night,event_x)){
                easy_db.execSQL("update music set theme=0");
            }
            Intent refersh=new Intent(getContext() , settings_sudoku.class);
            getContext().startActivity(refersh);
        }
        else if(a_c_b(event_x,screen_width-2*b_selected.getWidth())&&a_c_b(screen_width-b_selected.getWidth(),event_x)&&a_c_b(event_y,each_list*3+fontMetrics.top)&&a_c_b(each_list*3+fontMetrics.bottom,event_y)){
            if(!isInEditMode()) {
                Cursor cursor_for_settings = easy_db.rawQuery("SELECT * FROM music LIMIT 1", null);
                while (cursor_for_settings.moveToNext()) {
                    cursor_for_settings.getInt(0);
                    cursor_for_settings.getInt(1);
                    hint_mode =cursor_for_settings.getInt(2);
                }
                cursor_for_settings.close();
            }
            if(hint_mode==0){
                easy_db.execSQL("update music set hint=1");
            }
            else{
                easy_db.execSQL("update music set hint=0");
            }
        }
        invalidate();
        return true;
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

}

