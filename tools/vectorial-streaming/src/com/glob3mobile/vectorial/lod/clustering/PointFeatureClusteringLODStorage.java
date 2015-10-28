

package com.glob3mobile.vectorial.lod.clustering;

import java.io.IOException;
import java.util.List;

import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public interface PointFeatureClusteringLODStorage
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


   public static interface Statistics {


      long getFeaturesCount();


      long getClustersCount();


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


      boolean visit(PointFeatureClusteringLODStorage.InnerNode node);


      boolean visit(PointFeatureClusteringLODStorage.LeafNode node);


      void stop();

   }


   void acceptDepthFirstVisitor(final PointFeatureClusteringLODStorage.NodeVisitor visitor);


   Statistics getStatistics(boolean showProgress);


   Sector getSector();


   void processPendingNodes(boolean verbose);

   //   void processPendingNodes(Comparator<PointFeature> featuresComparator,
   //                            boolean verbose);


   //   List<PointFeatureClusterLODStorage.Node> getNodesFor(Sector searchSector);
   //
   //
   //   List<PointFeatureClusterLODStorage.Node> getNodesFor(Geodetic2D position);
   //
   //
   //   List<PointFeatureClusterLODStorage.Node> getAllNodesOfDepth(int depth);
   //
   //
   //   PointFeatureClusterLODStorage.Node getNode(String id);


}
