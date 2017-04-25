package wu.rang.hao.ecgapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private EditText editTextName;
	private EditText editTextAge;
	private EditText editTextOther;
	private Button buttonSimple;
	private Button buttonDeal;
	private Button buttonDisplay;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editTextName=(EditText)findViewById(R.id.editViewId1);
		editTextAge=(EditText)findViewById(R.id.editViewId2);
		editTextOther=(EditText)findViewById(R.id.editViewId3);
		buttonSimple=(Button)findViewById(R.id.buttonIdSimple);
		buttonDeal=(Button)findViewById(R.id.buttonIdDeal);
		buttonDisplay=(Button)findViewById(R.id.buttonIdContent);
		textView1=(TextView)findViewById(R.id.textViewId1);
		textView2=(TextView)findViewById(R.id.textViewId2);
		textView3=(TextView)findViewById(R.id.textViewId3);
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
}
