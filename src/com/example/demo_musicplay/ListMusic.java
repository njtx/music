package com.example.demo_musicplay;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.dancing.demo_musicplay.R;
import com.example.db.DBHelper;
import com.example.db.GroupChildEntity;
import com.example.db.GroupEntity;
import com.example.db.PathEntity;

public class ListMusic extends Activity {
	
	ImageButton play,pause,forward,next,stop,back,ib_mode;
	private MediaPlayer mediaPlayer;
	private List<String> audioList=new ArrayList<String>();
	private int currentItem=0;
	public static String mode = C.mode_1;
    SharedPreferencesSettings sps;
	DBHelper db;
	TextView tv_title;
	String groupid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listmusic);
		db = new DBHelper(this);
		sps = new SharedPreferencesSettings(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getIntent().getStringExtra("groupname"));
		groupid = getIntent().getStringExtra("groupid");
		mediaPlayer=new MediaPlayer();
		play=(ImageButton) super.findViewById(R.id.play);
		stop=(ImageButton) super.findViewById(R.id.stop);
		pause=(ImageButton) super.findViewById(R.id.pause);
		forward=(ImageButton) super.findViewById(R.id.forward);
		next=(ImageButton) super.findViewById(R.id.next);
		ib_mode=(ImageButton) super.findViewById(R.id.ib_mode);
		
		mode = sps.getPreferenceValue("mode", C.mode_1);
		if(mode.equals(C.mode_1)){
			ib_mode.setBackgroundResource(R.drawable.mode_1);
		}else if(mode.equals(C.mode_2)){
			ib_mode.setBackgroundResource(R.drawable.mode_2);
		}else if(mode.equals(C.mode_3)){
			ib_mode.setBackgroundResource(R.drawable.mode_3);
		}else if(mode.equals(C.mode_4)){
			ib_mode.setBackgroundResource(R.drawable.mode_4);
		}
		
		audioList();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
//				public static final String mode_1 = "单曲循环";
//				public static final String mode_2 = "全部循环";
//				public static final String mode_3 = "顺序播放";
//				public static final String mode_4 = "随机播放";
				if(mode.equals(C.mode_1)){
					mediaPlayer.start();
				}else if(mode.equals(C.mode_2)){
					currentItem++;
					if(currentItem > audioList.size() - 1) {	//变为第一首的位置继续播放
						currentItem = 0;
					}
					playMusic(audioList.get(currentItem));
				}else if(mode.equals(C.mode_3)){
					currentItem++;	//下一首位置
					if (currentItem <= audioList.size() - 1) {
						playMusic(audioList.get(currentItem));
					}
				}else if(mode.equals(C.mode_4)){
					currentItem = getRandomIndex(audioList.size() - 1);
					playMusic(audioList.get(currentItem));
				}
			}
		});
		
		ib_mode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				final String[] items = new String[] {C.mode_1, C.mode_2, C.mode_3,C.mode_4 };
				AlertDialog dialog = new AlertDialog.Builder(ListMusic.this).setTitle("请选择播放模式")
				        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				                
				            	switch(which){
				            	case 0:
				            		ib_mode.setBackgroundResource(R.drawable.mode_1);
				            		mode = C.mode_1;
				            		break;
				            	case 1:
				            		ib_mode.setBackgroundResource(R.drawable.mode_2);
				            		mode = C.mode_2;
				            		break;
				            	case 2:
				            		ib_mode.setBackgroundResource(R.drawable.mode_3);
				            		mode = C.mode_3;
				            		break;
				            	case 3:
				            		ib_mode.setBackgroundResource(R.drawable.mode_4);
				            		mode = C.mode_4;
				            		break;
				            	}
			            		sps.setPreferenceValue("mode", mode);
				            	Toast.makeText(ListMusic.this, "已切换到"+items[which]+"模式", Toast.LENGTH_SHORT).show();
				                dialog.dismiss();
				            }
				        }).create();
				dialog.show();
			}
		});	
		
		play.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				playMusic(audioList.get(currentItem));
			}
		});	
		
		pause.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(mediaPlayer.isPlaying()){
					mediaPlayer.pause();
				}else{
					mediaPlayer.start();
				}
			}
		});
		
		stop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mediaPlayer.isPlaying()){
					mediaPlayer.stop();
				}
				pause.setEnabled(false);
			}
		});
		
		forward.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				forwardMusic();
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextMusic();
			}
		});
	}
	
	/**
	 * 获取随机位置
	 * @param end
	 * @return
	 */
	protected int getRandomIndex(int end){
		int index = (int) (Math.random() * end);
		return index;
	}

	private void audioList() {
		// TODO Auto-generated method stub
		getAudioList();
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,audioList);
	    ListView listview=(ListView) super.findViewById(R.id.listview1);
	    listview.setAdapter(adapter);
	    //当单击列表时播放音乐
	    listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				currentItem=position;
				playMusic(audioList.get(currentItem));
			}
		});
	    
	    listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				    // TODO Auto-generated method stub
				    final List<GroupEntity> mData = new ArrayList<GroupEntity>(); 
				    mData.addAll(db.getAllGroupExceptDefault());
				    
				    List<String> mItems = new ArrayList<String>(); 
				    for(GroupEntity g:db.getAllGroupExceptDefault()){
				    	mItems.add(g.getGroup());
				    }
					String[] items = (String[])mItems.toArray(new String[mItems.size()]); 
					
					new AlertDialog.Builder(ListMusic.this).setTitle("请选择需要添加到的列表:")   
					.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {   
					 public void onClick(DialogInterface dialog, int item) { 
					        db.insertGroupChild(mData.get(item).getGroupid(), audioList.get(position));
					        Toast.makeText(getApplicationContext(),"添加到列表"+mData.get(item).getGroup()+"成功!",Toast.LENGTH_SHORT).show();  
					        dialog.cancel();   
					  }   
					}).show();  
				    return false;
			}
		});
	}
	
	private void getAudioList(){
		audioList.clear();
		if(groupid.equals("1")){
			List<String> datas = db.getAllHistory();
			Set<String> uniqueSet = new HashSet(datas);
			for (String temp : uniqueSet){
				if(Collections.frequency(datas, temp) > 5){
					audioList.add(temp);
				}
			}
			
		}
		else if(groupid.equals("2")){
			List<String> datas = db.getAllHistory();
			Set<String> uniqueSet = new HashSet(datas);
			for (String temp : uniqueSet){
				audioList.add(temp);
			}
		}
		else if(groupid.equals("3")){
			List<String> datas = db.getAllLocal();
			Set<String> uniqueSet = new HashSet(datas);
			for (String temp : uniqueSet){
				audioList.add(temp);
			}
		}else{
			 List<GroupChildEntity> gChildren = db.getAllGroupChildrenByGroupId(groupid);
			 for(GroupChildEntity g:gChildren){
				 audioList.add(g.getPath());
			 }
		}
	}
	
	private	void playMusic(String path){
		try{
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			db.insertHistory(path);
			pause.setEnabled(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    private void nextMusic(){
		if(++currentItem>=audioList.size()){
			currentItem=0;
		}
		playMusic(audioList.get(currentItem));
	}

    private	void forwardMusic(){
       if(--currentItem>=0){
	      if(currentItem>=audioList.size()){
		     currentItem=0;
	      }else{
		     currentItem=audioList.size()-1;
		 }
	     playMusic(audioList.get(currentItem));
	   }
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		mediaPlayer.release();
		super.onDestroy();
	}
}
