

package org.glob3.mobile.specific;

import java.io.ByteArrayOutputStream;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;


public class Image_Android
         extends
            IImage {

   final private Bitmap _image;


   public Image_Android(final Bitmap image) {
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
   public IImage combineWith(final IImage transparent,
                             final int width,
                             final int height) {
      final Bitmap bm1 = Bitmap.createBitmap(_image, 0, 0, width, height);
      final Bitmap bm2 = Bitmap.createBitmap(((Image_Android) transparent).getBitmap(), 0, 0, width, height);
      final Canvas canvas = new Canvas(bm1);

      canvas.drawBitmap(bm1, 0, 0, null);
      canvas.drawBitmap(bm2, 0, 0, null);

      return new Image_Android(bm1);
   }


   @Override
   public IImage combineWith(final IImage other,
                             final Rectangle rect,
                             final int width,
                             final int height) {

      Bitmap bm1 = null;
      if ((_image.getWidth() != width) || (_image.getHeight() != height)) {
         bm1 = Bitmap.createBitmap(_image, 0, 0, width, height);
      }
      else {
         bm1 = _image;
      }
      final Bitmap canvasBitmap = bm1.copy(Bitmap.Config.ARGB_8888, true); //MAKE MUTABLE
      final Canvas canvas = new Canvas(canvasBitmap);

      final Bitmap bm2 = ((Image_Android) other).getBitmap();

      final int left = (int) rect._x;
      final int right = (int) (rect._x + rect._width);

      final int bottom = height - (int) rect._y;
      final int top = height - (int) (rect._y + rect._height);

      final Rect dstRect = new Rect(left, top, right, bottom);

      canvas.drawBitmap(bm2, null, dstRect, null);

      return new Image_Android(canvasBitmap);
   }


   @Override
   public IImage subImage(final Rectangle rect) {
      final Bitmap bm = Bitmap.createBitmap(_image, (int) rect._x, (int) rect._y, (int) rect._width, (int) rect._height);

      return new Image_Android(bm);
   }


   @Override
   public ByteBuffer getEncodedImage() {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      _image.compress(Bitmap.CompressFormat.PNG, 100, baos);
      final byte[] b = baos.toByteArray();

      return new ByteBuffer(b, b.length);
   }


   @Override
   public void fillWithRGBA8888(final byte[] data,
                                final int width,
                                final int height) {
      //Scaling
      Bitmap scaledImage = null;
      if ((_image.getWidth() != width) || (_image.getHeight() != height)) {
         scaledImage = Bitmap.createScaledBitmap(_image, width, height, true);
      }
      else {
         scaledImage = _image;
      }

      //Getting pixels in Color format
      final int[] pixels = new int[scaledImage.getWidth() * scaledImage.getHeight()];
      scaledImage.getPixels(pixels, 0, scaledImage.getWidth(), 0, 0, scaledImage.getWidth(), scaledImage.getHeight());

      //To RGBA
      if (data.length != (pixels.length * 4)) {
         Log.d("", "FAILURE FillWithRGBA");
         return;
      }
      int p = 0;
      for (final int color : pixels) {
         data[p++] = (byte) ((color >> 16) & 0xFF); //R
         data[p++] = (byte) ((color >> 8) & 0xFF); //G
         data[p++] = (byte) (color & 0xFF); //B
         data[p++] = (byte) (color >>> 24); //A
      }
   }

}
