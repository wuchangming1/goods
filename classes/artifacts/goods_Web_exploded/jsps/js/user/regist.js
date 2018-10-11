/**
 * Created by Administrator on 2018/9/18.
 */
$(function () {

    $(".errorClass").each(function () {
        showError($(this));
    })


    $("#submitbtn").hover(
        function () {
            $("#submitbtn").attr("src","/goods/images/regist2.jpg")
        },
        function () {
            $("#submitbtn").attr("src","/goods/images/regist1.jpg")
        }
    )


    $(".inputClass").focus(function () {
        var labelId = $(this).attr("id")+"Error";
        $("#"+labelId).text("");
        showError($("#"+labelId));
    })
    
    $(".inputClass").blur(function () {
        // alert($(this).attr("id"));
        var id = $(this).attr("id");
        var funName = "validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";
        eval(funName);
        // validateLoginname();

    })
    $("#registForm").submit(function () {
        var bool = true;
        if(!validateLoginname()){
            bool = false;
        }
        if(!validateEmail()){
            bool = false;
        }
        if(!validateLoginpass()){
            bool = false;
        }
        if(!validateReloginpass()){
            bool = false;
        }
        if(!validateVerifyCode()){
            bool = false;
        }

    });
});

function validateLoginname() {

    var id = "loginname";
    var value = $("#"+id).val();
    if(!value){
        $("#"+id+"Error").text("用户名不能为空")
        showError($("#"+id+"Error"));
        return false;
    }

    if(value.length<3||value.length>20){
        $("#"+id+"Error").text("用户名长度错误")
        showError($("#"+id+"Error"));
        return false;
    }

    //3.是否注册校验
    alert("2222");
    $.ajax({
        url:"/goods/UserServlet",//要请求的servlet
        data:{method:"ajaxValidateLoginname", loginname:value},//给服务器的参数
        type:"POST",
        dataType:"json",
        async:false,//是否异步请求，如果是异步，那么不会等服务器返回，我们这个函数就向下运行了。
        cache:false,
        success:function(result) {
            if(!result) {//如果校验失败
                $("#" + id + "Error").text("用户名已被注册！");
                showError($("#" + id + "Error"));
                return false;
            }
        }
    });

    return true;

}
function validateLoginpass() {
    var id="loginpass";
    var value = $("#"+id).val();
    //1.非空校验
    if (!value){
        $("#"+id+"Error").text("密码不能为空");
        showError($("#"+id+"Error"));
        return false;
    }
    //2.长度校验
    if(value.length<3||value.l >20){
        $("#"+id+"Error").text("密码长度应为3-20");
        showError($("#"+id+"Error"));
        return false;

    }

    return true;
}

function validateReloginpass() {
    var id ="reloginpass";
    var value = $("#"+id).val();
    //1.not null
    if (value==null){
        $("#"+id+"Error").text("用户名不能为空");
        showError($("#"+id+"Error"));
        return false;
    }
    //2.length
    if(value.length<3||value.l >20){
        $("#"+id+"Error").text("密码长度应为3-20");
        showError($("#"+id+"Error"));
        return false;

    }
    //3.
    if (value!= $("#loginpass").val()){
        $("#"+id+"Error").text("前后密码不一致");
        showError($("#"+id+"Error"));
        return false;
    }

    return true;
}

function validateEmail() {
    var id = "email";
    var value = $("#"+id).val();
    if(!value) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("Email不能为空！");
        showError($("#" + id + "Error"));
        return false;
    }
    /*
     * 2. Email格式校验
     */
    if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("错误的Email格式！");
        showError($("#" + id + "Error"));
        return false;
    }
    //3.后台校验
    $.ajax({
        url:"/goods/UserServlet",
        data:{method:"ajaxValidateEmail",email:value},
        type:"post",
        dataType:"json",
        async:false,
        cache:false,
        success:function (result) {
            if(!result){
                $("#"+id+"Error").text("该邮箱已被注册");
                showError($("#"+id+"Error"));
                return false;
            }
        }
    });
    return true;
}

function validateVerifyCode() {
    var id ="verifyCode";
    var value = $("#"+id).val();
    //1.not null
    if(!value){
        $("#"+id+"Error").text("验证码不能为空")
        showError($("#"+id+"Error"));
        return false;
    }
    //2.length
    if (value.length!=4){
        $("#"+id+"Error").text("验证码长度为4位");
        showError($("#"+id+"Error"));
        return false;
    }
    //3.后台验证
    $.ajax({
        url:"/goods/UserServlet",
        data:{method:"ajaxVerifyCode",verifyCode:value},
        type:"post",
        dataType:"json",
        async:false,
        cache:false,
        success:function (result) {
            if(!result){
                $("#"+id+"Error").text("验证码错误");
                showError($("#"+id+"Error"));
                return false;
            }
        }
    });

    return true;
}



function showError(ele) {
    var text = ele.text();

    if(!text){
        ele.css("display","none");

    }else {

        ele.css("display","");

    }
}
