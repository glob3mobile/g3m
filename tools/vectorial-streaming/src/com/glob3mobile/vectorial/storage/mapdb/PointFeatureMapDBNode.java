

package com.glob3mobile.vectorial.storage.mapdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import com.glob3mobile.geo.Angle;
import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureStorage;
import com.glob3mobile.vectorial.storage.PointFeaturesSet;
import com.glob3mobile.vectorial.storage.QuadKey;
import com.glob3mobile.vectorial.storage.QuadKeyUtils;


class PointFeatureMapDBNode
   implements
      PointFeatureStorage.Node {


   public static void insertFeatures(final PointFeatureMapDBStorage storage,
                                     final QuadKey quadKey,
                                     final PointFeaturesSet featuresSet) {
      final byte[] id = quadKey._id;

      final PointFeatureMapDBNode ancestor = getAncestorOrSameLevel(storage, id);
      if (ancestor != null) {
         // System.out.println("==> found ancestor (" + ancestor.getID() + ") for tile " + toString(id));

         ancestor.mergeFeatures(featuresSet);
         return;
      }

      final List<PointFeatureMapDBNode> descendants = getDescendants(storage, id);
      if ((descendants != null) && !descendants.isEmpty()) {
         splitFeaturesIntoDescendants(storage, quadKey, featuresSet, descendants);
         return;
      }

      final PointFeatureMapDBNode tile = new PointFeatureMapDBNode(storage, id, quadKey._sector, featuresSet);
      tile.rawSave();
   }


   private static List<PointFeatureMapDBNode> getDescendants(final PointFeatureMapDBStorage storage,
                                                             final byte[] id) {
      final byte[] toKey = QuadKeyUtils.append(id, (byte) 9);
      final ConcurrentNavigableMap<byte[], NodeHeader> headers = storage.getNodesHeaders().subMap(id, true, toKey, true);

      final List<PointFeatureMapDBNode> result = new ArrayList<>(headers.size());
      for (final Map.Entry<byte[], NodeHeader> entry : headers.entrySet()) {
         final NodeHeader header = entry.getValue();
         final PointFeatureMapDBNode descendant = new PointFeatureMapDBNode(storage, entry.getKey(), header, null);
         result.add(descendant);
      }
      return result;
   }


   private static PointFeatureMapDBNode getAncestorOrSameLevel(final PointFeatureMapDBStorage storage,
                                                               final byte[] id) {
      // final byte[] ancestorID = storage.getNodesHeaders().ceilingKey(id);
      final byte[] ancestorID = storage.getNodesHeaders().floorKey(id);
      if (ancestorID == null) {
         return null;
      }

      return QuadKeyUtils.hasSamePrefix(id, ancestorID) ? get(storage, ancestorID) : null;
   }


   private static PointFeatureMapDBNode get(final PointFeatureMapDBStorage storage,
                                            final byte[] id) {
      if (id == null) {
         return null;
      }

      final NodeHeader header = storage.getNodesHeaders().get(id);
      if (header == null) {
         return null;
      }

      return new PointFeatureMapDBNode(storage, id, header, null);
   }

   private final PointFeatureMapDBStorage _storage;
   private final byte[]                   _id;
   private final Sector                   _sector;

   private int                            _featuresCount;
   private List<PointFeature>             _features;
   private Geodetic2D                     _averagePosition;


   private PointFeatureMapDBNode(final PointFeatureMapDBStorage storage,
                                 final byte[] id,
                                 final Sector sector,
                                 final PointFeaturesSet featuresSet) {
      _storage = storage;
      _id = id;
      _sector = sector;
      _averagePosition = featuresSet._averagePosition;
      _featuresCount = featuresSet.size();
      _features = featuresSet._features;
   }


   PointFeatureMapDBNode(final PointFeatureMapDBStorage storage,
                         final byte[] id,
                         final NodeHeader header,
                         final List<PointFeature> features) {
      _storage = storage;
      _id = id;
      _sector = header._sector;
      _averagePosition = header._averagePosition;
      _featuresCount = header._featuresCount;
      _features = features;
   }


   private void rawSave() {
      //      for (final Geodetic3D p : _points) {
      //         if (!_sector.contains(p._latitude, p._longitude)) {
      //            throw new RuntimeException("LOGIC ERROR!!");
      //         }
      //      }

      if (getFeatures().size() > _storage.getMaxFeaturesPerNode()) {
         split();
         return;
      }

      //      if (getFeatures().size() > _storage.getMaxFeaturesPerTile()) {
      //         System.out.println("***** logic error, tile " + getID() + " has more points than threshold (" + getFeatures().size() + ">"
      //                            + _storage.getMaxFeaturesPerTile() + ")");
      //      }

      _storage.getNodesHeaders().put(_id, new NodeHeader(getSector(), getAveragePosition(), getFeaturesCount()));

      if (_features == null) {
         throw new RuntimeException("Logic Error");
      }
      _storage.getNodesFeatures().put(_id, _features);
   }


   private void split() {
      final List<PointFeature> features = new ArrayList<>(getFeatures()); // ask for features before removing

      remove();

      final QuadKey key = new QuadKey(_id, _sector);
      final QuadKey[] childrenKeys = key.createChildren();
      for (final QuadKey childKey : childrenKeys) {
         final PointFeaturesSet childPointFeaturesSet = PointFeaturesSet.extractFeatures(childKey._sector, features);
         if (childPointFeaturesSet != null) {
            // System.out.println(">>> tile " + getID() + " split " + childPointFeaturesSet.size() + " points into " + toString(child._id));
            insertFeatures(_storage, childKey, childPointFeaturesSet);
         }
      }

      if (!features.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
   }


   private void remove() {
      _storage.getNodesHeaders().remove(_id);
      _storage.getNodesFeatures().remove(_id);
   }


   @Override
   public List<PointFeature> getFeatures() {
      if (_features == null) {
         _features = loadFeatures();
      }

      return _features;
   }


   private List<PointFeature> loadFeatures() {
      final List<PointFeature> features = _storage.getNodesFeatures().get(_id);

      if ((features == null) || (_featuresCount != features.size())) {
         throw new RuntimeException("Inconsistency in pointsCount");
      }

      return Collections.unmodifiableList(features);
   }


   @Override
   public String getID() {
      return QuadKeyUtils.toIDString(_id);
   }


   @Override
   public Sector getSector() {
      return _sector;
   }


   @Override
   public int getDepth() {
      return _id.length;
   }


   @Override
   public int getFeaturesCount() {
      return _featuresCount;
   }


   @Override
   public Geodetic2D getAveragePosition() {
      return _averagePosition;
   }


   private void mergeFeatures(final PointFeaturesSet newPointFeaturesSet) {
      final int mergedLength = getFeaturesCount() + newPointFeaturesSet.size();
      if (mergedLength > _storage.getMaxFeaturesPerNode()) {
         split(newPointFeaturesSet);
      }
      else {
         updateFromFeatures(newPointFeaturesSet);
      }
   }


   private void split(final PointFeaturesSet newPointFeaturesSet) {
      final List<PointFeature> features = getFeatures(); // ask for features before removing

      remove();

      final int oldFeaturesCount = getFeaturesCount();
      final int newFeaturesSize = newPointFeaturesSet.size();
      final int mergedFeaturesSize = oldFeaturesCount + newFeaturesSize;

      final List<PointFeature> mergedFeatures = new ArrayList<>(mergedFeaturesSize);
      mergedFeatures.addAll(features);
      mergedFeatures.addAll(newPointFeaturesSet._features);


      final QuadKey key = new QuadKey(_id, _sector);
      final QuadKey[] children = key.createChildren();
      for (final QuadKey child : children) {
         final PointFeaturesSet childPointFeaturesSet = PointFeaturesSet.extractFeatures(child._sector, mergedFeatures);
         if (childPointFeaturesSet != null) {
            // System.out.println(">>> tile " + getID() + " split " + childPointFeaturesSet.size() + " points into " + toString(child._id));
            insertFeatures(_storage, child, childPointFeaturesSet);
         }
      }

      if (!mergedFeatures.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
   }


   private void updateFromFeatures(final PointFeaturesSet newPointFeaturesSet) {
      final int oldFeaturesCount = getFeaturesCount();
      final int newFeaturesSize = newPointFeaturesSet.size();
      final int mergedFeaturesSize = oldFeaturesCount + newFeaturesSize;

      final List<PointFeature> mergedFeatures = new ArrayList<PointFeature>(mergedFeaturesSize);
      mergedFeatures.addAll(getFeatures());
      mergedFeatures.addAll(newPointFeaturesSet._features);

      final Geodetic2D mergedAveragePosition = weightedAverage( //
               _averagePosition, oldFeaturesCount, //
               newPointFeaturesSet._averagePosition, newFeaturesSize);

      //System.out.println(" merged " + mergedFeaturesSize + " points, old=" + oldFeaturesCount + ", new=" + newFeaturesSize);

      _featuresCount = mergedFeaturesSize;
      _features = mergedFeatures;
      _averagePosition = mergedAveragePosition;

      rawSave();
   }


   private static double weightedAverage(final double value1,
                                         final int count1,
                                         final double value2,
                                         final int count2) {
      return ((value1 * count1) + (value2 * count2)) / (count1 + count2);
   }


   private static Angle weightedAverage(final Angle value1,
                                        final int count1,
                                        final Angle value2,
                                        final int count2) {
      return Angle.fromRadians(weightedAverage(value1._radians, count1, value2._radians, count2));
   }


   private static Geodetic2D weightedAverage(final Geodetic2D value1,
                                             final int count1,
                                             final Geodetic2D value2,
                                             final int count2) {

      final Angle averageLatitude = weightedAverage( //
               value1._latitude, count1, //
               value2._latitude, count2);

      final Angle averageLongitude = weightedAverage( //
               value1._longitude, count1, //
               value2._longitude, count2);

      return new Geodetic2D(averageLatitude, averageLongitude);
   }


   private static List<QuadKey> descendantsQuadKeysOfLevel(final QuadKey key,
                                                           final int level) {
      final List<QuadKey> result = new ArrayList<QuadKey>();
      descendantsQuadKeysOfLevel(result, level, key);
      return result;
   }


   private static void descendantsQuadKeysOfLevel(final List<QuadKey> result,
                                                  final int level,
                                                  final QuadKey key) {
      if (key.getLevel() == level) {
         result.add(key);
      }
      else {
         final QuadKey[] children = key.createChildren();
         for (final QuadKey child : children) {
            descendantsQuadKeysOfLevel(result, level, child);
         }
      }
   }


   private static void splitFeaturesIntoDescendants(final PointFeatureMapDBStorage storage,
                                                    final QuadKey quadKey,
                                                    final PointFeaturesSet featuresSet,
                                                    final List<PointFeatureMapDBNode> descendants) {
      final List<PointFeature> features = new ArrayList<>(featuresSet._features);
      for (final PointFeatureMapDBNode descendant : descendants) {
         final PointFeaturesSet descendantPointFeaturesSet = PointFeaturesSet.extractFeatures(descendant._sector, features);
         if (descendantPointFeaturesSet != null) {
            // System.out.println(">>> tile " + toString(header._id) + " split " + descendantPointFeaturesSet.size()
            // + " points into descendant " + toString(descendant._id) + " (" + points.size()
            // + " points not yet distributed)");

            descendant.getFeatures(); // force features load
            descendant.mergeFeatures(descendantPointFeaturesSet);
            descendant._features = null; // release features' memory
         }
      }

      if (!features.isEmpty()) {
         final List<QuadKey> descendantsQuadKeys = descendantsQuadKeysOfLevel(quadKey, quadKey.getLevel() + 1);
         for (final QuadKey descendantQuadKey : descendantsQuadKeys) {
            final PointFeaturesSet descendantPointFeaturesSet = PointFeaturesSet.extractFeatures(descendantQuadKey._sector,
                     features);
            if (descendantPointFeaturesSet != null) {
               // System.out.println(">>> 2ND tile " + toString(header._id) + " split " + descendantPointFeaturesSet.size()
               // + " points into descendant " + toString(descendantQuadKey._id) + " (" + points.size()
               // + " points not yet distributed)");

               insertFeatures(storage, descendantQuadKey, descendantPointFeaturesSet);
            }
         }

         if (!features.isEmpty()) {
            throw new RuntimeException("Logic error!");
         }
      }
   }


}
