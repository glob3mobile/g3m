

package com.glob3mobile.pointcloud.octree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public interface PersistentLOD
extends
AutoCloseable {


   //   public class Level {
   //      private final int              _level;
   //      private final List<Geodetic3D> _points;
   //
   //
   //      public Level(final int level,
   //                   final List<Geodetic3D> points) {
   //         _level = level;
   //         _points = Collections.unmodifiableList(points);
   //      }
   //
   //
   //      @Override
   //      public String toString() {
   //         final double estimatedSize = _points.size() * 3 * 8;
   //         return "[Level " + _level + //
   //                ", points=" + _points.size() + //
   //                ", estimatedSize=" + GStringUtils.getSpaceMessage(estimatedSize) + "]";
   //      }
   //
   //
   //      public int size() {
   //         return _points.size();
   //      }
   //
   //
   //      public int getLevel() {
   //         return _level;
   //      }
   //
   //
   //      public List<Geodetic3D> getPoints() {
   //         return _points;
   //      }
   //
   //   }


   public interface Node {
      @Override
      String toString();


      String getID();


      int getPointsCount();


      List<Geodetic3D> getPoints();


      List<Geodetic3D> getPoints(PersistentLOD.Transaction transaction);


      Sector getSector();


      int getDepth();
   }


   public static class NodeLayoutData {
      private final String _id;


      public NodeLayoutData(final String id) {
         _id = id;
      }


      public String getID() {
         return _id;
      }
   }


   public static class NodeLayout {
      private final String                             _id;
      private final List<PersistentLOD.NodeLayoutData> _data;


      public NodeLayout(final String id,
                        final List<PersistentLOD.NodeLayoutData> data) {
         _id = id;
         _data = Collections.unmodifiableList(new ArrayList<>(data));
      }


      public String getID() {
         return _id;
      }


      public List<PersistentLOD.NodeLayoutData> getData() {
         return _data;
      }
   }


   public interface Visitor {

      void start(PersistentLOD.Transaction transaction);


      void stop(PersistentLOD.Transaction transaction);


      boolean visit(PersistentLOD.Transaction transaction,
                    PersistentLOD.Node node);

   }


   public static interface Transaction {

      void commit();


      void rollback();

   }

   public interface Statistics {
      void show();


      String getPointCloudName();


      long getPointsCount();


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
            int level,
            List<Geodetic3D> points);


   @Override
   void close();


   String getCloudName();


   void acceptDepthFirstVisitor(PersistentLOD.Transaction transaction,
                                PersistentLOD.Visitor visitor);


   //   List<PersistentLOD.Level> getLODLevels(String id);


   Sector getSector(String id);


   PersistentLOD.Statistics getStatistics(boolean fast,
                                          boolean showProgress);


   PersistentLOD.NodeLayout getNodeLayout(String id);

}
