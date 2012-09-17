

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import com.google.gwt.core.client.JavaScriptObject;


public class Image_WebGL
         extends
            IImage {

   private JavaScriptObject _imgObject;
   private boolean          _arrived = false;


   public Image_WebGL() {
      _imgObject = null;
   }


   public Image_WebGL(final JavaScriptObject data) {
      _imgObject = data;
      if ((jsGetWidth() <= 0) || (jsGetHeight() <= 0)) {
         _imgObject = null;
      }
      else {
         _arrived = true;
      }
   }


   public JavaScriptObject getImage() {
      if (_arrived) {
         return _imgObject;
      }

      return null;
   }


   public void loadFromURL(final String url) {
      _imgObject = jsCreateImgObject(this, url);
   }


   public boolean isLoadedFromURL() {
      return _arrived;
   }


   private void onArrive() {
      _arrived = true;
   }


   private void onError() {
      _arrived = false;
   }


   @Override
   public int getWidth() {
      return jsGetWidth();
   }


   @Override
   public int getHeight() {
      return jsGetHeight();
   }


   @Override
   public IImage combineWith(final IImage other,
                             final int width,
                             final int height) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public IImage combineWith(final IImage other,
                             final Rectangle rect,
                             final int width,
                             final int height) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public IImage subImage(final Rectangle rect) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public IImage scale(final int width,
                       final int height) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public String description() {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   @Override
   public IImage copy() {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED FROM FILENAME");
   }


   private native JavaScriptObject jsCreateImgObject(Image_WebGL instance,
                                                     String url) /*-{
		var imgObject = new Image();
		imgObject.onload = function() {
			debugger;
			$entry(instance.@org.glob3.mobile.specific.Image_WebGL::onArrive()());
		}
		imgObject.image.onabort = function() {
			debugger;
			$entry(instance.@org.glob3.mobile.specific.Image_WebGL::onError()());
		}
		imgObject.image.onerror = function() {
			debugger;
			$entry(instance.@org.glob3.mobile.specific.Image_WebGL::onError()());
		}

		imgObject.src = url;

		return imgObject;
   }-*/;


   private native int jsGetWidth() /*-{
		return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width;
   }-*/;


   private native int jsGetHeight() /*-{
		return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height;
   }-*/;
}
