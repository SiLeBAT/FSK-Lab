
$(document).ready(function(){
            var $footer = $('<footer></footer>');
			$footer.div = $('<div class="footer"></div>').appendTo($footer);
			$footer.div.col = $('<div class="col-md mb-3 mb-md-0"></div>').appendTo($footer.div);
			$footer.div.col.p1 = $('<p id="footer_p" class="text-center text-md-center mt-1 mb-0"> <span class="align-middle">This website was created with support from EU and EFSA funding (GP/EFSA/AMU/2016/01) by BfR. It only reflects the BfR researchers views. Neither EFSA, EU nor BfR is responsible for any use of the website or the service.</span></p>').appendTo($footer.div.col);

			$footer.div.col.p2 = $('<p id="footer_p" class="text-center text-md-center mt-1 mb-0"><a href="https://www.efsa.europa.eu/en"><img src="'+ "js-lib/bfr/fskapp/img/efsa_logo.svg" + '" alt="EU emblem" width="26" height="18"></a> <span class="align-middle">EFSA is an agency of the European Union </span><img src="'+ "js-lib/bfr/fskapp/img/eu_logo.png" + '" alt="EU emblem" width="26" height="18"></p>').appendTo($footer.div.col);
			$footer.appendTo(document.body);
});
$(function(){
      //Keep track of last scroll
      var lastScroll = 0;
      $(window).scroll(function(event){
          //Sets the current scroll position
          var st = $(this).scrollTop();

          //Determines up-or-down scrolling
          if (st > lastScroll){
            $(".footer").css("display",'inline')
          } 
          if(st == 0){
            $(".footer").css("display",'none')
          }
          //Updates scroll position
          lastScroll = st;
      });
    });



