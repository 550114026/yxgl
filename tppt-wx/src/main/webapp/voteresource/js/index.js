function isNumber(val) {
    var regPos = /^\d+$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if (regPos.test(val) || regNeg.test(val)) {
        return true;
    } else {
        return false;
    }

}

var captchaAppId = "2086779568";
var captchaEnabled = false;

/**
 * 搜索
 */
function search(eid) {
    var number = jm("#txtSearch").val();
    if (number.length > 0) {
        if (isNumber(number)) {
            var url = CONTEXT_PATH + "/event/search?no=" + number;
            if (eid) {
                url = url + "&eid=" + eid;
            }
            window.location.href = url;
        } else {
            Mobi.ing("请输入正确选手序号", true);
        }
    } else {
        Mobi.ing("请输入选手序号", true);
    }

}


function showMore(box, eid) {
    var p = jm(box).data("no");
    if (p != null) {
        p = parseInt(p) + 1;
    }
    var data = {"eid": eid, "p": p};
    jm.ajax({
        url: CONTEXT_PATH + "/event/list",
        type: "post",
        data: data,
        success: function (result) {
            if (result.length > 0) {
                jm(box).data("no", p);
                jm("#list-contaner").append(result);
            }
            else {
                jm(box).hide();
                Mobi.ing("没有更多了！");
            }
        },
        error: function () {
            Mobi.ing("加载失败！");
        }
    });

}


/**
 * 搜索
 */
function vote(id, type) {
    if (voteEnded) {
        Mobi.ing("活动已结束", true)
        return;
    }

    if (captchaEnabled) {
        var captcha = new TencentCaptcha(captchaAppId, function (res) {
            if(res.ret === 0){
                sendVote(id, res);
            }
        }, {bizState: id});
        captcha.show(); // 显示验证码
    } else {
        sendVote(id);
    }
}

function sendVote(id, res) {
    jm("#vipvote").data("mid", id);
    var data = {"mid": id};
    if (typeof(res) != "undefined") {
        data.appId=captchaAppId;
        data.ticket=res.ticket;
        data.randstr=res.randstr;
    }
    jm.ajax({
        url: CONTEXT_PATH + "/event/vote",
        type: "post",
        dataType: "json",
        data: data,
        success: function (resultVO) {
            if (resultVO.success) {
                Mobi.ing("投票成功！", true);
                jm("#ptvote").html("今日已投票");
                var mid = jm("#vipvote").data("mid");
                //首页选手总票数
                var boxtotle = jm("#boxtotle" + mid);
                if (boxtotle.length > 0) {
                    var totlevotes = boxtotle.data("totlevotes");
                    boxtotle.text(parseInt(totlevotes) + 1);
                }
                //活动累计票数
                var petotlevotes = jm("#petotlevotes");
                if (petotlevotes.length > 0) {
                    var etotlevotes = petotlevotes.data("totlevotes");
                    petotlevotes.text(parseInt(etotlevotes) + 1);
                }
                //选手总票数
                var ptotlevotes = jm("#ptotlevotes" + mid);
                if (ptotlevotes.length > 0) {
                    var mtotlevotes = ptotlevotes.data("totlevotes");
                    ptotlevotes.text(parseInt(mtotlevotes) + 1);
                }
            }
            else if (resultVO.resultCode == -1) {
               // if (typeof(type) == "undefined") {
                    Mobi.ing("今日您已参与投票！", true);
                // } else {
                //     //已超限制，跳转VIP
                //     jm("#vipvote").show();
                // }
            } else if (resultVO.resultCode == -9) {
                Mobi.ing("投票已结束，不可再投票！", true);
            } else if (resultVO.resultCode == -2) {
                Mobi.ing("已超过限制，不可再投票！", true);
            }else if(resultVO.resultMessage!=""){
                Mobi.ing(resultVO.resultMessage, true);
            }
            else {
                Mobi.ing("投票失败！", true);
            }
        },
        error: function () {
            Mobi.ing("投票失败！", true);
        }
    });


}


function reward() {
    var div = jm("#vipvote").find(".activ").eq(0);
    if (div.length == 0) {
        Mobi.ing("请选择票数！", true);
        return;
    }
    var input = div.find("input").eq(0);
    var tickets = 0;
    if (input.length > 0) {
        tickets = input.val();
    } else {
        tickets = div.data("val");
    }
    window.location.href = CONTEXT_PATH + "/pay?tickets=" + tickets + "&mid=" + jm("#vipvote").data("mid");
}

function pay() {
    jm("#vipvote").show();
}

jm(document).ready(function () {
    jm("#vipvote").find(".vbtn").on("click", function () {
        jm("#vipvote").find(".vbtn").removeClass("activ");
        jm(this).addClass("activ");
    })
    jm("#vipvotecover").on("click", function (e) {
        if (e.srcElement == this) {
            jm("#vipvote").fadeOut();
        }
    })
})