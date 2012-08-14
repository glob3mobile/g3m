package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;

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
		return _image.getWidth();
	}

	@Override
	public int getHeight() {
		return _image.getHeight();
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
		Bitmap bm = Bitmap.createBitmap(_image, (int) rect._x, (int) rect._y, (int) rect._width, (int) rect._height);
		
		return new Image_Android(bm);
	}

	@Override
	public ByteBuffer getEncodedImage() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		_image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		
		return new ByteBuffer(b, b.length);
	}

	@Override
	public void fillWithRGBA(byte[] data, int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
