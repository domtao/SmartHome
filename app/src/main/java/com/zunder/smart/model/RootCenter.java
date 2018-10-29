package com.zunder.smart.model;

public class RootCenter {
    private int Padding;
    private int ScreenOrientation;
    private int TabPosition;
    private int TabHeight=60;
    private int MinTabCount=4;
    private String ColorString;

    public int getPadding() {
        return Padding;
    }

    public void setPadding(int padding) {
        Padding = padding;
    }

    public int getScreenOrientation() {
        return ScreenOrientation;
    }

    public void setScreenOrientation(int screenOrientation) {
        ScreenOrientation = screenOrientation;
    }

    public int getTabPosition() {
        return TabPosition;
    }

    public void setTabPosition(int tabPosition) {
        TabPosition = tabPosition;
    }

    public int getTabHeight() {
        return TabHeight;
    }

    public void setTabHeight(int tabHeight) {
        TabHeight = tabHeight;
    }

    public int getMinTabCount() {
        return MinTabCount;
    }

    public void setMinTabCount(int minTabCount) {
        MinTabCount = minTabCount;
    }

    public String getColorString() {
        return ColorString;
    }

    public void setColorString(String colorString) {
        ColorString = colorString;
    }
}
