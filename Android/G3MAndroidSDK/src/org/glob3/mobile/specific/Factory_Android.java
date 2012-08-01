package org.glob3.mobile.specific;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.INetwork;
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
			InputStream is = _context.getAssets().open(filename);
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

	@Override
	public IImage createImageFromData(ByteBuffer bb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IImage createImageFromSize(int width, int height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INetwork createNetwork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletenetwork(INetwork image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String stringFromUTF8(byte[] data) {
		try {
			return new String(data, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
