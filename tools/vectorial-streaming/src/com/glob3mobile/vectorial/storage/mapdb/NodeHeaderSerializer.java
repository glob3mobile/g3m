

package com.glob3mobile.vectorial.storage.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.mapdb.Serializer;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class NodeHeaderSerializer
   implements
      Serializer<NodeHeader>,
      Serializable {


   private static final long serialVersionUID = 1L;


   @Override
   public void serialize(final DataOutput out,
                         final NodeHeader value) throws IOException {
      final Sector sector = value._sector;
      final Geodetic2D averagePosition = value._averagePosition;
      final int featuresCount = value._featuresCount;

      SerializerUtils.serialize(out, sector);
      SerializerUtils.serialize(out, averagePosition);
      out.writeInt(featuresCount);
   }


   @Override
   public NodeHeader deserialize(final DataInput in,
                                 final int available) throws IOException {
      final Sector sector = SerializerUtils.deserializeSector(in);
      final Geodetic2D averagePosition = SerializerUtils.deserializeGeodetic2D(in);
      final int featuresCount = in.readInt();
      return new NodeHeader(sector, averagePosition, featuresCount);
   }


   @Override
   public int fixedSize() {
      return SerializerUtils.sectorSerializationSize() + //
             SerializerUtils.geodetic2DSerializationSize() + //
             4 /* featuresCount:int */;
   }


}
