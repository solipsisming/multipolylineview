package solipsisming.multipolylineview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import solipsisming.multipolylineview.MultiPolylineView;
import solipsisming.multipolylineview.Type;


public class MultiPolylineViewActivity extends Activity {

    private MultiPolylineView multiPolylineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.multipolylineview_activity);

        int SIZE = 100;

        String[] xText = new String[SIZE];

        for (int i = 0; i < SIZE; i++) {
            xText[i] = String.valueOf(i);
        }

        String[] typeName = {"大气温度", "二氧化氮", "土壤温度", "大气湿度", "二氧化碳"};
        Random random = new Random();
        List<Type> types = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Type type = new Type();
            int N = random.nextInt(100) + 1;
            float[] data = new float[N];
            for (int j = 0; j < N; j++) {
                if (i % 2 == 0)
                    data[j] = random.nextInt(1000);
                else
                    data[j] = random.nextFloat() * 100;
            }
            type.setData(data);
            type.setTypeName(typeName[random.nextInt(typeName.length)]);
            types.add(type);
        }

        multiPolylineView = findViewById(R.id.multiPolylineView);
        multiPolylineView.setTypeValue(types)
                .setXValue(xText)//设置x轴文本
//                .setXAxisShowNum(5)//设置x轴显示的个数
//                .setYAxisShowNum(5)//设置y轴显示的个数
//                .setViewBackground(Color.GRAY)//设置折线图背景色
//                .setLineColor(Color.BLACK)//设置线的颜色
//                .setPointColor(Color.BLACK)//设置点的颜色
//                .setAxisColor(Color.BLACK)//设置坐标轴的颜色
//                .setLeftYAxisTextColor(Color.CYAN)//设置左边坐标轴文本的颜色
//                .setRightYAxisTextColor(Color.MAGENTA)//设置右边坐标轴文本的颜色
//                .setXAxisTextColor(Color.LTGRAY)//设置x轴文本颜色
//                .setMaxPointColor(Color.RED)//设置最大值的颜色
//                .setMinPointColor(Color.GREEN)//设置最小值颜色
//                .setTypeColors(new int[]{Color.RED, Color.CYAN})//设置类型的背景色
//                .setYTypeMargin(20)//设置类型之间的边距
//                .setXAxisTextSize(30)//设置x轴文本的字体大小
//                .setLeftYAxisTextSize(26)//设置左边y轴文本字体大小
//                .setRightYAxisTextSize(28)//设置右边y轴文本字体大小
//                .setPointRadiusSize(3)//设置圆点的半径
//                .setLineWidth(5)//设置线的宽度
//                .setAxisWidth(5)//设置坐标轴的宽度
                .paint();//绘制
    }
}
