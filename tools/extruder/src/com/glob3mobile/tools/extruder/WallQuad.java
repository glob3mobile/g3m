

package com.glob3mobile.tools.extruder;

import org.glob3.mobile.generated.Geodetic3D;


public class WallQuad {
   public final Geodetic3D _coordinate0;
   public final Geodetic3D _coordinate1;
   public final double     _lowerHeight;


   WallQuad(final Geodetic3D coordinate0,
            final Geodetic3D coordinate1,
            final double lowerHeight) {
      _coordinate0 = coordinate0;
      _coordinate1 = coordinate1;
      _lowerHeight = lowerHeight;
   }

}
