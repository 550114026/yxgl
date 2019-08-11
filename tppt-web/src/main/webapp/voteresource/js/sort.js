var sortfield=null;
var sortDirection="0";
function changeSort(th){
    $(th).parent().find("th").each(function(){
        if(this!=th){
            $(this).removeClass("descending");
            $(this).removeClass("ascending");
        }
    });
    th=$(th);
    sortfield=th.data("sort");
    if(th.hasClass("descending")){
        sortDirection="1"
        th.removeClass("descending")
        th.addClass("ascending")
    }else if(th.hasClass("ascending")){
        sortDirection="0"
        th.removeClass("ascending")
        th.addClass("descending")
    }else{
        sortDirection="0"
        th.addClass("descending")
    }
    search(1);
}

function initTH(){
    if(sortfield==null)
        return;
    $("table").find("th").each(function () {
        var sf=$(this).data("sort");
        if(sf==sortfield){
            if(sortDirection=="1"){
                $(this).addClass("ascending")
            }else{
                $(this).addClass("descending")
            }
        }
    });
}