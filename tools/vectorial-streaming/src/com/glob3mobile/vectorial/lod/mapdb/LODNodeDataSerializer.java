

package com.glob3mobile.vectorial.lod.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapdb.Serializer;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;
import com.glob3mobile.vectorial.storage.mapdb.SerializerUtils;


public class LODNodeDataSerializer
   implements
      Serializer<LODNodeData>,
      Serializable {


   private static final long serialVersionUID = 1L;


   @Override
   public void serialize(final DataOutput out,
                         final LODNodeData node) throws IOException {
      final List<PointFeatureCluster> clusters = node.getClusters();
      out.writeInt(clusters.size());
      for (final PointFeatureCluster cluster : clusters) {
         SerializerUtils.serializeGeodetic2D(out, cluster._position);
         out.writeLong(cluster._size);
      }

      final List<PointFeature> features = node.getFeatures();
      out.writeInt(features.size());
      for (final PointFeature feature : features) {
         SerializerUtils.serializeGeodetic2D(out, feature._position);
         SerializerUtils.serializeMap(out, feature._properties);
      }

   }


   @Override
   public LODNodeData deserialize(final DataInput in,
                               final int available) throws IOException {
      final int clustersCount = in.readInt();
      final List<PointFeatureCluster> clusters = new ArrayList<>(clustersCount);
      for (int i = 0; i < clustersCount; i++) {
         final Geodetic2D position = SerializerUtils.deserializeGeodetic2D(in);
         final long clusterSize = in.readLong();
         final PointFeatureCluster cluster = new PointFeatureCluster(position, clusterSize);
         clusters.add(cluster);
      }

      final int featuresCount = in.readInt();
      final List<PointFeature> features = new ArrayList<>(featuresCount);
      for (int i = 0; i < featuresCount; i++) {
         final Geodetic2D position = SerializerUtils.deserializeGeodetic2D(in);
         final Map<String, Object> properties = SerializerUtils.deserializeMap(in);
         final PointFeature feature = new PointFeature(properties, position);
         features.add(feature);
      }

      return new LODNodeData(clusters, features);
   }


   @Override
   public int fixedSize() {
      return -1;
   }


}
