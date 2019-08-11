var _faiAjax = function () {
    //for regexp
    var r = /\?/;
    var _o = {
        type: "get",
        url: "",
        data: "",
        error: function () {
        },
        success: function () {
        }
    };
    var _sendRequest = function (o) {
        var xmlhttp = null;
        //init option code
        o.type = o.type || _o.type;
        o.url = o.url || _o.url;
        o.data = o.data || _o.data;
        o.error = o.error || _o.error;
        o.success = o.success || _o.success;
        if (window.XMLHttpRequest) {
            // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {
            return;
        }
        //the instructions param takes the form of an eval statement
        if (o.type != "post") {
            o.url += (this.r.test(o.url) ? "&" : "?") + o.data;
            xmlhttp.open("GET", o.url, true);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    o.success(xmlhttp.responseText);
                } else if (o.error) {
                    o.error();
                }
            }
            xmlhttp.send();
        } else {
            xmlhttp.open("POST", o.url, true);
            //Send the proper header information along with the request
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    o.success(xmlhttp.responseText);
                } else {
                    o.error();
                }
            }
            xmlhttp.send(o.data);
        }
    }
    return {
        ajax: function (option) {
            try {
                //此次调用的错误不让抛出给window。防止函数重入
                _sendRequest(option);
            } catch (e) {
                //alert(e);
            }
        }
    };
}();
var _jsErrCahche = [];
window.onerror = function (sMsg, sUrl, sLine) {
    if (typeof Mobi == 'undefined') {
        if ((window.DocumentTouch && document instanceof window.DocumentTouch) || 'ontouchstart' in document.documentElement) {
            alert('您的网页未加载完成，请尝试按“刷新”重新加载。');
        } else {
            alert('您的网页未加载完成，请尝试按“CTRL+功能键F5”重新加载。');
        }

    }
    if (sLine < 1 || typeof sMsg != 'string' || sMsg.length < 1) {
        return;
    }

    var log = "Error:" + sMsg + ";Line:" + sLine + ";Url:" + sUrl + ";UserAgent:" + navigator.userAgent;
    var alertLog = "Error:" + sMsg + "\n" + "Line:" + sLine + "\n" + "Url:" + sUrl + "\n";
    var encodeUrl = function (url) {
        return typeof url === "undefined" ? "" : encodeURIComponent(url);
    };

    var ajax = true;
    var obj = {'m': sMsg, 'u': sUrl, 'l': sLine};
    for (var i = 0; i < _jsErrCahche.length; i++) {
        if (_jsErrCahche[i].m == obj.m && _jsErrCahche[i].u == obj.u && _jsErrCahche[i].l == obj.l) {
            ajax = false;
            break;
        }
    }

    if (ajax) {
        _jsErrCahche.push(obj);
        _faiAjax.ajax({
            type: "post",
            url: "ajax/logJsErr.jsp?cmd=jsErr",
            data: 'msg=' + encodeUrl(log)
        });
    }
    if (false) {
        console.log(alertLog);
    }
};


if (typeof Fai == 'undefined') {
    Fai = {};
    //解决页面被嵌套在iframe的场景

    Fai.top = window;

}


Fai.top._isFaiHost = true;
// Fai.top.moduleManageFontSize = {"module356": 20.5, "module357": 20.5, "module358": 20.5, "module302": 20.5}

Fai.top._aid = 12811184;
Fai.top._pageId = 3;
Fai.top._headerCode = ""; //网站插件代码

Fai.top._isFreeVer = false;
Fai.top._resRoot = '//mo.faisys.com';
Fai.top._templateFrameId = 1029;
Fai.top._templateLayoutId = 2;
Fai.top._mobiSiteTitleChanged = 0; 	//用于记录网站标题是否有改动
Fai.top._onlineServiceJson = {};
Fai.top._openOnlineService = true;
Fai.top._manageMode = false;
Fai.top.sessionMemberId = 0;
Fai.top.memberName = "";
//Fai.top._colInfo = {"aid":12811184,"wid":0,"id":3,"type":3,"flag":128,"moduleList":[356,357,358,302],"moduleHidden":[],"createTime":1494317417000,"updateTime":1494317265000,"authMemberLevelId":0,"extId":0,"other":{"pgt":0,"pgb":{"type":0,"catalogList":[],"s":4}},"name":"首页","defaultName":"首页","url":"/index.jsp","valid":true,"baseSetting":{"c":0,"i":0,"iconType":0,"id":0,"content":"","classname":"","color":"","colIconType":0,"colIconCusType":0,"iconFileId":"","jumpCtrl":{"ide":"","columnType":0}},"columnStyle":{"s":2},"title":{"fontType":0,"align":0,"font":{"size":12,"family":"","colorType":0,"color":"#000"},"bgType":"0","bgFont":{"color":"#000","alpha":100},"bgImgFileId":"","bgImgStyle":"1","mbt":0,"mhl":0,"mbc":{"color":"#000","alpha":100},"mbi":"","mbit":1,"mti":0,"mths":0,"mtht":1},"logo":{"i":"","h":true,"a":1,"style":0,"marginType":0,"margin":{"top":0,"bottom":0,"left":0,"right":0}},"banner":{"showType":0,"h":false,"bn":1,"b":[],"st":6,"et":1,"sd":0,"c":[]},"independent":false,"allowed":true,"selectable":true,"forbid":false};
Fai.top._lcid = 2052;   // 当前的语言版本
Fai.top._serviceList = [{
    "serviceType": "0",
    "serviceAccount": "10000",
    "showFont": "QQ客服",
    "showType": true,
    "isFontIcon": "true",
    "color": "rgb(238, 82, 102)",
    "fileId": "",
    "classname": "faisco-icons-scQq",
    "filePath": ""
}]; //商城主题在线客服
Fai.top._mallService = 1;
Fai.top.inTabMaqueeHelper = {}; // 记录标签模块内子模块的动画函数
Fai.top._isBUser = false;
Fai.top._isMobiMallOpen = false;//是否开通手机商城
Fai.top._bookingOpen = false
Fai.top._newsCommentOpen = false;
Fai.top._webDebug = false; // 本地环境(独立开发也是true)

