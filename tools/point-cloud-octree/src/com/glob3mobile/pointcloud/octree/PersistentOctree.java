

package com.glob3mobile.pointcloud.octree;

import java.util.List;

import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.Sector;


public interface PersistentOctree
         extends
            AutoCloseable {


   public interface Node {
      @Override
      String toString();


      String getID();


      int getPointsCount();


      List<Geodetic3D> getPoints();


      Geodetic3D getAveragePoint();


      Sector getSector();


      int getLevel();
   }


   public interface Visitor {
      void start();


      boolean visit(PersistentOctree.Node node);


      void stop();
   }


   void addPoint(Geodetic3D point);


   void optimize();


   void flush();


   @Override
   void close();


   void acceptVisitor(PersistentOctree.Visitor visitor);


   void showStatistics();

}
