

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ITextUtils;

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
               floatTo255(g3mColor.getRed()), //
               floatTo255(g3mColor.getGreen()), //
               floatTo255(g3mColor.getBlue()));
   }


   @Override
   public IImage createLabelBitmap(final String label,
                                   final float fontSize,
                                   final org.glob3.mobile.generated.Color color,
                                   final org.glob3.mobile.generated.Color shadowColor) {
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
      // canvas.drawARGB(255, 0, 255, 0); // for visualization

      canvas.drawText(label, 0, height - textBounds.bottom - 2, paint);

      return new Image_Android(bitmap, null);
   }

}
