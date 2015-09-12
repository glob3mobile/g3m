

package com.glob3mobile.vectorial.lod;

import java.io.IOException;
import java.util.List;

import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;


public interface PointFeatureLODStorage
   extends
      AutoCloseable {


   String getName();


   @Override
   void close() throws IOException;


   void addLeafNode(String id,
                    Sector nodeSector,
                    Sector minimumSector,
                    final List<PointFeatureCluster> clusters,
                    List<PointFeature> features);


   void optimize() throws IOException;


   public static interface Statistics {


      long getFeaturesCount();


      long getClustersCount();


      int getNodesCount();


      int getMinFeaturesPerNode();


      int getMaxFeaturesPerNode();


      double getAverageFeaturesPerNode();


      int getMinClustersPerNode();


      int getMaxClustersPerNode();


      double getAverageClustersPerNode();


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


      int getClustersCount();


      List<PointFeatureCluster> getClusters();


      List<String> getChildrenIDs();


      int getFeaturesCount();


      List<PointFeature> getFeatures();

   }


   public static interface NodeVisitor {

      void start();


      boolean visit(PointFeatureLODStorage.Node node);


      void stop();

   }


   void acceptDepthFirstVisitor(final PointFeatureLODStorage.NodeVisitor visitor);


   Statistics getStatistics(boolean showProgress);


   Sector getSector();


   void createLOD(boolean verbose);


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
