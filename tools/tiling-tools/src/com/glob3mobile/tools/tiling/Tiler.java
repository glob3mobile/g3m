

package com.glob3mobile.tools.tiling;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;

import com.glob3mobile.geo.GEOImage;
import com.glob3mobile.geo.GEOSector;
import com.glob3mobile.tools.tiling.pyramid.Pyramid;
import com.glob3mobile.tools.tiling.pyramid.Tile;
import com.glob3mobile.tools.tiling.pyramid.WebMercatorPyramid;
import com.glob3mobile.utils.IOUtils;
import com.glob3mobile.utils.Logger;


public class Tiler {

   public static void processDirectory(final Pyramid pyramid,
                                       final String inputDirectoryName,
                                       final String outputDirectoryName,
                                       final boolean recursive) throws IOException {
      final File inputDirectory = new File(inputDirectoryName);
      if (!inputDirectory.exists()) {
         throw new IOException("Input-Directory \"" + inputDirectoryName + "\" doesn't exist");
      }
      if (!inputDirectory.isDirectory()) {
         throw new IOException("\"" + inputDirectoryName + "\" is not a directory");
      }

      processDirectory(pyramid, inputDirectory, outputDirectoryName, recursive);
   }


   private static void processDirectory(final Pyramid pyramid,
                                        final File directory,
                                        final String outputDirectoryName,
                                        final boolean recursive) throws IOException {
      for (final File child : listFiles(directory)) {
         if (child.isDirectory()) {
            if (recursive) {
               processDirectory(pyramid, child, outputDirectoryName, recursive);
            }
         }
         else if (child.isFile()) {
            final String lowerCaseName = child.getName().toLowerCase();
            if (lowerCaseName.endsWith(".tif") || lowerCaseName.endsWith(".tiff")) {
               final String subdirectoryName = child.getName().replace('.', '_') + ".tiles";
               final String childOutputDirectoryName = new File(outputDirectoryName, subdirectoryName).getAbsolutePath();
               //System.out.println("- Found geotiff: " + child.getName() + " ==> " + childOutputDirectoryName);
               final Tiler tiler = new Tiler(pyramid, child.getAbsolutePath(), childOutputDirectoryName);
               tiler.process();
            }
         }
      }
   }


   private static File[] listFiles(final File directory) {
      final File[] result = directory.listFiles();
      final Comparator<File> comparator = new Comparator<File>() {
         @Override
         public int compare(final File o1,
                            final File o2) {
            return o1.compareTo(o2);
         }
      };
      Arrays.sort(result, comparator);
      return result;
   }


   public static void processFile(final Pyramid pyramid,
                                  final String inputFileName,
                                  final String outputDirectoryName) throws IOException {
      final Tiler tiler = new Tiler(pyramid, inputFileName, outputDirectoryName);
      tiler.process();
   }


   private final Pyramid _pyramid;
   private final File    _inputFile;
   private final File    _outputDirectory;


   private Tiler(final Pyramid pyramid,
                 final String inputFileName,
                 final String outputDirectoryName) throws IOException {
      _pyramid = pyramid;
      _inputFile = new File(inputFileName);
      if (!_inputFile.exists()) {
         throw new IOException("\"" + inputFileName + "\" not found!");
      }
      _outputDirectory = new File(outputDirectoryName);
      IOUtils.ensureEmptyDirectory(_outputDirectory);
   }


   private static GEOSector createSector(final GridCoverage2D coverage) {
      return new GEOSector(coverage.getEnvelope());
   }


   private static BufferedImage createImage(final GridCoverage2D coverage) {
      Logger.log("Creating image...");
      final RenderedImage image = coverage.getRenderedImage();
      final Raster data = image.getData();
      final ColorModel colorModel = image.getColorModel();
      final WritableRaster writableRaster = data.createCompatibleWritableRaster();
      writableRaster.setDataElements(0, 0, data);
      return new BufferedImage(colorModel, writableRaster, colorModel.isAlphaPremultiplied(), null);
   }


