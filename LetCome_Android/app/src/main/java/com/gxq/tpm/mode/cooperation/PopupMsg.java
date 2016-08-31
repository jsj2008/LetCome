package com.gxq.tpm.mode.cooperation;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class PopupMsg extends BaseRes{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6965417385436635323L;
	
	public int id;  //增量ID
	public int sender_id; //送信者ID uid
	public int msg_id; //消息编号
	public int msg_type; //消息类型:0-公告 1-通知 2-活动 3-提醒
	public String msg_type_name; //消息类型名称
	public String message_title; //消息标题
	public int read_status; //消息状态：0-未读，1-已读，2-删除
	public long create_time; //消息发送时间
	public String message; //消息内容
	public int popup;  //弹出类型 0：不弹 1：弹小窗 2： 弹全屏窗

	public static void doRequest(ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.MSG_POPUP_MESSAGE, null, PopupMsg.class, RETURN_TYPE, true);
   	}
	
}
