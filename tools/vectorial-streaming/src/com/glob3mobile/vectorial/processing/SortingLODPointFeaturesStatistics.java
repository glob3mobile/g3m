

package com.glob3mobile.vectorial.processing;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.lod.sorting.PointFeatureSortingLODStorage;
import com.glob3mobile.vectorial.lod.sorting.mapdb.PointFeatureSortingLODMapDBStorage;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.QuadKey;
import com.glob3mobile.vectorial.storage.QuadKeyUtils;
import com.glob3mobile.vectorial.utils.GEOBitmap;


public class SortingLODPointFeaturesStatistics {


   public static final class LODDrawer
      implements
         PointFeatureSortingLODStorage.NodeVisitor {

      private final PointFeatureSortingLODStorage _lodStorage;
      private final int                           _nodesCount;

      private Progress                            _progress;
      private GEOBitmap                           _bitmap;


      private LODDrawer(final PointFeatureSortingLODStorage lodStorage,
                        final int nodesCount) {
         _lodStorage = lodStorage;
         _nodesCount = nodesCount;
      }


      @Override
      public boolean visit(final PointFeatureSortingLODStorage.Node node) {
         // System.out.println(node.getID() + ", features=" + node.getFeaturesCount());

         final Color featureColor = new Color(1, 1, 0, 0.25f);

         final int level = node.getDepth();

         final int maxLevel = 15;
         if (level < maxLevel) {
            // final Font font = new Font(Font.SERIF, Font.BOLD, 12 + ((maxLevel - level) * 3));
            // final Font font = new Font(Font.SERIF, Font.BOLD, 14);
            // final int pointSize = (maxLevel - level);
            final int pointSize = 2;

            final Sector nodeSector = node.getNodeSector();
            _bitmap.drawSector(nodeSector, new Color(1, 1, 1, 0.05f), new Color(1, 1, 1, 0.2f));

            final Sector minimumSector = node.getMinimumSector();
            _bitmap.drawSector(minimumSector, new Color(0, 1, 1, 0.05f), new Color(0, 1, 1, 0.2f));

            for (final PointFeature feature : node.getFeatures()) {
               _bitmap.drawPoint(feature._position, pointSize, pointSize, featureColor);
               // final String featureName = (String) feature._properties.get("name");
               // _bitmap.drawPoint(featureName, feature._position, pointSize, pointSize, featureColor, font);
            }
         }

         _progress.stepDone();
         return true;
      }


      @Override
      public void start() {
         _progress = new Progress(_nodesCount) {
            @Override
            public void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish) {
               System.out.println(_lodStorage.getName() + " - Processing: "
                                  + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));

            }
         };


