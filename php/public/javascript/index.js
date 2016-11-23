$(document).ready(function(){
    "use strict";

	var dateFormatter = new DateFormatter();
    var announcementList = new Vue({
        "el": "#announcement_div",
        "data": {
            "loading": "active",
            "hasAnnouncement": false,
            "hasMore": false,
            "announcements": new Array()
        },
        "created": function () {
            $.ajax({
                "url": "announcement/list.do",
                "method": "GET",
                "data": {"page": 1, "size": 6},
                "dataType": "json",
                "error": function () {
                },
                "success": function (ret) {
                    if (0 === ret.code) {
                        var list = ret.data.list;
                        announcementList.hasAnnouncement = (list.length > 0);
                        announcementList.hasMore = (list.length > 5);
                        for (var i = 0; i < list.length && i < 5; ++i) {
							list[i].create_time = dateFormatter.formatDate(
									new Date(list[i].create_time), 'Y-m-d H:i');
                            announcementList.announcements.push(list[i]);
                        }
                        announcementList.loading = "";
                    }
                }
            });
        },
		methods: {
			toggleAnnouncement: function (event) {
				var $thiz = $(event.currentTarget);
				$thiz.siblings("div").slideToggle();
				$thiz.find(".angle.icon").toggleClass("up").toggleClass("down");
			}
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
	
});
