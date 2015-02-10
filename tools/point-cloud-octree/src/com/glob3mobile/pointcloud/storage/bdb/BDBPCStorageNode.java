

package com.glob3mobile.pointcloud.storage.bdb;

import com.glob3mobile.pointcloud.octree.berkeleydb.TileHeader;
import com.sleepycat.je.Transaction;


public class BDBPCStorageNode {

   static void insertPoints(final Transaction txn,
                            final BDBPCStorage storage,
                            final TileHeader header,
                            final PointsSet pointsSet) {
      throw new RuntimeException("TODO!");
   }


   private BDBPCStorageNode() {
   }


}
