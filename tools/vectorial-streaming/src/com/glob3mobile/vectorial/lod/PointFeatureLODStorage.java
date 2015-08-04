

package com.glob3mobile.vectorial.lod;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;


public interface PointFeatureLODStorage
   extends
      AutoCloseable {


   String getName();


   @Override
   void close() throws IOException;


   void addLeafNode(String id,
                    Sector sector,
                    Geodetic2D averagePosition,
                    List<PointFeature> features);


   void optimize() throws IOException;


   void processPendingNodes(Comparator<PointFeature> featuresComparator);


   public static interface Statistics {


      long getFeaturesCount();


      Geodetic2D getAveragePosition();


      int getNodesCount();


      int getMinFeaturesPerNode();


      int getMaxFeaturesPerNode();


      double getAverageFeaturesPerNode();


      int getMaxNodeDepth();


      int getMinNodeDepth();


      double getAverageNodeDepth();


      void show();


   }


   public static interface Node {


      @Override
      String toString();


      String getID();


      Sector getNodeSector();


      Sector getMinimumSector();


      int getDepth();


      int getFeaturesCount();


      List<PointFeature> getFeatures();


      Geodetic2D getAveragePosition();


      List<String> getChildrenIDs();


   }


   public static interface NodeVisitor {

      void start();


      boolean visit(PointFeatureLODStorage.Node node);


      void stop();

   }


   void acceptDepthFirstVisitor(final PointFeatureLODStorage.NodeVisitor visitor);


   Statistics getStatistics(boolean showProgress);


   Sector getSector();


   List<PointFeatureLODStorage.Node> getNodesFor(Sector searchSector);


   List<PointFeatureLODStorage.Node> getNodesFor(Geodetic2D position);


   List<PointFeatureLODStorage.Node> getAllNodesOfDepth(int depth);


   PointFeatureLODStorage.Node getNode(String id);


}
