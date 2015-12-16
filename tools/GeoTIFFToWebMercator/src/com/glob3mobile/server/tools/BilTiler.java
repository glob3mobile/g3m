package com.glob3mobile.server.tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.glob3mobile.utils.BilUtils;
import com.glob3mobile.utils.IOUtils;
import com.glob3mobile.utils.Logger;

/**
 * First approximation:
 * This class should be as the one for GeoTiff, but using Bil images instead.
 * Further refactoring and improvement will be applied.
 * 
 */

public class BilTiler {
	
	private final File _inputFile;
	private final File _outputDirectory;
	
	private void processTile(final GEOImage geoImage,
            final Pyramid pyramid,
            final GEOTile tile,
            final int maxLevel,
            final Level[] levels) {
		
		final int levelInt = tile._level;
		if (levelInt <= maxLevel) {
			if (tile._sector.touchesWith(geoImage._sector)) {
				final Level level = levels[levelInt];
				if (level != null) 
					level.processTile(tile);
		
				if (levelInt < maxLevel) {
					final List<GEOTile> children = pyramid.createChildren(geoImage._sector, tile);
					for (final GEOTile child : children) 
						processTile(geoImage, pyramid, child, maxLevel, levels);
				}
			}
		}
	}
	
	private static class GeoData {
		public double lowerlat, lowerlon, upperlat, upperlon;
		public int columns, rows;
		
		public GeoData(double lowlat, double upplat, double lowlon, double upplon, int rows, int cols){
			this.rows = rows;
			columns = cols;
			lowerlat = lowlat;
			upperlat = upplat;
			lowerlon = lowlon;
			upperlon = upplon;
		}
	}
	
	private static GEOImage read(final File inputFile, final GeoData data) throws IOException {

		final GEOSector sector = new GEOSector (new GEOGeodetic(data.lowerlat,data.lowerlon), new GEOGeodetic(data.upperlat,data.upperlon) );
		final BufferedImage image = BilUtils.BilFileToBufferedImage(inputFile.getAbsolutePath(), data.rows, data.columns);
		
		return new GEOImage(sector, image);	
	}

	private BilTiler(final String inputFileName, final String outputDirectoryName) throws IOException {
		_inputFile = new File(inputFileName);
		if (!_inputFile.exists()) {
			throw new IOException("\"" + inputFileName + "\" not found!");
		}
		_outputDirectory = new File(outputDirectoryName);
		IOUtils.ensureEmptyDirectory(_outputDirectory);
	}
	
	public static void convertFile(final String inputFileName, final GeoData data, int pyramidType,
            final String outputDirectoryName) throws IOException {
		
		final BilTiler converter = new BilTiler(inputFileName, outputDirectoryName);
		converter.process(data,pyramidType);
	}
	
	public static Level[] levels;
	
	public static void updateParentsStartingOnLevel(int level, int row, int column, short max, short min){

		int rowToUpdate = row; 
		int columnToUpdate = column;
		short maxToUpdate = max;
		short minToUpdate = min;
		
		for (int i=level-1; i >= 0; i--){
			Level currentLevel = levels[i];
			rowToUpdate = (int) Math.floor(rowToUpdate/2);
			columnToUpdate = (int) Math.floor(columnToUpdate/2);
			
			ArrayList<GEOTile> tiles = currentLevel._tiles;
			//TODO: If tiles are been ordered according to something, the following loop could be changed.
			
			for (int x=0; x<tiles.size();x++){
				GEOTile tile = tiles.get(x);
				if (tile._row == rowToUpdate && tile._column == columnToUpdate){
					tile.updateMaxMinValues(maxToUpdate, minToUpdate,true);
					maxToUpdate = tile._maxValue;
					minToUpdate = tile._minValue;
					break;
				}
			}
		}
	}
	
