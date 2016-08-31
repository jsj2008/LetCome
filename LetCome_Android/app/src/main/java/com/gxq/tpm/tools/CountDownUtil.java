package com.gxq.tpm.tools;

import com.letcome.R;
import com.gxq.tpm.tools.CountDown.OnCountDownListener;

import android.widget.EditText;
import android.widget.TextView;

public class CountDownUtil {
	
     /**
      * 计时器start
      * @param countdown  倒计时器类对象
      * @param listener   倒计时监听器
      * @param interval   间隔时间
      */
     public static void countStart(CountDown countdown,OnCountDownListener listener,int interval){
    	 countdown.setOnCountDownListener(listener);
    	 countdown.startTimer(interval);
     }
     /**
      * 计时器stop
      * @param countdown  
      * @param tvgetvewify
      */
     public static void countStop(CountDown countdown,TextView tvgetvewify){
    	if(null != countdown){
    		 countdown.stopTimer();
 		}
 		setCountTextFinished(tvgetvewify);
     }
     /**
      *  计时器stop
      * @param countdown
      * @param tvgetvewify
      * @param edittext
      */
     public static void countStop(CountDown countdown,TextView tvgetvewify,EditText edittext){
    	 countStop(countdown,tvgetvewify);
    	 edittext.setEnabled(true);
     }
    /**
     * 倒计时中
     * @param remainNum 剩余时间
     * @param tvgetvewify 获取验证码的TextView样式
     */
    public static void setCountTextRun(int remainNum,TextView tvgetvewify){
    	tvgetvewify.setEnabled(false);
    	tvgetvewify.setText(Util.transformString(R.string.phone_verify_countdown, remainNum));
    	tvgetvewify.setBackgroundColor(Util.transformColor(R.color.color_e0e0e0));
    	tvgetvewify.setTextColor(Util.transformColor(R.color.color_bebebe));
	}
    /**
     * 倒计时结束
     * @param tvgetvewify  获取验证码的TextView样式
     */
    public static void setCountTextFinished(TextView tvgetvewify){
    	tvgetvewify.setText(R.string.phone_get_number_verify);
    	tvgetvewify.setBackgroundResource(R.drawable.btn_white_selector);
    	tvgetvewify.setTextColor(Util.transformColor(R.color.text_color_main));
    	tvgetvewify.setEnabled(true);
	}
    /**
     * 倒计时中
     * @param remainNum 剩余时间
     * @param tvgetvewify 获取验证码的TextView样式
     * @param edittext 获取验证码旁边的输入框不可点击
     */
    public static void setCountTextRun(int remainNum,TextView tvgetvewify,EditText edittext){
    	setCountTextRun(remainNum,tvgetvewify);
    	edittext.setEnabled(false);
	}
    /**
     * 倒计时结束
     * @param tvgetvewify 获取验证码的TextView样式
     * @param edittext 获取验证码旁边的输入框不可点击
     */
    public static void setCountTextFinished(TextView tvgetvewify,EditText edittext){
    	setCountTextFinished(tvgetvewify);
    	edittext.setEnabled(true);
	}
    
   
}
