package com.example.artshoes2;

import android.content.Context;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;

import java.lang.reflect.Method;

public class ToolBarUnits {
    //用于展示溢出菜单中图标
    public static void setIconVisable(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //用于实现溢出菜单栏中的功能
    //新建功能
    public static void CreateOfOverflowMenu(Context mthis){
        Toast.makeText(mthis,"你点击了搜索按钮",Toast.LENGTH_SHORT).show();
    }
}
