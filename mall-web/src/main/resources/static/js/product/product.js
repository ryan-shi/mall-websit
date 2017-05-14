$(function() {
	$(".spec").click(function() {
		var clickSpecOptionMixId = $(this).attr("id");
		console.log(clickSpecOptionMixId);
		var clickSpecOption = clickSpecOptionMixId.split("-");
		var prefix = clickSpecOption[0];
		var specOptionId = new Array();
		var j = 0;
		specOptionId[j] = clickSpecOption[1];
		j++;
		// console.log(prefix);
		// console.log(clickSpecOption[1]);
		// console.log("----");
		var specOptions = $(".spec.current");
		for (var i = 0; i < specOptions.length; i++) {
			var currentMixId = $(specOptions[i]).attr("id");
			if (!currentMixId.startWith(prefix)) {
				specOptionId[j] = currentMixId.split("-")[1];
				j++;
			}
			// console.log("同类");
			// console.log(currentMixId);
		}

		// for (var int = 0; int < specOptionId.length; int++) {
		// console.log(specOptionId[int]);
		// }
		$.ajax({
			type : "GET",
			url : "/product/skuSpec",
			data : {
				specOptionId : specOptionId
			},
			traditional : true,
			success : function(data) {
				window.location.href="/product/"+data+".html"; 
			}
		});
	});

	String.prototype.startWith = function(str) {
		var reg = new RegExp("^" + str);
		return reg.test(this);
	}
});