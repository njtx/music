package com.example.demo_musicplay;

import java.util.ArrayList;
import java.util.List;
import com.dancing.demo_musicplay.R;
import com.example.db.DBHelper;
import com.example.db.GroupEntity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupListActivity extends Activity implements OnClickListener{

	private ListView mList;
	private List<GroupEntity> mData = new ArrayList<GroupEntity>(); 
	private GroupAdapter mAdapter; 
	private ImageView iv_back;
	private TextView tv_title;
	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.
		onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		db = new DBHelper(this);
		setupView();
	}
	
	public void setupView(){
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("本地列表");
		iv_back.setVisibility(View.GONE);
		
		mList = (ListView) findViewById(R.id.mList);
		mAdapter = new GroupAdapter(this, mData);
		mList.setAdapter(mAdapter);
		getData();
		mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				final String groupid = mData.get(position).getGroupid();
				Log.e("xxx", "groupid = "+groupid);
				if(groupid.equals("1") || groupid.equals("2") || groupid.equals("3")){
					final EditText et = new EditText(GroupListActivity.this);
					new AlertDialog.Builder(GroupListActivity.this).setTitle("请输入新建的列表名称") 
					.setIcon(android.R.drawable.ic_dialog_info) 
					.setView(et) 
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String group = et.getText().toString().trim();
							if(db.isExistGroup(group)){
								Toast.makeText(GroupListActivity.this, "列表名称已经存在", Toast.LENGTH_SHORT).show();
							}else{
								db.insertGroup(group);
								getData();
								Toast.makeText(GroupListActivity.this, "新建列表成功", Toast.LENGTH_SHORT).show();
							}
						}
					}).setNegativeButton("取消", null) .show();
				}else{
					String[] items = {"添加列表","重命名"};  
					new AlertDialog.Builder(GroupListActivity.this).setTitle("提示")   
					.setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {   
					 public void onClick(DialogInterface dialog, int item) {   
//					        Toast.makeText(getApplicationContext(), items[item],   Toast.LENGTH_SHORT).show();  
					         switch(item){
					         	case 0:
					         		final EditText et = new EditText(GroupListActivity.this);
									new AlertDialog.Builder(GroupListActivity.this).setTitle("请输入新建的列表名称") 
									.setIcon(android.R.drawable.ic_dialog_info) 
									.setView(et) 
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											String group = et.getText().toString().trim();
											if(db.isExistGroup(group)){
												Toast.makeText(GroupListActivity.this, "列表名称已经存在", Toast.LENGTH_SHORT).show();
											}else{
												db.insertGroup(group);
												getData();
												Toast.makeText(GroupListActivity.this, "新建列表成功", Toast.LENGTH_SHORT).show();
											}
										}
									}) 
									.setNegativeButton("取消", null) .show();
					         		break;
					         	case 1:
					         		final EditText et_rename = new EditText(GroupListActivity.this);
					         		et_rename.setText(mData.get(position).getGroup());
									new AlertDialog.Builder(GroupListActivity.this).setTitle("请输入修改的列表名称") 
									.setIcon(android.R.drawable.ic_dialog_info) 
									.setView(et_rename) 
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											String group = et_rename.getText().toString().trim();
											if(db.isExistGroup(group)){
												Toast.makeText(GroupListActivity.this, "列表名称已经存在", Toast.LENGTH_SHORT).show();
											}else{
												db.updateGroup(group, groupid);
												getData();
												Toast.makeText(GroupListActivity.this, "重命名成功", Toast.LENGTH_SHORT).show();
											}
										}
									}) 
									.setNegativeButton("取消", null) .show();
					         		break;
					         }
					        dialog.cancel();   
					  }   
					}).show();  
				}
				return false;
			}
		});
		
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(GroupListActivity.this,ListMusic.class);
				i.putExtra("groupid", mData.get(position).getGroupid());
				i.putExtra("groupname", mData.get(position).getGroup());
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getData();
		super.onResume();
	}
	
	public void getData(){
		mData.clear();
		mData.addAll(db.getAllGroup());
		mAdapter.setData(mData);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
//			case R.id.iv_back:
//				finish();
//				break;
		}
	}
}


