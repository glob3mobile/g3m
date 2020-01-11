

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

import com.google.gwt.core.client.*;


public final class Canvas_WebGL
         extends
            ICanvas {

   private final JavaScriptObject _domCanvas;
   private final JavaScriptObject _domCanvasContext;

   private String _currentDOMFont;
   private int    _currentFontSize;

   private int _width;
   private int _height;


   Canvas_WebGL(final boolean retina) {
      super(retina);
      _domCanvas = createCanvas();
      _domCanvasContext = getContext2D(_domCanvas);
   }


   private static native JavaScriptObject createCanvas() /*-{
		return $doc.createElement("canvas");
   }-*/;


   private static native JavaScriptObject getContext2D(final JavaScriptObject canvas) /*-{
		return canvas.getContext("2d");
   }-*/;


   @Override
   protected native void _initialize(final int width,
                                     final int height) /*-{
		var canvas = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvas;

		var isRetina = this.@org.glob3.mobile.generated.ICanvas::_retina;
		var ratio = isRetina ? ($wnd.devicePixelRatio || 1) : 1;
		var w = Math.ceil(width * ratio);
		var h = Math.ceil(height * ratio);

		this.@org.glob3.mobile.specific.Canvas_WebGL::_width = w;
		this.@org.glob3.mobile.specific.Canvas_WebGL::_height = h;

		canvas.width = w;
		canvas.height = h;

		if (ratio != 1) {
			var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
			context.scale(ratio, ratio);
		}

		this.@org.glob3.mobile.specific.Canvas_WebGL::tryToSetCurrentFontToContext()();
   }-*/;


   private static String createDOMFont(final GFont font) {
      final StringBuilder builder = new StringBuilder();

      if (font.isItalic()) {
         builder.append("italic ");

      }
      if (font.isBold()) {
         builder.append("bold ");
      }

      builder.append(Math.round(font.getSize()));
      builder.append("px ");

      if (font.isSerif()) {
         builder.append("serif");
      }
      else if (font.isSansSerif()) {
         builder.append("sans-serif");
      }
      else if (font.isMonospaced()) {
         builder.append("monospace");
      }

      return builder.toString();
   }


   private native void tryToSetCurrentFontToContext() /*-{
		var currentDOMFont = this.@org.glob3.mobile.specific.Canvas_WebGL::_currentDOMFont;
		if (currentDOMFont) {
			this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.font = currentDOMFont;
		}
   }-*/;


   @Override
   protected void _setFont(final GFont font) {
      _currentFontSize = Math.round(font.getSize());
      _currentDOMFont = createDOMFont(font);

      tryToSetCurrentFontToContext();
   }


   @Override
   protected native void _createImage(final IImageListener listener,
                                      final boolean autodelete) /*-{
		var width = this.@org.glob3.mobile.specific.Canvas_WebGL::_width;
		var height = this.@org.glob3.mobile.specific.Canvas_WebGL::_height;
		var jsImage = new Image(width, height);
		jsImage.onload = function() {
			var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsImage);
			listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
			if (autodelete) {
				listener.@org.glob3.mobile.generated.IImageListener::dispose()();
			}
		};
		var canvas = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvas;
		jsImage.src = canvas.toDataURL();
   }-*/;


   private static String createDOMColor(final Color color) {
      if (color == null) {
         return null;
      }

      final int r = Math.round(255 * color._red);
      final int g = Math.round(255 * color._green);
      final int b = Math.round(255 * color._blue);
      final float a = color._alpha;

      return "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
   }


   @Override
   protected native void _setFillColor(final Color color) /*-{
		var jsColor = @org.glob3.mobile.specific.Canvas_WebGL::createDOMColor(Lorg/glob3/mobile/generated/Color;)(color);
		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.fillStyle = jsColor;
   }-*/;


   @Override
   protected native void _setLineColor(final Color color) /*-{
		var jsColor = @org.glob3.mobile.specific.Canvas_WebGL::createDOMColor(Lorg/glob3/mobile/generated/Color;)(color);
		this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext.strokeStyle = jsColor;
   }-*/;


   @Override
   protected native void _setLineWidth(final float width) /*-{
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
   protected void _fillEllipse(final float left,
                               final float top,
                               final float width,
                               final float height) {
      drawEllipse(left, top, width, height, true, false);
   }


   @Override
   protected void _strokeEllipse(final float left,
                                 final float top,
                                 final float width,
                                 final float height) {
      drawEllipse(left, top, width, height, false, true);
   }


   @Override
   protected void _fillAndStrokeEllipse(final float left,
                                        final float top,
                                        final float width,
                                        final float height) {
      drawEllipse(left, top, width, height, true, true);
   }


   private native void drawEllipse(final float x,
                                   final float y,
                                   final float w,
                                   final float h,
                                   final boolean fill,
                                   final boolean stroke) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;

		var kappa = .5522848;
		var ox = (w / 2) * kappa; // control point offset horizontal
		var oy = (h / 2) * kappa; // control point offset vertical
		var xe = x + w; // x-end
		var ye = y + h; // y-end
		var xm = x + w / 2; // x-middle
		var ym = y + h / 2; // y-middle

		context.beginPath();
		context.moveTo(x, ym);
		context.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
		context.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
		context.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		context.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
		if (fill) {
			context.fill();
		}
		if (stroke) {
			context.stroke();
		}
   }-*/;


   @Override
   protected native void _fillText(final String text,
                                   final float left,
                                   final float top) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.textBaseline = "top";
		context.fillText(text, left, top - 1);
   }-*/;


   @Override
   protected native Vector2F _textExtent(final String text) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.textBaseline = "top";
		var width = Math.ceil(context.measureText(text).width);
		var height = this.@org.glob3.mobile.specific.Canvas_WebGL::_currentFontSize;
		return @org.glob3.mobile.generated.Vector2F::new(FF)(width, height);
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
   protected native void _clearRect(final float left,
                                    final float top,
                                    final float width,
                                    final float height) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;

		context.clearRect(left, top, width, height);
   }-*/;


   @Override
   protected native void _drawImage(final IImage image,
                                    final float destLeft,
                                    final float destTop) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		context.drawImage(imageJS, destLeft, destTop, imageJS.width,
				imageJS.height);
   }-*/;


   @Override
   protected native void _drawImage(final IImage image,
                                    final float destLeft,
                                    final float destTop,
                                    final float transparency) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		var currentGlobalAlpha = context.globalAlpha;
		context.globalAlpha = transparency;
		context.drawImage(imageJS, destLeft, destTop, imageJS.width,
				imageJS.height);
		context.globalAlpha = currentGlobalAlpha;
   }-*/;


   @Override
   protected native void _drawImage(IImage image,
                                    float left,
                                    float top,
                                    float width,
                                    float height) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		context.drawImage(imageJS, left, top, width, height);
   }-*/;


   @Override
   protected native void _drawImage(IImage image,
                                    float left,
                                    float top,
                                    float width,
                                    float height,
                                    final float transparency) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		var currentGlobalAlpha = context.globalAlpha;
		context.globalAlpha = transparency;
		context.drawImage(imageJS, left, top, width, height);
		context.globalAlpha = currentGlobalAlpha;
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


   @Override
   protected native void _drawImage(final IImage image,
                                    final float srcLeft,
                                    final float srcTop,
                                    final float srcWidth,
                                    final float srcHeight,
                                    final float destLeft,
                                    final float destTop,
                                    final float destWidth,
                                    final float destHeight,
                                    final float transparency) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		var imageJS = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();

		var currentGlobalAlpha = context.globalAlpha;
		context.globalAlpha = transparency;
		context.drawImage(imageJS, srcLeft, srcTop, srcWidth, srcHeight,
				destLeft, destTop, destWidth, destHeight);
		context.globalAlpha = currentGlobalAlpha;
   }-*/;


   @Override
   protected native void _beginPath() /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.beginPath();
   }-*/;


   @Override
   protected native void _stroke() /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.stroke();
   }-*/;


   @Override
   protected native void _moveTo(final float x,
                                 final float y) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.moveTo(x, y);
   }-*/;


   @Override
   protected native void _lineTo(final float x,
                                 final float y) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.lineTo(x, y);
   }-*/;


   @Override
   protected void _setLineCap(final StrokeCap cap) {
      switch (cap) {
         case CAP_BUTT:
            jsLineCap("butt");
            break;
         case CAP_ROUND:
            jsLineCap("round");
            break;
         case CAP_SQUARE:
            jsLineCap("square");
            break;
      }
   }


   private native void jsLineCap(final String cap) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.lineCap = cap;
   }-*/;


   @Override
   protected void _setLineJoin(final StrokeJoin join) {
      switch (join) {
         case JOIN_MITER:
            jsLineJoin("miter");
            break;
         case JOIN_ROUND:
            jsLineJoin("round");
            break;
         case JOIN_BEVEL:
            jsLineJoin("bevel");
            break;
      }
   }


   private native void jsLineJoin(final String join) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.lineJoin = join;
   }-*/;


   @Override
   protected native void _setLineMiterLimit(final float limit) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.miterLimit = limit;
   }-*/;


   @Override
   protected void _setLineDash(final float[] lengths,
                               final int count,
                               final float phase) {
      final JsArrayNumber jsArray = (JsArrayNumber) JavaScriptObject.createArray();
      for (int i = 0; i < count; i++) {
         jsArray.push(lengths[i]);
      }
      jsSetLineDash(jsArray, phase);
   }


   private native void jsSetLineDash(final JsArrayNumber lengths,
                                     final float phase) /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.setLineDash(lengths);
		context.lineDashOffset = phase;
   }-*/;


   @Override
   protected native void _closePath() /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.closePath();
   }-*/;


   @Override
   protected native void _fill() /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.fill();
   }-*/;


   @Override
   protected native void _fillAndStroke() /*-{
		var context = this.@org.glob3.mobile.specific.Canvas_WebGL::_domCanvasContext;
		context.fill();
		context.stroke();
   }-*/;


}
