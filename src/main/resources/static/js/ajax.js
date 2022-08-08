// AJAX
$(document).ready(function () {
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

	/*View Product*/
	$(".viewBtn").on("click", function (event) {
		event.preventDefault();
		// Clear Image of View Modal
		for (let i = 0; i < $(".small-img").length; i++) {
			$(".small-img:eq(" + i + ")").attr("src", "#");
		}
		//------------
		var href = $(this).attr("href");
		$.get(href, function (product, status) {
			// View Product Image
			$("#MainImg").attr(
				"src",
				"/static/uploads/product_images/" +
					product.id +
					"/" +
					product.images[0].imageName
			);
			let images = "";
			for (let i = 0; i < product.images.length; i++) {
				images += `<div class="small-img-col">
								<img
									src="/static/uploads/product_images/${product.id}/${product.images[i].imageName}"
									width="100%"
									class="small-img"
									onclick="showBigImage(this)"
								/>
							</div>`;
			}
			$(".small-img-group").html(images);
			// View Product Details
			if (product.gender) {
				$(".single-pro-details h6").html("Men / " + product.type);
			} else {
				$(".single-pro-details h6").html("Women / " + product.type);
			}
			$("#view_productName").html(product.productName);
			$("#view_available").html(product.available);
			$("#view_price").html(product.price);
			new AutoNumeric.multiple(".price", {
				decimalPlaces: "0",
				decimalCharacter: ",",
				digitGroupSeparator: ".",
				currencySymbol: "₫",
				currencySymbolPlacement: "s",
			});
			$("#view_sizes")
				.empty()
				.append('<option selected="selected" value="">Sizes</option>');
			for (let i = 0; i < product.sizes.length; i++) {
				$("#view_sizes").append(
					$("<option />").val("").text(product.sizes[i].size)
				);
			}
			$("#view_colors")
				.empty()
				.append('<option selected="selected" value="">Colors</option>');
			for (let i = 0; i < product.colors.length; i++) {
				$("#view_colors").append(
					$("<option />").val("").text(product.colors[i].color)
				);
			}
			$("#view_description").html(product.description);
		});
		$("#viewProduct").modal("show");
	});

	/* Edit Product */
	$(".editBtn").on("click", function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		$.get(href, function (product, status) {
			let images = "";
			for (let i = 0; i < product.images.length; i++) {
				images += `<div class="image" style="height: auto;
													width: auto;
													border-radius: 10px;
													box-shadow: 0 0 5px rgba(0, 0, 0, 0.15);
													position: relative;
													overflow: hidden;
													margin-bottom: 10px;
													margin-right: 10px">
                        <img
                        src="/static/uploads/product_images/${product.id}/${product.images[i].imageName}"
                        class="w-100"
                        alt="image"
						style="height=100%;
								width=100%"
                        />
                        <span onclick="edit_delImage(${i})" style="position: absolute;
															top: -4px;
															right: 4px;
															cursor: pointer;
															font-size: 22px;
															color: #fff;">&times;</span>
                    </div>`;
			}
			$(".edit_image_container").html(images);

			$("#edit_id").val(product.id);
			$("#edit_productName").val(product.productName);
			$("#edit_price").val(product.price);
			$("#edit_branch").val(product.branch);
			$("#edit_description").val(product.description);
			$("#edit_available").val(product.available);
			if (product.gender) {
				$("#edit_gender").val("1").change();
			} else {
				$("#edit_gender").val("0").change();
			}
			$("#edit_type").val(product.type).change();
			let sizes = [],
				colors = [];
			$.each(product.sizes, function (i, e) {
				sizes.push(e.size);
			});
			$.each(product.colors, function (i, e) {
				colors.push(e.color);
			});
			$("#edit_sizes").val(sizes).change();
			$("#edit_colors").val(colors).change();
		});
		$("#editProduct").modal("show");
	});

	/*Delete Product*/
	$(".delBtn").on("click", function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		$("#delBtn").attr("href", href);
		$("#delProduct").modal("show");
	});

	/*Search*/
	$("#btnSearch").on("click", function () {
		$("#form-search").submit();
	});

	/*Orders*/
	$(".orders-view-order").on("click", function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		$.get(href, function (order, status) {
			$("#orders-view-order #total-quantity").html(
				"(" + order.totalQuantity + " products)"
			);

			let items = "";
			for (let i = 0; i < order.items.length; i++) {
				items += `<div class="order-item">
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
			if (order.status != 1) {
				$("#orders-view-order #order-btnConfirm").hide();
			}
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
		});
	});

	$(".orders-confirm-order").on("click", function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		$("#orders-confirm-order a").attr("href", href);
		$("#orders-confirm-order").modal("show");
	});

	$(".orders-del-order").on("click", function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		$("#orders-del-order a").attr("href", href);
		$("#orders-del-order").modal("show");
	});

	/* Customers */
	$(".customers-view-user").click(function (event) {
		event.preventDefault();
		var href = $(this).attr("href");
		$.get(href, function (user) {
			$("#userName").val(user.userName);
			$("#email").val(user.email);
			$("#phoneNumber").val(user.phoneNumber);
			$("#province1").val(user.address.province);
			$("#district1").val(user.address.district);
			$("#ward1").val(user.address.ward);
			$("#detailAddress").val(user.address.detailAddress);
			$("#view-user-modal").modal("show");
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
