package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import android.graphics.Bitmap;

public class Image_Android extends IImage{
	
	final private Bitmap _image;
	
	public Image_Android(Bitmap image) {
		_image = image;
	}
	
	public Bitmap getBitmap() {
		return _image;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IImage combineWith(IImage transparent, int width, int height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage combineWith(IImage other, Rectangle rect, int width,
			int height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage subImage(Rectangle rect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuffer getEncodedImage() {
		// TODO Auto-generated method stub
		return null;
	}

}
