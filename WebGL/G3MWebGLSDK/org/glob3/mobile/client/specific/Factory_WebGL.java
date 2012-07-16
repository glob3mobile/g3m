

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

   boolean first = true;

   @Override
   public IImage createImageFromFileName(final String filename) {
	   if (first){
	   Image_WebGL im = new Image_WebGL();
	   im.loadFromURL(filename);
	   first = false;
	   
	   while(!im.isLoadedFromURL()){
		   //WAIT UNTIL LOADED
	   }

      return im;
	   } return null;
   }


   @Override
   public void deleteImage(final IImage image) {
      // TODO Auto-generated method stub

   }

}
