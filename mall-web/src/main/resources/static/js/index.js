$(function() {
	$(".brick-item").mouseenter(function() {
		$(this).addClass('brick-item-active');
	});
	$(".brick-item").mouseleave(function(event) {
		$(this).removeClass('brick-item-active');
	});

	$(".category-item").mouseenter(function() {
		$(this).addClass('category-item-active');
	});
	$(".category-item").mouseleave(function(event) {
		$(this).removeClass('category-item-active');
	});
});