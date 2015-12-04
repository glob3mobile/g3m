package com.glob3mobile.server.tools;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.glob3mobile.utils.Angle;

public class WebMercatorPyramid {
	static final int           topSectorSplitsByLatitude  = 1;
	static final int           topSectorSplitsByLongitude = 1;
	
	static int TILE_IMAGE_WIDTH = 256;
	static int TILE_IMAGE_HEIGHT = 256;
	
	public final GEOSector _topSector;
	public final List<GEOTile> _topTiles;
	
	WebMercatorPyramid(){
		//TODO: de cara a pirámide genérica: meter como parámetros topSectorSplits Lat-Lon
		_topSector = GEOSector.fullSphere();
	    _topTiles = createTopTiles();
	}
	
	public static void setTileImageDimensions (int width, int height){
		//TODO: candidata a pirámide genérica as is
		TILE_IMAGE_WIDTH = width;
		TILE_IMAGE_HEIGHT = height;
	}
	
	private List<GEOTile> createTopTiles() {
		//De cara a pirámide genérica: ¿do nothing?
		final List<GEOTile> result = new ArrayList<GEOTile>(topSectorSplitsByLatitude * topSectorSplitsByLongitude);

	      final double fromLatitude = _topSector._lower._latitude;
	      final double fromLongitude = _topSector._lower._longitude;

	      final double deltaLan = _topSector._delta._latitude;
	      final double deltaLon = _topSector._delta._longitude;

	      final double tileHeight = deltaLan / topSectorSplitsByLatitude;
	      final double tileWidth = deltaLon / topSectorSplitsByLongitude;

	      for (int row = 0; row < topSectorSplitsByLatitude; row++) {
	         final double tileLatFrom = (tileHeight * row) + fromLatitude;
	         final double tileLatTo = tileLatFrom + tileHeight;

	         for (int col = 0; col < topSectorSplitsByLongitude; col++) {
	            final double tileLonFrom = (tileWidth * col) + fromLongitude;
	            final double tileLonTo = tileLonFrom + tileWidth;

	            final GEOGeodetic tileLower = new GEOGeodetic(tileLatFrom, tileLonFrom);
	            final GEOGeodetic tileUpper = new GEOGeodetic(tileLatTo, tileLonTo);
	            final GEOSector sector = new GEOSector(tileLower, tileUpper);

	            final int level = 0;
	            result.add(new GEOTile(null, sector, level, row, col));
	         }
	      }

	      sortTiles(result);

	      return Collections.unmodifiableList(result);
	   }


	   private static void sortTiles(final List<GEOTile> tiles) {
	      Collections.sort( //
	               tiles, //
	               new Comparator<GEOTile>() {
	                  @Override
	                  public int compare(final GEOTile i,
	                                     final GEOTile j) {
	                     final int rowI = i._row;
	                     final int rowJ = j._row;
	                     if (rowI < rowJ) {
	                        return -1;
	                     }
	                     if (rowI > rowJ) {
	                        return 1;
	                     }

	                     final int columnI = i._column;
	                     final int columnJ = j._column;
	                     if (columnI < columnJ) {
	                        return -1;
	                     }
	                     if (columnI > columnJ) {
	                        return 1;
	                     }
	                     return 0;
	                  }
	               });
	   }
	   
	   public static Point2D resolutionForLevel(final int level) {
		   //TODO: ¿Do nothing de cara a una Pirámide genérica?
		   
		   final int splitsByLatitude = topSectorSplitsByLatitude * (int) Math.pow(2, level);
		   final int splitsByLongitude = topSectorSplitsByLongitude * (int) Math.pow(2, level);
		   
		   final double deltaLatitude = 180.0 / splitsByLatitude;
		   final double deltaLongitude = 360.0 / splitsByLongitude;

		   final double x = deltaLongitude / TILE_IMAGE_WIDTH;
		   final double y = deltaLatitude / TILE_IMAGE_HEIGHT;
		   return new Point2D.Double(x, y);
	   }
	   
