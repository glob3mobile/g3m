package com.glob3mobile.server.tools;

import java.util.List;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.glob3mobile.utils.Angle;

public class WebMercatorPyramid extends Pyramid {

	
	WebMercatorPyramid(){
		Pyramid.setTopSectorSplits(1, 1);
		
		_topSector = GEOSector.fullSphere();
	    _topTiles = createTopTiles();
	}

	
	public List<GEOTile> createChildren(final GEOSector sector,
            final GEOTile tile) {
		
		final double splitLatitude = calculateSplitLatitude(tile._sector)._degrees;
		
		final double splitLongitude = tile._sector._center._longitude;
		return tile.createSubTiles(sector, splitLatitude, splitLongitude);
	}

	public static GEOSector sectorFor(final int level, final int column, final int row) {

		final int splitsByLongitude = topSectorSplitsByLongitude * (int) Math.pow(2, level);

		final double[] deltaLatitudes = calculateDeltaLatitudesForLevel(level,column, row);
		
		final double deltaLongitude = 360.0 / splitsByLongitude;

		final GEOGeodetic lower = new GEOGeodetic( //
				deltaLatitudes[0], //
				-180 + (deltaLongitude * column));

		final GEOGeodetic upper = new GEOGeodetic( //
				deltaLatitudes[1], //
				lower._longitude + deltaLongitude);

		return new GEOSector(lower, upper);
	}
	
	private static double[] calculateDeltaLatitudesForLevel(int level, int column, int row){
		
		double [] deltaLats = new double[2]; 
		
		int[] parentsrows = new int[level+1];
		parentsrows[level] = row;
		for (int i=level-1;i>=0;i--) parentsrows[i] = (int) Math.floor(parentsrows[i+1]/2); 
		
		GEOSector sector = GEOSector.fullSphere();
		
		if (level != 0 ){
			for (int i=0; i<level; i++){
				Angle latitude = calculateSplitLatitude(sector);
				//if (parentsrows[i] == currentRow){
				if (parentsrows[i+1] % 2 == 0) {
					sector = new GEOSector( 
							new GEOGeodetic(latitude._degrees,sector._lower._longitude),
							new GEOGeodetic(sector._upper._latitude,sector._upper._longitude)
						);
					//Alto
				}
				else {
					sector = new GEOSector( 
							new GEOGeodetic(sector._lower._latitude,sector._lower._longitude),
							new GEOGeodetic(latitude._degrees,sector._upper._longitude)
						);
					//Bajo
				}
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
	
	public static double tileShorterSideDistance(int level, int column, int row){
		
		Pyramid.setTopSectorSplits(1, 1);
		   
		   GEOSector sector = sectorFor(level,column,row);
		   double distance = -1;
		   
		   try {
			   GeodeticCalculator geoCalculator = new GeodeticCalculator();
			   geoCalculator.setStartingGeographicPoint(sector._lower._longitude,sector._upper._latitude);
			   geoCalculator.setDestinationGeographicPoint(sector._lower._longitude, sector._lower._latitude);
			   distance = geoCalculator.getOrthodromicDistance();
		   }
		   catch (Exception e) {
			   e.printStackTrace();
			   System.out.println("Something really wrong happened while calculating distance"); }
		   return distance;
	   }

}

