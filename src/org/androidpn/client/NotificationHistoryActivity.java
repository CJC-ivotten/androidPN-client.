package org.androidpn.client;


import java.util.ArrayList;
import java.util.List;

import org.androidpn.demoapp.R;
import org.litepal.crud.DataSupport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 查看历史推送消息的Activity
 * @author chenjiacheng
 *
 */
public class NotificationHistoryActivity extends Activity {

	private ListView mListView;
	
	private NotificationHistoryAdapter mAdapter;
	
	private List<NotificationHistory> mList = new ArrayList<NotificationHistory>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationhistory);
		
		mList = DataSupport.findAll(NotificationHistory.class);//从（litepal）数据库查询所有数据
			
		mListView = (ListView) findViewById(R.id.list_view);
				
		//点击查看历史消息
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				NotificationHistory history = mList.get(index);
				 Intent intent = new Intent(NotificationHistoryActivity.this,
		                    NotificationDetailsActivity.class);
		            intent.putExtra(Constants.NOTIFICATION_API_KEY, history.getApiKey());
		            intent.putExtra(Constants.NOTIFICATION_TITLE, history.getTitle());
		            intent.putExtra(Constants.NOTIFICATION_MESSAGE, history.getMessage());
		            intent.putExtra(Constants.NOTIFICATION_URI, history.getUri());            
		            intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, history.getImageUrl());
				 startActivity(intent);
			}
		});
		
		mAdapter = new NotificationHistoryAdapter(this, 0, mList);
			
		mListView.setAdapter(mAdapter);
		
		registerForContextMenu(mListView);//给没ListView添加上下文菜单的功能
		
	}
	
	//创建上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0,0,0,"点击删除该历史推送");//参数一：id，分组所用（多个菜单情况下）参数二：添加菜单的id（区分点击）； 参数三：排序菜单；参数四：菜单显示的文字
	}
	
	//上下文菜单被点击
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if(item.getItemId() == 0){
			AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
			int index = menuInfo.position;
			NotificationHistory history = mList.get(index);
			history.delete();//从数据库删除掉
			mList.remove(index);//从内存删除
			mAdapter.notifyDataSetChanged();//更新界面
		}
		return super.onContextItemSelected(item);
	
	}
	
	
	class NotificationHistoryAdapter extends ArrayAdapter<NotificationHistory>{

		public NotificationHistoryAdapter(Context context,
				int textViewResourceId, List<NotificationHistory> objects) {
			super(context, textViewResourceId, objects);		
		}
		
		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NotificationHistory history = getItem(position);
			View view;
			if(convertView == null){
				view = LayoutInflater.from(getContext()).inflate(R.layout.notificationhistoryitem, null);
			}else{
				view = convertView;
			}
			
			TextView titleTextView = (TextView) view.findViewById(R.id.tv_title); 
			TextView timeTextView = (TextView) view.findViewById(R.id.tv_time); 
			titleTextView.setText(history.getTitle());
			timeTextView.setText(history.getTime());
			
			return view;
		}
		
	}
	
}
