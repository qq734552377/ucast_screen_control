package com.ucast.screen_program.jsonObject;

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
    private String priority ;
    //节目是否展示
    private boolean isDisplay ;
    //节目播放时长 单位 1s
    private int duration = 20;
    //是否有边框
    private boolean haveFrame;
    //边框显示速度 1（快） - 48 （慢）
    private int frameSpeed;
    //边框效果 0：闪烁。1：顺时针转动。2：逆时钟转动。3：闪烁并顺时钟转动。
    // 4：闪烁并逆时钟转动。5：红绿交替闪烁。6：红绿交替转动。7：静止打出。
    private int farmeStyle = 1;
    //节目展示类型 1: 文字  2:图片
    private int programType;
    //特技方式 0 - 48 0为随机
    private int displayStyle;
    //特技速度 1（快） - 48 （慢）
    private int displayStyleSpeed = 2;
    //特技结束后的节目展示时间 单位10ms
    private int stayDuration = 100;
    //图片的下载地址
    private String picDownLoadUrl;
    //文字内容
    private String textContent;
    //文本是否自动换行
    private boolean isTextAutoNewLine;
    //文字水平对齐方式 1:中 2:右或下  2:左或上
    private int horizontalAlignment;
    //文字垂直对齐方式 1:中 2:右或下  2:左或上
    private int verticalAlignment;
    //文字字体
    private String texFontType = "Arial";
    //文字字体大小
    private int texFontSize = 14;
    //文字颜色
    private String texFontColor;

    public int getId() {
        return id;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isHaveFrame() {
        return haveFrame;
    }

    public void setHaveFrame(boolean haveFrame) {
        this.haveFrame = haveFrame;
    }

    public int getFrameSpeed() {
        return frameSpeed;
    }

    public void setFrameSpeed(int frameSpeed) {
        this.frameSpeed = frameSpeed;
    }

    public int getFarmeStyle() {
        return farmeStyle;
    }

    public void setFarmeStyle(int farmeStyle) {
        this.farmeStyle = farmeStyle;
    }

    public int getProgramType() {
        return programType;
    }

    public void setProgramType(int programType) {
        this.programType = programType;
    }

    public int getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(int displayStyle) {
        this.displayStyle = displayStyle;
    }

    public int getDisplayStyleSpeed() {
        return displayStyleSpeed;
    }

    public void setDisplayStyleSpeed(int displayStyleSpeed) {
        this.displayStyleSpeed = displayStyleSpeed;
    }

    public int getStayDuration() {
        return stayDuration;
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

    public boolean isTextAutoNewLine() {
        return isTextAutoNewLine;
    }

    public void setTextAutoNewLine(boolean textAutoNewLine) {
        isTextAutoNewLine = textAutoNewLine;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getTexFontType() {
        return texFontType;
    }

    public void setTexFontType(String texFontType) {
        this.texFontType = texFontType;
    }

    public int getTexFontSize() {
        return texFontSize;
    }

    public void setTexFontSize(int texFontSize) {
        this.texFontSize = texFontSize;
    }

    public String getTexFontColor() {
        return texFontColor;
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
                ", \nfarmeStyle=" + farmeStyle +
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
