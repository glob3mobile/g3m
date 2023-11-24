
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

public class Factory_JavaDesktop extends IFactory {

   @Override
   public ITimer createTimer() {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public IFloatBuffer createFloatBuffer(final int size) {
      return new FloatBuffer_JavaDesktop(size);
   }

   @Override
   public IFloatBuffer createFloatBuffer(final float pF0, final float pF1, final float pF2, final float pF3, final float pF4, final float pF5, final float pF6,
                                         final float pF7, final float pF8, final float pF9, final float pF10, final float pF11, final float pF12,
                                         final float pF13, final float pF14, final float pF15) {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public IIntBuffer createIntBuffer(final int pSize) {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public IShortBuffer createShortBuffer(final int size) {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public IByteBuffer createByteBuffer(final int length) {
      return new ByteBuffer_JavaDesktop(length);
   }

   @Override
   public ICanvas createCanvas(final boolean retina) {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public ICanvas createCanvas(final boolean retina, final int maxSize) {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public IWebSocket createWebSocket(final URL url, final IWebSocketListener listener, final boolean autodeleteListener, final boolean autodeleteWebSocket,
                                     final boolean verboseErrors) {
      throw new RuntimeException("Not yet implemented");
   }

   //   @Override
   //   public IShortBuffer createShortBuffer(final short[] array) {
   //      throw new RuntimeException("Not yet implemented");
   //   }
   //   @Override
   //   public IFloatBuffer createFloatBuffer(final float[] array) {
   //      return new FloatBuffer_JavaDesktop(array);
   //   }

   @Override
   public IShortBuffer createShortBuffer(final short[] array, final int length) {
      throw new RuntimeException("Not yet implemented");
   }

   @Override
   public IFloatBuffer createFloatBuffer(final float[] array, final int length) {
      return new FloatBuffer_JavaDesktop(array, length);
   }

   @Override
   protected IDeviceInfo createDeviceInfo() {
      throw new RuntimeException("Not yet implemented");
   }

}
