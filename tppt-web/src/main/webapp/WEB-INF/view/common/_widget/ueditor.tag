
	<script type="text/javascript">
		function getCtxPath(){
			return "${ctxPath}";
		}
	</script>
	<script type="text/javascript" charset="utf-8" src="${ctxPath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxPath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxPath}/static/ueditor/ueditor.parse.min.js"></script>
	
	@ var val = x.value!'';
	@ var token = "token_";
	@ if (val != ""){
	@ 	token = "";	
	@}
	
	<!-- 加载编辑器的容器 -->
    <script id="_${x.index!}" type="text/plain" style="width:100%">
        ${x.value!}
    </script>
    
    <script>
		$(function(){
			// ueditor
	        var editor = UE.getEditor('_${x.index!}',{
	        	toolbars: [["source", "undo", "redo", "bold", "italic", "underline", "fontborder", "insertunorderedlist", "insertorderedlist", "unlink", "link", "insertimage", "insertvideo", "removeformat", "justifyleft", "justifycenter", "justifyright", "fontsize", "fontfamily", "autotypeset",'inserttable', 'deletetable', "imageleft", "imageright", "imagecenter", "imagenone", "preview", "fullscreen"]],
	        	readonly: ${readOnly!false},
	        	initialFrameHeight:180,
	        	textarea: "${table!x.table}.${x.index!}"
	        });
		});
		
	</script>	

