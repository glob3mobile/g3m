

package com.glob3mobile.pointcloud.octree;

import java.util.List;


public interface PersistentLOD
         extends
            AutoCloseable {


   public static interface NodeLevel {
      int getLevel();


      List<Geodetic3D> getPoints(PersistentLOD.Transaction transaction);
   }


   public static interface Node {
      @Override
      String toString();


      String getID();


      int getPointsCount();


      int getLevelsCount();


      List<PersistentLOD.NodeLevel> getLevels();


      int[] getLevelsPointsCount();


      Sector getSector();


      int getDepth();
   }


   //   public static class NodeLayoutData {
   //      private final String _id;
   //
   //
   //      public NodeLayoutData(final String id) {
   //         _id = id;
   //      }
   //
   //
   //      public String getID() {
   //         return _id;
   //      }
   //   }
   //
   //
   //   public static class NodeLayout {
   //      private final String                             _id;
   //      private final List<PersistentLOD.NodeLayoutData> _data;
   //
   //
   //      public NodeLayout(final String id,
   //                        final List<PersistentLOD.NodeLayoutData> data) {
   //         _id = id;
   //         _data = Collections.unmodifiableList(new ArrayList<>(data));
   //      }
   //
   //
   //      public String getID() {
   //         return _id;
   //      }
   //
   //
   //      public List<PersistentLOD.NodeLayoutData> getData() {
   //         return _data;
   //      }
   //   }


   public static interface Visitor {

      void start(PersistentLOD.Transaction transaction);


      void stop(PersistentLOD.Transaction transaction);


      boolean visit(PersistentLOD.Transaction transaction,
                    PersistentLOD.Node node);

   }


   public static interface Transaction {

      void commit();


      void rollback();

   }


   public static interface Statistics {
      void show();


      String getPointCloudName();


      long getPointsCount();


      long getLevelsCount();


      Sector getSector();


      double getMinHeight();


      double getMaxHeight();


      int getMinPointsPerNode();


      int getMaxPointsPerNode();


      int getMinDepth();


      int getMaxDepth();


      double getAverageDepth();

   }


   PersistentLOD.Transaction createTransaction();


   void put(PersistentLOD.Transaction transaction,
            String id,
            List<List<Geodetic3D>> levelsPoints);


   @Override
   void close();


   String getCloudName();


   void acceptDepthFirstVisitor(PersistentLOD.Transaction transaction,
                                PersistentLOD.Visitor visitor);


   Sector getSector(String id);


   PersistentLOD.Statistics getStatistics(boolean fast,
                                          boolean showProgress);


   //   PersistentLOD.NodeLayout getNodeLayout(String id);

}
