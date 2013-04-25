

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.Vector2F;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;


public class Canvas_Android
         extends
            ICanvas {

   private Bitmap      _bitmap          = null;
   private Canvas      _canvas          = null;
   private final Paint _fillPaint;
   private final Paint _strokePaint;
   private Typeface    _currentTypeface = null;

   private final RectF _rectF           = new RectF(); // RectF instance for reuse (and avoid garbage)
   private final Rect  _rect            = new Rect(); // Rect instance for reuse (and avoid garbage)


   Canvas_Android() {
      _fillPaint = new Paint();
      _fillPaint.setAntiAlias(true);
      _fillPaint.setStyle(Paint.Style.FILL);

      _strokePaint = new Paint();
      _strokePaint.setAntiAlias(true);
      _strokePaint.setStyle(Paint.Style.STROKE);
      // _strokePaint.setARGB(0, 0, 0, 0);
   }


   @Override
   protected void _initialize(final int width,
                              final int height) {
      _bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      _canvas = new Canvas(_bitmap);
   }


   @Override
   protected void _setFont(final GFont font) {
      _currentTypeface = createTypeface(font);

      _fillPaint.setTypeface(_currentTypeface);
      _fillPaint.setTextSize(font.getSize());

      _strokePaint.setTypeface(_currentTypeface);
      _strokePaint.setTextSize(font.getSize());
   }


   private static Typeface createTypeface(final GFont font) {
      final Typeface fontFamily;
      if (font.isSansSerif()) {
         fontFamily = Typeface.SANS_SERIF;
      }
      else if (font.isSerif()) {
         fontFamily = Typeface.SERIF;
      }
      else if (font.isMonospaced()) {
         fontFamily = Typeface.MONOSPACE;
      }
      else {
         throw new RuntimeException("Unsupported Font type");
      }

      int style = Typeface.NORMAL;
      if (font.isBold()) {
         style |= Typeface.BOLD;
      }
      if (font.isItalic()) {
         style |= Typeface.ITALIC;
      }

      return Typeface.create(fontFamily, style);
   }


   @Override
   public void dispose() {
      super.dispose();
      if (_bitmap != null) {
         _bitmap.recycle();
         _bitmap = null;
      }
      _canvas = null;
   }


   @Override
   protected void _createImage(final IImageListener listener,
                               final boolean autodelete) {
      final Bitmap bitmap = Bitmap.createBitmap(_bitmap);
      final Image_Android result = new Image_Android(bitmap, null);
      listener.imageCreated(result);
      if (autodelete) {
         listener.dispose();
      }
   }


   @Override
   protected Vector2F _textExtent(final String text) {
      //final Rect textBounds = new Rect();
      final Rect textBounds = _rect;
      _fillPaint.getTextBounds(text, 0, text.length(), textBounds);
      final int width = textBounds.width();
      final int height = textBounds.height();
      return new Vector2F(width, height);
   }


   private static int floatTo255(final float value) {
      return Math.round(value * 255);
   }


   private static int toAndroidColor(final org.glob3.mobile.generated.Color g3mColor) {
      return android.graphics.Color.argb( //
               floatTo255(g3mColor.getAlpha()), //
               floatTo255(g3mColor.getRed()), //
               floatTo255(g3mColor.getGreen()), //
               floatTo255(g3mColor.getBlue()));
   }


   @Override
   protected void _setFillColor(final org.glob3.mobile.generated.Color color) {
      _fillPaint.setColor(toAndroidColor(color));
   }


   @Override
   protected void _setStrokeColor(final org.glob3.mobile.generated.Color color) {
      _strokePaint.setColor(toAndroidColor(color));
   }


   @Override
   protected void _setStrokeWidth(final float width) {
      _strokePaint.setStrokeWidth(width);
   }


   @Override
   protected void _setShadow(final org.glob3.mobile.generated.Color color,
                             final float blur,
                             final float offsetX,
                             final float offsetY) {
      _fillPaint.setShadowLayer(blur, offsetX, offsetY, toAndroidColor(color));
      _strokePaint.setShadowLayer(blur, offsetX, offsetY, toAndroidColor(color));
   }


   @Override
   protected void _removeShadow() {
      _fillPaint.clearShadowLayer();
      _strokePaint.clearShadowLayer();
   }


   @Override
   protected void _fillRectangle(final float left,
                                 final float top,
                                 final float width,
                                 final float height) {
      _canvas.drawRect(left, top, left + width, top + height, _fillPaint);
   }


   @Override
   protected void _strokeRectangle(final float left,
                                   final float top,
                                   final float width,
                                   final float height) {
      _canvas.drawRect(left, top, left + width, top + height, _strokePaint);
   }


   @Override
   protected void _fillAndStrokeRectangle(final float left,
                                          final float top,
                                          final float width,
                                          final float height) {
      _fillRectangle(left, top, width, height);
      _strokeRectangle(left, top, width, height);
   }


   @Override
   protected void _fillRoundedRectangle(final float left,
                                        final float top,
                                        final float width,
                                        final float height,
                                        final float radius) {
      _rectF.set(left, top, left + width, top + height);
      _canvas.drawRoundRect(_rectF, radius, radius, _fillPaint);
   }


   @Override
   protected void _strokeRoundedRectangle(final float left,
                                          final float top,
                                          final float width,
                                          final float height,
                                          final float radius) {
      _rectF.set(left, top, left + width, top + height);
      _canvas.drawRoundRect(_rectF, radius, radius, _strokePaint);
   }


   @Override
   protected void _fillAndStrokeRoundedRectangle(final float left,
                                                 final float top,
                                                 final float width,
                                                 final float height,
                                                 final float radius) {
      _rectF.set(left, top, left + width, top + height);
      _canvas.drawRoundRect(_rectF, radius, radius, _fillPaint);
      _canvas.drawRoundRect(_rectF, radius, radius, _strokePaint);
   }


   @Override
   protected void _fillText(final String text,
                            final float left,
                            final float top) {
      _canvas.drawText(text, left, top, _fillPaint);
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float left,
                             final float top) {
      final Bitmap bitmap = ((Image_Android) image).getBitmap();
      _canvas.drawBitmap(bitmap, left, top, null);
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float left,
                             final float top,
                             final float width,
                             final float height) {
      final Bitmap bitmap = ((Image_Android) image).getBitmap();

      //      final RectF dst = new RectF( //
      //               left, //
      //               top, //
      //               left + width, // Right
      //               top + height); // Bottom

      final RectF dst = _rectF;
      dst.set(left, //
               top, //
               left + width, // Right
               top + height); // Bottom

      _canvas.drawBitmap(bitmap, null, dst, null);
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float srcLeft,
                             final float srcTop,
                             final float srcWidth,
                             final float srcHeight,
                             final float destLeft,
                             final float destTop,
                             final float destWidth,
                             final float destHeight) {
      //ILogger.instance().logError("RECT: %f, %f, %f, %f - %f, %f, %f, %f ", srcLeft, srcTop, srcWidth, srcHeight, destLeft, destTop, destWidth, destHeight);
      final Bitmap bitmap = ((Image_Android) image).getBitmap();

      //      final RectF dst = new RectF( //
      //               destLeft, //
      //               destTop, //
      //               destLeft + destWidth, // Right
      //               destTop + destHeight); // Bottom

      final RectF dst = _rectF;
      dst.set(destLeft, //
               destTop, //
               destLeft + destWidth, // Right
               destTop + destHeight); // Bottom

      //      final Rect src = new Rect( //
      //               Math.round(srcLeft), //
      //               Math.round(srcTop), //
      //               Math.round(srcLeft + srcWidth), // Right
      //               Math.round(srcTop + srcHeight)); // Bottom

      final Rect src = _rect;
      src.set(Math.round(srcLeft), //
               Math.round(srcTop), //
               Math.round(srcLeft + srcWidth), // Right
               Math.round(srcTop + srcHeight)); // Bottom

      _canvas.drawBitmap(bitmap, src, dst, null);
   }


}
