

package org.glob3.mobile.client.specific;

import org.glob3.mobile.client.generated.IFactory;
import org.glob3.mobile.client.generated.IImage;
import org.glob3.mobile.client.generated.ITimer;

import com.google.gwt.user.client.ui.Image;


public class Factory_WebGL
         extends
            IFactory {

   @Override
   public ITimer createTimer() {
      return new Timer_WebGL();
   }


   @Override
   public void deleteTimer(final ITimer timer) {
      // TODO Auto-generated method stub

   }


   @Override
   public IImage createImageFromFileName(final String filename) {
      final Image image = new Image(filename);

      //      image.addLoadHandler(new LoadHandler() {
      //         public void onLoad(LoadEvent event) {
      //            context.drawImage(ImageElement.as(image.getElement()), 0, 0);
      //          }
      //        });

      return new Image_WebGL(image);
   }


   @Override
   public void deleteImage(final IImage image) {
      // TODO Auto-generated method stub

   }

}
