package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import com.google.gwt.core.client.JavaScriptObject;


public class Image_WebGL extends IImage {

   private JavaScriptObject _imgObject;

   private boolean          _arrived = false;


   public Image_WebGL() {
   }


   public JavaScriptObject getImage() {
      if (_arrived) {
         return jsGetImage(_imgObject);
      }

      return null;
   }


   public void loadFromURL(String url) {
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


   private native JavaScriptObject jsGetImage(JavaScriptObject imgObject) /*-{
		return imgObject.image;
   }-*/;


   private native JavaScriptObject jsCreateImgObject(Image_WebGL instance,
                                                     String url) /*-{
		imgObject = new Object();
		imgObject.image = new Image();
		imgObject.image.onload = function() {
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

		imgObject.image.src = url;

		return imgObject;
   }-*/;


   @Override
   public int getWidth() {
      // TODO Auto-generated method stub
      return 0;
   }


   @Override
   public int getHeight() {
      // TODO Auto-generated method stub
      return 0;
   }


   @Override
   public IImage combineWith(IImage other,
                             int width,
                             int height) {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public IImage combineWith(IImage other,
                             Rectangle rect,
                             int width,
                             int height) {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public IImage subImage(Rectangle rect) {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public ByteBuffer getEncodedImage() {
      // TODO Auto-generated method stub
      return null;
   }


   @Override
   public void fillWithRGBA8888(byte[] data,
                                int width,
                                int height) {
      // TODO Auto-generated method stub

   }

}
