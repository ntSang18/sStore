// TOGGLE SIDEBAR
const menuBar = document.querySelector("#content nav .bx.bx-menu");
const sidebar = document.getElementById("sidebar");

menuBar.addEventListener("click", function () {
	sidebar.classList.toggle("hide");
});

const searchButton = document.querySelector(
	"#content nav form .form-input button"
);
const searchButtonIcon = document.querySelector(
	"#content nav form .form-input button .bx"
);
const searchForm = document.querySelector("#content nav form");

searchButton.addEventListener("click", function (e) {
	if (window.innerWidth < 576) {
		e.preventDefault();
		searchForm.classList.toggle("show");
		if (searchForm.classList.contains("show")) {
			searchButtonIcon.classList.replace("bx-search", "bx-x");
		} else {
			searchButtonIcon.classList.replace("bx-x", "bx-search");
		}
	}
});

if (window.innerWidth < 768) {
	sidebar.classList.add("hide");
} else if (window.innerWidth > 576) {
	searchButtonIcon.classList.replace("bx-x", "bx-search");
	searchForm.classList.remove("show");
}

window.addEventListener("resize", function () {
	if (this.innerWidth > 576) {
		searchButtonIcon.classList.replace("bx-x", "bx-search");
		searchForm.classList.remove("show");
	}
});

const switchMode = document.getElementById("switch-mode");

switchMode.addEventListener("change", function () {
	if (this.checked) {
		document.body.classList.add("dark");
	} else {
		document.body.classList.remove("dark");
	}
});

// Admin My Store
// New Product
let files = [],
	form = document.querySelector("#new_form_image"),
	image_container = document.querySelector(".new_image_container"),
	text = document.querySelector(".new_inner"),
	browse = document.querySelector(".new_select"),
	// file
	input = document.querySelector("#new_form_image input");

browse.addEventListener("click", () => input.click());

input.addEventListener("change", () => {
	let file = input.files;

	for (let i = 0; i < file.length; i++) {
		if (files.every((e) => e.name != file[i].name)) {
			files.push(file[i]);
		}
	}

	//form.reset();
	showImages();
});

const showImages = () => {
	let images = "";
	files.forEach((e, i) => {
		images += `<div class="image">
                        <img
                        src="${URL.createObjectURL(e)}"
                        class="w-100"
                        alt="image"
                        />
                        <span onclick="delImage(${i})">&times;</span>
                    </div>`;
	});

	image_container.innerHTML = images;
};

const delImage = (index) => {
	files.splice(index, 1);
	showImages();
};

form.addEventListener("dragover", (e) => {
	e.preventDefault();
	form.classList.add("dragover");
	text.innerHTML = "Drop images here";
});

form.addEventListener("dragleave", (e) => {
	e.preventDefault();
	form.classList.remove("dragover");
	text.innerHTML =
		'Drag & drop image here or <span class="select">Browse</span>';
});

form.addEventListener("drop", (e) => {
	e.preventDefault();

	form.classList.remove("dragover");
	text.innerHTML =
		'Drag & drop image here or <span class="select">Browse</span>';

	let file = e.dataTransfer.files;

	for (let i = 0; i < file.length; i++) {
		if (files.every((e) => e.name != file[i].name)) {
			files.push(file[i]);
		}
	}
	showImages();
});
//Edit Product
let edit_files = [],
	edit_form = document.querySelector("#edit_form_image"),
	edit_image_container = document.querySelector(".edit_image_container"),
	edit_text = document.querySelector(".edit_inner"),
	edit_browse = document.querySelector(".edit_select"),
	// file
	edit_input = document.querySelector("#edit_form_image input");

edit_browse.addEventListener("click", () => edit_input.click());

edit_input.addEventListener("change", () => {
	let file = edit_input.files;

	for (let i = 0; i < file.length; i++) {
		if (files.every((e) => e.name != file[i].name)) {
			edit_files.push(file[i]);
		}
	}

	//form.reset();
	edit_showImages();
});

const edit_showImages = () => {
	let images = "";
	edit_files.forEach((e, i) => {
		images += `<div class="image">
							<img
							src="${URL.createObjectURL(e)}"
							class="w-100"
							alt="image"
							/>
							<span onclick="edit_delImage(${i})">&times;</span>
						</div>`;
	});

	edit_image_container.innerHTML = images;
};

const edit_delImage = (index) => {
	edit_files.splice(index, 1);
	edit_showImages();
};

edit_form.addEventListener("dragover", (e) => {
	e.preventDefault();
	edit_form.classList.add("dragover");
	edit_text.innerHTML = "Drop images here";
});

edit_form.addEventListener("dragleave", (e) => {
	e.preventDefault();
	edit_form.classList.remove("dragover");
	edit_text.innerHTML =
		'Drag & drop image here or <span class="select">Browse</span>';
});

edit_form.addEventListener("drop", (e) => {
	e.preventDefault();

	edit_form.classList.remove("dragover");
	edit_text.innerHTML =
		'Drag & drop image here or <span class="select">Browse</span>';

	let file = e.dataTransfer.files;

	for (let i = 0; i < file.length; i++) {
		if (files.every((e) => e.name != file[i].name)) {
			edit_files.push(file[i]);
		}
	}
	edit_showImages();
});
// Modal View
var MainImg = document.getElementById("MainImg");
const showBigImage = (element) => {
	MainImg.src = element.src;
};
