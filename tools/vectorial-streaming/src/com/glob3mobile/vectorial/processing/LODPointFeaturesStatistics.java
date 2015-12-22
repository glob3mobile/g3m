

package com.glob3mobile.vectorial.processing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.lod.mapdb.PointFeatureLODMapDBStorage;


public class LODPointFeaturesStatistics {

   private static final Charset UTF8 = Charset.forName("UTF8");


   public static void main(final String[] args) throws IOException {
      System.out.println("LODPointFeaturesStatistics 0.1");
      System.out.println("------------------------------\n");


      final File directory = new File("PointFeaturesLOD");
      // final String name = "Cities1000_LOD";
      // final String name = "AR_LOD";
      // final String name = "ES_LOD";
      final String name = "GEONames-PopulatedPlaces_LOD";
      //      final String name = "SpanishBars_LOD";
      //      final String name = "Tornados_LOD";


      // Touched on position (lat=24.74251163696720468d, lon=-21.288931897141267768d, height=0)
      // Touched on position (lat=57.860351031010154088d, lon=26.967094061871140553d, height=4.2678561945965884523e-09)

      final Sector sectorToProcess = Sector.fromDegrees( //
               25.74251163696720468, -22.288931897141267768, //
               90.0, 28);

      try (final PointFeatureLODStorage clusterStorage = PointFeatureLODMapDBStorage.openReadOnly(directory, name)) {
         final PointFeatureLODStorage.Statistics statistics = clusterStorage.getStatistics(true);
         statistics.show();

         final int nodesCount = statistics.getNodesCount();
         final long featuresCount = statistics.getFeaturesCount();
         final int minNodeDepth = 0;
         final int maxNodeDepth = 4; //statistics.getMaxNodeDepth();

         final AtomicLong visitedFeaturesCounter = new AtomicLong(0);

         final Set<String> properties = new HashSet<String>();
         properties.add("name");
         properties.add("population");

         final Path path = FileSystems.getDefault().getPath(name + ".geojson");
         try (BufferedWriter writter = Files.newBufferedWriter(path, UTF8, StandardOpenOption.TRUNCATE_EXISTING,
                  StandardOpenOption.WRITE)) {

            writter.append("{\"type\":\"FeatureCollection\",\"features\":[");
            writter.newLine();

            for (int depth = minNodeDepth; depth <= maxNodeDepth; depth++) {
               final int minDepth = depth;
               final int maxDepth = depth;

               //            final PointFeatureLODStorage.NodeVisitor visitor = new LODDrawerVisitor(sectorToProcess, clusterStorage, nodesCount,
               //                     minDepth, maxDepth, featuresCount);
               final PointFeatureLODStorage.NodeVisitor visitor = new LODExporterVisitor(sectorToProcess, clusterStorage,
                        nodesCount, minDepth, maxDepth, featuresCount, visitedFeaturesCounter, writter, properties);
               clusterStorage.acceptDepthFirstVisitor(visitor);
            }

            writter.append("]}");
            writter.newLine();
         }

      }

      System.out.println("\n- done!");
   }


}
