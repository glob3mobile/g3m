

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import com.google.gwt.core.client.JavaScriptObject;


public class Image_WebGL
         extends
            IImage {

   private JavaScriptObject _imgObject;      //IMAGE JS
   private boolean          _arrived = false;


   public Image_WebGL() {
      _imgObject = null;
   }


   public Image_WebGL(final String url) {
      loadFromURL(url);
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
      _imgObject = jsCreateImgObject(url);
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
      if (_arrived) {
         return jsGetWidth();
      }
      return 0;
   }


   @Override
   public int getHeight() {
      if (_arrived) {
         return jsGetHeight();
      }
      return 0;
   }


   @Override
   public IImage combineWith(final IImage other,
                             final int width,
                             final int height) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED Image_WebGL::combineWith");
   }


   @Override
   public IImage combineWith(final IImage other,
                             final Rectangle rect,
                             final int width,
                             final int height) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED Image_WebGL::combineWith rect");
   }


   @Override
   public IImage subImage(final Rectangle rect) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED Image_WebGL::subImage");
   }


   @Override
   public IImage scale(final int width,
                       final int height) {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED Image_WebGL::scale");
   }


   @Override
   public String description() {
      // TODO this method must be implemented
      throw new RuntimeException("NOT IMPLEMENTED Image_WebGL::description");
   }


   @Override
   public IImage shallowCopy() {
      //    throw new RuntimeException("NOT IMPLEMENTED Image_WebGL::copy");
      final IImage imgCopy = new Image_WebGL(_imgObject);

      return imgCopy;
   }


   private native JavaScriptObject jsCreateImgObject(String url) /*-{
		debugger;
		var thisInstance = this;

		var imgObject = new Image();
		imgObject.onload = function() {
			//			debugger;
			thisInstance.@org.glob3.mobile.specific.Image_WebGL::onArrive()();
			console.log("loaded");
		}
		imgObject.onabort = function() {
			//			debugger;
			thisInstance.@org.glob3.mobile.specific.Image_WebGL::onError()();
			console.log("abort");
		}
		imgObject.onerror = function() {
			//			debugger;
			thisInstance.@org.glob3.mobile.specific.Image_WebGL::onError()();
			console.log("error");
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
