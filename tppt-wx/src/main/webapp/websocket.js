
//用于展示用户的聊天信息
Ext.define('MessageContainer', {

	extend : 'Ext.view.View',

	trackOver : true,

	multiSelect : false,

	itemCls : 'l-im-message',

	itemSelector : 'div.l-im-message',

	overItemCls : 'l-im-message-over',

	selectedItemCls : 'l-im-message-selected',

	style : {
		overflow : 'auto',
		backgroundColor : '#fff'
	},

	tpl : [
			'<div class="l-im-message-warn">​交谈中请勿轻信汇款、中奖信息、陌生电话。 请遵守相关法律法规。</div>',
			'<tpl for=".">',
			'<div class="l-im-message">',
			'<div class="l-im-message-header l-im-message-header-{source}">{from}  {timestamp}</div>',
			'<div class="l-im-message-body">{content}</div>', '</div>',
			'</tpl>'],

	messages : [],

	initComponent : function() {
		var me = this;
		me.messageModel = Ext.define('Leetop.im.MessageModel', {
					extend : 'Ext.data.Model',
					fields : ['from', 'timestamp', 'content', 'source']
				});
		me.store = Ext.create('Ext.data.Store', {
					model : 'Leetop.im.MessageModel',
					data : me.messages
				});
		me.callParent();
	},

	//将服务器推送的信息展示到页面中
	receive : function(message) {
		var me = this;
		message['timestamp'] = Ext.Date.format(new Date(message['timestamp']),
				'H:i:s');
		if(message.from == user){
			message.source = 'self';
		}else{
			message.source = 'remote';
		}
		me.store.add(message);
		if (me.el.dom) {
			me.el.dom.scrollTop = me.el.dom.scrollHeight;
		}
	}
});

var xmove=0;
var ymove=0;
/**
 * 显示窗口
 * @param win
 */
function showWin(win){
	if(!win.x){
		win.x=(Ext.getBody().getWidth()-win.width)/2+xmove;
		win.y=(Ext.getBody().getHeight()-win.height)/2+ymove;
		if(xmove==0){
			xmove=20;
			ymove=20;
		}
		else{
			xmove=0;
			ymove=0;
		}
	}
	win.show();
}

/**
 * 对话框组
 */
var wingroup = new Ext.WindowGroup();
/**
 * 处理收到的消息
 * @param msg
 */
function showMsg(msg){
	var userId=msg.from;
	//看窗口是否存在
	//如果存在，
	if(Ext.getCmp("msgWin_"+userId)){
		//且显示了，则置顶
		if(Ext.getCmp("msgWin_"+userId).hidden==false)
			showWin(Ext.getCmp("msgWin_"+userId));//.show();
			//wingroup.bringToFront("msgWin_"+userId);
		//闪烁头像
		else{
			var node=onlineUser.getRootNode().findChild('id',userId);
			node.set("stopBrink","");
			//brinkNode(userId);
		}
	}
	//否则创建一个窗体，但不显示
	else{
		var node=onlineUser.getRootNode().findChild('id',userId);
		createMsgWin(node,true);
		//brinkNode(userId);
	}
	//
	tryBrinkNode(userId);
	//展示接收的消息
	Ext.getCmp("output_"+userId).receive(msg);
}

/**
 * 闪烁节点事件
 * @param userId
 */
function tryBrinkNode(userId){

	var node=onlineUser.getRootNode().findChild('id',userId);
	if(node.get("isBrinking")=="1")
		return;
	else
		brinkNode(userId);
}



/**
 * 闪烁节点
 */
function brinkNode(userId){
	var node=onlineUser.getRootNode().findChild('id',userId);
	node.set("isBrinking","1");
	
	if(!node.get("oldText"))
		node.set("oldText",node.get("text"));
	if(node.get("stopBrink")=="true"){
		node.set("text",node.get("oldText"));
		node.set("isBrinking",null);
	}
	else{
		if(node.get("b")=="1"){
			node.set("b",0);
			node.set("text",node.get("oldText"));
		}else{
			node.set("b",1);
			node.set("text","<font color=\"red\">"+node.get("oldText")+"<font/>");
		}
		window.setTimeout(function(){
			brinkNode(userId);
		},500);
	}
}



