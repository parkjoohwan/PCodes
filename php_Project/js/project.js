$(function(){

	var SLIDER_INDEX = 1;
	var SLIDER_LEN = 0;
	var SLIDER_WIDTH = 1400;
	var SHOW_DURATION = 200;

	var timer = 0;

	SLIDER_LEN = $("#slider .page").length;

	$("#slider").css({"left":-SLIDER_WIDTH});

	setInterval(function() {
		var nIndex = SLIDER_INDEX-1;
		showSlider(nIndex);
	}, 2000);

	$("#btn_left").click(function(){
		var nIndex = SLIDER_INDEX-1;
		showSlider(nIndex);
	});

	$("#btn_right").click(function(){
		var nIndex = SLIDER_INDEX+1;
		showSlider(nIndex);
	});


	function showSlider(nIndex){
		var nPos = -SLIDER_WIDTH * nIndex;

		$("#slider").stop().animate({
			left:nPos
		},SHOW_DURATION, function(){

			if(nIndex < 1){
				nIndex = SLIDER_LEN;
				nPos = -SLIDER_WIDTH * nIndex;
				$("#slider").css({"left":nPos + "px"});
			}

			if(nIndex > SLIDER_LEN){
				nIndex = 1;
				nPos = -SLIDER_WIDTH * nIndex;
				$("#slider").css({"left":nPos + "px"});
			}

			SLIDER_INDEX = nIndex;
	});
	}
});


$(function(){

	var SLIDER_INDEX = 1;
	var SLIDER_LEN = 0;
	var SLIDER_WIDTH = 280;
	var SHOW_DURATION = 200;

	var timer = 0;

	SLIDER_LEN = $("#slider2 .page2").length;

	$("#slider2").css({"left2":-SLIDER_WIDTH});

	setInterval(function() {
		var nIndex = SLIDER_INDEX-1;
		showSlider(nIndex);
	}, 2000);

	$("#btn_left2").click(function(){
		var nIndex = SLIDER_INDEX-1;
		showSlider(nIndex);
	});

	$("#btn_right2").click(function(){
		var nIndex = SLIDER_INDEX+1;
		showSlider(nIndex);
	});


	function showSlider(nIndex){
		var nPos = -SLIDER_WIDTH * nIndex;

		$("#slider2").stop().animate({
			left:nPos
		},SHOW_DURATION, function(){

			if(nIndex < 1){
				nIndex = SLIDER_LEN;
				nPos = -SLIDER_WIDTH * nIndex;
				$("#slider2").css({"left2":nPos + "px"});
			}

			if(nIndex > SLIDER_LEN){
				nIndex = 1;
				nPos = -SLIDER_WIDTH * nIndex;
				$("#slider2").css({"left2":nPos + "px"});
			}

			SLIDER_INDEX = nIndex;
	});
	}
});
