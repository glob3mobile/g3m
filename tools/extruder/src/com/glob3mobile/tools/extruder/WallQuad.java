

package com.glob3mobile.tools.extruder;

import org.glob3.mobile.generated.Geodetic2D;


public class WallQuad {
   public final Geodetic2D _coordinate0;
   public final Geodetic2D _coordinate1;
   public final double     _lowerHeight;
   public final double     _upperHeight;


   WallQuad(final Geodetic2D coordinate0,
            final Geodetic2D coordinate1,
            final double lowerHeight,
            final double upperHeight) {
      _coordinate0 = coordinate0;
      _coordinate1 = coordinate1;
      _lowerHeight = lowerHeight;
      _upperHeight = upperHeight;
   }

}
