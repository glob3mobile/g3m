package com.glob3mobile.server.tools.pyramid;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.glob3mobile.server.tools.GEOSector;
import com.glob3mobile.server.tools.Pyramid;
import com.glob3mobile.server.tools.WGS84Pyramid;
import com.glob3mobile.server.tools.WebMercatorPyramid;
import com.glob3mobile.utils.BilUtils;
import com.glob3mobile.utils.BilUtils.MaxMinBufferedImage;
import com.glob3mobile.utils.CollectionsUtils;
import com.glob3mobile.utils.Function;
import com.glob3mobile.utils.Progress;

public class BilMergedPyramid {
	private final SourcePyramid[] _sourcePyramids;
	private static int _type;

	private static int BIL_DIM = 16;

	   private static class MergedTile {
	      private final MergedColumn            _column;
	      private final int                     _row;
	      private final List<SourcePyramidTile> _sourceTiles = new ArrayList<>();


	      private MergedTile(final MergedColumn column,
	                         final int row) {
	         _column = column;
	         _row = row;
	      }


	      private void addSourceTile(final SourcePyramidTile sourceTile) {
	         _sourceTiles.add(sourceTile);
	      }


	      private MaxMinBufferedImage createImage(final List<File> sourceImageFiles) throws IOException {
	         if (sourceImageFiles.isEmpty()) {
	            return null;
	         }
	         else if (sourceImageFiles.size() == 1) {
	            return BilUtils.BilFileMaxMinToBufferedImage(sourceImageFiles.get(0).getAbsolutePath(),BIL_DIM,BIL_DIM);
	         }
	         else {
	            BufferedImage image = null;
	            short max = Short.MIN_VALUE;
	            short min = Short.MAX_VALUE;
	            short withChildren = 0;
	            Graphics2D g2d = null;
	            for (final File sourceImageFile : sourceImageFiles) {

	               final MaxMinBufferedImage sourceImage = BilUtils.BilFileMaxMinToBufferedImage(sourceImageFile.getAbsolutePath(), BIL_DIM, BIL_DIM);
	               if (g2d == null) {
	                  image = new BufferedImage(sourceImage._image.getWidth(), sourceImage._image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	                  g2d = image.createGraphics();
	               }

	               g2d.drawImage(sourceImage._image, 0, 0, null);
	               max = (short) Math.max(max, sourceImage._max);
	               min = (short) Math.min(min, sourceImage._min);
	               //One from chosen with children, all with children, I guess ...
	               withChildren = (short) Math.max(withChildren, sourceImage._childrenData);
	            }
	            if (g2d != null) {
	               g2d.dispose();
	            }

	            return new MaxMinBufferedImage(image,min,max,withChildren,(short) 0);
	         }
	      }


	      private String getRelativeFileName() {
	         return _column.getRelativeFileName() + "/" + _row + ".bil";
	      }


	      private void process(final SourcePyramid[] sourcePyramids,
	                           final File outputDirectory,
	                           final Object mutex) throws IOException {
	         final File output = new File(outputDirectory, getRelativeFileName());

	         Collections.sort( //
	                  _sourceTiles, //
	                  new Comparator<SourcePyramidTile>() {
	                     @Override
	                     public int compare(final SourcePyramidTile o1,
	                                        final SourcePyramidTile o2) {
	                        return Integer.compare(o1.getPyramidMaxLevel(), o2.getPyramidMaxLevel());
	                     }
	                  });

	         // all sourcePyramids contributed to the tile, just mix the images
	         if (_sourceTiles.size() == sourcePyramids.length) {
	            mergeFromSourceTiles(output, mutex);
	         }
	         else {
	            final List<SourcePyramidTile> ancestors = new ArrayList<>();
	            for (final SourcePyramid sourcePyramid : sourcePyramids) {
	               if (!sourcePyramidContributed(sourcePyramid)) {
	                  final SourcePyramidTile ancestor = sourcePyramid.getBestAncestor(_column._level._level, _column._column, _row);
	                  if (ancestor != null) {
	                     ancestors.add(ancestor);
	                  }
	               }
	            }

	            if (ancestors.isEmpty()) {
	               // no ancestors for this tile 
	               mergeFromSourceTiles(output, mutex);
	            }
	            else if ((_sourceTiles.size() == 1) && isFullOpaque(BilUtils.BilFileMaxMinToBufferedImage(_sourceTiles.get(0).getImageFile().getAbsolutePath(),BIL_DIM,BIL_DIM)._image)) {
	               mergeFromSourceTiles(output, mutex);
	            }
	            else {
	               //               Logger.log("Found ancestors for " + _column._level._level + "/" + _column._column + "/" + _row);
	               //               for (final SourcePyramidTile ancestor : ancestors) {
	               //                  Logger.log("  Ancestor " + ancestor.getImageFile());
	               //               }

	               final Comparator<SourcePyramidTile> comparator = new Comparator<SourcePyramidTile>() {
	                  @Override
	                  public int compare(final SourcePyramidTile o1,
	                                     final SourcePyramidTile o2) {
	                     return Integer.compare(o1.getPyramidMaxLevel(), o2.getPyramidMaxLevel());
	                  }
	               };
	               Collections.sort(ancestors, comparator);
	               mergeFromSourceTilesAndAncestors(ancestors, output, mutex);
	            }
	         }
	      }


	      private void mergeFromSourceTilesAndAncestors(final List<SourcePyramidTile> ancestors,
	                                                    final File output,
	                                                    final Object mutex) throws IOException {
	         //Logger.log("    Merging tile \"" + output.getAbsolutePath() + "\"");

	         final List<MaxMinBufferedImage> sourceImageFiles = CollectionsUtils.map( //
	                  _sourceTiles, //
	                  new Function<SourcePyramidTile, MaxMinBufferedImage>() {
	                     @Override
	                     public MaxMinBufferedImage evaluate(final SourcePyramidTile sourceTile) {
	                        try {
	                           return BilUtils.BilFileMaxMinToBufferedImage(sourceTile.getImageFile().getAbsolutePath(),BIL_DIM,BIL_DIM);
	                        }
	                        catch (final Exception e) {
	                           throw new RuntimeException(e);
	                        }
	                     }
	                  });

	         final MaxMinBufferedImage firstImage = sourceImageFiles.get(0);

	         final BufferedImage image = new BufferedImage(firstImage._image.getWidth(), firstImage._image.getHeight(),
	                  BufferedImage.TYPE_INT_ARGB);
	         short max = firstImage._max;
	         short min = firstImage._min;
	         short withChildren = firstImage._childrenData;

	         final Graphics2D g2d = image.createGraphics();
	         g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	         GEOSector tileSector = null;
	         switch (_type){
		         case Pyramid.PYR_WGS84:
		        	 tileSector = WGS84Pyramid.sectorFor(_column._level._level, _column._column, _row);
		        	 break;
		         case Pyramid.PYR_WEBMERC:
		        	 tileSector = WebMercatorPyramid.sectorFor(_column._level._level, _column._column, _row);
		        	 break;
	             default:
	        		 System.out.println("Failure: uninitialized pyramid type");
	        		 System.exit(-1);
	         }

	         for (final SourcePyramidTile ancestor : ancestors) {
	            final MaxMinBufferedImage ancestorImage = BilUtils.BilFileMaxMinToBufferedImage(ancestor.getImageFile().getAbsolutePath(),BIL_DIM,BIL_DIM);

	            GEOSector ancestorSector = null;
	            switch (_type){
			         case Pyramid.PYR_WGS84:
			        	 ancestorSector = WGS84Pyramid.sectorFor(ancestor._column._level._level, ancestor._column._column,
			                     ancestor._row);
			        	 break;
			         case Pyramid.PYR_WEBMERC:
			        	 ancestorSector = WebMercatorPyramid.sectorFor(ancestor._column._level._level, ancestor._column._column,
			                     ancestor._row);
			        	 break;
		             default:
		        		 System.out.println("Failure: uninitialized pyramid type");
		        		 System.exit(-1);
		         }

	            final Point2D lowerUV = ancestorSector.getUVCoordinates(tileSector._lower);
	            final Point2D upperUV = ancestorSector.getUVCoordinates(tileSector._upper);

	            final int ancestorImageWidth = ancestorImage._image.getWidth();
	            final int ancestorImageHeight = ancestorImage._image.getHeight();

	            final int dx1 = 0;
	            final int dy1 = 0;
	            final int dx2 = BIL_DIM;
	            final int dy2 = BIL_DIM;
	            final int sx1 = (int) Math.round(lowerUV.getX() * ancestorImageWidth);
	            final int sy2 = (int) Math.round(lowerUV.getY() * ancestorImageHeight);
	            final int sx2 = (int) Math.round(upperUV.getX() * ancestorImageWidth);
	            final int sy1 = (int) Math.round(upperUV.getY() * ancestorImageHeight);
	            g2d.drawImage(ancestorImage._image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	            max = (short) Math.max(max,ancestorImage._max);
	            min = (short) Math.min(min, ancestorImage._min);
	            //Ancestors should not be taken into account for own children info.

	         }

	         for (final MaxMinBufferedImage sourceImage : sourceImageFiles) {
	            g2d.drawImage(sourceImage._image, 0, 0, null);
	            max = (short) Math.max(max,sourceImage._max);
	            min = (short) Math.min(min, sourceImage._min);
	            withChildren = (short) Math.max(withChildren, sourceImage._childrenData);
	         }

	         g2d.dispose();

	         saveImage(output, new MaxMinBufferedImage(image,min,max,withChildren,(short) 0), mutex);
	      }


	      private void mergeFromSourceTiles(final File output,
	                                        final Object mutex) throws IOException {
	         
	         final List<File> sourceImageFiles = CollectionsUtils.map( //
	                  _sourceTiles, //
	                  new Function<SourcePyramidTile, File>() {
	                     @Override
	                     public File evaluate(final SourcePyramidTile sourceTile) {
	                        return sourceTile.getImageFile();
	                     }
	                  });

	         saveImage(output, createImage(sourceImageFiles), mutex);
	      }


	      private boolean sourcePyramidContributed(final SourcePyramid sourcePyramid) {
	         for (final SourcePyramidTile sourceTile : _sourceTiles) {
	            if (sourceTile._column._level._pyramid == sourcePyramid) {
	               return true;
	            }
	         }
	         return false;
	      }
	   }


	   private static class MergedColumn {
	      private final MergedLevel              _level;
	      private final int                      _column;
	      private final Map<Integer, MergedTile> _tiles = new HashMap<>();


	      MergedColumn(final MergedLevel level,
	                   final int column) {
	         _level = level;
	         _column = column;
	      }


	      private void addSourceTile(final SourcePyramidTile sourceTile) {
	         final Integer rowKey = sourceTile._row;
	         MergedTile current = _tiles.get(rowKey);
	         if (current == null) {
	            current = new MergedTile(this, rowKey);
	            _tiles.put(rowKey, current);
	         }
	         current.addSourceTile(sourceTile);
	      }


	      private void process(final SourcePyramid[] sourcePyramids,
	                           final File outputDirectory,
	                           final Progress progress,
	                           final ExecutorService executor,
	                           final Object mutex) {

	         final List<Integer> keys = new ArrayList<>(_tiles.keySet());
	         Collections.sort(keys);

	         for (final Integer key : keys) {
	            final MergedTile tile = _tiles.get(key);

	            executor.execute(new Runnable() {
	               @Override
	               public void run() {
	                  try {
	                     tile.process(sourcePyramids, outputDirectory, mutex);
	                     progress.stepDone();
	                  }
	                  catch (final IOException e) {
	                     e.printStackTrace();
	                  }
	               }
	            });

	         }
	      }


	      private String getRelativeFileName() {
	         return _level.getRelativeFileName() + "/" + _column;
	      }


	      private long getTilesCount() {
	         return _tiles.size();
	      }


	   }


	   private static class MergedLevel {
	      private final int                        _level;
	      private final Map<Integer, MergedColumn> _columns = new HashMap<>();


	      private MergedLevel(final int level) {
	         _level = level;
	      }


	      public void addSourceTile(final SourcePyramidColumn sourceColumn,
	                                final SourcePyramidTile sourceTile) {
	         final Integer columnKey = sourceColumn._column;
	         MergedColumn current = _columns.get(columnKey);
	         if (current == null) {
	            current = new MergedColumn(this, columnKey);
	            _columns.put(columnKey, current);
	         }
	         current.addSourceTile(sourceTile);
	      }


	      private void process(final SourcePyramid[] sourcePyramids,
	                           final File outputDirectory,
	                           final Progress progress,
	                           final ExecutorService executor,
	                           final Object mutex) {

	         final List<Integer> keys = new ArrayList<>(_columns.keySet());
	         Collections.sort(keys);

	         for (final Integer key : keys) {
	            final MergedColumn column = _columns.get(key);
	            column.process(sourcePyramids, outputDirectory, progress, executor, mutex);
	         }
	      }


	      private String getRelativeFileName() {
	         return Integer.toString(_level);
	      }


	      private long getTilesCount() {
	         long tilesCount = 0;
	         for (final MergedColumn column : _columns.values()) {
	            tilesCount += column.getTilesCount();
	         }
	         return tilesCount;
	      }

	   }

	   private final Map<Integer, MergedLevel> _levels = new HashMap<>();


	   public BilMergedPyramid(final SourcePyramid[] sourcePyramids, final int pyramidType) {
	      _sourcePyramids = sourcePyramids;
	      _type = pyramidType;

	      for (final SourcePyramid sourcePyramid : _sourcePyramids) {
	         for (final SourcePyramidLevel sourceLevel : sourcePyramid.getLevels()) {
	            for (final SourcePyramidColumn sourceColumn : sourceLevel.getColumns()) {
	               for (final SourcePyramidTile sourceTile : sourceColumn.getTiles()) {
	                  addSourceTile(sourceLevel, sourceColumn, sourceTile);
	               }
	            }
	         }
	      }
	   }


	   private void addSourceTile(final SourcePyramidLevel sourceLevel,
	                              final SourcePyramidColumn sourceColumn,
	                              final SourcePyramidTile sourceTile) {
	      final Integer levelKey = sourceLevel._level;
	      MergedLevel current = _levels.get(levelKey);
	      if (current == null) {
	         current = new MergedLevel(levelKey);
	         _levels.put(levelKey, current);
	      }
	      current.addSourceTile(sourceColumn, sourceTile);
	   }


	   public void process(final File outputDirectory,
	                       final Progress progress,
	                       final ExecutorService executor,
	                       final Object mutex) {
	      final List<Integer> keys = new ArrayList<>(_levels.keySet());
	      Collections.sort(keys);

	      for (final Integer key : keys) {

	         final MergedLevel level = _levels.get(key);
	         level.process(_sourcePyramids, outputDirectory, progress, executor, mutex);
	      }
	   }
	   
	   public void similarity(){
		   final List<Integer> keys = new ArrayList<>(_levels.keySet());
		      Collections.sort(keys, Collections.reverseOrder());
		      
		      keys.remove(0); //Ninguno tendrá hijos. Eso supondría una similaridad 0. Esto podría
		      //ser necesario cambiarlo.

		      for (final Integer key : keys) {

		         final MergedLevel level = _levels.get(key);
		         for (final Integer columnKey : level._columns.keySet()){
		        	 final MergedColumn column = level._columns.get(columnKey);
		        	 for (final Integer rowKey : column._tiles.keySet()){
		        		 final MergedTile tile = column._tiles.get(rowKey);
		        		 // Magic goes here
		        		 try {
		        			 String str = tile._sourceTiles.get(0).getImageFile().getAbsolutePath();
		        			 String thePart = level._level + "/" + tile._column._column + "/" + tile._row;
		        			 
		        			 String childPartA = (level._level + 1) + "/" + (tile._column._column * 2) + "/" + (tile._row *2);
		        			 String childPartB = (level._level + 1) + "/" + (tile._column._column * 2 + 1) + "/" + (tile._row *2);
		        			 String childPartC = (level._level + 1) + "/" + (tile._column._column * 2) + "/" + (tile._row *2 + 1);
		        			 String childPartD = (level._level + 1) + "/" + (tile._column._column * 2 + 1) + "/" + (tile._row *2 + 1);
		        			 
		        			 String childA_str = str.replace(thePart,childPartA);
		        			 String childB_str = str.replace(thePart,childPartB);
		        			 String childC_str = str.replace(thePart,childPartC);
		        			 String childD_str = str.replace(thePart,childPartD);
		        			 
		        			 MaxMinBufferedImage parent = BilUtils.BilFileMaxMinToBufferedImage(str, BIL_DIM, BIL_DIM);
		        			 MaxMinBufferedImage childA = BilUtils.BilFileMaxMinToBufferedImage(childA_str, BIL_DIM, BIL_DIM);
		        			 MaxMinBufferedImage childB = BilUtils.BilFileMaxMinToBufferedImage(childB_str, BIL_DIM, BIL_DIM);
		        			 MaxMinBufferedImage childC = BilUtils.BilFileMaxMinToBufferedImage(childC_str, BIL_DIM, BIL_DIM);
		        			 MaxMinBufferedImage childD = BilUtils.BilFileMaxMinToBufferedImage(childD_str, BIL_DIM, BIL_DIM);
		        			 
		        			 double similarity = calculateSimilarity (parent, childA, childB, childC, childD);
		        			 
		        			 System.out.println(str);
		        			 System.out.println("Error medio por vértice (similaridad no completada):"+similarity);
		        			 
		        		 }
		        		 catch (Exception e) {
		        			 
		        		 }
		        		 
		        	 }
		         }
		      }
		      
	   }


	   private static void saveImage(final File output,
	                                 final MaxMinBufferedImage image,
	                                 final Object mutex) throws IOException {
	      synchronized (mutex) {
	         final File directory = output.getParentFile();
	         if (!directory.exists()) {
	            if (!directory.mkdirs()) {
	               throw new IOException("Can't create directory \"" + directory.getAbsolutePath() + "\"");
	            }
	         }
	      }
	      
	      BilUtils.BufferedImageToBilFileMaxMin(image._image, output.getAbsolutePath(), BIL_DIM, BIL_DIM, image._max, image._min, image._childrenData, image._similarity);
	   }


	   private static boolean isFullOpaque(final BufferedImage image) {
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      for (int x = 0; x < width; x++) {
	         for (int y = 0; y < height; y++) {
	            final int rbg = image.getRGB(x, y);
	            final int alpha = (rbg >> 24) & 0xff;
	            if (alpha < 255) {
	               return false;
	            }
	         }
	      }
	      return true;
	   }


	   public long getTilesCount() {
	      long tilesCount = 0;
	      for (final MergedLevel level : _levels.values()) {
	         tilesCount += level.getTilesCount();
	      }
	      return tilesCount;
	   }
	   
	   private double calculateSimilarity(MaxMinBufferedImage parent, MaxMinBufferedImage childA, 
			   MaxMinBufferedImage childB, MaxMinBufferedImage childC, MaxMinBufferedImage childD) {
		   double res = 0;
		   
		   //Misma vertical, restar. Diferente vertical, intuir altura del padre.
		   for (int x=0 ; x < childA._image.getWidth() ; x+=2) for (int y=0; y < childA._image.getHeight(); y+=2) {
			   short childData = (short) ((childA._image.getRGB(x,y)) & 0x0000FFFF);
			   short parentData = (short) ((parent._image.getRGB(x/2,y/2)) & 0x0000FFFF);
			   
			   short childDataY1 = (short) ((childA._image.getRGB(x,y+1)) & 0x0000FFFF);
			   short childDataR1 = (short) ((childA._image.getRGB(x+1,y)) & 0x0000FFFF);
			   short childDataR1Y1 = (short) ((childA._image.getRGB(x+1,y+1)) & 0x0000FFFF);
			   short parentDataY1 = (short) ((parentData + (parent._image.getRGB(x/2,(y/2)+1)) & 0x0000FFFF)/2);
			   short parentDataR1 = (short) ((parentData + (parent._image.getRGB((x/2) + 1,y/2)) & 0x0000FFFF)/2);
			   short parentDataR1Y1 = (short) ((((parent._image.getRGB(x/2,(y/2)+1)) & 0x0000FFFF) + ((parent._image.getRGB((x/2) + 1,y/2)) & 0x0000FFFF))/2) ;
			   
			   res += Math.pow((childData-parentData),2);
			   res += Math.pow((childDataY1-parentDataY1),2);
			   res += Math.pow((childDataR1-parentDataR1),2);
			   res += Math.pow((childDataR1Y1-parentDataR1Y1),2);
		   }
		   
		   res = res / Math.pow(BIL_DIM,2);
		   res = Math.sqrt(res);
		   //TODO: hacer la similaridad invariante a escala.
		   //TODO: repetir para cada tipo de hijo. De lo contrario failure.;
		   
		   return res;
				   
		   
	   }
}
