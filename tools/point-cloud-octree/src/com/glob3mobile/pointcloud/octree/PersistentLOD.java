

package com.glob3mobile.pointcloud.octree;

import java.util.List;


public interface PersistentLOD
extends
AutoCloseable {

   public interface Node {
      @Override
      String toString();


      String getID();


      boolean isDirty();


      int getPointsCount();


      List<Geodetic3D> getPoints();


      Sector getSector();


      int getLevel();
   }

   public interface Visitor {

      void start();


      void stop();


      boolean visit(PersistentLOD.Node node);

   }


   public static interface Transaction {

      void commit();


      void rollback();

   }


   PersistentLOD.Transaction createTransaction();


   void put(PersistentLOD.Transaction transaction,
            String id,
            boolean dirty,
            List<Geodetic3D> points);


   void putOrMerge(PersistentLOD.Transaction transaction,
                   String id,
                   boolean dirty,
                   List<Geodetic3D> points);


   @Override
   void close();


   String getCloudName();


   void acceptDepthFirstVisitor(PersistentLOD.Visitor visitor);

}
