package com.vote.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.api.PaymentApi.TradeType;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import com.vote.model.*;
import com.vote.util.ApiConfigUtil;
import com.vote.util.VoteCheckUtil;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 感谢 *半杯* 童鞋联调支付API
 *
 * @author L.cm
 */
public class PayController extends BaseController {
    private static Logger logger = Logger.getLogger(PayController.class);

    //商户相关资料
    //private static String appid = "";
    private static String partner = "";
    private static String paternerKey = "";
    private static String notify_url = "";

    private static ApiConfig apiConfig = null;

//    /**
//     * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
//     * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
//     */
//    public ApiConfig getApiConfig() {
//
//        ApiConfig ac = new ApiConfig();
//        // 配置微信 API 相关常量
//        ac.setToken(PropKit.get("token"));
//        ac.setAppId(PropKit.get("appId"));
//        ac.setAppSecret(PropKit.get("appSecret"));
//
//        /**
//         *  是否对消息进行加密，对应于微信平台的消息加解密方式：
//         *  1：true进行加密且必须配置 encodingAesKey
//         *  2：false采用明文模式，同时也支持混合模式
//         */
//        ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
//        ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
//        apiConfig = ac;
//        return ac;
//    }

    /**
     * 公众号支付js-sdk
     */
    public void index() {

        if (apiConfig == null) {
            ApiConfigKit.setThreadLocalApiConfig(ApiConfigUtil.getApiConfig());
            apiConfig = ApiConfigKit.getApiConfig();

            //appid=apiConfig.getAppId();
            partner = PropKit.get("wx.partner");
            paternerKey = PropKit.get("wx.paternerKey");
            notify_url = PropKit.get("wx.notify_url");
        }

        //选手ID
        Integer mid = getParaToInt("mid");
        Event event=Event.dao.findById(getEventId());

        //补充活动信息
        if(mid!=null&&event==null){
            Merchant model = Merchant.dao.findById(mid);
            if (model != null) {
                event = Event.dao.findFirstById(model.getInt("eventid"));
                if(event!=null) {
                    setSessionAttr("event", event);
                    setEventId(model.getInt("eventid"));
                }
            }
        }
        //检查是否可投票
        VoteCheckUtil voteCheckUtil = new VoteCheckUtil();
        Integer checkResult = voteCheckUtil.voteCheck(getEventId(), getOpenId(), mid);
        //不可VIP投票
        if (checkResult != null && checkResult==-2) {
            logger.error("超过了投票限制，不可投票");
            render("error.html");
            return;
        }


        Integer tickets = getParaToInt("tickets");
        Merchant mModel= Merchant.dao.findById(mid);
        Double price =Double.parseDouble(event.getFloat("vipprice").toString());
        Double amount = price * tickets;
        VoteList model = new VoteList();
        model.set("eventid", getEventId());
        model.set("merchantid", mid);
        model.set("openid", getOpenId());
        model.set("votetype", 2);
        model.set("status", 2);
        model.set("vote_time", new Date());
        model.set("version", 1);
        model.set("tickets", tickets);
        model.set("amount", amount);
        //避免用戶重复
        String out_trade_no = System.currentTimeMillis() +"_"+ getUserId()+"_"+(int)(Math.random()*10);
        model.set("orderid",out_trade_no);
        model.save();

        setAttr("ac",apiConfig);
        setAttr("amount",amount);
        setAttr("eModel",event);
        setAttr("mModel",mModel);

        Map<String, String> packageParams = submitOrder(model);
        if (packageParams != null) {
            setAttr("packageParams", JsonUtils.toJson(packageParams));
        }
        render("pay.html");
    }


