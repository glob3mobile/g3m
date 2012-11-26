

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IG3MBuilder;

import android.content.Context;


public class G3MBuilder_Android
         extends
            IG3MBuilder {

   private final G3MWidget_Android _nativeWidget;


   public G3MBuilder_Android(final Context context) {
      super();

      _nativeWidget = new G3MWidget_Android(context);
   }


   public G3MWidget_Android createWidget() {
      final NativeGL2_Android nativeGL = new NativeGL2_Android();
      setNativeGL(nativeGL);

      _nativeWidget.setWidget(create());

      return _nativeWidget;
   }
}
