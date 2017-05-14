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
	
	
	var time=setInterval(autoPlay,5000);
	var dir=1;
	
	function autoPlay(){
		if(dir==1)
			controlNext();
		else
			controlPrev();
	}
	
	$(".control-prev").click(function(){
		controlPrev();
	});
	
	$(".control-next").click(function(){
		controlNext();
	});
	
	function controlPrev(){
		$(".control-prev").addClass("control-disabled");
		$(".control-next").removeClass("control-disabled");
		$(".J_carouselList").css("margin-left","0px");
		dir=1;
	}
	
	function controlNext(){
		$(".control-prev").removeClass("control-disabled");
		$(".control-next").addClass("control-disabled");
		$(".J_carouselList").css("margin-left","-1240px");
		dir=2;
	}
});