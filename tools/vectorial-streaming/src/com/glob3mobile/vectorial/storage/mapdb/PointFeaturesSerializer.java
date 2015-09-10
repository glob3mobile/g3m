

package com.glob3mobile.vectorial.storage.mapdb;

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


public class PointFeaturesSerializer
   implements
      Serializer<List<PointFeature>>,
      Serializable {


   private static final long serialVersionUID = 1L;


   @Override
   public void serialize(final DataOutput out,
                         final List<PointFeature> features) throws IOException {
      final int size = features.size();
      out.writeInt(size);

      for (final PointFeature feature : features) {
         SerializerUtils.serializeGeodetic2D(out, feature._position);
         SerializerUtils.serializeMap(out, feature._properties);
      }
   }


   @Override
   public List<PointFeature> deserialize(final DataInput in,
                                         final int available) throws IOException {
      final int size = in.readInt();

      final List<PointFeature> features = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
         final Geodetic2D position = SerializerUtils.deserializeGeodetic2D(in);
         final Map<String, Object> properties = SerializerUtils.deserializeMap(in);
         final PointFeature feature = new PointFeature(properties, position);
         features.add(feature);
      }

      return features;
   }


   @Override
   public int fixedSize() {
      return -1;
   }


}
