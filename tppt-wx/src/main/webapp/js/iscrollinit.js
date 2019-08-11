
var myScroll,
 pullDownEl, pullDownOffset,
 pullUpEl, pullUpOffset,
 generatedCount = 0;


function pullDownAction () {
	window.location.href=window.location.href;
	return;
}

function pullUpAction () {
	
	if($(".bookHouse-list").length<20){
		pullDownAction();
		return ;
	}
	
	
 setTimeout(function () { 
	 //debugger;
	 var p=$("#hdp").val();
	 if(p=="")
		 p=2;
	 else
		 p=parseInt(p)+1;
	 var url=window.location.href;
	 $.ajax({
		url : url,
		type : "post",
		dataType : "html",
		data : {"p":p},
		showCover:false
	}).done(function(html) {
			if(html!=null){
				if(html.indexOf("bookHouse-list")>0){
					$("#wrapper").find("[page="+p+"]").remove();
					$(html).insertBefore("#pullUp");
					if($("#wrapper").find("[page="+p+"]").length==20)
						$("#hdp").val(p);
					myScroll.refresh(); 
				}
				else{
					 var opt={msg:"没有更多内容了！"};
						sysAlert(opt);
					myScroll.refresh(); 
				}
				 //$(pullUpEl).hide();
			}
	})

  
  myScroll.refresh();  // Remember to refresh when contents are loaded (ie: on ajax completion)
 }, 100); // <-- Simulate network congestion, remove setTimeout from production!
}


$(function() {

	function loaded() {
		
		if($("#pullDown").length==0)
			return;
		
		if($(".bookHouse-list").length<20)
		{
			//$("#pullDown").hide();
			$("#pullUp").find("span.pullUpLabel").text("点击刷新");
			$("#pullUp").bind("click",pullDownAction);
			//return;
		}else if($(".bookHouse-list").length<20){
			$("#pullUp").hide();
			//return ;
		}
		
	 pullDownEl = document.getElementById('pullDown');
	 pullDownOffset = 30;//$("#pullDown").offset().top+40-parseInt($("#wrapper").css("top").replace("px",""));
	 pullUpEl = document.getElementById('pullUp'); 
	 pullUpOffset = 30;
	 
	// $(pullUpEl).hide();
	
	 myScroll = new iScroll('wrapper', {
	  useTransition: true,useTransition: false,useTransform: false, momentum: true,
	  topOffset: pullDownOffset,
	  //y:100,
	  onRefresh: function () {
		  //console.log("onRefresh"+this.maxScrollY+":"+this.y);
	   if (pullDownEl.className.match('loading')) {
	    pullDownEl.className = '';
	    pullDownEl.querySelector('.pullDownLabel').innerHTML = '向下滑动刷新...';
	   } else if (pullUpEl.className.match('loading')) {
	    pullUpEl.className = '';
	    pullUpEl.querySelector('.pullUpLabel').innerHTML = '向上滑动加载更多...';
	   }
	  },
	  onScrollMove: function () {
		  //console.log("onScrollMove"+this.maxScrollY+":"+this.y);
	   if (this.y > 5 && !pullDownEl.className.match('flip')) {
	    pullDownEl.className = 'flip';
	    pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放刷新...';
	    this.minScrollY = 0;
	   } else if (this.y < 5 && pullDownEl.className.match('flip')) {
	    pullDownEl.className = '';
	    pullDownEl.querySelector('.pullDownLabel').innerHTML = '向下滑动刷新...';
	    this.minScrollY = -pullDownOffset;
	   } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
	    pullUpEl.className = 'flip';
	    pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放刷新...';
	    this.maxScrollY = this.maxScrollY;
	   } else if (this.y > (this.maxScrollY + 5)&&this.y < (this.maxScrollY + 10) && pullUpEl.className.match('flip')) {
		$(pullUpEl).show();
	    pullUpEl.className = '';
	    pullUpEl.querySelector('.pullUpLabel').innerHTML = '向上滑动加载更多...';
	    this.maxScrollY = pullUpOffset;
	   }
	  },
	  onScrollEnd: function () {
		  //console.log("onScrollEnd"+this.maxScrollY+":"+this.y);
	   if (pullDownEl.className.match('flip')) {
		   $(pullUpEl).show();
	    pullDownEl.className = 'loading';
	    pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';    
	    pullDownAction(); // Execute custom function (ajax call?)
	   } else if (pullUpEl.className.match('flip')) {
		   $(pullUpEl).show();
	    pullUpEl.className = 'loading';
	    pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';    
	    pullUpAction(); // Execute custom function (ajax call?)
	   }
	  }
	 });
	 
	 setTimeout(function () { document.getElementById('wrapper').style.left = '0'; }, 800);
	}
	document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
	loaded();
});