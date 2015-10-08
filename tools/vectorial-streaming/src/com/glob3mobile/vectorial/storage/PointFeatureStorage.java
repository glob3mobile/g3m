

package com.glob3mobile.vectorial.storage;

import java.io.IOException;
import java.util.List;

import com.glob3mobile.geo.Sector;


public interface PointFeatureStorage
   extends
      AutoCloseable {


   public static interface Node {


      @Override
      String toString();


      String getID();


      Sector getNodeSector();


      Sector getMinimumSector();


      int getDepth();


      int getFeaturesCount();


      List<PointFeature> getFeatures();


   }


   public static interface NodeVisitor {

      void start();


      boolean visit(PointFeatureStorage.Node node);


      void stop();

   }


   public static interface Statistics {


      long getFeaturesCount();


      int getNodesCount();


      int getMinFeaturesPerNode();


      int getMaxFeaturesPerNode();


      double getAverageFeaturesPerNode();


      int getMaxNodeDepth();


      int getMinNodeDepth();


      double getAverageNodeDepth();


      void show();


   }


   String getName();


   void addFeature(PointFeature feature) throws IOException;


   void flush() throws IOException;


   @Override
   void close() throws IOException;


   void optimize() throws IOException;


   void acceptDepthFirstVisitor(final PointFeatureStorage.NodeVisitor visitor);


   Statistics getStatistics(boolean showProgress);


   Sector getSector();


}
