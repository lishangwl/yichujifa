package com.liyi.flow;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FlowConfig.INVALID_VAL,
        FlowConfig.FLOW_HORIZONTAL_ALIGN_LEFT,
        FlowConfig.FLOW_HORIZONTAL_ALIGN_MIDDLE,
        FlowConfig.FLOW_HORIZONTAL_ALIGN_RIGHT,
        FlowConfig.FLOW_VERTICAL_ALIGN_TOP,
        FlowConfig.FLOW_VERTICAL_ALIGN_MIDDLE,
        FlowConfig.FLOW_VERTICAL_ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface FlowConfig {
    // 无效的值
    int INVALID_VAL = -1;

    /**
     * item 横向对齐
     */
    // 横向左对齐
    int FLOW_HORIZONTAL_ALIGN_LEFT = 1;
    //  横向居中对齐
    int FLOW_HORIZONTAL_ALIGN_MIDDLE = 2;
    // 横向右对齐
    int FLOW_HORIZONTAL_ALIGN_RIGHT = 3;

    /**
     * item 纵向对齐
     */
    // 纵向上对齐
    int FLOW_VERTICAL_ALIGN_TOP = 4;
    // 纵向居中对齐
    int FLOW_VERTICAL_ALIGN_MIDDLE = 5;
    // 纵向下对齐
    int FLOW_VERTICAL_ALIGN_BOTTOM = 6;
}
