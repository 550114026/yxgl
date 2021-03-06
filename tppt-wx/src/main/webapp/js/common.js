function selected(a){
	var data=$(a).data();
	//alert(data.value);
	var txt=$(a).parents("div.dropdown-group").find("input.bookHouse-input");
	var val=$(a).parents("div.dropdown-group").find("input[type='hidden']");
	val.val(data.value);
	txt.val(data.text);
	
}

/**
 * 设置下拉选中之 
 * @param id
 * @param value
 */
function setSelected(id,value){
	$("#"+id).find("a[role='menuitem']").each(function(){
		if($(this).data("value")==value){
			$(this).trigger("click");
			return true;
		}
		
	});
}

/**
 * 设置滑块值
 * @param id
 * @param value
 */
function setSilde(id,value){
	 if($(this).hasClass("ifTrue")){
         $(this).addClass("ifFalse");
         $(this).removeClass("ifTrue");
         $(this).next().val("0");
     }else{
         $(this).removeClass("ifFalse");
         $(this).addClass("ifTrue");
         $(this).next().val("1");
     }
}


function  ttp(url){
	window.location.href=url;
}


/**
 * 
 * @param formObj
 * @returns {___anonymous555_556}
 */
function formToJson(formObj){
	   var o={};
	   var a=formObj.serializeArray();
	   $.each(a, function() {

	       if(this.value){
	           if (o[this.name]) {
	               if (!o[this.name].push) {
	                   o[this.name]=[ o[this.name] ];
	               }
	                   o[this.name].push(this.value || null);
	           }else {
	               if($("[name='"+this.name+"']:checkbox",formObj).length){
	                   o[this.name]=[this.value];
	               }else{
	                   o[this.name]=this.value || null;
	               }
	           }
	       }
	   });
	   //alert(JSON.stringify(o));
	   return o;
	}; 

	
	
	function getUrlParms()    {
	    var args=new Object();   
	    var query=window.location.search.substring(1);//获取查询串 
	    var pairs=query.split("&");//在逗号处断开   
	    for(var   i=0;i<pairs.length;i++) {   
	        var pos=pairs[i].indexOf('=');//查找name=value 
	            if(pos==-1)   continue;//如果没有找到就跳过 
	            var argname=pairs[i].substring(0,pos);//提取name   
	            var value=pairs[i].substring(pos+1);//提取value   
	            args[argname]=unescape(value);//存为属性   
	    }
	    return args;
	}
	
	function formmatSearchUrl(s,t){
		var url=window.location.href.split("?")[0];
		var search="";
	    var query=window.location.search.substring(1);//获取查询串 
	    var pairs=query.split("&");//在逗号处断开   
	    for(var   i=0;i<pairs.length;i++) {   
	        var pos=pairs[i].indexOf('=');//查找name=value 
            if(pos==-1)   continue;//如果没有找到就跳过 
            var argname=pairs[i].substring(0,pos);//提取name   
            var value=pairs[i].substring(pos+1);//提取value 
            if(argname=="p"){
            	value=1;
            }else if(argname=="s"){
            	if(!s)
            		continue;
            	value=(s);//escape
            }else if(argname=="t"){
            	if(!t)
            		continue;
            	value=(t);//escape
            }
        	search=formmatSearch(search,argname,value);
	    }
	    //如果没有，则添加字符串
	    if(search.length==0){
	    	if(s)
	    		search=formmatSearch(search,"s",s);
        	if(t)
        		search=search+formmatSearch(search,"t",t);
	    }
	    return url+search;
	}
	
	function formmatSearch(search,key,value){
		if(search.indexOf("?")>=0){
			search=search+"&"+key+"="+value;
		}else{
			search=search+"?"+key+"="+value;
		}
			return search;
	}
