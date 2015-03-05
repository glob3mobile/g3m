

package com.glob3mobile.utils;

import es.igosoftware.euclid.vector.GVector3D;


public interface Planet {

   GVector3D toCartesian(Geodetic3D position,
                         float verticalExaggeration);


   GVector3D toCartesian(Angle latitude,
                         Angle longitude,
                         double height,
                         float verticalExaggeration);


}
