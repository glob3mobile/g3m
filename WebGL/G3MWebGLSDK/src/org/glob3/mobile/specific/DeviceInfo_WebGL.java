

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.DeviceInfo_Platform;
import org.glob3.mobile.generated.IDeviceInfo;


public class DeviceInfo_WebGL
         extends
            IDeviceInfo {

   @Override
   public float getDPI() {
      return 96;
   }


   @Override
   public DeviceInfo_Platform getPlatform() {
      return DeviceInfo_Platform.DEVICE_GWT;
   }


   @Override
   public float getQualityFactor() {
      return 1;
   }

}
