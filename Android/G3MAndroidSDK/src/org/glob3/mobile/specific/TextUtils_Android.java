

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.ITextUtils;
import org.glob3.mobile.generated.LabelPosition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public class TextUtils_Android
         extends
            ITextUtils {

   private static int floatTo255(final float value) {
      return Math.round(value * 255);
   }


   private static int toAndroidColor(final org.glob3.mobile.generated.Color g3mColor) {
      return android.graphics.Color.rgb( //
               floatTo255(g3mColor._red), //
               floatTo255(g3mColor._green), //
               floatTo255(g3mColor._blue));
   }


   //   @Override
   //   public IImage createLabelBitmap(final String label,
   //                                   final float fontSize,
   //                                   final org.glob3.mobile.generated.Color color,
   //                                   final org.glob3.mobile.generated.Color shadowColor) {
   //      //final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
   //      final Paint paint = new Paint();
   //      paint.setAntiAlias(true);
   //      paint.setColor(toAndroidColor(color));
   //      paint.setTextSize((fontSize * 3) / 2);
   //      if (shadowColor != null) {
   //         paint.setShadowLayer(1f, 2f, 2f, toAndroidColor(shadowColor));
   //      }
   //
   //      final Rect textBounds = new Rect();
   //      paint.getTextBounds(label, 0, label.length(), textBounds);
   //      int width = textBounds.width();
   //      int height = textBounds.height();
   //      if (shadowColor != null) {
   //         width += 2;
   //         height += 2;
   //      }
   //
   //      final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
   //      final Canvas canvas = new Canvas(bitmap);
   //      // canvas.drawARGB(255, 0, 255, 0); // for visualization
   //
   //      canvas.drawText(label, 0, height - textBounds.bottom - 2, paint);
   //
   //      return new Image_Android(bitmap, null);
   //   }


   @Override
   public void createLabelImage(final String label,
                                final float fontSize,
                                final Color color,
                                final Color shadowColor,
                                final IImageListener listener,
                                final boolean autodelete) {
      //final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      final Paint paint = new Paint();
      paint.setAntiAlias(true);
      paint.setColor(toAndroidColor(color));
      paint.setTextSize((fontSize * 3) / 2);
      if (shadowColor != null) {
         paint.setShadowLayer(1f, 2f, 2f, toAndroidColor(shadowColor));
      }

      final Rect textBounds = new Rect();
      paint.getTextBounds(label, 0, label.length(), textBounds);
      int width = textBounds.width();
      int height = textBounds.height();
      if (shadowColor != null) {
         width += 2;
         height += 2;
      }

      final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      final Canvas canvas = new Canvas(bitmap);
      //canvas.drawARGB(255, 0, 255, 0); // for visualization

      canvas.drawText(label, 0, -textBounds.top, paint);

      final Image_Android result = new Image_Android(bitmap, null);
      listener.imageCreated(result);
   }


   @Override
   public void labelImage(final IImage image,
                          final String label,
                          final LabelPosition labelPosition,
                          final int separation,
                          final float fontSize,
                          final Color color,
                          final Color shadowColor,
                          final IImageListener listener,
                          final boolean autodelete) {

      if (image == null) {
         createLabelImage(label, fontSize, color, shadowColor, listener, autodelete);
         return;
      }

      final Paint paint = new Paint();
      paint.setAntiAlias(true);
      paint.setTextSize((fontSize * 3) / 2);

      final Rect textBounds = new Rect();
      paint.getTextBounds(label, 0, label.length(), textBounds);
      int labelWidth = textBounds.width();
      int labelHeight = textBounds.height();
      if (shadowColor != null) {
         labelWidth += 2;
         labelHeight += 2;
      }

      final int imageWidth;
      final int imageHeight;
      if (labelPosition == LabelPosition.Bottom) {
         imageWidth = Math.max(labelWidth, image.getWidth());
         imageHeight = labelHeight + separation + image.getHeight();
      }
      else if (labelPosition == LabelPosition.Right) {
         imageWidth = labelWidth + separation + image.getWidth();
         imageHeight = Math.max(labelHeight, image.getHeight());
      }
      else {
         ILogger.instance().logError("Unsupported LabelPosition");
         listener.imageCreated(null);
         return;
      }

      final Bitmap bitmap = Bitmap.createBitmap(imageWidth + 2, imageHeight + 2, Bitmap.Config.ARGB_8888);
      final Canvas canvas = new Canvas(bitmap);
      //canvas.drawARGB(255, 0, 255, 0); // for visualization

      final Bitmap androidBitmap = ((Image_Android) image).getBitmap();
      if (labelPosition == LabelPosition.Bottom) {
         canvas.drawBitmap( //
                  androidBitmap, //
                  (imageWidth - image.getWidth()) / 2, //
                  0, //
                  paint);
      }
      else if (labelPosition == LabelPosition.Right) {
         canvas.drawBitmap( //
                  androidBitmap, //
                  0, //
                  (imageHeight - image.getHeight()) / 2, //
                  paint);
      }

      paint.setColor(toAndroidColor(color));
      if (shadowColor != null) {
         paint.setShadowLayer(1f, 2f, 2f, toAndroidColor(shadowColor));
      }

      if (labelPosition == LabelPosition.Bottom) {
         canvas.drawText( //
                  label, //
                  (imageWidth - labelWidth) / 2, //
                  (image.getHeight() + separation) - textBounds.top, //
                  paint);
      }
      else if (labelPosition == LabelPosition.Right) {
         canvas.drawText( //
                  label, //
                  image.getWidth() + separation, //
                  ((imageHeight - labelHeight) / 2) - textBounds.top, //
                  paint);
      }

      final Image_Android result = new Image_Android(bitmap, null);
      listener.imageCreated(result);
   }
}
