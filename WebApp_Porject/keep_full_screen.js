// 링크 클릭시 새로운 브라우저가 뜨지 않게 함 
// 유의: 장면전환효과는 기본값만 실행됩니다. 
$(document).ready(function(){
      $('a').on('click', function(e){
        e.preventDefault();
        var new_location = $(this).attr('href');
		window.location = new_location;
      });
});	  