   private GEOImage read(final File inputFile) throws IOException {
      Logger.log("Reading image \"" + inputFile.getAbsolutePath() + "\"...");

      final GeoTiffReader reader = new GeoTiffReader(inputFile);
      final GridCoverage2D coverage = reader.read(null);

      _pyramid.checkCRS(coverage);

      final GEOSector sector = createSector(coverage);
      Logger.log("Read image, sector " + sector);

      final BufferedImage image = createImage(coverage);
      Logger.log("Created image " + image.getWidth() + "x" + image.getHeight());

      return new GEOImage(sector, image);
   }


   private void processTile(final GEOImage geoImage,
                            final Pyramid pyramid,
                            final Tile tile,
                            final int maxLevel,
                            final Level[] levels) {
      final int levelInt = tile._level;
      if (levelInt <= maxLevel) {
         if (tile._sector.touchesWith(geoImage._sector)) {
            final Level level = levels[levelInt];
            if (level != null) {
               level.processTile(tile);
            }

            if (levelInt < maxLevel) {
               final List<Tile> children = pyramid.createChildren(geoImage._sector, tile);
               for (final Tile child : children) {
                  processTile(geoImage, pyramid, child, maxLevel, levels);
               }
            }
         }
      }
   }

   private static class Level {
      private final Pyramid         _pyramid;
      private final int             _level;

      private int                   _minRow    = Integer.MAX_VALUE;
      private int                   _maxRow    = Integer.MIN_VALUE;
      private int                   _minColumn = Integer.MAX_VALUE;
      private int                   _maxColumn = Integer.MIN_VALUE;

      private final ArrayList<Tile> _tiles     = new ArrayList<Tile>();


      private Level(final Pyramid pyramid,
                    final int level) {
         _pyramid = pyramid;
         _level = level;
      }


      private void processTile(final Tile tile) {
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

         final Point2D levelResolution = _pyramid.resolutionForLevel(_level);
         final int width = Math.round((float) ((previousImage.getWidth() * previousResolution.getX()) / levelResolution.getX()));
         final int height = Math.round((float) ((previousImage.getHeight() * previousResolution.getY()) / levelResolution.getY()));

         if ((width <= 1) || (height <= 1)) {
            return null;
         }

         Logger.log("  Resizing image from " + previousImage.getWidth() + "x" + previousImage.getHeight() + " to " + width + "x"
                    + height + "...");
         final BufferedImage levelImage = scaleImage(previousImage, width, height);

         //         IOUtils.writeJPEG(levelImage, output, height)
         //                  final File output = new File(outputDirectory, _level + ".png");
         //                  ImageIO.write(levelImage, "png", output);

         Logger.log("  Saving " + _tiles.size() + " tiles...");
         for (final Tile tile : _tiles) {
            //            log("    Processing tile: " + tile);
            saveTile(outputDirectory, levelImage, sector, tile);
         }

         return levelImage;
      }


