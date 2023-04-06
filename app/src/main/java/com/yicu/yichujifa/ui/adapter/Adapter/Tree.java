package com.yicu.yichujifa.ui.adapter.Adapter;

import java.util.List;

/**
 * @author: WelliJohn
 * @time: 2018/8/3-15:29
 * @email: wellijohn1991@gmail.com
 * @desc:
 */
public interface Tree<T extends Tree> {

    int getLevel();

    List<T> getChilds();

    boolean isExpand();

}