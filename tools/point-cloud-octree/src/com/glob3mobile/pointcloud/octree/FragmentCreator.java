

package com.glob3mobile.pointcloud.octree;

import java.io.File;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;
import com.glob3mobile.utils.Geodetic3D;
import com.glob3mobile.utils.Sector;

import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.util.GUndeterminateProgress;


public class FragmentCreator
implements
PersistentOctree.Visitor {
   private final File             _cloudDirectory;
   private final String           _fragmentCloudName;
   private final Sector           _sector;
   private final long             _cacheSizeInBytes;
   private final int              _maxPointsPerTitle;

   private long                   _totalPointsCount;
   private long                   _nodesCount;
   private long                   _edgesNodes;
   private long                   _fullNodes;
   private GUndeterminateProgress _progress;
   private double                 _totalLatitudeDensity;
   private double                 _totalLongitudeDensity;
   private PersistentOctree       _fragmentOctree;


   public FragmentCreator(final File cloudDirectory,
                          final String fragmentCloudName,
                          final Sector sector,
                          final long cacheSizeInBytes,
                          final int maxPointsPerTitle) {
      _cloudDirectory = cloudDirectory;
      _fragmentCloudName = fragmentCloudName;
      _sector = sector;
      _cacheSizeInBytes = cacheSizeInBytes;
      _maxPointsPerTitle = maxPointsPerTitle;
   }


   @Override
   public void start() {
      _totalPointsCount = 0;
      _nodesCount = 0;
      _fullNodes = 0;
      _edgesNodes = 0;

      _totalLatitudeDensity = 0;
      _totalLongitudeDensity = 0;

      BerkeleyDBOctree.delete(_cloudDirectory, _fragmentCloudName);

      _fragmentOctree = BerkeleyDBOctree.open(_cloudDirectory, _fragmentCloudName, true, _maxPointsPerTitle, _maxPointsPerTitle,
               _cacheSizeInBytes);

      _progress = new GUndeterminateProgress(10, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println("- creating \"" + _fragmentCloudName + "\"" + progressString(stepsDone, elapsed));
         }
      };
   }


   private static GVector2D getDensity(final Sector nodeSector,
                                       final int pointsCount) {
      return new GVector2D( //
               nodeSector._deltaLongitude._radians / pointsCount, //
               nodeSector._deltaLatitude._radians / pointsCount);
   }


   private static GVector2D getDensity(final PersistentOctree.Node node) {
      return getDensity(node.getSector(), node.getPointsCount());
   }


   @Override
   public boolean visit(final PersistentOctree.Node node) {

      final Sector nodeSector = node.getSector();
      _nodesCount++;

      // System.out.println("-> " + node.getID() + ", points=" + node.getPointsCount() + ", sector=" + nodeSector);

      if (_sector.fullContains(nodeSector)) {
         _fullNodes++;
         _totalPointsCount += node.getPointsCount();

         final GVector2D density = getDensity(node);
         _totalLatitudeDensity += density._y;
         _totalLongitudeDensity += density._x;

         for (final Geodetic3D point : node.getPoints()) {
            _fragmentOctree.addPoint(point);
         }
      }
      else if (_sector.touchesWith(nodeSector)) {
         _edgesNodes++;

         final GVector2D density = getDensity(node);
         _totalLatitudeDensity += density._y;
         _totalLongitudeDensity += density._x;

         for (final Geodetic3D point : node.getPoints()) {
            if (_sector.contains(point._latitude, point._longitude)) {
               _totalPointsCount++;
               _fragmentOctree.addPoint(point);
            }
         }
      }

      _progress.stepDone();

      // final boolean keepVisiting = _nodesCount < 50;
      final boolean keepVisiting = true;
      return keepVisiting;
   }


   @Override
   public void stop() {
      _fragmentOctree.close();
      _fragmentOctree = null;


      _progress.finish();
      _progress = null;


      System.out.println("== Total points: " + _totalPointsCount + //
                         ", nodes: " + _nodesCount + //
                         ", full: " + _fullNodes + //
               ", edges: " + _edgesNodes);

      final double averageLatitudeDensity = _totalLatitudeDensity / (_fullNodes + _edgesNodes);
      final double averageLongitudeDensity = _totalLongitudeDensity / (_fullNodes + _edgesNodes);

      System.out.println("== Avr Density=" + averageLatitudeDensity + " / " + averageLongitudeDensity);

      try (PersistentOctree targetOctree = BerkeleyDBOctree.openReadOnly(_cloudDirectory, _fragmentCloudName, _cacheSizeInBytes)) {
         final PersistentOctree.Statistics statistics = targetOctree.getStatistics(true);
         statistics.show();
      }
   }
}
