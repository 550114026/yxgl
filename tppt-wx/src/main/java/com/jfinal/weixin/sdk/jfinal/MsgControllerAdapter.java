/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.jfinal;

import com.jfinal.weixin.sdk.msg.in.*;
import com.jfinal.weixin.sdk.msg.in.event.*;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;

/**
 * MsgControllerAdapter 对 MsgController 部分方法提供了默认实现，
 * 以便开发者不去关注 MsgController 中不需要处理的抽象方法，节省代码量
 */
public abstract class MsgControllerAdapter extends MsgController {
	
	protected abstract void processInFollowEvent(InFollowEvent inFollowEvent);
	
	protected abstract void processInTextMsg(InTextMsg inTextMsg);
	
	protected abstract void processInMenuEvent(InMenuEvent inMenuEvent);
	
	protected void processInImageMsg(InImageMsg inImageMsg) {
		
	}
	
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		
	}
	
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
		
	}
	
	protected void processInLocationMsg(InLocationMsg inLocationMsg) {
		
	}
	
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
		
	}
	
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		
	}
	
	protected void processInLocationEvent(InLocationEvent inLocationEvent) {
		
	}
	
	protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {
		
	}
	
	protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent) {

	}

	protected void processInMassEvent(InMassEvent inMassEvent) {

	}

	protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {

	}

	protected void processInCustomEvent(InCustomEvent inCustomEvent) {

	}

	protected void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent) {

	}

	protected void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent) {

	}

	protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent){

	}

	protected void processInPoiCheckNotifyEvent(InPoiCheckNotifyEvent inPoiCheckNotifyEvent) {
		
	}

	protected void processInWifiEvent(InWifiEvent inWifiEvent) {
		// WIFI连网后下发消息 by unas at 2016-1-29
	}

	@Override
	protected void processInUserViewCardEvent(InUserViewCardEvent msg) {

	}

	@Override
	protected void processInSubmitMemberCardEvent(InSubmitMemberCardEvent msg) {

	}

	@Override
	protected void processInUpdateMemberCardEvent(InUpdateMemberCardEvent msg) {

	}

	@Override
	protected void processInUserPayFromCardEvent(InUserPayFromCardEvent msg) {

	}

	@Override
	protected void processInMerChantOrderEvent(InMerChantOrderEvent inMerChantOrderEvent) {

	}

	// 没有找到对应的事件消息
	@Override
	protected void processIsNotDefinedEvent(InNotDefinedEvent inNotDefinedEvent) {

	}

	// 没有找到对应的消息
	@Override
	protected void processIsNotDefinedMsg(InNotDefinedMsg inNotDefinedMsg) {

	}
}
