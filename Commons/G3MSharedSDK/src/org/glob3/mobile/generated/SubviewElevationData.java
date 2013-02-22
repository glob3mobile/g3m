package org.glob3.mobile.generated; 
//
//  SubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//



public class SubviewElevationData extends ElevationData
{
  private final ElevationData _elevationData;
  private final boolean _ownsElevationData;

  public SubviewElevationData(ElevationData elevationData, boolean ownsElevationData, Sector sector, Vector2I resolution, double noDataValue)
  {
     super(sector, resolution, noDataValue);
     _elevationData = elevationData;
     _ownsElevationData = ownsElevationData;
  }

  public void dispose()
  {
    if (_ownsElevationData)
    {
      if (_elevationData != null)
         _elevationData.dispose();
    }
  }

  public final double getElevationAt(int x, int y, int type)
  {
  //  const Angle latitude  = Angle::fromRadians( _stepInLatitudeRadians  * y );
  //  const Angle longitude = Angle::fromRadians( _stepInLongitudeRadians * x );
  
    final Geodetic2D position = _sector.getInnerPoint((double) x / _width, 1.0 - ((double) y / _height));
  
    return getElevationAt(position.latitude(), position.longitude(), type);
  }

  public final double getElevationAt(Angle latitude, Angle longitude, int type)
  {
    if (!_sector.contains(latitude, longitude))
    {
      ILogger.instance().logError("Sector %s doesn't contain lat=%s lon=%s", _sector.description(), latitude.description(), longitude.description());
  
  //    return IMathUtils::instance()->NanD();
      return _noDataValue;
    }
    return _elevationData.getElevationAt(latitude, longitude, type);
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(SubviewElevationData extent=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(" sector=");
    isb.addString(_sector.description());
    isb.addString(" on ElevationData=");
    isb.addString(_elevationData.description(detailed));
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}