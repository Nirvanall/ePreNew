<%@page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<base href="/ePreNew/" />
<title>Welcome to ePre</title>

<link rel="stylesheet" type="text/css" href="css/semantic.min.css" />
<style type="text/css">
.page{
	min-width: 800px;
}
.logo{
	margin-top: 5px;
	margin-left: 8px;
}
.segment{
	max-width: 360px;
	width: 360px;
}
table{
	margin-left: auto;
	margin-right: auto;
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
});
</script>
</head>

<body>
<article class="page">
	<section>
		<img src="images/polyu_logo.jpg" class="logo" />
		<img src="images/eie_logo.jpg" class="logo" />
		<img src="images/logo.png" class="logo" />
	</section>
	<div class="ui divider"></div>

	<table>
		<tr>
			<td>
				<div class="ui blue segment">
					<h3 class="ui dividing header">Login</h3>
					<form action="login.action" method="post" class="ui form" id="login-form">
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
						<div class="ui error message"></div>
						<div>
							<button class="ui blue submit button">Login</button>
						</div>
					</form>
				</div>
			</td>
			<td width="10"></td>
			<td>
				<div class="ui blue segment">
					<h3 class="ui dividing header">Useful Links</h3>
					<div class="ui vertical text menu">
						<a class="item" href="http://www.polyu.edu.hk" >PolyU Main Page</a>
						<a class="item" href="http://eie.polyu.edu.hk" >EIE Main Page</a>
						<a class="item" href="https://www.lib.polyu.edu.hk" >PolyU Library</a>
						<a class="item" href="https://www38.polyu.edu.hk/eStudent/" >PolyU eStudent</a>
						<a class="item" href="https://learn.polyu.edu.hk" >PolyU Blackboard</a>
					</div>
				</div>
			</td>
		</tr>
	</table>
</article>
</body>
</html>