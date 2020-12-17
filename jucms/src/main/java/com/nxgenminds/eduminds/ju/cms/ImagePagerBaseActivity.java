package com.nxgenminds.eduminds.ju.cms;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;


public class ImagePagerBaseActivity extends ActionBarActivity{

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_clear_memory_cache:
			imageLoader.clearMemoryCache();
			return true;
		case R.id.item_clear_disc_cache:
			imageLoader.clearDiscCache();
			return true;
		default:
			return false;
		}
	}
}
