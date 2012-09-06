

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IImage;
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
      if (first) {
         final Image_WebGL im = new Image_WebGL();
         im.loadFromURL(filename);
         first = false;

         while (!im.isLoadedFromURL()) {
            //WAIT UNTIL LOADED
         }

         return im;
      }
      return null;
   }


   @Override
   public void deleteImage(final IImage image) {
      // TODO this method must be implemented

   }


   @Override
   public IImage createImageFromData(final ByteBuffer buffer) {
      // TODO this method must be implemented
      return null;
   }


   @Override
   public IImage createImageFromSize(final int width,
                                     final int height) {
      /// TODO this method must be implemented
      return null;
   }

}
