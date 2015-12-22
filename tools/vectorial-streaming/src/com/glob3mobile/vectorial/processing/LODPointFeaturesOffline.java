

package com.glob3mobile.vectorial.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.glob3mobile.json.JSONUtils;
import com.glob3mobile.utils.IOUtils;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.lod.mapdb.PointFeatureLODMapDBStorage;
import com.glob3mobile.vectorial.utils.JSON;


public class LODPointFeaturesOffline {


   public static void process(final File sourceDirectory,
                              final String sourceName,
                              final File targetDirectory,
                              final String targetName,
                              final String[] properties) throws IOException {
      try (final PointFeatureLODStorage lodStorage = PointFeatureLODMapDBStorage.openReadOnly(sourceDirectory, sourceName)) {
         final PointFeatureLODStorage.Statistics statistics = lodStorage.getStatistics(true);
         statistics.show();

         final int nodesCount = statistics.getNodesCount();

         final File target = new File(targetDirectory, targetName);
         IOUtils.ensureEmptyDirectory(target);

         saveLODMetadata(lodStorage, target);

         final PointFeatureLODStorage.NodeVisitor visitor = new PointFeatureLODStorage.NodeVisitor() {
            private Progress _progress;


            @Override
            public void start() {
               _progress = new Progress(nodesCount) {
                  @Override
                  public void informProgress(final long stepsDone,
                                             final double percent,
                                             final long elapsed,
                                             final long estimatedMsToFinish) {
                     System.out.println("Caching: " + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
                  }
               };
            }


            @Override
            public boolean visit(final PointFeatureLODStorage.Node node) {

               //               final List<PointFeature> features = node.getFeatures();
               //               System.out.println(features);
               //
               //               throw new RuntimeException();

               try {
                  saveNodeFeatures(target, node, properties);
                  saveNodeChildrenMetadata(target, node, lodStorage);
               }
               catch (final IOException e) {
                  throw new RuntimeException(e);
               }
               return true;
            }


            @Override
            public void stop() {
               _progress.finish();
               _progress = null;
            }
         };

         lodStorage.acceptDepthFirstVisitor(visitor);
      }
   }


   private static void saveNodeChildrenMetadata(final File target,
                                                final PointFeatureLODStorage.Node node,
                                                final PointFeatureLODStorage lodStorage) throws IOException {
      final List<String> childrenIDs = node.getChildrenIDs();

      final List<Map<String, Object>> childrenMetadataJSON = new ArrayList<>(childrenIDs.size());

      for (final String childID : childrenIDs) {
         final PointFeatureLODStorage.Node child = lodStorage.getNode(childID);
         childrenMetadataJSON.add(JSON.toJSON(child));
      }

      final File file = new File(target, toNodesDirectories(node.getID()) + "children.json");
      final String childrenMetadata = JSONUtils.toJSON(childrenMetadataJSON);
      IOUtils.save(file, childrenMetadata);
   }


   private static void saveNodeFeatures(final File target,
                                        final PointFeatureLODStorage.Node node,
                                        final String[] properties) throws IOException {
      final File file = new File(target, toNodesDirectories(node.getID()) + "features.json");
      final String features = JSONUtils.toJSON(JSON.toGEOJSON(node, properties));
      IOUtils.save(file, features);
   }


   private static String toNodesDirectories(final String id) {
      final StringBuilder sb = new StringBuilder("nodes" + File.separator);
      final int length = id.length();
      for (int i = 0; i < length; i++) {
         final char c = id.charAt(i);
         sb.append(c);
         sb.append(File.separatorChar);
      }
      return sb.toString();
   }


   private static void saveLODMetadata(final PointFeatureLODStorage lodStorage,
                                       final File target) throws IOException {
      final String metadata = JSONUtils.toJSON(JSON.getMetadataAsJSON(lodStorage));
      IOUtils.save(new File(target, "metadata.json"), metadata);
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("LODPointFeaturesOffline 0.1");
      System.out.println("---------------------------\n");


      final File sourceDirectory = new File("PointFeaturesLOD");
      final String sourceName = "GEONames-PopulatedPlaces_LOD";

      final File targetDirectory = new File("PointFeaturesLOD_Cache");
      final String targetName = sourceName + "_Cache";

      final String[] properties = { "name", "population" };

      LODPointFeaturesOffline.process(sourceDirectory, sourceName, targetDirectory, targetName, properties);

      System.out.println("\n - done!");
   }


}
