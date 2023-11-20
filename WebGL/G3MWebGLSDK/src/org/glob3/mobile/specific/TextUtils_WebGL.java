
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

public class TextUtils_WebGL extends ITextUtils {

   private static String toJSColor(final Color color) {
      if (color == null) {
         return null;
      }

      final int   r = Math.round(255 * color._red);
      final int   g = Math.round(255 * color._green);
      final int   b = Math.round(255 * color._blue);
      final float a = color._alpha;

      return "rgba(" + r + "," + g + "," + b + "," + a + ")";
   }

   @Override
   public native void createLabelImage(final String label, final float fontSize, final Color color, final Color shadowColor, final IImageListener listener,
                                       final boolean autodelete) /*-{
		var canvas = $doc.createElement("canvas");
		var context = canvas.getContext("2d");

		var font = "" + Math.round(fontSize) + "px sans-serif";
		context.font = font;

		var width = context.measureText(label).width;
		var height = Math.ceil(fontSize);
		if (shadowColor) {
			width += 2;
			height += 2;
		}

		canvas.width = width;
		canvas.height = height;
		context.font = font; // set font as the width/height changes reset the context

		context.fillStyle = @org.glob3.mobile.specific.TextUtils_WebGL::toJSColor(Lorg/glob3/mobile/generated/Color;)(color);

		if (shadowColor) {
			context.shadowColor = @org.glob3.mobile.specific.TextUtils_WebGL::toJSColor(Lorg/glob3/mobile/generated/Color;)(shadowColor);
			context.shadowBlur = 2;
			context.shadowOffsetX = 2;
			context.shadowOffsetY = 2;
		}

		context.textAlign = "left";
		context.textBaseline = "top";
		context.fillText(label, 0, 0);

		var jsResult = new Image();
		jsResult.onload = function() {
			var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
		};
		jsResult.src = canvas.toDataURL();

   }-*/;

   @Override
   public void labelImage(final IImage image, final String label, final LabelPosition labelPosition, final int separation, final float fontSize,
                          final Color color, final Color shadowColor, final IImageListener listener, final boolean autodelete) {

      final boolean labelBottom;
      if (labelPosition == LabelPosition.Bottom) {
         labelBottom = true;
      }
      else if (labelPosition == LabelPosition.Right) {
         labelBottom = false;
      }
      else {
         ILogger.instance().logError("Unsupported LabelPosition");

         listener.imageCreated(null);
         return;
      }

      if (image == null) {
         createLabelImage(label, fontSize, color, shadowColor, listener, autodelete);
      }
      else {
         nativeLabelImage(image, label, labelBottom, separation, fontSize, color, shadowColor, listener);
      }
   }

   private native void nativeLabelImage(final IImage image, final String label, final boolean labelBottom, final int separation, final float fontSize,
                                        final Color color, final Color shadowColor, final IImageListener listener) /*-{
		//debugger;

		var canvas = $doc.createElement("canvas");
		var context = canvas.getContext("2d");

		var font = "" + Math.round(fontSize) + "px sans-serif";
		context.font = font;

		var textWidth = context.measureText(label).width;
		var textHeight = Math.ceil(fontSize);
		if (shadowColor) {
			textWidth += 2;
			textHeight += 2;
		}

		var imageWidth = image.@org.glob3.mobile.generated.IImage::getWidth()();
		var imageHeight = image.@org.glob3.mobile.generated.IImage::getHeight()();

		var resultWidth;
		var resultHeight;
		if (labelBottom) {
			resultWidth = Math.max(textWidth, imageWidth);
			resultHeight = textHeight + separation + imageHeight;
		} else {
			resultWidth = textWidth + separation + imageWidth;
			resultHeight = Math.max(textHeight, imageHeight);
		}

		canvas.width = resultWidth;
		canvas.height = resultHeight;
		context.font = font; // set font as the width/height changes reset the context

		//context.fillStyle = "green"; // for debug
		//context.fillRect(0, 0, resultWidth, resultHeight); // for debug

		var htmlImage = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		if (labelBottom) {
			context.drawImage(htmlImage, (resultWidth - imageWidth) / 2, 0);
		} else {
			context.drawImage(htmlImage, 0, (resultHeight - imageHeight) / 2);
		}

		context.fillStyle = @org.glob3.mobile.specific.TextUtils_WebGL::toJSColor(Lorg/glob3/mobile/generated/Color;)(color);

		if (shadowColor) {
			context.shadowColor = @org.glob3.mobile.specific.TextUtils_WebGL::toJSColor(Lorg/glob3/mobile/generated/Color;)(shadowColor);
			context.shadowBlur = 2;
			context.shadowOffsetX = 2;
			context.shadowOffsetY = 2;
		}

		context.textAlign = "left";
		context.textBaseline = "top";
		//context.fillText(label, 0, 0);

		if (labelBottom) {
			context.fillText(label, (resultWidth - textWidth) / 2, imageHeight
					+ separation);
		} else {
			context.fillText(label, imageWidth + separation,
					(resultHeight - textHeight) / 2);
		}

		var jsResult = new Image();
		jsResult.onload = function() {
			var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
		};
		jsResult.src = canvas.toDataURL();

   }-*/;

}
