package org.glob3.mobile.generated; 
//
//  PlanetTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  PlanetTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



//class IShortBuffer;
//class Sector;

public class PlanetTileTessellator extends TileTessellator
{
  private final boolean _skirted;
  private final Sector _renderedSector ;

  private java.util.HashMap<Vector2I, IShortBuffer> _indicesMap = new java.util.HashMap<Vector2I, IShortBuffer>();

  private Vector2I calculateResolution(Vector2I resolution, Tile tile, Sector renderedSector)
  {
    Sector sector = tile.getSector();
  
    final double latRatio = sector.getDeltaLatitude()._degrees / renderedSector.getDeltaLatitude()._degrees;
    final double lonRatio = sector.getDeltaLongitude()._degrees / renderedSector.getDeltaLongitude()._degrees;
  
    final IMathUtils mu = IMathUtils.instance();
  
    int resX = (int) mu.ceil((resolution._x / lonRatio));
    if (resX < 2)
    {
      resX = 2;
    }
  
    int resY = (int) mu.ceil((resolution._y / latRatio));
    if (resY < 2)
    {
      resY = 2;
    }
  
    final Vector2I meshRes = new Vector2I(resX, resY);
    return meshRes;
  
  
    //  return rawResolution;
  
    //  /* testing for dynamic latitude-resolution */
    //  const double cos = sector._center._latitude.cosinus();
    //
    //  int resolutionY = (int) (rawResolution._y * cos);
    //  if (resolutionY < 8) {
    //    resolutionY = 8;
    //  }
    //
    //  int resolutionX = (int) (rawResolution._x * cos);
    //  if (resolutionX < 8) {
    //    resolutionX = 8;
    //  }
    //
    //  return Vector2I(resolutionX, resolutionY);
  }

  private IShortBuffer createTileIndices(Planet planet, Sector sector, Vector2I tileResolution)
  {
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (short j = 0; j < (tileResolution._y-1); j++)
    {
      final short jTimesResolution = (short)(j *tileResolution._x);
      if (j > 0)
      {
        indices.add(jTimesResolution);
      }
      for (short i = 0; i < tileResolution._x; i++)
      {
        indices.add((short)(jTimesResolution + i));
        indices.add((short)(jTimesResolution + i + tileResolution._x));
      }
      indices.add((short)(jTimesResolution + 2 *tileResolution._x - 1));
    }
  
  
    // create skirts
    if (_skirted)
    {
  
      int posS = tileResolution._x * tileResolution._y;
      indices.add((short)(posS-1));
  
      // east side
      for (int j = tileResolution._y-1; j > 0; j--)
      {
        indices.add((short)(j *tileResolution._x + (tileResolution._x-1)));
        indices.add((short) posS++);
      }
  
      // north side
      for (int i = tileResolution._x-1; i > 0; i--)
      {
        indices.add((short) i);
        indices.add((short) posS++);
      }
  
      // west side
      for (int j = 0; j < tileResolution._y-1; j++)
      {
        indices.add((short)(j *tileResolution._x));
        indices.add((short) posS++);
      }
  
      // south side
      for (int i = 0; i < tileResolution._x-1; i++)
      {
        indices.add((short)((tileResolution._y-1)*tileResolution._x + i));
        indices.add((short) posS++);
      }
  
      // last triangle
      indices.add((short)((tileResolution._x *tileResolution._y)-1));
      indices.add((short)(tileResolution._x *tileResolution._y));
    }
  
    return indices.create();
  }

