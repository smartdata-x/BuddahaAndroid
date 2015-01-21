package book.entity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.util.ToolUtil;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-27 下午10:28:24
 * 类说明
 */
public class BookGridAdapter extends BaseAdapter {
	ArrayList<BookInfo> list;
	Context con;
	String path;
	Bitmap bitmap;

	public BookGridAdapter(Context con, ArrayList<BookInfo> list) {
		super();
		this.con = con;
		if (list != null)
			this.list = list;

		path = ImageUtil.initImagePath(con, ImageUtil.ICON_PATH);
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list != null ? list.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		BookGridHolder holder;
		if (view == null) {
			holder = new BookGridHolder();
			view = holder.parent;
		} else {
			holder = (BookGridHolder) view.getTag();
		}
		BookInfo info = list.get(position);
		if (holder != null && info != null) {
			holder.initData(info);
		}
		return view;
	}
	
	class BookGridHolder{
		View parent;
		ImageView iv_icon;
		TextView tv_name;
		
		public BookGridHolder(){
			parent = LayoutInflater.from(con).inflate(
					R.layout.item_book, null);
			iv_icon = (ImageView) parent.findViewById(R.id.iv_icon);
			tv_name = (TextView) parent.findViewById(R.id.tv_name);
			parent.setTag(this);
		}
		
		void initData(BookInfo info) {
			if (info == null)
				return;
			// image
			setImage(info.pic);

			tv_name.setText(info.name);
		}

		void setImage(String url) {
			boolean isSetImage = false;
			if (url != null && url.length() > 0) {
				isSetImage = ImageUtil.setIcon(iv_icon, ToolUtil.md5(url),
						path, bitmap);
			}

			if (!isSetImage)
				iv_icon.setImageResource(R.drawable.around_book);
		}
	
	}

}