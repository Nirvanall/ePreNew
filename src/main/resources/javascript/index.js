$(document).ready(function(){
    "use strict";

    var announcementList = new Vue({
        "el": "announcement_div",
        "data": {
            "loading": "active",
            "hasAnnouncement": false,
            "hasMore": false,
            "announcements": new Array()
        },
        "created": function () {
            $.ajax({
                "url": "announcement/list",
                "method": "GET",
                "data": {"start": 0, "count": 6},
                "dataType": "json",
                "error": function () {
                },
                "success": function (ret) {
                    if (0 === ret.code) {
                        var list = ret.data;
                        this.hasAnnouncement = (list.length > 0);
                        this.hasMore = (list.length > 5);
                        for (var i = 0; i < 5; ++i) {
                            this.announcements.push(list[i]);
                        }
                        this.loading = "";
                    }
                }
            });
        }
    });

	$("#login-form").form({
		"fields": {
			"user": {
				"identifier" : "user",
				"rules": [{
					"type"   : "empty",
					"prompt" : "Please enter your user ID"
				}]
			},
			"password": {
				"identifier" : "password",
				"rules": [{
					"type"   : "empty",
					"prompt" : "Please enter a password"
				}]
			}
		}
	});
	$(".announcement_header").click(function toggleAnnouncement() {
		$(this).siblings("div").slideToggle();
		$(this).find(".angle.icon").toggleClass("up").toggleClass("down");
	});
});