    /**
     * 订单提交微信，并入库
     *
     * @param model
     * @return
     */
    private Map<String, String> submitOrder(VoteList model) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", apiConfig.getAppId());
        params.put("mch_id", partner);
        params.put("body", "打赏");
        String out_trade_no =model.get("orderid");
        params.put("out_trade_no", out_trade_no);
        params.put("total_fee", String.valueOf(Double.valueOf(model.getDouble("amount") * 100).intValue()));
        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        params.put("spbill_create_ip", ip);
        params.put("trade_type", PaymentApi.TradeType.JSAPI.name());
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        params.put("notify_url", notify_url);
        params.put("openid", getOpenId());

        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);
        logger.info("订单参数:" + JsonUtils.toJson(params));
        String xmlResult = PaymentApi.pushOrder(params);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        logger.info("订单提交结果:" + JsonUtils.toJson(result));
        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        String result_code = result.get("result_code");


        //订单入库
        Order order = new Order();
        order.set("appid", apiConfig.getAppId());
        order.set("out_trade_no", out_trade_no);
        order.set("openId", getOpenId());
        order.set("mch_id", partner);
        order.set("cash_fee", Double.valueOf(model.getDouble("amount")*100F).intValue());
        order.set("total_fee",Double.valueOf(model.getDouble("amount")*100F).intValue());
        order.set("result_code", return_code);
        order.set("err_code", return_code);
        order.set("err_code_des",return_msg);
        order.set("result_code", model.getFloat("return_code"));
        order.set("status",0);
        order.set("result_code","");
        order.set("eventid",model.getInt("eventid"));
        order.set("merchantid",model.getInt("merchantid"));
        Map<String, String> packageParams = new HashMap<String, String>();
        if (!StrKit.isBlank(return_code) && "SUCCESS".equals(return_code) && !StrKit.isBlank(result_code) && "SUCCESS".equals(result_code)) {
            // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
            String prepay_id = result.get("prepay_id");
            packageParams.put("appId", apiConfig.getAppId());
            packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
            packageParams.put("nonceStr", System.currentTimeMillis() + "");
            packageParams.put("package", "prepay_id=" + prepay_id);
            packageParams.put("signType", "MD5");
            String packageSign = PaymentKit.createSign(packageParams, paternerKey);
            packageParams.put("paySign", packageSign);
            order.set("transaction_id", prepay_id);
            order.save();
            logger.info("支付对象:" + JsonUtils.toJson(packageParams));
        }else{
            logger.error("订单提交结果:" + JsonUtils.toJson(result));
            logger.error("支付对象:" + JsonUtils.toJson(packageParams));
        }
        //String jsonStr = JsonUtils.toJson(packageParams);
        return packageParams;
    }

    /**
     * 支付成功通知
     */
    public void pay_notify() {
        // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
        String xmlMsg = HttpKit.readData(getRequest());
        logger.debug("支付通知=" + xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

        String result_code = params.get("result_code");
        // 总金额
        String totalFee = params.get("total_fee");
        // 商户订单号
        String orderId = params.get("out_trade_no");
        // 微信支付订单号
        String transId = params.get("transaction_id");
        // 支付完成时间，格式为yyyyMMddHHmmss
        String timeEnd = params.get("time_end");
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 避免已经成功、关闭、退款的订单被再次更新
        if (PaymentKit.verifyNotify(params, paternerKey)) {
            if (("SUCCESS").equals(result_code)) {
                //更新订单信息
                logger.debug("更新订单信息");
                Order order=Order.dao.findFirstByTradeNo(orderId);
                if(order!=null&&!order.getInt("status").equals("1")){
                    order.set("result_code",result_code);
                    order.set("time_end",timeEnd);
                    order.set("status",1);
                    order.update();
                    logger.debug("更新订单"+transId+"成功！");

                    try {
                        //更新用户的选手，活动ID
                        VoteUser user = VoteUser.dao.findUserByOpenid(order.getStr("openId"));
                        user.set("eventid",order.getInt("eventid"));
                        user.set("merchantid",order.getInt("merchantid"));
                        user.update();
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error(e.toString());
                        logger.error("更新用户的选手，活动信息失败:"+order.toJson());
                    }
                }
                Map<String, String> xml = new HashMap<String, String>();
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                renderText(PaymentKit.toXml(xml));
                return;
            }
        }
        renderText("");
    }


}
