

package com.glob3mobile.vectorial.storage.test;


import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.PointFeatureStorage.Node;
import com.glob3mobile.vectorial.storage.mapdb.PointFeatureMapDBStorage;
import com.glob3mobile.vectorial.utils.GEOBitmap;


public class PointFeatureMapDBStorageTest {

   private final File   _directory          = new File("PointFeatureStorageTest");
   private final String _name               = "Test";
   private final Sector _sector             = Sector.FULL_SPHERE;
   private final int    _maxBufferSize      = 512;
   private final int    _maxFeaturesPerNode = 512;


   @Before
   public void setUp() throws IOException {
      PointFeatureMapDBStorage.delete(_directory, _name);
   }


   @After
   public void tearDown() throws IOException {
      PointFeatureMapDBStorage.delete(_directory, _name);
   }


   @Test(
            expected = IOException.class)
   public void testOpenReadOnlyNonExisting() throws IOException {
      try (final PointFeatureStorage x = PointFeatureMapDBStorage.openReadOnly(_directory, _name)) {
      }
   }


   @Test
   public void testOpen() throws IOException {
      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.createEmpty(_sector, _directory, _name, _maxBufferSize,
               _maxFeaturesPerNode)) {
      }

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.open(_directory, _name)) {
      }

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(_directory, _name)) {
      }
   }


   private static final Random random = new Random(0);


   private static PointFeature addSampleFeature(final PointFeatureStorage storage,
                                                final int i) throws IOException {
      final Map<String, Object> properties = new HashMap<>();
      properties.put("name", "Feature #" + i);
      final double latitude = (random.nextDouble() * 180) - 90;
      final double longitude = (random.nextDouble() * 360) - 180;
      final PointFeature feature = new PointFeature(properties, Geodetic2D.fromDegrees(latitude, longitude));
      storage.addFeature(feature);
      return feature;
   }


   private static void removeIfPresent(final Collection<PointFeature> collection,
                                       final PointFeature feature) {
      final Iterator<PointFeature> iterator = collection.iterator();
      while (iterator.hasNext()) {
         final PointFeature f = iterator.next();
         if (f._properties.equals(feature._properties) && f._position.closeTo(feature._position)) {
            iterator.remove();
         }
      }
   }


   @Test
   public void testAdd() throws IOException {
      final Set<PointFeature> expected = new HashSet<>();

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.createEmpty(_sector, _directory, _name, _maxBufferSize,
               _maxFeaturesPerNode)) {
         expected.add(addSampleFeature(storage, 0));
         expected.add(addSampleFeature(storage, 1));
         expected.add(addSampleFeature(storage, 2));
         expected.add(addSampleFeature(storage, 3));
         expected.add(addSampleFeature(storage, 4));
      }

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(_directory, _name)) {

         storage.acceptDepthFirstVisitor(new PointFeatureStorage.NodeVisitor() {
            @Override
            public void start() {
            }


            @Override
            public boolean visit(final Node node) {
               final List<PointFeature> nodeFeatures = node.getFeatures();
               for (final PointFeature feature : nodeFeatures) {
                  removeIfPresent(expected, feature);
               }
               return true;
            }


            @Override
            public void stop() {
            }
         });

         Assert.assertEquals(0, expected.size());
      }

   }


   @Test
   public void testHugeAdd() throws IOException {
      final List<PointFeature> expected = new ArrayList<>();

      final int count = 15_000;
      System.out.println("Saving " + count + " features...");

      final int maxBufferSize = 1024;
      final int maxFeaturesPerNode = 1024;

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.createEmpty(_sector, _directory, _name, maxBufferSize,
               maxFeaturesPerNode)) {
         for (int i = 0; i < count; i++) {
            expected.add(addSampleFeature(storage, i));
         }
      }

      System.out.println("Testing " + count + " features...");

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(_directory, _name)) {

         storage.acceptDepthFirstVisitor(new PointFeatureStorage.NodeVisitor() {
            @Override
            public void start() {
            }


            @Override
            public boolean visit(final Node node) {
               System.out.println("  visiting node #" + node.getID() + ", features: " + node.getFeaturesCount());

               final List<PointFeature> nodeFeatures = node.getFeatures();
               for (final PointFeature feature : nodeFeatures) {
                  removeIfPresent(expected, feature);
               }
               return true;
            }


            @Override
            public void stop() {
            }
         });

         Assert.assertEquals(0, expected.size());
      }


   }


   @Test
   public void testBitmap() throws IOException {

      final int count = 15_000;
      System.out.println("Saving " + count + " features...");

      final int maxBufferSize = 1024;
      final int maxFeaturesPerNode = 1024;

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.createEmpty(_sector, _directory, _name, maxBufferSize,
               maxFeaturesPerNode)) {
         for (int i = 0; i < count; i++) {
            addSampleFeature(storage, i);
         }
      }

      System.out.println("Testing " + count + " features...");

      try (final PointFeatureStorage storage = PointFeatureMapDBStorage.openReadOnly(_directory, _name)) {

         storage.acceptDepthFirstVisitor(new PointFeatureStorage.NodeVisitor() {
            private GEOBitmap _geoBitmap;


            @Override
            public void start() {
               _geoBitmap = new GEOBitmap(Sector.FULL_SPHERE, 4096, 2048, Color.BLACK);
            }


            @Override
            public boolean visit(final Node node) {
               System.out.println("  visiting node #" + node.getID() + ", features: " + node.getFeaturesCount());

               _geoBitmap.drawSector(node.getNodeSector(), new Color(1, 1, 1, 0.1f), new Color(1, 1, 1, 0.4f));
               _geoBitmap.drawSector(node.getMinimumSector(), new Color(0, 1, 1, 0.1f), new Color(0, 1, 1, 0.4f));

               final List<PointFeature> nodeFeatures = node.getFeatures();
               for (final PointFeature feature : nodeFeatures) {
                  _geoBitmap.drawPoint(feature._position, 2, 2, new Color(1, 1, 0, 0.5f));
               }
               return true;
            }


            @Override
            public void stop() {
               try {
                  _geoBitmap.save(new File("test.png"));
               }
               catch (final IOException e) {
                  e.printStackTrace();
               }
            }
         });


      }


   }

}
