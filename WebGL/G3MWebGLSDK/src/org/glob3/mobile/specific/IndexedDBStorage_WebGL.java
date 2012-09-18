

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.InitializationContext;
import org.glob3.mobile.generated.URL;


public class IndexedDBStorage_WebGL
         implements
            IStorage {

   @Override
   public boolean containsBuffer(final URL url) {
      // TODO Auto-generated method stub
      return false;
   }


   @Override
   public void saveBuffer(final URL url,
                          final IByteBuffer buffer) {
      // TODO Auto-generated method stub

   }


   @Override
   public IByteBuffer readBuffer(final URL url) {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public boolean containsImage(final URL url) {
      // TODO Auto-generated method stub
      return false;
   }


   @Override
   public void saveImage(final URL url,
                         final IImage image) {
      // TODO Auto-generated method stub

   }


   @Override
   public IImage readImage(final URL url) {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public void onResume(final InitializationContext ic) {
      // TODO Auto-generated method stub

   }


   @Override
   public void onPause(final InitializationContext ic) {
      // TODO Auto-generated method stub

   }

}
