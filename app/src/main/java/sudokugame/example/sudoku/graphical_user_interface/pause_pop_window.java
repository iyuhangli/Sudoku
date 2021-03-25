package sudokugame.example.sudoku.graphical_user_interface;

import java.util.*;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.*;
import android.widget.*;
import com.example.sudoku.R;
import sudokugame.example.sudoku.control_sudoku.sudoku_data;

public class pause_pop_window extends PopupWindow {

    private Context context;
    private ImageView image_views;
    private View view_circle;
    private List<View> view_lists = new ArrayList<>();
    private float metrics_density;
    private OnPathItemClickListener mOnPathItemClickListener;
    private int icon_x;
    private int icon_y;
    private int item_r;
    private long time_already_use;
    Chronometer timer_in_window;
    private sudoku_data easy_database_for_time;
    private SQLiteDatabase easy_db_for_time;

    public void setOnPathItemClickListener(OnPathItemClickListener mOnPathItemClickListener){
        this.mOnPathItemClickListener = mOnPathItemClickListener;
    }

    public pause_pop_window(Context context) {
        super(context);
        this.context = context;

        metrics_density = context.getResources().getDisplayMetrics().density;
        setFocusable(true);
        setOutsideTouchable(false);
        setTouchable(true);
        int icon_size = (int) (66 * metrics_density);
        int item_size = (int)(306* metrics_density);//(int)(dpValue * scale + 0.5F)
        item_r = (int) (100 * metrics_density);//whole size
        int space_left_plus_right = (int)(53* metrics_density);//four icons move, if  bigger, then left and down.

        ArrayList<four_lists> four = new_list();

        setWidth(item_size);
        setHeight(item_size);
        setBackgroundDrawable(new ColorDrawable());
        FrameLayout layout_all = new FrameLayout(context);
        view_circle = new View(context);
        view_circle.setBackgroundResource(R.drawable.pause_background_control);
        FrameLayout.LayoutParams view_four_icons_settings = new FrameLayout.LayoutParams(item_r * 2, item_r * 2);
        view_four_icons_settings.leftMargin = view_four_icons_settings.topMargin =(int)(53*metrics_density);//(item_size - item_r * 2) / 2;
        layout_all.addView(view_circle, view_four_icons_settings);
        for(int temp = 0;temp<four.size();temp++){
            LinearLayout linearLayout = add_item_views(four.get(temp));
            icon_x=icon_getx(temp,1);
            icon_y=icon_gety(temp,1);
            FrameLayout.LayoutParams four_icons_settings;
            four_icons_settings = new FrameLayout.LayoutParams(icon_size, icon_size);
            four_icons_settings.leftMargin = item_r + icon_x + space_left_plus_right - icon_size / 2;
            four_icons_settings.topMargin = item_r - icon_y + space_left_plus_right - icon_size / 2;
            layout_all.addView(linearLayout, four_icons_settings);
            view_lists.add(linearLayout);
        }
        image_views = new ImageView(context);
        image_views.setImageResource(R.drawable.continue_icon);
        FrameLayout.LayoutParams iv_four_icons_settings = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iv_four_icons_settings.gravity = Gravity.CENTER;
        layout_all.addView(image_views,iv_four_icons_settings);
        setContentView(layout_all);
        image_views.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private ArrayList<four_lists> new_list() {
        ArrayList<four_lists> temp=new ArrayList<>();
        four_lists index_list=new four_lists().get_all_list("Home",R.drawable.index_pause_icon);
        four_lists save_list=new four_lists().get_all_list("Save",R.drawable.save_icon);
        four_lists help_list=new four_lists().get_all_list("Help",R.drawable.help_pause_icon);
        four_lists share_list=new four_lists().get_all_list("Share",R.drawable.share_pause_icon);
        temp.add(index_list);
        temp.add(save_list);
        temp.add(help_list);
        temp.add(share_list);
        return temp;
    }

    public void displayPop(View view){
        easy_database_for_time=new sudoku_data(context,"easy.db",null,1);
        easy_db_for_time=easy_database_for_time.getWritableDatabase();
        showAtLocation(view, Gravity.CENTER, 0, 0);
        RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(true);
        image_views.startAnimation(rotate);
        ScaleAnimation scale_animation = new ScaleAnimation(0.3f, 1f, 0.3f, 1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scale_animation.setDuration(300);
        scale_animation.setFillAfter(true);
        view_circle.startAnimation(scale_animation);
        for(int temp1 = 0; temp1< view_lists.size(); temp1++){
            icon_x=icon_getx(temp1,0);
            icon_y=icon_gety(temp1,0);
            TranslateAnimation translateAnimation = new TranslateAnimation(icon_x, 0F, icon_y, 0F);
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(true);
            translateAnimation.setInterpolator(new OvershootInterpolator(2F));
            view_lists.get(temp1).startAnimation(translateAnimation);
        }
    }

    private boolean is_dismiss = false;

    @Override
    public void dismiss() {
        String cursor_str = null;
        String cursor_str_tau=null;
        if(is_dismiss ||!isShowing()){
            return;
        }
        is_dismiss = true;

        Cursor cursor=easy_db_for_time.rawQuery("SELECT * FROM easy_time ORDER BY id DESC LIMIT 1",null);
        while (cursor.moveToNext()) {
            int cursor_int=cursor.getInt(0);
            cursor_str=cursor.getString(1);
            cursor_str_tau=cursor.getString(2);

        }
        easy_database_for_time.close();
        long timer_in=Long.parseLong(cursor_str);
        long timer_already=Long.parseLong(cursor_str_tau);
        timer_in_window.setBase(timer_in+SystemClock.elapsedRealtime()-timer_already);
        timer_in_window.start();

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(false);
        image_views.startAnimation(rotate);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.3f, 1f, 0.3f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(false);
        view_circle.startAnimation(scaleAnimation);
        for(int i = 0; i< view_lists.size(); i++){
            icon_x= icon_getx(i,0);
            icon_y=icon_gety(i,0);
            TranslateAnimation translate_animation = new TranslateAnimation(0F, icon_x, 0F, icon_y);
            translate_animation.setDuration(300);
            translate_animation.setFillAfter(false);
            if(i == view_lists.size() - 1){
                translate_animation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }
                    public void onAnimationRepeat(Animation animation) {
                    }
                    public void onAnimationEnd(Animation animation) {
                        pause_pop_window.super.dismiss();
                        is_dismiss = false;
                    }
                });
            }
            view_lists.get(i).startAnimation(translate_animation);
        }
    }

    @SuppressLint("ResourceType")
    private LinearLayout add_item_views(final four_lists four_items){
        LinearLayout layout_all = new LinearLayout(context);
        LinearLayout.LayoutParams set_image_views = new LinearLayout.LayoutParams((int)(35* metrics_density), (int)(35* metrics_density));
        LinearLayout.LayoutParams about_four_items = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        layout_all.setPadding((int)(8* metrics_density), (int)(8* metrics_density), (int)(8* metrics_density), (int)(8* metrics_density));
        layout_all.setBackgroundResource(four_items.list_background);
        layout_all.setOrientation(LinearLayout.VERTICAL);
        ImageView image_views = new ImageView(context);
        image_views.setImageResource(four_items.list_image);
        set_icon_black2white(image_views);

        set_image_views.gravity = Gravity.CENTER_HORIZONTAL;

        about_four_items.gravity = Gravity.CENTER_HORIZONTAL;
        TextView text_views = set_text(four_items);

        layout_all.addView(image_views, set_image_views);
        layout_all.addView(text_views, about_four_items);
        layout_all.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(mOnPathItemClickListener != null){
                    mOnPathItemClickListener.onItemClick(four_items);
                }
            }
        });
        return layout_all;
    }

    @SuppressLint("ResourceAsColor")
    private TextView set_text(four_lists four) {
        TextView text= new TextView(context);
        text.setTextColor(R.color.black);
        text.setTextSize(12);
        text.setText(four.list_name);
        return text;
    }

    private void set_icon_black2white(ImageView image_views) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, 255, 0, 1, 0, 0, 255, 0, 0, 1, 0, 255, 0, 0, 0, 1, 0});//light
        image_views.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }

    private int icon_getx(int temp1,int flag) {
        if(flag==0) {
            return -(int) (Math.cos(2 * Math.PI / view_lists.size() * temp1 + Math.PI / 2) * item_r);
        }
        else{
            return (int) (Math.cos(2 * Math.PI / 4 * temp1 + Math.PI / 2) * item_r);
        }
    }

    private int icon_gety(int temp1, int flag){
        if(flag==0) {
            return (int) (Math.sin(2 * Math.PI / view_lists.size() * temp1 + Math.PI / 2) * item_r);
        }
        else {
            return (int) (Math.sin(2 * Math.PI / 4 * temp1 + Math.PI / 2) * item_r);
        }
    }

    public void control_timer(Chronometer timer,long time_for_pause) {
        time_already_use=time_for_pause;
        timer_in_window = timer;

        long temp=timer.getBase();
        String time_base=temp+"";
        String time_already_use_str=""+time_for_pause;
        ContentValues values=new ContentValues();
        values.put("time",time_base);
        values.put("time_already_use", time_already_use_str);
        easy_db_for_time.insert("easy_time",null,values);

        timer.stop();
    }

    public interface OnPathItemClickListener{
        void onItemClick(four_lists item);
    }

    public static class four_lists {
        public String list_name;
        int list_image;
        int list_background;
        four_lists get_all_list(String list_name, int list_image){
            this.list_name=list_name;
            this.list_image=list_image;
            this.list_background=R.drawable.pause_item_background_control;
            return this;
        }
    }
}
