




<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%
	String path = request.getContextPath();//获得项目名 
	request.setAttribute("ctx", path);
%>

<meta charset="utf-8">
<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta content="email=no" name="format-detection">
<link href="${ctx}/static/resources/styles/parking.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/static/gdpm/app/weixin/gzh/js/jquery-1.11.3.min.js"></script>

<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script language="javascript">

var myDate = new Date();
var second = myDate.getTime();
//加载
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: '${appId}', // 必填，公众号的唯一标识
    timestamp: ${timeStamp}, // 必填，生成签名的时间戳
    nonceStr: '${nonceStr}', // 必填，生成签名的随机串
    signature: '${signature}',// 必填，签名，见附录1
    jsApiList: [
    'checkJsApi',
            'chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
wx.ready(function(){
//支付

    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
});
wx.error(function(res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
});
wx.checkJsApi({
    jsApiList: ['chooseWXPay'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
    success: function(res) {
    //alert("检测接口:"+res.err_msg);
    }
    });
function payNow(){
	var myDate1 = new Date();
	var second1 = myDate1.getTime();
	if(second1-second > 160000){
		alert('订单过期，请重新进入');
	}else{
		wx.chooseWXPay({
		    timestamp: ${timeStamp}, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
		    nonceStr: '${nonceStr}', // 支付签名随机串，不长于 32 位
		    package: '${package11}', // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
		    signType: '${signType}', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
		    paySign: '${paySign}', // 支付签名
		    success: function (res) {
		        // 支付成功后的回调函数
		        WeixinJSBridge.log(res.err_msg);
		        //alert("支付接口:"+res.err_code + res.err_desc + res.err_msg);
		        if(!res.err_msg){
		                    //支付完后.跳转到成功页面.
		        	WeixinJSBridge.call('closeWindow');
		        }
		    }
		});
	}
}
</script>

<title>管理费</title>
</head>
<body>
	<section class="main">
		<br>
		 <c:choose>
            <c:when test="${typeFlag=='-1'}">
            	<center>
		        	<p>温馨提示</p>
		        </center>
            </c:when>
            <c:otherwise>
            	 <center>
		        	<p>温馨提示：请在三分钟内完成操作</p>
		        </center>
            </c:otherwise>
         </c:choose>
      
			<fieldset>
				
        <c:choose>
            <c:when test="${typeFlag=='-1'}">
                 
            <dl class="info-area">
                <dt class="totle">
                  <h2 class="totle-titile">${message}</h2>
                </dt>
            </dl>
            </c:when>
            <c:otherwise>
                <dl class="info-area">
                         <dt class="totle">
                            <h2 class="totle-titile">管理费</h2>
                            <h2 class="totle-num">￥${payCount}</h2>
                        </dt>
                </dl>
                <a href="#" class="btn btn-green form-btn" id="chooseWXPay" onclick="payNow()">立即缴费</a>
            </c:otherwise>
        </c:choose>
     </fieldset>
        </section>
        
        
	<div class="footer">
	<span>Copyright ? 2016 广电物业. All Rights Reserved.<br>由广电物业技术支持</span>
	</div>		

</body>
</html>
