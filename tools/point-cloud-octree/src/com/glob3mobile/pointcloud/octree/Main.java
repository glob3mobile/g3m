

package com.glob3mobile.pointcloud.octree;


public class Main {
   private static final String SERVER     = "192.168.1.12";
   private static final String DB_NAME    = "TESTING";
   private static final String USER       = "postgres";
   private static final String PASSWORD   = "postgres";

   private static final String CLOUD_NAME = "Loudoun-VA";


   public static void main(final String[] args) {
      System.out.println("PointClout OcTree 0.1");
      System.out.println("---------------------\n");


      cleanupOctree();


      final boolean createIfNotExists = true;
      final PersistentOctree octree = PostgreSQLOctree.get(SERVER, DB_NAME, USER, PASSWORD, CLOUD_NAME, createIfNotExists);
      octree.close();


      System.out.println("\n- done!");
   }


   private static void cleanupOctree() {
      final boolean createIfNotExists = true;
      final PersistentOctree octree = PostgreSQLOctree.get(SERVER, DB_NAME, USER, PASSWORD, CLOUD_NAME, createIfNotExists);
      octree.remove();
      octree.close();
   }


}
