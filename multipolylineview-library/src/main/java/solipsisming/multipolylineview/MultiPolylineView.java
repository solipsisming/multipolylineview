package solipsisming.multipolylineview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

/**
 * 多维折线图
 * <p>
 * 创建于 2018-4-13 11:19:30
 *
 * @author 洪东明
 */
public class MultiPolylineView extends View {

    private Context mContext;//当前应用
    private RectF clipRectF;//剪切的矩形
    private Rect textRect;//文本矩形
    private Rect calcRect;//计算文本矩形
    //画笔
    private Paint axisPaint = new Paint();
    private Paint pointPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint xTextPaint = new Paint();
    private Paint rightTextPaint = new Paint();
    private Paint leftTextPaint = new Paint();
    private Paint typeBackgroundPaint = new Paint();
    private Paint clipPaint = new Paint();
    //界面宽高度
    private int width;
    private int height;
    //坐标轴边距
    private final int TOP_X_AXIS_MARGIN = dpToPx(10);
    private final int BOTTOM_X_AXIS_MARGIN = dpToPx(10);
    private final int LEFT_Y_AXIS_MARGIN = dpToPx(10);
    private final int RIGHT_Y_AXIS_MARGIN = dpToPx(10);

    private final int Y_TYPE_MARGIN = dpToPx(15);//y轴每个类型之间的边距
    //    private final int X_TEXT_MARGIN = dpToPx(20);//x轴文本之间的边距
    private final int X_TEXT_AXIS_DEFAULT_MARGIN = dpToPx(5);//文本距离x轴的默认边距
    private final int RIGHT_Y_TEXT_AXIS_DEFAULT_MARGIN = dpToPx(5);//文本距离右边y轴的默认边距
    private final int LEFT_Y_TEXT_AXIS_DEFAULT_MARGIN = dpToPx(5);//文本距离右边y轴的默认边距
    private final int X_TEXT_DEFAULT_TEXTSIZE = spToPx(12);
    private final int LEFT_Y_TEXT_DEFAULT_TEXTSIZE = spToPx(12);
    private final int RIGHT_Y_DEFAULT_TEXTSIZE = spToPx(12);
    private static final int POINT_RADIUS = 8;//圆点的半径
    private static final int X_TEXT_DEFAULT_NUM = 5;//x轴默认显示个数
    private static final int Y_TYPE_DEFAULT_NUM = 5;//y轴默认显示个数
    private static final int LINE_DEFAULT_WIDTH = 3;//折线的默认宽度
    private static final int AXIS_DEFAULT_WIDTH = 3;//坐标轴的默认宽度
    //x轴处理
    private int xAxisLen;//x轴长度
    private int xMaxTextLen;//x轴文本放在x轴的最大长度
    private int xTextShowNum = X_TEXT_DEFAULT_NUM;//默认显示的类型个数
    private int xTextMargin;//x轴文本之间的边距
    private int xAxisMargin;//文本距离x轴的边距
    private int xTextMaxWidth;//文本宽度
    private int xTextMaxHeight;//文本高度
    //y轴
    private int yAxisLen;//y轴长度
    private float yMaxTypeLen;//y轴类型的最长文本长度
    private float yTypeMargin = Y_TYPE_MARGIN;//y轴文本之间的边距
    private float yTypeAxisHeight;//y轴显示文本所占的区域高度
    private int yTextShowNum = Y_TYPE_DEFAULT_NUM;//默认显示的类型个数
    //左边y轴
    private int leftYAxisMargin;//左边y轴文本之间的边距
    private int leftYMaxTextHeight;//文本高度
    private int leftYMaxTextWidth;//文本宽度
    //右边y轴
    private int rightYAxisMargin;//右边文本距离y轴的边距
    private int rightYMaxTextHeight;//文本高度
    private int rightYMaxTextWidth;//文本宽度
    //圆点坐标
    private float topLeftOriginX;//左上角圆点x轴
    private float topLeftOriginY;//左上角圆点y轴
    private float topRightOriginX;//右上角圆点x轴
    private float topRightOriginY;//右上角圆点y轴
    private float bottomLeftOriginX;//左下角圆点x轴
    private float bottomLeftOriginY;//左下角圆点y轴
    private float bottomRightOriginX;//右下角圆点x轴
    private float bottomRightOriginY;//右下角圆点y轴
    //背景色
    private int mBackground = Color.TRANSPARENT;
    //线的颜色
    private int lineColor = Color.WHITE;
    //点的颜色
    private int pointColor = Color.WHITE;
    //左边文本的颜色
    private int leftYTextColor = Color.WHITE;
    //右边文本的颜色
    private int rightYTextColor = Color.WHITE;
    //x轴文本颜色
    private int xTextColor = Color.WHITE;
    //坐标轴颜色
    private int axisColor = Color.WHITE;
    //最大值颜色
    private int maxPointColor = Color.WHITE;
    //最小值颜色
    private int minPointColor = Color.WHITE;
    //圆点的半径
    private int mPointRadius = POINT_RADIUS;
    //x轴文本字体大小
    private int xAxisTextSize = X_TEXT_DEFAULT_TEXTSIZE;
    //左边y轴文本字体大小
    private int leftYAxisTextSize = LEFT_Y_TEXT_DEFAULT_TEXTSIZE;
    //右边y轴文本字体大小
    private int rightYAxisTextSize = RIGHT_Y_DEFAULT_TEXTSIZE;
    //线的宽度
    private int lineWidth = LINE_DEFAULT_WIDTH;
    //坐标轴的宽度
    private int axisWidth = AXIS_DEFAULT_WIDTH;
    //y轴数据
    private List<Type> yTypes;
    //x轴数据
    private String[] xTexts;
    //默认文本
    private static final String DEFAULT_X_AXIS_TEXT = "MM";
    //类型颜色
    private int[] mTypeColrs = TYPE_DEFAULT_COLOR;
    //背景颜色
    private static final int[] TYPE_DEFAULT_COLOR = {
            Color.parseColor("#26CDDD")
            , Color.parseColor("#FF8F78"),
            Color.parseColor("#00CD9B")
            , Color.parseColor("#1EB1ED")
            , Color.parseColor("#E8B039")};

