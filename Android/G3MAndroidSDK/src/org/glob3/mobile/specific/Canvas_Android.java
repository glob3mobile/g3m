

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.GFont;
import org.glob3.mobile.generated.ICanvas;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.Vector2F;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;


public final class Canvas_Android
   extends
      ICanvas {

   private Bitmap      _bitmap          = null;
   private Canvas      _canvas          = null;
   private final Paint _fillPaint;
   private final Paint _strokePaint;
   private Typeface    _currentTypeface = null;

   private Path        _path            = null;

   private final RectF _rectF           = new RectF(); // RectF instance for reuse (and avoid garbage)
   private final Rect  _rect            = new Rect(); // Rect instance for reuse (and avoid garbage)


   Canvas_Android() {
      _fillPaint = new Paint();
      _fillPaint.setAntiAlias(true);
      _fillPaint.setStyle(Paint.Style.FILL);

      _strokePaint = new Paint();
      _strokePaint.setAntiAlias(true);
      _strokePaint.setStyle(Paint.Style.STROKE);
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
      if (_bitmap != null) {
         _bitmap.recycle();
         _bitmap = null;
      }
      _canvas = null;
      super.dispose();
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
               floatTo255(g3mColor._alpha), //
               floatTo255(g3mColor._red), //
               floatTo255(g3mColor._green), //
               floatTo255(g3mColor._blue));
   }


   @Override
   protected void _setFillColor(final org.glob3.mobile.generated.Color color) {
      _fillPaint.setColor(toAndroidColor(color));
   }


   @Override
   protected void _setLineColor(final org.glob3.mobile.generated.Color color) {
      _strokePaint.setColor(toAndroidColor(color));
   }


   @Override
   protected void _setLineWidth(final float width) {
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
   protected void _clearRect(final float left,
                             final float top,
                             final float width,
                             final float height) {

      final Paint clearPaint = new Paint();
      clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
      _canvas.drawRect(left, top, width, height, clearPaint);
      //_canvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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
   protected void _fillEllipse(final float left,
                               final float top,
                               final float width,
                               final float height) {
      _rectF.set(left, top, left + width, top + height);
      _canvas.drawOval(_rectF, _fillPaint);
   }


   @Override
   protected void _strokeEllipse(final float left,
                                 final float top,
                                 final float width,
                                 final float height) {
      _rectF.set(left, top, left + width, top + height);
      _canvas.drawOval(_rectF, _strokePaint);
   }


   @Override
   protected void _fillAndStrokeEllipse(final float left,
                                        final float top,
                                        final float width,
                                        final float height) {
      _fillEllipse(left, top, width, height);
      _strokeEllipse(left, top, width, height);
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
      final Rect textBounds = _rect;
      _fillPaint.getTextBounds(text, 0, text.length(), textBounds);
      _canvas.drawText(text, left, top - textBounds.top, _fillPaint);
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float destLeft,
                             final float destTop) {
      _drawImage(image, destLeft, destTop, image.getWidth(), image.getHeight());
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float destLeft,
                             final float destTop,
                             final float transparency) {
      _drawImage(image, destLeft, destTop, image.getWidth(), image.getHeight(), transparency);
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float left,
                             final float top,
                             final float width,
                             final float height) {
      final Bitmap bitmap = ((Image_Android) image).getBitmap();

      final RectF dst = _rectF;
      dst.set(left, //
               top, //
               left + width, // Right
               top + height); // Bottom

      _canvas.drawBitmap(bitmap, null, dst, null);
   }


   @Override
   protected void _drawImage(final IImage image,
                             final float left,
                             final float top,
                             final float width,
                             final float height,
                             final float transparency) {
      final Bitmap bitmap = ((Image_Android) image).getBitmap();

      final RectF dst = _rectF;
      dst.set(left, //
               top, //
               left + width, // Right
               top + height); // Bottom

      final Paint paint = new Paint();
      paint.setAlpha(Math.round(255 * transparency));
      _canvas.drawBitmap(bitmap, null, dst, paint);
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
      final Bitmap bitmap = ((Image_Android) image).getBitmap();

      final RectF dst = _rectF;
      dst.set(destLeft, //
               destTop, //
               destLeft + destWidth, // Right
               destTop + destHeight); // Bottom

      final Rect src = _rect;
      src.set(Math.round(srcLeft), //
               Math.round(srcTop), //
               Math.round(srcLeft + srcWidth), // Right
               Math.round(srcTop + srcHeight)); // Bottom

      _canvas.drawBitmap(bitmap, src, dst, null);
   }


   @Override
   protected void _setLineCap(final StrokeCap cap) {
      switch (cap) {
         case CAP_BUTT:
            _strokePaint.setStrokeCap(Paint.Cap.BUTT);
            break;
         case CAP_ROUND:
            _strokePaint.setStrokeCap(Paint.Cap.ROUND);
            break;
         case CAP_SQUARE:
            _strokePaint.setStrokeCap(Paint.Cap.SQUARE);
            break;
      }
   }


   @Override
   protected void _setLineJoin(final StrokeJoin join) {
      switch (join) {
         case JOIN_MITER:
            _strokePaint.setStrokeJoin(Paint.Join.MITER);
            break;
         case JOIN_ROUND:
            _strokePaint.setStrokeJoin(Paint.Join.ROUND);
            break;
         case JOIN_BEVEL:
            _strokePaint.setStrokeJoin(Paint.Join.BEVEL);
            break;
      }
   }


   @Override
   protected void _setLineMiterLimit(final float limit) {
      _strokePaint.setStrokeMiter(limit);
   }


   @Override
   protected void _setLineDash(final float[] lengths,
                               final int count,
                               final float phase) {
      if ((count == 0) || (lengths.length == 0)) {
         _strokePaint.setPathEffect(null);
      }
      else {
         _strokePaint.setPathEffect(new DashPathEffect(lengths, phase));
      }
   }


   @Override
   protected void _beginPath() {
      if (_path == null) {
         _path = new Path();
         _path.setFillType(Path.FillType.EVEN_ODD);
      }
      else {
         _path.reset();
      }
   }


   @Override
   protected void _closePath() {
      _path.close();
   }


   @Override
   protected void _stroke() {
      _canvas.drawPath(_path, _strokePaint);
   }


   @Override
   protected void _fill() {
      _canvas.drawPath(_path, _fillPaint);
   }


   @Override
   protected void _fillAndStroke() {
      _canvas.drawPath(_path, _fillPaint);
      _canvas.drawPath(_path, _strokePaint);
   }


   @Override
   protected void _moveTo(final float x,
                          final float y) {
      _path.moveTo(x, y);
   }


   @Override
   protected void _lineTo(final float x,
                          final float y) {
      _path.lineTo(x, y);
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
                             final float destHeight,
                             final float transparency) {

      final Bitmap bitmap = ((Image_Android) image).getBitmap();

      final RectF dst = _rectF;
      dst.set(destLeft, //
               destTop, //
               destLeft + destWidth, // Right
               destTop + destHeight); // Bottom

      final Rect src = _rect;
      src.set(Math.round(srcLeft), //
               Math.round(srcTop), //
               Math.round(srcLeft + srcWidth), // Right
               Math.round(srcTop + srcHeight)); // Bottom

      final Paint paint = new Paint();
      paint.setAlpha(Math.round(255 * transparency));

      _canvas.drawBitmap(bitmap, src, dst, paint);
   }


}
