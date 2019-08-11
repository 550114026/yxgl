/* 微信支付 */
function wxpay() {
    var data = payDate;
    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady(data), false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady(data));
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(data));
        }
    } else {
        setTimeout(function () {
            onBridgeReady(data);
        },200);
    }
}

function onBridgeReady(json) {
    WeixinJSBridge.invoke(
        'getBrandWCPayRequest',
        json,
        function (res) {
             alert(JSON.stringify(res));
            // document.write(JSON.stringify(payDate));
            // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
            if (res.err_msg == "get_brand_wcpay_request:ok") {
                Mobi.ing("支付成功！", true);
                self.location = CONTEXT_PATH+"/event?eid="+event_id;
            } else {
                Mobi.ing("支付失败！", true);
            }
        }
    );
}
	