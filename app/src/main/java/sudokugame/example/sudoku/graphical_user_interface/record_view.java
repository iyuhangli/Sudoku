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
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sudoku.R;
import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.home_page;

import java.math.BigDecimal;

public class record_view extends View {
    Bitmap b_return;
    Bitmap b_started;
    Bitmap b_started_one;
    Bitmap b_started_two;
    Bitmap b_not_very_skilled;
    Bitmap b_not_very_skilled_one;
    Bitmap b_not_very_skilled_two;
    Bitmap b_skilled;
    Bitmap b_skilled_one;
    Bitmap b_skilled_two;
    Bitmap b_skilled_three;
    Bitmap b_mogul;
    Bitmap b_mogul_one;
    Bitmap b_mogul_two;
    Bitmap b_mogul_three;
    Bitmap b_invincible;
    Bitmap b_invincible_one;
    Bitmap b_invincible_two;
    Bitmap b_invincible_three;
    Bitmap b_invincible_four;
    Bitmap b_invincible_five;
    Bitmap b_invincible_six;
    Bitmap b_novice;
    Bitmap b_novice_one;
    Bitmap b_novice_two;
    Bitmap b_novice_three;
    Bitmap b_novice_four;
    Bitmap b_novice_five;
    Bitmap b_novice_six;
    Bitmap b_rookie;
    Bitmap b_rookie_one;
    Bitmap b_rookie_two;
    Bitmap b_rookie_three;
    Bitmap b_rookie_four;
    Bitmap b_rookie_five;
    Bitmap b_rookie_six;
    Bitmap b_rookie_seven;
    Bitmap b_rookie_eight;
    Bitmap b_rookie_nine;
    Bitmap b_rookie10;
    Bitmap b_rookie11;
    Bitmap b_rookie12;
    Bitmap b_rookie13;
    Bitmap b_rookie14;
    Bitmap b_rookie15;
    Bitmap b_rookie16;
    Bitmap b_rookie17;
    Bitmap b_rookie18;
    Bitmap b_rookie19;
    Bitmap b_rookie20;
    Bitmap b_rookie_completed;
    Bitmap b_unknow;
    private sudoku_data easy_database;
    private SQLiteDatabase easy_db;
    private int screen_height;
    private int screen_width;
    private String date_easy=null;
    private String half_easy=null;
    private String date_medium=null;
    private String half_medium=null;
    private String date_hard=null;
    private String half_hard=null;
    private int mark=0;

    Paint draw_text=new Paint();

    public record_view(Context context) {
        super(context);
        do_before_onDraw();
    }

