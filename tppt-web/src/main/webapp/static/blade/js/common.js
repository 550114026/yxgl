var polygonOptions={strokeColor:"#0066FF",fillColor:"red", fillOpacity:0.5,strokeWeight:1, strokeOpacity:1};
var areaPolygonOptions={strokeColor:"red",strokeStyle:"dashed", fillOpacity:0.1,strokeWeight:3, strokeOpacity:1};
var showWin=function(tmsp_url,title,width,height){
		if(!width)
			width="800";
		if(!height)
			height="520";
    	var iframe_width=window.top.$(window).width();
    	var iframe_height=window.top.$(window).height();
    	var flag=(parseInt(width)>=iframe_width||parseInt(height)>=iframe_height)?true:false;
    	width+="px";height+="px";

    	var index=layer.open({
    	    type: 2,
    	    title:title,
    	    area: [width,height],
    	    fix: false, //不固定
    	    maxmin: true,
    	    content: tmsp_url
    	});
    	if(flag){
    		layer.full(index);
    	}
    };
 
function closeLayerWin(){
	var layerIndex = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.close(layerIndex);
	
}
    
 var mapTool={
		 getOverlayType:function (overlay){	 
			 if(overlay.getPosition)
				 return "marker";
			 else if(overlay.getPath)
				 return "polygon"
		 }
 }
 
 
 var editTool={
		 setFormData:function(_model,_keyList,_keys){
			 _keys = editTool.switchBeetl(_keyList,_keys);
			 if(_model == "error"){
					layer_alert("读取数据失败!", "error");
					return;
				}
				for(var x in _model){
					var $x = $("#_" + x);
					if($x.is("input") || $x.is("textarea")){
						$x.bind("focus", function(){
							var _name = $(this).attr("name").replace("token_", "");
							$(this).attr("name", _name);
							$("#form_token").val(1);
						});
					}
					
					if(_model[x] != null && $x.attr("data-type") == "image"){
						var src = $("#_"+x).attr("data-auto") + _model[x];
						$x.attr("src",src);
						$x.bind("click",function(){
							window.open($(this).attr("src"));
						});
					}
					else if(_model[x] != null && $x.attr("data-type") == "imgupload"){
						var id = _model[x];
						$x.val(id);
						initImgUpload(id, "edit");
					}
					else if(_model[x] != null && $x.attr("data-type") == "fileupload"){
						var ids = _model[x];
						$x.val(ids);
						initFileUpload(ids, "edit");
					}
					else if(_model[x] != null && $x.attr("data-type") == "opentree"){
						var ids = _model[x];
						$x.val(ids);
						initOpenTree($x.data("x"));
					}
					else if(_model[x] != null && $x.attr("data-type") == "openpage"){
						var ids = _model[x];
						$x.val(ids);;
					}
					else if(_model[x] != null && $("#" + x + "_chb").attr("type") == "checkbox"){
						if(_model[x] == "1"){
							$("#"+x+"_chb").attr("checked","checked");
							$x.val(_model[x]);
						}
						else{
							$("#"+x+"_chb").removeAttr("checked");
						}
					}
					else if($x.is("span")){
						var _x = _model[x];
						if(_x == null || _x == ""){
							_x = "请选择";
						}
						$x.html(_x);
					}
					else if(_model[x]!=null && $x.is("div") && $x.attr("class").indexOf("edui")>0){
						//适配ueditor
						editor.setContent(_model[x]);
					}
					else if(_model[x] != null && $x.attr("data-type")=="laydate") {
						if((_model[x]+'').length<=4){
							$x.val(_model[x]);
							continue;
						}
						 var stateDate=new Date(_model[x].replace(/-/g,"/"));  
						var format=$x.data("format");      
						if(!format)
							format="yyyy-MM-dd";  
						$x.val(stateDate.format(format));
					}
					else if(_model[x] != null && $x.attr("type")!="autohidden"){
						$x.val(_model[x]);
					}
					
				}
				
				for(var k in _keys){
					var id = _keys[k].table + "_" + _keys[k].key;
					$("#" + id).val(_model[id]);
				}
		 },
		 switchBeetl:function(_keyList,_keys){
				if(_keyList=="true"){
					_keys=_keys.replace(/=/g,":").replace(/{/g,"{'").replace(/}/g,"'}").replace(/:/g,"':'").replace(/, /g,"','");				
					_keys=_keys.replace(/}','{/g,"},{");
					_keys=eval(_keys);
				}
				return _keys;
			}
		 
 }
 /**
  *X为控件配置信息json
  * @param x
  */
 function initOpenTree(x){
	 var value=$("#_"+x.index).val();
	 x.val=value;
		$.post(basePath+"/ztree/getTreeListName",x,function(data){
			if(data.code === 0){
				if(data.data.length>0||$("#_"+x.index+"_INPUT").val().length>0)
					$("#_"+x.index+"_INPUT").val(data.data);
					
			}
		}, "json");
	}
 
 var viewTool={
		 setView:function(_model){
			 if(_model == "error"){
					layer_alert("读取数据失败!", "error");
					return;
				}
				for(var x in _model){
					var $x = $("#_" + x);
					if(_model[x] != null &&$x.attr("dic"))
						continue;
					if(_model[x] != null && $x.attr("data-type")=="image"){
						var src = $x.attr("data-auto") + _model[x];
						$x.attr("src", src);
						$x.bind("click", function(){
							window.open($(this).attr("src"));
						});
					}
					else if(_model[x] != null && $x.attr("data-type") == "imgupload"){
						var id = _model[x];
						initImgUpload(id, "view");
						$x.val(id);
					}
					else if(_model[x] != null && $x.attr("data-type") == "fileupload"){
						var ids = _model[x];
						initFileUpload(ids, "view");
						$x.val(ids);
					}
					else if($x.is("span")){
						var _x = _model[x];
						if(_x == null || _x == ""){
							_x = "请选择";
						}
						$x.html(_x);
					}
					else if(_model[x]!=null && $("#" + x + "_chb").attr("type")=="checkbox"){
						if(_model[x]=="1"){
							$("#"+x+"_chb").attr("checked", "checked");
						}
						else{
							$x.removeAttr("checked");
						}
					}
					else if(_model[x]!=null && $x.is("div") && $x.attr("class").indexOf("edui")>0){
						//适配ueditor
						editor.setContent(_model[x]);
					}
					else if(_model[x] != null && $x.attr("data-type") == "opentree"){
						var ids = _model[x];
						$x.val(ids);
						initOpenTree($x.data("x"));
					}
					else if(_model[x] != null &&  typeof $x.attr("data-format")!="undefined") {
						 var stateDate=new Date(_model[x].replace(/-/g,"/"));  
						var format=$x.attr("data-format");   
						if(!format)
							format="yyyy-MM-dd";
						$x.val(stateDate.format(format));
					}
					else {
						$x.val(_model[x]);
					}
				}
		 }
 }
 
 Date.prototype.format=function(mask)
 {
     var d = this;
     var zeroize = function (value, length)
     {
         if (!length) length = 2;
         value = String(value);
         for (var i = 0, zeros = ''; i < (length - value.length); i++)
         {
             zeros += '0';
         }
         return zeros + value;
     };
  
     return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
     {
         switch ($0)
         {
             case 'd': return d.getDate();
             case 'dd': return zeroize(d.getDate());
             case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
             case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
             case 'M': return d.getMonth() + 1;
             case 'MM': return zeroize(d.getMonth() + 1);
             case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
             case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
             case 'yy': return String(d.getFullYear()).substr(2);
             case 'yyyy': return d.getFullYear();
             case 'h': return d.getHours() % 12 || 12;
             case 'hh': return zeroize(d.getHours() % 12 || 12);
             case 'H': return d.getHours();
             case 'HH': return zeroize(d.getHours());
             case 'm': return d.getMinutes();
             case 'mm': return zeroize(d.getMinutes());
             case 's': return d.getSeconds();
             case 'ss': return zeroize(d.getSeconds());
             case 'l': return zeroize(d.getMilliseconds(), 3);
             case 'L': var m = d.getMilliseconds();
                 if (m > 99) m = Math.round(m / 10);
                 return zeroize(m);
             case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
             case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
             case 'Z': return d.toUTCString().match(/[A-Z]+$/);
             // Return quoted strings with the surrounding quotes removed
             default: return $0.substr(1, $0.length - 2);
         }
     });
 };
 
 
 function setMapTool(map){
		var mapType2 = new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT});

		var mapType1 = new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP],anchor: BMAP_ANCHOR_TOP_LEFT});
		var overView = new BMap.OverviewMapControl();
		map.addControl(mapType1);          //左上角，默认地图控件
		map.addControl(overView);          //添加默认缩略地图控件
 }
 
 
 
 String.prototype.startWith=function(str){     
	  var reg=new RegExp("^"+str);     
	  return reg.test(this);        
	}  

	String.prototype.endWith=function(str){     
	  var reg=new RegExp(str+"$");     
	  return reg.test(this);        
	}
	
 function initBrowser() {
	 var browserType=$.cookie('browserType');
	 if(!browserType){
	     var sUserAgent = navigator.userAgent.toLowerCase();
	     var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
	     var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
	     var bIsMidp = sUserAgent.match(/midp/i) == "midp";
	     var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
	     var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
	     var bIsAndroid = sUserAgent.match(/android/i) == "android";
	     var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
	     var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
	     if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
	    	 browserType="isPad";
	    	 $.cookie('browserType',browserType);
	    	 //alert(browserType);
	     } 
	 }
 }	
