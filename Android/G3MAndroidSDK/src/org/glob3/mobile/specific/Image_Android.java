package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Image_Android extends IImage{
	
	private Bitmap _image;

	@Override
	public void loadFromFileName(String filename) {
	      _image = BitmapFactory.decodeFile(filename);
	}
	
	public Bitmap getBitmap() {
		return _image;
	}

}
