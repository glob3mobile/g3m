

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
      //TODO CHECK IMPLEMENTATION
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
      //      final Image_WebGL im = new Image_WebGL(filename);
      //      while (!im.isLoadedFromURL()) {
      //         //WAIT UNTIL LOADED
      //         try {
      //            Thread.sleep(100);
      //         }
      //         catch (final InterruptedException e) {
      //            // TODO Auto-generated catch block
      //            e.printStackTrace();
      //         }
      //      }
      //
      //      return im;
   }


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
