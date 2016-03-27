/**
 * 
 */
$(document).ready(function () {
	var $highlighted;
	var handlingClicking = false;
	var videoPlayer = document.getElementById("video_player");
	var $commentList = $("#comment_list");
	var $commentInput = $("#comment");
	var $commentButton = $("#comment_button");

	$("#video_player").on("timeupdate", function () {
		if (handlingClicking) return;
		var playTime = this.currentTime;
		for (var $li = $commentList.find("li").last();
				1 == $li.length; $li = $li.prev()) {
			if ($li.data("time") <= playTime) {
				if ($highlighted) $highlighted.removeClass("highlighted");
				$highlighted = $li.addClass("highlighted");
				// TODO: move the comment so that it can be seen on the list
				break;
			}
		}
	});
	$commentList.find("li").click(function () {
		var $this = $(this);
		handlingClicking = true;
		videoPlayer.currentTime = $this.data("time");
		if ($highlighted) $highlighted.removeClass("highlighted");
		$highlighted = $this.addClass("highlighted");
		// TODO: move the comment so that it can be seen on the list
		handlingClicking = false;
	});

	$(".comment_edit").click(function () {
		// Editing a comment = Deleting the comment + Making a new comment based on it
		var $this = $(this), $p = $this.parent(), $li = $p.parent();
		var commentId = $p.data("id");
		
		// Set the playtime according to the comment
		videoPlayer.currentTime = $li.data("time");
		
		// Set the value of the comment textarea to the content of the comment
		$commentInput.val($li.find(".comment_content").text());
		
		// Delete the comment
		$.ajax({
			url: "comment/delete.do",
			method: "POST",
			data: {id: commentId},
			dataType: "json",
			success: function (json) {
				if (typeof(json.code) == "number" && 0 == json.code) {
					$li.remove();
				} else {
					alert("Fail to remove the original comment");
				}
			},
			error: function () {
				alert("Fail to remove the original comment");
			}
		});
	});
	$(".comment_delete").click(function () {
		var $this = $(this), $p = $this.parent(), $li = $p.parent();
		var commentId = $p.data("id");
		$.ajax({
			url: "comment/delete.do",
			method: "POST",
			data: {id: commentId},
			dataType: "json",
			success: function (json) {
				if (typeof(json.code) == "number" && 0 == json.code) {
					$li.remove();
				} else {
					alert("Fail to delete the comment");
				}
			},
			error: function () {
				alert("Fail to delete the comment");
			}
		});
	});
	
	$commentInput.on("input", function () {
		var $this = $(this);
		if ($this.val().length <= 0) {
			$commentButton.attr("disabled", "disabled");
		} else {
			$commentButton.removeAttr("disabled");
		}
	});
	$commentButton.click(function () {
		$.ajax({
			url: "comment/create.do",
			method: "POST",
			data: {
				video_id: $("#video_title").data("id"),
				playtime: videoPlayer.currentTime,
				content: $commentInput.val()
			},
			dataType: "json",
			success: function (json) {
				if (typeof(json.code) == "number" && 0 == json.code) {
					// TODO: insert an item in $commentList
				} else {
					alert("Fail to make the comment");
				}
			},
			error: function () {
				alert("Fail to make the comment");
			}
		})
	});
});