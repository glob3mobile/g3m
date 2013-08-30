package org.glob3.mobile.generated; 
//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



//class Sector;
//class IShortBuffer;

public class EllipsoidalTileTessellator extends TileTessellator
{
  private final boolean _skirted;

  private java.util.HashMap<Vector2I, IShortBuffer> _indicesMap = new java.util.HashMap<Vector2I, IShortBuffer>();

  private Vector2I calculateResolution(Vector2I rawResolution, Sector sector)
  {
    return rawResolution;
  
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

  private IShortBuffer getTileIndices(Planet planet, Sector sector, Vector2I tileResolution)
  {
    IShortBuffer indices = _indicesMap.get(tileResolution);
    if (indices == null){
      indices = createTileIndices(planet, sector, tileResolution);
      _indicesMap.put(tileResolution, indices);
    }
    return indices;
  
  }


  public EllipsoidalTileTessellator(boolean skirted)
  {
     _skirted = skirted;

  }

  public void dispose()
  {
    java.util.Iterator it = _indicesMap.entrySet().iterator();
    while (it.hasNext()) {
      java.util.Map.Entry pairs = (java.util.Map.Entry)it.next();
      IShortBuffer b = (IShortBuffer) pairs.getValue();
      b.dispose();
    }
  
    super.dispose();
  
  }

  public final Vector2I getTileMeshResolution(Planet planet, Vector2I rawResolution, Tile tile, boolean debug)
  {
    return calculateResolution(rawResolution, tile.getSector());
  }


  public final Mesh createTileMesh(Planet planet, Vector2I rawResolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean mercator, boolean renderDebug)
  {
  
    final Sector sector = tile.getSector();
    final Vector2I tileResolution = calculateResolution(rawResolution, sector);
  
    double minElevation = 0;
  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
  //                                          planet,
  //                                          sector._center);
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, sector._center);
  
    final IMathUtils mu = IMathUtils.instance();
  
    for (int j = 0; j < tileResolution._y; j++)
    {
      final double v = (double) j / (tileResolution._y - 1);
  
      for (int i = 0; i < tileResolution._x; i++)
      {
        final double u = (double) i / (tileResolution._x - 1);
  
        final Geodetic2D position = sector.getInnerPoint(u, v);
  
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
      final Vector3D sw = planet.toCartesian(sector.getSW());
      final Vector3D nw = planet.toCartesian(sector.getNW());
      final double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minElevation;
  
      // east side
      for (int j = tileResolution._y-1; j > 0; j--)
      {
        vertices.add(sector.getInnerPoint(1, (double)j/(tileResolution._y-1)), skirtHeight);
      }
  
      // north side
      for (int i = tileResolution._x-1; i > 0; i--)
      {
        vertices.add(sector.getInnerPoint((double)i/(tileResolution._x-1), 0), skirtHeight);
      }
  
      // west side
      for (int j = 0; j < tileResolution._y-1; j++)
      {
        vertices.add(sector.getInnerPoint(0, (double)j/(tileResolution._y-1)), skirtHeight);
      }
  
      // south side
      for (int i = 0; i < tileResolution._x-1; i++)
      {
        vertices.add(sector.getInnerPoint((double)i/(tileResolution._x-1), 1), skirtHeight);
      }
    }
  
  //  Color* color = Color::newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);
  
  //  return new IndexedMesh(//renderDebug ? GLPrimitive::lineStrip() : GLPrimitive::triangleStrip(),
  //                         GLPrimitive::triangleStrip(),
  //                         //GLPrimitive::lineStrip(),
  //                         true,
  //                         vertices.getCenter(),
  //                         vertices.create(),
  //                         indices.create(),
  //                         1,
  //                         1,
  //                         color);
  
    return new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, getTileIndices(planet, sector, tileResolution), false, 1, 1);
  }

  public final Mesh createTileDebugMesh(Planet planet, Vector2I rawResolution, Tile tile)
  {
    final Sector sector = tile.getSector();
  
    final int resolutionXMinus1 = rawResolution._x - 1;
    final int resolutionYMinus1 = rawResolution._y - 1;
    short posS = 0;
  
    // compute offset for vertices
    final Vector3D sw = planet.toCartesian(sector.getSW());
    final Vector3D nw = planet.toCartesian(sector.getNW());
    final double offset = nw.sub(sw).length() * 1e-3;
  
  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
  //                                          planet,
  //                                          sector._center);
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
  
    final Vector2I tileResolution = calculateResolution(rawResolution, tile.getSector());
  
    float[] u = new float[tileResolution._x * tileResolution._y];
    float[] v = new float[tileResolution._x * tileResolution._y];
  
    final Sector sector = tile.getSector();
  
    final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(sector._lower._latitude);
    final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(sector._upper._latitude);
    final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
    for (int j = 0; j < tileResolution._y; j++)
    {
      for (int i = 0; i < tileResolution._x; i++)
      {
        final int pos = j *tileResolution._x + i;
  
        u[pos] = (float) i / (tileResolution._x-1);
  
        final double linearV = (double) j / (tileResolution._y-1);
        if (mercator)
        {
          final Angle latitude = sector.getInnerPointLatitude(linearV);
          final double mercatorGlobalV = MercatorUtils.getMercatorV(latitude);
          final double mercatorLocalV = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
          v[pos] = (float) mercatorLocalV;
        }
        else
        {
          v[pos] = (float) linearV;
        }
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
  
    //  return textCoords.create();
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