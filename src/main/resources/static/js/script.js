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
			$("#province").append(
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
	$("#province").change(function () {
		// selectedProvince = this.options[this.selectedIndex].text;
		provinceCode = $(this).children(":selected").attr("id");
		var url =
			"https://vapi.vnappmob.com//api/province/district/" + provinceCode;
		$.getJSON(url, function (data) {
			$("#district option").remove();
			console.log(data);
			$.each(data.results, function (index, value) {
				// APPEND OR INSERT DATA TO SELECT ELEMENT.
				$("#district").append(
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
	$("#district").change(function () {
		// selectedDistrict = this.options[this.selectedIndex].text;
		districtCode = $(this).children(":selected").attr("id");
		var url = "https://vapi.vnappmob.com/api/province/ward/" + districtCode;
		$.getJSON(url, function (data) {
			$("#ward option").remove();
			console.log(data);
			$.each(data.results, function (index, value) {
				// APPEND OR INSERT DATA TO SELECT ELEMENT.
				$("#ward").append(
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
			data = {
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

	// ------------------------------Cart---------------------------------//
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
