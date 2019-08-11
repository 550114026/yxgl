

<input type="text" id="_${x.index!}_INPUT" style="cursor: pointer;"  onfocus="this.blur()" ${x.required!} placeholder="${placeholder!}" value="${isEmpty(x.value)?x.text!:""}" class="form-control" />
<input type="hidden" id="_${x.index!}"  data-type="opentree" name="${table!x.table}.${x.index!}" value="${x.value!}" />


<script type="text/javascript">
	$(function(){
		@if(x.disabled!="disabled"){
		
			$("#_${x.index!}_INPUT").bind("click",function(){
				@ var ename = func.encodeUrl(x.name);
				var val = $("#_${x.index!}").val();
				var urlParaSeparator = "-";
				val = (val == "") ? 0 : val;
				layer.open({
	        	    type: 2,
	        	    title:"${x.name!}选择",
	        	    area: ["350px", "420px"],
	        	    fix: false, //不固定
	        	    maxmin: true,
	        	    content: "${basePath}/ztree/open/"
						+"${x.type!0}"+urlParaSeparator
						+"_${x.index!0}"+urlParaSeparator
						+"${ename!0}"+urlParaSeparator
						+"${x.source!0}"+urlParaSeparator
						+"${x.check!0}"+urlParaSeparator
						+"${x.where!0}"+urlParaSeparator
						+"${x.intercept!0}"+urlParaSeparator
						+"${x.ext!0}"+urlParaSeparator + val
	        	});
			});
		@}
		$("#_${x.index!}").data("x",${json(x)});
		if($("#_${x.index!}").val() != ""){
			initOpenTree(${json(x)});
		}
		
		
	});
	
	
	
</script>