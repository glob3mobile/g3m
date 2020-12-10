

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.*;


public final class Factory_WebGL
                                 extends
                                    IFactory {


   @Override
   public ITimer createTimer() {
      return new Timer_WebGL();
   }


   @Override
   public IFloatBuffer createFloatBuffer(final int size) {
      return new FloatBuffer_WebGL(size);
   }


   @Override
   public IByteBuffer createByteBuffer(final int length) {
      return new ByteBuffer_WebGL(length);
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


   @Override
   public IIntBuffer createIntBuffer(final int size) {
      return new IntBuffer_WebGL(size);
   }


   @Override
   public IShortBuffer createShortBuffer(final int size) {
      return new ShortBuffer_WebGL(size);
   }


   @Override
   public ICanvas createCanvas(final boolean retina) {
      return new Canvas_WebGL(retina, -1);
   }


   @Override
   public ICanvas createCanvas(final boolean retina,
                               final int maxSize) {
      return new Canvas_WebGL(retina, maxSize);
   }


   @Override
   public IWebSocket createWebSocket(final URL url,
                                     final IWebSocketListener listener,
                                     final boolean autodeleteListener,
                                     final boolean autodeleteWebSocket) {
      return new WebSocket_WebGL(url, listener, autodeleteListener, autodeleteWebSocket);
   }


   @Override
   public IShortBuffer createShortBuffer(final short[] array,
                                         final int length) {
      return new ShortBuffer_WebGL(array, length);
   }


   @Override
   public IFloatBuffer createFloatBuffer(final float[] array,
                                         final int length) {
      return new FloatBuffer_WebGL(array, length);
   }


   @Override
   protected IDeviceInfo createDeviceInfo() {
      return new DeviceInfo_WebGL();
   }


}
