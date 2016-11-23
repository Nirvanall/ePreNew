$(document).ready(function(){
	$("#password-form").form({
		fields: {
			old_password: {
				identifier : "old_password",
				rules: [{
					type   : "empty",
					prompt : "Please enter your old password"
				}]
			},
			new_password: {
				identifier : "new_password",
				rules: [{
					type   : "empty",
					prompt : "Please enter a new password"
				}]
			},
			repeat_password: {
				identifier : "repeat_password",
				rules: [{
					type   : "match[new_password]",
					prompt : "The repeat password should be the same as the new password"
				}]
			}
		},
		onSuccess: function (event, fields) {
			$.ajax({
				url: "password.do",
				method: "POST",
				data: fields,
				dataType: "json",
				success: function (data, status) {
					if (0 == data.code) {
						$("#message").toggleClass("error", false).toggleClass("success", true);
					} else {
						$("#message").toggleClass("success", false).toggleClass("error", true);
					}
					$("#message").html(data.desc);
				},
				error: function () {
					$("#message").toggleClass("success", false).toggleClass("error", true)
						.html("Network Request Error");
				}
			});
			return false;
		}
	});
	
});