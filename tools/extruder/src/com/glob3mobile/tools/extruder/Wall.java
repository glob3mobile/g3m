

package com.glob3mobile.tools.extruder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Wall {
   public final List<WallQuad> _quads;


   Wall(final List<WallQuad> quads) {
      _quads = Collections.unmodifiableList(new ArrayList<>(quads));
   }

}
