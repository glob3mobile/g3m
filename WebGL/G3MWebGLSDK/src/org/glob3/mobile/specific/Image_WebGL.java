

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Rectangle;

import com.google.gwt.core.client.JavaScriptObject;


public final class Image_WebGL
         extends
            IImage {

   private JavaScriptObject       _imgObject;    //IMAGE JS

   // image handling functions
   private final JavaScriptObject _canvas = null;
   private JavaScriptObject       _context;


   public Image_WebGL() {
      _imgObject = null;
   }


   public Image_WebGL(final JavaScriptObject data) {
      _imgObject = data;
      if ((jsGetWidth() <= 0) || (jsGetHeight() <= 0)) {
         _imgObject = null;
      }
   }


   public Image_WebGL(final JavaScriptObject data,
                      final int width,
                      final int height) {
      _imgObject = data;
      if (!jsIsDataValid(width, height)) {
         _imgObject = null;
      }
   }


   private native boolean jsIsDataValid(final int width,
                                        final int height) /*-{
		var that = this;
		if (that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.src == "data:,") {
			return false;
		} else {
			that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width = width;
			that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height = height;
			return true;
		}
   }-*/;


   private native void jsInitImgHandlingObjects(final int width,
                                                final int height) /*-{
		var that = this;

		if (that.@org.glob3.mobile.specific.Image_WebGL::_canvas == null) {

			that.@org.glob3.mobile.specific.Image_WebGL::_canvas = $doc
					.createElement("canvas");
			that.@org.glob3.mobile.specific.Image_WebGL::_context = that.@org.glob3.mobile.specific.Image_WebGL::_canvas
					.getContext("2d");
		}

		that.@org.glob3.mobile.specific.Image_WebGL::_canvas.width = width;
		that.@org.glob3.mobile.specific.Image_WebGL::_canvas.height = height;

		that.@org.glob3.mobile.specific.Image_WebGL::_context.clearRect(0, 0,
				width, height);
   }-*/;


   public JavaScriptObject getImage() {
      return _imgObject;
   }


   @Override
   public int getWidth() {
      return jsGetWidth();
   }


   private native int jsGetWidth() /*-{
		if (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject) {
			return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width;
		}
		return 0;
   }-*/;


   @Override
   public int getHeight() {
      return jsGetHeight();
   }


   private native int jsGetHeight() /*-{
		if (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject) {
			return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height;
		}
		return 0;
   }-*/;


   @Override
   public IImage combineWith(final IImage other,
                             final int width,
                             final int height) {
      jsInitImgHandlingObjects(width, height);

      final JavaScriptObject combinedImgJs = jsCombineWith(((Image_WebGL) other).getImage(), width, height);
      final Image_WebGL combinedImg = new Image_WebGL(combinedImgJs, width, height);

      if (combinedImg.getImage() != null) {
         return combinedImg;
      }
      throw new RuntimeException("Unable to combine image");
   }


   private native JavaScriptObject jsCombineWith(final JavaScriptObject other,
                                                 final int width,
                                                 final int height) /*-{
		//		debugger;
		var that = this;

		if ((that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width != width)
				|| (that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height != height)) {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					that.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
					0, width, height);
		} else {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					that.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
					0);
		}
		if ((other.width != width) || (other.height != height)) {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					other, 0, 0, width, height);
		} else {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					other, 0, 0);
		}
		var combinedImg = new Image();
		combinedImg.src = that.@org.glob3.mobile.specific.Image_WebGL::_canvas
				.toDataURL();

		return combinedImg;
   }-*/;


   @Override
   public IImage combineWith(final IImage other,
                             final Rectangle rect,
                             final int width,
                             final int height) {
      jsInitImgHandlingObjects(width, height);

      final JavaScriptObject combinedImgJs = jsCombineWithRect(((Image_WebGL) other).getImage(), (int) rect._x, (int) rect._y,
               (int) rect._width, (int) rect._height, width, height);
      final Image_WebGL combinedImg = new Image_WebGL(combinedImgJs, width, height);

      if (combinedImg.getImage() != null) {
         return combinedImg;
      }
      throw new RuntimeException("Unable to combine rect image");
   }


   private native JavaScriptObject jsCombineWithRect(final JavaScriptObject other,
                                                     final int rectX,
                                                     final int rectY,
                                                     final int rectWidth,
                                                     final int rectHeight,
                                                     final int width,
                                                     final int height) /*-{
		//		debugger;
		var that = this;

		if ((that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width != width)
				|| (that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height != height)) {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					that.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
					0, width, height);
		} else {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					that.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
					0);
		}

		that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(other,
				rectX, (height - (rectY + rectHeight)), rectWidth, rectHeight);

		var combinedImg = new Image();
		combinedImg.src = that.@org.glob3.mobile.specific.Image_WebGL::_canvas
				.toDataURL();

		return combinedImg;
   }-*/;


   @Override
   public IImage subImage(final Rectangle rect) {
      jsInitImgHandlingObjects((int) rect._width, (int) rect._height);

      final JavaScriptObject subImgJs = jsSubImage(rect._x, rect._y, rect._width, rect._height);
      final Image_WebGL subImg = new Image_WebGL(subImgJs);

      if (subImg.getImage() != null) {
         return subImg;
      }
      throw new RuntimeException("Unable to create subimage");
   }


   private native JavaScriptObject jsSubImage(final double x,
                                              final double y,
                                              final double width,
                                              final double height) /*-{
		//	debugger;
		var that = this;

		that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
				that.@org.glob3.mobile.specific.Image_WebGL::_imgObject, x, y,
				width, height, 0, 0, width, height);
		var subImage = new Image();
		subImage.src = that.@org.glob3.mobile.specific.Image_WebGL::_canvas
				.toDataURL();

		return subImage;
   }-*/;


   @Override
   public IImage scale(final int width,
                       final int height) {
      jsInitImgHandlingObjects(width, height);

      final JavaScriptObject scaledImgJs = jsScaleImage(width, height);
      final Image_WebGL scaledImg = new Image_WebGL(scaledImgJs);

      if (scaledImg.getImage() != null) {
         return scaledImg;
      }
      throw new RuntimeException("Unable to scale image");
   }


   private native JavaScriptObject jsScaleImage(final int width,
                                                final int height) /*-{
		//     debugger;
		var that = this;

		if ((that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width != width)
				|| (that.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height != height)) {
			that.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
					that.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
					0, width, height);

			var scaledImage = new Image();
			scaledImage.src = that.@org.glob3.mobile.specific.Image_WebGL::_canvas
					.toDataURL();

			return scaledImage;
		} else {
			return that.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
		}

   }-*/;


   @Override
   public String description() {
      // TODO review
      return "Image WebGL " + jsGetWidth() + " x " + jsGetHeight() + ", _image=(" + _imgObject + ")";
   }


   @Override
   public IImage shallowCopy() {
      final IImage imgCopy = new Image_WebGL(_imgObject);
      //      final IImage imgCopy = new Image_WebGL(jsShallowCopy(_imgObject));

      return imgCopy;
   }


   //   private native JavaScriptObject jsShallowCopy(final JavaScriptObject img) /*-{
   //		var copyImg = new Image();
   //		copyImg.src = img.src;
   //		copyImg.width = img.width;
   //		copyImg.height = img.height;
   //
   //		return copyImg;
   //   }-*/;


   //   private boolean          _isValid = false;
   //   
   //   
   //   // TODO Check if used. Functions to load image from "file name"
   //   private boolean _arrived = false;
   //   
   //   
   //   public Image_WebGL(final String url) {
   //      loadFromURL(url);
   //   }
   //   
   //   
   //   public void loadFromURL(final String url) {
   //      _imgObject = jsCreateImgObject(url);
   //   }
   //   
   //   
   //   public boolean isLoadedFromURL() {
   //      return _arrived;
   //   }
   //   
   //   
   //   private native JavaScriptObject jsCreateImgObject(String url) /*-{
   //		debugger;
   //		var thisInstance = this;
   //
   //		var imgObject = new Image();
   //		imgObject.onload = function() {
   //			//			debugger;
   //			thisInstance.@org.glob3.mobile.specific.Image_WebGL::onArrive()();
   //			console.log("loaded");
   //		}
   //		imgObject.onabort = function() {
   //			//			debugger;
   //			thisInstance.@org.glob3.mobile.specific.Image_WebGL::onError()();
   //			console.log("abort");
   //		}
   //		imgObject.onerror = function() {
   //			//			debugger;
   //			thisInstance.@org.glob3.mobile.specific.Image_WebGL::onError()();
   //			console.log("error");
   //		}
   //
   //		imgObject.src = url;
   //
   //		return imgObject;
   //   }-*/;
   //   
   //   
   //   private void onArrive() {
   //      _arrived = true;
   //   }
   //   
   //   
   //   private void onError() {
   //      _arrived = false;
   //   }

}
