

package org.glob3.mobile.specific;

import java.io.IOException;
import java.io.InputStream;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.ITimer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public final class Factory_Android
         extends
            IFactory {
   private final Context _context;


   public Factory_Android(final Context c) {
      _context = c;
   }


   @Override
   public ITimer createTimer() {
      return new Timer_Android();
   }


   @Override
   public void deleteTimer(final ITimer timer) {
   }


   @Override
   public IImage createImageFromFileName(final String filename) {

      Bitmap bitmap = null;
      InputStream is = null;
      try {
         is = _context.getAssets().open(filename);

         final int size = is.available();
         final byte[] imageData = new byte[size];
         is.read(imageData);
         bitmap = BitmapFactory.decodeByteArray(imageData, 0, size);
      }
      catch (final IOException e) {
         e.printStackTrace();
      }
      finally {
         if (is != null) {
            try {
               is.close();
            }
            catch (final IOException e) {
               // do nothing, just ignore the exception
            }
         }
      }

      if (bitmap == null) {
         ILogger.instance().logError("FACTORY: Can't create image from fileName");

         return null;
      }

      return new Image_Android(bitmap, null);
   }


   @Override
   public void deleteImage(final IImage image) {
   }


   @Override
   public IImage createImageFromSize(final int width,
                                     final int height) {
      final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      if (bitmap == null) {
         ILogger.instance().logError("FACTORY: Can't create empty image");
         return null;
      }
      return new Image_Android(bitmap, null);
   }


   @Override
   public IFloatBuffer createFloatBuffer(final int size) {
      return new FloatBuffer_Android(size);
   }


   @Override
   public IIntBuffer createIntBuffer(final int size) {
      return new IntBuffer_Android(size);
   }


   @Override
   public IByteBuffer createByteBuffer(final byte[] data,
                                       final int length) {
      return new ByteBuffer_Android(data);
   }


   @Override
   public IImage createImageFromBuffer(final IByteBuffer buffer) {
      final byte[] data = ((ByteBuffer_Android) buffer).getBuffer().array();
      final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
      if (bitmap == null) {
         ILogger.instance().logError("FACTORY", "Can't create image from data");
         return null;
      }
      return new Image_Android(bitmap, data);
   }


   @Override
   public IByteBuffer createByteBuffer(final int length) {
      return new ByteBuffer_Android(length);
   }


   @Override
   public IFloatBuffer createFloatBuffer(final float f0,
                                         final float f1,
                                         final float f2,
                                         final float f3,
                                         final float f4,
                                         final float f5,
                                         final float f6,
                                         final float f7,
                                         final float f8,
                                         final float f9,
                                         final float f10,
                                         final float f11,
                                         final float f12,
                                         final float f13,
                                         final float f14,
                                         final float f15) {
      return new FloatBuffer_Android(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
   }

}
