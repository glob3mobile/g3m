package org.glob3.mobile.specific;

import java.io.IOException;
import java.io.InputStream;

import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ITimer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Factory_Android extends IFactory {
	
	final Context _context;
	
	public Factory_Android(Context c){
		_context = c;
	}

	@Override
	public ITimer createTimer() {
		return new Timer_Android();
	}

	@Override
	public void deleteTimer(ITimer timer) {}
	
	@Override
	public IImage createImageFromFileName(String filename) {
		
		final Bitmap bitmap;
		try {
			InputStream is = _context.getAssets().open("world.jpg");
		    int size = is.available();
			final byte[] imageData = new byte[size];
			is.read(imageData);
			bitmap = BitmapFactory.decodeByteArray(imageData, 0, size);
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		
		if (bitmap != null)
			return new Image_Android(bitmap);
		else 
			return null;
		
	}

	@Override
	public void deleteImage(IImage image) {}

}
