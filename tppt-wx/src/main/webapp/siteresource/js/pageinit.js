
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
Fai.top.moduleManageFontSize = {"module444": 23.1875, "module445": 23.1875}

Fai.top._aid = 17357129;
Fai.top._pageId = 3;
Fai.top._headerCode = ""; //网站插件代码
Fai.top._other = {
    "productNewDeInfo": {"isNewUser": 1, "isProductNewDetail": 2},
    "newsNewDefInfo": {"isNewUser": 1, "isNewsNewDetail": 2, "prevVersion": 1},
    "mobiServiceOnline": {
        "sl": [{
            "serviceType": "0",
            "serviceAccount": "10000",
            "showFont": "QQ客服",
            "showType": true,
            "isFontIcon": "true",
            "color": "rgb(238, 82, 102)",
            "fileId": "",
            "classname": "faisco-icons-scQq",
            "filePath": ""
        }], "o": false
    },
    "qrcode_ImageId": "",
    "dc": 5,
    "mobiScreenFull": 0,
    "cml": [],
    "mc": {"h": true, "c": "", "t": 1},
    "hc": {"h": true},
    "favIcon": {"showType": 0, "id": ""},
    "iosIcon": {"showType": 0, "id": ""},
    "WXShareIcon": {"showType": 0, "id": "", "titType": 0, "tit": "", "cont": ""},
    "templateInfo": {"templatePresetIndex": 1, "templateDesignType": 1},
    "mobiBgMusic": {
        "isOpen": 0,
        "id": "",
        "isGlobal": 0,
        "setting": {"isAuto": 0, "isLoop": 0, "isHidden": 0, "position": 0},
        "filePath": ""
    },
    "mobiMallHidden": 0,
    "tc": {"h": true},
    "pa": {"st": 0, "ap": "", "qrp": "", "ma": ""},
    "sdss": {"type": 0},
    "hssnt": false,
    "mobiBackTop": {"isOpen": 0, "color": "", "style": 0}
};
Fai.top._bannerData = {
    "bn": 2,
    "b": [{
        "i": "AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI",
        "t": 1,
        "lt": 0,
        "u": "",
        "ide": "",
        "col": 0,
        "aj": "",
        "du": "project/0/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI?f=AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI.jpg&v=",
        "des": "",
        "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!100x100.jpg",
        "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!640x640.jpg",
        "w": 640,
        "h": 300
    }, {
        "i": "AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI",
        "t": 1,
        "lt": 0,
        "u": "",
        "ide": "",
        "col": 0,
        "aj": "",
        "du": "project/0/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI?f=AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI.jpg&v=",
        "des": "",
        "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!100x100.jpg",
        "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!640x640.jpg",
        "w": 640,
        "h": 300
    }],
    "c": [{
        "i": "AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI",
        "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI.jpg",
        "t": 1,
        "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!100x100.jpg",
        "w": 640,
        "h": 300
    }, {
        "i": "AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI",
        "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI.jpg",
        "t": 1,
        "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!100x100.jpg",
        "w": 640,
        "h": 300
    }],
    "st": 6,
    "et": 1,
    "sd": 0,
    "showType": 0,
    "h": false
};  // 横幅数据
Fai.top._pageBannerData = {"bn": 1, "b": [], "st": 6, "et": 1, "sd": 0, "c": [], "showType": 0, "h": false};  // 栏目独立设置横幅的数据


