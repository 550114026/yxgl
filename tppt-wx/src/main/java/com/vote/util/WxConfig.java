package com.vote.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WxConfig {
    Boolean debug=false;
    String appId;
    String timestamp;
    String nonceStr;
    String signature;
    List<String> jsApiList=new ArrayList<>();
}
