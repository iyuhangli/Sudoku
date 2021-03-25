package sudokugame.example.sudoku.graphical_user_interface;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudoku.R;

import java.math.BigDecimal;

import sudokugame.example.sudoku.control_sudoku.sudoku_data;
import sudokugame.example.sudoku.home_page;
import sudokugame.example.sudoku.sudoku_play_easy;
import sudokugame.example.sudoku.sudoku_play_medium;

public class submit_pop_window_true_medium extends View {
    private Context context;
    private int draw_sudoku_grid_width;
    private int draw_sudoku_grid_height;
    private Bitmap b_congratulation;
    private Bitmap b_homepage;
    private Bitmap b_replay;
    private Bitmap b_share;
    Paint draw_big_line=new Paint();



    public submit_pop_window_true_medium(Context context) {
        super(context);
        this.context=context;

    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        draw_sudoku_grid_width=getResources().getDisplayMetrics().heightPixels/2;
        draw_sudoku_grid_height= (int) (1.65*draw_sudoku_grid_width);

        b_congratulation= BitmapFactory.decodeResource(getResources(), R.drawable.congratulation);
        Rect src_congratulation = new Rect(0,0,b_congratulation.getWidth()+0,b_congratulation.getHeight()+0);
        Rect dst_congratulation = new Rect(0,getWidth()/6+0,getWidth()+0,getWidth()*3/6+0);
        canvas.drawBitmap(b_congratulation, src_congratulation,dst_congratulation,null);

        b_replay=BitmapFactory.decodeResource(getResources(),R.drawable.newgame_icon);
        Rect src_newgame = new Rect(0,0,b_replay.getWidth()+0,b_replay.getHeight()+0);
        Rect dst_newgame = new Rect(getWidth()*7/18,getWidth()*5/9,getWidth()*11/18,getWidth()*7/9+0);
        canvas.drawBitmap(b_replay, src_newgame,dst_newgame,null);

        b_homepage=BitmapFactory.decodeResource(getResources(),R.drawable.index_pause_icon);
        Rect src_homepage = new Rect(0,0,b_homepage.getWidth()+0,b_homepage.getHeight()+0);
        Rect dst_homepage = new Rect(getWidth()*2/9,getWidth()*8/9,getWidth()*3/9,getWidth()*9/9+0);
        canvas.drawBitmap(b_homepage, src_homepage,dst_homepage,null);

        b_share=BitmapFactory.decodeResource(getResources(),R.drawable.share_pause_icon);
        Rect src_share = new Rect(0,0,b_share.getWidth()+0, b_share.getHeight()+0);
        Rect dst_share = new Rect(getWidth()*6/9,getWidth()*8/9,getWidth()*7/9,getWidth()*9/9+0);
        canvas.drawBitmap(b_share, src_share,dst_share,null);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        int digit_x = -1;
        int digit_y = -1;
        int board_event_x = (int) event.getX();
        int board_event_y = (int) event.getY();
        if(a_c_b(board_event_y,getWidth()*8/9)&&a_c_b(getWidth(),board_event_y)){
            if(a_c_b(board_event_x,getWidth()*2/9)&&a_c_b(getWidth()*3/9,board_event_x)){
                Intent to_homepage=new Intent(context, home_page.class);
                context.startActivity(to_homepage);
            }
            else if(a_c_b(board_event_x,getWidth()*6/9)&&a_c_b(getWidth()*7/9,board_event_x)){
                String share_data=get_share_data();

                //get clipboardmanager
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // create char clipdata
                ClipData mClipData = ClipData.newPlainText("Label", share_data);
                // copy clipdata content to system clipdata
                cm.setPrimaryClip(mClipData);

                @SuppressLint("WrongConstant")
                Toast toast_for_share=Toast.makeText(getContext(), "", 3000);
                LinearLayout layout = (LinearLayout) toast_for_share.getView();
                TextView str_for_share = (TextView) layout.getChildAt(0);
                str_for_share.setTextSize(14);
                str_for_share.setTextColor(R.color.white);
                toast_for_share.setText("Please send clipboard contents to friends");
                toast_for_share.setGravity(Gravity.CENTER, 0, 0);
                toast_for_share.show();
            }
        }
        else if(a_c_b(board_event_y,getWidth()*5/9)&&a_c_b(getWidth()*7/9,board_event_y)&&a_c_b(board_event_x,getWidth()*7/18)&&a_c_b(getWidth()*11/18,board_event_x)){
            Intent to_medium=new Intent(context, sudoku_play_medium.class);
            context.startActivity(to_medium);
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

    public String get_share_data(){

        sudoku_data easy_database_for_share;
        SQLiteDatabase easy_db_for_share;
        String all_puzzle = null;
        String half_puzzle = null;
        easy_database_for_share=new sudoku_data(getContext(),"easy.db",null,1);
        easy_db_for_share=easy_database_for_share.getWritableDatabase();
        Cursor cursor=easy_db_for_share.rawQuery("SELECT * FROM easy_level ORDER BY id LIMIT 1",null);
        while (cursor.moveToNext()) {
            int cursor_int=cursor.getInt(0);
            all_puzzle=cursor.getString(1);
            half_puzzle=cursor.getString(2);
        }

        return "&"+half_puzzle+"&";
    }

}