    public record_view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        do_before_onDraw();

    }

    public record_view(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        do_before_onDraw();

    }

    private void do_before_onDraw() {
        if(!isInEditMode()) {
            easy_database = new sudoku_data(getContext(), "easy.db", null, 1);
            easy_db = easy_database.getWritableDatabase();
        }
        b_return = BitmapFactory.decodeResource(getResources(), R.drawable.laststep_icon);
        b_started = BitmapFactory.decodeResource(getResources(), R.drawable.just_started);
        b_started_one = BitmapFactory.decodeResource(getResources(), R.drawable.just_started_one);
        b_started_two = BitmapFactory.decodeResource(getResources(), R.drawable.just_started_two);
        b_not_very_skilled = BitmapFactory.decodeResource(getResources(), R.drawable.not_very_skilled);
        b_not_very_skilled_one = BitmapFactory.decodeResource(getResources(), R.drawable.not_very_skilled_one);
        b_not_very_skilled_two = BitmapFactory.decodeResource(getResources(), R.drawable.not_very_skilled_two);
        b_skilled = BitmapFactory.decodeResource(getResources(), R.drawable.skilled);
        b_skilled_one = BitmapFactory.decodeResource(getResources(), R.drawable.skilled_one);
        b_skilled_two = BitmapFactory.decodeResource(getResources(), R.drawable.skilled_two);
        b_skilled_three = BitmapFactory.decodeResource(getResources(), R.drawable.skilled_three);
        b_mogul = BitmapFactory.decodeResource(getResources(), R.drawable.mogul);
        b_mogul_one = BitmapFactory.decodeResource(getResources(), R.drawable.mogul_one);
        b_mogul_two = BitmapFactory.decodeResource(getResources(), R.drawable.mogul_two);
        b_mogul_three = BitmapFactory.decodeResource(getResources(), R.drawable.mogul_three);
        b_invincible = BitmapFactory.decodeResource(getResources(), R.drawable.invincible);
        b_invincible_one = BitmapFactory.decodeResource(getResources(), R.drawable.invincible_one);
        b_invincible_two = BitmapFactory.decodeResource(getResources(), R.drawable.invincible_two);
        b_invincible_three = BitmapFactory.decodeResource(getResources(), R.drawable.invincible_three);
        b_invincible_four = BitmapFactory.decodeResource(getResources(), R.drawable.invincible_four);
        b_invincible_five= BitmapFactory.decodeResource(getResources(), R.drawable.invincible_five);
        b_invincible_six = BitmapFactory.decodeResource(getResources(), R.drawable.invincible_six);
        b_novice = BitmapFactory.decodeResource(getResources(), R.drawable.novice);
        b_novice_one = BitmapFactory.decodeResource(getResources(), R.drawable.novice_one);
        b_novice_two = BitmapFactory.decodeResource(getResources(), R.drawable.novice_two);
        b_novice_three = BitmapFactory.decodeResource(getResources(), R.drawable.novice_three);
        b_novice_four = BitmapFactory.decodeResource(getResources(), R.drawable.novice_four);
        b_novice_five = BitmapFactory.decodeResource(getResources(), R.drawable.novice_five);
        b_novice_six = BitmapFactory.decodeResource(getResources(), R.drawable.novice_six);
        b_rookie = BitmapFactory.decodeResource(getResources(), R.drawable.rookie);
        b_rookie_one = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_one);
        b_rookie_two = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_two);
        b_rookie_three = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_three);
        b_rookie_four = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_four);
        b_rookie_five = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_five);
        b_rookie_six = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_six);
        b_rookie_seven = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_seven);
        b_rookie_eight = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_eight);
        b_rookie_nine = BitmapFactory.decodeResource(getResources(), R.drawable.rookie_nine);
        b_rookie10 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie10);
        b_rookie11 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie11);
        b_rookie12 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie12);
        b_rookie13 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie13);
        b_rookie14 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie14);
        b_rookie15 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie15);
        b_rookie16 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie16);
        b_rookie17 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie17);
        b_rookie18 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie18);
        b_rookie19 = BitmapFactory.decodeResource(getResources(), R.drawable.rookie19);
        b_rookie_completed = BitmapFactory.decodeResource(getResources(), R.drawable.rookiecompleted);
        b_unknow=BitmapFactory.decodeResource(getResources(),R.drawable.unknow);

        screen_height=getResources().getDisplayMetrics().heightPixels;
        screen_width=getResources().getDisplayMetrics().widthPixels;
        if(!isInEditMode()) {
            easy_database = new sudoku_data(getContext(), "easy.db", null, 1);
            easy_db = getContext().openOrCreateDatabase("easy.db", Context.MODE_PRIVATE, null);

            Cursor cursor_score = easy_db.rawQuery("SELECT * FROM mark ORDER BY mark DESC LIMIT 1", null);
            while (cursor_score.moveToNext()) {
                mark = cursor_score.getInt(0);
            }
            cursor_score.close();
        }

        draw_text.setTextAlign(Paint.Align.CENTER);
        draw_text.setTextSize(screen_width/15);
        draw_text.setColor(getResources().getColor(R.color.black));
    }



    public void onDraw(Canvas canvas){
        draw_honors(canvas);

    }

    private void draw_honors(Canvas canvas) {
        Rect src_return = new Rect(0,0,b_return.getWidth(),b_return.getHeight());
        Rect dst_return = new Rect(screen_width-b_return.getWidth()*14/9,b_return.getHeight()*5/9,screen_width-b_return.getWidth()*5/9,b_return.getHeight()*14/9);
        canvas.drawBitmap(b_return,src_return,dst_return,null);

        Rect src_record = new Rect(0,0,b_started.getWidth(),b_started.getHeight());
        Rect dst_mark = new Rect(screen_width*4/16,screen_height/10,screen_width*12/16,screen_height/10+screen_width*20/47);
        Rect dst_unknow1=new Rect(screen_width*4/16, screen_height/10+(screen_width*20/47)*7/6,screen_width*12/16,screen_height/10+(screen_width*20/47)*13/6);
        Rect dst_unknow2=new Rect(screen_width*4/16, screen_height/10+(screen_width*20/47)*14/6,screen_width*12/16,screen_height/10+(screen_width*20/47)*20/6);

        if(mark<10) {
            canvas.drawBitmap(b_started, src_record, dst_mark, null);
        }
        else if(mark <20){
            canvas.drawBitmap(b_started_one, src_record, dst_mark, null);
        }
        else if(mark <30){
            canvas.drawBitmap(b_started_two, src_record, dst_mark, null);
        }
        else if(mark <40){
            canvas.drawBitmap(b_skilled_three, src_record, dst_mark, null);
        }
        else if(mark <50){
            canvas.drawBitmap(b_not_very_skilled, src_record, dst_mark, null);
        }
        else if(mark <60){
            canvas.drawBitmap(b_not_very_skilled_one, src_record, dst_mark, null);
        }
        else if(mark <70){
            canvas.drawBitmap(b_not_very_skilled_two, src_record, dst_mark, null);
        }
        else if(mark <80){
            canvas.drawBitmap(b_skilled, src_record, dst_mark, null);
        }
        else if(mark <90){
            canvas.drawBitmap(b_skilled_one, src_record, dst_mark, null);
        }
        else if(mark <100){
            canvas.drawBitmap(b_skilled_two, src_record, dst_mark, null);
        }
        else if(mark <110){
            canvas.drawBitmap(b_skilled_three, src_record, dst_mark, null);
        }
        else if(mark <120){
            canvas.drawBitmap(b_mogul, src_record, dst_mark, null);
        }
        else if(mark <130){
            canvas.drawBitmap(b_mogul_one, src_record, dst_mark, null);
        }
        else if(mark <140){
            canvas.drawBitmap(b_mogul_two, src_record, dst_mark, null);
        }
        else if(mark <150){
            canvas.drawBitmap(b_mogul_three, src_record, dst_mark, null);
        }
        else if(mark <160){
            canvas.drawBitmap(b_invincible, src_record, dst_mark, null);
        }
        else if(mark <170){
            canvas.drawBitmap(b_invincible_one, src_record, dst_mark, null);
        }
        else if(mark <180){
            canvas.drawBitmap(b_invincible_two, src_record, dst_mark, null);
        }
        else if(mark <190){
            canvas.drawBitmap(b_invincible_three, src_record, dst_mark, null);
        }
        else if(mark <200){
            canvas.drawBitmap(b_invincible_four, src_record, dst_mark, null);
        }
        else if(mark <210){
            canvas.drawBitmap(b_invincible_five, src_record, dst_mark, null);
        }
        else if(mark <220){
            canvas.drawBitmap(b_invincible_six, src_record, dst_mark, null);
        }
        else if(mark <230){
            canvas.drawBitmap(b_novice, src_record, dst_mark, null);
        }
        else if(mark <240){
            canvas.drawBitmap(b_novice_one, src_record, dst_mark, null);
        }
        else if(mark <250){
            canvas.drawBitmap(b_novice_two, src_record, dst_mark, null);
        }
        else if(mark <260){
            canvas.drawBitmap(b_novice_three, src_record, dst_mark, null);
        }
        else if(mark <270){
            canvas.drawBitmap(b_novice_four, src_record, dst_mark, null);
        }
        else if(mark <280){
            canvas.drawBitmap(b_novice_five, src_record, dst_mark, null);
        }
        else if(mark <290){
            canvas.drawBitmap(b_novice_six, src_record, dst_mark, null);
        }
        else if(mark <300){
            canvas.drawBitmap(b_rookie, src_record, dst_mark, null);
        }
        else if(mark <310){
            canvas.drawBitmap(b_rookie_one, src_record, dst_mark, null);
        }
        else if(mark <320){
            canvas.drawBitmap(b_rookie_two, src_record, dst_mark, null);
        }
        else if(mark <330){
            canvas.drawBitmap(b_rookie_three, src_record, dst_mark, null);
        }
        else if(mark <340){
            canvas.drawBitmap(b_rookie_four, src_record, dst_mark, null);
        }
        else if(mark <350){
            canvas.drawBitmap(b_rookie_five, src_record, dst_mark, null);
        }
        else if(mark <360){
            canvas.drawBitmap(b_rookie_six, src_record, dst_mark, null);
        }
        else if(mark <370){
            canvas.drawBitmap(b_rookie_seven, src_record, dst_mark, null);
        }
        else if(mark <380){
            canvas.drawBitmap(b_rookie_eight, src_record, dst_mark, null);
        }
        else if(mark <390){
            canvas.drawBitmap(b_rookie_nine, src_record, dst_mark, null);
        }
        else if(mark <400){
            canvas.drawBitmap(b_rookie10, src_record, dst_mark, null);
        }
        else if(mark <410){
            canvas.drawBitmap(b_rookie11, src_record, dst_mark, null);
        }
        else if(mark <420){
            canvas.drawBitmap(b_rookie12, src_record, dst_mark, null);
        }
        else if(mark <430){
            canvas.drawBitmap(b_rookie13, src_record, dst_mark, null);
        }
        else if(mark <440){
            canvas.drawBitmap(b_rookie14, src_record, dst_mark, null);
        }
        else if(mark <450){
            canvas.drawBitmap(b_rookie15, src_record, dst_mark, null);
        }
        else if(mark <460){
            canvas.drawBitmap(b_rookie16, src_record, dst_mark, null);
        }
        else if(mark <470){
            canvas.drawBitmap(b_rookie17, src_record, dst_mark, null);
        }
        else if(mark <480){
            canvas.drawBitmap(b_rookie18, src_record, dst_mark, null);
        }
        else if(mark <490){
            canvas.drawBitmap(b_rookie19, src_record, dst_mark, null);
        }
        else if(mark <500){
            canvas.drawBitmap(b_rookie_completed, src_record, dst_mark, null);
        }
        else {
            canvas.drawBitmap(b_rookie_completed, src_record, dst_mark, null);
        }

        canvas.drawBitmap(b_unknow,src_record,dst_unknow1,null);
        canvas.drawBitmap(b_unknow,src_record,dst_unknow2,null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        int board_event_x=(int) event.getX();
        int board_event_y=(int) event.getY();
        if(a_c_b(board_event_x,screen_width-b_return.getWidth()*14/9)&&a_c_b(screen_width-b_return.getWidth()*5/9,board_event_x)&&a_c_b(board_event_y,b_return.getHeight()*5/9)&&a_c_b(b_return.getHeight()*14/9,board_event_y)){
            Intent to_teaching = new Intent(getContext(), home_page.class);
            getContext().startActivity(to_teaching);
        }

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
