package introduce.task;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookInfo;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-9 下午4:14:58
 * 类说明	介绍首页
 */
public class IntroduceHomeTask extends BaseTask{
	
	public IntroduceHomeTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.INTRODUCE_HOME;
		String params = MyUser.getApiBasicParams();

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		
		Object[] objs = new Object[3];
		String[] str = { "his", "thought", "art" };
		ArrayList<BookInfo> list;
		JSONArray array;
		for (int i = 0; i < objs.length; i++) {
			array = jresult.optJSONArray(str[i]);
			list = new ArrayList<BookInfo>();
			if (array != null && array.length() > 0) {
				for (int j = 0; j < array.length(); j++) {
					list.add(new BookInfo(array.optJSONObject(j)));
				}
			}
			objs[i] = list;
		}

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, objs));
		return true;
	}
	
}
