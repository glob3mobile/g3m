
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.DeviceInfo_Platform;
import org.glob3.mobile.generated.IDeviceInfo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceInfo_Android extends IDeviceInfo {

   private final float _dpi;

   DeviceInfo_Android(final Context context) {
      _dpi = calculateDPI(context);
   }

   private static float calculateDPI(final Context context) {
      final WindowManager  windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      final DisplayMetrics metrics       = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(metrics);

      return (metrics.xdpi + metrics.ydpi) / 2.0f;
   }

   @Override
   public DeviceInfo_Platform getPlatform() {
      return DeviceInfo_Platform.DEVICE_Android;
   }

   @Override
   public float getDPI() {
      return _dpi;
   }

   @Override
   public float getDevicePixelRatio() {
      return _dpi / 150.0f;
   }

}
