package com.example.demo_musicplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dancing.demo_musicplay.R;
import com.example.db.GroupEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {
	
	private final List<GroupEntity> list = new ArrayList<GroupEntity>();
	private final Context context;
	private LayoutInflater infater = null;

	public GroupAdapter(Context context, List<GroupEntity> list) {
		this.infater = LayoutInflater.from(context);
		this.context = context;
		this.list.addAll(list);
	}
	
	public void setData(List<GroupEntity> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = infater.inflate(R.layout.layout_adapter, null);
			holder.iv_logo = (ImageView) convertview.findViewById(R.id.iv_logo);
			holder.tv_title = (TextView) convertview.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertview.findViewById(R.id.tv_content);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.iv_logo.setVisibility(View.GONE);
		holder.tv_title.setText(list.get(position).getGroup());
		holder.tv_content.setVisibility(View.GONE);
		return convertview;
	}
	
	private class ViewHolder {
		
        private ImageView iv_logo;
		private TextView tv_title;
		private TextView tv_content;
	}
}
