package cn.itcast.user.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.itcast.user.domin.User;
import cn.itcast.user.service.UserService;
import cn.itcast.user.service.exception.UserException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/18.
 */
public class UserServlet extends BaseServlet{
    private UserService userService = new UserService();
    //退出功能
    public String quit(HttpServletRequest req, HttpServletResponse resp) throws SQLException{
        req.getSession().invalidate();
        return "f:/jsps/user/login.jsp";
    }

    //修改密码功能

    public String updatePassword(HttpServletRequest req, HttpServletResponse resp) throws SQLException{
        User formUser = CommonUtils.toBean(req.getParameterMap(),User.class);
        User user = (User) req.getSession().getAttribute("sessionUser");
        if(user==null){
            req.setAttribute("msg","您还没有登录");
            return "f:/jsps/user/login.jsp";
        }
        try {
            userService.updatePassword(user.getUid(),formUser.getLoginpass(),formUser.getNewpass());
            req.setAttribute("msg","修改密码成功");
            req.setAttribute("code","success");
            return "f:/jsps/msg.jsp";
        } catch (UserException e) {
            req.setAttribute("msg",e.getMessage());
            req.setAttribute("user",formUser);
            return "f:/jsps/user/pwd.jsp";
        }
    }

    //登录功能
    public String login(HttpServletRequest req, HttpServletResponse resp) throws SQLException, UnsupportedEncodingException {
        /*
		 * 1. 封装表单数据到User
		 * 2. 校验表单数据
		 * 3. 使用service查询，得到User
		 * 4. 查看用户是否存在，如果不存在：
		 *   * 保存错误信息：用户名或密码错误
		 *   * 保存用户数据：为了回显
		 *   * 转发到login.jsp
		 * 5. 如果存在，查看状态，如果状态为false：
		 *   * 保存错误信息：您没有激活
		 *   * 保存表单数据：为了回显
		 *   * 转发到login.jsp
		 * 6. 登录成功：
		 * 　　* 保存当前查询出的user到session中
		 *   * 保存当前用户的名称到cookie中，注意中文需要编码处理。
		 */
        User formUser = CommonUtils.toBean(req.getParameterMap(),User.class);
        Map<String,String> errors = validateLogin(formUser,req.getSession());
        if(errors.size() > 0) {
            req.setAttribute("user", formUser);
            req.setAttribute("errors", errors);
            return "f:/jsps/user/login.jsp";
        }
        User user = userService.login(formUser);
        //开始判断
        if (user==null){
            req.setAttribute("user",formUser);
            req.setAttribute("msg","用户名或密码错误");
            return "f:/jsps/user/login.jsp";
        }else{
            req.getSession().setAttribute("sessionUser",user);

            String loginname = user.getLoginname();
            loginname = URLEncoder.encode(loginname, "utf-8");
            Cookie cookie = new Cookie("loginname",loginname);
            cookie.setMaxAge(60 * 60 * 24);
            resp.addCookie(cookie);
            return "r:/index.jsp";

        }

    }

    public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginname = req.getParameter("loginname");
        boolean b = userService.ajaxValidateLoginname(loginname);
        resp.getWriter().print(b);
        return null;
    }

    public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        boolean b = userService.ajaxValidateEmail(email);
        resp.getWriter().print(b);
        return null;
    }

    public String ajaxVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String verifyCode = req.getParameter("verifyCode");
        String vCode = (String) req.getSession().getAttribute("vCode");
        boolean b = verifyCode.equalsIgnoreCase(vCode);
        resp.getWriter().print(b);
        return null;
    }

//注册功能
    public String regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
//        String a = req.getParameter("loginname");
		/*
		 * 2. 校验之, 如果校验失败，保存错误信息，返回到regist.jsp显示
		 */
        Map<String,String> errors = validateRegist(formUser, req.getSession());
        if(errors.size() > 0) {
            req.setAttribute("form", formUser);
            req.setAttribute("errors", errors);
            return "f:/jsps/user/regist.jsp";
        }
		/*
		 * 3. 使用service完成业务
		 */
        userService.regist(formUser);
		/*
		 * 4. 保存成功信息，转发到msg.jsp显示！
		 */
        req.setAttribute("code", "success");
        req.setAttribute("msg", "注册功能，请马上到邮箱激活！");
        return "f:/jsps/msg.jsp";
    }

    public String activation(HttpServletRequest req, HttpServletResponse resp){
        String code = req.getParameter("activationCode");
        try {
            userService.activation(code);
            req.setAttribute("code", "success");//通知msg.jsp显示对号
            req.setAttribute("msg", "恭喜，激活成功，请马上登录！");
        } catch (UserException e) {
            req.setAttribute("msg", e.getMessage());
            req.setAttribute("code", "error");//通知msg.jsp显示X
        }
        return "f:/jsps/msg.jsp";
    }

    /*
	 * 注册校验
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key，错误信息为value，保存到map中
	 * 返回map
	 */
    private Map<String,String> validateRegist(User formUser, HttpSession session) {
        Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 校验登录名
		 */
        String loginname = formUser.getLoginname();
        if(loginname == null || loginname.trim().isEmpty()) {
            errors.put("loginname", "用户名不能为空！");
        } else if(loginname.length() < 3 || loginname.length() > 20) {
            errors.put("loginname", "用户名长度必须在3~20之间！");
        } else if(!userService.ajaxValidateLoginname(loginname)) {
            errors.put("loginname", "用户名已被注册！");
        }

		/*
		 * 2. 校验登录密码
		 */
        String loginpass = formUser.getLoginpass();
        if(loginpass == null || loginpass.trim().isEmpty()) {
            errors.put("loginpass", "密码不能为空！");
        } else if(loginpass.length() < 3 || loginpass.length() > 20) {
            errors.put("loginpass", "密码长度必须在3~20之间！");
        }

		/*
		 * 3. 确认密码校验
		 */
        String reloginpass = formUser.getReloginpass();
        if(reloginpass == null || reloginpass.trim().isEmpty()) {
            errors.put("reloginpass", "确认密码不能为空！");
        } else if(!reloginpass.equals(loginpass)) {
            errors.put("reloginpass", "两次输入不一致！");
        }

		/*
		 * 4. 校验email
		 */
        String email = formUser.getEmail();
        if(email == null || email.trim().isEmpty()) {
            errors.put("email", "Email不能为空！");
        } else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            errors.put("email", "Email格式错误！");
        } else if(!userService.ajaxValidateEmail(email)) {
            errors.put("email", "Email已被注册！");
        }

		/*
		 * 5. 验证码校验
		 */
        String verifyCode = formUser.getVerifyCode();
        String vcode = (String) session.getAttribute("vCode");
        if(verifyCode == null || verifyCode.trim().isEmpty()) {
            errors.put("verifyCode", "验证码不能为空！");
        } else if(!verifyCode.equalsIgnoreCase(vcode)) {
            errors.put("verifyCode", "验证码错误！");
        }

        return errors;
    }
    private Map<String,String> validateLogin(User formUser, HttpSession session) {
        Map<String,String> errors = new HashMap<String,String>();
        return errors;
    }
}
