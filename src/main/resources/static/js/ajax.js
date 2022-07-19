// AJAX
$(document).ready(function () {
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
			if (product.price % 1000 == 0) {
				$("#view_price").html(
					product.price / 1000 + "." + "000" + "&#8363;"
				);
			} else {
				$("#view_price").html(product.price / 1000 + "&#8363;");
			}

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
});
