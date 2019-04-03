package com.hyf.intelligence.kotlin.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hyf.intelligence.kotlin.domain.device.FaKongBean;
import com.hyf.intelligence.kotlin.domain.device.ValveUseTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HorizontalChartView extends View {

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
    private int color_plan1 = Color.rgb(164, 206, 104);
    private int color_plan2 = Color.rgb(236, 124, 60);
    private int color_plan3 = Color.rgb(53, 162, 189);
    private int color_plan4 = Color.rgb(42, 187, 157);

    /**
     * 比例图画笔
     */
    private Paint paint_plan1;
    private Paint paint_plan2;
    private Paint paint_plan3;
    private Paint paint_plan4;

    private Paint timePaint;

    private ArrayList<Paint> paints;

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

    private int interval_height;

    /**
     * 线的条数
     */
    private int line_num = 24;

    /**
     * 比例数
     */
    private String ratio_num = "0";


    private ArrayList<ArrayList<FaKongBean>> list;
    private ArrayList<ValveUseTime> mData = new ArrayList<>();
    private SimpleDateFormat sdf;

    public HorizontalChartView(Context context) {
        super(context);
    }

    public HorizontalChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        paint_plan1 = new Paint();
        paint_plan1.setColor(color_plan1);

        paint_plan2 = new Paint();
        paint_plan2.setColor(color_plan2);

        paint_plan3 = new Paint();
        paint_plan3.setColor(color_plan3);

        paint_plan4 = new Paint();
        paint_plan4.setColor(color_plan4);

        paints = new ArrayList<>();
        paints.add(paint_plan1);
        paints.add(paint_plan2);
        paints.add(paint_plan3);
        paints.add(paint_plan4);

        paint_content = new Paint();
        paint_content.setColor(Color.WHITE);
        paint_content.setTextSize(dp2px(context, 12f));
        paint_content.setAntiAlias(true);
        paint_content.setTextAlign(Paint.Align.CENTER);

        paint_font = new Paint();
        paint_font.setColor(color_font);
        paint_font.setTextSize(dp2px(context, 11f));
        paint_font.setAntiAlias(true);
        paint_font.setTextAlign(Paint.Align.CENTER);

        paint_font2 = new Paint();
        paint_font2.setColor(color_font);
        paint_font2.setTextSize(dp2px(context, 11f));
        paint_font2.setAntiAlias(true);
        paint_font2.setTextAlign(Paint.Align.RIGHT);

        timePaint = new Paint();
        timePaint.setColor(Color.RED);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int heights = 0;

        if (hmode == MeasureSpec.AT_MOST) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    heights += 50;
                }
                heights += 50;
            }
        }
        setMeasuredDimension(wsize, heights);
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
        interval_height = 45;
        plan_height = 30;

        for (int i = 0; i < line_num; i++) {
            canvas.save();
            ratio_num = (i + 1) + "";
            //底部百分比数字
            canvas.drawText(ratio_num, lift_width + i * line_width, height - 10, paint_font);
            //网络线
            canvas.drawLine(lift_width + i * line_width, 0, lift_width + i * line_width, line_length, paint);
            canvas.restore();
        }
        canvas.drawText("h", (line_num + 2) * line_width, height - 10, paint_font);

        Calendar calendar = Calendar.getInstance();
        long curr = calendar.get(Calendar.HOUR_OF_DAY) * 3600 + calendar.get(Calendar.MINUTE) * 60;

        canvas.drawLine(line_width + (float) curr / 3600 * line_width, 0, line_width + (float) curr / 3600 * line_width, line_length, timePaint);

        //获取月份文字信息
        Paint.FontMetrics fm1 = paint_font2.getFontMetrics();

