$(document).ready(function () {
	alert("hello");
	$(".orders1").click(function (event) {
		event.preventDefault();
		$("#order-detail-modal").modal("show");
	});

	$("#change-address").click(function (event) {
		event.preventDefault();
		$("#change-address-modal").modal("show");
	});

	$("#vision1").on("click", function (event) {
		event.preventDefault();
		if ($("#current-password").attr("type") == "text") {
			$("#current-password").attr("type", "password");
			$(this).addClass("bx-hide");
			$(this).removeClass("bx-show");
		} else if ($("#current-password").attr("type") == "password") {
			$("#current-password").attr("type", "text");
			$(this).addClass("bx-show");
			$(this).removeClass("bx-hide");
		}
	});

	$("#vision2").on("click", function (event) {
		event.preventDefault();
		if ($("#new-password").attr("type") == "text") {
			$("#new-password").attr("type", "password");
			$(this).addClass("bx-hide");
			$(this).removeClass("bx-show");
		} else if ($("#new-password").attr("type") == "password") {
			$("#new-password").attr("type", "text");
			$(this).addClass("bx-show");
			$(this).removeClass("bx-hide");
		}
	});

	$("#vision3").on("click", function (event) {
		event.preventDefault();
		if ($("#repeat-password").attr("type") == "text") {
			$("#repeat-password").attr("type", "password");
			$(this).addClass("bx-hide");
			$(this).removeClass("bx-show");
		} else if ($("#repeat-password").attr("type") == "password") {
			$("#repeat-password").attr("type", "text");
			$(this).addClass("bx-show");
			$(this).removeClass("bx-hide");
		}
	});

	$("#form-search input[name='search']").keypress(function (event) {
		if (event.which === 13) {
			alert("hello");
			$("#form-search").submit();
		}
	});
});
