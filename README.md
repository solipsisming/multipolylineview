# multipolylineview

 <cn.com.ming.mutilpolylineview.MutilPolylineView
    android:id="@+id/mutilPolylineView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:axisColor="#FF0000"
    app:axisWidth="2"
    app:leftYAxisTextColor="#00FF00"
    app:leftYAxisTextSize="20sp"
    app:lineColor="#0000FF"
    app:lineWidth="3"
    app:maxPointColor="#F0000F"
    app:minPointColor="#000FF0"
    app:pointColor="#F0F0F0"
    app:pointRadius="2"
    app:rightYAxisTextColor="#0FF000"
    app:rightYAxisTextSize="20sp"
    app:viewBackground="#0F000F"
    app:xAxisShowNum="5"
    app:xAxisTextColor="#00F000"
    app:xAxisTextSize="20sp"
    app:yAxisShowNum="5"
    app:yAxisTypeMargin="20dp" />
    
    mutilPolylineView = findViewById(R.id.mutilPolylineView);
    mutilPolylineView.setTypeValue(types)
            .setXValue(xText)//设置x轴文本
            .setXAxisShowNum(5)//设置x轴显示的个数
            .setYAxisShowNum(5)//设置y轴显示的个数
            .setViewBackground(Color.GRAY)//设置折线图背景色
            .setLineColor(Color.BLACK)//设置线的颜色
            .setPointColor(Color.BLACK)//设置点的颜色
            .setAxisColor(Color.BLACK)//设置坐标轴的颜色
            .setLeftYAxisTextColor(Color.CYAN)//设置左边坐标轴文本的颜色
            .setRightYAxisTextColor(Color.MAGENTA)//设置右边坐标轴文本的颜色
            .setXAxisTextColor(Color.LTGRAY)//设置x轴文本颜色
            .setMaxPointColor(Color.RED)//设置最大值的颜色
            .setMinPointColor(Color.GREEN)//设置最小值颜色
            .setTypeColors(new int[]{Color.RED, Color.CYAN})//设置类型的背景色
            .setYTypeMargin(20)//设置类型之间的边距
            .setXAxisTextSize(30)//设置x轴文本的字体大小
            .setLeftYAxisTextSize(26)//设置左边y轴文本字体大小
            .setRightYAxisTextSize(28)//设置右边y轴文本字体大小
            .setPointRadiusSize(3)//设置圆点的半径
            .setLineWidth(5)//设置线的宽度
            .setAxisWidth(5)//设置坐标轴的宽度
            .paint();//绘制
