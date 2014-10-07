package org.glob3.mobile.generated; 
//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  PlanetRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class Tile;
//class TileTessellator;
//class LayerSet;
//class VisibleSectorListenerEntry;
//class VisibleSectorListener;
//class ElevationDataProvider;
//class LayerTilesRenderParameters;
//class TerrainTouchListener;
//class ChangedInfoListener;
//class TileRenderingListener;




//class EllipsoidShape;


public class TilesStatistics
{
  private int _tilesProcessed;
  private int _tilesVisible;
  private int _tilesRendered;

  private static final int _maxLOD = 128;

  private int[] _tilesProcessedByLevel = new int[_maxLOD];
  private int[] _tilesVisibleByLevel = new int[_maxLOD];
  private int[] _tilesRenderedByLevel = new int[_maxLOD];

  private int _buildersStartsInFrame;

  private double _visibleLowerLatitudeDegrees;
  private double _visibleLowerLongitudeDegrees;
  private double _visibleUpperLatitudeDegrees;
  private double _visibleUpperLongitudeDegrees;


  public TilesStatistics()
  {
    clear();
  }

  public void dispose()
  {
  }

  public final void clear()
  {
    _tilesProcessed = 0;
    _tilesVisible = 0;
    _tilesRendered = 0;
    _buildersStartsInFrame = 0;

    final IMathUtils mu = IMathUtils.instance();
    _visibleLowerLatitudeDegrees = mu.maxDouble();
    _visibleLowerLongitudeDegrees = mu.maxDouble();
    _visibleUpperLatitudeDegrees = mu.minDouble();
    _visibleUpperLongitudeDegrees = mu.minDouble();

    for (int i = 0; i < _maxLOD; i++)
    {
      _tilesProcessedByLevel[i] = 0;
      _tilesVisibleByLevel[i] = 0;
      _tilesRenderedByLevel[i] = 0;
    }
  }

  public final int getBuildersStartsInFrame()
  {
    return _buildersStartsInFrame;
  }

  public final void computeBuilderStartInFrame()
  {
    _buildersStartsInFrame++;
  }

  public final void computeTileProcessed(Tile tile)
  {
    _tilesProcessed++;

    final int level = tile._level;
    _tilesProcessedByLevel[level] = _tilesProcessedByLevel[level] + 1;
  }

  public final void computeVisibleTile(Tile tile)
  {
    _tilesVisible++;

    final int level = tile._level;
    _tilesVisibleByLevel[level] = _tilesVisibleByLevel[level] + 1;
  }

  public final void computeRenderedSector(Tile tile)
  {
    final Sector sector = tile._sector;

    final double lowerLatitudeDegrees = sector._lower._latitude._degrees;
    final double lowerLongitudeDegrees = sector._lower._longitude._degrees;
    final double upperLatitudeDegrees = sector._upper._latitude._degrees;
    final double upperLongitudeDegrees = sector._upper._longitude._degrees;

    if (lowerLatitudeDegrees < _visibleLowerLatitudeDegrees)
    {
      _visibleLowerLatitudeDegrees = lowerLatitudeDegrees;
    }
    if (upperLatitudeDegrees < _visibleLowerLatitudeDegrees)
    {
      _visibleLowerLatitudeDegrees = upperLatitudeDegrees;
    }
    if (lowerLatitudeDegrees >_visibleUpperLatitudeDegrees)
    {
      _visibleUpperLatitudeDegrees = lowerLatitudeDegrees;
    }
    if (upperLatitudeDegrees > _visibleUpperLatitudeDegrees)
    {
      _visibleUpperLatitudeDegrees = upperLatitudeDegrees;
    }

    if (lowerLongitudeDegrees < _visibleLowerLongitudeDegrees)
    {
      _visibleLowerLongitudeDegrees = lowerLongitudeDegrees;
    }
    if (upperLongitudeDegrees < _visibleLowerLongitudeDegrees)
    {
      _visibleLowerLongitudeDegrees = upperLongitudeDegrees;
    }
    if (lowerLongitudeDegrees > _visibleUpperLongitudeDegrees)
    {
      _visibleUpperLongitudeDegrees = lowerLongitudeDegrees;
    }
    if (upperLongitudeDegrees > _visibleUpperLongitudeDegrees)
    {
      _visibleUpperLongitudeDegrees = upperLongitudeDegrees;
    }
  }

  public final void computeTileRenderered(Tile tile)
  {
    _tilesRendered++;

    final int level = tile._level;
    _tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;

    computeRenderedSector(tile);
  }

  public final Sector updateVisibleSector(Sector visibleSector)
  {
    if ((visibleSector == null) || (visibleSector._lower._latitude._degrees != _visibleLowerLatitudeDegrees) || (visibleSector._lower._longitude._degrees != _visibleLowerLongitudeDegrees) || (visibleSector._upper._latitude._degrees != _visibleUpperLatitudeDegrees) || (visibleSector._upper._longitude._degrees != _visibleUpperLongitudeDegrees))
    {
      if (visibleSector != null)
         visibleSector.dispose();

      if ((_visibleLowerLatitudeDegrees > _visibleUpperLatitudeDegrees) || (_visibleLowerLongitudeDegrees > _visibleUpperLongitudeDegrees))
      {
        return null;
      }

      return new Sector(Geodetic2D.fromDegrees(_visibleLowerLatitudeDegrees, _visibleLowerLongitudeDegrees), Geodetic2D.fromDegrees(_visibleUpperLatitudeDegrees, _visibleUpperLongitudeDegrees));
    }
    return visibleSector;
  }

  public static String asLogString(int[] m, int nMax)
  {
    boolean first = true;
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    for(int i = 0; i < nMax; i++)
    {
      final int level = i;
      final int counter = m[i];
      if (counter != 0)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          isb.addString(",");
        }
        isb.addInt(level);
        isb.addString(":");
        isb.addInt(counter);
      }
    }

    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final void log(ILogger logger)
  {
    logger.logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).", _tilesProcessed, asLogString(_tilesProcessedByLevel, _maxLOD), _tilesVisible, asLogString(_tilesVisibleByLevel, _maxLOD), _tilesRendered, asLogString(_tilesRenderedByLevel, _maxLOD));
  }


}