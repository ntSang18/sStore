$(document).ready(function () {
	// SweetAlert2
	const Toast = Swal.mixin({
		toast: true,
		position: "top-end",
		showConfirmButton: false,
		timer: 3000,
		timerProgressBar: true,
	});
	// ------------------------------Navbar------------------------------//
	if ($("#bar")) {
		$("#bar").click(function () {
			$("#navbar").addClass("active");
		});
	}

	if ($("#close")) {
		$("#close").click(function () {
			$("#navbar").removeClass("active");
		});
	}
	//---------------------------Login/Register--------------------------//
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
		var data_province = $("#province1").data("province");
		if (data_province != "" || data_province != null) {
			$("#province1").val(data_province).change();
		}
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
			var data_district = $("#district1").data("district");
			if (data_district != "" || data_district != null) {
				$("#district1").val(data_district).change();
			}
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
			var data_ward = $("#ward1").data("ward");
			if (data_ward != "" || data_ward != null) {
				$("#ward1").val(data_ward).change();
			}
		});
	});

	// ------------------------------Shop------------------------------- //
	$("#form-filter input[name='search']").keypress(function (event) {
		if (event.which === 13) {
			var pageNum = $("#form-filter input[name='pageNum']").val();
			var gender = $("#form-filter input[name='gender']").val();
			var type = $("#form-filter input[name='type']").val();
			var search = $(this).val();
			var href =
				"/sStore/shop?pageNum=" +
				pageNum +
				"&gender=" +
				gender +
				"&type=" +
				type +
				"&search=" +
				search;
			window.location.href = href;
		}
	});
	// -------------------------Single Product---------------------------//
	var rating = $("#number-rating").data("rate");
	var starsTotal = 5;
	var starPercent = `${(rating / starsTotal) * 100}%`;

	var stars = $(".stars").data("rate");
	let inner = "";
	if (stars == 1) {
		inner +=
			"<i class='bx bxs-star' ></i> <i class='bx bx-star' ></i> <i class='bx bx-star' ></i> <i class='bx bx-star' ></i> <i class='bx bx-star' ></i>";
	} else if (stars == 2) {
		inner +=
			"<i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bx-star' ></i> <i class='bx bx-star' ></i> <i class='bx bx-star' ></i>";
	} else if (stars == 3) {
		inner +=
			"<i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bx-star' ></i> <i class='bx bx-star' ></i>";
	} else if (stars == 4) {
		inner +=
			"<i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bx-star' ></i>";
	} else if (stars == 5) {
		inner +=
			"<i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i> <i class='bx bxs-star' ></i>";
	}
	$(".stars").html(inner);

	$("#inner").css("width", starPercent);

	$(".swatch-element.color label span").each(function () {
		$(this).css(
			"background-image",
			"url(../static/color/" + this.id + ".png)"
		);
	});

	$(".swatch-element .color").click(function () {
		$(".swatch-element .color").each(function () {
			$(this).removeClass("sd");
		});
		$(".select-container.color .select-header span").html(this.id);
		$(this).addClass("sd");
	});

	$(".swatch-element .size").click(function () {
		$(".swatch-element .size").each(function () {
			$(this).removeClass("sd");
		});
		$(".select-container.size .select-header span").html(this.id);
		$(this).addClass("sd");
	});

	$("#add-to-cart").click(function (event) {
		event.preventDefault();
		var color = $("input[name='color']:checked").val();
		var size = $("input[name='size']:checked").val();
		var quantity = $("input[name='quantity']").val();
		var productId = $("input[name='product-id']").val();
		var url = "/sStore/add-to-cart";
		if (!color || !size) {
			Toast.fire({
				icon: "warning",
				title: "Size or color are empty",
			});
		} else {
			var data = {
				color: color,
				size: size,
				quantity: quantity,
				productId: productId,
			};
			$.post(url, data, function (message, status) {
				if (status == "success") {
					Toast.fire({
						icon: "success",
						title: "Add to cart successfully!",
					});
				} else {
					Toast.fire({
						icon: "error",
						title: "Add to cart failed!",
					});
				}
			});
		}
	});

	//-----------------------------Account-------------------------------//

	$("#change-address").click(function (event) {
		event.preventDefault();
		$("#change-address-modal").modal("show");
	});

	$("#vision1").on("click", function (event) {
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
				type: 1,
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
				type: 1,
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
			type: 1,
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

	$(".order").click(function (event) {
		event.preventDefault();
		var id = $(this).data("id");
		var href = "/sStore/view-order?id=" + id;
		$.get(href, function (order, status) {
			$("#orders-view-order #total-quantity").html(
				"(" + order.totalQuantity + " products)"
			);
			let items = "";
			for (let i = 0; i < order.items.length; i++) {
				// Product already review
				if (order.items[i].status == 5 || order.items[i].status < 4) {
					items += `<div class="order-item" data-id="${
						order.items[i].product.id
					}">
							<img
								src=${order.items[i].product.images[0].image}
							/>
							<div class="order-item-info">
								<span>${order.items[i].product.productName}</span>
								<span>${order.items[i].product.branch}</span>
								<div class="order-item-info-variants">
									<label class="color">
										<span id="${order.items[i].id}-${order.items[i].color}">Kem</span>
									</label>
									<div class="size">${order.items[i].size}</div>
								</div>
								<div class="order-item-info-price price">
									${order.items[i].quantity * order.items[i].product.price}
								</div>
							</div>
							<div class="order-item-info-quantity">
								<span>QUANTITY</span>
								<span> ${order.items[i].quantity} </span>
							</div>
						</div>`;
				} else {
					items += `<div class="order-item" data-id="${
						order.items[i].product.id
					}">
							<img
								src=${order.items[i].product.images[0].image}
							/>
							<div class="order-item-info">
								<span>${order.items[i].product.productName}</span>
								<span>${order.items[i].product.branch}</span>
								<div class="order-item-info-variants">
									<label class="color">
										<span id="${order.items[i].id}-${order.items[i].color}">Kem</span>
									</label>
									<div class="size">${order.items[i].size}</div>
								</div>
								<div class="order-item-info-price price">
									${order.items[i].quantity * order.items[i].product.price}
								</div>
							</div>
							<div class="order-item-info-quantity">
								<span>QUANTITY</span>
								<span> ${order.items[i].quantity} </span>
								<span class="review" data-id="${order.items[i].id}">Review</span>
							</div>
						</div>`;
				}
			}

			$("#orders-view-order .order-items-list").html(items);
			$("#orders-view-order #order-btnConfirm").attr(
				"href",
				"/sStore/admin/confirmOrder?id=" + order.id
			);
			$("#orders-view-order #order-btnDel").attr(
				"href",
				"/sStore/admin/delOrder/" + order.id
			);
			$("#orders-view-order").modal("show");
			$(".color span").each(function () {
				var color = this.id.split("-")[1];
				$(this).css(
					"background-image",
					"url(../static/color/" + color + ".png)"
				);
			});
			new AutoNumeric.multiple(".price", {
				decimalPlaces: "0",
				decimalCharacter: ",",
				digitGroupSeparator: ".",
				currencySymbol: "₫",
				currencySymbolPlacement: "s",
			});
			$(".order-item").click(function (event) {
				event.preventDefault();
				var id = $(this).data("id");
				href = "/sStore/sproduct?id=" + id;
				window.location.href = href;
			});
			$(".review").click(function (event) {
				event.stopPropagation();
				var id = $(this).data("id");
				$("#review-order-modal .post").attr("data-id", id);
				$("input:radio[name='rate']").prop("checked", false);
				$("textarea[name='review']").val("");
				$("#header-review").html("&nbsp;");
				$("#review-order-modal").modal("show");
				$("#orders-view-order").modal("hide");
			});
		});
	});

	$("input:radio[name='rate']").change(function () {
		if ($(this).val() == 1) {
			$("#header-review").html("I just hate it 😠");
		}
		if ($(this).val() == 2) {
			$("#header-review").html("I don't like it 😒");
		}
		if ($(this).val() == 3) {
			$("#header-review").html("It is awesome 😊");
		}
		if ($(this).val() == 4) {
			$("#header-review").html("I just like it 😎");
		}
		if ($(this).val() == 5) {
			$("#header-review").html("I just love it 😍");
		}
	});

	$("#review-order-modal .post").click(function (event) {
		var id = $(this).data("id");
		var href = "/sStore/review-product?piid=" + id;
		if (!$("input:radio[name='rate']").is(":checked")) {
			Toast.fire({
				icon: "warning",
				title: "Please rate the product!",
			});
		} else {
			var star = $("input:radio[name='rate']").val();
			var review = $("textarea[name='review']").val();
			var data = {
				star: star,
				review: review,
			};
			$.post(href, data, function (message, status) {
				location.reload();
			});
		}
	});

	//------------------------------Cart---------------------------------//
	$(".del-cart-item").click(function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		Swal.fire({
			title: "Are you sure?",
			text: "You won't be able to revert this!",
			icon: "warning",
			showCancelButton: true,
			confirmButtonColor: "#3085d6",
			cancelButtonColor: "#d33",
			confirmButtonText: "Yes, delete it!",
		}).then((result) => {
			if (result.isConfirmed) {
				$.get(href, function (status) {
					location.reload();
				});
			}
		});
	});

	$("#checkout").click(function (event) {
		event.preventDefault();
		var url = "/sStore/newOrder";
		$.get(url, function (status) {
			location.reload();
			if (status == "success") {
				Toast.fire({
					icon: "success",
					title: "Order is in process!",
				});
			} else {
				Toast.fire({
					icon: "error",
					title: "Error processing order!",
				});
			}
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

const sproduct = (element) => {
	window.location.href = "/sStore/sproduct?id=" + element.id;
};