         final int width = 2048;
         // final Sector sector = _lodStorage.getSector();
         final Sector sector = Sector.FULL_SPHERE;
         final double sectorFactor = sector._deltaLatitude._radians / sector._deltaLongitude._radians;
         final int height = Math.round((float) sectorFactor * width);
         _bitmap = new GEOBitmap(sector, width, height, Color.BLACK);
      }


      @Override
      public void stop() {
         try {
            _bitmap.save(new File("LOD_" + _lodStorage.getName() + ".png"));
         }
         catch (final IOException e) {
            throw new RuntimeException(e);
         }
         _bitmap = null;

         _progress.finish();
         _progress = null;
      }
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("SortingLODPointFeaturesStatistics 0.1");
      System.out.println("-------------------------------------\n");


      final File directory = new File("PointFeaturesLOD");
      // final String name = "Cities1000_LOD";
      // final String name = "AR_LOD";
      // final String name = "ES_LOD";
      final String name = "GEONames-PopulatedPlaces_LOD";


      try (final PointFeatureSortingLODStorage lodStorage = PointFeatureSortingLODMapDBStorage.openReadOnly(directory, name)) {
         final PointFeatureSortingLODStorage.Statistics statistics = lodStorage.getStatistics(true);
         statistics.show();

         final int nodesCount = statistics.getNodesCount();


         //         // final String id = "102311321213";
         //         //final String id = "100221122202";
         //
         //         //         drawLODForNode(lodStorage, "100221122200");
         //         //         drawLODForNode(lodStorage, "100221122201");
         //         //         drawLODForNode(lodStorage, "100221122202");
         //         //         drawLODForNode(lodStorage, "100221122203");
         //
         //         //         drawLODForNode(lodStorage, "100221033310");
         //         //         drawLODForNode(lodStorage, "100221033311");
         //         //         drawLODForNode(lodStorage, "100221033312");
         //         //         drawLODForNode(lodStorage, "100221033313");
         //
         //         //         drawLODForSector(lodStorage, "Spain", Sector.fromDegrees(-8, 37, 1, 42));
         //
         //         //  drawLODForNode(lodStorage, "03311101");
         //
         //         //         final Sector baSector = Sector.fromDegrees( //
         //         //                  -34.703643764020576157, -58.579915412480858095, //
         //         //                  -34.519657217233664426, -58.307698526247456527);
         //         //
         //         //         drawLODForSector(lodStorage, "Buenos Aires", baSector);
         //
         //         // drawLODForPosition(lodStorage, "Buenos Aires", Geodetic2D.fromDegrees(-34.610202831685171532, -58.385756267343843717));
         //         // drawLODForPosition(lodStorage, "Madrid", Geodetic2D.fromDegrees(40.414924394015059761, -3.6980228798493248732));
         //         // drawLODForPosition(lodStorage, "Caceres", Geodetic2D.fromDegrees(39.483333, -6.366667));
         //
         //         // drawLODForPosition(lodStorage, "DC", Geodetic2D.fromDegrees(38.904722, -77.016389));
         //         // drawLODForPosition(lodStorage, "New York City", Geodetic2D.fromDegrees(40.7127, -74.0059));
         //         drawLODForPosition(lodStorage, "San Francisco", Geodetic2D.fromDegrees(37.783333, -122.416667));

         lodStorage.acceptDepthFirstVisitor(new LODDrawer(lodStorage, nodesCount));
      }


      System.out.println("\n- done!");
   }


   public static void drawLODForNode(final PointFeatureSortingLODStorage lodStorage,
                                     final String nodeID) throws IOException {
      final QuadKey root = new QuadKey(new byte[] {}, lodStorage.getSector());
      final Sector searchSector = QuadKey.sectorFor(root, QuadKeyUtils.toBinaryID(nodeID));

      drawLODForSector(lodStorage, nodeID, searchSector);
   }


   public static void drawLODForSector(final PointFeatureSortingLODStorage lodStorage,
                                       final String name,
                                       final Sector searchSector) throws IOException {
      final List<PointFeatureSortingLODStorage.Node> nodes = lodStorage.getNodesFor(searchSector);

      drawNodes(name, searchSector, nodes);
   }


   public static void drawLODForPosition(final PointFeatureSortingLODStorage lodStorage,
                                         final String name,
                                         final Geodetic2D position) throws IOException {
      final List<PointFeatureSortingLODStorage.Node> nodes = lodStorage.getNodesFor(position);
      final Sector sector = nodes.get(nodes.size() - 1).getNodeSector();
      drawNodes(name, sector, nodes);
   }


   public static void drawNodes(final String name,
                                final Sector sector,
                                final List<PointFeatureSortingLODStorage.Node> nodes) throws IOException {
      final GEOBitmap bitmap = new GEOBitmap(sector, 2048, 2048, Color.BLACK);
      for (int i = 0; i < nodes.size(); i++) {
         final PointFeatureSortingLODStorage.Node node = nodes.get(i);
         if (node != null) {
            System.out.println("Node #\"" + node.getID() + "\", features=" + node.getFeaturesCount());

            final int pointSize = ((nodes.size() - i) + 1) * 3;
            final Color featureColor = new Color(1, 1, 0, 0.8f);
            final Font featureFont = new Font(Font.SERIF, Font.BOLD, 6 + pointSize);

            for (final PointFeature features : node.getFeatures()) {
               final String featuresName = (String) features._properties.get("name");
               bitmap.drawPoint(featuresName + " LOD:" + i, features._position, pointSize, pointSize, featureColor, featureFont,
                        featureColor);
            }
         }
      }

      bitmap.save(new File("LOD-Levels-" + name + ".png"));
   }


}
