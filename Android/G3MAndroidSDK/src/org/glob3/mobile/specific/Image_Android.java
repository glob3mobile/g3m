package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;

import android.graphics.Bitmap;

public class Image_Android extends IImage{
	
	final private Bitmap _image;
	
	public Image_Android(Bitmap image) {
		_image = image;
	}

	@Override
	public void loadFromFileName(String filename) {
	      //_image = BitmapFactory.decodeFile(filename);
	}
	
	public Bitmap getBitmap() {
		return _image;
	}

}
