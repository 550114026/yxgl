package com.yqglpt.common;

import com.vote.model.VoteUser;
import org.apache.commons.lang.StringEscapeUtils;

import com.jfinal.core.Controller;
import com.yqglpt.model.User;

public class BaseController extends Controller {

    public int getPageNum() {
        if (!isParaBlank("page")) {// 页码
            return getParaToInt("page");
        } else return 1;
    }

    public int getPageSize() {
        if (!isParaBlank("limit")) {// 页码
            return getParaToInt("limit");
        }
//		if (!isParaBlank("start")&&!isParaBlank("limit")) {// 页码
//			return getParaToInt("limit")-getParaToInt("start");
//		}
//		else 
        return 10;
    }

    public String getSearchText() {
        if (!isParaBlank("s")) {// 页码
            return StringEscapeUtils.unescapeJava(getPara("s"));
        }
        return "";
    }

    public VoteUser getUser() {
        return getSessionAttr("vote_user");
    }

    public String getUserId() {
        VoteUser user = getUser();
        if (user != null)
            return user.getInt("id").toString();
        return "";
    }

    public String getOpenId() {
        VoteUser user = getUser();
        if (user != null)
            return user.getStr("openid");
        return "";
    }


    public String getNickName() {
        VoteUser user = getUser();
        if (user != null)
            return user.getStr("NAME");
        return "";
    }

    /**
     * 设置活动ID
     *
     * @param eventId
     */
    public void setEventId(Integer eventId) {
        setSessionAttr("event_id", eventId);
    }

    /**
     * 获取活动ID
     *
     * @return
     */
    public Integer getEventId() {
        Object eid = getSessionAttr("event_id");
        if (eid != null) {
            return Integer.valueOf(eid.toString());
        } else {
            return null;
        }
    }


    public Boolean isAdmin() {
        VoteUser user = getSessionAttr("vote_user");
        if (user != null)
            return user.getInt("user_type").toString().equals("2");
        return false;
    }
}
