

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IDeviceInfo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class DeviceInfo_Android
         extends
            IDeviceInfo {

   private final Context _context;
   private float         _dpi = -1;


   public DeviceInfo_Android(final Context context) {
      _context = context;
   }


   @Override
   public float getDPI() {
      if (_dpi < 0) {
         final DisplayMetrics metrics = new DisplayMetrics();
         final WindowManager windowManager = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
         windowManager.getDefaultDisplay().getMetrics(metrics);
         _dpi = (metrics.xdpi + metrics.ydpi) / 2;
      }
      return _dpi;
   }

}
