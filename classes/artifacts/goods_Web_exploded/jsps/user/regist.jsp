<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/9/18
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册页面</title>
    <link rel="stylesheet" type="text/css" href="../css/user/regist.css">
    <%--<c:url value='/jquery/jquery-1.5.1.js'/>--%>
    <script type="text/javascript" src="/goods/jquery/jquery-1.5.1.js"></script>
    <%--<c:url value='/jsps/js/user/regist.js'/>--%>


</head>
<body>
<div id="divMain">
    <div id="divTitle">
        <span id="spanTitle">新用户注册</span>
    </div>
    <div id="divBody">
        <form action="<c:url value='/UserServlet'/>" method="post" id="registForm">
            <input type="hidden" name="method" value="regist">
            <table id="tableForm">
            <tr>
                <td class="tdText">
                    用户名
                </td>
                <td class="tdInput">
                    <input class="inputClass" type="text" id="loginname" name="loginname" value="${form.loginname}"/>
                </td>
                <td class="tdError">
                    <label class="errorClass" id="loginnameError">${errors.loginname}</label>
                </td>
            </tr>
            <tr>
                <td class="tdText">
                    登录密码
                </td>
                <td class="tdInput">
                    <input class="inputClass" type="text" id="loginpass" name="loginpass" value="${form.loginpass}"/>
                </td>
                <td>
                    <label class="errorClass" id="loginpassError">${errors.loginpass}</label>
                </td>
            </tr>
            <tr>
                <td class="tdText">确认密码</td>
                <td class="tdInput">
                    <input class="inputClass" type="text" id="reloginpass" name="reloginpass" value="${form.reloginpass}"/>
                </td>
                <td>
                    <label class="errorClass" id="reloginpassError">${errors.reloginpass}</label>
                </td>
            </tr>
            <tr>
                <td class="tdText">Email</td>
                <td class="tdInput">
                    <input class="inputClass" type="text" id="email" name="email" value="${form.email}"/>
                </td>
                <td>
                    <label  class="errorClass" id="emailError">${errors.email}</label>
                </td>
            </tr>
            <tr>
                <td class="tdText">图形验证码</td>
                <td class="tdInput">
                    <input class="inputClass" type="text" id="verifyCode" name="verifyCode" value="${form.verifyCode}"/>
                </td>
                <td>
                    <label class="errorClass" id="verifyCodeError">${errors.verifyCode}</label>
                </td>
            </tr>
            <tr>
                <td class="tdText"></td>
                <td  class="tdInput">
                    <div id="divVerifyCode">
                        <img id="imgVerifyCode" src="/goods/VerifyCodeServlet"/>
                    </div>
                </td>
                <td>
                    <label>
                        <a href="">换一张</a>
                    </label>
                </td>
            </tr>
            <tr>
                <td class="tdText"></td>
                <td>
                    <input type="image" src="/goods/images/regist1.jpg" id="submitbtn">
                    <%--<input type="submit" value="提交">--%>
                </td>
                <td>
                    <label>

                    </label>
                </td>
            </tr>
            </table>
        </form>
    </div>
</div>
</body>
<script type="text/javascript" src="/goods/jsps/js/user/regist.js"></script>
</html>