	public static int bestLevelForResolution(final double resX, final double resY) {
		//TODO: ¿Do nothing de cara a una Pirámide genérica?
		int level = 0;
		while (true) {
			final Point2D resolution = resolutionForLevel(level);
			if ((resolution.getX() < resX) || (resolution.getY() < resY)) {
				return (level > 0) ? level - 1 : level;
			}
			level++;
		}
	}
	
	public List<GEOTile> createChildren(final GEOSector sector,
            final GEOTile tile) {
		//TODO: Esta será local a ambas pirámides
		
		final double splitLatitude = calculateSplitLatitude(tile._sector)._degrees;
		
		final double splitLongitude = tile._sector._center._longitude;
		return tile.createSubTiles(sector, splitLatitude, splitLongitude);
	}

	public static GEOSector sectorFor(final int level, final int column, final int row) {
		//TODO: Esta será local a ambas pirámides y no pasa a Pyramid.
		
		final int splitsByLongitude = topSectorSplitsByLongitude * (int) Math.pow(2, level);

		//TODO: Revisar MUCHO este código. Es candidato nº 1 a reventar y hacer aguas.
	
		final double[] deltaLatitudes = calculateDeltaLatitudesForLevel(level, row);
		final double deltaLongitude = 360.0 / splitsByLongitude;

		final GEOGeodetic lower = new GEOGeodetic( //
				deltaLatitudes[0], //
				-180 + (deltaLongitude * column));

		final GEOGeodetic upper = new GEOGeodetic( //
				deltaLatitudes[1], //
				lower._longitude + deltaLongitude);

		return new GEOSector(lower, upper);
	}
	
	private static double[] calculateDeltaLatitudesForLevel(int level, int row){
		//Supongamos row 0 == lat 90 == delta1 0, delta 2:0
		double [] deltaLats = new double[2]; 
		
		int[] parentsrows = new int[level+1];
		parentsrows[level] = row;
		for (int i=level-1;i>=0;i--) parentsrows[i] = (int) Math.floor(parentsrows[i]/2); 
		
		GEOSector sector = GEOSector.fullSphere();
		int currentRow = 0;
		if (level != 0 ) for (int i=1; i<=level; i++){
			Angle latitude = calculateSplitLatitude(sector);
			if (parentsrows[i] == currentRow){
				sector = new GEOSector( 
							new GEOGeodetic(latitude._degrees,sector._lower._longitude),
							new GEOGeodetic(sector._upper._latitude,sector._upper._longitude)
						);
				currentRow = currentRow * 2;
			}
			else {
				sector = new GEOSector( 
						new GEOGeodetic(sector._lower._latitude,sector._lower._longitude),
						new GEOGeodetic(latitude._degrees,sector._upper._longitude)
					);
				currentRow = currentRow * 2 + 1;
			}
		}
		deltaLats[0] = sector._lower._latitude;
		deltaLats[1] = sector._upper._latitude;
		return deltaLats;
	}
	
	private static Angle calculateSplitLatitude (GEOSector sector){
		
		final double middleV = (getMercatorV(Angle.fromDegrees(sector._lower._latitude)) + getMercatorV(Angle.fromDegrees(sector._upper._latitude))) / 2;
		return toLatitude(middleV);
	}
	
	public static Angle toLatitude(double v)
	  {
	    final double exp = Math.exp(-2 * Math.PI * (1.0 - v - 0.5));
	    final double atan = Math.atan(exp);
	    return Angle.fromRadians((Math.PI / 2) - 2 * atan);
	  }
	
	public static double getMercatorV(Angle latitude)
	{
		final double _upperLimitInDegrees = 85.0511287798;
		final double _lowerLimitInDegrees = -85.0511287798;
		
	    if (latitude._degrees >= _upperLimitInDegrees)
	    {
	      return 0;
	    }
	    if (latitude._degrees <= _lowerLimitInDegrees)
	    {
	      return 1;
	    }

	    final double pi4 = Math.PI * 4;

	    final double latSin = java.lang.Math.sin(latitude._radians);
	    return 1.0 - ((Math.log((1.0 + latSin) / (1.0 - latSin)) / pi4) + 0.5);
	  }

}

