




<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${ctx}/css/parking.css">
<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>

<script type="text/javascript">
	$(function() {
		document.getElementsByName("usualCar").value = "1";
	})
	function doSwitch(){
		var status = document.getElementsByName("usualCar").value;
		if (status == "1") {
			$("#_switch").toggleClass("switch-on");                                // 清除div的样式
			document.cardForm.usualCar.value = "2";
			document.getElementsByName("usualCar").value = "2";
		} else {
			$("#_switch").toggleClass("switch-on");                                     // 清除div的样式
			document.cardForm.usualCar.value = "1";
		}
	}
	function isAllJiao(str)//True 有全角，False没有全角
	{
	    for (var i = 0; i < str.length; i++)
	    {
	        strCode = str.charCodeAt(i);
	        if ((strCode > 65248) || (strCode == 12288))
	        {
	            return true;
	        }
	    }
	    return false;
	}
	
	//是否含有中文（也包含日文和韩文） onkeyup='toUpperCase("licenseNo")'
	function isChineseChar(str){     
	   var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;  
	   return reg.test(str);  
	}  
	//是否含有非法字符
	function isillegalCharacter(str){
	    
	    var patrn=/[`~!@#$%^&*()+<>?:"{},.\/;'[\]]/im;  
	                          
	    return patrn.test(str); 
	}

	function validateNumber(payCount){
	    var reg = /^\d+(\.\d+)?$/;
	   	if(!reg.test(payCount)){
	        alert("消费金额请输入数字!");
	        return false;
	    }
	    return true;
	}
	var checkSubmitFlg = false; 
	function createHairPay(){//
		if(checkSubmitFlg == false){
			if (document.hairForm.payCount.value == "") {
				alert("亲，请输入您的支付金额吧");
			}else if(document.hairForm.USER_MEMO.value == ""){
				alert("亲，请输入您本次支付的备注信息");
			}else if(document.hairForm.SOURCE_TYPE.value == ""){
				alert("亲，请输入您所支付的类别");
			}else {
				alert("支付成功后给收费员确认！");
				if(validateNumber(document.hairForm.payCount.value)){
					checkSubmitFlg = true;
					document.getElementById("submitButt").onclick = function(){};
					var param = document.hairForm.USER_MEMO.value+"※"+document.hairForm.payCount.value+"※"+document.hairForm.SOURCE_TYPE.value;
					document.forms[0].action= "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx99a2e4e8ebedbc54"
						+"&redirect_uri=http://ipoc.grguser.com/pywx"
						+"/pay&response_type=code&scope=snsapi_base&state="+param+"#wechat_redirect";
					document.forms[0].submit();
				}
			}
		}
		
		
	}
</script>

<title>平云广场费用支付</title>
</head>
<body>
	<section class="main">
		<br>
		<center>
        	<p>温馨提示：请输入金额后进入下一步</p>
        </center>
        
       
		<!-- 输入美发内容[[ -->
		<form id="hairForm" name="hairForm" action="${ctx}/weixin/parking/toPayView" method="post" role="form" class="form lpn-form">
			<p style="color: red" id="msg"></p>
		    <input type="hidden" name="openId" value="">
		    <input type="hidden" name="licenseNumber" value="">
			<input type="hidden" name="usualCar" value="1" >
			<fieldset>
				
				<div class="input-area">
					
					<dl class="form-line">
						<dt class="label">支付金额</dt>
						<dd class="element lpn-element">
							<input class="text" type="text" value="" name="payCount" id="payCount" placeholder="请输入支付金额" maxlength="6" >
						</dd>
					</dl>
					<dl class="form-line">
						<dt class="label">支付类别</dt>
						<select name="SOURCE_TYPE" id="SOURCE_TYPE" class="element lpn-element" placeholder="请输入支付类别" style="padding-left:68px">
								<option value="">请输入支付类别</option>
								<option value="物业管理费">物业管理费</option>
								<option value="月租车费">月租车费</option>
								<option value="临时停车费">临时停车费</option>
								<option value="有偿服务费">有偿服务费</option>
								<option value="维护服务费">维护服务费</option>
							  	<option value="清洁服务费">清洁服务费</option>
								<option value="保安服务费">保安服务费</option>
								<option value="特约服务费">特约服务费</option>
							  	</select>
					</dl>
					<dl class="form-line">
						<dt class="label">支付备注</dt>
						<dd class="element lpn-element">
							<input class="text" type="text" value="" name="USER_MEMO" id="USER_MEMO" placeholder="请输入本次支付备注" maxlength="20" >
						</dd>
					</dl>
				</div>

			</fieldset>
		</form>
		<a href="#" id="submitButt" class="btn btn-green form-btn" onclick = "createHairPay();">下一步</a>
        </section>
        
        
	<div class="footer">
	<span>Copyright ? 2016 广电物业. All Rights Reserved.<br>由广电物业技术支持</span>
	</div>		

</body>
</html>
