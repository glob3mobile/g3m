package org.glob3.mobile.generated; 
//
//  Sector.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class Sector
{

   private final Geodetic2D _min = new Geodetic2D();
   private final Geodetic2D _max = new Geodetic2D();


  public Sector(Angle minLat, Angle minLon, Angle maxLat, Angle maxLon)
  {
	  _min = new Geodetic2D(minLat, minLon);
	  _max = new Geodetic2D(maxLat, maxLon);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D min() const
  public final Geodetic2D min()
  {
	return _min;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D max() const
  public final Geodetic2D max()
  {
	return _max;
  }

}