/**
 * 点击用户时创建一个对话框
 * @param user
 */
function createMsgWin(node,notShowWin){

	//去用户数据
	node=node.data;
	if(Ext.getCmp("msgWin_"+node.id)){
		showWin(Ext.getCmp("msgWin_"+node.id));
		//Ext.getCmp("msgWin_"+node.id).show();
		return;
	}
	
	
	//创建用户输入框
	var input = Ext.create('Ext.form.field.HtmlEditor', {
				region : 'south',
				height : 120,
				id:"input_"+node.id,
				enableFont : false,
				enableSourceEdit : false,
				enableAlignments : false,
				listeners : {
					initialize : function() {
						Ext.EventManager.on(me.input.getDoc(), {
									keyup : function(e) {
										if (e.ctrlKey === true
												&& e.keyCode == 13) {
											e.preventDefault();
											e.stopPropagation();
											send();
										}
									}
								});
					}
				}
			});
	//创建消息展示容器
	var output = Ext.create('MessageContainer', {
				region : 'center',
				id:"output_"+node.id
			});

	var dialog = Ext.create('Ext.panel.Panel', {
				region : 'center',
				layout : 'border',
				id:"dialog_"+node.id,
				items : [input, output],
				buttons : [{
							text : '发送',
							handler : function(){
								send(input,output)
								}
						},
						{
							text : '关闭',
							handler : function(){
								Ext.getCmp("msgWin_"+node.id).hide();
								}
						}
				]
			});
	
	//聊天窗口
	var win = Ext.create('Ext.window.Window', {
				title : "当前用户:"+node.text ,
				layout : 'border',
				id:"msgWin_"+node.id,
				iconCls : 'user-win',
				minWidth : 650,
				minHeight : 460,
				width : 650,
				animateTarget : 'websocket_button',
				height : 460,
				items : [dialog],
				border : false,
				closeAction: "hide",
				listeners : {
					render : function() {
						//initWebSocket();
					}
				}
			});
	if(!notShowWin)
		showWin(win);//.show();
	else
		win.hide();
	//保存
	//wingroup.register(win);
}

//节点点击事件  
/**
 * 
 */
function node_click( node, event )    
{    
    var userId = event.data.id ;   
    var node=onlineUser.getRootNode().findChild('id',userId);
	node.set("stopBrink","true");
    createMsgWin(event);
      
};    
var input;
var onlineUser;
var winUserList;
var websocket;
var send;//发送事件

