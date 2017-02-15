package com.pingan.pinganwifiboss.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.pingan.pinganwifiboss.R;
import com.pingan.pinganwifiboss.bean.ConnectEarningFormattInfo;
import com.pingan.pinganwifiboss.net.response.ConnectEarningResponse;
import com.pingan.pinganwifiboss.net.response.WalletEarningResponse;
import com.pingan.pinganwifiboss.view.widget.ParentsChartView;

import java.util.ArrayList;
import java.util.Collections;

import cn.core.util.DateUtil;
import cn.core.util.DimensionUtils;
import common.achartengine.chart.PointStyle;
import common.achartengine.model.XYMultipleSeriesDataset;
import common.achartengine.model.XYSeries;
import common.achartengine.renderer.XYMultipleSeriesRenderer;
import common.achartengine.renderer.XYSeriesRenderer;


public class ChartUtil {

    private static final String TAG = ChartUtil.class.getSimpleName();


    private static final String DOUBLE_DIGIT = "%.2f";
    private static final String THREE_DIGIT = "%.3f";
    public static final int Y_AXIS_COUNT = 6;
    public static final int FUND_Y_AXIS_COUNT = 3;
    public static final int X_AXIS_COUNT = 7;
    public static final double X_MAX_AXIS = 6.5;

    public static XYMultipleSeriesRenderer getChartRenderer(Context context) {
        XYMultipleSeriesRenderer renderer = getXyMultipleSeriesRenderer(context);
        XYSeriesRenderer xySeriesRenderer = getXYSeriesRenderer(context);
        renderer.addSeriesRenderer(xySeriesRenderer);
        return renderer;
    }

    private static XYSeriesRenderer getXYSeriesRenderer(Context context) {
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setLineWidth(DimensionUtils.dip2px(context, 2.6f));
        r.setColor(context.getResources().getColor(R.color.syb_base_theme_color_orange));
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesTextSize(DimensionUtils.dip2px(context, 14f));
        r.setDisplayChartValues(true);
        return r;
    }


