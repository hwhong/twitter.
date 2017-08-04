package com.example.hwhong.twitter.Utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.example.hwhong.twitter.R;

/**
 * Created by hwhong on 8/4/17.
 */

public class Utils {
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }
}
