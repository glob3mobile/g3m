

package com.glob3mobile.pointcloud;

import com.glob3mobile.pointcloud.octree.Angle;
import com.glob3mobile.pointcloud.octree.Geodetic3D;

import es.igosoftware.euclid.vector.GVector3D;


public interface Planet {

   GVector3D toCartesian(Geodetic3D position);


   GVector3D toCartesian(Angle latitude,
                         Angle longitude,
                         double height);

}
