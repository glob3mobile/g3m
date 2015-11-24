

package com.glob3mobile.vectorial.storage.mapdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.BTreeMap;

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


   private static int PROPERTIES_KEY;


   private static class MapDBFeaturesSet {


      public final List<MapDBFeature> _features;
      public final Sector             _minimumSector;


      private MapDBFeaturesSet(final List<MapDBFeature> features,
                               final Sector minimumSector) {
         _features = features;
         _minimumSector = minimumSector;
      }


      private int size() {
         return _features.size();
      }


      private static MapDBFeaturesSet extractFeatures(final Sector sector,
                                                      final List<MapDBFeature> features) {
         double minLatRad = Double.POSITIVE_INFINITY;
         double minLonRad = Double.POSITIVE_INFINITY;
         double maxLatRad = Double.NEGATIVE_INFINITY;
         double maxLonRad = Double.NEGATIVE_INFINITY;

         final List<MapDBFeature> extracted = new ArrayList<>();

         final Iterator<MapDBFeature> iterator = features.iterator();
         while (iterator.hasNext()) {
            final MapDBFeature feature = iterator.next();
            final Geodetic2D point = feature._position;
            if (sector.contains(point)) {
               extracted.add(feature);

               final double latRad = point._latitude._radians;
               final double lonRad = point._longitude._radians;

               if (latRad < minLatRad) {
                  minLatRad = latRad;
               }
               if (latRad > maxLatRad) {
                  maxLatRad = latRad;
               }

               if (lonRad < minLonRad) {
                  minLonRad = lonRad;
               }
               if (lonRad > maxLonRad) {
                  maxLonRad = lonRad;
               }

               iterator.remove();
            }
         }

         if (extracted.isEmpty()) {
            return null;
         }

         return new MapDBFeaturesSet( //
                  extracted, //
                  Sector.fromRadians(minLatRad, minLonRad, maxLatRad, maxLonRad));
      }

   }


   static void insertFeatures(final PointFeatureMapDBStorage storage,
                              final QuadKey quadKey,
                              final PointFeaturesSet featuresSet) {
      final List<MapDBFeature> features = new ArrayList<>(featuresSet._features.size());
      for (final PointFeature pointFeature : featuresSet._features) {
         final Geodetic2D position = pointFeature._position;
         final long propertiesID = saveProperties(storage, pointFeature._properties);
         features.add(new MapDBFeature(position, propertiesID));
      }

      final MapDBFeaturesSet mapDBFeaturesSet = new MapDBFeaturesSet(features, featuresSet._minimumSector);

      insertFeatures(storage, quadKey, mapDBFeaturesSet);
   }


   private static long saveProperties(final PointFeatureMapDBStorage storage,
                                      final Map<String, Object> properties) {
      final BTreeMap<Long, Map<String, Object>> propertiesMap = storage.getPropertiesMap();
      final long key = PROPERTIES_KEY++;
      propertiesMap.put(key, properties);
      return key;
   }


   private static void insertFeatures(final PointFeatureMapDBStorage storage,
                                      final QuadKey quadKey,
                                      final MapDBFeaturesSet featuresSet) {
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

      final Sector nodeSector = quadKey._sector;
      final PointFeatureMapDBNode tile = new PointFeatureMapDBNode(storage, id, nodeSector, featuresSet);
      tile.rawSave();
   }


   private static List<PointFeatureMapDBNode> getDescendants(final PointFeatureMapDBStorage storage,
                                                             final byte[] id) {
      final byte[] toKey = QuadKeyUtils.append(id, (byte) 9);
      final ConcurrentNavigableMap<byte[], NodeHeader> headers = storage.getNodesHeadersMap().subMap(id, true, toKey, true);

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
      final byte[] ancestorID = storage.getNodesHeadersMap().floorKey(id);
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

      final NodeHeader header = storage.getNodesHeadersMap().get(id);
      if (header == null) {
         return null;
      }

      return new PointFeatureMapDBNode(storage, id, header, null);
   }

   private final PointFeatureMapDBStorage _storage;
   private final byte[]                   _id;
   private final Sector                   _nodeSector;
   private Sector                         _minimumSector;

   private int                            _featuresCount;
   private List<MapDBFeature>             _features;


   private PointFeatureMapDBNode(final PointFeatureMapDBStorage storage,
                                 final byte[] id,
                                 final Sector nodeSector,
                                 final MapDBFeaturesSet featuresSet) {
      _storage = storage;
      _id = id;
      _nodeSector = nodeSector;
      _minimumSector = featuresSet._minimumSector;
      _featuresCount = featuresSet.size();
      _features = featuresSet._features;
   }


   PointFeatureMapDBNode(final PointFeatureMapDBStorage storage,
                         final byte[] id,
                         final NodeHeader header,
                         final List<MapDBFeature> features) {
      _storage = storage;
      _id = id;
      _nodeSector = header._nodeSector;
      _minimumSector = header._minimumSector;
      _featuresCount = header._featuresCount;
      _features = features;
   }


   private void rawSave() {
      for (final MapDBFeature feature : _features) {
         if (!_nodeSector.contains(feature._position)) {
            throw new RuntimeException("LOGIC ERROR!!");
         }
         if (!_minimumSector.contains(feature._position)) {
            throw new RuntimeException("LOGIC ERROR!!");
         }
      }

      if (getFeatures().size() > _storage.getMaxFeaturesPerNode()) {
         if (split()) {
            return;
         }
      }

      final NodeHeader header = new NodeHeader(getNodeSector(), getMinimumSector(), getFeaturesCount());
      _storage.getNodesHeadersMap().put(_id, header);

      if (_features == null) {
         throw new RuntimeException("Logic Error");
      }
      _storage.getNodesFeaturesMap().put(_id, _features);
   }


   private void remove() {
      _storage.getNodesHeadersMap().remove(_id);
      _storage.getNodesFeaturesMap().remove(_id);
   }


   private List<MapDBFeature> getMapDBFeatures() {
      if (_features == null) {
         _features = loadFeatures();
      }
      return _features;
   }


   private List<MapDBFeature> loadFeatures() {
      final List<MapDBFeature> features = _storage.getNodesFeaturesMap().get(_id);
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
   public Sector getNodeSector() {
      return _nodeSector;
   }


   @Override
   public Sector getMinimumSector() {
      return _minimumSector;
   }


   @Override
   public int getDepth() {
      return _id.length;
   }


   @Override
   public int getFeaturesCount() {
      return _featuresCount;
   }


   private void mergeFeatures(final MapDBFeaturesSet newPointFeaturesSet) {
      final int mergedLength = getFeaturesCount() + newPointFeaturesSet.size();
      if (mergedLength > _storage.getMaxFeaturesPerNode()) {
         if (split(newPointFeaturesSet)) {
            return;
         }
      }
      updateFromFeatures(newPointFeaturesSet);
   }


   private static class ChildSplitResult {
      private final QuadKey          _key;
      private final MapDBFeaturesSet _featuresSet;


      private ChildSplitResult(final QuadKey key,
                               final MapDBFeaturesSet featuresSet) {
         super();
         _key = key;
         _featuresSet = featuresSet;
      }

   }

   private static final int MAX_SPLIT_DEPTH = 8;


   private List<ChildSplitResult> splitIntoChildren(final List<MapDBFeature> features) {
      return splitIntoChildren(new QuadKey(_id, _nodeSector), features, 0);
   }


   private static List<ChildSplitResult> splitIntoChildren(final QuadKey key,
                                                           final List<MapDBFeature> features,
                                                           final int splitDepth) {
      final int featuresSize = features.size(); // save the size here, to be compare after the features get cleared in MapDBFeaturesSet.extractFeatures()

      final QuadKey[] childrenKeys = key.createChildren();
      final List<ChildSplitResult> result = new ArrayList<>(childrenKeys.length);
      for (final QuadKey childKey : childrenKeys) {
         final MapDBFeaturesSet childPointFeaturesSet = MapDBFeaturesSet.extractFeatures(childKey._sector, features);
         if (childPointFeaturesSet != null) {
            final List<MapDBFeature> childFeatures = childPointFeaturesSet._features;
            if ((childFeatures.size() == featuresSize) && (splitDepth < MAX_SPLIT_DEPTH)) {
               return splitIntoChildren(childKey, childFeatures, splitDepth + 1);
            }
            result.add(new ChildSplitResult(childKey, childPointFeaturesSet));
         }
      }
      if (!features.isEmpty()) {
         throw new RuntimeException("Logic error!");
      }
      if (result.size() == 0) {
         throw new RuntimeException("Logic error!");
      }
      return result;
   }


   private boolean split() {
      final List<MapDBFeature> features = new ArrayList<>(getMapDBFeatures()); // ask for features before removing

      final List<ChildSplitResult> splits = splitIntoChildren(features);
      if (splits.size() == 1) {
         System.out.println("- can't split \"" + getID() + "\" (2)");
         return false;
      }


      remove();
      for (final ChildSplitResult split : splits) {
         insertFeatures(_storage, split._key, split._featuresSet);
      }

      return true;
   }


   private boolean split(final MapDBFeaturesSet newPointFeaturesSet) {
      final List<MapDBFeature> oldFeatures = getMapDBFeatures(); // ask for features before removing

      final int oldFeaturesCount = getFeaturesCount();
      final int newFeaturesSize = newPointFeaturesSet.size();
      final int mergedFeaturesSize = oldFeaturesCount + newFeaturesSize;

      final List<MapDBFeature> mergedFeatures = new ArrayList<>(mergedFeaturesSize);
      mergedFeatures.addAll(oldFeatures);
      mergedFeatures.addAll(newPointFeaturesSet._features);


      final List<ChildSplitResult> splits = splitIntoChildren(mergedFeatures);
      if (splits.size() == 1) {
         System.out.println("- can't split \"" + getID() + "\" (1)");
         return false;
      }


      remove();
      for (final ChildSplitResult split : splits) {
         insertFeatures(_storage, split._key, split._featuresSet);
      }

      return true;
   }


   private void updateFromFeatures(final MapDBFeaturesSet newPointFeaturesSet) {
      final int oldFeaturesCount = getFeaturesCount();
      final int newFeaturesSize = newPointFeaturesSet.size();
      final int mergedFeaturesSize = oldFeaturesCount + newFeaturesSize;

      final List<MapDBFeature> mergedFeatures = new ArrayList<>(mergedFeaturesSize);
      mergedFeatures.addAll(getMapDBFeatures());
      mergedFeatures.addAll(newPointFeaturesSet._features);

      _featuresCount = mergedFeaturesSize;
      _features = mergedFeatures;
      _minimumSector = _minimumSector.mergedWith(newPointFeaturesSet._minimumSector);

      rawSave();
   }


   //   private static double weightedAverage(final double value1,
   //                                         final int count1,
   //                                         final double value2,
   //                                         final int count2) {
   //      return ((value1 * count1) + (value2 * count2)) / (count1 + count2);
   //   }
   //   private static Angle weightedAverage(final Angle value1,
   //                                        final int count1,
   //                                        final Angle value2,
   //                                        final int count2) {
   //      return Angle.fromRadians(weightedAverage(value1._radians, count1, value2._radians, count2));
   //   }
   //   private static Geodetic2D weightedAverage(final Geodetic2D value1,
   //                                             final int count1,
   //                                             final Geodetic2D value2,
   //                                             final int count2) {
   //
   //      final Angle averageLatitude = weightedAverage( //
   //               value1._latitude, count1, //
   //               value2._latitude, count2);
   //
   //      final Angle averageLongitude = weightedAverage( //
   //               value1._longitude, count1, //
   //               value2._longitude, count2);
   //
   //      return new Geodetic2D(averageLatitude, averageLongitude);
   //   }


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
                                                    final MapDBFeaturesSet featuresSet,
                                                    final List<PointFeatureMapDBNode> descendants) {
      final List<MapDBFeature> features = new ArrayList<>(featuresSet._features);
      for (final PointFeatureMapDBNode descendant : descendants) {
         final MapDBFeaturesSet descendantPointFeaturesSet = MapDBFeaturesSet.extractFeatures(descendant._nodeSector, features);
         if (descendantPointFeaturesSet != null) {
            descendant.getFeatures(); // force features load
            descendant.mergeFeatures(descendantPointFeaturesSet);
            descendant._features = null; // release features' memory
         }
      }

      if (!features.isEmpty()) {
         final List<QuadKey> descendantsQKs = descendantsQuadKeysOfLevel(quadKey, quadKey.getLevel() + 1);
         for (final QuadKey descendantQK : descendantsQKs) {
            final MapDBFeaturesSet descendantPointFeaturesSet = MapDBFeaturesSet.extractFeatures(descendantQK._sector, features);
            if (descendantPointFeaturesSet != null) {
               insertFeatures(storage, descendantQK, descendantPointFeaturesSet);
            }
         }

         if (!features.isEmpty()) {
            throw new RuntimeException("Logic error!");
         }
      }
   }


   @Override
   public List<PointFeature> getFeatures() {
      final BTreeMap<Long, Map<String, Object>> propertiesMap = _storage.getPropertiesMap();
      final List<MapDBFeature> mapDBFeatures = getMapDBFeatures();
      final List<PointFeature> result = new ArrayList<>(mapDBFeatures.size());
      for (final MapDBFeature mapDBFeature : mapDBFeatures) {
         final Map<String, Object> properties = propertiesMap.get(mapDBFeature._propertiesID);
         result.add(new PointFeature(properties, mapDBFeature._position));
      }
      return result;
   }


}
