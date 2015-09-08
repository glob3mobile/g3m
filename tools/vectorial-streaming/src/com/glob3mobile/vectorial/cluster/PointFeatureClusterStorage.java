

package com.glob3mobile.vectorial.cluster;

import java.io.IOException;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public interface PointFeatureClusterStorage
   extends
      AutoCloseable {


   String getName();


   @Override
   void close() throws IOException;


   void addLeafNode(String id,
                    Sector nodeSector,
                    Sector minimumSector,
                    Geodetic2D averagePosition,
                    List<PointFeature> features);


   void optimize() throws IOException;


   //   void processPendingNodes(Comparator<PointFeature> featuresComparator,
   //                            boolean verbose);


   public static interface Statistics {


      long getFeaturesCount();


      long getClustersCount();


      Geodetic2D getFeaturesAveragePosition();


      int getNodesCount();


      int getLeafNodesCount();


      int getInnerNodesCount();


      int getMinFeaturesPerLeafNode();


      int getMaxFeaturesPerLeafNode();


      double getAverageFeaturesPerLeafNode();


      int getMinClustersPerInnerNode();


      int getMaxClustersPerInnerNode();


      double getAverageClustersPerInnerNode();


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


      Geodetic2D getAveragePosition();

   }


   public static interface InnerNode
      extends
         Node {


      int getClustersCount();


      List<PointFeatureCluster> getClusters();


      List<String> getChildrenIDs();

   }


   public static interface LeafNode
      extends
         Node {


      int getFeaturesCount();


      List<PointFeature> getFeatures();


   }


   public static interface NodeVisitor {

      void start();


      boolean visit(PointFeatureClusterStorage.InnerNode node);


      boolean visit(PointFeatureClusterStorage.LeafNode node);


      void stop();

   }


   void acceptDepthFirstVisitor(final PointFeatureClusterStorage.NodeVisitor visitor);


   Statistics getStatistics(boolean showProgress);


   Sector getSector();


   //   List<PointFeatureClusterStorage.Node> getNodesFor(Sector searchSector);
   //
   //
   //   List<PointFeatureClusterStorage.Node> getNodesFor(Geodetic2D position);
   //
   //
   //   List<PointFeatureClusterStorage.Node> getAllNodesOfDepth(int depth);
   //
   //
   //   PointFeatureClusterStorage.Node getNode(String id);


}
