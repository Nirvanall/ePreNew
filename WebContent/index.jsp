<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<base href="/ePreNew/" />
<title>Welcome to ePre</title>

<link rel="stylesheet" type="text/css" href="css/semantic.min.css" />
<link rel="stylesheet" type="text/css" href="css/page.css" />
<style type="text/css">
#row1 .segment{
	max-width: 360px;
	width: 360px;
}
table{
	margin-left: auto;
	margin-right: auto;
	border-collapse: separate;
	border-spacing: 10px;
}
td{
	vertical-align: top;
}
</style>

<script type="text/javascript" src="javascript/jquery.js"></script>
<script type="text/javascript" src="javascript/jquery.form.js"></script>
<script type="text/javascript" src="javascript/semantic.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var login_form = $("#login-form");
	if(1 == login_form.length){
		login_form.form({
			fields: {
				user: {
					identifier: "user",
					rules: [{
						type   : "empty",
						prompt : "Please enter your user ID"
					}]
				},
				password: {
					identifier : "password",
					rules: [{
						type   : "empty",
						prompt : "Please enter a password"
					}]
				}
			}
		});
	}
});
</script>
</head>

<body>
<article class="ui container page">
	<section id="page-header">
		<a href="http://www.polyu.edu.hk" ><img src="images/polyu_logo.jpg" class="logo" /></a>
		<a href="http://eie.polyu.edu.hk" ><img src="images/eie_logo.jpg" class="logo" /></a>
		<img src="images/logo.png" class="logo" />
	</section>
	<div class="ui divider"></div>
	
	<section id="page-content">
	<table>
		<tr id="row1">
			<td>
				<div class="ui blue segment" id="login-segment">
					<h3 class="ui dividing header">Login</h3>
					<form action="login.do" method="post" class="ui form" id="login-form">
						<div class="required field">
							<label class="ui left aligned header">User ID</label>
							<div class="ui left icon input">
								<i class="user icon"></i>
								<input type="text" name="user" required="required" maxlength="16" />
							</div>
						</div>
						<div class="required field">
							<label class="ui left aligned header">Password</label>
							<div class="ui left icon input">
								<i class="lock icon"></i>
								<input type="password" name="password" required="required" maxlength="32" />
							</div>
						</div>
						<div class="field">
							<div class="ui checkbox">
								<input type="checkbox" name="remember" />
								<label>Remember Me</label>
							</div>
						</div>
						<div class="ui error message"></div>
						<div>
							<button class="ui blue submit button">Login</button>
						</div>
					</form>
				</div>
			</td>
			<td>
				<div class="ui blue segment">
					<h3 class="ui dividing header">Useful Links</h3>
					<div class="ui vertical text menu">
						<a class="item" href="http://www.polyu.edu.hk" >PolyU Main Page</a>
						<a class="item" href="http://www.polyu.edu.hk/feng/" >FENG Main Page</a>
						<a class="item" href="http://eie.polyu.edu.hk" >EIE Main Page</a>
						<a class="item" href="https://www.lib.polyu.edu.hk" >PolyU Library</a>
						<a class="item" href="https://www38.polyu.edu.hk/eStudent/" >PolyU eStudent</a>
						<a class="item" href="https://learn.polyu.edu.hk" >PolyU Blackboard</a>
						<a class="item" href="https://www.outlook.com/connect.polyu.hk" >PolyU Connect</a>
						<a class="item" href="http://www.polyu.edu.hk/sao/CDP/" >PolyU CDP</a>
						<a class="item" href="https://www40.polyu.edu.hk/saosport/" >Sports Facilities</a>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div class="ui blue segment">
					<h3 class="ui dividing header">Announcements</h3>
					
				</div>
			</td>
		</tr>
	</table>
	</section>
	
	<div id="page-footer" class="ui inverted blue segment">Copyright &copy; 2015 The Hong Kong Polytechnic University. All Rights Reserved</div>
</article>
</body>
</html>