

package com.glob3mobile.pointcloud.octree;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;


public class ProcessOT {

   public static void main(final String[] args) {
      System.out.println("ProcessOT 0.1");
      System.out.println("-------------\n");

      final String cloudName = "Loudoun-VA";

      final boolean createIfNotExists = true;
      try (final PersistentOctree octree = BerkeleyDBOctree.open(cloudName, createIfNotExists)) {

         final PersistentOctree.Statistics statistics = octree.getStatistics(true);
         statistics.show();
      }
   }


}
