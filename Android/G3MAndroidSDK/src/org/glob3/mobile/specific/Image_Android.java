

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Vector2I;

import android.graphics.Bitmap;


public final class Image_Android
         extends
            IImage {


   //   private static String createCallStackString() {
   //      final Exception e = new Exception();
   //      final StringWriter wr = new StringWriter();
   //      final PrintWriter err = new PrintWriter(wr);
   //      e.printStackTrace(err);
   //      err.flush();
   //      return wr.toString() //
   //      .replace("org.glob3.mobile.specific.", "") //
   //      .replace("org.glob3.mobile.generated.", "") //
   //      .replace("android.opengl.", "") //
   //      .replace("at", "") //
   //      .replace("java.lang.Exception\n", "");
   //   }


   private static class BitmapHolder {
      private Bitmap _bitmap;
      private int    _referencesCount;


      //      private final String       _createdAt;
      //      private final List<String> _retainedAt = new ArrayList<String>();
      //      private final List<String> _releasedAt = new ArrayList<String>();


      private BitmapHolder(final Bitmap bitmap) {
         _bitmap = bitmap;
         _referencesCount = 1;
         //         _createdAt = createCallStackString();
      }


      private void _retain() {
         synchronized (this) {
            _referencesCount++;
            //            _retainedAt.add(createCallStackString());
         }
      }


      private void _release() {
         synchronized (this) {
            _referencesCount--;
            if (_referencesCount == 0) {
               _bitmap.recycle();
               _bitmap = null;
            }
            //            _releasedAt.add(createCallStackString());
         }
      }


      //      @Override
      //      protected void finalize() throws Throwable {
      //         if (_referencesCount != 0) {
      //            synchronized (ILogger.instance()) {
      //               ILogger.instance().logError("=======");
      //               ILogger.instance().logError("***** BitmapHolder deleted with invalid _referencesCount=" + _referencesCount);
      //
      //               final StringBuffer msg = new StringBuffer();
      //               msg.append("Created At:\n");
      //               msg.append(_createdAt);
      //               msg.append("Retained At:\n");
      //               for (final String e : _retainedAt) {
      //                  msg.append(e);
      //                  msg.append("---\n");
      //               }
      //               msg.append("Released At:\n");
      //               for (final String e : _releasedAt) {
      //                  msg.append(e);
      //                  msg.append("---\n");
      //               }
      //               msg.append("=======\n");
      //
      //               ILogger.instance().logError(msg.toString());
      //            }
      //         }
      //         super.finalize();
      //      }

   }


   final private BitmapHolder _bitmapHolder;
   private byte[]             _source;


   //   private boolean            _bitmapHolderReleased = false;
   //   private final String       _createdAt;


   Image_Android(final Bitmap bitmap,
                 final byte[] source) {
      //      _createdAt = createCallStackString();

      if (bitmap == null) {
         throw new RuntimeException("Can't create an Image_Android with a null bitmap");
      }

      _bitmapHolder = new BitmapHolder(bitmap);
      _source = source;
   }


   private Image_Android(final BitmapHolder bitmapHolder,
                         final byte[] source) {
      //      _createdAt = createCallStackString();

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
   //   public void combineWith(final ArrayList<IImage> images,
   //                           final ArrayList<RectangleI> rectangles,
   //                           final int width,
   //                           final int height,
   //                           final IImageListener listener,
   //                           final boolean autodelete) {
   //      final int imagesSize = images.size();
   //      if (imagesSize == 0) {
   //         scale(width, height, listener, autodelete);
   //      }
   //      else if (imagesSize == 1) {
   //         final IImage other = images.get(0);
   //         final RectangleI rect = rectangles.get(0);
   //
   //         combineWith(other, rect, width, height, listener, autodelete);
   //      }
   //      else {
   //         final Bitmap bm1;
   //         if ((getWidth() != width) || (getHeight() != height)) {
   //            bm1 = Bitmap.createBitmap(_bitmapHolder._bitmap, 0, 0, width, height);
   //         }
   //         else {
   //            bm1 = _bitmapHolder._bitmap;
   //         }
   //         final Bitmap canvasBitmap = bm1.copy(Bitmap.Config.ARGB_8888, true); //MAKE MUTABLE
   //         final Canvas canvas = new Canvas(canvasBitmap);
   //
   //         for (int i = 0; i < imagesSize; i++) {
   //            final IImage other = images.get(i);
   //            final RectangleI rect = rectangles.get(i);
   //            final Bitmap bm2 = ((Image_Android) other).getBitmap();
   //
   //            final int left = rect._x;
   //            final int right = rect._x + rect._width;
   //
   //            final int bottom = height - rect._y;
   //            final int top = height - (rect._y + rect._height);
   //
   //            final Rect dstRect = new Rect(left, top, right, bottom);
   //
   //            canvas.drawBitmap(bm2, null, dstRect, null);
   //         }
   //
   //         final Image_Android result = new Image_Android(canvasBitmap, null);
   //         listener.imageCreated(result);
   //      }
   //   }


   //   @Override
   //   public void combineWith(final IImage other,
   //                           final RectangleI rect,
   //                           final int width,
   //                           final int height,
   //                           final IImageListener listener,
   //                           final boolean autodelete) {
   //      final Bitmap bm1 = Bitmap.createScaledBitmap(_bitmapHolder._bitmap, width, height, false);
   //      final Bitmap canvasBitmap = bm1.copy(Bitmap.Config.ARGB_8888, true); //MAKE MUTABLE
   //      final Canvas canvas = new Canvas(canvasBitmap);
   //
   //      final Bitmap otherBitmap = ((Image_Android) other).getBitmap();
   //
   //      final int left = rect._x;
   //      final int right = rect._x + rect._width;
   //      final int bottom = height - rect._y;
   //      final int top = height - (rect._y + rect._height);
   //      canvas.drawBitmap(otherBitmap, null, new Rect(left, top, right, bottom), null);
   //
   //      final Image_Android result = new Image_Android(canvasBitmap, null);
   //      listener.imageCreated(result);
   //   }


   //   @Override
   //   public void subImage(final RectangleI rect,
   //                        final IImageListener listener,
   //                        final boolean autodelete) {
   //      final Image_Android result;
   //      if ((rect._x == 0) && (rect._y == 0) && (rect._width == getWidth()) && (rect._height == getHeight())) {
   //         result = shallowCopy();
   //      }
   //      else {
   //         final Bitmap bitmap = Bitmap.createBitmap(_bitmapHolder._bitmap, rect._x, rect._y, rect._width, rect._height);
   //         result = new Image_Android(bitmap, null);
   //      }
   //      listener.imageCreated(result);
   //   }


   //   @Override
   //   public void scale(final int width,
   //                     final int height,
   //                     final IImageListener listener,
   //                     final boolean autodelete) {
   //      final Image_Android result;
   //      if ((width == getWidth()) && (height == getHeight())) {
   //         result = shallowCopy();
   //      }
   //      else {
   //         final Bitmap bitmap = Bitmap.createScaledBitmap(_bitmapHolder._bitmap, width, height, false);
   //         if (bitmap == null) {
   //            ILogger.instance().logError("Can't scale Image");
   //            result = null;
   //         }
   //         else {
   //            result = new Image_Android(bitmap, null);
   //         }
   //      }
   //      listener.imageCreated(result);
   //   }


   @Override
   public String description() {
      return "Image_Android " + getWidth() + " x " + getHeight() + ", _image=(" + _bitmapHolder._bitmap.describeContents() + ")";
   }


   public byte[] getSourceBuffer() {
      return _source;
   }


   public void releaseSourceBuffer() {
      _source = null;
   }


   @Override
   public Image_Android shallowCopy() {
      return new Image_Android(_bitmapHolder, _source);
   }


   @Override
   public void dispose() {
      synchronized (this) {
         //         _bitmapHolderReleased = true;
         _bitmapHolder._release();
      }

      super.dispose();
   }


   //@Override
   //public void combineWith(RectangleI thisSourceRect, IImage other,
   //		RectangleI sourceRect, RectangleI destRect, Vector2I destSize,
   //		IImageListener listener, boolean autodelete) {
   //	// TODO Auto-generated method stub
   //	
   //}


   //@Override
   //public void combineWith(RectangleI thisSourceRect, 
   //		ArrayList<IImage> images,
   //		ArrayList<RectangleI> sourceRects, 
   //		ArrayList<RectangleI> destRects,
   //		Vector2I size, 
   //		IImageListener listener, 
   //		boolean autodelete) {
   //	
   //	int width = size._x;
   //	int height = size._y;
   //	
   //    final int imagesSize = images.size();
   //    if (imagesSize == 0) {
   //       scale(width, height, listener, autodelete);
   //    }
   ////    else
   ////    	if (imagesSize == 1) {
   ////       final IImage other = images.get(0);
   ////       final RectangleI rect = rectangles.get(0);
   ////
   ////       combineWith(other, rect, width, height, listener, autodelete);
   ////    }
   //    else {
   //       Bitmap bm1;
   //	   //Cropping bitmap if necessary
   //       if (thisSourceRect._x != 0 || thisSourceRect._y != 0 ||
   //    	  (getWidth() != thisSourceRect._width) || (getHeight() != thisSourceRect._height)) {
   //
   //          bm1 = Bitmap.createBitmap(_bitmapHolder._bitmap, 0, 0, width, height);
   //       }
   //       else {
   //          bm1 = _bitmapHolder._bitmap;
   //       }
   //       
   //     //Scaling bitmap if necessary
   //       if (bm1.getHeight() != height || bm1.getWidth() != width){
   //           bm1 = Bitmap.createScaledBitmap(bm1, width, height, false);
   //       }
   //       
   //       final Bitmap canvasBitmap = bm1.copy(Bitmap.Config.ARGB_8888, true); //MAKE MUTABLE
   //       final Canvas canvas = new Canvas(canvasBitmap);
   //
   //       for (int i = 0; i < imagesSize; i++) {
   //          final IImage other = images.get(i);
   //          
   //          final RectangleI srcR = sourceRects.get(i);
   //          final RectangleI dstR = destRects.get(i);
   //          
   //
   //          final Rect srcRect = new Rect(srcR._x, //LEFT
   //        		  height - (srcR._y + srcR._height), //TOP
   //        		  right, //RIGHT
   //        		  bottom);
   //          
   //
   //          final Rect dstRect = new Rect(left, top, right, bottom);
   //          
   //          final RectangleI rect = rectangles.get(i);
   //          final Bitmap bm2 = ((Image_Android) other).getBitmap();
   //
   //          final int left = rect._x;
   //          final int right = rect._x + rect._width;
   //
   //          final int bottom = height - rect._y;
   //          final int top = height - (rect._y + rect._height);
   //
   //
   //          canvas.drawBitmap(bm2, null, dstRect, null);
   //       }
   //
   //       final Image_Android result = new Image_Android(canvasBitmap, null);
   //       listener.imageCreated(result);
   //    }
   //}


   //   @Override
   //   protected void finalize() throws Throwable {
   //      if (!_bitmapHolderReleased) {
   //         synchronized (ILogger.instance()) {
   //            final StringBuffer msg = new StringBuffer();
   //            msg.append("************\n");
   //            msg.append("Image_Android finalized without releasing the _bitmapHolder created at: \n");
   //            msg.append(_createdAt);
   //            msg.append("************\n");
   //            ILogger.instance().logError(msg.toString());
   //         }
   //      }
   //      super.finalize();
   //   }

}