Fai.top._isFreeVer = true;
Fai.top._resRoot = 'resource';
Fai.top._templateFrameId = 1030;
Fai.top._templateLayoutId = 4;
Fai.top._mobiSiteTitleChanged = 0; 	//用于记录网站标题是否有改动
Fai.top._mobiSiteTitle = {
    "fontType": 0,
    "align": 0,
    "font": {"size": 12, "family": "", "colorType": 0, "color": "#000"},
    "bgType": "0",
    "bgFont": {"color": "#000", "alpha": 100},
    "bgImgFileId": "",
    "bgImgStyle": "1",
    "mbt": 0,
    "mbc": {"color": "#000", "alpha": 100},
    "mbi": "",
    "mbit": 1,
    "mths": 0,
    "name": "网站模板\u2014文化传媒",
    "mhl": 0,
    "mbip": "//2.ss.faisys.com/image/no-pic.jpg",
    "mti": 0,
    "mtht": 1
};   	//网站标题数据
Fai.top._onlineServiceJson = {
    "serviceId": {"serviceId": 6},
    "phone": {
        "open": true,
        "type": 1,
        "fName": "电话咨询",
        "phoneInfo": [{"name": "电话咨询", "number": "400-000-0000"}],
        "baseSetting": {"colIconType": 0, "content": "\\e62c", "classname": "faisco-icons-call1"},
        "typeStr": "phone"
    },
    "sms": {
        "open": false,
        "name": "信息咨询",
        "number": "",
        "type": 2,
        "baseSetting": {
            "c": 0,
            "i": 0,
            "id": 0,
            "iconType": 0,
            "colIconType": 0,
            "colIconCusType": 0,
            "classname": "faisco-icons-mail1",
            "content": "\\e6a0",
            "color": "",
            "iconFileId": ""
        },
        "typeStr": "sms",
        "cf": true
    },
    "map": {
        "open": true,
        "name": "在线地图",
        "city": "龙岩",
        "mark": "",
        "d_address": "长汀",
        "type": 3,
        "baseSetting": {"colIconType": 0, "content": "\\e67c", "classname": "faisco-icons-gps1"},
        "typeStr": "map"
    },
    "msg": {
        "open": true,
        "name": "在线留言",
        "type": 4,
        "baseSetting": {
            "c": 0,
            "i": 0,
            "id": 0,
            "iconType": 0,
            "colIconType": 0,
            "colIconCusType": 0,
            "classname": "faisco-icons-message1",
            "content": "\\e6b2",
            "color": "",
            "iconFileId": ""
        },
        "typeStr": "msg"
    },
    "qq": {
        "open": true,
        "type": 5,
        "fName": "QQ客服",
        "qqInfo": [{"name": "QQ客服", "number": "258506508"}],
        "baseSetting": {"colIconType": 0, "content": "\\e6ca", "classname": "faisco-icons-qq1"},
        "typeStr": "qq"
    },
    "open": true,
    "order": [1, 3, 4, 2, 5],
    "baiduBridge": {
        "open": false,
        "name": "百度商桥",
        "type": -1,
        "baseSetting": {
            "c": 0,
            "i": 0,
            "id": 0,
            "iconType": 0,
            "colIconType": 0,
            "colIconCusType": 0,
            "classname": "faisco-icons-scBdsq2",
            "content": "\\eada",
            "color": "",
            "iconFileId": "iconFileId"
        },
        "cf": true
    },
    "weChat": {
        "open": false,
        "name": "微信客服",
        "type": -2,
        "code": "",
        "baseSetting": {
            "c": 0,
            "i": 0,
            "id": 0,
            "iconType": 0,
            "colIconType": 0,
            "colIconCusType": 0,
            "classname": "faisco-icons-M001038",
            "content": "\\eb25",
            "color": "",
            "iconFileId": ""
        }
    },
    "oneKeySharing": {
        "open": false,
        "type": 1,
        "shareLinkList": ["Weixin", "sina_weibo", "qq_zone", "qq_share", "douban", "baidu_tieba", "copy_url"],
        "baseSetting": {
            "c": 0,
            "i": 0,
            "id": 0,
            "iconType": 0,
            "colIconType": 0,
            "colIconCusType": 0,
            "classname": "faisco-icons-call1",
            "content": "\\e62c",
            "color": "",
            "iconFileId": ""
        }
    },
    "bg": {"t": 0, "c": ""},
    "wordIcon": {"t": 0, "c": ""}
};//在线客服
Fai.top.all_oks_LinkUrl = [{
    "url": "?",
    "icon": "Weixin"
}, {
    "url": "http://service.weibo.com/share/share.php?title=&nbsp;&url=http://m.gd16618058.icoc.bz/&pic=",
    "icon": "sina_weibo"
}, {
    "url": "http://connect.qq.com/widget/shareqq/index.html?summary=http%3A%2F%2Fm.gd16618058.icoc.bz%2F&url=http://m.gd16618058.icoc.bz/&pics=&title=",
    "icon": "qq_share"
}, {
    "url": "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?summary=&nbsp;&url=http://m.gd16618058.icoc.bz/&pics=&title=&nbsp;",
    "icon": "qq_zone"
}, {
    "url": "http://shuo.douban.com/!service/share?name=http%3A%2F%2Fm.gd16618058.icoc.bz%2F&text=http://m.gd16618058.icoc.bz/",
    "icon": "douban"
}, {
    "url": "http://tieba.baidu.com/i/app/open_share_api?comment=&url=http://m.gd16618058.icoc.bz/&title=http%3A%2F%2Fm.gd16618058.icoc.bz%2F",
    "icon": "baidu_tieba"
}, {
    "url": "http://m.gd16618058.icoc.bz/?",
    "icon": "copy_url"
}, {
    "url": "http://www.kaixin001.com/rest/records.php?content=http%3A%2F%2Fm.gd16618058.icoc.bz%2F&style=11",
    "icon": "kaixin001"
}, {
    "url": "http://www.facebook.com/sharer.php?t=http%3A%2F%2Fm.gd16618058.icoc.bz%2F&u=http://m.gd16618058.icoc.bz/",
    "icon": "FaceBook"
}, {
    "url": "http://twitter.com/intent/tweet?text=http%3A%2F%2Fm.gd16618058.icoc.bz%2F",
    "icon": "Twitter"
}, {
    "url": "http://www.linkedin.com/shareArticle?summary=http%3A%2F%2Fm.gd16618058.icoc.bz%2F&url=http://m.gd16618058.icoc.bz/&title=",
    "icon": "LinkedIn"
}];
Fai.top._openOnlineService = true;
Fai.top._manageMode = false;
Fai.top.sessionMemberId = 0;
Fai.top.memberName = "";
Fai.top._colInfo = {
    "aid": 17357129,
    "wid": 0,
    "id": 3,
    "type": 3,
    "flag": 384,
    "banner": {"bn": 1, "b": [], "st": 6, "et": 1, "sd": 0, "c": [], "showType": 0, "h": false},
    "moduleList": [444, 445],
    "moduleHidden": [443, 442],
    "createTime": 1534262537000,
    "updateTime": 1534262752000,
    "authMemberLevelId": 0,
    "extId": 0,
    "other": {"pgt": 0, "pgb": {"type": 0, "catalogList": [], "s": 4}},
    "name": "首页",
    "defaultName": "首页",
    "url": "index.html",
    "valid": true,
    "baseSetting": {
        "c": 0,
        "i": 0,
        "iconType": 0,
        "id": 0,
        "content": "",
        "classname": "",
        "color": "",
        "colIconType": 0,
        "colIconCusType": 0,
        "iconFileId": "",
        "jumpCtrl": {"ide": "", "columnType": 0}
    },
    "columnStyle": {"s": 2},
    "title": {
        "fontType": 0,
        "align": 0,
        "font": {"size": 12, "family": "", "colorType": 0, "color": "#000"},
        "bgType": "0",
        "bgFont": {"color": "#000", "alpha": 100},
        "bgImgFileId": "",
        "bgImgStyle": "1",
        "mbt": 0,
        "mhl": 0,
        "mbc": {"color": "#000", "alpha": 100},
        "mbi": "",
        "mbit": 1,
        "mti": 0,
        "mths": 0,
        "mtht": 1
    },
    "logo": {
        "i": "",
        "h": true,
        "a": 1,
        "style": 0,
        "marginType": 0,
        "margin": {"top": 0, "bottom": 0, "left": 0, "right": 0}
    },
    "independent": false,
    "allowed": true,
    "selectable": true,
    "forbid": false
};
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
Fai.top._isBUser = true;
Fai.top._isMobiMallOpen = false;//是否开通手机商城
Fai.top._bookingOpen = false
Fai.top._newsCommentOpen = false;
Fai.top._webDebug = false; // 本地环境(独立开发也是true)

