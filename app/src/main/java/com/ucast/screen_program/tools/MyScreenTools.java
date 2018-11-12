package com.ucast.screen_program.tools;

import com.ucast.screen_program.entity.Config;
import com.ucast.screen_program.jsonObject.ProgramJsonObj;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.ImageFileBxPage;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.TextBinary;

/**
 * Created by pj on 2018/4/26.
 */
public class MyScreenTools {
    public static ProgramBxFile getOneProgramBxFileWithText(Bx6GScreenProfile profile,int id,String text){
        ProgramBxFile p002 = null;
       try{
           DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

           p002 = new ProgramBxFile(id, profile);
           //设定节目播放时间长度，单位为秒，0：循序播放。
           p002.setProgramTimeSpan(20);
           //设定是否显示边框
           p002.setFrameShow(false);
           //设置边框显示速度，1 - 48
           p002.setFrameSpeed(20);
           //styleIndex - 内建效果编号，双基色 1 ~ 18，单基色 1 ~ 14。
           p002.loadFrameImage(1);

           TextCaptionBxArea textAreaTop = new TextCaptionBxArea(0, 0, 128, 64, profile);
           textAreaTop.setFrameShow(false);
           textAreaTop.loadFrameImage(4);
           TextCaptionBxArea textAreaBottom = new TextCaptionBxArea(0, 64, 128, 64, profile);
           textAreaBottom.setFrameShow(false);
           textAreaBottom.loadFrameImage(4);


           TextBxPage page = new TextBxPage(text);
           // 对文本的处理是否自动换行
           page.setLineBreak(true);
           // 设置文本水平对齐方式
           page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
           // 设置文本垂直居中方式
           page.setVerticalAlignment(TextBinary.Alignment.CENTER);
           // 设置文本字体
           page.setFont(new Font("Arial" , Font.PLAIN, 14));
           // 设置文本颜色
           page.setForeground(Color.magenta);
           // 设置区域背景色，默认为黑色
           page.setBackground(Color.BLACK);
           // 调整特技方式
           page.setDisplayStyle(styles[0]);
           // 调整特技速度
           page.setSpeed(2);
           // 调整停留时间, 单位 10ms
           page.setStayTime(200);

           textAreaTop.addPage(page);
           textAreaBottom.addPage(page);

           p002.addArea(textAreaTop);
           p002.addArea(textAreaBottom);

       }catch (Exception e){
           FileTools.writeToLogFile(e.toString());
       }
        return p002;
    }
    public static ProgramBxFile getOneProgramBxFileWithProgramJsonObj(Bx6GScreenProfile profile, ProgramJsonObj textProgram){
        ProgramBxFile p002 = null;
        if (textProgram.getProgramType() == 2){
            String url = textProgram.getPicDownLoadUrl().trim();
            String path = isExitInSdcard(url);
            if (path == null){
                return null;
            }else{
                return getOneProgramBxFileWitProgramJsonObjAndhPicpath(profile,textProgram,path);
            }
        }
       try{
           DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

           p002 = new ProgramBxFile(textProgram.getId(), profile);
           //设定节目播放时间长度，单位为秒，0：循序播放。
           p002.setProgramTimeSpan(textProgram.getDuration());
           //设定是否显示边框
           p002.setFrameShow(textProgram.getHaveFrame());
           //设置边框显示速度，1 - 48
           p002.setFrameSpeed(textProgram.getFrameSpeed());
           //styleIndex - 内建效果编号，双基色 1 ~ 18，单基色 1 ~ 14。
           p002.loadFrameImage(textProgram.getFrameStyle());
           //节目播放优先级  0：一般，1：优先
           p002.setPriority(textProgram.getPriority());

           TextCaptionBxArea textAreaTop = new TextCaptionBxArea(0, 0, 128, 64, profile);
           textAreaTop.setFrameShow(textProgram.getHaveFrame());
           textAreaTop.loadFrameImage(textProgram.getFrameStyle());
           TextCaptionBxArea textAreaBottom = new TextCaptionBxArea(0, 64, 128, 64, profile);
           textAreaBottom.setFrameShow(textProgram.getHaveFrame());
           textAreaBottom.loadFrameImage(textProgram.getFrameStyle());


           TextBxPage page = new TextBxPage(textProgram.getTextContent());
           // 对文本的处理是否自动换行
           page.setLineBreak(textProgram.getIsTextAutoNewLine());
           // 设置文本水平对齐方式
           page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
           // 设置文本垂直居中方式
           page.setVerticalAlignment(TextBinary.Alignment.CENTER);
           // 设置文本字体
           page.setFont(new Font(textProgram.getTexFontType() , Font.PLAIN, textProgram.getTexFontSize()));
           // 设置文本颜色
           page.setForeground(textProgram.getTexFontColor());
           // 设置区域背景色，默认为黑色
           page.setBackground(Color.BLACK);
           // 调整特技方式
           page.setDisplayStyle(styles[textProgram.getDisplayStyle()]);
           // 调整特技速度
           page.setSpeed(textProgram.getDisplayStyleSpeed());
           // 调整停留时间, 单位 10ms
           page.setStayTime(textProgram.getStayDuration());

           textAreaTop.addPage(page);
           textAreaBottom.addPage(page);

           p002.addArea(textAreaTop);
           p002.addArea(textAreaBottom);

       }catch (Exception e){
            FileTools.writeToLogFile(e.toString());
       }
        return p002;
    }
    //128x64的图片
    public static ProgramBxFile getOneProgramBxFileWithPicpath(Bx6GScreenProfile profile,int id,String path){
        ProgramBxFile p002 = null;
       try{
           DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

           p002 = new ProgramBxFile(id, profile);
           //设定节目播放时间长度，单位为秒，0：循序播放。
           p002.setProgramTimeSpan(15);
           //设定是否显示边框
           p002.setFrameShow(false);
           //设置边框显示速度，1 - 48
           p002.setFrameSpeed(20);
           //styleIndex - 内建效果编号，双基色 1 ~ 18，单基色 1 ~ 14。
           p002.loadFrameImage(1);

           TextCaptionBxArea textAreaTop = new TextCaptionBxArea(0, 0, 128, 64, profile);
           textAreaTop.setFrameShow(false);
           textAreaTop.loadFrameImage(4);
           TextCaptionBxArea textAreaBottom = new TextCaptionBxArea(0, 64, 128, 64, profile);
           textAreaBottom.setFrameShow(false);
           textAreaBottom.loadFrameImage(4);


           ImageFileBxPage page = new ImageFileBxPage(path);
           // 调整特技方式
           page.setDisplayStyle(styles[0]);
           // 调整特技速度
           page.setSpeed(1);
           // 调整停留时间, 单位 10ms
           page.setStayTime(1000);

           textAreaTop.addPage(page);
           textAreaBottom.addPage(page);

           p002.addArea(textAreaTop);
           p002.addArea(textAreaBottom);

       }catch (Exception e){

       }
        return p002;
    }
    //128x64的图片
    public static ProgramBxFile getOneProgramBxFileWitProgramJsonObjAndhPicpath(Bx6GScreenProfile profile,ProgramJsonObj picProgram,String path){
        ProgramBxFile p002 = null;
       try{
           DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

           p002 = new ProgramBxFile(picProgram.getId(), profile);
           //设定节目播放时间长度，单位为秒，0：循序播放。
           p002.setProgramTimeSpan(picProgram.getDuration());
           //设定是否显示边框
           p002.setFrameShow(picProgram.getHaveFrame());
           //设置边框显示速度，1 - 48
           p002.setFrameSpeed(picProgram.getFrameSpeed());
           //styleIndex - 内建效果编号，双基色 1 ~ 18，单基色 1 ~ 14。
           p002.loadFrameImage(picProgram.getFrameStyle());
           //节目播放优先级  0：一般，1：优先
           p002.setPriority(picProgram.getPriority());

           TextCaptionBxArea textAreaTop = new TextCaptionBxArea(0, 0, 128, 64, profile);
           textAreaTop.setFrameShow(picProgram.getHaveFrame());
           textAreaTop.loadFrameImage(picProgram.getFrameStyle());
           TextCaptionBxArea textAreaBottom = new TextCaptionBxArea(0, 64, 128, 64, profile);
           textAreaBottom.setFrameShow(picProgram.getHaveFrame());
           textAreaBottom.loadFrameImage(picProgram.getFrameStyle());


           ImageFileBxPage page = new ImageFileBxPage(path);
           // 调整特技方式
           page.setDisplayStyle(styles[picProgram.getDisplayStyle()]);
           // 调整特技速度
           page.setSpeed(picProgram.getDisplayStyleSpeed());
           // 调整停留时间, 单位 10ms
           page.setStayTime(picProgram.getStayDuration());

           textAreaTop.addPage(page);
           textAreaBottom.addPage(page);

           p002.addArea(textAreaTop);
           p002.addArea(textAreaBottom);

       }catch (Exception e){
           FileTools.writeToLogFile(e.toString());
       }
        return p002;
    }
    //128x128的图片
    public static ProgramBxFile getOneProgramBxFileWithGoodPicpath(Bx6GScreenProfile profile,int id,String path){
        ProgramBxFile p002 = null;
       try{
           DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

           p002 = new ProgramBxFile(id, profile);
           //设定节目播放时间长度，单位为秒，0：循序播放。
           p002.setProgramTimeSpan(15);
           //设定是否显示边框
           p002.setFrameShow(false);
           //设置边框显示速度，1 - 48
           p002.setFrameSpeed(20);
           //styleIndex - 内建效果编号，双基色 1 ~ 18，单基色 1 ~ 14。
           p002.loadFrameImage(1);

           TextCaptionBxArea textAreaTop = new TextCaptionBxArea(0, 0, 128, 128, profile);
           textAreaTop.setFrameShow(false);
           textAreaTop.loadFrameImage(4);
           ImageFileBxPage page = new ImageFileBxPage(path);
           // 调整特技方式
           page.setDisplayStyle(styles[8]);
           // 调整特技速度
           page.setSpeed(1);
           // 调整停留时间, 单位 10ms
           page.setStayTime(1000);

           textAreaTop.addPage(page);

           p002.addArea(textAreaTop);

       }catch (Exception e){
           FileTools.writeToLogFile(e.toString());
       }
        return p002;
    }

    public static int getIdByProgramName(String name){
        String nameId = name.substring(1);

        return Integer.parseInt(nameId);
    }


    public static String isExitInSdcard(String url){
        String path = Config.PICPATHDIR + "/" + url.substring(url.lastIndexOf("/") + 1) ;
        File file = new File(path);
        if (!file.exists()){
            MyHttpRequetTool.downLoadOnePic(url,path);
            return null;
        }else{
            return path;
        }
    }

}
