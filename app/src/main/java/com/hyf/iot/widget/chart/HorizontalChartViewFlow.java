package com.hyf.iot.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hyf.iot.domain.device.FaKongBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HorizontalChartViewFlow extends View {

    /**
     * 间隔线画笔
     */
    private Paint paint;

    /**
     * 线的颜色
     */
    private int color_line = Color.rgb(230, 230, 230);

    /**
     * 字的颜色
     */
    private int color_font = Color.rgb(51, 51, 51);

    /**
     * 比例图颜色
     */
    private int color_plan = Color.rgb(42, 187, 157);

    /**
     * 比例图画笔
     */
    private Paint paint_plan;

    /**
     * 比例图高度
     */
    private int plan_height;

    /**
     * 文字画笔1
     */
    private Paint paint_font;

    /**
     * 文字画笔2
     */
    private Paint paint_font2;

    private Paint paint_content;

    /**
     * 线的条数
     */
    private int line_num = 24;

    /**
     * 比例数
     */
    private String ratio_num = "0";


    private ArrayList<FaKongBean> list;
    private SimpleDateFormat sdf;

    public HorizontalChartViewFlow(Context context) {
        super(context);
    }

    public HorizontalChartViewFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalChartViewFlow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalChartViewFlow(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    public void init(Context context, AttributeSet attrs) {

        sdf = new SimpleDateFormat("dd");
        list = new ArrayList<>();
        paint = new Paint();
        paint.setColor(color_line);

        paint_plan = new Paint();
        paint_plan.setColor(color_plan);

        paint_content = new Paint();
        paint_content.setColor(Color.WHITE);
        paint_content.setTextSize(dp2px(context, 12f));
        paint_content.setAntiAlias(true);
        paint_content.setTextAlign(Paint.Align.CENTER);

        paint_font = new Paint();
        paint_font.setColor(color_font);
        paint_font.setTextSize(dp2px(context, 12f));
        paint_font.setAntiAlias(true);
        paint_font.setTextAlign(Paint.Align.CENTER);

        paint_font2 = new Paint();
        paint_font2.setColor(color_font);
        paint_font2.setTextSize(dp2px(context, 12f));
        paint_font2.setAntiAlias(true);
        paint_font2.setTextAlign(Paint.Align.RIGHT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int lift_width = (int) (width * 0.08);
        int line_width = (int) (width / 27);
        //获取底部文字信息
        Paint.FontMetrics fm = paint_font.getFontMetrics();

        int line_length = (int) (height - (fm.bottom - fm.top) - 4);

        plan_height = 35;

        for (int i = 0; i < line_num; i++) {
            canvas.save();
            ratio_num = (i + 1) + "";
            if ((i + 1) % 4 == 0)
                //底部数字
                canvas.drawText(ratio_num, lift_width + i * line_width, height - 10, paint_font);
            //网络线
            canvas.drawLine(lift_width + i * line_width, 0, lift_width + i * line_width, line_length, paint);
            canvas.restore();
        }
        canvas.drawText("h", (line_num + 2) * line_width, height - 10, paint_font);
        //获取月份文字信息
        Paint.FontMetrics fm1 = paint_font2.getFontMetrics();

        if(list.size() == 0)
            return;
        for (int i = 6; i >= 0; i--) {
            canvas.save();
            //纵坐标日期
            canvas.drawText(sdf.format(getBeforeOrAfterDate(new Date(), -(i + 1))), lift_width - 10,
                    ((line_length / 7)) * (i + 1) + (fm1.bottom - fm1.top) / 2 - 40, paint_font2);
            //比例图
            canvas.drawRect(line_width * (list.get(i).getStart()) + lift_width,
                    line_length / list.size() * (list.size() - i) - (line_length / 20 + plan_height / 2), // + fm1.bottom
                    line_width * (list.get(i).getEnd()) + lift_width,
                    line_length / list.size() * (list.size() - i) - (line_length / 20 + plan_height / 2) + plan_height,  // + fm1.bottom
                    paint_plan);

            canvas.drawText(list.get(i).getTitls(),
                    (line_width * (list.get(i).getEnd() - list.get(i).getStart())) / 2 + line_width * (list.get(i).getStart()) + lift_width,
                    line_length / list.size() * (list.size() - i) - line_length / 20 + (fm1.bottom - fm1.top) / 2 - 10, paint_content);

            canvas.restore();
        }
    }

    public Date getBeforeOrAfterDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        calendar.add(Calendar.DATE, num);
        Date d = calendar.getTime();//把日历转换为Date
        return d;
    }

    public void setData(ArrayList<FaKongBean> list) {
        this.list.addAll(list);
        invalidate();
    }

    private int dp2px(Context context, Float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                f, context.getResources().getDisplayMetrics());
    }

}