    private static XYMultipleSeriesRenderer getXyMultipleSeriesRenderer(Context context) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(30);
        renderer.setPanEnabled(false); // 设置沿X或Y轴是否可以拖动
        renderer.setLegendTextSize(15);
        renderer.setPanLimits(new double[]{0, 7, 0, 0}); // 限制xy轴的长度
        renderer.setXLabels(X_AXIS_COUNT); // 当设置为10时，x轴单位为1
        renderer.setYLabels(Y_AXIS_COUNT);
        renderer.setXAxisMax(X_MAX_AXIS);
        renderer.setXRoundedLabels(false);
        renderer.setLabelsTextSize(DimensionUtils.dip2px(context, 10f));
        renderer.setMarginsColor(Color.WHITE);
        renderer.setYAxisMin(0);
        renderer.setSelectableBuffer(DimensionUtils.dip2px(context, 16f));
        renderer.setClickEnabled(true);
        renderer.setLabelsColor(Color.GRAY);
        renderer.setShowGridX(true);
        renderer.setShowGridY(true);
        renderer.setZoomEnabled(false);
        renderer.setZoomEnabled(false, false);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setPointSize(0f);
        return renderer;
    }

    public static XYMultipleSeriesDataset initDataset(int viewPagerType, ArrayList<ConnectEarningFormattInfo> kLineLists, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        if (dataset.getSeriesCount() > 0) {
            dataset.removeSeries(0);
        }
        XYSeries series = new XYSeries("syb_k_line");
        final int size = kLineLists.size();
        renderer.clearYTextLabels();
        renderer.clearXTextLabels();
        ArrayList<Double> values = setYLabelUnit(kLineLists, renderer, viewPagerType);
        for (int k = 0; k < size; k++) {
            if (null != kLineLists.get(k).earningData && !"".equals(kLineLists.get(k).earningData)) {
                if (viewPagerType == ParentsChartView.VIEW_TYPE_CONNECT) {//连接收益保留两位
                    series.add(k, formattDigit(values.get(k), 100.0));
                } else {//保留三位
                    series.add(k, formattDigit(values.get(k), 1000.0));
                }
                series.addTime(k, kLineLists.get(k).time);
            } else {
                series.add(k, 0);
            }
        }
        dataset.addSeries(0, series);
        return dataset;
    }

    /**
     * 初始化xy轴线条的坐标
     *
     * @param kLineLists
     * @param renderer
     * @return
     */
    private static ArrayList<Double> setYLabelUnit(ArrayList<ConnectEarningFormattInfo> kLineLists, XYMultipleSeriesRenderer renderer, int viewPagerType) {
        ArrayList<Double> values = ConnectEarningFormattInfo.getYAxiseValueList(kLineLists, viewPagerType);
        renderer.setYLabels(values.size());// 设置y坐标显示原本单位的个数
        if (ConnectEarningFormattInfo.getDeltYAxiseValue(values) == 0) {
            setYLabelUnitAbnormality(renderer, values);
        } else {
            if (viewPagerType == ParentsChartView.VIEW_TYPE_CONNECT) {//连接收益绘制方法
                setConnectYLabelUnitNormal(renderer, values, viewPagerType);
            } else {//大华基金收益率绘制方法
                setFundYLabelUnitNormal(renderer, values, viewPagerType);
            }
        }
        return values;
    }


    /**
     * 数据格式正常的情况下处理大华基金收益K线数据
     *
     * @param renderer
     * @param values
     */
    private static void setFundYLabelUnitNormal(XYMultipleSeriesRenderer renderer, ArrayList<Double> values, int viewPagerType) {
        double offSet = formattDigit(Collections.max(values), 1000.0) - formattDigit(Collections.min(values), 1000.0);
        double unit = formattDigit(offSet / FUND_Y_AXIS_COUNT, 1000.0);
        double marginOffset = unit / 5;
        double yMax = formattDigit(Collections.max(values), 1000.0);
        for (int i = 0; i < Y_AXIS_COUNT - 1; i++) {
            //如果Y轴上的数据为负数，就走原来的策略
            if (formattDigit(yMax - unit * i, 1000.0) < 0) {
                setConnectYLabelUnitNormal(renderer, values, viewPagerType);
                return;
            }
        }
        renderer.setYAxisMax(yMax + unit);// 设置y轴的最大值
        renderer.setYAxisMin(formattDigit(yMax - unit * (Y_AXIS_COUNT - 1) - unit / 4, 1000.0));// 设置y轴的起始值
        double value;
        for (int i = 0; i < Y_AXIS_COUNT; i++) {
            if (i == Y_AXIS_COUNT - 1) {
                value = yMax + marginOffset;
            } else if (i == 0) {
                value = yMax - unit * (Y_AXIS_COUNT - i - 1) - marginOffset;
                if (value <= 0) {
                    value = formattDigit(value, 1000.0);
                    renderer.addYTextLabel(value, /*formatDouble(THREE_DIGIT, value)*/"0");
                    continue;
                }
            } else {
                value = yMax - unit * (Y_AXIS_COUNT - i - 1) - marginOffset;
            }
            value = formattDigit(value, 1000.0);
            renderer.addYTextLabel(value, formatDouble(THREE_DIGIT, value));
        }
    }


    /**
     * 数据格式正常的情况下处理连接收益K线数据
     *
     * @param renderer
     * @param values
     */
    private static void setConnectYLabelUnitNormal(XYMultipleSeriesRenderer renderer, ArrayList<Double> values, int viewPagerType) {
        double offSet = formattDigit(Collections.max(values), 1000.0) - formattDigit(Collections.min(values), 1000.0);
        double unit = formattDigit(offSet / (Y_AXIS_COUNT - 1), 1000.0);
        double yMin = formattDigit(Collections.min(values), 1000.0);
        double yMax = formattDigit(Collections.max(values) + unit, 1000.0);
        renderer.setYAxisMin(yMin - (unit / 8));// 设置y轴的起始值
        renderer.setYAxisMax(yMax);// 设置y轴的最大值
        double value;
        for (int i = 0; i < Y_AXIS_COUNT; i++) {
            if (i != 0) {
                value = yMin + unit * i + (unit / 5);
            } else {
                value = yMin + unit * i;
            }
            if (viewPagerType == ParentsChartView.VIEW_TYPE_WALLET) {
                value = formattDigit(value, 1000.0);
            } else {
                value = formattDigit(value, 100.0);
            }
            renderer.addYTextLabel(value, formatDouble(DOUBLE_DIGIT, value));
        }
    }

    public static double formattDigit(double value, double unit) {
        return Math.round(value * (int) unit) / unit;
    }

    /**
     * 如果只是用于程序中的然后输出，那么这个方法还是挺方便的。
     * 应该是这样使用：System.out.println(String.format("%.2f", d));
     *
     * @param d
     * @return
     */
    public static String formatDouble(String digit, double d) {
        return String.format(digit, d);
    }

    /**
     * 数据格式不正常(全部数据相同且全部为0／非全部为0)的情况下处理K线数据
     *
     * @param renderer
     * @param values
     */
    private static void setYLabelUnitAbnormality(XYMultipleSeriesRenderer renderer, ArrayList<Double> values) {
        double maxValue = Collections.max(values);
        if (Double.doubleToRawLongBits(maxValue ) == 0) {//最大值、最小值相等，而且都为0
            for (int i = 0; i < Y_AXIS_COUNT; i++) {
                if (i == 0) {
                    renderer.setYAxisMin(0);// 设置y轴的起始值
                }
                int value = i * 20;
                renderer.addYTextLabel(value, String.valueOf(value));
            }
            renderer.setYAxisMax(100);
        } else {//最大值，、最小值相等，为非0
            renderer.addYTextLabel(maxValue, String.valueOf(maxValue));
            double offset = maxValue / Y_AXIS_COUNT;
            double currentValue;
            for (int i = 0; i < Y_AXIS_COUNT; i++) {
                if (i > 3) {
                    currentValue = maxValue + offset * (i - 3);
                    renderer.addYTextLabel(maxValue + offset * (i - 3), String.valueOf(currentValue));
                    if (i == Y_AXIS_COUNT - 1) {
                        renderer.setYAxisMax(currentValue);// 设置y轴的最大值
                    }
                } else {
                    currentValue = maxValue + offset * (3 - i);
                    renderer.addYTextLabel(currentValue, String.valueOf(currentValue));
                    if (i == 0) {
                        renderer.setYAxisMin(currentValue);// 设置y轴的起始值
                    }
                }
            }
        }
    }

    /**
     * 如果服务器返回连接收益的列表为空，为了显示表格，client生成数据
     *
     * @param response
     */
    public static void mockDefaultConnectDate(ConnectEarningResponse response) {
        response.data.link7DayRate = new ArrayList<ConnectEarningResponse.Link7DayRate>(7);
        ConnectEarningResponse.Link7DayRate dayRate;
        for (int i = 0; i < ConnectEarningResponse.LINK7DAYRATE_COUNT; i++) {
            dayRate = response.new Link7DayRate();
            dayRate.flowAmount = String.valueOf(0.0);
            String data = DateUtil.beforNowDay(i);
            int len = data.length();
            if (len > 5) {
                dayRate.flowDate = data.substring(len - 5, len);
            } else {
                dayRate.flowDate = data;
            }
            response.data.link7DayRate.add(dayRate);
        }
    }

    /**
     * 如果服务器返回钱包收益的列表为空，为了显示表格，client生成数据
     *
     * @param response
     */
    public static void mockDefaultWalletDate(WalletEarningResponse response) {
        response.data.last7YieldsRate = new ArrayList<WalletEarningResponse.Last7YieldsRate>();
        WalletEarningResponse.Last7YieldsRate dayRate;
        for (int i = 0; i < WalletEarningResponse.LINK7DAYRATE_COUNT; i++) {
            dayRate = response.new Last7YieldsRate();
            dayRate.yieldRate = String.valueOf(0.0);
            String data = DateUtil.beforNowDay(i);
            int len = data.length();
            if (len > 5) {
                dayRate.date = data.substring(len - 5, len);
            } else {
                dayRate.date = data;
            }
            response.data.last7YieldsRate.add(dayRate);
        }
    }


}
