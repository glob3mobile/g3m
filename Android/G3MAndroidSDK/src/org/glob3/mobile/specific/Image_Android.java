

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.RectangleI;
import org.glob3.mobile.generated.Vector2I;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public final class Image_Android
         extends
            IImage {


   private static class BitmapHolder {
      private Bitmap _bitmap;
      private int    _referencesCount;


      private BitmapHolder(final Bitmap bitmap) {
         _bitmap = bitmap;
         _referencesCount = 1;
      }


      private void _retain() {
         _referencesCount++;
      }


      private void _release() {
         _referencesCount--;
         if (_referencesCount == 0) {
            _bitmap.recycle();
            _bitmap = null;
         }
      }
   }


   //   final private Bitmap _bitmap;
   final private BitmapHolder _bitmapHolder;
   private byte[]             _source;


   public Image_Android(final Bitmap bitmap,
                        final byte[] source) {
      if (bitmap == null) {
         throw new RuntimeException("Can't create an Image_Android with a null bitmap");
      }

      _bitmapHolder = new BitmapHolder(bitmap);
      _source = source;
   }


   private Image_Android(final BitmapHolder bitmapHolder,
                         final byte[] source) {
      if (bitmapHolder == null) {
         throw new RuntimeException("Can't create an Image_Android with a null bitmap");
      }
      bitmapHolder._retain();

      _bitmapHolder = bitmapHolder;
      _source = source;
   }


   public Bitmap getBitmap() {
      return _bitmapHolder._bitmap;
   }


   @Override
   public int getWidth() {
      return (_bitmapHolder._bitmap == null) ? 0 : _bitmapHolder._bitmap.getWidth();
   }


   @Override
   public int getHeight() {
      return (_bitmapHolder._bitmap == null) ? 0 : _bitmapHolder._bitmap.getHeight();
   }


   @Override
   public Vector2I getExtent() {
      return new Vector2I(getWidth(), getHeight());
   }


   //   @Override
   //   public void combineWith(final IImage transparent,
   //                           final int width,
   //                           final int height,
   //                           final IImageListener listener,
   //                           final boolean autodelete) {
   //      final Bitmap bm1 = Bitmap.createBitmap(_bitmapHolder._bitmap, 0, 0, width, height);
   //      final Bitmap bm2 = Bitmap.createBitmap(((Image_Android) transparent).getBitmap(), 0, 0, width, height);
   //      final Canvas canvas = new Canvas(bm1);
   //
   //      canvas.drawBitmap(bm1, 0, 0, null);
   //      canvas.drawBitmap(bm2, 0, 0, null);
   //
   //      return new Image_Android(bm1, null);
   //   }

   @Override
   public void combineWith(final ArrayList<IImage> images,
                           final ArrayList<RectangleI> rectangles,
                           final int width,
                           final int height,
                           final IImageListener listener,
                           final boolean autodelete) {

      final int imagesSize = images.size();
      if (imagesSize == 0) {
         scale(width, height, listener, autodelete);
      }
      else if (imagesSize == 1) {
         final IImage other = images.get(0);
         final RectangleI rect = rectangles.get(0);

         combineWith(other, rect, width, height, listener, autodelete);
      }
      else {

         final Bitmap bm1;
         if ((_bitmapHolder._bitmap.getWidth() != width) || (_bitmapHolder._bitmap.getHeight() != height)) {
            bm1 = Bitmap.createBitmap(_bitmapHolder._bitmap, 0, 0, width, height);
         }
         else {
            bm1 = _bitmapHolder._bitmap;
         }
         final Bitmap canvasBitmap = bm1.copy(Bitmap.Config.ARGB_8888, true); //MAKE MUTABLE
         final Canvas canvas = new Canvas(canvasBitmap);

         for (int i = 0; i < imagesSize; i++) {
            final IImage other = images.get(i);
            final RectangleI rect = rectangles.get(i);
            final Bitmap bm2 = ((Image_Android) other).getBitmap();

            final int left = rect._x;
            final int right = rect._x + rect._width;

            final int bottom = height - rect._y;
            final int top = height - (rect._y + rect._height);

            final Rect dstRect = new Rect(left, top, right, bottom);

            canvas.drawBitmap(bm2, null, dstRect, null);
         }

         final Image_Android result = new Image_Android(canvasBitmap, null);
         listener.imageCreated(result);
      }
   }


   @Override
   public void combineWith(final IImage other,
                           final RectangleI rect,
                           final int width,
                           final int height,
                           final IImageListener listener,
                           final boolean autodelete) {

      //      final Bitmap bm1;
      //      if ((_bitmapHolder._bitmap.getWidth() == width) && (_bitmapHolder._bitmap.getHeight() == height)) {
      //         bm1 = _bitmapHolder._bitmap;
      //      }
      //      else {
      //         // bm1 = Bitmap.createBitmap(_bitmapHolder._bitmap, 0, 0, width, height);
      //         bm1 = Bitmap.createScaledBitmap(_bitmapHolder._bitmap, width, height, false);
      //      }
      final Bitmap bm1 = Bitmap.createScaledBitmap(_bitmapHolder._bitmap, width, height, false);
      final Bitmap canvasBitmap = bm1.copy(Bitmap.Config.ARGB_8888, true); //MAKE MUTABLE
      final Canvas canvas = new Canvas(canvasBitmap);

      final Bitmap otherBitmap = ((Image_Android) other).getBitmap();

      final int left = rect._x;
      final int right = rect._x + rect._width;
      final int bottom = height - rect._y;
      final int top = height - (rect._y + rect._height);
      canvas.drawBitmap(otherBitmap, null, new Rect(left, top, right, bottom), null);

      final Image_Android result = new Image_Android(canvasBitmap, null);
      listener.imageCreated(result);
   }


   @Override
   public void subImage(final RectangleI rect,
                        final IImageListener listener,
                        final boolean autodelete) {
      if ((rect._x == 0) && (rect._y == 0) && (_bitmapHolder._bitmap.getWidth() == rect._width)
          && (_bitmapHolder._bitmap.getHeight() == rect._height)) {
         listener.imageCreated(this.shallowCopy());
      }
      else {
         final Bitmap bm = Bitmap.createBitmap(_bitmapHolder._bitmap, rect._x, rect._y, rect._width, rect._height);

         final Image_Android result = new Image_Android(bm, null);
         listener.imageCreated(result);
      }
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
   public void scale(final int width,
                     final int height,
                     final IImageListener listener,
                     final boolean autodelete) {
      if ((_bitmapHolder._bitmap.getWidth() == width) && (_bitmapHolder._bitmap.getHeight() == height)) {
         listener.imageCreated(this.shallowCopy());
      }
      else {
         final Bitmap bitmap = Bitmap.createScaledBitmap(_bitmapHolder._bitmap, width, height, false);
         final Image_Android result;
         if (bitmap == null) {
            ILogger.instance().logError("Can't scale Image");
            result = null;
         }
         else {
            result = new Image_Android(bitmap, null);
         }
         listener.imageCreated(result);
      }
   }


   @Override
   public String description() {
      return "Image Android " + getWidth() + " x " + getHeight() + ", _image=(" + _bitmapHolder._bitmap.describeContents() + ")";
   }


   public byte[] getSourceBuffer() {
      return _source;
   }


   public void releaseSourceBuffer() {
      _source = null;
   }


   @Override
   public IImage shallowCopy() {
      return new Image_Android(_bitmapHolder, _source);
   }


   @Override
   public void dispose() {
      super.dispose();

      _bitmapHolder._release();
      //_bitmap.recycle();
   }


}
