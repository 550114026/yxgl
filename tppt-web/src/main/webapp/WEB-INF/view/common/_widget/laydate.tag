	
	
	<script type="text/javascript"> 
		$(function(){
			var _elem="_${x.index}";
			$('#'+_elem).data("format","${x.format!'yyyy-MM-dd'}");
			var json={
					type:'${x.datetype!"date"}',
				    elem: '#'+_elem,
				    //festival: true, //æ¾ç¤ºèæ¥
				    choose: function(datas){ //éæ©æ¥æå®æ¯çåè°
				      
				      if(typeof ${x.callback!"\"none\""} =="function"){
				    	  ${x.callback!"none"}(datas);
				      }
				    }
				};
			if("${x.datetype!""}"!="year")
				json.format="${x.format!'yyyy-MM-dd'}";
			laydate.render(json);
			
			 $("#_${x.index!}").bind("focus",function(){
					var _name = $("#_${x.index!}").attr("name").replace("token_", "");
					$("#_${x.index!}").attr("name", _name);
					$("#form_token").val(1);
			 });
		});	
	</script>	
	@ var val = x.value!'';
	@ var token = "token_";
	@ if (val != ""){
	@ 	token = "";	
	@}
	<input type="text" id="_${x.index!}" data-type="${x.type}" onfocus="this.blur()" name="${token}${table!x.table}.${x.index!}" class="form-control" ${x.required!} ${x.disabled!}  value="${x.value!}" placeholder="请选择${x.name!}"  style="cursor: pointer; ${isEmpty(x.width)?"":"width:"+x.width} " />