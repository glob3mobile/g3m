

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.RectangleI;
import org.glob3.mobile.generated.Vector2I;

import com.google.gwt.core.client.JavaScriptObject;


public final class Image_WebGL
         extends
            IImage {

   private JavaScriptObject _imgObject; //IMAGE JS


   // image handling functions
   //   private JavaScriptObject _canvas;
   //   private JavaScriptObject _context;


   //   private static class CanvasContext {
   //      final private JavaScriptObject _canvas;
   //      final private JavaScriptObject _context;
   //
   //
   //      private CanvasContext(final JavaScriptObject canvas,
   //                            final JavaScriptObject context) {
   //         _canvas = canvas;
   //         _context = context;
   //      }
   //
   //
   //      public JavaScriptObject getCanvas() {
   //         return _canvas;
   //      }
   //
   //
   //      public JavaScriptObject getContext() {
   //         return _context;
   //      }
   //   }


   //   private static native CanvasContext createCanvasContext(final int width,
   //                                                           final int height) /*-{
   //		var canvas = $doc.createElement("canvas");
   //		var context = canvas.getContext("2d");
   //
   //		canvas.width = width;
   //		canvas.height = height;
   //
   //		//context.clearRect(0, 0, width, height);
   //
   //		return @org.glob3.mobile.specific.Image_WebGL.CanvasContext::new(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(canvas, context);
   //   }-*/;


   //   public Image_WebGL() {
   //      _imgObject = null;
   //   }


   public Image_WebGL(final JavaScriptObject data) {
      _imgObject = data;
      if ((getWidth() <= 0) || (getHeight() <= 0)) {
         _imgObject = null;
      }
   }


   //   public Image_WebGL(final JavaScriptObject data,
   //                      final int width,
   //                      final int height) {
   //      _imgObject = data;
   //      if (!jsIsDataValid(width, height)) {
   //         _imgObject = null;
   //      }
   //   }


   //   public Image_WebGL(final int width,
   //                      final int height) {
   //      jsInitImgHandlingObjects(width, height);
   //      _imgObject = jsCreateFromCanvasDataURL(width, height);
   //   }


   //   private native boolean jsIsDataValid(final int width,
   //                                        final int height) /*-{
   //		if (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.src == "data:,") {
   //			return false;
   //		} else {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width = width;
   //			this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height = height;
   //			return true;
   //		}
   //   }-*/;


   //   private native JavaScriptObject jsCreateFromCanvasDataURL(final int width,
   //                                                             final int height) /*-{
   //		var img = new Image();
   //		img.src = this.@org.glob3.mobile.specific.Image_WebGL::_canvas
   //				.toDataURL();
   //		img.width = width;
   //		img.height = height;
   //
   //		return img;
   //
   //   }-*/;


   //   private native void jsInitImgHandlingObjects(final int width,
   //                                                final int height) /*-{
   //		if (this.@org.glob3.mobile.specific.Image_WebGL::_canvas == null) {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_canvas = $doc
   //					.createElement("canvas");
   //			this.@org.glob3.mobile.specific.Image_WebGL::_context = this.@org.glob3.mobile.specific.Image_WebGL::_canvas
   //					.getContext("2d");
   //		}
   //
   //		this.@org.glob3.mobile.specific.Image_WebGL::_canvas.width = width;
   //		this.@org.glob3.mobile.specific.Image_WebGL::_canvas.height = height;
   //
   //		this.@org.glob3.mobile.specific.Image_WebGL::_context.clearRect(0, 0,
   //				width, height);
   //   }-*/;


   public JavaScriptObject getImage() {
      return _imgObject;
   }


   @Override
   public native int getWidth() /*-{
		//		if (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject) {
		//			return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width;
		//		}
		//		return 0;
		var jsImage = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
		return jsImage ? jsImage.width : 0;
   }-*/;


   @Override
   public native int getHeight() /*-{
		//		if (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject) {
		//			return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height;
		//		}
		//		return 0;
		var jsImage = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
		return jsImage ? jsImage.height : 0;
   }-*/;


   @Override
   public Vector2I getExtent() {
      return new Vector2I(getWidth(), getHeight());
   }


   @Override
   public native void combineWith(final ArrayList<IImage> images,
                                  final ArrayList<RectangleI> rectangles,
                                  final int width,
                                  final int height,
                                  final IImageListener listener,
                                  final boolean autodelete) /*-{
		var imagesSize = images.@java.util.ArrayList::size();
		if (imagesSize == 0) {
			this.@org.glob3.mobile.specific.Image_WebGL::scale(IILorg/glob3/mobile/generated/IImageListener;Z)(width, height, listener, autodelete);
		} else if (imagesSize == 1) {
			var other = images.@java.util.ArrayList::get(I)(0);
			var rect = rectangles.@java.util.ArrayList::get(I)(0);
			this.@org.glob3.mobile.specific.Image_WebGL::combineWith(Lorg/glob3/mobile/generated/IImage;Lorg/glob3/mobile/generated/RectangleI;IILorg/glob3/mobile/generated/IImageListener;Z)(other, rect, width, height, listener, autodelete);
		} else {
			var canvas = $doc.createElement("canvas");
			canvas.width = width;
			canvas.height = height;

			var context = canvas.getContext("2d");
			context.drawImage(
					this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
					0, width, height);

			for ( var i = 0; i < imagesSize; i++) {
				var other = images.@java.util.ArrayList::get(I)(i);
				var jsOther = other.@org.glob3.mobile.specific.Image_WebGL::_imgObject;

				var rect = rectangles.@java.util.ArrayList::get(I)(i);
				var rectX = rect.@org.glob3.mobile.generated.RectangleI::_x;
				var rectY = rect.@org.glob3.mobile.generated.RectangleI::_y;
				var rectWidth = rect.@org.glob3.mobile.generated.RectangleI::_width;
				var rectHeight = rect.@org.glob3.mobile.generated.RectangleI::_height;

				context.drawImage(jsOther, rectX,
						(height - (rectY + rectHeight)), rectWidth, rectHeight);
			}
		}

		var jsResult = new Image();
		jsResult.onload = function() {
			var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
		};
		jsResult.src = canvas.toDataURL();

		//      final int imagesSize = images.size();
		//
		//      if (imagesSize == 0) {
		//         scale(width, height, listener, autodelete);
		//      }
		//      else {
		//         final Canvas canvas = Canvas.createIfSupported();
		//         canvas.setWidth(width + "px");
		//         canvas.setHeight(height + "px");
		//         canvas.setCoordinateSpaceWidth(width);
		//         canvas.setCoordinateSpaceHeight(height);
		//
		//         final Image image = new Image();
		//         final image.
		//         final ImageElement ie = ImageElement.
		//final CanvasElement ce;
		//
		//         final Context2d context = canvas.getContext2d();
		////         context.drawImage(_imgObject, 0, 0, width, height);
		////         context.drawImage
		//
		//         for (int i = 0; i < imagesSize; i++) {
		//            final Image_WebGL image = (Image_WebGL) images.get(i);
		//            final RectangleI rect = rectangles.get(i);
		//         }
		//      }
		//
		//      //context.;
		//      //     canvas.
		//      final String url = canvas.toDataUrl();
		//
		//      jsInitImgHandlingObjects(width, height);
		//
		//      final JavaScriptObject combinedImgJs = jsCombineWithRect( //
		//               ((Image_WebGL) other).getImage(), //
		//               rect._x, rect._y, rect._width, rect._height, //
		//               width, height);
		//      final Image_WebGL combinedImg = new Image_WebGL(combinedImgJs, width, height);
		//
		//      if (combinedImg.getImage() != null) {
		//         return combinedImg;
		//      }
		//      throw new RuntimeException("Unable to combine rect image");
   }-*/;


   //   @Override
   //   public void combineWith(final IImage other,
   //                           final int width,
   //                           final int height,
   //                           final IImageListener listener,
   //                           final boolean autodelete) {
   //      jsInitImgHandlingObjects(width, height);
   //
   //      final JavaScriptObject combinedImgJs = jsCombineWith(((Image_WebGL) other).getImage(), width, height);
   //      final Image_WebGL combinedImg = new Image_WebGL(combinedImgJs, width, height);
   //
   //      if (combinedImg.getImage() != null) {
   //         return combinedImg;
   //      }
   //      throw new RuntimeException("Unable to combine image");
   //   }


   //   private native JavaScriptObject jsCombineWith(final JavaScriptObject other,
   //                                                 final int width,
   //                                                 final int height) /*-{
   //		//		debugger;
   //
   //		if ((this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width != width)
   //				|| (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height != height)) {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
   //					this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
   //					0, width, height);
   //		} else {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
   //					this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
   //					0);
   //		}
   //		if ((other.width != width) || (other.height != height)) {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
   //					other, 0, 0, width, height);
   //		} else {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
   //					other, 0, 0);
   //		}
   //		var combinedImg = new Image();
   //		combinedImg.src = this.@org.glob3.mobile.specific.Image_WebGL::_canvas
   //				.toDataURL();
   //
   //		return combinedImg;
   //   }-*/;


   @Override
   public native void combineWith(final IImage other,
                                  final RectangleI rect,
                                  final int width,
                                  final int height,
                                  final IImageListener listener,
                                  final boolean autodelete) /*-{
		//      jsInitImgHandlingObjects(width, height);
		//
		//      final JavaScriptObject combinedImgJs = jsCombineWithRect( //
		//               ((Image_WebGL) other).getImage(), //
		//               rect._x, rect._y, rect._width, rect._height, //
		//               width, height);
		//      final Image_WebGL combinedImg = new Image_WebGL(combinedImgJs, width, height);
		//
		//      if (combinedImg.getImage() != null) {
		//         return combinedImg;
		//      }
		//      throw new RuntimeException("Unable to combine rect image");

		var canvas = $doc.createElement("canvas");
		canvas.width = width;
		canvas.height = height;

		var context = canvas.getContext("2d");
		context.drawImage(
				this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0, 0,
				width, height);

		var jsOther = other.@org.glob3.mobile.specific.Image_WebGL::_imgObject;

		var rectX = rect.@org.glob3.mobile.generated.RectangleI::_x;
		var rectY = rect.@org.glob3.mobile.generated.RectangleI::_y;
		var rectWidth = rect.@org.glob3.mobile.generated.RectangleI::_width;
		var rectHeight = rect.@org.glob3.mobile.generated.RectangleI::_height;

		context.drawImage(jsOther, rectX, (height - (rectY + rectHeight)),
				rectWidth, rectHeight);

		var jsResult = new Image();
		jsResult.onload = function() {
			var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
		};
		jsResult.src = canvas.toDataURL();

   }-*/;


   //   private native JavaScriptObject jsCombineWithRect(final JavaScriptObject other,
   //                                                     final int rectX,
   //                                                     final int rectY,
   //                                                     final int rectWidth,
   //                                                     final int rectHeight,
   //                                                     final int width,
   //                                                     final int height) /*-{
   //		//		debugger;
   //
   //		var context = this.@org.glob3.mobile.specific.Image_WebGL::_context;
   //
   //		if ((this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width != width)
   //				|| (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height != height)) {
   //			context.drawImage(
   //					this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
   //					0, width, height);
   //		} else {
   //			context.drawImage(
   //					this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
   //					0);
   //		}
   //
   //		context.drawImage(other, rectX, (height - (rectY + rectHeight)),
   //				rectWidth, rectHeight);
   //
   //		var result = new Image();
   //
   //		//		result.onload = function() {
   //		//		};
   //
   //		result.src = this.@org.glob3.mobile.specific.Image_WebGL::_canvas
   //				.toDataURL();
   //
   //		return result;
   //
   //		//		return @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;II)(result, width, height);
   //   }-*/;


   @Override
   public native void subImage(final RectangleI rect,
                               final IImageListener listener,
                               final boolean autodelete) /*-{
		var x = rect.@org.glob3.mobile.generated.RectangleI::_x;
		var y = rect.@org.glob3.mobile.generated.RectangleI::_y;
		var width = rect.@org.glob3.mobile.generated.RectangleI::_width;
		var height = rect.@org.glob3.mobile.generated.RectangleI::_height;

		var imgObject = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;

		if ((x == 0) && (y == 0) && (imgObject.width == width)
				&& (imgObject.height == height)) {
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(this);
		} else {
			var canvas = $doc.createElement("canvas");
			canvas.width = width;
			canvas.height = height;

			var context = canvas.getContext("2d");
			context.drawImage(imgObject, x, y, width, height, 0, 0, width,
					height);

			var jsResult = new Image();
			jsResult.onload = function() {
				var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
				listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
			};
			jsResult.src = canvas.toDataURL();
		}
   }-*/;


   @Override
   public native void scale(final int width,
                            final int height,
                            final IImageListener listener,
                            final boolean autodelete) /*-{
		var imgObject = this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;

		if ((imgObject.width == width) && (imgObject.height == height)) {
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(this);
		} else {
			var canvas = $doc.createElement("canvas");
			canvas.width = width;
			canvas.height = height;

			var context = canvas.getContext("2d");
			context.drawImage(imgObject, 0, 0, width, height);

			var jsResult = new Image();
			jsResult.onload = function() {
				var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
				listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
			};
			jsResult.src = canvas.toDataURL();
		}
   }-*/;


   //		}

   //      if ((width == getWidth()) && (height == getHeight())) {
   //         listener.imageCreated(this);
   //      }
   //      else {
   //         final CanvasContext cc = CanvasContext.create(width, height);
   //         
   //      }

   //      jsInitImgHandlingObjects(width, height);
   //
   //      final JavaScriptObject scaledImgJs = jsScaleImage(width, height);
   //      final Image_WebGL scaledImg = new Image_WebGL(scaledImgJs);
   //
   //      if (scaledImg.getImage() != null) {
   //         return scaledImg;
   //      }
   //      throw new RuntimeException("Unable to scale image");
   //   }-*/;


   //   private native JavaScriptObject jsScaleImage(final int width,
   //                                                final int height) /*-{
   //		//     debugger;
   //
   //		if ((this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.width != width)
   //				|| (this.@org.glob3.mobile.specific.Image_WebGL::_imgObject.height != height)) {
   //			this.@org.glob3.mobile.specific.Image_WebGL::_context.drawImage(
   //					this.@org.glob3.mobile.specific.Image_WebGL::_imgObject, 0,
   //					0, width, height);
   //
   //			var scaledImage = new Image();
   //			scaledImage.src = this.@org.glob3.mobile.specific.Image_WebGL::_canvas
   //					.toDataURL();
   //
   //			return scaledImage;
   //		} else {
   //			return this.@org.glob3.mobile.specific.Image_WebGL::_imgObject;
   //		}
   //
   //   }-*/;


   @Override
   public String description() {
      return "Image WebGL " + getWidth() + " x " + getHeight() + ", _image=(" + _imgObject + ")";
   }


   @Override
   public IImage shallowCopy() {
      return new Image_WebGL(_imgObject);
   }


}
