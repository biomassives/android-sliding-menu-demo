package grimbo.android.demo.slidingmenu;

import grimbo.android.demo.slidingmenu.HorzScrollWithListMenu.SizeCallbackForMenu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Miko D. Santos 
 * http://dev.mikorobyo.com
 * 
 *         Original code by Paul Grime
 * 
 *         This example uses a FrameLayout to display a menu List and a
 *         HorizontalScrollView (HSV).
 * 
 *         The HSV has a transparent View as the first child, which means the
 *         menu will show through when the HSV is scrolled. It will then launch
 *         the new activity after.
 */

public class HorzScrollWithActivityMenu extends Activity {

	static ImageView btnSlide;
	static ClickListenerForScrolling clfs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(R.layout.horz_scroll_with_activity_menu);

		MyHorizontalScrollView scrollView = (MyHorizontalScrollView) findViewById(R.id.menuScrollView);
		ListView menu = (ListView) findViewById(R.id.menulist);
		final View app = inflater.inflate(R.layout.horz_scroll_app, null);
		app.setOnClickListener(clfs);

		ListView listView = (ListView) app.findViewById(R.id.list);
		ViewUtils.initListView(this, listView, "Item ", 30,
				android.R.layout.simple_list_item_1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				app.performClick();
			}
		});

		ViewUtils.initListView(this, menu, "Activity ", 3,
				android.R.layout.simple_list_item_1);

		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				clfs.performClick(pos + 1);
				// activity indices start at 1

			}
		});

		btnSlide = (ImageView) app.findViewById(R.id.BtnSlide);
		clfs = new ClickListenerForScrolling(scrollView, menu);
		app.setOnClickListener(clfs);

		// Create a transparent view that pushes the other views in the HSV to
		// the right.
		// This transparent view allows the menu to be shown when the HSV is
		// scrolled.
		View transparent = new TextView(this);
		transparent.setBackgroundColor(android.R.color.transparent);

		final View[] children = new View[] { transparent, app };

		// Scroll to menu (view[0]) when layout finished.
		int scrollToViewIdx = 0;
		((ViewGroup) scrollView.getChildAt(0)).removeAllViews();
		scrollView.initViews(children, scrollToViewIdx,
				new HorzScrollWithListMenu.SizeCallbackForMenu(btnSlide));
	}

	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	public static class ClickListenerForScrolling implements OnClickListener {
		private MyHorizontalScrollView scrollView;
		private View menu;
		private Bitmap appCache;
		private ImageView app;
		private int show = 0; // determines which activity to show
		private int statusBarHeight = 0;

		/**
		 * Menu must NOT be out/shown to start with.
		 */
		boolean menuOut = false;

		public ClickListenerForScrolling(MyHorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.app = new ImageView(menu.getContext());
			this.app.setOnClickListener(this);
			this.statusBarHeight = (int) Math.ceil(25 * menu.getContext()
					.getResources().getDisplayMetrics().density);
		}

		public int getShow() {
			return show;
		}

		public void setShow(int i) {
			show = i;
		}

		public void setCache(Bitmap bmp) {
			if (appCache != null) {
				appCache.recycle();
				appCache = null;
			}
			appCache = bmp;
			app.setImageBitmap(appCache);
			app.setPadding(0, -statusBarHeight, 0, 0);

			// Create a transparent view that pushes the other views in the HSV
			// to the right.
			// This transparent view allows the menu to be shown when the HSV is
			// scrolled.
			View transparent = new TextView(menu.getContext());
			transparent.setBackgroundColor(android.R.color.transparent);

			View[] children = new View[] { transparent, app };

			// Scroll to menu (view[0]) when layout finished.
			int scrollToViewIdx = 0;
			((ViewGroup) scrollView.getChildAt(0)).removeAllViews();
			scrollView.initViews(children, scrollToViewIdx,
					new SizeCallbackForMenu(btnSlide));
		}

		public void performClick(int i, View v) {
			show = i;
			onClick(v);
		}

		public void performClick(int i) {
			performClick(i, null);
		}

		public void onClick() {
			onClick(null);
		}

		@Override
		public void onClick(View v) {
			Context context = menu.getContext();

			int menuWidth = menu.getMeasuredWidth();

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			if (!menuOut) {
				context.startActivity(new Intent(context,
						HorzScrollWithActivityMenu.class)
						.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

				// Scroll to 0 to reveal menu
				int left = 0;
				scrollView.smoothScrollTo(left, 0);

			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);

				Class<?> cls = NewActivity1.class;
				if (show == NewActivity2.ACTIVITY_NO)
					cls = NewActivity2.class;
				else if (show == NewActivity3.ACTIVITY_NO)
					cls = NewActivity3.class;

				context.startActivity(new Intent(context, cls)
						.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

			}
			menuOut = !menuOut;
		}
	}

}
