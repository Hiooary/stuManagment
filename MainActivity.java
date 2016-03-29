package com.example.studentmanger;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private EditText number, name, scord;
	private TextView info;
	private Button btnAdd, btnDelete, btnUpdate, btnCheck, btnAll, btnRefer;
	SQLiteDatabase db;

	private void initUI() {
	
		number = (EditText)findViewById(R.id.activity_second_etNumber);
		name = (EditText)findViewById(R.id.activity_second_etName);
		scord = (EditText)findViewById(R.id.activity_second_etScord);
		info = (TextView)findViewById(R.id.activity_second_info);
		btnAdd = (Button)findViewById(R.id.activity_second_add);
		btnDelete = (Button)findViewById(R.id.activity_second_delete);
		btnUpdate = (Button)findViewById(R.id.activity_second_update);
		btnCheck = (Button)findViewById(R.id.activity_second_checkon);
		btnAll = (Button)findViewById(R.id.antivity_second_allinfo);
		btnRefer = (Button)findViewById(R.id.activity_second_referinfo);
		
		btnAdd.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnCheck.setOnClickListener(this);
		btnAll.setOnClickListener(this);
		btnRefer.setOnClickListener(this);
		
	}
	 //数据库的初始化
    private void initDataBase() {
		db = openOrCreateDatabase("StudentDB",Context.MODE_PRIVATE,null);
		db.execSQL("CREATE TABLE IF NOT EXISTS student(number VARCHAR,name VARCHAR, scord VARCHAR);");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		initUI();
		initDataBase();
		
	}

	public boolean addRecord() {
		
		if ((number.getText().toString().trim().length() == 0) || (name.getText().toString().trim().length() == 0) ||(
				scord.getText().toString().trim().length() == 0))
		{
			showMessage("Error","请输入所有的信息");
			return false;
		}
		db.execSQL("INSERT INTO student VALUES('"+number.getText()+"','"+name.getText()+"','"+scord.getText()+"');");
		showMessage("Success", "成功添加记录");
		clearText();
		return true;
		
	}
	 public boolean allRecord() {
			
	    	Cursor c = db.rawQuery("SELECT * FROM student", null);
	    	if(c.getCount() == 0)
	    	{
	    		showMessage("Error","数据库无记录");
	    		return false;
	    	}
	    	StringBuffer buffer = new StringBuffer();
	    	while(c.moveToNext())
	    	{
	    		buffer.append("编号:"+c.getString(0)+"\n");
	    		buffer.append("姓名:"+c.getString(1)+"\n");
	    		buffer.append("成绩:"+c.getString(2)+"\n");
	    	}
	    	showMessage("学生信息:",buffer.toString());
	    	return true;
			
			
		}

		public boolean cheRecord() {
			
			if (number.getText().toString().trim().length() == 0)
			{
				allRecord();
				return false;
			}
			Cursor c = db.rawQuery("SELECT * FROM student WHERE number='"+number.getText()+"'", null);
			if (c.moveToFirst())
			{
				name.setText(c.getString(1));
				scord.setTag(c.getString(2));
			}else
			{
				showMessage("Error","查无此人编号");
				clearText();
			}
			return true;
			
		}

		public boolean updRecord() {
			if (number.getText().toString().trim().length() == 0)
			{
				showMessage("Error", "请输入编号");
				return false;
			}
			Cursor c = db.rawQuery("SELECT * FROM student WHERE number='"+number.getText()+"'", null);
			if (c.moveToFirst())
			{
				db.execSQL("UPDATE student SET name='"+name.getText()+"',scord='"+scord.getText()+"' WHERE number='"+number.getText()+"'");
				showMessage("SUCCESS","修改成功");
			}else
			{
				showMessage("Error","查无此人编号");
			}
			clearText();
			return true;
		}

		public boolean delRecord() {
			
			if (number.getText().toString().trim().length() == 0)
			{
				showMessage("Error", "请输入编号");
				return false;
			}
			Cursor c = db.rawQuery("SELECT * FROM student WHERE number='"+number.getText()+"'", null);
			if (c.moveToFirst())
			{
				db.execSQL("DELETE FROM student WHERE number='"+number.getText()+"'");
				showMessage("SUCCESS","成功删除");
			}else
			{
				showMessage("Error","查无此人编号");
			}
			clearText();
			return true;
			
		}

		
    public void onClick(View arg0)
    {
    	switch(arg0.getId())
    	{
    	case R.id.activity_second_add:
    		addRecord();
    		break;
    	case R.id.activity_second_delete:
    		delRecord();
    		break;
    	case R.id.activity_second_update:
    		updRecord();
    		break;
    	case R.id.activity_second_checkon:
    		cheRecord();
    		break;
    	case R.id.antivity_second_allinfo:
    		allRecord();
    		break;
    	default:
    			showMessage("学生成绩管理系统：","四川师范大学");
    			break;
    	}
    }
   
    
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    //显示对话框
	private void showMessage(String title, String message) {
	
		Builder builder = new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		info.append(message+"\n");
		builder.show();
	}
	
    //清除UI文本输入框内容：
	public void clearText() {
		number.setText("");
		name.setText("");
		scord.setText("");
		info.setText("");
		number.requestFocus();
	}
	
}
