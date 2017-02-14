

package com.glob3mobile.pointcloud.octree;

import java.util.List;

import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;


public interface PersistentLOD
extends
AutoCloseable {


   public static interface NodeLevel {
      int getLevel();


      List<Geodetic3D> getPoints(PersistentLOD.Transaction transaction);


      int getPointsCount();
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


   //   public static class NodeLayout {
   //      private final String       _id;
   //      private final List<String> _nodesIDs;
   //
   //
   //      public NodeLayout(final String id,
   //                        final List<String> nodesIDs) {
   //         _id = id;
   //         _nodesIDs = Collections.unmodifiableList(new ArrayList<>(nodesIDs));
   //      }
   //
   //
   //      public String getID() {
   //         return _id;
   //      }
   //
   //
   //      public List<String> getNodesIDs() {
   //         return _nodesIDs;
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


      double getAverageHeight();


      int getMinPointsPerNode();


      int getMaxPointsPerNode();


      int getMinDepth();


      int getMaxDepth();


      double getAverageDepth();


      long getNodesCount();

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


   PersistentLOD.Statistics getStatistics(boolean showProgress);


   //   PersistentLOD.NodeLayout getNodeLayout(String id);


   void acceptVisitor(PersistentLOD.Transaction transaction,
                      PersistentLOD.Visitor visitor,
                      Sector sector);


   PersistentLOD.Node getNode(String id,
                              boolean loadPoints);


   PersistentLOD.NodeLevel getNodeLevel(String nodeID,
                                        int level,
                                        boolean loadPoints);


}
