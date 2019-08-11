//jichu JS by wcg 

//预留变量
var asyn_ = null;
var showLoadMsg = null;
//扩展
$.extend({
    setCookie: function (c_name, value, expiredays) {
        var exdate = new Date()
        exdate.setDate(exdate.getDate() + expiredays)
        document.cookie = c_name + "=" + escape(value) +
            ((expiredays === null) ? "" : ";expires=" + exdate.toGMTString())
    },
    getCookie: function (c_name) {
        if (document.cookie.length > 0) {
            c_start = document.cookie.indexOf(c_name + "=")
            if (c_start !== -1) {
                c_start = c_start + c_name.length + 1
                c_end = document.cookie.indexOf(";", c_start)
                if (c_end === -1) c_end = document.cookie.length
                return unescape(document.cookie.substring(c_start, c_end))
            }
        }
        return ""
    }
    ,
    /*ext post 方法 */
    expost: function (formid, tourl) {
        var formParam = $("#" + formid).serialize();
        $.ajax({
            type: 'post',
            data: formParam,
            url: tourl,
            dataType: 'json',
            success: function (jsons) {
                asyn_(jsons);
            },
            beforeSend: function () {
                if (showLoadMsg === null || showLoadMsg === "") {
                    showLoadMsg = "提交数据中";
                }
                $.showLoading(showLoadMsg)
            },
            complete: function () {
                $.hideLoading();
            },
            error: function (er) {
                debugger;
                alert('服务繁忙');
                $.toptips('服务繁忙');
            }
        });
    },
    exajax: function (data, url, method) {
        $.ajax({
            type: method,
            data: data,
            url: url,
            dataType: 'json',
            success: function (jsons) {
                asyn_(jsons);
            },
            beforeSend: function () {
                 
            },
            complete: function () {
                 
            },
            error: function (er) {
                

            }
        });
    }
});

var errorfun = null;

//扩展2
var ext = function () {
    return {
        post: function (formid,tourl) {
            var formParam = $("#" + formid).serialize();
            $.ajax({
                type: 'post',
                data: formParam,
                url: tourl,
                dataType: 'json',
                success: function (jsons) {
                    asyn_(jsons);
                },
                beforeSend: function () {
                    if (showLoadMsg === null || showLoadMsg === "") {
                        showLoadMsg = "提交数据中";
                    }
                    $.showLoading(showLoadMsg)
                },
                complete: function () {
                    $.hideLoading();
                },
                error: function (er) {
                    debugger;
                    alert('服务繁忙');
                    $.toptips('服务繁忙');
                }
            });
        },
        ajax: function (data, url, method) {
            $.ajax({
                type: method,
                data: data,
                url: url,
                dataType: 'json',
                success: function (jsons) {
                    asyn_(jsons);
                },
                beforeSend: function () {

                },
                complete: function () {

                },
                error: function (er) {

                    errorfun(er);
                    console.log(er);
                }
            });
        },
        goto: function (e) {
            var url = $(e).data('link');
            $.showLoading('正在跳转')
            window.location.href = url;
        },
        vmform: function (fmid,data,action,method) {
            var body = $("body");
            var html = '<form  action="' + action + '" method="' + method + '" id="' + fmid + '" style="display:none;" >';
            $.each(data, function (key) {
                html += '<input type="hidden" name="' + key + '" id="' + key + '"  value="' + data[key] + '"/>';
            });
            html += '</form>';
            body.prepend(html);
            var subform = document.getElementById(fmid);
            subform.submit();
        }
    }
}();








/*
** randomWord 产生任意长度随机字母数字组合
** randomFlag-是否任意长度 min-任意长度最小位[固定位数] max-任意长度最大位
** xuanfeng 2014-08-28
*/

function randomWord(randomFlag, min, max) {
    var str = "",
        range = min,
        arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

    // 随机产生
    if (randomFlag) {
        range = Math.round(Math.random() * (max - min)) + min;
    }
    for (var i = 0; i < range; i++) {
        pos = Math.round(Math.random() * (arr.length - 1));
        str += arr[pos];
    }
    return str;
}

String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
};

function setSortCla(obj, cursort) {
    var newsort = cursort;
    $(obj).each(function () {
        $(this).removeClass("ascending descending");
        var sort = $(this).data("sort");
        var classname = "descending";
        if (cursort.indexOf(' asc') > -1) {
            classname = "ascending";
            newsort = cursort.replace(" asc", "");
        }
        else if (cursort.indexOf(' desc') > -1) {
            classname = "descending";
            newsort = cursort.replace(" desc", "");
        }
        if (newsort == sort)
            $(this).addClass(classname);
    });
}
function sortClick(obj, url) {
    $(obj).click(function () {
        var sort = $(this).data("sort");
        if ($(this).hasClass("descending"))
            sort += " asc";
        else
            sort += " desc";
        url = url.replaceAll("&amp;", "&");
        location.href = url + '&sort=' + sort;
    });
}
function setPages(cont, pages, curr, url) {
    laypage({
        cont: cont,//分页容器的id
        pages: pages, //总页数
        curr: curr, //当前页
        skin: 'yahei',  //当前页的颜色
        groups: 7,//连续显示分页数
        jump: function (e, first) {
            if (!first) {
                url = url.replaceAll("&amp;", "&");
                location.href = url + '&page=' + e.curr;
            }
        }
    });
}