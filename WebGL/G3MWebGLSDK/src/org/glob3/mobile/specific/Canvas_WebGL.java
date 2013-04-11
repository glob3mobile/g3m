

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.Vector2F;

import com.google.gwt.core.client.JavaScriptObject;


public class Canvas_WebGL
         extends
            ICanvas {

   private JavaScriptObject _canvas;
   private JavaScriptObject _context;

   private String           _currentDOMFont;
   private float            _currentFontSize;


   Canvas_WebGL() {
      initialize();
   }


   native void initialize() /*-{
		this.@org.glob3.mobile.specific.Canvas_WebGL::_canvas = $doc
				.createElement("canvas");

		this.@org.glob3.mobile.specific.Canvas_WebGL::_context = canvas
				.getContext("2d");
   }-*/;


   @Override
   protected native void _initialize(final int width,
                                     final int height) /*-{
		var canvas = this.@org.glob3.mobile.specific.Canvas_WebGL::_canvas;
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
			this.@org.glob3.mobile.specific.Canvas_WebGL::_context.font = currentDOMFont;
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
		jsImage.src = this.@org.glob3.mobile.specific.Canvas_WebGL::_canvas
				.toDataURL();
   }-*/;


   @Override
   protected native Vector2F _textExtent(final String text) /*-{
		var width = this.@org.glob3.mobile.specific.Canvas_WebGL::_context
				.measureText(text).width;

		var height = Math.round(_currentFontSize * 1.66);

		return @org.glob3.mobile.generated.Vector2F::new(FF)(width, height);
   }-*/;


   private static native String toJSColor(final Color color) /*-{
		if (color) {
			var r = Math
					.round(255 * color.@org.glob3.mobile.generated.Color::getRed()());
			var g = Math
					.round(255 * color.@org.glob3.mobile.generated.Color::getGreen()());
			var b = Math
					.round(255 * color.@org.glob3.mobile.generated.Color::getBlue()());
			var a = Math
					.round(255 * color.@org.glob3.mobile.generated.Color::getAlpha()());
			return "rgba(" + r + "," + g + "," + b + "," + a + ")";
		} else {
			return null;
		}
   }-*/;


   @Override
   protected native void _setFillColor(final Color color) /*-{
		var jsColor = this.@org.glob3.mobile.specific.Canvas_WebGL::toJSColor(Lorg/glob3/mobile/generated/Color;)(color);
		this.@org.glob3.mobile.specific.Canvas_WebGL::_context.fillStyle = jsColor;
   }-*/;


   @Override
   protected native void _setStrokeColor(final Color color) /*-{
		var jsColor = this.@org.glob3.mobile.specific.Canvas_WebGL::toJSColor(Lorg/glob3/mobile/generated/Color;)(color);
		this.@org.glob3.mobile.specific.Canvas_WebGL::_context.strokeStyle = jsColor;
   }-*/;


   @Override
   protected native void _setStrokeWidth(final float width) /*-{
		this.@org.glob3.mobile.specific.Canvas_WebGL::_context.lineWidth = width;
   }-*/;


}
