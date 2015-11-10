

package com.glob3mobile.vectorial.lod.sorting;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;


public interface PointFeatureSortingLODStorage
   extends
      AutoCloseable {


   String getName();


   @Override
   void close() throws IOException;


   void addLeafNode(String id,
                    Sector nodeSector,
                    Sector minimumSector,
                    List<PointFeature> features);


   void optimize() throws IOException;


   void processPendingNodes(Comparator<PointFeature> featuresComparator,
                            boolean verbose);


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


   public static interface Node {


      @Override
      String toString();


      String getID();


      Sector getNodeSector();


      Sector getMinimumSector();


      int getDepth();


      int getFeaturesCount();


      List<PointFeature> getFeatures();


      List<String> getChildrenIDs();


   }


   public static interface NodeVisitor {

      void start();


      boolean visit(PointFeatureSortingLODStorage.Node node);


      void stop();

   }


   void acceptDepthFirstVisitor(final PointFeatureSortingLODStorage.NodeVisitor visitor);


   Statistics getStatistics(boolean showProgress);


   Sector getSector();


   List<PointFeatureSortingLODStorage.Node> getNodesFor(Sector searchSector);


   List<PointFeatureSortingLODStorage.Node> getNodesFor(Geodetic2D position);


   List<PointFeatureSortingLODStorage.Node> getAllNodesOfDepth(int depth);


   PointFeatureSortingLODStorage.Node getNode(String id);


}
