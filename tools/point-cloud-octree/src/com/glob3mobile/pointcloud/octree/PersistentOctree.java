

package com.glob3mobile.pointcloud.octree;

import org.glob3.mobile.generated.Geodetic3D;


public interface PersistentOctree
extends
AutoCloseable {

   void addPoint(Geodetic3D point);


   void optimize();


   void flush();


   @Override
   void close();


}