Fai.top._jzProUrl = '';
Fai.top._portalHost = 'adm.webportal.top';
Fai.top._homeHost = '';

Fai.top._bookingModuleTitle = '在线预约'
Mobi.bookingBoxBind(false);


//多网站聚合页地址
Fai.top._jzAllSiteDomain = _oem ? 'i.vip.webportal.top' : 'i.jz.fkw.com';


Fai.top._jzProUrl = 'jz.fkw.com';
Fai.top._portalHost = 'www.faisco.cn';
Fai.top._homeHost = 'www.faisco.com';


jm(document).ready(function () {

    Mobi.ajaxLoadModuleDom(3, 0, {
        "_ajaxLoadModuleIDList": [],
        "fullUrl": "http://m.gd16618058.icoc.bz/",
        "topBarOptionInfo": {"mobiMallOpen": false}
    });


    //判断该栏目是否被删除
    if (jm.isEmptyObject(Fai.top._colInfo)) {
        alert("该页面已经删除，点击确定后，将返回到首页");
        document.location.href = "index.html"
    }
    //Mobi.viewPageBeforeInit();
    // 绑定退出事件
    jm(window).bind("load", function () {

//当前模块 start


        Mobi.initBannerDefaultDom({
            "bn": 2,
            "b": [{
                "i": "AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI",
                "t": 1,
                "lt": 0,
                "u": "",
                "ide": "",
                "col": 0,
                "aj": "",
                "du": "project/0/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI?f=AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI.jpg&v=",
                "des": "",
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!100x100.jpg",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!640x640.jpg",
                "w": 640,
                "h": 300
            }, {
                "i": "AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI",
                "t": 1,
                "lt": 0,
                "u": "",
                "ide": "",
                "col": 0,
                "aj": "",
                "du": "project/0/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI?f=AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI.jpg&v=",
                "des": "",
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!100x100.jpg",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!640x640.jpg",
                "w": 640,
                "h": 300
            }],
            "c": [{
                "i": "AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI.jpg",
                "t": 1,
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!100x100.jpg",
                "w": 640,
                "h": 300
            }, {
                "i": "AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI.jpg",
                "t": 1,
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!100x100.jpg",
                "w": 640,
                "h": 300
            }],
            "st": 6,
            "et": 1,
            "sd": 0,
            "showType": 0,
            "h": false
        });
        Mobi.initSwipe('bannerSwipe', {
            "bn": 2,
            "b": [{
                "i": "AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI",
                "t": 1,
                "lt": 0,
                "u": "",
                "ide": "",
                "col": 0,
                "aj": "",
                "du": "project/0/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI?f=AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI.jpg&v=",
                "des": "",
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!100x100.jpg",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!640x640.jpg",
                "w": 640,
                "h": 300
            }, {
                "i": "AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI",
                "t": 1,
                "lt": 0,
                "u": "",
                "ide": "",
                "col": 0,
                "aj": "",
                "du": "project/0/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI?f=AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI.jpg&v=",
                "des": "",
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!100x100.jpg",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!640x640.jpg",
                "w": 640,
                "h": 300
            }],
            "c": [{
                "i": "AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI.jpg",
                "t": 1,
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyubHygUon5Sm3AIwgAU4rAI!100x100.jpg",
                "w": 640,
                "h": 300
            }, {
                "i": "AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI",
                "p": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI.jpg",
                "t": 1,
                "tp": CONTEXT_PATH+"/siteresource/image/banners/AD0I9OScBhACGAAgyebHygUoyPSj3QMwgAU4rAI!100x100.jpg",
                "w": 640,
                "h": 300
            }],
            "st": 6,
            "et": 1,
            "sd": 0,
            "showType": 0,
            "h": false
        });



        //调整导航栏与g_web的top值
        //部分主题有css3动画，所以会导致计算失误，需要等css3动画执行完之后计算
        Mobi.autoFixGWebTop(4);


        Mobi.initBgmCookie();


        Mobi.initFooterHeight();
    });

    //手机视图初始化函数
    try {
        Mobi.initMobiPage({
            "id": 1030,
            "createTime": "2017-09-05",
            "preview": "resource/image/template/1030/1030.jpg",
            "previewGif": "resource/image/template/1030/1030.gif",
            "color": 0,
            "style": ["resource/css/template/1030_1.min.css?v=201808021242", "resource/css/template/1030_2.min.css?v=201808021242", "resource/css/template/1030_3.min.css?v=201808021242", "resource/css/template/1030_4.min.css?v=201808021242", "resource/css/template/1030_5.min.css?v=201808021242", "resource/css/template/1030_6.min.css?v=201808021242", "resource/css/template/1030_7.min.css?v=201808021242", "resource/css/template/1030_8.min.css?v=201808021242", "resource/css/template/1030_9.min.css?v=201808021242"],
            "type": 0,
            "moduleStyle": 0,
            "designType": 1,
            "presetIndex": 0,
            "cube": 0,
            "layout": 4,
            "imagePage": 347,
            "backgroundImage": 0,
            "colors": [],
            "colorsName": []
        });
    } catch (err) {
        console.log(err);
    }


    //前端性能数据上报
    //Mobi.report();

    // 用于添加分销的标识码 dannel
    //Mobi.addDistCode();


    Mobi.setShowSiteTitleBgFlag('0');
    Mobi.changeSiteTitleBg('null', 'null', 'null', 'null', 'null');
    Mobi.logoSizeCompressByMargin();
    Mobi.titlePositionRefreshByLogo();
    Mobi.bindTopIconBack();
    Mobi.photoCrossedSlideSecSwipe('444', 'photoSlideList444', 'photoSlide444');
    if($("#photoSlideList444").length>0){
        Mobi.richMarquee.init(444, '#photoSlideList', {"d": "left", "i": true, "sp": "slow", "s": true, "mm": true});
    }
    Mobi.copyModulePhotoDetailSwipe('module444');
    Mobi.defImageHeightType2('module444', 1);
    Mobi.loadImgAlone(444, 2);
    ;jzUtils.run({'name': 'moduleAnimation.subscribe', 'callMethod': true}, {"id": 444, "animation": {"t": 0}});
    Mobi.initModuletimeAxisNewsList({'moduleId': 445, 'picNum': 7});
    Mobi.fixIphoneMargin({moduleId: 445});
    ;jzUtils.run({'name': 'moduleAnimation.subscribe', 'callMethod': true}, {"id": 445, "animation": {"t": 0}});
    Mobi.manageFaiscoAd(4);


    // 访客态页面提示错误
    if (!Fai.top._manageMode) {
        errnoIng();
    }
    //setTimeout("hasOpenFaiscoBaiduBridge()", 1000);//延迟一秒，等待百度商桥加载完成

    //initManagePage();


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

    // var isManageMode = (Fai.top._manageMode === true);
    // if (!isManageMode) {
    //     return;
    // }
    //
    // Fai.top.Mobi.logDogWithInitManagePage();


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

//Mobi.preventScroll(document.getElementById("g_body"));//阻止滚轮事件冒泡

//访客态下，统计微信浏览器的使用情况

// if (Mobi.isWechat()) {
//     Mobi.logDog(200055, 1);
// } else {
//     Mobi.logDog(200055, 0);
// }


//管理下，禁止点击跳转外链


//接入我们的百度商桥才隐藏默认的图标
// function hasOpenFaiscoBaiduBridge() {
//     if (Fai.top._mallService == 2) {
//         //商城主题
//         for (var i in Fai.top._serviceList) {
//             if (Fai.top._serviceList[i].serviceType == 2) {
//                 if (Fai.top._serviceList[i].showType == true) {
//                     if ("_other" in Fai.top && !Fai.top._other.hc.h && Fai.top._headerCode.indexOf("hm.baidu.com") > -1) {
//                         Fai.top.Mobi.getPreviewWindow().jm("ins#newBridge").show();
//                     } else {
//                         Fai.top.Mobi.getPreviewWindow().jm("ins#newBridge").hide();
//                     }
//                     break;
//                 } else {
//                     if (jm("a[href*='Mobi.showBaiduBridge']").length < 1) {
//                         Fai.top.Mobi.getPreviewWindow().jm("ins#newBridge").show();
//                         break;
//                     }
//                 }
//             } else {
//                 if (jm("a[href*='Mobi.showBaiduBridge']").length < 1) {
//                     Fai.top.Mobi.getPreviewWindow().jm("ins#newBridge").show();
//                 }
//             }
//         }
//     } else {
//         //非商城主题
//         if (Fai.top._onlineServiceJson.baiduBridge.open == true) {
//             Fai.top.Mobi.getPreviewWindow().jm("ins#newBridge").hide();
//         } else {
//             if (jm("a[href*='Mobi.showBaiduBridge']").length < 1) {
//                 Fai.top.Mobi.getPreviewWindow().jm("ins#newBridge").show();
//             }
//
//         }
//     }
//     //百度商桥统计
//     jm("#newBridge #nb_icon_wrap").off('click.log').on('click.log', function () {
//         Mobi.logDog(200171, 5);
//     });
// }

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
if (receiveCouponFromHd) {
    var windowWidth = Fai.top.document.body.clientWidth;
    var windowHeight = Fai.top.document.body.clientHeight;

    var html = [];
    html.push("<div style='display: inline-block; width: 18rem; height: 18rem; position: fixed; top: -12rem; left: -2.2rem; z-index: 9999;'>");
    html.push("<img src='" + _resRoot + "/image/hdCoupon.png'>");
    html.push("<div style='display: inline-block; width: 7.2rem; height: 2.5rem; position: fixed; top: -1.5rem; left: 2.2rem; z-index: 9999; text-align: center;'>");
    html.push("<span style='font-size: 2.2rem; color: #ff4c38; font-weight: bold;'>" + savePrice + "</span>");
    html.push("<span style='font-size: 1rem; color: #ff4c38; position: relative; top: -5px; left: 3px;'>元</span>")
    html.push("</div>");
    html.push("<div class='J-prompt-callback' style='display: inline-block; width: 5.2rem; height: 1.5rem; position: fixed; top: 1.8rem; left: 4.35rem; z-index: 9999; text-align: center;'></div>")
    html.push("</div>");
    html.push("<div class='J-prompt-cancel' style='display: inline-block; width: 2rem; height: 2rem; position: fixed; top: 6rem; left: 5.925rem; z-index: 9999;'>");
    html.push("<img src='" + _resRoot + "/image/hdCouponClose.png'>");
    html.push("</div>");

    var options = {
        'content': html.join(""),
        'callback': function () {
            jm.ajax({
                type: 'post',
                url: "ajax/mallCoupon_h.jsp",
                data: "cmd=receiveCoupon&fromHd=true&bgId=0&cid=" + couponId,//针对IE6缓存
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        var coupon = result.coupon;
                        var content = [];
                        content.push('<div class="mobiCouponMsgPanl">');
                        content.push('<div class="mobiCouponMsg">');
                        content.push('<div class="sTop">' + LS.successGet + '</div>');
                        content.push('</div>');
                        content.push('</div>');
                        popupCouponMsg(content.join(''));
                        window.setTimeout(function () {
                            Fai.top.location.href = '/'
                        }, 1500);
                    } else {
                        var rt = result.rt;
                        if (rt == -23) {//WEB_NO_LOGIN
                            Fai.top.location.href = 'login.jsp';
                            jm.cookie("hdCoupon", true);
                            jm.cookie("hdCouponId", couponId);
                        } else if (rt == -4) { //COUNT_LIMIT
                            var content = [];
                            content.push('<div class="mobiCouponMsgPanl2">');
                            content.push('<div class="mobiCouponMsg">');
                            content.push('<div class="fBottom">' + LS.overGet + '</div>');
                            content.push('</div>');
                            content.push('</div>');
                            popupCouponMsg(content.join(''));
                        } else {
                            var content = [];
                            content.push('<div class="mobiCouponMsgPanl2">');
                            content.push('<div class="mobiCouponMsg">');
                            content.push('<div class="fBottom">' + LS.isNoneCoupon + '</div>');
                            content.push('</div>');
                            content.push('</div>');
                            popupCouponMsg(content.join(''));
                        }
                    }
                },
                error: function () {
                    Mobi.ing(LS.systemError);
                }
            });
        }
    };

    Mobi.customPopup(options);
}
Mobi.initNewGuestGiftInfo();


/**
 * 展示新聞
 * @param pageNum
 */
function showPage(pageNum){

    var data = {"p": pageNum};
    $.ajax({
        url: CONTEXT_PATH + "/index/list",
        type: "post",
        dataType: "html",
        data: data
    }).done(function (html) {
        $("#listBox").html(html);
    });
}


function newsPageChange(select){

}

/**
 * 展示新聞
 * @param pageNum
 */
function newsPage(pageNum){
    var data = {"p": pageNum};
    $.ajax({
        url: CONTEXT_PATH + "/news/list",
        type: "post",
        dataType: "html",
        data: data
    }).done(function (html) {
        $("#newsBox").html(html);
    });
}