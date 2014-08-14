

package com.glob3mobile.pointcloud.octree;

import java.util.List;


public interface PersistentLOD
extends
AutoCloseable {


   public static interface Transaction {

      void commit();


      void rollback();

   }


   PersistentLOD.Transaction createTransaction();


   void put(PersistentLOD.Transaction transaction,
            String id,
            List<Geodetic3D> points,
            boolean dirty);


   @Override
   void close();


   String getCloudName();


}
