package com.pingan.pinganwifiboss.util;

import com.pingan.pinganwifiboss.SYBConfig;

/**
 * @author lidajun
 * @email solidajun@gmail.com
 * @date 15/10/31 11:07.
 * @desc: 收支类型
 */
public enum FlowType {
    /**
     * 时长收益
     */
    S0001,
    /**
     * 注册奖励
     */
    S0002,
    /**
     * 提现失败
     */
    S0003,
    /**
     * 活动奖励
     */
    S0004,
    /**
     * 其他收入
     */
    S0005, S0006, S0007, S0008, S0009, S0010,
    /**
     * 提现
     */
    G0001,
    /**
     * 其他支出
     */
    G0002, G0003, G0004, G0005,
    NOVALUE;

    public static FlowType toType(String type) {
        try {
            return valueOf(type);
        } catch (Exception ex) {
            return NOVALUE;
        }
    }

    public static String getTypeIconUrl(String type) {
        String typeIconUrl = "";
        String flowTypeIconUrl = SYBConfig.getBaseFlowTypeIconUrl() + "/page/shop/app/images/nativeIcon";
        switch (toType(type)) {
            case S0001:
                typeIconUrl = flowTypeIconUrl + "/S0001.png";
                break;
            case S0002:
                typeIconUrl = flowTypeIconUrl + "/S0002.png";
                break;
            case S0003:
                typeIconUrl = flowTypeIconUrl + "/S0003.png";
                break;
            case S0004:
                typeIconUrl = flowTypeIconUrl + "/S0004.png";
                break;
            case S0005:
                typeIconUrl = flowTypeIconUrl + "/S0005.png";
                break;
            case S0006:
                typeIconUrl = flowTypeIconUrl + "/S0006.png";
                break;
            case S0007:
                typeIconUrl = flowTypeIconUrl + "/S0007.png";
                break;
            case S0008:
                typeIconUrl = flowTypeIconUrl + "/S0008.png";
                break;
            case S0009:
                typeIconUrl = flowTypeIconUrl + "/S0009.png";
                break;
            case S0010:
                typeIconUrl = flowTypeIconUrl + "/S0010.png";
                break;
            case G0001:
                typeIconUrl = flowTypeIconUrl + "/G0001.png";
                break;
            case G0002:
                typeIconUrl = flowTypeIconUrl + "/G0002.png";
                break;
            case G0003:
                typeIconUrl = flowTypeIconUrl + "/G0003.png";
                break;
            case G0004:
                typeIconUrl = flowTypeIconUrl + "/G0004.png";
                break;
            case G0005:
                typeIconUrl = flowTypeIconUrl + "/G0005.png";
                break;
            default:
                typeIconUrl = flowTypeIconUrl + "/S0001.png";
                break;
        }
        return typeIconUrl;
    }
}