	private void process(GeoData data, final int pyramidType) throws IOException {
		
		  final GEOImage geoImage = read(_inputFile,data);
		  

	      final int minLevel = 0;
	      final int maxLevel = Pyramid.bestLevelForResolution(geoImage._resolution.getX(), geoImage._resolution.getY());
	      
	      Logger.log("MaxLevel: " + maxLevel);

	      //final Level[] levels = new Level[maxLevel + 1];
	      levels = new Level[maxLevel + 1];
	      for (int i = 0; i <= maxLevel; i++) {
	         levels[i] = (i < minLevel) ? null : new Level(i);
	         
	      }

	      //This one will be for DEM purposes.
	      Pyramid pyramid = null;
	      switch (pyramidType){
		      case Pyramid.PYR_WGS84:
		    	  pyramid = new WGS84Pyramid();
		    	  break;
		      case Pyramid.PYR_WEBMERC:
		    	  pyramid = new WebMercatorPyramid();
		    	  break;
	      }

	      for (final GEOTile tile : pyramid._topTiles) {
	         processTile(geoImage, pyramid, tile, maxLevel, levels);
	      }
	      

	      for (final Level level : levels) {
	         if (level != null) {
	            level.initialize();
	         }
	      }
	      

	      BufferedImage currentImage = geoImage._bufferedImage;
	      for (int i = maxLevel; i >= minLevel; i--) {
	         final Level level = levels[i];
	         currentImage = level.process(_outputDirectory, geoImage._sector, currentImage);
	         if (currentImage == null) {
	            break;
	         }
	         levels[i] = null; // release some memory
	      }

	      Logger.log("done!");
	   }
	
	
	public static void main(final String[] args) throws IOException {
	      System.out.println("BilTiler 0.1");
	      System.out.println("----------------\n");
	      
	      String inputName, outputDirectoryName;
	      GeoData data;
	      
	      int pyramidType = Pyramid.PYR_WEBMERC;
		  Pyramid.setTileImageDimensions(32, 32);
		  Pyramid.setTopSectorSplits(1, 1);
	      
	      System.out.println(" --- Cañón del colorado --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/elev.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/elev.biltiles"; 
	      data = new GeoData(36,36.5,-112,-111.5,3000,3000);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      
	      System.out.println(" --- Tierra A --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/a.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/a.biltiles"; 
	      data = new GeoData(50,90,-180,-90,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra B --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/b.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/b.biltiles"; 
	      data = new GeoData(50,90,-90,0,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra C --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/c.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/c.biltiles"; 
	      data = new GeoData(50,90,0,90,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra D --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/d.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/d.biltiles"; 
	      data = new GeoData(50,90,90,180,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      
	      System.out.println(" --- Tierra E --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/e.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/e.biltiles"; 
	      data = new GeoData(0,50,-180,-90,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra F --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/f.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/f.biltiles"; 
	      data = new GeoData(0,50,-90,0,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra G --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/g.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/g.biltiles"; 
	      data = new GeoData(0,50,0,90,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra H --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/h.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/h.biltiles"; 
	      data = new GeoData(0,50,90,180,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      
	      System.out.println(" --- Tierra I --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/i.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/i.biltiles"; 
	      data = new GeoData(-50,0,-180,-90,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra J --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/j.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/j.biltiles"; 
	      data = new GeoData(-50,0,-90,0,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra K --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/k.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/k.biltiles"; 
	      data = new GeoData(-50,0,0,90,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra L --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/l.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/l.biltiles"; 
	      data = new GeoData(-50,0,90,180,6000,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      
	      System.out.println(" --- Tierra M --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/m.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/m.biltiles"; 
	      data = new GeoData(-90,-50,-180,-90,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra N --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/n.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/n.biltiles"; 
	      data = new GeoData(-90,-50,-90,0,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra O --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/o.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/o.biltiles"; 
	      data = new GeoData(-90,-50,0,90,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	      System.out.println(" --- Tierra P --- ");
	      inputName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/sources/p.bil";
	      outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/p.biltiles"; 
	      data = new GeoData(-90,-50,90,180,4800,10800);
	      BilTiler.convertFile(inputName, data, pyramidType, outputDirectoryName);
	}
	
	// Auxiliar classes
	
	private static class Level {
	      private final int                _level;

	      private int                      _minRow    = Integer.MAX_VALUE;
	      private int                      _maxRow    = Integer.MIN_VALUE;
	      private int                      _minColumn = Integer.MAX_VALUE;
	      private int                      _maxColumn = Integer.MIN_VALUE;

	      private final ArrayList<GEOTile> _tiles     = new ArrayList<GEOTile>();


	      private Level(final int level) {
	         _level = level;
	      }


	      private void processTile(final GEOTile tile) {
	         final int row = tile._row;
	         if (row < _minRow) {
	            _minRow = row;
	         }
	         if (row > _maxRow) {
	            _maxRow = row;
	         }
	         final int column = tile._column;
	         if (column < _minColumn) {
	            _minColumn = column;
	         }
	         if (column > _maxColumn) {
	            _maxColumn = column;
	         }
	         _tiles.add(tile);
	      }


	      @Override
	      public String toString() {
	         final StringBuilder builder = new StringBuilder();
	         builder.append("[Level level=");
	         builder.append(_level);
	         builder.append(", row=");
	         builder.append(_minRow);
	         builder.append("/");
	         builder.append(_maxRow);
	         builder.append(", column=");
	         builder.append(_minColumn);
	         builder.append("/");
	         builder.append(_maxColumn);
	         builder.append(", tiles=");
	         builder.append(((_maxRow - _minRow) + 1) * ((_maxColumn - _minColumn) + 1));
	         builder.append("/");
	         builder.append(_tiles.size());
	         builder.append("]");
	         return builder.toString();
	      }


	      private void initialize() {
	         _tiles.trimToSize();
	      }


	      private Point2D calculateResolution(final GEOSector sector,
	                                          final BufferedImage image) {
	         final double x = sector._delta._longitude / image.getWidth();
	         final double y = sector._delta._latitude / image.getHeight();
	         return new Point2D.Double(x, y);
	      }


	      private BufferedImage process(final File outputDirectory,
	                                    final GEOSector sector,
	                                    final BufferedImage previousImage) throws IOException {
	         Logger.log("Processing: " + this);

	         final Point2D previousResolution = calculateResolution(sector, previousImage);

	         final Point2D levelResolution = Pyramid.resolutionForLevel(_level);
	         final int width = Math.round((float) ((previousImage.getWidth() * previousResolution.getX()) / levelResolution.getX()));
	         final int height = Math.round((float) ((previousImage.getHeight() * previousResolution.getY()) / levelResolution.getY()));

	         if ((width <= 1) || (height <= 1)) {
	            return null;
	         }

	         Logger.log("  Resizing image from " + previousImage.getWidth() + "x" + previousImage.getHeight() + " to " + width + "x"
	                    + height + "...");
	         final BufferedImage levelImage = scaleImage(previousImage, width, height);



	         Logger.log("  Saving " + _tiles.size() + " tiles...");
	         for (final GEOTile tile : _tiles) {

	            saveTile(outputDirectory, levelImage, sector, tile);
	         }

	         return levelImage;
	      }


	      private void saveTile(final File outputDirectory,
	                            final BufferedImage image,
	                            final GEOSector imageSector,
	                            final GEOTile tile) throws IOException {
	         final int tileImageWidth = Pyramid.TILE_IMAGE_WIDTH;
	         final int tileImageHeight = Pyramid.TILE_IMAGE_HEIGHT;

	         final Point2D lowerUV = imageSector.getUVCoordinates(tile._sector._lower);
	         final Point2D upperUV = imageSector.getUVCoordinates(tile._sector._upper);
	         final int dx1 = 0;
	         final int dy1 = 0;
	         final int dx2 = tileImageWidth;
	         final int dy2 = tileImageHeight;

	         final int sx1 = Math.round((float) lowerUV.getX() * image.getWidth());
	         final int sy1 = Math.round((float) upperUV.getY() * image.getHeight());
	         final int sx2 = Math.round((float) upperUV.getX() * image.getWidth());
	         final int sy2 = Math.round((float) lowerUV.getY() * image.getHeight());
	         
	        final BufferedImage tileImage = new BufferedImage(tileImageWidth, tileImageHeight, BufferedImage.TYPE_INT_ARGB);

	        final Graphics2D g2d = tileImage.createGraphics();

	         g2d.drawImage( //
	                  image, //
	                  dx1, dy1, dx2, dy2, //
	                  sx1, sy1, sx2+1, sy2+1, //
	                  null);
	         
	         g2d.dispose();
	         
	         final File output = new File(outputDirectory, _level + "/" + tile._column + "/" + tile._row + ".bil");

	         final File parentDirectory = output.getParentFile();
	         if (!parentDirectory.exists()) {
	            if (!parentDirectory.mkdirs()) {
	               throw new IOException("Can't create directory \"" + parentDirectory.getAbsolutePath() + "\"");
	            }
	         }
	         
	         short[] maxmin = getTileMaxMin(tileImage);
	         
	         tile.updateMaxMinValues(maxmin[0],maxmin[1],tile._withChildren);
	         updateParentsStartingOnLevel(tile._level, tile._row, tile._column, tile._maxValue, tile._minValue);

	         //System.out.println("Saving tile: "+tile._level+"-"+tile._row+"-"+tile._column+", max= "+tile._maxValue+" , min= "+tile._minValue+", withChildren = "+(tile._withChildren? (short) 1: (short) 0) );
	         BilUtils.BufferedImageToBilFileMaxMin(tileImage, output.getAbsolutePath(), tileImageWidth, tileImageHeight, tile._maxValue, tile._minValue,
	        		 tile._withChildren ? (short) 1: (short) 0, (short) 0);
	      }
	      
	      private short[] getTileMaxMin(final BufferedImage image){
	    	  short [] maxmin = new short[2];
	    	  maxmin[0] = Short.MIN_VALUE; maxmin[1] = Short.MAX_VALUE;
	    	  
	    	  for (int i=0; i<image.getWidth(); i++) for (int j=0;j<image.getHeight(); j++){
	    		  short data = (short) ((image.getRGB(j,i)) & 0x0000FFFF);
	    		  maxmin[0] = (short) Math.max(data, maxmin[0]);
	    		  maxmin[1] = (short) Math.min(data, maxmin[1]);
	    	  }
	    	  
	    	  return maxmin;
	      }


	      private static BufferedImage scaleImage(final BufferedImage image,
	                                              final int width,
	                                              final int height) {
	         //return toBufferedImage(previousImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
	    	  

	         final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	         final Graphics2D g2d = result.createGraphics();
	         g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	         final int dx1 = 0;
	         final int dy1 = 0;
	         final int dx2 = width;
	         final int dy2 = height;
	         final int sx1 = 0;
	         final int sy1 = 0;
	         final int sx2 = image.getWidth();
	         final int sy2 = image.getHeight();
	         g2d.drawImage(//
	                  image, //
	                  dx1, dy1, dx2, dy2, //
	                  sx1, sy1, sx2, sy2, //
	                  null);

	         g2d.dispose();
	         return result;
	      }
   }

}
