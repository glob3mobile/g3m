

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
<<<<<<< HEAD
=======
import org.glob3.mobile.generated.ICanvas;
>>>>>>> webgl-port
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.IShortBuffer;
import org.glob3.mobile.generated.ITimer;
<<<<<<< HEAD


public class Factory_JavaDesktop
    extends
      IFactory {

  @Override
  public void createImageFromFileName(final String pFilename,
                                      final IImageListener pListener,
                                      final boolean pAutodelete) {
    // TODO Auto-generated method stub

  }


  @Override
  public void createImageFromBuffer(final IByteBuffer pBuffer,
                                    final IImageListener pListener,
                                    final boolean pAutodelete) {
    // TODO Auto-generated method stub

  }


  @Override
  public void createImageFromSize(final int pWidth,
                                  final int pHeight,
                                  final IImageListener pListener,
                                  final boolean pAutodelete) {
    // TODO Auto-generated method stub

  }


  @Override
  public void deleteImage(final IImage pImage) {
    // TODO Auto-generated method stub

  }


  @Override
  public ITimer createTimer() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void deleteTimer(final ITimer pTimer) {
    // TODO Auto-generated method stub

  }


  @Override
  public IFloatBuffer createFloatBuffer(final int size) {
    return new FloatBuffer_JavaDesktop(size);
  }


  @Override
  public IFloatBuffer createFloatBuffer(final float pF0,
                                        final float pF1,
                                        final float pF2,
                                        final float pF3,
                                        final float pF4,
                                        final float pF5,
                                        final float pF6,
                                        final float pF7,
                                        final float pF8,
                                        final float pF9,
                                        final float pF10,
                                        final float pF11,
                                        final float pF12,
                                        final float pF13,
                                        final float pF14,
                                        final float pF15) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public IIntBuffer createIntBuffer(final int pSize) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public IShortBuffer createShortBuffer(final int size) {
    return null;
  }


  @Override
  public IByteBuffer createByteBuffer(final int length) {
    return new ByteBuffer_JavaDesktop(length);
  }


  @Override
  public IByteBuffer createByteBuffer(final byte[] data,
                                      final int length) {
    return new ByteBuffer_JavaDesktop(data);
  }
=======
import org.glob3.mobile.generated.IWebSocket;
import org.glob3.mobile.generated.IWebSocketListener;
import org.glob3.mobile.generated.URL;


public class Factory_JavaDesktop
         extends
            IFactory {

   @Override
   public void createImageFromFileName(final String pFilename,
                                       final IImageListener pListener,
                                       final boolean pAutodelete) {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public void createImageFromBuffer(final IByteBuffer pBuffer,
                                     final IImageListener pListener,
                                     final boolean pAutodelete) {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public void deleteImage(final IImage pImage) {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public ITimer createTimer() {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public void deleteTimer(final ITimer pTimer) {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public IFloatBuffer createFloatBuffer(final int size) {
      return new FloatBuffer_JavaDesktop(size);
   }


   @Override
   public IFloatBuffer createFloatBuffer(final float pF0,
                                         final float pF1,
                                         final float pF2,
                                         final float pF3,
                                         final float pF4,
                                         final float pF5,
                                         final float pF6,
                                         final float pF7,
                                         final float pF8,
                                         final float pF9,
                                         final float pF10,
                                         final float pF11,
                                         final float pF12,
                                         final float pF13,
                                         final float pF14,
                                         final float pF15) {
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
   public IByteBuffer createByteBuffer(final byte[] data,
                                       final int length) {
      return new ByteBuffer_JavaDesktop(data);
   }


   @Override
   public ICanvas createCanvas() {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public IWebSocket createWebSocket(final URL url,
                                     final IWebSocketListener listener,
                                     final boolean autodeleteListener,
                                     final boolean autodeleteWebSocket) {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public IShortBuffer createShortBuffer(final short[] array) {
      throw new RuntimeException("Not yet implemented");
   }


   @Override
   public IFloatBuffer createFloatBuffer(final float[] array) {
      return new FloatBuffer_JavaDesktop(array);
   }

>>>>>>> webgl-port

}