  private IShortBuffer getTileIndices(Planet planet, Sector sector, Vector2I tileResolution, boolean reusableIndices)
  {
    if (reusableIndices)
    {
      return createTileIndices(planet, sector, tileResolution);
    }
  
    IShortBuffer indices = _indicesMap.get(tileResolution);
    if (indices == null){
      indices = createTileIndices(planet, sector, tileResolution);
      _indicesMap.put(tileResolution, indices);
    }
    return indices;
  
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Geodetic3D getGeodeticOnPlanetSurface(IMathUtils mu, Planet planet, ElevationData elevationData, float verticalExaggeration, Geodetic2D g);

  private boolean needsEastSkirt(Sector tileSector)
  {
    return _renderedSector.upperLongitude().greaterThan(tileSector.upperLongitude());
  }

  private boolean needsNorthSkirt(Sector tileSector)
  {
    return _renderedSector.upperLatitude().greaterThan(tileSector.upperLatitude());
  }

  private boolean needsWestSkirt(Sector tileSector)
  {
    return _renderedSector.lowerLongitude().lowerThan(tileSector.lowerLongitude());
  }

  private boolean needsSouthSkirt(Sector tileSector)
  {
    return _renderedSector.lowerLatitude().lowerThan(tileSector.lowerLatitude());
  }

  private Sector getRenderedSectorForTile(Tile tile)
  {
    return tile.getSector().intersection(_renderedSector);
  }

  private double getHeight(Geodetic2D g, ElevationData elevationData, double verticalExaggeration)
  {
    if (elevationData == null)
    {
      return 0;
    }
    final double h = elevationData.getElevationAt(g);
    if (IMathUtils.instance().isNan(h))
    {
      return 0;
    }
  
    return h;
  }


  public PlanetTileTessellator(boolean skirted, Sector sector)
  {
     _skirted = skirted;
     _renderedSector = new Sector(sector);
  }

  public void dispose()
  {
    for (final java.util.Map.Entry<Vector2I, IShortBuffer> entry : _indicesMap.entrySet()) {
      final IShortBuffer buffer = entry.getValue();
      buffer.dispose();
    }
  
    super.dispose();
  }

  public final Vector2I getTileMeshResolution(Planet planet, Vector2I rawResolution, Tile tile, boolean debug)
  {
    Sector sector = getRenderedSectorForTile(tile); // tile->getSector();
    return calculateResolution(rawResolution, tile, sector);
  }


  public final Mesh createTileMesh(Planet planet, Vector2I rawResolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean mercator, boolean renderDebug)
  {
  
    final Sector tileSector = tile.getSector();
    final Sector meshSector = getRenderedSectorForTile(tile); // tile->getSector();
    final Vector2I tileResolution = calculateResolution(rawResolution, tile, meshSector);
  
    double minElevation = 0;
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, meshSector._center);
  
    final IMathUtils mu = IMathUtils.instance();
  
    for (int j = 0; j < tileResolution._y; j++)
    {
      final double v = (double) j / (tileResolution._y - 1);
  
      for (int i = 0; i < tileResolution._x; i++)
      {
        final double u = (double) i / (tileResolution._x - 1);
  
        final Geodetic2D position = meshSector.getInnerPoint(u, v);
  
        double elevation = 0;
  
        //TODO: MERCATOR!!!
  
        if (elevationData != null)
        {
          final double rawElevation = elevationData.getElevationAt(position);
          if (!mu.isNan(rawElevation))
          {
            elevation = rawElevation * verticalExaggeration;
  
            if (elevation < minElevation)
            {
              minElevation = elevation;
            }
          }
        }
        vertices.add(position, elevation);
      }
    }
  
    // create skirts
    if (_skirted)
    {
      // compute skirt height
      final Vector3D sw = planet.toCartesian(meshSector.getSW());
      final Vector3D nw = planet.toCartesian(meshSector.getNW());
      final double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minElevation;
  
      // east side
      boolean hasSkirt = needsEastSkirt(tileSector);
      for (int j = tileResolution._y-1; j > 0; j--)
      {
  
        final double x = 1;
        final double y = (double)j/(tileResolution._y-1);
        final Geodetic2D g = meshSector.getInnerPoint(x, y);
  
        double h = skirtHeight;
        if (!hasSkirt)
        {
          h = getHeight(g, elevationData, verticalExaggeration);
        }
  
        vertices.add(g, h);
      }
  
      // north side
      hasSkirt = needsNorthSkirt(tileSector);
      for (int i = tileResolution._x-1; i > 0; i--)
      {
        final double x = (double)i/(tileResolution._x-1);
        final double y = 0;
        final Geodetic2D g = meshSector.getInnerPoint(x, y);
  
        double h = skirtHeight;
        if (!hasSkirt)
        {
          h = getHeight(g, elevationData, verticalExaggeration);
        }
  
        vertices.add(g, h);
      }
  
      // west side
      hasSkirt = needsWestSkirt(tileSector);
      for (int j = 0; j < tileResolution._y-1; j++)
      {
        final double x = 0;
        final double y = (double)j/(tileResolution._y-1);
        final Geodetic2D g = meshSector.getInnerPoint(x, y);
  
        double h = skirtHeight;
        if (!hasSkirt)
        {
          h = getHeight(g, elevationData, verticalExaggeration);
        }
  
        vertices.add(g, h);
      }
  
      // south side
      hasSkirt = needsSouthSkirt(tileSector);
      for (int i = 0; i < tileResolution._x-1; i++)
      {
        final double x = (double)i/(tileResolution._x-1);
        final double y = 1;
        final Geodetic2D g = meshSector.getInnerPoint(x, y);
  
        double h = skirtHeight;
        if (!hasSkirt)
        {
          h = getHeight(g, elevationData, verticalExaggeration);
        }
  
        vertices.add(g, h);
      }
    }
  
    final boolean reusableMeshIndex = _renderedSector.fullContains(meshSector);
  
    return new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, getTileIndices(planet, meshSector, tileResolution, reusableMeshIndex), !reusableMeshIndex, 1, 1);
  }

  public final Mesh createTileDebugMesh(Planet planet, Vector2I rawResolution, Tile tile)
  {
    final Sector sector = getRenderedSectorForTile(tile); // tile->getSector();
  
    final int resolutionXMinus1 = rawResolution._x - 1;
    final int resolutionYMinus1 = rawResolution._y - 1;
    short posS = 0;
  
    // compute offset for vertices
    final Vector3D sw = planet.toCartesian(sector.getSW());
    final Vector3D nw = planet.toCartesian(sector.getNW());
    final double offset = nw.sub(sw).length() * 1e-3;
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, sector._center);
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    // west side
    for (int j = 0; j < resolutionYMinus1; j++)
    {
      vertices.add(sector.getInnerPoint(0, (double)j/resolutionYMinus1), offset);
      indices.add(posS++);
    }
  
    // south side
    for (int i = 0; i < resolutionXMinus1; i++)
    {
      vertices.add(sector.getInnerPoint((double)i/resolutionXMinus1, 1), offset);
      indices.add(posS++);
    }
  
    // east side
    for (int j = resolutionYMinus1; j > 0; j--)
    {
      vertices.add(sector.getInnerPoint(1, (double)j/resolutionYMinus1), offset);
      indices.add(posS++);
    }
  
    // north side
    for (int i = resolutionXMinus1; i > 0; i--)
    {
      vertices.add(sector.getInnerPoint((double)i/resolutionXMinus1, 0), offset);
      indices.add(posS++);
    }
  
    Color color = Color.newFromRGBA((float) 1.0, (float) 0.0, (float) 0, (float) 1.0);
  
    return new IndexedMesh(GLPrimitive.lineLoop(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color, null, 0, false); // colorsIntensity -  colors
  }

  public final boolean isReady(G3MRenderContext rc)
  {
    return true;
  }

  public final IFloatBuffer createTextCoords(Vector2I rawResolution, Tile tile, boolean mercator)
  {
  
    final Sector tileSector = tile.getSector();
    final Sector meshSector = getRenderedSectorForTile(tile);
    final Vector2I tileResolution = calculateResolution(rawResolution, tile, meshSector);
  
    float[] u = new float[tileResolution._x * tileResolution._y];
    float[] v = new float[tileResolution._x * tileResolution._y];
  
    final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(meshSector._lower._latitude);
    final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(meshSector._upper._latitude);
    final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
      for (int j = 0; j < tileResolution._y; j++)
      {
  
        double linearV = (float) j / (tileResolution._y-1);
  
        if (mercator)
        {
          final Angle latitude = meshSector.getInnerPointLatitude(linearV);
          final double mercatorGlobalV = MercatorUtils.getMercatorV(latitude);
          final double mercatorLocalV = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
          linearV = mercatorLocalV;
        }
  
        for (int i = 0; i < tileResolution._x; i++)
        {
          final int pos = j *tileResolution._x + i;
  
          final double linearU = (float) i / (tileResolution._x-1);
          Geodetic2D g = meshSector.getInnerPoint(linearU, linearV);
  
          Vector2D uv = tileSector.getUVCoordinates(g);
          u[pos] = (float)uv._x;
          v[pos] = (float)uv._y;
        }
      }
  
    FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
    for (int j = 0; j < tileResolution._y; j++)
    {
      for (int i = 0; i < tileResolution._x; i++)
      {
        final int pos = j *tileResolution._x + i;
        textCoords.add(u[pos], v[pos]);
      }
    }
  
    // create skirts
    if (_skirted)
    {
      // east side
      for (int j = tileResolution._y-1; j > 0; j--)
      {
        final int pos = j *tileResolution._x + tileResolution._x-1;
        textCoords.add(u[pos], v[pos]);
      }
  
      // north side
      for (int i = tileResolution._x-1; i > 0; i--)
      {
        final int pos = i;
        textCoords.add(u[pos], v[pos]);
      }
  
      // west side
      for (int j = 0; j < tileResolution._y-1; j++)
      {
        final int pos = j *tileResolution._x;
        textCoords.add(u[pos], v[pos]);
      }
  
      // south side
      for (int i = 0; i < tileResolution._x-1; i++)
      {
        final int pos = (tileResolution._y-1) * tileResolution._x + i;
        textCoords.add(u[pos], v[pos]);
      }
    }
  
    // free temp memory
    u = null;
    v = null;
  
    return textCoords.create();
  }

  public final Vector2D getTextCoord(Tile tile, Angle latitude, Angle longitude, boolean mercator)
  {
    final Sector sector = tile.getSector();
  
    final Vector2D linearUV = sector.getUVCoordinates(latitude, longitude);
    if (!mercator)
    {
      return linearUV;
    }
  
    final double lowerGlobalV = MercatorUtils.getMercatorV(sector._lower._latitude);
    final double upperGlobalV = MercatorUtils.getMercatorV(sector._upper._latitude);
    final double deltaGlobalV = lowerGlobalV - upperGlobalV;
  
    final double globalV = MercatorUtils.getMercatorV(latitude);
    final double localV = (globalV - upperGlobalV) / deltaGlobalV;
  
    return new Vector2D(linearUV._x, localV);
  }

}