    public MultiPolylineView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MultiPolylineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiPolylineView);
            int count = attrs.getAttributeCount();

            ColorStateList backgroundColorStateList = null;
            ColorStateList lineColorStateList = null;
            ColorStateList pointColorStateList = null;
            ColorStateList maxPointColorStateList = null;
            ColorStateList minPointColorStateList = null;
            ColorStateList axisColorStateList = null;
            ColorStateList leftYAxisTextColorStateList = null;
            ColorStateList rightYAxisTextColorStateList = null;
            ColorStateList xAxisTextColorStateList = null;

            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.MultiPolylineView_viewBackground) {
                    backgroundColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_lineColor) {
                    lineColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_pointColor) {
                    pointColorStateList = a.getColorStateList(attr);

                    maxPointColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_maxPointColor) {
                    maxPointColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_minPointColor) {
                    minPointColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_axisColor) {
                    axisColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_leftYAxisTextColor) {
                    leftYAxisTextColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_rightYAxisTextColor) {
                    rightYAxisTextColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_xAxisTextColor) {
                    xAxisTextColorStateList = a.getColorStateList(attr);

                } else if (attr == R.styleable.MultiPolylineView_yAxisTypeMargin) {
                    yTypeMargin = a.getDimension(attr, Y_TYPE_MARGIN);

                } else if (attr == R.styleable.MultiPolylineView_xAxisTextSize) {
                    xAxisTextSize = a.getDimensionPixelSize(attr, X_TEXT_DEFAULT_TEXTSIZE);

                } else if (attr == R.styleable.MultiPolylineView_leftYAxisTextSize) {
                    leftYAxisTextSize = a.getDimensionPixelSize(attr, LEFT_Y_TEXT_DEFAULT_TEXTSIZE);

                } else if (attr == R.styleable.MultiPolylineView_rightYAxisTextSize) {
                    rightYAxisTextSize = a.getDimensionPixelSize(attr, RIGHT_Y_DEFAULT_TEXTSIZE);

                } else if (attr == R.styleable.MultiPolylineView_pointRadius) {
                    mPointRadius = a.getInteger(attr, POINT_RADIUS);

                } else if (attr == R.styleable.MultiPolylineView_lineWidth) {
                    lineWidth = a.getInteger(attr, LINE_DEFAULT_WIDTH);

                } else if (attr == R.styleable.MultiPolylineView_axisWidth) {
                    axisWidth = a.getInteger(attr, AXIS_DEFAULT_WIDTH);

                    xTextShowNum = a.getInteger(attr, X_TEXT_DEFAULT_NUM);

                } else if (attr == R.styleable.MultiPolylineView_xAxisShowNum) {
                    xTextShowNum = a.getInteger(attr, X_TEXT_DEFAULT_NUM);

                } else if (attr == R.styleable.MultiPolylineView_yAxisShowNum) {
                    yTextShowNum = a.getInteger(attr, Y_TYPE_DEFAULT_NUM);

                }
            }

            if (backgroundColorStateList != null)
                mBackground = backgroundColorStateList.getColorForState(getDrawableState(), Color.TRANSPARENT);
            if (lineColorStateList != null)
                lineColor = lineColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (pointColorStateList != null)
                pointColor = pointColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (maxPointColorStateList != null)
                maxPointColor = maxPointColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (minPointColorStateList != null)
                minPointColor = minPointColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (axisColorStateList != null)
                axisColor = axisColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (leftYAxisTextColorStateList != null)
                leftYTextColor = leftYAxisTextColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (rightYAxisTextColorStateList != null)
                rightYTextColor = rightYAxisTextColorStateList.getColorForState(getDrawableState(), Color.WHITE);
            if (xAxisTextColorStateList != null)
                xTextColor = xAxisTextColorStateList.getColorForState(getDrawableState(), Color.WHITE);

            a.recycle();
        }

        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        initPaint();
        initXTextDefaultWidth();
        clipRectF = new RectF();
        textRect = new Rect();
        calcRect = new Rect();
    }

    /**
     * 默认x轴文本的最大宽度
     */
    private void initXTextDefaultWidth() {
        Rect defaultRect = new Rect();
        xTextPaint.getTextBounds(DEFAULT_X_AXIS_TEXT, 0, DEFAULT_X_AXIS_TEXT.length(), defaultRect);
        xTextMaxWidth = defaultRect.width();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        axisPaint.setColor(axisColor);
        axisPaint.setDither(true);
        axisPaint.setAntiAlias(true);
        axisPaint.setStrokeWidth(axisWidth);

        xTextPaint.setTextSize(xAxisTextSize);
        xTextPaint.setColor(xTextColor);

        rightTextPaint.setTextSize(rightYAxisTextSize);
        rightTextPaint.setColor(rightYTextColor);

        leftTextPaint.setTextSize(leftYAxisTextSize);
        leftTextPaint.setColor(leftYTextColor);

        typeBackgroundPaint.setColor(mTypeColrs[0]);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setDither(true);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);
        linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setColor(pointColor);

        clipPaint = new Paint();
        clipPaint.setStyle(Paint.Style.FILL);
        clipPaint.setColor(Color.TRANSPARENT);
        clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    /**
     * 设置左边文本大小
     */
    public MultiPolylineView setLeftYAxisTextSize(int textSize) {
        leftTextPaint.setTextSize(textSize);
        return this;
    }

    /**
     * 设置右边文本大小
     */
    public MultiPolylineView setRightYAxisTextSize(int textSize) {
        rightTextPaint.setTextSize(textSize);
        return this;
    }

    /**
     * 设置x轴文本大小
     */
    public MultiPolylineView setXAxisTextSize(int textSize) {
        xTextPaint.setTextSize(textSize);
        return this;
    }

    public MultiPolylineView setPointRadiusSize(int radius) {
        mPointRadius = dpToPx(radius);
        return this;
    }

    /**
     * 设置折线的宽度
     */
    public MultiPolylineView setLineWidth(int width) {
        linePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置坐标轴的宽度
     */
    public MultiPolylineView setAxisWidth(int width) {
        axisPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置背景色
     */
    public MultiPolylineView setBackground(String colorStr) {
        setViewBackground(Color.parseColor(colorStr));
        return this;
    }

    /**
     * 设置背景色
     */
    public MultiPolylineView setViewBackground(ColorStateList colorStateList) {
        setViewBackground(colorStateList.getColorForState(getDrawableState(), 0));
        return this;
    }

    /**
     * 设置背景色
     */
    public MultiPolylineView setViewBackground(int color) {
        mBackground = color;
        return this;
    }

    /**
     * 设置y轴之间的边距
     */
    public MultiPolylineView setYTypeMargin(int margin) {
        yTypeMargin = dpToPx(margin);
        return this;
    }

    /**
     * 设置线的颜色
     */
    public MultiPolylineView setLineColor(String colorStr) {
        return setLineColor(Color.parseColor(colorStr));
    }

    /**
     * 设置线的颜色
     */
    public MultiPolylineView setLineColor(ColorStateList colorStateList) {
        return setLineColor(colorStateList.getColorForState(getDrawableState(), 0));
    }

    /**
     * 设置线的颜色
     */
    public MultiPolylineView setLineColor(int color) {
        linePaint.setColor(color);
        return this;
    }

    /**
     * 设置点的颜色
     */
    public MultiPolylineView setPointColor(ColorStateList colorStateList) {
        return setPointColor(colorStateList.getColorForState(getDrawableState(), 0));
    }

    /**
     * 设置点的颜色
     */
    public MultiPolylineView setPointColor(String colorStr) {
        return setPointColor(Color.parseColor(colorStr));
    }

    /**
     * 设置点的颜色
     */
    public MultiPolylineView setPointColor(int color) {
        pointPaint.setColor(color);
        return this;
    }

    /**
     * 设置最大值点的颜色
     */
    public MultiPolylineView setMaxPointColor(ColorStateList colorStateList) {
        return setMaxPointColor(colorStateList.getColorForState(getDrawableState(), 0));
    }

    /**
     * 设置最大值点的颜色
     */
    public MultiPolylineView setMaxPointColor(String colorStr) {
        return setMaxPointColor(Color.parseColor(colorStr));
    }

    /**
     * 设置最大值点的颜色
     */
    public MultiPolylineView setMaxPointColor(int color) {
        maxPointColor = color;
        return this;
    }

    /**
     * 设置最小值点的颜色
     */
    public MultiPolylineView setMinPointColor(ColorStateList colorStateList) {
        return setMinPointColor(colorStateList.getColorForState(getDrawableState(), 0));
    }

    /**
     * 设置最小值点的颜色
     */
    public MultiPolylineView setMinPointColor(String colorStr) {
        return setMinPointColor(Color.parseColor(colorStr));
    }

    /**
     * 设置最小值点的颜色
     */
    public MultiPolylineView setMinPointColor(int color) {
        minPointColor = color;
        return this;
    }

    /**
     * 设置x轴文本颜色
     */
    public MultiPolylineView setXAxisTextColor(String colorStr) {
        setXAxisTextColor(Color.parseColor(colorStr));
        return this;
    }

    /**
     * 设置x轴文本颜色
     */
    public MultiPolylineView setXAxisTextColor(ColorStateList colorStateList) {
        setXAxisTextColor(colorStateList.getColorForState(getDrawableState(), 0));
        return this;
    }

    /**
     * 设置x轴文本颜色
     */
    public MultiPolylineView setXAxisTextColor(int colorStr) {
        xTextPaint.setColor(colorStr);
        return this;
    }

    /**
     * 设置左边y轴文本颜色
     */
    public MultiPolylineView setLeftYAxisTextColor(String colorStr) {
        setLeftYAxisTextColor(Color.parseColor(colorStr));
        return this;
    }

    /**
     * 设置左边y轴文本颜色
     */
    public MultiPolylineView setLeftYAxisTextColor(ColorStateList colorStateList) {
        setLeftYAxisTextColor(colorStateList.getColorForState(getDrawableState(), 0));
        return this;
    }

    /**
     * 设置左边y轴文本颜色
     */
    public MultiPolylineView setLeftYAxisTextColor(int colorStr) {
        leftTextPaint.setColor(colorStr);
        return this;
    }

    /**
     * 设置右边y轴文本颜色
     */
    public MultiPolylineView setRightYAxisTextColor(String colorStr) {
        setRightYAxisTextColor(Color.parseColor(colorStr));
        return this;
    }

    /**
     * 设置右边y轴文本颜色
     */
    public MultiPolylineView setRightYAxisTextColor(ColorStateList colorStateList) {
        setRightYAxisTextColor(colorStateList.getColorForState(getDrawableState(), 0));
        return this;
    }

    /**
     * 设置右边y轴文本颜色
     */
    public MultiPolylineView setRightYAxisTextColor(int colorStr) {
        rightTextPaint.setColor(colorStr);
        return this;
    }

    /**
     * 设置坐标轴的颜色
     */
    public MultiPolylineView setAxisColor(String colorStr) {
        return setAxisColor(Color.parseColor(colorStr));
    }

    /**
     * 设置坐标轴的颜色
     */
    public MultiPolylineView setAxisColor(ColorStateList colorStateList) {
        return setAxisColor(colorStateList.getColorForState(getDrawableState(), 0));
    }

    /**
     * 设置坐标轴的颜色
     */
    public MultiPolylineView setAxisColor(int color) {
        axisPaint.setColor(color);
        return this;
    }

    /**
     * 设置x轴显示的个数
     */
    public MultiPolylineView setXAxisShowNum(int num) {
        if (num <= 0)
            throw new IllegalArgumentException("Illegal num: " + num);
        if (num == 1)
            throw new IllegalArgumentException(" num:" + num + " must more than 1");

        xTextShowNum = num;
        return this;
    }

    /**
     * 设置y轴显示的个数
     */
    public MultiPolylineView setYAxisShowNum(int num) {
        if (num <= 0)
            throw new IllegalArgumentException("Illegal num: " + num);
        yTextShowNum = num;
        return this;
    }

    public MultiPolylineView setTypeColors(int[] color) {
        if (color != null && color.length == 0)
            throw new IllegalArgumentException("Illegal array's size: " + color.length);
        mTypeColrs = color;
        return this;
    }

    /**
     * 设置y轴数据
     */
    public MultiPolylineView setTypeValue(List<Type> yTypes) {
        this.yTypes = yTypes;
        return this;
    }

    /**
     * 设置x轴数据
     */
    public MultiPolylineView setXValue(String[] xTexts) {
        this.xTexts = xTexts;
        return this;
    }

    /**
     * 绘制
     */
    public void paint() {
        if (yTypes.size() > 0 && xTexts.length > 0) {
            calcAxisText();
            requestLayout();
            invalidate();
        }
    }

    /**
     * 计算轴上文本的宽高度
     */
    private void calcAxisText() {

        for (String text : xTexts)
            calcXTextRect(text);

        for (Type type : yTypes) {
            extractMaxMin(type);
            calcLeftYRect(type.maxStr, type.minStr);
            String name = type.getTypeName();
            if (name != null)
                calcRightYRect(name);
        }
    }

    /**
     * 获取type的最大值和最小值
     */
    private void extractMaxMin(Type type) {

        float[] datas = type.getData();
        if (datas != null && datas.length > 0) {
            type.min = datas[0] = type.max = datas[0];
            for (int j = 1; j < datas.length; j++) {
                if (datas[j] > type.max)
                    type.max = datas[j];
                else if (datas[j] < type.min)
                    type.min = datas[j];
            }
        }

        //保持数据格式一致,10==10.f  消除小数点
        int maxInt = ((int) type.max);
        int minInt = ((int) type.min);

        if (maxInt == type.max && minInt == type.min) {
            type.maxStr = String.valueOf(((int) type.max));
            type.minStr = String.valueOf(((int) type.min));
        } else {
            type.maxStr = String.valueOf(type.max);
            type.minStr = String.valueOf(type.min);
        }
    }

    /**
     * 计算左边文本的最大宽高度
     */
    private void calcLeftYRect(String max, String min) {
        leftTextPaint.getTextBounds(max, 0, max.length(), calcRect);
        leftYMaxTextWidth = swapMax(calcRect.width(), leftYMaxTextWidth);
        leftYMaxTextHeight = swapMax(calcRect.height(), leftYMaxTextHeight);
        leftTextPaint.getTextBounds(min, 0, min.length(), calcRect);
        leftYMaxTextWidth = swapMax(calcRect.width(), leftYMaxTextWidth);
        leftYMaxTextHeight = swapMax(calcRect.height(), leftYMaxTextHeight);
    }

    /**
     * 计算右边y轴的文本宽高
     */
    private void calcRightYRect(String typeName) {
        rightTextPaint.getTextBounds(typeName, 0, typeName.length(), calcRect);
        rightYMaxTextHeight = swapMax(calcRect.height(), rightYMaxTextHeight);
        rightYMaxTextWidth = swapMax(calcRect.width(), rightYMaxTextWidth);
    }

    /**
     * 计算x轴文本的宽高
     */
    private void calcXTextRect(String text) {
        xTextPaint.getTextBounds(text, 0, text.length(), calcRect);
        xTextMaxHeight = swapMax(calcRect.height(), xTextMaxHeight);
        xTextMaxWidth = swapMax(calcRect.width(), xTextMaxWidth);
    }

    private int swapMax(int num1, int num2) {
        return num1 > num2 ? num1 : num2;
    }

    /**
     * 无数据不需要绘制
     */
    private boolean rest() {
        return xTexts == null || xTexts.length == 0 || yTypes == null || yTypes.size() == 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if (rest())
            return;

        height = getHeight();
        width = getWidth();

        //x轴和文本的边距
        xAxisMargin = X_TEXT_AXIS_DEFAULT_MARGIN + BOTTOM_X_AXIS_MARGIN + xTextMaxHeight;
        //左边y轴和文本的边距
        leftYAxisMargin = LEFT_Y_TEXT_AXIS_DEFAULT_MARGIN + LEFT_Y_AXIS_MARGIN + leftYMaxTextWidth;
        //右边y轴和文本的边距
        rightYAxisMargin = RIGHT_Y_TEXT_AXIS_DEFAULT_MARGIN + RIGHT_Y_AXIS_MARGIN + rightYMaxTextWidth;

        //左上角圆点
        topLeftOriginX = leftYAxisMargin;
        topLeftOriginY = TOP_X_AXIS_MARGIN;
        //左下角圆点
        bottomLeftOriginX = topLeftOriginX;
        bottomLeftOriginY = height - xAxisMargin;
        //右上角圆点
        topRightOriginX = width - rightYAxisMargin;
        topRightOriginY = topLeftOriginY;
        //右下角圆点
        bottomRightOriginX = topRightOriginX;
        bottomRightOriginY = bottomLeftOriginY;

        //x轴移动的起始坐标，没有移动前为圆点坐标
        xTextMoveStartX = bottomLeftOriginX;
        //y轴移动的起始坐标，没有移动前为左上角的边距
        yTypeMoveStartY = TOP_X_AXIS_MARGIN;

        //x轴边距
        int xMargin = leftYAxisMargin + rightYAxisMargin;
        //x轴的长度
        xAxisLen = width - xMargin;
        //计算x轴显示个数
        if (xTexts.length < xTextShowNum)
            xTextShowNum = xTexts.length;
        //x轴文本间的边距
        if (xTextShowNum == 1)
            xTextMargin = 0;
        else
            xTextMargin = (xAxisLen - (xTextShowNum * xTextMaxWidth)) / (xTextShowNum - 1);
        //x轴文本的最大长度
        xMaxTextLen = (xTexts.length) * xTextMaxWidth + (xTexts.length - 1) * xTextMargin;

        //右边y轴边距
        int yMargin = TOP_X_AXIS_MARGIN + xAxisMargin;
        yAxisLen = height - yMargin;
        //计算右边y轴显示个数
        if (yTypes.size() < yTextShowNum)
            yTextShowNum = yTypes.size();

        //右边y轴文本间的边距
        yTypeAxisHeight = (yAxisLen - (yTextShowNum * yTypeMargin)) / yTextShowNum;
        //y轴类型的最大长度
        yMaxTypeLen = yTypeAxisHeight * yTypes.size() + yTypeMargin * yTypes.size();

        //可滑动区域
        scrollLeftBorder = bottomLeftOriginX;
        scrollRightBorder = bottomLeftOriginX - (xMaxTextLen - xAxisLen);
        scrollTopBorder = topLeftOriginY;
        scrollBottomBirder = topLeftOriginY - (yMaxTypeLen - yAxisLen);

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rest())
            return;

        canvas.drawColor(mBackground);

        //重新开一个图层
        int layerOne = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        //绘制背景色和折线和点
        drawLinePointBackground(canvas);

        //切除中间区域的左边
        clipRectF.set(0, 0, bottomLeftOriginX, height);
        canvas.drawRect(clipRectF, clipPaint);
        //切除中间区域的右边
        clipRectF.set(bottomRightOriginX, 0, width, height);
        canvas.drawRect(clipRectF, clipPaint);
        //切除中间区域的上边
        clipRectF.set(topLeftOriginX, 0, topRightOriginX, topRightOriginY);
        canvas.drawRect(clipRectF, clipPaint);
        //切除中间区域的下边
        clipRectF.set(bottomLeftOriginX, bottomLeftOriginY, bottomRightOriginX, height);
        canvas.drawRect(clipRectF, clipPaint);
        //保存图层
        canvas.restoreToCount(layerOne);

        //重新开一个图层
        int layerTwo = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        //绘制x轴文本
        drawXText(canvas);

        //切除中间区域的左边
        clipRectF.set(0, 0, bottomLeftOriginX, height);
        canvas.drawRect(clipRectF, clipPaint);
        //切除中间区域的右边
        clipRectF.set(topRightOriginX, 0, width, height);
        canvas.drawRect(clipRectF, clipPaint);
        //保存图层
        canvas.restoreToCount(layerTwo);

        //重新开一个图层
        int layerThree = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        //绘制y轴文本
        drawYAxisText(canvas);

        //切除中间区域的上边
        clipRectF.set(0, 0, width, topRightOriginY);
        canvas.drawRect(clipRectF, clipPaint);
        //切除中间区域的下边
        clipRectF.set(0, bottomLeftOriginY, width, height);
        canvas.drawRect(clipRectF, clipPaint);

        //保存图层
        canvas.restoreToCount(layerThree);

        //画坐标轴
        drawAxis(canvas);
    }

    /**
     * 绘制y轴文本
     */
    private void drawYAxisText(Canvas canvas) {
        for (int i = 0; i < yTypes.size(); i++) {
            Type type = yTypes.get(i);
            if (type != null) {
                //绘制左边文本
                drawYAxisLeftText(canvas, type, i);
                //绘制右边文本
                drawYAxisRightText(canvas, type, i);
            }
        }
    }

    /**
     * 绘制右边文本
     */
    private void drawYAxisRightText(Canvas canvas, Type type, int index) {

        String text = type.getTypeName();

        if (!TextUtils.isEmpty(text)) {

            float startX = topRightOriginX + RIGHT_Y_TEXT_AXIS_DEFAULT_MARGIN;
            //文本居中，字体居中
            float startStaticY = yTypeMoveStartY + rightYMaxTextHeight / 2 + yTypeAxisHeight / 2;

            float startY = startStaticY + (index * yTypeMargin) + (index * yTypeAxisHeight);

            //如果文本在中间的区域范围内，则画出来，超过边界的不画
            boolean canDraw = startY > topRightOriginY && startY < bottomRightOriginY + rightYMaxTextHeight;
            if (canDraw)
                canvas.drawText(text, startX, startY, rightTextPaint);
        }
    }

    /**
     * 绘制左边文本
     */
    private void drawYAxisLeftText(Canvas canvas, Type type, int index) {

        leftTextPaint.getTextBounds(type.maxStr, 0, type.maxStr.length(), textRect);
        //起点减去y轴边距减去字体宽度
        float startMaxX = topLeftOriginX - LEFT_Y_TEXT_AXIS_DEFAULT_MARGIN - textRect.width();
        //轴边距+文本间距+文本区域+字体高度
        float startMaxY = yTypeMoveStartY + index * yTypeMargin
                + index * yTypeAxisHeight + leftYMaxTextHeight;

        //如果文本在中间的区域范围内，则画出来，超过边界的不画
        boolean canDrawMax = startMaxY > topLeftOriginY && startMaxY < bottomLeftOriginY + leftYMaxTextHeight;
        if (canDrawMax)
            canvas.drawText(type.maxStr, startMaxX, startMaxY, leftTextPaint);

        leftTextPaint.getTextBounds(type.minStr, 0, type.minStr.length(), textRect);
        //起点减去y轴边距减去字体宽度
        float startMinX = topLeftOriginX - LEFT_Y_TEXT_AXIS_DEFAULT_MARGIN - textRect.width();
        //轴边距+文本间距+文本区域+字体高度
        float startMinY = yTypeMoveStartY + ((index + 1) * yTypeAxisHeight) + index * yTypeMargin;

        //如果文本在中间的区域范围内，则画出来，超过边界的不画
        boolean canDrawMin = startMinY > topLeftOriginY && startMinY < bottomLeftOriginY + leftYMaxTextHeight;
        if (canDrawMin)
            canvas.drawText(type.minStr, startMinX, startMinY, leftTextPaint);
    }

    /**
     * 绘制背景色点和折线
     */
    private void drawLinePointBackground(Canvas canvas) {

        for (int i = 0; i < yTypes.size(); i++) {
            Type type = yTypes.get(i);
            if (type != null) {
                //绘制背景色
                drawTypeBackground(canvas, i);
                //绘制点和折线
                drawTypePointLine(canvas, type, i);
            }
        }
    }

    /**
     * 绘制折线和点
     */
    private void drawTypePointLine(Canvas canvas, Type type, int index) {
        //加上图层，不然线的颜色会覆盖点的颜色
        int layer = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        //数值区间,max+1:因为最大值时圆点会画出界，不美观
        float between = type.max - type.min;

        if (between == 0)//折线数据都一致时以最低数值画起
            between = 1;

        boolean lineDraw = false;

        //每个数值差所占的像素大小
        //因为在最低点时，如果不减去直径的话，最低会露出边界，不美观
        int narrow = mPointRadius * 2;
        float perYPx = (yTypeAxisHeight - narrow) / between;
        float[] data = type.getData();
        if (data != null) {
            Path path = new Path();
            for (int j = 0; j < data.length; j++) {

                float num = type.max - data[j];

                //数值差
                //圆点和曲线的起点 圆点x轴=边距+半个文本大小
                float startX = xTextMoveStartX + xTextMaxWidth / 2 + j * xTextMaxWidth + j * xTextMargin;
                //轴边距+数值差像素+类型边距+类型区域边距

                //最大值时会露出边界不美观,所以加上半径
                int tenesmus = mPointRadius;
                float startY = (yTypeMoveStartY + num * perYPx + tenesmus)
                        + index * yTypeAxisHeight + index * yTypeMargin;

                //画圆点
                //圆点还没滑出上下边界和左右边界
                boolean xCan = startX + mPointRadius > topLeftOriginX && startX - mPointRadius < topRightOriginX;
                boolean yCan = startY + mPointRadius > topLeftOriginY && startY - mPointRadius < bottomLeftOriginY;
                if (xCan && yCan) {
                    if (data[j] == type.max) {//标记最高值和最低值
                        pointPaint.setColor(maxPointColor);
                    } else if (data[j] == type.min) {
                        pointPaint.setColor(minPointColor);
                    } else {
                        pointPaint.setColor(pointColor);
                    }
                    canvas.drawCircle(startX, startY, mPointRadius, pointPaint);
                }

                //只要还有折线显示在区域内,有则画出来，没有则不画
                lineDraw = lineDraw || startY >= topLeftOriginY && startY <= bottomLeftOriginY;

                //连接折线
                //折线在显示的范围内则画出来
                //默认显示左边边界前一个点到右边边界后一个点
                //如果是第一个点，或者点还在左边边界的前一个点的范围内，避免滑动时折线短
                boolean first = startX >= topLeftOriginX - xTextMaxWidth - xTextMargin
                        && startX <= topLeftOriginX + xTextMaxWidth / 2;

                if (first && path.isEmpty()) path.moveTo(startX, startY);
                else {
                    boolean line = startX > topLeftOriginX && startX <= topRightOriginX + xTextMaxWidth + xTextMargin;
                    if (line) {
                        path.lineTo(startX, startY);
                    }
                }
            }

            //画折线图
            if (lineDraw)
                canvas.drawPath(path, linePaint);
        }
        canvas.restoreToCount(layer);
    }

    /**
     * 绘制背景色
     */
    private void drawTypeBackground(Canvas canvas, int index) {

        float top = yTypeMoveStartY;
        float left = topLeftOriginX;
        float right = topRightOriginX;
        float bottom = top + yTypeAxisHeight;

        //画类型的背景色
        typeBackgroundPaint.setColor(mTypeColrs[index % mTypeColrs.length]);

        //区域随着类型的递增加上类型区域大小
        float newTop = top + (index * yTypeMargin) + (index * yTypeAxisHeight);
        float newBottom = bottom + (index * yTypeAxisHeight) + (index * yTypeMargin);

        //画区域背景色
        RectF rect = new RectF(left, newTop, right, newBottom);

        boolean able = newTop >= topLeftOriginY && newTop <= bottomLeftOriginY
                || newBottom >= topLeftOriginY && newBottom < bottomLeftOriginY;

        //如果文本在中间的区域范围内，则画出来，超过边界的不画
        if (able)
            canvas.drawRect(rect, typeBackgroundPaint);
    }

    /**
     * 画x轴文本
     */
    private void drawXText(Canvas canvas) {

        //文本的纵坐标
        float startY = bottomLeftOriginY + RIGHT_Y_TEXT_AXIS_DEFAULT_MARGIN + xTextMaxHeight;

        for (int i = 0; i < xTexts.length; i++) {

            xTextPaint.getTextBounds(xTexts[i], 0, xTexts[i].length(), textRect);
            int centerText = 0;
            if (textRect.width() < xTextMaxWidth) {//当x轴文本不一样长度时，以最长文本居中
                centerText = (xTextMaxWidth - textRect.width()) / 2;
            }

            //起点+文本边距+文本宽度
            float startX = xTextMoveStartX + (i * xTextMargin) + (i * xTextMaxWidth) + centerText;

            //超出范围的文本不进行绘制
            boolean beyond = startX + xTextMaxWidth <= bottomLeftOriginX || startX > bottomRightOriginX;
            if (beyond)
                continue;

            String text = xTexts[i];

            canvas.drawText(text, startX, startY, xTextPaint);
        }
    }

    /**
     * 画坐标
     */
    private void drawAxis(Canvas canvas) {
        //左y轴
        canvas.drawLine(topLeftOriginX, topLeftOriginY, bottomLeftOriginX, bottomLeftOriginY, axisPaint);
        //x轴
        canvas.drawLine(bottomLeftOriginX, bottomLeftOriginY, bottomRightOriginX, bottomRightOriginY, axisPaint);
        //右y轴
        canvas.drawLine(topRightOriginX, topRightOriginY, bottomRightOriginX, bottomRightOriginY, axisPaint);
    }

    //当前点击的x坐标
    private float clickDownX;
    //当前点击的y坐标
    private float clickDownY;
    //每次操作x轴的偏移量
    private float xMoveDistance;
    //每次操作y轴的偏移量
    private float yMoveDistance;
    //x轴的移动后的起始坐标
    private float xTextMoveStartX;
    //y轴的移动后的起始坐标
    private float yTypeMoveStartY;
    //速度检测器
    private VelocityTracker mVelocityTracker;
    //滑动动画
    private ValueAnimator animator;
    //滑动的左边界
    float scrollLeftBorder;
    //滑动的右边界
    float scrollRightBorder;
    //滑动的上边界
    float scrollTopBorder;
    //滑动的下边界
    float scrollBottomBirder;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickDownX = event.getX();
                clickDownY = event.getY();
                recycleAnimator();
                break;
            case MotionEvent.ACTION_MOVE:
                //每一个滑动的滑动距离
                float moveX = event.getX();
                xMoveDistance = moveX - clickDownX;
                clickDownX = moveX;

                float moveY = event.getY();
                yMoveDistance = moveY - clickDownY;
                clickDownY = moveY;

                boolean dir = Math.abs(xMoveDistance) > Math.abs(yMoveDistance);
                float distance = dir ? xMoveDistance : yMoveDistance;
                move(distance, dir);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(250);

                float xVelocity = mVelocityTracker.getXVelocity();
                float yVelocity = mVelocityTracker.getYVelocity();
                final boolean direction = Math.abs(xVelocity) > Math.abs(yVelocity);
                float goal = direction ? xVelocity : yVelocity;

                animator = ValueAnimator.ofFloat(0, goal);
                animator.setDuration(1000);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    float previous = 0;
                    float difference = 0;

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        float value = (float) animation.getAnimatedValue();
                        difference = value - previous;
                        previous = value;
                        move(difference, direction);
                    }
                });
                animator.start();
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 滑动
     *
     * @param difference 滑动距离
     * @param direction  滑动方向
     */
    private void move(float difference, boolean direction) {
        if (direction) {//正在横向滑动，禁止纵向滑动
            //判断x轴的可滑动区域
            boolean canXMove = xTextMoveStartX + difference <= scrollLeftBorder
                    && xTextMoveStartX + difference >= scrollRightBorder;
            if (canXMove) {
                //x轴移动后的起始坐标
                xTextMoveStartX += difference;
                invalidate();
            } else {//起始点+移动距离超过边界，但是起始点小于边界，防止移动距离过大卡顿
                if (difference > 0 && xTextMoveStartX < scrollLeftBorder) {//起始点还未超过左边界
                    xTextMoveStartX = scrollLeftBorder;
                    invalidate();
                    //起始点还未超过右边界
                } else if (difference < 0 && xTextMoveStartX > scrollRightBorder) {
                    xTextMoveStartX = scrollRightBorder;
                    invalidate();
                } else {
                    if (animator != null && animator.isRunning())
                        animator.cancel();
                }
            }
        } else {//正在纵向滑动，禁止横向滑动
            //判断y轴的可滑动区域
            boolean canYMove = yTypeMoveStartY + difference <= scrollTopBorder &&
                    yTypeMoveStartY + difference >= scrollBottomBirder;
            if (canYMove) {
                //y轴移动后的起始坐标
                yTypeMoveStartY += difference;
                invalidate();
            } else {//起始点+移动距离超过边界，但是起始点小于边界，防止移动距离过大卡顿
                if (difference > 0 && yTypeMoveStartY < scrollTopBorder) {//起始点还未超过上边界
                    yTypeMoveStartY = scrollTopBorder;
                    invalidate();
                    //起始点还未超过下边界
                } else if (difference < 0 && yTypeMoveStartY > scrollBottomBirder) {
                    yTypeMoveStartY = scrollBottomBirder;
                    invalidate();
                } else {
                    if (animator != null && animator.isRunning())
                        animator.cancel();
                }
            }
        }
    }

    /**
     * 停止动画
     */

    private void recycleAnimator() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    /**
     * 获取速度跟踪器
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = mVelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * dp转化成为px
     */
    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * sp转化为px
     */
    private int spToPx(int sp) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * sp + 0.5f * (sp >= 0 ? 1 : -1));
    }
}