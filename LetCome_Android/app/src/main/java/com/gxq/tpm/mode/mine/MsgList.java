package com.gxq.tpm.mode.mine;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class MsgList extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = 3697981069234913204L;
	public ArrayList<Msg> records;
	public int unread_count;

	public static class Msg implements Serializable {
        private static final long serialVersionUID = -454744576350166054L;

        public long  sender_id;//送信者ID uid	
        public long msg_id; //消息编号	
        public int msg_type;//	消息类型:0-公告 1-通知 2-活动 3-提醒	
        public String msg_type_name;
        public String message_title;//	消息标题	
        public int read_status;	//消息状态：0-未读，1-已读，2-删除	
        public String Msg_content;	//消息内容	
        public long create_time;	//消息发送时间			
	}
	
    public static class Params implements Serializable{
        private static final long serialVersionUID = 1512253529221177103L;      
//      public long uid;
        public long start_id=0;
        public long limit=50;
        public int msg_type=-1;       //消息类型:0-公告 1-通知 2-活动 3-提醒
        public long read_status=-1;    //消息状态：0-未读，1-已读，2-删除
//       public long from_time=-1;     // 增量拉去时间 from
//      public long to_time=-1;        //增量拉去时间 to
    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		doRequest(params, netBack, RETURN_TYPE);
   	}
    
    public static void doRequest(Params params, ICallBack netBack, int tag) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.MSG_LIST, params, MsgList.class, tag, true);
   	}
}
