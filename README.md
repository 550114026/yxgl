# tppt

#### 项目介绍
投票平台

#### 软件架构
软件架构说明
1.tppt-web 后端管理台
2.tppt-wx  微信投票公众号
3.sql  数据库
4.nginx 配置参考

#### 安装教程

0. 执行数据库脚本

1. 部署web

    配置以下节点：    
    
    #mysql数据库配置
     master.driver=com.mysql.jdbc.Driver
     master.dbType = mysql
     master.url = jdbc:mysql://0000:3306/tppt?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
     master.username = root
     master.password = xxx
       
    #web部署路径
    config.basePath =/web
    
    #微信端访问地址
    wx.uri=http://localhost:8080/vote
    
    #图片服务器地址（nginx静态映射）
    imageSite.Url=http://localhost:8800/images
    #图片保存路径（nginx映射文件目录）
    images.savePath=D:\\images
    

2. 部署wx

    配置以下节点：    
    #数据库类型，固定
    dbType =mysql
    #mysql数据库配置
    jdbcUrl = jdbc:mysql://59.110.137.168:3306/tppt?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
    password =Linbiao523@sohu
    user =root
    
    #公众号基础参数配置##
    # 微信服务器回调所用的 token，必须和公众号上设置的一致（按实际修改）
    token=
    # 公众号appID（按实际修改）
    appId=wx7xxxxxc40cb
    #公众号安全密钥（按实际修改）
    appSecret=bdcxxxx0e1b56f710e67
    #是否对消息进行加密，是否对消息进行加密，对应于微信平台的消息加解密方式，false支持明文模式及兼容模式，true支持安全模式及兼容模式
    encryptMessage=false
    #消息加密密钥
    encodingAesKey=gGDitrstEJEDlEIsnoUTCPFQ9QxGJKFQnhW98GrrLrD
    
    #############系统参数配置#################################
    #图片保存路径
    images.savePath=/root/tppt/images
    #图片访问服务器地址（共享WEB和微信的图片）
    imageSite.Url=http://www.xxx.com/images
    #站点部署生产后的访问路径，（按实际修改）
    webSite.Url=http://www.s.com
    #
    webSite.name=投票营销
    #部署路径
    webSite.basePath=/vote
    
    #微信支付分配的appid
    wx.appId=111
    #微信支付分配的商户号（按实际修改）
    wx.partner=222
    #支付相关API密钥（按实际修改）
    wx.paternerKey=ebd702052ea082c6a
    #微信支付通知地址
    wx.notify_url=http://www.xxx.com.cn/vote/pay_notify
    
    #验证码配置（不启用）
    tencentCaptcha.enabled=false

3. 部署nginx

    配置web端、wx端、web端上传的图片访问。
    配置图片压缩，缓存等
    
    
4. 登录密码
    admin/123456

