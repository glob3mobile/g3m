

package com.glob3mobile.pointcloud.octree;

import org.glob3.mobile.generated.Geodetic3D;


public interface PersistentOctree {

   void close();


   void remove();


   void addPoint(Geodetic3D point);

}
