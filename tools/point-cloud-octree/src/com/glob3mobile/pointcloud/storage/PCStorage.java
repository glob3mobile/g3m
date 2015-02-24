

package com.glob3mobile.pointcloud.storage;


import java.awt.Color;

import com.glob3mobile.pointcloud.Classification;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;


public interface PCStorage
extends
AutoCloseable {

   public interface Node {

      String getID();


      Sector getSector();


      int getDepth();


      int getPointsCount();


      //      List<Geodetic3D> getPoints();


      Geodetic3D getAveragePoint();


   }


   boolean hasIntensity();


   boolean hasClassification();


   boolean hasColor();


   void addPoint(Geodetic3D position);


   void addPoint(Geodetic3D position,
                 float intensity);


   void addPoint(Geodetic3D position,
                 Classification classification);


   void addPoint(Geodetic3D position,
                 Color color);


   void addPoint(Geodetic3D position,
                 float intensity,
                 Classification classification);


   void addPoint(Geodetic3D position,
                 float intensity,
                 Color color);


   void addPoint(Geodetic3D position,
                 Classification classification,
                 Color color);


   void addPoint(Geodetic3D position,
                 float intensity,
                 Classification classification,
                 Color color);


   void flush();


}
