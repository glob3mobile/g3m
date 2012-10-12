

package org.glob3.mobile.specific;

import java.util.HashMap;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.ITimer;

import com.google.gwt.core.client.JavaScriptObject;


public final class Factory_WebGL
         extends
            IFactory {

   boolean first = true;


   @Override
   public ITimer createTimer() {
      return new Timer_WebGL();
   }


   @Override
   public void deleteTimer(final ITimer timer) {
   }

   // TODO TEMP HACK TO PRELOAD IMAGES

   final HashMap<String, IImage> _downloadedImages = new HashMap<String, IImage>();


   @Override
   public IImage createImageFromFileName(final String filename) {
      return _downloadedImages.get(filename);

      //      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   public void storeDownloadedImage(final String url,
                                    final JavaScriptObject imgJS) {
      final IImage img = new Image_WebGL(imgJS);

      if (((Image_WebGL) img).getImage() != null) {
         _downloadedImages.put(url, img);
      }
   }


   // END TEMP HACK TO PRELOAD IMAGES


   @Override
   public void deleteImage(final IImage image) {
   }


   @Override
   public IImage createImageFromSize(final int width,
                                     final int height) {
      return new Image_WebGL(jsCreateImageFromSize(width, height));
   }


   private native JavaScriptObject jsCreateImageFromSize(final int width,
                                                         final int height) /*-{
		var img = new Image();
		img.width = width;
		img.height = height;

		return img;
   }-*/;


   @Override
   public IImage createImageFromBuffer(final IByteBuffer buffer) {
      // TODO Auto-generated method stub
      throw new RuntimeException("NOT IMPLEMENTED IMAGE FORM BUFFER");
   }


   @Override
   public IFloatBuffer createFloatBuffer(final int size) {
      return new FloatBuffer_WebGL(size);
   }


   @Override
   public IIntBuffer createIntBuffer(final int size) {
      return new IntBuffer_WebGL(size);
   }


   @Override
   public IByteBuffer createByteBuffer(final int length) {
      return new ByteBuffer_WebGL(length);
   }


   @Override
   public IByteBuffer createByteBuffer(final byte[] data,
                                       final int length) {
      return new ByteBuffer_WebGL(data, length);
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
      return new FloatBuffer_WebGL(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
   }

}
