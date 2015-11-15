$(document).ready(function(){
	$("#login-form").form({
		fields: {
			user: {
				identifier : "user",
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
	$(".announcement_header").click(function toggleAnnouncement() {
		$(this).siblings("div").slideToggle();
		$(this).find(".angle.icon").toggleClass("up").toggleClass("down");
	});
});