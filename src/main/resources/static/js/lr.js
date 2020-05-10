$(function () {
    $('#regist_btn').click(function () {
        var name  = $('#regist_account').val();
        var pass1 = $('#regist_password1').val();
        var pass2 = $('#regist_password2').val();
        console.log(name+"\t"+pass1+"\t"+pass2)
        if (name==''||pass1==''||pass2==''){
            alert("输入不能为空")
            return;
        }
        if (pass1!=pass2){
            alert("两次输入密码不一致")
            return;
        }

        var obj = ajaxRequest("/regist","{\"name\":\""+name+"\",\"pass\":\""+pass1+"\"}",false)
        if (obj.code == 200){
            alert("注册成功!!")
        }else {
            alert("注册失败！")
        }
    })

    $('#login_btn').click(function () {
        var name = $('#login_number').val()
        var pass = $('#login_password').val();
        if (name==''||pass==''){
            alert("输入不能为空")
            return;
        }
        var obj = ajaxRequest("/login","{\"name\":\""+name+"\",\"pass\":\""+pass+"\"}",false);
        if (obj.code == 200){
            // alert("登录成功!!")
            console.log("save userID by storage "+obj.data)
            var storage = null;
            if (window.sessionStorage){
                storage = window.sessionStorage;
                storage.setItem("userId",obj.data);
            }
            window.location.href="/editCode.html"
        }else {
            alert("登录失败！")
        }
    })
})

function ajaxRequest(url, data, asyn, contentType) {
    var re = undefined;
    var ct = contentType;
    if (ct == undefined) {
        ct = "application/json; charset=UTF-8";
    }
    console.log("------ajaxRequest -------")
    console.log("url : " + url)
    console.log("data : " + data)
    console.log("asyn : " + asyn)
    console.log("contentType : " + contentType)
    console.log("-------------------------")
    $.ajax({
        type: "POST",//方法类型
        // dataType: "application/json;charset=UTF-8",
        contentType: ct,
        url: url,
        data: data,
        async: asyn,
        success: function (result) {
            console.log(result);
            // re = JSON.parse(result);
            re = result;
        },
        error: function (XMLHttpResponse, textStatus, errorThrown) {
            //todo 便于bug调试
            console.log("1 异步调用返回失败,XMLHttpResponse.readyState:" + XMLHttpResponse.readyState);
            console.log("2 异步调用返回失败,XMLHttpResponse.status:" + XMLHttpResponse.status);
            console.log("3 异步调用返回失败,textStatus:" + textStatus);
            console.log("4 异步调用返回失败,errorThrown:" + errorThrown);
        }
    });
    return re;
}