

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


   private static final int  FIXED_SIZE       = SerializerUtils.sectorSerializationSize() + //
                                                SerializerUtils.sectorSerializationSize() + //
                                                SerializerUtils.geodetic2DSerializationSize() + //
                                                4 /* featuresCount:int */;

   private static final long serialVersionUID = 1L;


   @Override
   public void serialize(final DataOutput out,
                         final NodeHeader value) throws IOException {
      final Sector nodeSector = value._nodeSector;
      final Sector minimumSector = value._minimumSector;
      final Geodetic2D averagePosition = value._averagePosition;
      final int featuresCount = value._featuresCount;

      SerializerUtils.serializeSector(out, nodeSector);
      SerializerUtils.serializeSector(out, minimumSector);
      SerializerUtils.serializeGeodetic2D(out, averagePosition);
      out.writeInt(featuresCount);
   }


   @Override
   public NodeHeader deserialize(final DataInput in,
                                 final int available) throws IOException {
      final Sector nodeSector = SerializerUtils.deserializeSector(in);
      final Sector minimumSector = SerializerUtils.deserializeSector(in);
      final Geodetic2D averagePosition = SerializerUtils.deserializeGeodetic2D(in);
      final int featuresCount = in.readInt();
      return new NodeHeader(nodeSector, minimumSector, averagePosition, featuresCount);
   }


   @Override
   public int fixedSize() {
      return FIXED_SIZE;
   }


}
