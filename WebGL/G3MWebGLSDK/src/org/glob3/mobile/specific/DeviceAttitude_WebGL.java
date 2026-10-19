
package org.glob3.mobile.specific;

import org.glob3.mobile.generated.*;
import com.google.gwt.core.client.*;

public class DeviceAttitude_WebGL extends IDeviceAttitude {

   private InterfaceOrientation _currentIO = null;

   private double _beta  = Double.NaN;
   private double _gamma = Double.NaN;
   private double _alpha = Double.NaN;

   private boolean _isTracking;

   private JavaScriptObject _deviceOrientationListener = null;

   //Implementation of DeviceAttitude_WebGL may be inconsistent with device natural screen orientation
   //Further development needed
   public DeviceAttitude_WebGL() {
      trackInterfaceOrientation(this);
      initInterfaceOrientation(this);
   }

   private static native void trackGyroscope(DeviceAttitude_WebGL devAtt) /*-{
    try {
      var listener = function(event) {
        devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_beta = event.beta;
        devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_gamma = event.gamma;
        devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_alpha = event.alpha;

        // Chrome - Safari points alpha to the first position instead
        // of true north.
        //								try{
        //									if ((navigator.userAgent.indexOf("Chrome") != -1 ) || (navigator.userAgent.indexOf("Safari") != -1 )){
        //										devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_alpha = event.webkitCompassHeading;
        //									}
        //								} catch(err){
        //								}

      };
      // removeEventListener needs the same function object
      devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_deviceOrientationListener = listener;
      $wnd.addEventListener('deviceorientation', listener, false);
    } catch (err) {
    }

   }-*/;

   private static native void stopTrackingGyroscope(DeviceAttitude_WebGL devAtt) /*-{
    try {
      $wnd.removeEventListener('deviceorientation', devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_deviceOrientationListener, false);
      devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::_deviceOrientationListener = null;
    } catch (err) {
    }
   }-*/;

   private void storeInterfaceOrientation(final String orientation) {
      if (orientation.equalsIgnoreCase("portrait-primary")) {
         _currentIO = InterfaceOrientation.PORTRAIT;
      }
      else if (orientation.equalsIgnoreCase("portrait-secondary")) {
         _currentIO = InterfaceOrientation.PORTRAIT_UPSIDEDOWN;
      }
      else if (orientation.equalsIgnoreCase("landscape-primary")) {
         _currentIO = InterfaceOrientation.LANDSCAPE_RIGHT;
      }
      else if (orientation.equalsIgnoreCase("landscape-secondary")) {
         _currentIO = InterfaceOrientation.LANDSCAPE_LEFT;
      }

      // ILogger.instance().logInfo("SIO " + orientation + " -> " + _currentIO.toString());
   }

   private native void initInterfaceOrientation(DeviceAttitude_WebGL devAtt) /*-{
    try {
      if ($wnd.screen.orientation !== undefined) { //CHROME, SAFARI
        devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
      } else if ($wnd.screen.mozOrientation !== undefined) { //MOZILLA
        devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.mozOrientation);
      }
    } catch (err) {
      console.error("Unable to track Interface Orientation. " + err);
    }
   }-*/;

   private native void trackInterfaceOrientation(DeviceAttitude_WebGL devAtt) /*-{

    try {
      if ($wnd.screen.orientation !== undefined) { //CHROME, SAFARI
        $wnd.screen.orientation.onchange = function() {
          devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
        };
      } else if ($wnd.screen.mozOrientation !== undefined) { //MOZILLA
        $wnd.screen.onmozorientationchange = function(event) {
          event.preventDefault();
          devAtt.@org.glob3.mobile.specific.DeviceAttitude_WebGL::storeInterfaceOrientation(Ljava/lang/String;)($wnd.screen.orientation.type);
        }
      }
    } catch (err) {
      console.error("Unable to track Interface Orientation. " + err);
    }

   }-*/;

   @Override
   public void startTrackingDeviceOrientation() {
      trackGyroscope(this);
      _isTracking = true;
   }

   @Override
   public void stopTrackingDeviceOrientation() {
      stopTrackingGyroscope(this);
      _beta = _gamma = _alpha = Double.NaN;

      _isTracking = false;
   }

   @Override
   public boolean isTracking() {
      return _isTracking;
   }

   private Angle getHeading() {
      return Angle.fromDegrees(_alpha);
   }

   private Angle getPitch() {
      return Angle.fromDegrees(_gamma);
   }

   private Angle getRoll() {
      return Angle.fromDegrees(-_beta);
   }

   @Override
   public void copyValueOfRotationMatrix(final MutableMatrix44D rotationMatrix) {
      if (Double.isNaN(_beta) || Double.isNaN(_gamma) || Double.isNaN(_alpha)) {
         rotationMatrix.setValid(false);
         ILogger.instance().logError("Browser attitude data is undefined.");
      }
      else {
         final CoordinateSystem cs = CoordinateSystem.global().applyTaitBryanAngles(getHeading(), getPitch(), getRoll());
         rotationMatrix.copyValue(cs.getRotationMatrix());
      }
   }

   @Override
   public InterfaceOrientation getCurrentInterfaceOrientation() {
      return _currentIO;
   }

}
