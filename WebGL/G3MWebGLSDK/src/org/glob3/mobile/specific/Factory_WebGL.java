

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.ITimer;


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


   @Override
   public IImage createImageFromFileName(final String filename) {

      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");

      //      if (first) {
      //         final int TODO_IMPLEMENT; //!!!!
      //         final Image_WebGL im = new Image_WebGL(null);
      //         im.loadFromURL(filename);
      //         first = false;
      //
      //         while (!im.isLoadedFromURL()) {
      //            //WAIT UNTIL LOADED
      //         }
      //
      //         return im;
      //      }
      //      return null;
   }


   @Override
   public void deleteImage(final IImage image) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public IImage createImageFromSize(final int width,
                                     final int height) {
      /// TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public IImage createImageFromBuffer(final IByteBuffer buffer) {
      // TODO Auto-generated method stub
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
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
      // TODO Auto-generated method stub
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }

}
