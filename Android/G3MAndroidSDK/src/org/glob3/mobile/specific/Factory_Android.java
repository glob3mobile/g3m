

package org.glob3.mobile.specific;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.glob3.mobile.generated.ByteArrayWrapper;
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


public class Factory_Android
         extends
            IFactory {
   final Context _context;
   static Locale locale = new Locale("myLocale");


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
         return null;
      }

      return new Image_Android(bitmap);
   }


   @Override
   public void deleteImage(final IImage image) {
   }

   @Override
   public IImage createImageFromSize(final int width,
                                     final int height) {
      final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
      final Bitmap bmp = Bitmap.createBitmap(width, height, conf);
      return new Image_Android(bmp);
   }


   @Override
   public IFloatBuffer createFloatBuffer(int size) {
      return new FloatBuffer_Android(size);
   }


   @Override
   public IIntBuffer createIntBuffer(int size) {
      return new IntBuffer_Android(size);
   }


   @Override
   public IImage createImageFromData(ByteArrayWrapper buffer) {
      final Bitmap b = BitmapFactory.decodeByteArray(buffer.getData(), 0, buffer.getData().length);
      if (b == null) {
         ILogger.instance().logError("FACTORY", "Can't create image from data");
         return null;
      }
      return new Image_Android(b);
   }


   @Override
   public IByteBuffer createByteBuffer(byte[] data,
                                       int length) {
      return new ByteBuffer_Android(data);
   }

}
