package cn.itcast.user.dao;

import cn.itcast.jdbc.TxQueryRunner;
import cn.itcast.user.domin.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * Created by Administrator on 2018/9/18.
 */
public class UserDao {
    QueryRunner qr = new TxQueryRunner();



    public boolean findByUidAndPass(String uid,String loginpass) throws SQLException {
        String sql = "select count(*) from t_user where uid=? and loginpass=?";
        Number number = (Number) qr.query(sql,new ScalarHandler(),uid,loginpass);
        return  number.intValue()>0;
    }
    public void updatePassword(String uid,String loginpass) throws SQLException {
        String sql = "update t_user set loginpass=? where uid=?";
        qr.update(sql,loginpass,uid);
    }


    //登录查询

    public User findByLoginnameAndLoginpass(String loginname,String loginpass) throws SQLException {
        String sql = "select * from t_user where loginname=? and loginpass=?";
        return qr.query(sql,new BeanHandler<User>(User.class),loginname,loginpass);
    }
    //通过激活码查询用户
    public  User findByCode(String code) throws SQLException {
        String sql = "select * from t_user where activationCode=?";
        return qr.query(sql,new BeanHandler<User>(User.class),code);
    }

    //修改用户状态
    public void updateStatus(String uid,boolean status) throws SQLException {
        String sql = "update t_user set status=? where uid=?";
        qr.update(sql,status,uid);
    }

    //校验用户名是否注册
    public boolean ajaxValidateLoginname (String loginname) throws SQLException{
        String sql = "select count(1) from t_user where loginname = ?";
        Number number = (Number) qr.query(sql,new ScalarHandler(),loginname);
        return number.intValue()==0;
    }

    //校验邮箱是否注册
    public  boolean ajaxValidateEmail(String email) throws SQLException{
        String sql = "select count(1) from t_user where email = ?";
        Number number = (Number)qr.query(sql,new ScalarHandler(),email);
        return number.intValue()==0;
    }

    public void addUser(User user) throws SQLException {
        String sql ="insert into t_user values(?,?,?,?,?,?)";
        Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.isStatus(),user.getActivationCode()};
        qr.update(sql,params);
    }
}
