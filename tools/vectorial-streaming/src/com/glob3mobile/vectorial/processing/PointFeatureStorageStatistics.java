

package com.glob3mobile.vectorial.processing;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.mapdb.PointFeatureMapDBStorage;
import com.glob3mobile.vectorial.utils.Counter;
import com.glob3mobile.vectorial.utils.GEOBitmap;


public class PointFeatureStorageStatistics {


   public static void main(final String[] args) throws IOException {
      System.out.println("PointFeatureStorageStatistics 0.1");
      System.out.println("---------------------------------\n");


      final File storageDir = new File("PointFeaturesStorage");
      // final String storageName = "PopulatedPlaces";
      // final String storageName = "NO";

      // final String storageName = "Cities1000";
      // final String storageName = "AR";
      // final String storageName = "ES";
      // final String storageName = "MX";
      // final String storageName = "NO";

      final String storageName = "GEONames-PopulatedPlaces";
      //      final String storageName = "SFCrimes";

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(storageDir, storageName)) {

         final PointFeatureStorage.Statistics statistics = storage.getStatistics(true);
         statistics.show();
         final int nodesCount = statistics.getNodesCount();

         System.out.println();


         storage.acceptDepthFirstVisitor(new PointFeatureStorage.NodeVisitor() {
            private GEOBitmap _geoBitmap = null;
            private Progress  _progress  = null;

            private Counter   _featureClassCounter;
            private Counter   _featureCodeCounter;
            private long      _maxPopulation;


            //          properties.put("featureClass", tokens[6]);
            //          properties.put("featureCode", tokens[7]);

            @Override
            public boolean visit(final PointFeatureStorage.Node node) {
               // System.out.println(node.getID() + ", features: " + node.getFeaturesCount());

               //               final String id = node.getID();
               //               if (node.getFeaturesCount() > 4096) {
               //                  System.out.println(id + " " + node.getFeaturesCount());
               final Sector nodeSector = node.getNodeSector();
               final Sector minimumSector = node.getMinimumSector();

               _geoBitmap.drawSector(nodeSector, new Color(1, 1, 0, 0.2f), new Color(1, 1, 0, 0.5f));
               _geoBitmap.drawSector(minimumSector, new Color(0, 1, 1, 0.2f), new Color(0, 1, 1, 0.5f));

               final int pointSize = 8;

               for (final PointFeature feature : node.getFeatures()) {
                  final Geodetic2D position = feature._position;
                  //                  if (!nodeSector.contains(position)) {
                  //                     throw new RuntimeException("LOGIC ERROR 1");
                  //                  }
                  //                  if (!minimumSector.contains(position)) {
                  //                     throw new RuntimeException("LOGIC ERROR 2");
                  //                  }
                  _geoBitmap.drawPoint(position, pointSize, pointSize, new Color(1, 1, 0, 0.05f));

                  //                  final Map<String, Object> properties = feature._properties;
                  //
                  //                  final long population = (Long) properties.get("population");
                  //
                  //                  _maxPopulation = Math.max(_maxPopulation, population);

                  //                  final String featureClass = (String) properties.get("featureClass");
                  //                  final String featureCode = (String) properties.get("featureCode");
                  //
                  //
                  //                  if (featureClass.equals("A")) {
                  //                     _featureClassCounter.count(featureClass);
                  //                     _featureCodeCounter.count(featureClass + "." + featureCode);
                  //
                  //                     //System.out.println(properties);
                  //                     System.out.println("\"" + properties.get("name") + "\"" + //
                  //                                        ", featureClass=" + featureClass + //
                  //                                        ", featureCode=" + featureCode);
                  //                  }
               }
               //               }

               _progress.stepDone();

               return true;
            }


            @Override
            public void start() {
               _progress = new Progress(nodesCount, true) {
                  @Override
                  public void informProgress(final long stepsDone,
                                             final double percent,
                                             final long elapsed,
                                             final long estimatedMsToFinish) {
                     System.out.println("Processing " + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
                  }
               };

               final int width = 2048 + 1024;
               final Sector sector = storage.getSector();
               // final Sector sector = Sector.FULL_SPHERE;
               final double sectorFactor = sector._deltaLatitude._radians / sector._deltaLongitude._radians;
               final int height = Math.round((float) sectorFactor * width);
               _geoBitmap = new GEOBitmap(sector, width, height, Color.BLACK);

               _featureClassCounter = new Counter("featureClass");
               _featureCodeCounter = new Counter("featureCode");

               _maxPopulation = 0;
            }


            @Override
            public void stop() {
               try {
                  _geoBitmap.save(new File(storageName + "-quadtree.png"));
               }
               catch (final IOException e) {
                  e.printStackTrace();
               }
               _geoBitmap = null;
               _progress.finish();

               _featureClassCounter.show();
               _featureCodeCounter.show();

               System.out.println("_maxPopulation:" + _maxPopulation);
            }


         });


      }


      System.out.println("\n- done!");
   }

}
