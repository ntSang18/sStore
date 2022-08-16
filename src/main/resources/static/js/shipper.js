$(document).ready(function () {
	const Toast = Swal.mixin({
		toast: true,
		position: "top-end",
		showConfirmButton: false,
		timer: 3000,
		timerProgressBar: true,
	});

	var provinceCode = "";
	var districtCode = "";

	var url = "https://vapi.vnappmob.com/api/province/";

	// EXTRACT JSON DATA.
	$.getJSON(url, function (data) {
		console.log(data);
		$.each(data.results, function (index, value) {
			// APPEND OR INSERT DATA TO SELECT ELEMENT. Set the province code in the id section rather than in the value.
			$(".province").append(
				'<option id="' +
					value.province_id +
					'" value="' +
					value.province_name +
					'">' +
					value.province_name +
					"</option>"
			);
		});
	});
	//Province selected --> update district list .
	$(".province").change(function () {
		// selectedProvince = this.options[this.selectedIndex].text;
		provinceCode = $(this).children(":selected").attr("id");
		var url =
			"https://vapi.vnappmob.com//api/province/district/" + provinceCode;
		$.getJSON(url, function (data) {
			$(".district option").remove();
			console.log(data);
			$.each(data.results, function (index, value) {
				// APPEND OR INSERT DATA TO SELECT ELEMENT.
				$(".district").append(
					'<option id="' +
						value.district_id +
						'" value="' +
						value.district_name +
						'">' +
						value.district_name +
						"</option>"
				);
			});
		});
	});
	//District selected --> update ward list .
	$(".district").change(function () {
		// selectedDistrict = this.options[this.selectedIndex].text;
		districtCode = $(this).children(":selected").attr("id");
		var url = "https://vapi.vnappmob.com/api/province/ward/" + districtCode;
		$.getJSON(url, function (data) {
			$(".ward option").remove();
			console.log(data);
			$.each(data.results, function (index, value) {
				// APPEND OR INSERT DATA TO SELECT ELEMENT.
				$(".ward").append(
					'<option id="' +
						value.ward_id +
						'" value="' +
						value.ward_name +
						'">' +
						value.ward_name +
						"</option>"
				);
			});
		});
	});

	$("#change-address").click(function (event) {
		event.preventDefault();
		$("#change-address-modal").modal("show");
	});

	$("#vision1").on("click", function () {
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

	$("#vision2").on("click", function () {
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

	$("#vision3").on("click", function () {
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

	$("#form-search-1 input[name='search']").keypress(function (event) {
		if (event.which === 13) {
			$("#form-search-1").submit();
		}
	});

	$("#form-search-2 input[name='search']").keypress(function (event) {
		if (event.which === 13) {
			$("#form-search-2").submit();
		}
	});

	$("#sort-date-1").click(function (event) {
		var oldSortMode = $(this).data("sort");
		var search = $("#form-search-1 input[name='search']").val();
		var pageNum = $("#form-search-1 input[name='pageNum']").val();
		var newSortMode = 0;
		if (oldSortMode == 1) {
			//Đang giảm dần nhấn 1 lần nữa -> tăng dần
			newSortMode = 2;
		} else if (oldSortMode == 2) {
			//Đang giảm dần nhấn 1 lần nữa -> giảm dần
			newSortMode = 1;
		} else {
			// Đang chưa nhấn lần nào
			newSortMode = 1;
		}
		window.location.href =
			"/sStore/shipper-available-orders?pageNum=" +
			pageNum +
			"&search=" +
			search +
			"&sort=" +
			newSortMode;
	});

	$("#sort-date-2").click(function (event) {
		var oldSortMode = $(this).data("sort");
		var search = $("#form-search-2 input[name='search']").val();
		var pageNum = $("#form-search-2 input[name='pageNum']").val();
		var newSortMode = 0;
		if (oldSortMode == 1) {
			//Đang giảm dần nhấn 1 lần nữa -> tăng dần
			newSortMode = 2;
		} else if (oldSortMode == 2) {
			//Đang giảm dần nhấn 1 lần nữa -> giảm dần
			newSortMode = 1;
		} else {
			// Đang chưa nhấn lần nào
			newSortMode = 1;
		}
		window.location.href =
			"/sStore/shipper-my-orders?pageNum=" +
			pageNum +
			"&search=" +
			search +
			"&sort=" +
			newSortMode;
	});

	$("#sort-price-1").click(function (event) {
		var oldSortMode = $(this).data("sort");
		var search = $("#form-search-1 input[name='search']").val();
		var pageNum = $("#form-search-1 input[name='pageNum']").val();
		var newSortMode = 0;
		if (oldSortMode == 3) {
			//Đang giảm dần nhấn 1 lần nữa -> tăng dần
			newSortMode = 4;
		} else if (oldSortMode == 4) {
			//Đang giảm dần nhấn 1 lần nữa -> giảm dần
			newSortMode = 3;
		} else {
			// Đang chưa nhấn lần nào
			newSortMode = 3;
		}
		window.location.href =
			"/sStore/shipper-available-orders?pageNum=" +
			pageNum +
			"&search=" +
			search +
			"&sort=" +
			newSortMode;
	});

	$("#sort-price-2").click(function (event) {
		var oldSortMode = $(this).data("sort");
		var search = $("#form-search-2 input[name='search']").val();
		var pageNum = $("#form-search-2 input[name='pageNum']").val();
		var newSortMode = 0;
		if (oldSortMode == 3) {
			//Đang giảm dần nhấn 1 lần nữa -> tăng dần
			newSortMode = 4;
		} else if (oldSortMode == 4) {
			//Đang giảm dần nhấn 1 lần nữa -> giảm dần
			newSortMode = 3;
		} else {
			// Đang chưa nhấn lần nào
			newSortMode = 3;
		}
		window.location.href =
			"/sStore/shipper-my-orders?pageNum=" +
			pageNum +
			"&search=" +
			search +
			"&sort=" +
			newSortMode;
	});

	$(".view-order").click(function () {
		var id = $(this).data("id");
		var view_mode = $(this).data("mode");
		var href = "/sStore/view-order?id=" + id;
		$.get(href, function (order) {
			$("#order-detail-createAt").html(order.createAt);
			$("#order-detail-totalPrice").html(order.totalPrice);
			$("#order-detail-userName").html(order.user.userName);
			$("#order-detail-phoneNumber").html(order.user.phoneNumber);
			$("#order-detail-province").html(order.user.address.province);
			$("#order-detail-district").html(order.user.address.district);
			$("#order-detail-ward").html(order.user.address.ward);
			$("#order-detail-detailAddress").html(
				order.user.address.detailAddress
			);
			$;
			let items = "";
			for (let i = 0; i < order.items.length; i++) {
				items += `<tr>
							<td>${order.items[i].product.productName}</td>
							<td class="price">${order.items[i].product.price}</td>
							<td>${order.items[i].quantity}</td>
							<td class="price">${order.items[i].product.price * order.items[i].quantity}</td>
						</tr>`;
			}
			$("#order-detail-items").html(items);
			if (view_mode == 1) {
				$("#take-order").attr("data-id", order.id);
				$("#delivered-order").addClass("hide");
			} else if (view_mode == 2) {
				$("#delivered-order").attr("data-id", order.id);
				$("#take-order").addClass("hide");
			}
			new AutoNumeric.multiple(".price", {
				decimalPlaces: "0",
				decimalCharacter: ",",
				digitGroupSeparator: ".",
				currencySymbol: "₫",
				currencySymbolPlacement: "s",
			});
		});
		$("#order-detail-modal").modal("show");
	});

	$(".take").click(function (event) {
		event.stopPropagation();
	});

	$(".delivered").click(function (event) {
		event.stopPropagation();
	});

	$("#take-order").click(function () {
		var id = $(this).data("id");
		var href = "/sStore/shipper-take-order?id=" + id;
		window.location.href = href;
	});

	$("#delivered-order").click(function () {
		var id = $(this).data("id");
		var href = "/sStore/shipper-delivered-order?id=" + id;
		window.location.href = href;
	});

	$("#change-email").click(function () {
		if ($("#email").prop("disabled")) {
			$("#email").prop("disabled", false);
			$(this).addClass("bx-check-square");
			$(this).removeClass("bx-edit");
		} else {
			var email = $("#email").val();
			var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
			if (email.trim() == "" || emailReg.test(email) == false) {
				Toast.fire({
					icon: "warning",
					title: "Invalid email!",
				});
				return;
			}
			var href = "/sStore/change-email";
			var data = {
				type: 3,
				email: email,
			};
			$.post(href, data)
				.done(function (data) {
					Toast.fire({
						icon: "success",
						title: data.message,
					});
					$("#email").val(data.email);
					$("#email").prop("disabled", true);
					$("#change-email").removeClass("bx-check-square");
					$("#change-email").addClass("bx-edit");
				})
				.fail(function () {
					Toast.fire({
						icon: "error",
						title: "Email taken!",
					});
				});
		}
	});

	$("#change-phone").click(function () {
		if ($("#phoneNumber").prop("disabled")) {
			$("#phoneNumber").prop("disabled", false);
			$(this).addClass("bx-check-square");
			$(this).removeClass("bx-edit");
		} else {
			var phone = $("#phoneNumber").val();
			var phoneReg =
				/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im;
			if (phone.trim() == "" || phoneReg.test(phone) == false) {
				Toast.fire({
					icon: "warning",
					title: "Invalid phone number!",
				});
				return;
			}
			var href = "/sStore/change-phone";
			var data = {
				type: 3,
				phone: phone,
			};
			$.post(href, data)
				.done(function (data) {
					Toast.fire({
						icon: "success",
						title: data.message,
					});
					$("#phoneNumber").val(data.phone);
					$("#phoneNumber").prop("disabled", true);
					$("#change-phone").removeClass("bx-check-square");
					$("#change-phone").addClass("bx-edit");
				})
				.fail(function () {
					Toast.fire({
						icon: "error",
						title: "Phone number taken!",
					});
				});
		}
	});

	$("#form-address").submit(function (event) {
		event.preventDefault();
		$.post($(this).attr("action"), $(this).serialize())
			.done(function (data) {
				$("#change-address-modal").modal("hide");
				$("#display-province").html(data.address.province);
				$("#display-district").html(data.address.district);
				$("#display-ward").html(data.address.ward);
				$("#display-detailAddress").html(data.address.detailAddress);
				Toast.fire({
					icon: "success",
					title: data.message,
				});
			})
			.fail(function () {
				Toast.fire({
					icon: "error",
					title: "Error",
				});
			});
	});

	$("#save-change-pass").click(function () {
		var currentPass = $("#current-password").val();
		var newPass = $("#new-password").val();
		var repeatPass = $("#repeat-password").val();
		var href = "/sStore/change-password";
		if (!(newPass === repeatPass)) {
			Toast.fire({
				icon: "warning",
				title: "Repeat password does not match the new password",
			});
			return;
		}
		var data = {
			type: 3,
			currentPass: currentPass,
			newPass: newPass,
		};
		$.post(href, data)
			.done(function (data) {
				$("#change-password-modal").modal("hide");
				Toast.fire({
					icon: "success",
					title: data.message,
				});
			})
			.fail(function () {
				Toast.fire({
					icon: "error",
					title: "Invalid current password!",
				});
			});
	});
});

new AutoNumeric.multiple(".price", {
	decimalPlaces: "0",
	decimalCharacter: ",",
	digitGroupSeparator: ".",
	currencySymbol: "₫",
	currencySymbolPlacement: "s",
});
