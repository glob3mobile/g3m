package org.glob3.mobile.specific;

import com.google.gwt.core.client.*;

public class WebGLContextAttributes {
   //   dictionary WebGLContextAttributes
   //   {
   //      Boolean              alpha                        = true;
   //      Boolean              depth                        = true;
   //      Boolean              stencil                      = false;
   //      Boolean              antialias                    = true;
   //      Boolean              premultipliedAlpha           = true;
   //      Boolean              preserveDrawingBuffer        = false;
   //      WebGLPowerPreference powerPreference              = "default";
   //      Boolean              failIfMajorPerformanceCaveat = false;
   //      Boolean              desynchronized               = false;
   //   }

   public enum PowerPreference {
      DEFAULT("default"),
      HIGH_PERFORMANCE("high-performance"),
      LOW_POWER("low-power");

      public final String _value;

      PowerPreference(final String value) {
         _value = value;
      }
   }

   public final Boolean         _alpha;
   public final Boolean         _depth;
   public final Boolean         _stencil;
   public final Boolean         _antialias;
   public final Boolean         _premultipliedAlpha;
   public final Boolean         _preserveDrawingBuffer;
   public final PowerPreference _powerPreference;
   public final Boolean         _failIfMajorPerformanceCaveat;
   public final Boolean         _desynchronized;

   //   public WebGLContextAttributes() {
   //      this(null, null, null, null, null, null, null, null, null);
   //   }

   public WebGLContextAttributes(final Boolean alpha, //
                                 final Boolean depth, //
                                 final Boolean stencil, //
                                 final Boolean antialias, //
                                 final Boolean premultipliedAlpha, //
                                 final Boolean preserveDrawingBuffer, //
                                 final PowerPreference powerPreference, //
                                 final Boolean failIfMajorPerformanceCaveat, //
                                 final Boolean desynchronized) {
      _alpha                        = alpha;
      _depth                        = depth;
      _stencil                      = stencil;
      _antialias                    = antialias;
      _premultipliedAlpha           = premultipliedAlpha;
      _preserveDrawingBuffer        = preserveDrawingBuffer;
      _powerPreference              = powerPreference;
      _failIfMajorPerformanceCaveat = failIfMajorPerformanceCaveat;
      _desynchronized               = desynchronized;
   }

   public native JavaScriptObject toJS() /*-{
    var result = {};

    function setBool(key, boolObj) {
      if (boolObj != null) {
        result[key] = boolObj.@java.lang.Boolean::booleanValue()();
      }
    }

    setBool('alpha', this.@org.glob3.mobile.specific.WebGLContextAttributes::_alpha);
    setBool('depth', this.@org.glob3.mobile.specific.WebGLContextAttributes::_depth);
    setBool('stencil', this.@org.glob3.mobile.specific.WebGLContextAttributes::_stencil);
    setBool('antialias', this.@org.glob3.mobile.specific.WebGLContextAttributes::_antialias);
    setBool('premultipliedAlpha', this.@org.glob3.mobile.specific.WebGLContextAttributes::_premultipliedAlpha);
    setBool('preserveDrawingBuffer', this.@org.glob3.mobile.specific.WebGLContextAttributes::_preserveDrawingBuffer);
    setBool('failIfMajorPerformanceCaveat', this.@org.glob3.mobile.specific.WebGLContextAttributes::_failIfMajorPerformanceCaveat);
    setBool('desynchronized', this.@org.glob3.mobile.specific.WebGLContextAttributes::_desynchronized);

    var powerPref = this.@org.glob3.mobile.specific.WebGLContextAttributes::_powerPreference;
    if (powerPref != null) {
      result['powerPreference'] = powerPref.@org.glob3.mobile.specific.WebGLContextAttributes$PowerPreference::_value;
    }

    return result;
   }-*/;

}