Ext.onReady(function() {
//			//创建用户输入框
//			input = Ext.create('Ext.form.field.HtmlEditor', {
//						region : 'south',
//						height : 120,
//						enableFont : false,
//						enableSourceEdit : false,
//						enableAlignments : false,
//						listeners : {
//							initialize : function() {
//								Ext.EventManager.on(me.input.getDoc(), {
//											keyup : function(e) {
//												if (e.ctrlKey === true
//														&& e.keyCode == 13) {
//													e.preventDefault();
//													e.stopPropagation();
//													send();
//												}
//											}
//										});
//							}
//						}
//					});
//			//创建消息展示容器
//			var output = Ext.create('MessageContainer', {
//						region : 'center'
//					});
//
//			var dialog = Ext.create('Ext.panel.Panel', {
//						region : 'center',
//						layout : 'border',
//						items : [input, output],
//						buttons : [{
//									text : '发送',
//									handler : function(){send}
//								}]
//					});

			//初始话WebSocket
			function initWebSocket() {
				if (window.WebSocket) {
					websocket = new WebSocket(encodeURI('ws://localhost:8050/bk/message'));
					websocket.onopen = function() {
						//连接成功
						winUserList.setTitle(title + '</br>&nbsp;&nbsp;(状态：已连接)');
					}
					websocket.onerror = function() {
						//连接失败
						winUserList.setTitle(title + '</br>&nbsp;&nbsp;(状态：连接发生错误)');
					}
					websocket.onclose = function() {
						//连接断开
						winUserList.setTitle(title + '</br>&nbsp;&nbsp;(状态：已经断开连接)');
					}
					//消息接收
					websocket.onmessage = function(message) {
						var message = JSON.parse(message.data);
						//接收用户发送的消息
						if (message.type == 'message') {
							showMsg(message);
							//output.receive(message);
						} else if (message.type == 'get_online_user') {
							//获取在线用户列表
							var root = onlineUser.getRootNode();
							Ext.each(message.list,function(user){
								if(!root.findChild('id',user.userId)){
									var node = root.createNode({
										id : user.userId,
										text : user.user,
										iconCls : 'user',
										leaf : true
									});
									root.appendChild(node);
								}
							});
						} else if (message.type == 'user_join') {
							//用户上线
								var root = onlineUser.getRootNode();
								var user = message.user;
								var node = root.createNode({
									id : user.userId,
									text : user.user,
									iconCls : 'user',
									leaf : true
								});
								root.appendChild(node);
						} else if (message.type == 'user_leave') {
								//用户下线
								var root = onlineUser.getRootNode();
								var user = message.user;
								var node = root.findChild('id',user.userId);
								root.removeChild(node);
						}
					}
				}
			};

			//在线用户树
			 onlineUser = Ext.create('Ext.tree.Panel', {
						title : '客户列表',
						rootVisible : false,
						region : 'east',
						width : 250,
						lines : false,
						useArrows : true,
						autoScroll : true,
						split : true,
						iconCls : 'user-online',
						store : Ext.create('Ext.data.TreeStore', {
									root : {
										text : '在线用户',
										expanded : true,
										children : []
									}
								}),
						listeners:{ 
							itemclick:node_click
						}  
					});
			
			var title = '欢迎您：' + user;
//			//聊天窗口
//			var win = Ext.create('Ext.window.Window', {
//						title : title + '&nbsp;&nbsp;(未连接)',
//						layout : 'border',
//						iconCls : 'user-win',
//						minWidth : 650,
//						minHeight : 460,
//						width : 650,
//						animateTarget : 'websocket_button',
//						height : 460,
//						items : [dialog],
//						border : false,
//						listeners : {
//							render : function() {
//								//initWebSocket();
//							}
//						}
//					});
//			win.show();
			

			//用户列表
			 winUserList = Ext.create('Ext.window.Window', {
						title : title + '&nbsp;&nbsp;(未连接)',
						layout : 'border',
						iconCls : 'user-win',
						minWidth : 250,
						minHeight : 560,
						minimizable: true,
						maximizable: true,
						closeAction: "hide",
						width : 150,
						animateTarget : 'websocket_button',
						height : Ext.getBody().getHeight(),
						x:Ext.getBody().getWidth()-250,
						items : [onlineUser],
						border : false,
						listeners : {
							render : function() {
								initWebSocket();
							},
							 minimize: function (win, opts) {
							        win.collapse();
							}
						}
					});

			winUserList.show();

			//发送消息
			send=function(input,output){
				var message = {};
				if (websocket != null) {
					if (input.getValue()) {
						Ext.apply(message, {
									from : user,
									content : input.getValue(),
									timestamp : new Date().getTime(),
									type : 'message'
								});
						websocket.send(JSON.stringify(message));
						output.receive(message);
						input.setValue('');
					}
					else{
						Ext.Msg.alert('提示', '消息不能为空！'); 
					}
				} else {
					Ext.Msg.alert('提示', '您已经掉线，无法发送消息!');
				}
			}
		});
