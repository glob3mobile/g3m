

package com.glob3mobile.pointcloud.octree;

import java.util.List;

import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;


public interface PersistentOctree
extends
AutoCloseable {


   public interface Node {
      @Override
      String toString();


      String getID();


      Sector getSector();


      int getDepth();


      int getPointsCount();


      List<Geodetic3D> getPoints();


      Geodetic3D getAveragePoint();


   }


   public interface Visitor {
      void start();


      boolean visit(PersistentOctree.Node node);


      void stop();
   }


   public interface Statistics {
      void show();


      long getPointsCount();


      Sector getSector();


      double getMinHeight();


      double getMaxHeight();


      int getMinPointsPerNode();


      int getMaxPointsPerNode();


      double getAveragePointsPerNode();
   }


   void addPoint(Geodetic3D point);


   void optimize();


   void flush();


   @Override
   void close();


   void acceptDepthFirstVisitor(PersistentOctree.Visitor visitor);


   void acceptDepthFirstVisitor(Sector sector,
                                PersistentOctree.Visitor visitor);


   PersistentOctree.Statistics getStatistics(boolean showProgress);


   String getCloudName();

}
