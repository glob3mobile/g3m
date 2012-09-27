

package org.glob3.mobile.specific;

import java.util.HashMap;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.ITimer;

import com.google.gwt.core.client.JavaScriptObject;


public class Factory_WebGL
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
      /// TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED IMAGE FROM SIZE");
   }


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
      return new ByteBuffer_WebGL(data);
   }

}
