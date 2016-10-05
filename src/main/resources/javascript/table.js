$(document).ready(function () {
	$(".ui.search.icon.link").popup({
		position: "bottom center",
		inline: true,
		hoverable: true,
		delay: {
			show: 300,
			hide: 800
		},
		onVisible: function (element) {
			this.find('input:first').trigger('focus');
		}
	});
	
	$("td[data-id] > i[data-content]").popup({
		position: "top center",
		variation: "small"
	});
	
	$(".ui.edit.icon").on("click", function () {
		var id = $(this).parent("td").data("id");
		var base_url = window.location.href;
		var edit_url = $(this).parents("tbody").data("edit_url");
		if (typeof(edit_url) != "string") {
			base_url = base_url.substring(0, base_url.indexOf("?"));
			base_url = base_url.substring(0, base_url.lastIndexOf("/"));
			edit_url = base_url + "edit.do?id=" + id;
		} else if (edit_url.charAt(0) != "/") {
			base_url = base_url.substring(0, base_url.indexOf("?"));
			base_url = base_url.substring(0, base_url.lastIndexOf("/"));
			edit_url = base_url + edit_url + "?id=" + id;
		} else {
			var base_tags = document.getElementsByTagName("base");
			if (base_tags.length > 0) {
				base_url = base_tags[0].href;
				if (base_url.charAt(base_url.length - 1) == '/')
					base_url = base_url.substring(0, base_url.length - 1);
			} else {
				base_url = window.location.protocol + window.location.hostname;
				if (window.location.port != 80)
					base_url = base_url + ":" + window.location.port;
			}
			edit_url = base_url + edit_url + "?id=" + id;
		}
		
		window.location.href = edit_url;
	});
	
	$(".ui.trash.icon").on("click", function () {
		var id = $(this).parent("td").data("id");
		var base_url = window.location.href;
		var delete_url = $(this).parents("tbody").data("delete_url");
		if (typeof(delete_url) != "string") {
			base_url = base_url.substring(0, base_url.indexOf("?"));
			base_url = base_url.substring(0, base_url.lastIndexOf("/"))
			delete_url = base_url + "delete.do?id=" + id;
		} else if (delete_url.charAt(0) != "/") {
			base_url = base_url.substring(0, base_url.indexOf("?"));
			base_url = base_url.substring(0, base_url.lastIndexOf("/"))
			delete_url = base_url + delete_url + "?id=" + id;
		} else {
			var base_tags = document.getElementsByTagName("base");
			if (base_tags.length > 0) {
				base_url = base_tags[0].href;
				if (base_url.charAt(base_url.length - 1) == '/')
					base_url = base_url.substring(0, base_url.length - 1);
			} else {
				base_url = window.location.protocol + window.location.hostname;
				if (window.location.port != 80)
					base_url = base_url + ":" + window.location.port;
			}
			delete_url = base_url + delete_url + "?id=" + id;
		}
		
		$.ajax({
			url: delete_url,
			method: "POST",
			data: {id: id},
			dataType: "json",
			success: function () {
				
			},
			error: function () {
				
			}
		});
	});

	$("td.pagination_menu").each(function () {
		var $this = $(this);
		var $table = $this.parents("table");
		var tableHeads = $table.find("th");
		$this.attr("colspan", tableHeads.length);
		
		$this.find("button.jump_page").on("click", function() {
			$(document).setUrlParam("page",
					$(this).siblings("input[name='number_page']").val());
		});
		
		$this.find("button.first_page").on("click", function() {
			$(document).setUrlParam("page", 1);
		});
		
		$this.find("button.last_page").on("click", function() {
			$(document).setUrlParam("page",
					$(this).parent("td").find("input[name='number_page']").attr("max"));
		});
		
		$this.find("button.prev_page").on("click", function() {
			var currentPage = parseInt($(this).parent("td").data("page"));
			if (currentPage > 1) $(document).setUrlParam("page", currentPage - 1);
		});
		
		$this.find("button.next_page").on("click", function() {
			var $td = $(this).parent("td");
			var currentPage = parseInt($td.data("page"));
			var totalPage = parseInt($td.find("input[name='number_page']").attr("max"));
			if (currentPage < totalPage) $(document).setUrlParam("page", currentPage + 1);
		});
	});
	
});