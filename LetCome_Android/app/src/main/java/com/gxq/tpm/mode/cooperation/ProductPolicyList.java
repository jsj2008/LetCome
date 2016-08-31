package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gxq.tpm.mode.BaseParse;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.Print;

public class ProductPolicyList extends BaseRes{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1621036669941131605L;
	public ArrayList<PolicyItem> records;
	public int stock_total; //	支持的股票数	int
	
	public static class PolicyItem implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1436023340501511095L;
		
		public ArrayList<Policy> policies;
	}
	
	public static class Policy implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 781369919419869923L;
		
		public int id; //	单元id	int
		public int cell_id; //	策略id	int
		public int scheme_id; //	方案id	int
		public String stock_code; //	产品代码	string
		public String stock_name; //	产品名称	string
		public String p_type; //	产品类型	string
		public int investor_id; //	投资人ID	int
		public String dealer_replace; //	投资人代号	string
		public double fund; //	资金	float
		public String fund_str; //	资金是否充足的翻译	string
		public int state; //	状态 0:不可参与 1：可参与	int
		public String end_time_str; //	结束时间翻译	string
		public int fund_state;
		public long expire_time;
		public int left_day_num;
		public String investor_code;
	}

	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8471999665461621171L;
		public String stock_code; 	//	产品代码 0:全部股票	string	TRUE
		public int start_id;	//	自增ID >0:向上翻 <0:	int	TRUE
		public int limit;	//一次获取记录数	int	TRUE
		
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PRODUCT_POLICY_LIST, params, new ProductPolicyListParse(), RETURN_TYPE, false);
   	}
	
	private static class ProductPolicyListParse extends BaseParse<ProductPolicyList>{

		@Override
		protected ProductPolicyList parse(JSONObject jsonObject) {
			ProductPolicyList productPolicyList = new ProductPolicyList();
			productPolicyList.records = new ArrayList<ProductPolicyList.PolicyItem>();
			
			
			try {
				productPolicyList.stock_total = jsonObject.getInt("stock_total");
				Print.e("ccc", "ccc stock_total = "+productPolicyList.stock_total);
				JSONArray jsonArray = jsonObject.getJSONArray("records");
				for (int index = 0; index < jsonArray.length(); index++) {
					Print.e("ccc", "ccc 111111 = " + jsonArray.getJSONArray(index).length());
					ProductPolicyList.PolicyItem policyItem = new ProductPolicyList.PolicyItem();
					policyItem.policies = new ArrayList<ProductPolicyList.Policy>();
					for (int i = 0; i < jsonArray.getJSONArray(index).length(); i++) {
						JSONObject object = jsonArray.getJSONArray(index).getJSONObject(i);
						Policy policy = new Policy();
						policy.id = parseInteger(object, "id");
						policy.cell_id = parseInteger(object, "cell_id");
						policy.scheme_id = parseInteger(object, "scheme_id");
						policy.stock_code= parseString(object, "stock_code");
						policy.stock_name= parseString(object, "stock_name");
						policy.p_type= parseString(object, "p_type");
						policy.investor_id= parseInteger(object, "investor_id");
						policy.fund = parseDouble(object, "fund");
						policy.dealer_replace = parseString(object, "dealer_replace");
						policy.state = parseInteger(object, "state");
						policy.fund_state = parseInteger(object, "fund_state");
						policy.expire_time = parseLong(object, "expire_time");
						policy.left_day_num = parseInteger(object, "left_day_num");
						policy.investor_code = parseString(object, "investor_code");
						policy.end_time_str = parseString(object, "end_time_str");
						policyItem.policies.add(policy);
					}
					productPolicyList.records.add(policyItem);
				}
				Print.e("ccc", "ccc length = "+jsonArray.length());
				return productPolicyList;
			} catch (JSONException e) {
				return null;
			}
		}
		
	}

}
