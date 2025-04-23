package com.example.test_planigo.modeles.entitees;

import androidx.annotation.DrawableRes;

public class AccueilNavigationItem {
    private String title;
    @DrawableRes private int iconResId;
    private Class<?> targetActivity;

    public AccueilNavigationItem(String title, int iconResId, Class<?> targetActivity) {
        this.title = title;
        this.iconResId = iconResId;
        this.targetActivity = targetActivity;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }
}