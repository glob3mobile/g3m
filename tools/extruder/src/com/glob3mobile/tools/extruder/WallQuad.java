

package com.glob3mobile.tools.extruder;

import org.glob3.mobile.generated.Geodetic3D;


public class WallQuad {
   public final Geodetic3D _topCorner0;
   public final Geodetic3D _topCorner1;
   public final double     _lowerHeight;


   WallQuad(final Geodetic3D topCorner0,
            final Geodetic3D topCorner1,
            final double lowerHeight) {
      _topCorner0 = topCorner0;
      _topCorner1 = topCorner1;
      _lowerHeight = lowerHeight;
   }

}
