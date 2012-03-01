package grimbo.android.demo.slidingmenu;

/**
 *  @author Miko D. Santos
 *  http://dev.mikorobyo.com
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class NewActivity3 extends Activity {

	static int ACTIVITY_NO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		View app = inflater.inflate(R.layout.horz_scroll_app, null);
		setContentView(app);
		setTitle("Activity " + ACTIVITY_NO);

		ListView listView = (ListView) findViewById(R.id.list);
		ViewUtils.initListView(this, listView, "Item ", 30,
				android.R.layout.simple_list_item_1);

		findViewById(R.id.BtnSlide).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMenu();
			}
		});

	}

	@Override
	public void onBackPressed() {
		showMenu();
	}

	private void showMenu() {
		Bitmap viewCapture = null;
		View rootView = findViewById(android.R.id.content).getRootView();
		rootView.setDrawingCacheEnabled(true);
		viewCapture = Bitmap.createBitmap(rootView.getDrawingCache());
		rootView.setDrawingCacheEnabled(false);

		if (viewCapture != null)
			HorzScrollWithActivityMenu.clfs.setCache(viewCapture);

		// mark an index for the activity
		HorzScrollWithActivityMenu.clfs.performClick(ACTIVITY_NO);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		overridePendingTransition(0, 0);
	}
}
