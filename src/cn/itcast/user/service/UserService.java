package cn.itcast.user.service;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.user.dao.UserDao;
import cn.itcast.user.domin.User;
import cn.itcast.user.service.exception.UserException;
import com.mchange.v2.codegen.bean.Property;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Administrator on 2018/9/18.
 */
public class UserService {
    private UserDao userDao = new UserDao();

    //修改密码
    public void updatePassword(String uid,String oldpass,String newpass) throws SQLException, UserException {
       boolean bool = userDao.findByUidAndPass(uid,oldpass);
       if (!bool){
           throw new UserException("老密码错误");
       }
       userDao.updatePassword(uid,newpass);
    }

    //登录功能
    public User login(User user) throws SQLException {
        return userDao.findByLoginnameAndLoginpass(user.getLoginname(),user.getLoginpass());
    }

    //激活功能
    public void activation(String code) throws UserException {
        try {
            User user = userDao.findByCode(code);
            if (user==null) throw new UserException("无效的激活码");
            if (user.isStatus()) throw new UserException("已激活 无需二次激活");
            userDao.updateStatus(user.getUid(),true);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }

    //用户名校验
    public boolean ajaxValidateLoginname (String loginname){
        try {
            return userDao.ajaxValidateLoginname(loginname);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    //邮箱校验
    public  boolean ajaxValidateEmail(String email){
        try {
            return userDao.ajaxValidateEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    public void regist(User user){
        //1.数据补齐
        user.setUid(CommonUtils.uuid());
        user.setStatus(false);
        user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
        //2.向数据库插入信息
        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //3.发邮件
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String host = prop.getProperty("host");//服务器主机名
        String name = prop.getProperty("username");//登录名
        String pass = prop.getProperty("password");//登录密码
        Session session = MailUtils.createSession(host, name, pass);

		/*
		 * 创建Mail对象
		 */
        String from = prop.getProperty("from");
        String to = user.getEmail();
        String subject = prop.getProperty("subject");
        // MessageForm.format方法会把第一个参数中的{0},使用第二个参数来替换。
        // 例如MessageFormat.format("你好{0}, 你{1}!", "张三", "去死吧"); 返回“你好张三，你去死吧！”
        String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());
        Mail mail = new Mail(from, to, subject, content);
		/*
		 * 发送邮件
		 */
        try {
            MailUtils.send(session, mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
