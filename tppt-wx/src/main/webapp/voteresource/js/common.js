


(function() {
//ie678对jqmobi包是不支持的，所以只要提取到前面来
    if (typeof Mobi == 'undefined') {
        Mobi = {};
    }
    Mobi.CHROME = function (userAgent) {
        return userAgent.match(/Chrome/) ? true : false
    };
    Mobi.OPERA = function (userAgent) {
        return userAgent.match(/Opera/) ? true : false
    };
    Mobi.FENNEC = function (userAgent) {
        return userAgent.match(/fennec/i) ? true : userAgent.match(/Firefox/) ? true : false
    };
    Mobi.WEBKIT = function (userAgent) {
        return userAgent.match(/WebKit\/([\d.]+)/) ? true : false
    };
    Mobi.isIE6 = function (userAgent) {
        return userAgent.toUpperCase().match(/MSIE 6.0/i) ? true : false;
    }
    Mobi.isIE7 = function (userAgent) {
        return userAgent.toUpperCase().match(/MSIE 7.0/i) ? true : false;
    }
    Mobi.isIE8 = function (userAgent) {
        return userAgent.toUpperCase().match(/MSIE 8.0/i) ? true : false;
    }
    Mobi.isIE = function (userAgent) {
        return userAgent.toUpperCase().match(/MSIE /i) ? true : false;
    }
    Mobi.isSupportBrowser = function () {
        var userAgent = navigator.userAgent,
            browserSupported = false;
        if (Mobi.CHROME(userAgent) || Mobi.OPERA(userAgent) || Mobi.FENNEC(userAgent) || Mobi.WEBKIT(userAgent)) {
            browserSupported = true;
        }
        if (Mobi.isIE(userAgent)) {
            if (Mobi.isIE6(userAgent) || Mobi.isIE8(userAgent) || Mobi.isIE7(userAgent)) {
                browserSupported = false;
            } else {
                browserSupported = true;
            }
        }
        if (!browserSupported) {
            if (userAgent == null) {
                userAgent = '';
            }
            if (userAgent.indexOf('UCWEB') >= 0) {
                browserSupported = false;
            }
            if (!browserSupported) {
                document.body.innerHTML = '';
                document.body.style.backgroundColor = '#fff';
                var tips = ''
// 这里不用printErrorPage是因为一些功能机是不支持html的，直接输出文字就好了
                if (userAgent.indexOf('UCWEB') >= 0) {
// uc的极速模式
                    document.write('<p class="sorryTips">抱歉 O(>_<)O</p>');
                    document.write('<p class="sorryTips">不支持UC浏览器的“极速模式”访问，请点击UC浏览器右上角的菜单关闭“极速模式”。</p>');
                } else if (userAgent.indexOf('TencentTraveler') >= 0) {
                    document.write('<p class="sorryTips" style="color:#b0afae">请点击右上角的“查看原网页”</p>');
                    document.write('<p class="sorryTips"></p>');
                } else {
                    document.write('<p class="sorryTips">抱歉 O(>_<)O</p>');
                    document.write('<p class="sorryTips">您的浏览器无法浏览触屏版网站。</p>');
                }
                document.close();
                return true;
            }
        }
        return false;
    };
})();
(function () {
    var _isSupportBrowser = Mobi.isSupportBrowser();
    if (_isSupportBrowser) {
        return;
    }
})();