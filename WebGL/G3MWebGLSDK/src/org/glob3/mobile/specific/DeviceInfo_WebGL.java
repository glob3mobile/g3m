
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;

public class DeviceInfo_WebGL extends IDeviceInfo {

   @Override
   public float getDPI() {
      return 96 * getDevicePixelRatio();
   }

   @Override
   public DeviceInfo_Platform getPlatform() {
      return DeviceInfo_Platform.DEVICE_GWT;
   }

   @Override
   public native float getDevicePixelRatio() /*-{
    return $wnd.devicePixelRatio || 1;
   }-*/;

   // CSS pixels of env(safe-area-inset-<side>), read through a hidden probe; 0 where the browser has
   // no safe area (desktop) or the page lacks viewport-fit=cover
   private native float getSafeAreaInsetCSSPixels(final String side) /*-{
    var probe = $doc.createElement("div");
    probe.style.position = "fixed";
    probe.style.visibility = "hidden";
    probe.style.paddingTop = "env(safe-area-inset-" + side + ")";
    $doc.body.appendChild(probe);
    var value = parseFloat($wnd.getComputedStyle(probe).paddingTop);
    $doc.body.removeChild(probe);
    return isNaN(value) ? 0 : value;
   }-*/;

   @Override
   public float getSafeAreaInsetTop() {
      return getSafeAreaInsetCSSPixels("top") * getDevicePixelRatio();
   }

   @Override
   public float getSafeAreaInsetRight() {
      return getSafeAreaInsetCSSPixels("right") * getDevicePixelRatio();
   }

   @Override
   public float getSafeAreaInsetBottom() {
      return getSafeAreaInsetCSSPixels("bottom") * getDevicePixelRatio();
   }

   @Override
   public float getSafeAreaInsetLeft() {
      return getSafeAreaInsetCSSPixels("left") * getDevicePixelRatio();
   }

}
