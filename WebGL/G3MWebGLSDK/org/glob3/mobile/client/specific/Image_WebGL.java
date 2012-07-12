

package org.glob3.mobile.client.specific;

import org.glob3.mobile.client.generated.IImage;

import com.google.gwt.user.client.ui.Image;


public class Image_WebGL
         extends
            IImage {

   final private Image _image;


   public Image_WebGL(final Image image) {
      _image = image;
   }


   public Image getImage() {
      return _image;
   }
}