      private void saveTile(final File outputDirectory,
                            final BufferedImage image,
                            final GEOSector imageSector,
                            final Tile tile) throws IOException {
         final int tileImageWidth = _pyramid.getTileImageWidth();
         final int tileImageHeight = _pyramid.getTileImageHeight();

         final Point2D lowerUV = imageSector.getUVCoordinates(tile._sector._lower);
         final Point2D upperUV = imageSector.getUVCoordinates(tile._sector._upper);
         final int dx1 = 0;
         final int dy1 = 0;
         final int dx2 = tileImageWidth;
         final int dy2 = tileImageHeight;
         //         final int sx1 = Math.round((float) lowerUV.getX() * image.getWidth());
         //         final int sy1 = Math.round((float) (1.0 - lowerUV.getY()) * image.getHeight());
         //         final int sx2 = Math.round((float) upperUV.getX() * image.getWidth());
         //         final int sy2 = Math.round((float) (1.0 - upperUV.getY()) * image.getHeight());
         final int sx1 = Math.round((float) lowerUV.getX() * image.getWidth());
         final int sy1 = Math.round((float) upperUV.getY() * image.getHeight());
         final int sx2 = Math.round((float) upperUV.getX() * image.getWidth());
         final int sy2 = Math.round((float) lowerUV.getY() * image.getHeight());

         final BufferedImage tileImage = new BufferedImage(tileImageWidth, tileImageHeight, BufferedImage.TYPE_4BYTE_ABGR);

         final Graphics2D g2d = tileImage.createGraphics();

         //         g2d.setColor(Color.RED);
         //         g2d.fillRect(0, 0, width, height);

         g2d.drawImage( //
                  image, //
                  dx1, dy1, dx2, dy2, //
                  sx1, sy1, sx2, sy2, //
                  null);
         g2d.dispose();


         final int numRows = _pyramid.getNumberOfRows(_level);
         final int row = numRows - tile._row - 1;
         // final int row = tile._row;

         final File output = new File(outputDirectory, _level + "/" + tile._column + "/" + row + ".png");
         //final File output = new File(outputDirectory, _level + "/" + row + "-" + tile._column + ".png");
         final File parentDirectory = output.getParentFile();
         if (!parentDirectory.exists()) {
            if (!parentDirectory.mkdirs()) {
               throw new IOException("Can't create directory \"" + parentDirectory.getAbsolutePath() + "\"");
            }
         }
         //log("    Saving tile: " + tile);
         ImageIO.write(tileImage, "png", output);
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


      //      private static BufferedImage toBufferedImage(final Image image) {
      //         if (image == null) {
      //            return null;
      //         }
      //         if (image instanceof BufferedImage) {
      //            return (BufferedImage) image;
      //         }
      //
      //         final BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      //         final Graphics2D g = result.createGraphics();
      //         g.drawImage(image, 0, 0, null);
      //         g.dispose();
      //         return result;
      //      }

   }


   private void process() throws IOException {
      final GEOImage geoImage = read(_inputFile);

      final int minLevel = 0;
      final int maxLevel = _pyramid.bestLevelForResolution(geoImage._resolution.getX(), geoImage._resolution.getY());
      Logger.log("MaxLevel: " + maxLevel);

      final Level[] levels = new Level[maxLevel + 1];
      for (int i = 0; i <= maxLevel; i++) {
         levels[i] = (i < minLevel) ? null : new Level(_pyramid, i);
      }

      for (final Tile tile : _pyramid.getTopTiles()) {
         processTile(geoImage, _pyramid, tile, maxLevel, levels);
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

      //      final File outputFile = new File("/Users/dgd/Desktop/TEST.jpeg");
      //      ImageIO.write(geoImage._bufferedImage, "jpeg", outputFile);

      Logger.log("done!");
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("Tiler 0.1");
      System.out.println("---------\n");


      /*
        gdalwarp --config GDAL_CACHEMAX 512 -wm 512 -multi -s_srs EPSG:4326 -t_srs EPSG:3857 -r lanczos TrueMarble.1km.21600x21600.A1.tif  mercator_TrueMarble.1km.21600x21600.A1.tif
       */


      //      final String inputName = "/Volumes/SSD1/GIS_DATA/true_marble/TrueMarble.4km.10800x5400.tif";
      //      final String outputDirectoryName = "/Volumes/SSD1/_TEST_/TrueMarble.4km/";
      //      final Pyramid pyramid = WGS84Pyramid.createDefault();


      //      final String inputName = "/Volumes/SSD1/earth-pyramid/1000m/mercator_TrueMarble.1km.21600x21600.A1.tif";
      //      final String outputDirectoryName = "/Volumes/SSD1/_TEST_/mercator_TrueMarble.1km/";
      //      final Pyramid pyramid = WebMercatorPyramid.createDefault();
      //
      //      Tiler.processFile(pyramid, inputName, outputDirectoryName);


      final String inputDirectoryName = "/Volumes/SSD1/earth-pyramid/1000m/";
      final String outputDirectoryName = "/Volumes/SSD1/_TEST_/mercator_TrueMarble.1km/";
      final Pyramid pyramid = WebMercatorPyramid.createDefault();
      Tiler.processDirectory(pyramid, inputDirectoryName, outputDirectoryName, true);

   }


}
