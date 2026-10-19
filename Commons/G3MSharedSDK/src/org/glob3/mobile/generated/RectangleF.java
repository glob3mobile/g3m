package org.glob3.mobile.generated;
//
//  RectangleF.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

//
//  RectangleF.hpp
//  G3M
//
//  Created by Jose Miguel SN on 19/04/13.
//
//




//class Sector;


public class RectangleF
{

  public final float _x;
  public final float _y;
  public final float _width;
  public final float _height;

  public RectangleF(float x, float y, float width, float height)
  {
     _x = x;
     _y = y;
     _width = width;
     _height = height;
    if ((_width < 0) || (_height < 0))
    {
      throw new RuntimeException("Invalid rectangle extent");
    }
  }

  public RectangleF(RectangleF that)
  {
     _x = that._x;
     _y = that._y;
     _width = that._width;
     _height = that._height;
  }

  public void dispose()
  {

  }

  public final boolean equalTo(RectangleF that)
  {
    return ((_x == that._x) && (_y == that._y) && (_width == that._width) && (_height == that._height));
  }

  public final boolean fullContains(RectangleF that)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    if (!mu.isBetween(that._x, _x, _x + _width))
    {
      return false;
    }
  
    if (!mu.isBetween(that._x + that._width, _x, _x + _width))
    {
      return false;
    }
  
    if (!mu.isBetween(that._y, _y, _y + _height))
    {
      return false;
    }
  
    if (!mu.isBetween(that._y + that._height, _y, _y + _height))
    {
      return false;
    }
  
    return true;
  }

  public static boolean fullContains(float outerX, float outerY, float outerWidth, float outerHeight, float innerX, float innerY, float innerWidth, float innerHeight)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    if (!mu.isBetween(innerX, outerX, outerX + outerWidth))
    {
      return false;
    }
  
    if (!mu.isBetween(innerX + innerWidth, outerX, outerX + outerWidth))
    {
      return false;
    }
  
    if (!mu.isBetween(innerY, outerY, outerY + outerHeight))
    {
      return false;
    }
  
    if (!mu.isBetween(innerY + innerHeight, outerY, outerY + outerHeight))
    {
      return false;
    }
  
    return true;
  }

  public final boolean contains(float x, float y)
  {
    return (x >= _x) && (y >= _y) && (x <= (_x + _width)) && (y <= (_y + _height));
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(RectangleF x:");
    isb.addFloat(_x);
    isb.addString(", y:");
    isb.addFloat(_y);
    isb.addString(", width:");
    isb.addFloat(_width);
    isb.addString(", height:");
    isb.addFloat(_height);
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final String id()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("RectangleF|");
    isb.addFloat(_x);
    isb.addString("|");
    isb.addFloat(_y);
    isb.addString("|");
    isb.addFloat(_width);
    isb.addString("|");
    isb.addFloat(_height);
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public static RectangleF calculateInnerRectangleFromSector(int wholeSectorWidth, int wholeSectorHeight, Sector wholeSector, Sector innerSector, boolean mercator)
  {
    if (wholeSector.isNan() || innerSector.isNan())
    {
      //I think that this case doesn't exist
      ILogger.instance().logError("Testing this case: view code");
      return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
    }
  
    if (wholeSector.isEquals(innerSector))
    {
      return new RectangleF(0, 0, wholeSectorWidth, wholeSectorHeight);
    }
  
    final double lowerU = wholeSector.getUCoordinate(innerSector._lower._longitude);
    final double upperU = wholeSector.getUCoordinate(innerSector._upper._longitude);
  
    double upperV;
    double lowerV;
    if (mercator)
    {
      // a mercator image is linear in mercator V, not in latitude
      final double wholeUpperGlobalV = MercatorUtils.getMercatorV(wholeSector._upper._latitude);
      final double wholeLowerGlobalV = MercatorUtils.getMercatorV(wholeSector._lower._latitude);
      upperV = MercatorUtils.getLocalMercatorV(innerSector._upper._latitude, wholeUpperGlobalV, wholeLowerGlobalV);
      lowerV = MercatorUtils.getLocalMercatorV(innerSector._lower._latitude, wholeUpperGlobalV, wholeLowerGlobalV);
    }
    else
    {
      upperV = wholeSector.getVCoordinate(innerSector._upper._latitude);
      lowerV = wholeSector.getVCoordinate(innerSector._lower._latitude);
    }
  
    return new RectangleF((float)(lowerU * wholeSectorWidth), (float)(upperV * wholeSectorHeight), (float)((upperU - lowerU) * wholeSectorWidth), (float)((lowerV - upperV) * wholeSectorHeight));
  }

}