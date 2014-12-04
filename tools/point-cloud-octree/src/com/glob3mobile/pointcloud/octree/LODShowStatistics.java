

package com.glob3mobile.pointcloud.octree;


import es.igosoftware.util.GStringUtils;


final class LODShowStatistics
implements
PersistentLOD.Visitor {
   private long _pointsCount;
   private long _nodesCount;


   @Override
   public void start(final PersistentLOD.Transaction transaction) {
      _pointsCount = 0;
      _nodesCount = 0;
   }


   @Override
   public void stop(final PersistentLOD.Transaction transaction) {
      final float pointPerNode = (float) _pointsCount / _nodesCount;
      System.out.println("== total points=" + _pointsCount + ", nodes=" + _nodesCount + ", point/node=" + pointPerNode);
   }


   @Override
   public boolean visit(final PersistentLOD.Transaction transaction,
                        final PersistentLOD.Node node) {
      //final List<Geodetic3D> points = node.getPoints(transaction);
      //      for (final PersistentLOD.NodeLevel level : node.getLevels()) {
      //         final List<Geodetic3D> points = level.getPoints(transaction);
      //      }

      final int pointsCount = node.getPointsCount();
      final double value = pointsCount * 3 * 8;
      System.out.println("[" + node.getID() + "]" + //
                         " depth=" + node.getDepth() + //
               " points=" + pointsCount + //
               " estimatesSize=" + GStringUtils.getSpaceMessage(value));
      _pointsCount += pointsCount;
      _nodesCount++;
      return true;
   }


}
