package com.ucast.screen_program.jsonObject;


import java.awt.Color;

/**
 * Created by pj on 2018/4/28.
 */
public class ProgramJsonObj {
    //节目编号
    private int id = 1;
    //节目开始日期
    private String startData ;
    //节目结束日期
    private String endData ;
    //节目播放优先级  0：一般，1：优先
    private int priority = 0;
    //节目是否展示
    private String isDisplay ;
    //节目播放时长 单位 1s
    private int duration = 20;
    //是否有边框
    private String haveFrame;
    //边框显示速度 1（快） - 48 （慢）
    private int frameSpeed = 2;
    //边框效果 0：闪烁。1：顺时针转动。2：逆时钟转动。3：闪烁并顺时钟转动。
    // 4：闪烁并逆时钟转动。5：红绿交替闪烁。6：红绿交替转动。7：静止打出。
    private int frameStyle = 7;
    //节目展示类型 1: 文字  2:图片
    private int programType;
    //特技方式 0 - 48 0为随机
    private int displayStyle = 0;
    //特技速度 1（快） - 48 （慢）
    private int displayStyleSpeed = 1;
    //特技结束后的节目展示时间 单位s
    private int stayDuration = 10;
    //图片的下载地址
    private String picDownLoadUrl;
    //文字内容
    private String textContent;
    //文本是否自动换行
    private String isTextAutoNewLine;
    //文字水平对齐方式 1:中 2:右或下  2:左或上
    private int horizontalAlignment = 1;
    //文字垂直对齐方式 1:中 2:右或下  2:左或上
    private int verticalAlignment = 1;
    //文字字体
    private String texFontType = "Arial";
    //文字字体大小
    private int texFontSize = 14;
    //文字颜色
    private String texFontColor;

    public int getId() {
        return id % 999;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public boolean getIsDisplay() {
        if (isDisplay != null && isDisplay.equals("true"))
            return true;
        return false;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getDuration() {
        if(duration <= 0)
            return 20;
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean getHaveFrame() {
        if (haveFrame != null && haveFrame.equals("true"))
            return true;
        return false;
    }

    public void setHaveFrame(String haveFrame) {
        this.haveFrame = haveFrame;
    }

    public int getFrameSpeed() {
        if (frameSpeed <=0)
            return 2;
        return frameSpeed;
    }

    public void setFrameSpeed(int frameSpeed) {
        this.frameSpeed = frameSpeed;
    }

    public int getFrameStyle() {
        if (frameStyle <= 0)
            return 0;
        return frameStyle;
    }

    public void setFrameStyle(int frameStyle) {
        this.frameStyle = frameStyle;
    }

    public int getProgramType() {
        return programType;
    }

    public void setProgramType(int programType) {
        this.programType = programType;
    }

    public int getDisplayStyle() {
        if (displayStyle <= 0)
            return 0;
        return displayStyle;
    }

    public void setDisplayStyle(int displayStyle) {
        this.displayStyle = displayStyle;
    }

    public int getDisplayStyleSpeed() {
        if (displayStyleSpeed <= 0)
            return 1;
        return displayStyleSpeed;
    }

    public void setDisplayStyleSpeed(int displayStyleSpeed) {
        this.displayStyleSpeed = displayStyleSpeed;
    }

    public int getStayDuration() {
        if (stayDuration <= 0)
            return 1000;
        return stayDuration * 100;
    }

    public void setStayDuration(int stayDuration) {
        this.stayDuration = stayDuration;
    }

    public String getPicDownLoadUrl() {
        return picDownLoadUrl;
    }

    public void setPicDownLoadUrl(String picDownLoadUrl) {
        this.picDownLoadUrl = picDownLoadUrl;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public boolean getIsTextAutoNewLine() {
        if (isTextAutoNewLine == null || isTextAutoNewLine.equals("true"))
            return true;
        return false;
    }

    public void setIsTextAutoNewLine(String isTextAutoNewLine) {
        this.isTextAutoNewLine = isTextAutoNewLine;
    }

    public int getHorizontalAlignment() {
        if (horizontalAlignment <= 0)
            return 1;
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        if (verticalAlignment <= 0)
            return 1;
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getTexFontType() {
        if (texFontType == null)
            return "Arial";
        return texFontType;
    }

    public void setTexFontType(String texFontType) {
        this.texFontType = texFontType;
    }

    public int getTexFontSize() {
        if (texFontSize <= 0)
            return 14;
        return texFontSize;
    }

    public void setTexFontSize(int texFontSize) {
        this.texFontSize = texFontSize;
    }

    public Color getTexFontColor() {
        if (texFontColor == null || texFontColor.length() < 6)
            return Color.red;
        int red = Integer.parseInt(texFontColor.substring(0,2));
        int green = Integer.parseInt(texFontColor.substring(2,4));
        int blue = Integer.parseInt(texFontColor.substring(4,6));
        return new Color(red & 0xFF,green & 0xFF,blue & 0xFF);
    }

    public void setTexFontColor(String texFontColor) {
        this.texFontColor = texFontColor;
    }

    @Override
    public String toString() {
        return "ProgramJsonObj{" +
                "id=" + id +
                ", \nstartData='" + startData + '\'' +
                ", \nendData='" + endData + '\'' +
                ", \npriority='" + priority + '\'' +
                ", \nisDisplay=" + isDisplay +
                ", \nduration=" + duration +
                ", \nhaveFrame=" + haveFrame +
                ", \nframeSpeed=" + frameSpeed +
                ", \nframeStyle=" + frameStyle +
                ", \nprogramType=" + programType +
                ", \ndisplayStyle=" + displayStyle +
                ", \ndisplayStyleSpeed=" + displayStyleSpeed +
                ", \nstayDuration=" + stayDuration +
                ", \npicDownLoadUrl='" + picDownLoadUrl + '\'' +
                ", \ntextContent='" + textContent + '\'' +
                ", \nisTextAutoNewLine=" + isTextAutoNewLine +
                ", \nhorizontalAlignment=" + horizontalAlignment +
                ", \nverticalAlignment=" + verticalAlignment +
                ", \ntexFontType='" + texFontType + '\'' +
                ", \ntexFontSize=" + texFontSize +
                ", \ntexFontColor='" + texFontColor + '\'' +
                '}';
    }
}
