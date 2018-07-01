package com.yehowah.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by VULCAN on 2018/6/29.
 */
public class LoginAction extends ActionSupport{
    private String username;
    private String password;
    public File mPhoto;
    public String mPhotoFileName;
    public String mPhotoContentType;

    @Override
    public String execute() throws Exception {
        if (username.equals("admin")&&password.equals("123")){
            return SUCCESS;
        }else {
            return LOGIN;
        }
    }

    public String uploadFile(){
        System.out.println(username+", "+password);
        if (mPhoto == null){
            System.out.println(mPhotoFileName+" is null");
        }
        String dir = ServletActionContext.getServletContext().getRealPath("files");
        System.out.println("dir: "+dir+", mPhotoFileName: "+mPhotoFileName);//yehowah.jpg

        File file = new File(dir,mPhotoFileName);//修改之前"timg.jpg"，获取从客户端得到的图片
        try {
            FileUtils.copyFile(mPhoto,file);
            System.out.println("mPhoto "+mPhoto.toPath());
            // C:\Users\VULCAN\.IntelliJIdea2017.1\system\tomcat\Tomcat_9_0_1_StartLogin\work\Catalina\localhost\StartLogin\upload_57038ef0_ba00_49d3_9759_954c74665de5_00000002.tmp
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postFile() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        ServletInputStream inputStream = request.getInputStream();

        //D:\Soft_Study\java_Study\JavaEE\StartLogin\out\artifacts\StartLogin_war_exploded\
        // 该文件夹下新建一个文件夹files
        String dir = ServletActionContext.getServletContext().getRealPath("files");
        System.out.println("dir: "+dir);
        File file = new File(dir,"timg.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int len = 0;
        byte[] buf = new byte[1024];

        while((len = inputStream.read(buf)) != -1){
            fileOutputStream.write(buf,0,len);
        }

        fileOutputStream.flush();
        fileOutputStream.close();
        return null;
    }

    public String postString() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        ServletInputStream inputStream = request.getInputStream();

        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        byte[] buf = new byte[1024];

        while((len = inputStream.read(buf)) != -1){
            String string = new String(buf, 0, len);
            stringBuilder.append(string);
        }
        System.out.println(stringBuilder.toString());
        return null;
    }

    public String login() throws IOException {
        System.out.println("用户名："+username+", 密码： "+password);
        //服务器返回数据给客户端
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = response.getWriter();
        writer.write("login success");//将数据写入
        writer.flush();
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