//        for (int i = 4; i >= 0; i--) {
//            canvas.drawText(sdf.format(getBeforeOrAfterDate(new Date(), -(i + 1))), lift_width - 10,
//                    ((line_length / 5)) * (i + 1) + (fm1.bottom - fm1.top) / 2 - i * 10 - line_length / 5 / 2, paint_font2);
//
//            for (int j = list.size() - 1; j >= 0; j--) {
//                for (int n = 0; n < list.get(j).size(); n++) {
//                    canvas.save();
//                    canvas.drawRect(line_width * (list.get(j).get(n).getStart() - 1) + lift_width,
//                            interval_height * (i * 4 + j + 3f) - (interval_height + plan_height / 2) + fm1.bottom + 50 * i,
//                            line_width * (list.get(j).get(n).getEnd() - 1) + lift_width,
//                            interval_height * (i * 4 + j + 3f) - (interval_height + plan_height / 2) + plan_height + fm1.bottom + 50 * i,
//                            paints.get(j));
//
//                    canvas.drawText(list.get(j).get(n).getTitls(),
//                            (line_width * (list.get(j).get(n).getEnd() - list.get(j).get(n).getStart() - 2)) / 2 + line_width * (list.get(j).get(n).getStart()) + lift_width,
//                            interval_height * (i * 4 + j + 3f) - interval_height + (fm1.bottom - fm1.top) / 2 + 50 * i, paint_content);
//                    canvas.restore();
//                }
//            }
//
//        }


        for (int i = mData.size() - 1; i >= 0; i--) {
//            canvas.drawText(sdf.format(getBeforeOrAfterDate(new Date(), -(i + 1))), lift_width - 10,
//                    line_length / 5 * (i + 1) + (fm1.bottom - fm1.top) / 2 - i * 10 - line_length / 5 / 2, paint_font2);
            canvas.drawText(String.valueOf(mData.get(i).getDate()), lift_width - 10,
                    line_length / 5 * (i + 1) + (fm1.bottom - fm1.top) / 2 - i * 10 - line_length / 5 / 2, paint_font2);
            for (int j = mData.get(i).getUseTime().size() - 1; j >= 0; j--) {
                for (int n = 0; n < mData.get(i).getUseTime().get(j).getStartEnds().size(); n++) {
                    canvas.save();
                    canvas.drawRect(line_width * (Float.valueOf(mData.get(i).getUseTime().get(j).getStartEnds().get(n).getStart()) - 1) + lift_width,
                            interval_height * (i * 4 + j + 3f) - (interval_height + plan_height / 2) + fm1.bottom + 50 * i,
                            line_width * (Float.valueOf(mData.get(i).getUseTime().get(j).getStartEnds().get(n).getEnd()) - 1) + lift_width,
                            interval_height * (i * 4 + j + 3f) - (interval_height + plan_height / 2) + plan_height + fm1.bottom + 50 * i,
                            paints.get(j));

                    canvas.drawText(mData.get(i).getUseTime().get(j).getStartEnds().get(n).getInterval(),
                            (line_width * (Float.valueOf(mData.get(i).getUseTime().get(j).getStartEnds().get(n).getEnd())
                                    - Float.valueOf(mData.get(i).getUseTime().get(j).getStartEnds().get(n).getStart()) - 2)) / 2
                                    + line_width * Float.valueOf(mData.get(i).getUseTime().get(j).getStartEnds().get(n).getStart()) + lift_width,
                            interval_height * (i * 4 + j + 3f) - interval_height + (fm1.bottom - fm1.top) / 2 + 50 * i, paint_content);
                    canvas.restore();
                }
            }

        }

    }

    public Date getBeforeOrAfterDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        calendar.add(Calendar.DATE, num);
        Date d = calendar.getTime();//把日历转换为Date
        return d;
    }

    public void setData(ArrayList<ArrayList<FaKongBean>> list) {
        this.list.addAll(list);
        invalidate();
    }

    public void setDatas(ArrayList<ValveUseTime> data) {
        this.mData.addAll(data);
        invalidate();
    }

    private int dp2px(Context context, Float f) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                f, context.getResources().getDisplayMetrics());
    }

}