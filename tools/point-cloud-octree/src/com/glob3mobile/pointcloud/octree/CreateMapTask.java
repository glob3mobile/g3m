

package com.glob3mobile.pointcloud.octree;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.glob3mobile.pointcloud.octree.PersistentOctree.Node;
import com.glob3mobile.pointcloud.octree.PersistentOctree.Statistics;
import com.glob3mobile.utils.Sector;
import com.glob3mobile.utils.Utils;

import es.igosoftware.euclid.colors.GColorF;
import es.igosoftware.util.GProgress;


class CreateMapTask
         implements
            PersistentOctree.Visitor {

   private final GProgress _progress;
   private final Sector    _mapSector;
   private final int       _imageWidth;
   private final int       _imageHeight;
   private BufferedImage   _image;
   private Graphics2D      _g;
   private final int       _minPointsPerNode;
   private final int       _maxPointsPerNode;


   CreateMapTask(final String sourceCloudName,
                 final Statistics statistics,
                 final int imageWidth) {
      _progress = new GProgress(statistics.getPointsCount(), true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println("- drawing map \"" + sourceCloudName + "\" "
                               + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };
      _mapSector = statistics.getSector();
      _minPointsPerNode = statistics.getMinPointsPerNode();
      _maxPointsPerNode = statistics.getMaxPointsPerNode();

      _imageWidth = imageWidth;
      final float ratio = (float) (_mapSector._deltaLatitude._radians / _mapSector._deltaLongitude._radians);
      _imageHeight = Math.round(imageWidth * ratio);
   }


   @Override
   public void start() {
      _image = new BufferedImage(_imageWidth, _imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
      _g = _image.createGraphics();
   }


   @Override
   public boolean visit(final Node node) {
      final Sector nodeSector = node.getSector();

      final int pointsCount = node.getPointsCount();

      final int deltaPoints = _maxPointsPerNode - _minPointsPerNode;
      final float alpha = (float) (pointsCount - _minPointsPerNode) / deltaPoints;
      final GColorF color = GColorF.BLACK.mixedWidth(GColorF.WHITE, alpha);


      final int xFrom = Math.round((float) (_mapSector.getUCoordinate(nodeSector._lower._longitude) * _imageWidth));
      final int yFrom = Math.round((float) (_mapSector.getVCoordinate(nodeSector._upper._latitude) * _imageHeight));
      final int xTo = Math.round((float) (_mapSector.getUCoordinate(nodeSector._upper._longitude) * _imageWidth));
      final int yTo = Math.round((float) (_mapSector.getVCoordinate(nodeSector._lower._latitude) * _imageHeight));
      final int width = xTo - xFrom;
      final int height = yTo - yFrom;
      _g.setColor(Utils.toAWTColor(color));
      _g.fillRect(xFrom, yFrom, width, height);

      _g.setColor(Utils.toAWTColor(GColorF.YELLOW));
      _g.drawRect(xFrom, yFrom, width, height);

      //         _g.drawString("" + pointsCount, (xTo + xFrom) / 2, (yTo + yFrom) / 2);

      _progress.stepsDone(pointsCount);

      final boolean keepVisiting = true;
      return keepVisiting;
   }


   @Override
   public void stop() {
      _g.dispose();

      try {
         ImageIO.write(_image, "png", new File("_DEBUG_ot_map.png"));
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }

      _progress.finish();
   }

}
