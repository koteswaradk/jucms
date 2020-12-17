package com.nxgenminds.eduminds.ju.cms.utils;



import com.nxgenminds.eduminds.ju.cms.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;



public class AlertDialogManager {

	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 *               - pass null if you don't want icon
	 * */


	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Setting alert dialog icon
			//  alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});

		// Showing Alert Message
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialogAndGotoPrevScreen(final Activity context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Setting alert dialog icon
			//  alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					context.finish();
				}
			});

		// Showing Alert Message
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	public void customAlertDialogWithSingleOption(final Activity activity,String title,String message,String buttonText){
		Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.customalertdialog_with_singleoptions);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay(); 
		//This makes the dialog take up the full width
		lp.width = display.getWidth()-40;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		//		window.setFormat(PixelFormat.TRANSPARENT);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}

}
