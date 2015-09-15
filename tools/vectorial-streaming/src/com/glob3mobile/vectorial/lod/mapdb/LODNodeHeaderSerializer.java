

package com.glob3mobile.vectorial.lod.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.mapdb.Serializer;

import com.glob3mobile.geo.Sector;
import com.glob3mobile.vectorial.storage.mapdb.SerializerUtils;


public class LODNodeHeaderSerializer
   implements
      Serializer<LODNodeHeader>,
      Serializable {


   private static final long serialVersionUID = 1L;


   private static final int  FIXED_SIZE       = //
                                              SerializerUtils.sectorSerializationSize() + /* nodeSector */
                                              SerializerUtils.sectorSerializationSize() + /* minimumSector */
                                              4 + /*  clustersCount */
                                              4 /* featuresCount */;


   @Override
   public void serialize(final DataOutput out,
                         final LODNodeHeader node) throws IOException {
      SerializerUtils.serializeSector(out, node.getNodeSector());
      SerializerUtils.serializeSector(out, node.getMinimumSector());
      out.writeInt(node.getClustersCount());
      out.writeInt(node.getFeaturesCount());
   }


   @Override
   public LODNodeHeader deserialize(final DataInput in,
                                 final int available) throws IOException {
      final Sector nodeSector = SerializerUtils.deserializeSector(in);
      final Sector minimumSector = SerializerUtils.deserializeSector(in);
      final int clustersCount = in.readInt();
      final int featuresCount = in.readInt();
      return new LODNodeHeader(nodeSector, minimumSector, clustersCount, featuresCount);
   }


   @Override
   public int fixedSize() {
      return FIXED_SIZE;
   }


}
