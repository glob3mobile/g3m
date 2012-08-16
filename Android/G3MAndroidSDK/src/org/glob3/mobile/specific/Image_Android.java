package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Image_Android extends IImage {

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
   public IImage combineWith(IImage transparent,
                             int width,
                             int height) {
      Bitmap bm1 = Bitmap.createBitmap(_image, 0, 0, width, height);
      Bitmap bm2 = Bitmap.createBitmap(((Image_Android) transparent).getBitmap(), 0, 0, width, height);
      Canvas canvas = new Canvas(bm1);

      canvas.drawBitmap(bm1, 0, 0, null);
      canvas.drawBitmap(bm2, 0, 0, null);

      return new Image_Android(bm1);
   }


   @Override
   public IImage combineWith(IImage other,
                             Rectangle rect,
                             int width,
                             int height) {
      Bitmap bm1 = Bitmap.createBitmap(_image, 0, 0, width, height);
      Bitmap bm2 = Bitmap.createBitmap(((Image_Android) other).getBitmap(), (int) rect._x, (int) rect._y, (int) rect._width,
               (int) rect._height);
      Canvas canvas = new Canvas(bm1);

      canvas.drawBitmap(bm1, 0, 0, null);
      canvas.drawBitmap(bm2, (int) rect._x, (int) rect._y, null);

      return new Image_Android(bm1);
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
   public void fillWithRGBA(byte[] data,
                            int width,
                            int height) {
	   //Scaling
	   Bitmap scaledImage = null;
	   if ((_image.getWidth() != width) && (_image.getHeight() != height)) {
		   scaledImage = Bitmap.createScaledBitmap(_image, width, height, true);
	   } else{
		   scaledImage = _image;
	   }
	   
	   //Getting pixels in Color format
	   int[] pixels = new int[getWidth() * getHeight()];
	   scaledImage.getPixels(pixels, 0, getWidth(), 0, 0, getWidth(), getHeight());
	   
	   //To RGBA
	   data = new byte[pixels.length*4];
	   int p = 0;
	   for(int i = 0; i < pixels.length; i++){
		   int color = pixels[i];
		   data[p++] = (byte) ((color >> 16) & 0xFF);  	//R
		   data[p++] = (byte) ((color >> 8) & 0xFF);  	//G
		   data[p++] = (byte) (color & 0xFF);  			//B
		   data[p++] = (byte) (color >>> 24);  			//A
	   }
   }

}
