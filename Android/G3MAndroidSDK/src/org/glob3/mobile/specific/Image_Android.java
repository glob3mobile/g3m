

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Rectangle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public final class Image_Android
         extends
            IImage {

   final private Bitmap _bitmap;
   private byte[]       _source;


   public Image_Android(final Bitmap bitmap,
                        final byte[] source) {
      if (bitmap == null) {
         throw new RuntimeException("Can't create an Image_Android with a null bitmap");
      }

      _bitmap = bitmap;
      _source = source;
   }


   public Bitmap getBitmap() {
      return _bitmap;
   }


   @Override
   public int getWidth() {
      return (_bitmap == null) ? 0 : _bitmap.getWidth();
   }


   @Override
   public int getHeight() {
      return (_bitmap == null) ? 0 : _bitmap.getHeight();
   }


   @Override
   public IImage combineWith(final IImage transparent,
                             final int width,
                             final int height) {
      final Bitmap bm1 = Bitmap.createBitmap(_bitmap, 0, 0, width, height);
      final Bitmap bm2 = Bitmap.createBitmap(((Image_Android) transparent).getBitmap(), 0, 0, width, height);
      final Canvas canvas = new Canvas(bm1);

      canvas.drawBitmap(bm1, 0, 0, null);
      canvas.drawBitmap(bm2, 0, 0, null);

      return new Image_Android(bm1, null);
   }


   @Override
   public IImage combineWith(final IImage other,
                             final Rectangle rect,
                             final int width,
                             final int height) {

      Bitmap bm1 = null;
      if ((_bitmap.getWidth() != width) || (_bitmap.getHeight() != height)) {
         bm1 = Bitmap.createBitmap(_bitmap, 0, 0, width, height);
      }
      else {
         bm1 = _bitmap;
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

      return new Image_Android(canvasBitmap, null);
   }


   @Override
   public IImage subImage(final Rectangle rect) {
      final Bitmap bm = Bitmap.createBitmap(_bitmap, (int) rect._x, (int) rect._y, (int) rect._width, (int) rect._height);

      return new Image_Android(bm, null);
   }


   //   public IByteBuffer createByteBufferRGBA8888(final int width,
   //                                               final int height) {
   //
   //      //Scaling
   //      Bitmap scaledImage = null;
   //      if ((_image.getWidth() != width) || (_image.getHeight() != height)) {
   //         scaledImage = Bitmap.createScaledBitmap(_image, width, height, true);
   //      }
   //      else {
   //         scaledImage = _image;
   //      }
   //
   //      //Getting pixels in Color format
   //      final int[] pixels = new int[scaledImage.getWidth() * scaledImage.getHeight()];
   //      scaledImage.getPixels(pixels, 0, scaledImage.getWidth(), 0, 0, scaledImage.getWidth(), scaledImage.getHeight());
   //
   //      //To RGBA
   //      final byte[] data = new byte[pixels.length * 4];
   //      int p = 0;
   //      for (final int color : pixels) {
   //         data[p++] = (byte) ((color >> 16) & 0xFF); //R
   //         data[p++] = (byte) ((color >> 8) & 0xFF); //G
   //         data[p++] = (byte) (color & 0xFF); //B
   //         data[p++] = (byte) (color >>> 24); //A
   //      }
   //
   //      return new ByteBuffer_Android(data);
   //   }


   @Override
   public IImage scale(final int width,
                       final int height) {
      final Bitmap b = Bitmap.createScaledBitmap(_bitmap, width, height, false);
      if (b == null) {
         ILogger.instance().logError("Can't scale Image");
         return null;
      }
      return new Image_Android(b, null);
   }


   @Override
   public String description() {
      return "Image Android " + getWidth() + " x " + getHeight() + ", _image=(" + _bitmap.describeContents() + ")";
   }


   public byte[] getSourceBuffer() {
      return _source;
   }


   public void releaseSourceBuffer() {
      _source = null;
   }


   @Override
   public IImage shallowCopy() {
      return new Image_Android(_bitmap, _source);
   }

}
