

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.Vector2F;

import com.google.gwt.core.client.JavaScriptObject;


public class Canvas_WebGL
         extends
            ICanvas {

   private JavaScriptObject _domCanvas;
   private JavaScriptObject _domCanvasContext;

   private String           _currentDOMFont;
   private float            _currentFontSize;


   Canvas_WebGL() {
      initialize();
   }


   native void initialize() /*-{
		var canvas = $doc.createElement("canvas");
		var context = canvas.getContext("2d");

		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvas = canvas;
		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext = context;
   }-*/;


   @Override
   protected native void _initialize(final int width,
                                     final int height) /*-{
		var canvas = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvas;
		canvas.width = width;
		canvas.height = height;

		this.@org.glob3.mobile.specific.Canvas_WebGL::tryToSetCurrentFontToContext()();
   }-*/;


   private static String createDOMFont(final GFont font) {
      String domFont = "";

      if (font.isItalic()) {
         domFont += "italic ";
      }
      if (font.isBold()) {
         domFont += "bold ";
      }

      domFont += font.getSize() + "pt";

      if (font.isSerif()) {
         domFont += " serif";
      }
      else if (font.isSansSerif()) {
         domFont += " sans-serif";
      }
      else if (font.isMonospaced()) {
         domFont += " monospace";
      }

      return domFont;
   }


   private native void tryToSetCurrentFontToContext() /*-{
		var currentDOMFont = this.@org.glob3.mobile.specific.Canvas_WebGL::_currentDOMFont;
		if (currentDOMFont) {
			this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.font = currentDOMFont;
		}
   }-*/;


   @Override
   protected void _setFont(final GFont font) {
      _currentDOMFont = createDOMFont(font);
      _currentFontSize = font.getSize();

      tryToSetCurrentFontToContext();
   }


   @Override
   protected native void _createImage(final IImageListener listener,
                                      final boolean autodelete) /*-{
		var jsImage = new Image();
		jsImage.onload = function() {
			var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsImage);
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
			if (autodelete) {
				listener.@org.glob3.mobile.generated.IImageListener::dispose()();
			}
		};
		jsImage.src = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvas
				.toDataURL();
   }-*/;


   @Override
   protected native Vector2F _textExtent(final String text) /*-{
		var width = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext
				.measureText(text).width;

		var height = Math
				.round(this.@org.glob3.mobile.specific.Canvas_WebGL::_currentFontSize * 1.66);

		return @org.glob3.mobile.generated.Vector2F::new(FF)(width, height);
   }-*/;


   private static String createDOMColor(final Color color) {
      if (color == null) {
         return null;
      }

      final int r = Math.round(255 * color.getRed());
      final int g = Math.round(255 * color.getGreen());
      final int b = Math.round(255 * color.getBlue());
      final float a = color.getAlpha();

      return "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
   }


   @Override
   protected native void _setFillColor(final Color color) /*-{
		var jsColor = @org.glob3.mobile.specific.Canvas_WebGL::createDOMColor(Lorg/glob3/mobile/generated/Color;)(color);
		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.fillStyle = jsColor;
   }-*/;


   @Override
   protected native void _setStrokeColor(final Color color) /*-{
		var jsColor = @org.glob3.mobile.specific.Canvas_WebGL::createDOMColor(Lorg/glob3/mobile/generated/Color;)(color);
		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.strokeStyle = jsColor;
   }-*/;


   @Override
   protected native void _setStrokeWidth(final float width) /*-{
		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.lineWidth = width;
   }-*/;


   @Override
   protected native void _setShadow(final Color color,
                                    final float blur,
                                    final float offsetX,
                                    final float offsetY) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;

		context.shadowColor = @org.glob3.mobile.specific.Canvas_WebGL::createDOMColor(Lorg/glob3/mobile/generated/Color;)(color);
		context.shadowBlur = blur;
		context.shadowOffsetX = offsetX;
		context.shadowOffsetY = offsetY;
   }-*/;


   @Override
   protected native void _removeShadow() /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;

		context.shadowColor = "rgba(0,0,0,0)";
		context.shadowBlur = 0;
		context.shadowOffsetX = 0;
		context.shadowOffsetY = 0;
   }-*/;


   @Override
   protected native void _fillRectangle(final float left,
                                        final float top,
                                        final float width,
                                        final float height) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.fillRect(left, top, width, height);
   }-*/;


   @Override
   protected native void _strokeRectangle(final float left,
                                          final float top,
                                          final float width,
                                          final float height) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.strokeRect(left, top, width, height);
   }-*/;


   @Override
   protected native void _fillAndStrokeRectangle(final float left,
                                                 final float top,
                                                 final float width,
                                                 final float height) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.fillRect(left, top, width, height);
		context.strokeRect(left, top, width, height);
   }-*/;


   @Override
   protected native void _fillText(final String text,
                                   final float left,
                                   final float top) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var textHeight = this.@org.glob3.mobile.specific.Canvas_WebGL::_currentFontSize * 1.66;
		context.fillText(text, left, top + textHeight);
   }-*/;


   private native void roundRect(final float x,
                                 final float y,
                                 final float width,
                                 final float height,
                                 final float radius,
                                 final boolean fill,
                                 final boolean stroke) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.beginPath();
		context.moveTo(x + radius, y);
		context.lineTo(x + width - radius, y);
		context.quadraticCurveTo(x + width, y, x + width, y + radius);
		context.lineTo(x + width, y + height - radius);
		context.quadraticCurveTo(x + width, y + height, x + width - radius, y
				+ height);
		context.lineTo(x + radius, y + height);
		context.quadraticCurveTo(x, y + height, x, y + height - radius);
		context.lineTo(x, y + radius);
		context.quadraticCurveTo(x, y, x + radius, y);
		context.closePath();
		if (fill) {
			context.fill();
		}
		if (stroke) {
			context.stroke();
		}
   }-*/;


   @Override
   protected void _fillRoundedRectangle(final float left,
                                        final float top,
                                        final float width,
                                        final float height,
                                        final float radius) {
      roundRect(left, top, width, height, radius, true, false);
   }


   @Override
   protected void _strokeRoundedRectangle(final float left,
                                          final float top,
                                          final float width,
                                          final float height,
                                          final float radius) {
      roundRect(left, top, width, height, radius, false, true);
   }


   @Override
   protected void _fillAndStrokeRoundedRectangle(final float left,
                                                 final float top,
                                                 final float width,
                                                 final float height,
                                                 final float radius) {
      roundRect(left, top, width, height, radius, true, true);
   }


   @Override
   protected native void _drawImage(IImage image,
                                    float left,
                                    float top) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::_imgObject

		context.drawImage(imageJS, left, top);
   }-*/;


   @Override
   protected native void _drawImage(IImage image,
                                    float left,
                                    float top,
                                    float width,
                                    float height) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::_imgObject

		context.drawImage(imageJS, left, top, width, height);
   }-*/;


   @Override
   protected native void _drawImage(IImage image,
                                    float srcLeft,
                                    float srcTop,
                                    float srcWidth,
                                    float srcHeight,
                                    float destLeft,
                                    float destTop,
                                    float destWidth,
                                    float destHeight) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		context.drawImage(imageJS, srcLeft, srcTop, srcWidth, srcHeight,
				destLeft, destTop, destWidth, destHeight);
   }-*/;


}
