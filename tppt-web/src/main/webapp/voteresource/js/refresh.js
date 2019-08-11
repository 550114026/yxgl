
var selectedids=null;
function saveids() {
    selectedids= getGridXls().join(",");
}

function setSelectetRows() {
    if(selectedids!=null){
        var ids=selectedids.split(",");
        $.each(ids,function(index,value){
            $(grid_selector).jqGrid('setSelection',value);
        });

    }
}

function refresh() {
    doSearch();
    //setTimeout(setSelectetRows,200);
    //setSelectetRows();
}



var seconds=5;
var timer=null;
function initRefresh(ckb,callback,waitseconds){
    if(ckb.checked){
        timer=setInterval(function(){
            initInterval(ckb,callback,waitseconds);
        },1000);
    }else{
        window.clearInterval(timer);
        $(ckb).data("times",null);
        var label=$("label[for="+$(ckb).attr("id")+"]");
        if(label.data("oldtext")!=null){
            label.text(label.data("oldtext"));
        }
    }
}

function initInterval(ckb,callback,waitseconds){
    //当前次数
    if($(ckb).data("times")==null){
        $(ckb).data("times",waitseconds);
    }else{
        $(ckb).data("times",  parseInt($(ckb).data("times"))-1);
    }

    var label=$("label[for="+$(ckb).attr("id")+"]");
    if(parseInt($(ckb).data("times"))<1){
        label.text("刷新中..")
        callback();
        $(ckb).data("times",null);
        return ;
    }
    if(label.data("oldtext")==null){
        label.data("oldtext",label.text());
    }
    label.text($(ckb).data("times")+"（秒）")
}
function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}