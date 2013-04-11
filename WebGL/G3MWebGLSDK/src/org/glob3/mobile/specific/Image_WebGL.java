

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
   public native void combineWith(final ArrayList<IImage> images,
                                  final ArrayList<RectangleI> rectangles,
                                  final int width,
                                  final int height,
                                  final IImageListener listener,
                                  final boolean autodelete) /*-{

		var imagesSize = images.@java.util.ArrayList::size()();
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

			var jsResult = new Image();
			jsResult.onload = function() {
				var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
				listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
			};
			jsResult.src = canvas.toDataURL();

		}

   }-*/;


   @Override
   public native void combineWith(final IImage other,
                                  final RectangleI rect,
                                  final int width,
                                  final int height,
                                  final IImageListener listener,
                                  final boolean autodelete) /*-{

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


   @Override
   public String description() {
      return "Image WebGL " + getWidth() + " x " + getHeight() + ", _image=(" + _imgObject + ")";
   }


   @Override
   public IImage shallowCopy() {
      return new Image_WebGL(_imgObject);
   }


}
