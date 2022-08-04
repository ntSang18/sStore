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

	$(".show-hide-pass a").on("click", function (event) {
		event.preventDefault();
		if ($(".show-hide-pass input").attr("type") == "text") {
			$(".show-hide-pass input").attr("type", "password");
			$(".show-hide-pass i").addClass("bx-hide");
			$(".show-hide-pass i").removeClass("bx-show");
		} else if ($(".show-hide-pass input").attr("type") == "password") {
			$(".show-hide-pass input").attr("type", "text");
			$(".show-hide-pass i").addClass("bx-show");
			$(".show-hide-pass i").removeClass("bx-hide");
		}
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
	$("#btn-edit-save").click(function (event) {
		event.preventDefault();
		if ($(this).attr("class") == "edit") {
			$(this).removeClass("edit");
			$(this).addClass("save");
			$(this).html("Save Information");
			$("#form-user-info").prop("disabled", false);
		} else if ($(this).attr("class") == "save") {
			if (!$("#form-edit-user input[name='userName']").val()) {
				Toast.fire({
					icon: "warning",
					title: "User name is empty!",
				});
			} else if (!$("#form-edit-user input[name='password']").val()) {
				Toast.fire({
					icon: "warning",
					title: "Password is empty!",
				});
			} else if (!$("#form-edit-user input[name='email']").val()) {
				Toast.fire({
					icon: "warning",
					title: "Email is empty!",
				});
			} else if (!$("#form-edit-user input[name='phoneNumber']").val()) {
				Toast.fire({
					icon: "warning",
					title: "PhoneNumber is empty!",
				});
			} else if (!$("#form-edit-user select[name='province']").val()) {
				Toast.fire({
					icon: "warning",
					title: "Province is empty!",
				});
			} else if (!$("#form-edit-user select[name='district']").val()) {
				Toast.fire({
					icon: "warning",
					title: "District is empty!",
				});
			} else if (!$("#form-edit-user select[name='ward']").val()) {
				Toast.fire({
					icon: "warning",
					title: "Ward is empty!",
				});
			} else if (
				!$("#form-edit-user input[name='detailAddress']").val()
			) {
				Toast.fire({
					icon: "warning",
					title: "Province is empty!",
				});
			} else {
				var data = $("#form-edit-user").serialize();
				var href = "/sStore/edit-user";
				$.post(href, data, function (message, status) {
					if (status == "success") {
						Toast.fire({
							icon: "success",
							title: message,
						});
					} else {
						Toast.fire({
							icon: "error",
							title: message,
						});
					}
				});
				$(this).removeClass("save");
				$(this).addClass("edit");
				$(this).html("Edit Information");
				$("#form-user-info").prop("disabled", true);
			}
		}
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
				if (order.items[i].status == 4) {
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