Fai.top._jzProUrl = '';
Fai.top._portalHost = 'adm.webportal.top';
Fai.top._homeHost = '';

Fai.top._bookingModuleTitle = '在线预约'


jm(document).ready(function () {

    // 绑定退出事件
    jm(window).bind("load", function () {


        Mobi.initSwipe('bannerSwipe', {
            "bn": 2,
            "b": [],
            "c": [],
            "st": 6,
            "et": 1,
            "sd": 0,
            "showType": 0,
            "h": false
        });

        //调整导航栏与g_web的top值
        //部分主题有css3动画，所以会导致计算失误，需要等css3动画执行完之后计算
        Mobi.autoFixGWebTop(2);


        Mobi.initBgmCookie();


        Mobi.initFooterHeight();


    });

    // //手机视图初始化函数
    // try {
    //     Mobi.initMobiPage({});
    // } catch (err) {
    //     console.log(err);
    // }


    jzUtils.trigger({
        "name": "moduleAnimation.subscribe",
        "base": Mobi
    });

    // 监听页面滚动
    jzUtils.run({
        "name": "moduleAnimation.scroll",
        "base": Mobi
    });

    // 发布模块动画
    jzUtils.run({
        "name": "moduleAnimation.publish",
        "base": Mobi
    });

    Mobi.initFooter();//初始化页脚
});

//针对管理态做初始化 by jser 2017-10-23
function initManagePage() {

    var isManageMode = (Fai.top._manageMode === true);
    if (!isManageMode) {
        return;
    }


}

// 获取url参数
function getUrlQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

// 页面错误提示
function errnoIng() {
    // 获取参数errno
    var errno = getUrlQueryString("errno");
    if (errno == 14) {
        Mobi.ing(LS.memberLoginNoPermission);
    }
}

//解决苹果手机浏览模板网站，右下角的手机模板显示不正常的bug


if (false) {
    var mallBuy = localStorage.getItem("mallBuyCallBack");
    if (!!mallBuy) {
        localStorage.removeItem("mallBuyCallBack");
        mallBuy = jm.parseJSON(mallBuy);
        if (mallBuy.login) {
            Mobi.mallBuy(mallBuy.productId, mallBuy.buyType, mallBuy.entry, mallBuy.moduleId, jm.parseJSON(mallBuy.option), mallBuy.count, mallBuy.options);
        }
    }
}

function popupCouponMsg(msg) {
    var ing = jm(".mobiCouponMsgBox");
    ing.remove();
    jm("<div class='mobiCouponMsgBox'></div>").appendTo("body");
    ing = jm(".mobiCouponMsgBox");
    jm(msg).appendTo(ing);

    window.setTimeout(function () {
        ing.css("visibility", "hidden");
        ing.css("opacity", "0");
    }, 1500);
}


var receiveCouponFromHd = false;
var couponId = 0;
var savePrice = 0.0;

//Mobi.initNewGuestGiftInfo();


//将秒数专为天数
function toDays(lastTime) {
    //计算出相差天数
    var days = Math.floor(lastTime / (24 * 3600 * 1000))
    //计算出小时数
    var leave1 = lastTime % (24 * 3600 * 1000)    //计算天数后剩余的毫秒数
    var hours = Math.floor(leave1 / (3600 * 1000))
    //计算相差分钟数
    var leave2 = leave1 % (3600 * 1000)        //计算小时数后剩余的毫秒数
    var minutes = Math.floor(leave2 / (60 * 1000))
    //计算相差秒数
    var leave3 = leave2 % (60 * 1000)      //计算分钟数后剩余的毫秒数
    var seconds = Math.round(leave3 / 1000)
    var timeStr = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
    return timeStr;
    // }else{
    //     clearInterval(setInter);
    //     return "<font style=\"font-size: 40px;color: red;\" class=\"ff\">0天0时0分0秒</font>";
    // }
}

var timer = null;
var currentTime = null;
var endTime = null;

function doTiming() {
    currentTime = currentTime + 1000;
    var lastTime = endTime - currentTime;
    if (lastTime > 0) {
        jm("#boxTimer").html(toDays(lastTime));
    } else {
        voteEnded = true;
        jm("#boxTimeEnded").show();
        jm("#boxTiming").hide();
        jm("#timepiece").html("<font style=\"font-size: 40px;color: #DCDCDC;\" class=\"ff\">时间过期</font>");
        clearInterval(timer);
    }
}

/**
 * 开始倒计时
 */
function startTimer() {
    var endDate = jm("#boxTiming").data("endtime");
    var currentDate = jm("#boxTiming").data("currenttime");
    currentTime = (strToDate(currentDate)).getTime();
//js倒计时
//从服务器上获取初始时间并转换为毫秒
//目标时间
    endTime = (strToDate(endDate)).getTime();
    doTiming();
    timer = setInterval(doTiming, 1000);
}

/**
 * 字符串转时间
 * @param dateObj yyyy/MM/dd HH:mm:ss
 * @returns {Date}
 */
function strToDate(dateObj) {
    dateObj = dateObj.replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '').replace(/(-)/g, '/')
    if (dateObj.indexOf(".") > 0) {
        dateObj = dateObj.slice(0, dateObj.indexOf("."))
    }
    return new Date(dateObj)
}

