

package com.glob3mobile.vectorial.cluster.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapdb.Serializer;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.vectorial.cluster.nodes.CInnerNodeData;
import com.glob3mobile.vectorial.cluster.nodes.CLeafNodeData;
import com.glob3mobile.vectorial.cluster.nodes.CNodeData;
import com.glob3mobile.vectorial.storage.PointFeature;
import com.glob3mobile.vectorial.storage.PointFeatureCluster;
import com.glob3mobile.vectorial.storage.mapdb.SerializerUtils;


public class CNodeDataSerializer
   implements
      Serializer<CNodeData>,
      Serializable {


   private static final long serialVersionUID = 1L;


   @Override
   public void serialize(final DataOutput out,
                         final CNodeData node) throws IOException {
      if (node instanceof CLeafNodeData) {
         out.writeBoolean(true);
         serialize(out, (CLeafNodeData) node);
      }
      else if (node instanceof CInnerNodeData) {
         out.writeBoolean(true);
         serialize(out, (CInnerNodeData) node);
      }
      else {
         throw new RuntimeException("Unknown node type: " + node.getClass());
      }
   }


   private void serialize(final DataOutput out,
                          final CLeafNodeData node) throws IOException {
      final List<PointFeature> features = node.getFeatures();
      out.writeInt(features.size());
      for (final PointFeature feature : features) {
         SerializerUtils.serialize(out, feature._position);
         SerializerUtils.serialize(out, feature._properties);
      }
   }


   @Override
   public CNodeData deserialize(final DataInput in,
                                final int available) throws IOException {
      final boolean isLeaf = in.readBoolean();
      return isLeaf ? deserializeLeaf(in) : deserializeInner(in);
   }


   private CLeafNodeData deserializeLeaf(final DataInput in) throws IOException {
      final int size = in.readInt();
      final List<PointFeature> features = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
         final Geodetic2D position = SerializerUtils.deserializeGeodetic2D(in);
         final Map<String, Object> properties = SerializerUtils.deserialize(in);
         final PointFeature feature = new PointFeature(properties, position);
         features.add(feature);
      }
      return new CLeafNodeData(features);
   }


   private void serialize(final DataOutput out,
                          final CInnerNodeData node) throws IOException {
      final List<PointFeatureCluster> clusters = node.getClusters();
      out.writeInt(clusters.size());
      for (final PointFeatureCluster cluster : clusters) {
         SerializerUtils.serialize(out, cluster._position);
         out.writeLong(cluster._size);
      }
   }


   private CInnerNodeData deserializeInner(final DataInput in) throws IOException {
      final int size = in.readInt();
      final List<PointFeatureCluster> clusters = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
         final Geodetic2D position = SerializerUtils.deserializeGeodetic2D(in);
         final long clusterSize = in.readLong();
         final PointFeatureCluster cluster = new PointFeatureCluster(position, clusterSize);
         clusters.add(cluster);
      }
      return new CInnerNodeData(clusters);
   }


   @Override
   public int fixedSize() {
      return -1;
   }


}
