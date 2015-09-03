

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Vector2I;

import com.google.gwt.core.client.JavaScriptObject;


public final class Image_WebGL
   extends
      IImage {

   private JavaScriptObject _imgObject; //IMAGE JS


   public Image_WebGL(final JavaScriptObject data) {
      _imgObject = data;
      if ((getWidth() <= 0) || (getHeight() <= 0)) {
         _imgObject = null;
      }
   }


   public JavaScriptObject getImage() {
      return _imgObject;
   }


   @Override
   public native int getWidth() /*-{
		var jsImage = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
		return jsImage ? jsImage.width : 0;
   }-*/;


   @Override
   public native int getHeight() /*-{
		var jsImage = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
		return jsImage ? jsImage.height : 0;
   }-*/;


   @Override
   public Vector2I getExtent() {
      return new Vector2I(getWidth(), getHeight());
   }


   @Override
   public String description() {
      return "Image_WebGL " + getWidth() + " x " + getHeight() + ", _image=(" + _imgObject + ")";
   }


   @Override
   public IImage shallowCopy() {
      return new Image_WebGL(_imgObject);
   }


   @Override
   public boolean isPremultiplied() {
      return false;
   }


}
