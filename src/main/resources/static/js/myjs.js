console.log("Xxx");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		//true
		//dikh ra hai tho band karna hai
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		//false
		//nahi dikh ra hai tho show karna hai
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};
/*this query is in view contact to make column clickable*/
jQuery(document).ready(function($) {
	$(".clickable-row").click(function() {
		window.location = $(this).data("href");
	});
});

/*this js is for deleting contact make facilitate*/
function deleteContact(cid) {

	swal({
		title: "Are you sure?",
		text: "You want to delete this contact!!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {

				window.location = "/user/delete/" + cid;

			} else {
				swal("Your contact is safe!");
			}
		});
}
/*function deleteContact(cid) {
	Swal.fire({
		title: 'Are you sure?',
		text: "You won't be able to revert this!",
		icon: 'success',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location = "/user/delete/" + cid;
			Swal.fire(

				'Deleted!',
				'Your file has been deleted.',
				'success'
			)
		}
	})
}*/