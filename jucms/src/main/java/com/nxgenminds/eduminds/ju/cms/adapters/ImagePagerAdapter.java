package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.UserPhotosGalleryModel;


public class ImagePagerAdapter extends PagerAdapter {
	private List<UserPhotosGalleryModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public ImagePagerAdapter(Context context, List<UserPhotosGalleryModel> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		prova = context;
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override 
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = layoutInflater.inflate(R.layout.custom_item_pager_image, view, false);
		assert imageLayout != null;
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
		UserPhotosGalleryModel app = listData.get(position);
		String SIcon=app.getActual_photo_path();
		imageLoader.getInstance().displayImage(SIcon, imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				/*String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "Input/Output error";
					break;
				case DECODING_ERROR:
					message = "Image can't be decoded";
					break;
				case NETWORK_DENIED:
					message = "Downloads are denied";
					break;
				case OUT_OF_MEMORY:
					message = "Out Of Memory error";
					break;
				case UNKNOWN:
					message = "Unknown error";
					break;
				}

				spinner.setVisibility(View.GONE);*/
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				/*spinner.setVisibility(View.GONE);*/
			}
		});

		view.addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() { 
		return null;
	}
}
