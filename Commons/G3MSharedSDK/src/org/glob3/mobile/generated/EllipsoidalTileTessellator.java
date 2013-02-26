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

public class EllipsoidalTileTessellator extends TileTessellator
{

  private final int _resolutionX;
  private final int _resolutionY;
  private final boolean _skirted;

  private Vector2I calculateResolution(Sector sector)
  {
  //  return Vector2I(_resolutionX, _resolutionY);
  
  
    /* testing for dynamic latitude-resolution */
    final double cos = sector.getCenter().latitude().cosinus();
  
    int resolutionY = (int)(_resolutionY * cos);
    if (resolutionY < 6)
    {
      resolutionY = 6;
    }
  
    int resolutionX = (int)(_resolutionX * cos);
    if (resolutionX < 6)
    {
      resolutionX = 6;
    }
  
    return new Vector2I(resolutionX, resolutionY);
  }


  public EllipsoidalTileTessellator(Vector2I resolution, boolean skirted)
  {
     _resolutionX = resolution._x;
     _resolutionY = resolution._y;
     _skirted = skirted;
    //    int __TODO_width_and_height_resolutions;
  }

  public void dispose()
  {
  }


  ///#include "FloatBufferBuilderFromCartesian2D.hpp"
  
  
  public final Vector2I getTileMeshResolution(Planet planet, Tile tile, boolean debug)
  {
    return calculateResolution(tile.getSector());
  }


  public final Mesh createTileMesh(Planet planet, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean debug)
  {
  
    final Sector sector = tile.getSector();
    final Vector2I resolution = calculateResolution(sector);
  
    double minHeight = 0;
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
    for (int j = 0; j < resolution._y; j++)
    {
      final double v = (double) j / (resolution._y-1);
      for (int i = 0; i < resolution._x; i++)
      {
        final double u = (double) i / (resolution._x-1);
  
        final Geodetic2D position = sector.getInnerPoint(u, v);
  
        double height = 0;
        if (elevationData != null)
        {
  //        height = elevationData->getElevationAt(i, j) * verticalExaggeration;
          height = elevationData.getElevationAt(position.latitude(), position.longitude()) * verticalExaggeration;
          if (height < minHeight)
          {
            minHeight = height;
          }
        }
  
        vertices.add(position, height);
      }
    }
  
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (short j = 0; j < (resolution._y-1); j++)
    {
      final short jTimesResolution = (short)(j *resolution._x);
      if (j > 0)
      {
        indices.add(jTimesResolution);
      }
      for (short i = 0; i < resolution._x; i++)
      {
        indices.add((short)(jTimesResolution + i));
        indices.add((short)(jTimesResolution + i + resolution._x));
      }
      indices.add((short)(jTimesResolution + 2 *resolution._x - 1));
    }
  
  
    // create skirts
    if (_skirted)
    {
      // compute skirt height
      final Vector3D sw = planet.toCartesian(sector.getSW());
      final Vector3D nw = planet.toCartesian(sector.getNW());
      final double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minHeight;
  
      indices.add((short) 0);
      int posS = resolution._x * resolution._y;
  
      // west side
      for (int j = 0; j < resolution._y-1; j++)
      {
        vertices.add(sector.getInnerPoint(0, (double)j/(resolution._y-1)), skirtHeight);
  
        indices.add((short)(j *resolution._x));
        indices.add((short) posS++);
      }
  
      // south side
      for (int i = 0; i < resolution._x-1; i++)
      {
        vertices.add(sector.getInnerPoint((double)i/(resolution._x-1), 1), skirtHeight);
  
        indices.add((short)((resolution._y-1)*resolution._x + i));
        indices.add((short) posS++);
      }
  
      // east side
      for (int j = resolution._y-1; j > 0; j--)
      {
        vertices.add(sector.getInnerPoint(1, (double)j/(resolution._y-1)), skirtHeight);
  
        indices.add((short)(j *resolution._x + (resolution._x-1)));
        indices.add((short) posS++);
      }
  
      // north side
      for (int i = resolution._x-1; i > 0; i--)
      {
        vertices.add(sector.getInnerPoint((double)i/(resolution._x-1), 0), skirtHeight);
  
        indices.add((short) i);
        indices.add((short) posS++);
      }
  
      // last triangle
      indices.add((short) 0);
      indices.add((short)(resolution._x *resolution._y));
    }
  
    Color color = Color.newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);
  
    return new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color); //debug ? GLPrimitive::lineStrip() : GLPrimitive::triangleStrip(),
                           //GLPrimitive::lineStrip(),
  }

  public final Mesh createTileDebugMesh(Planet planet, Tile tile)
  {
    final Sector sector = tile.getSector();
  
    final int resolutionXMinus1 = _resolutionX - 1;
    final int resolutionYMinus1 = _resolutionY - 1;
    short posS = 0;
  
    // compute offset for vertices
    final Vector3D sw = planet.toCartesian(sector.getSW());
    final Vector3D nw = planet.toCartesian(sector.getNW());
    final double offset = nw.sub(sw).length() * 1e-3;
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
  
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
  
    Color color = Color.newFromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0);
  
    return new IndexedMesh(GLPrimitive.lineLoop(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color);
  }

  public final boolean isReady(G3MRenderContext rc)
  {
    return true;
  }

  public final IFloatBuffer createUnitTextCoords(Tile tile)
  {
  
    final Vector2I resolution = calculateResolution(tile.getSector());
  
    float[] u = new float[resolution._x * resolution._y];
    float[] v = new float[resolution._x * resolution._y];
  
    for (int j = 0; j < resolution._y; j++)
    {
      for (int i = 0; i < resolution._x; i++)
      {
        final int pos = j *resolution._x + i;
        u[pos] = (float) i / (resolution._x-1);
        v[pos] = (float) j / (resolution._y-1);
      }
    }
  
    int textCoordsSize = (resolution._x * resolution._y) * 2;
    if (_skirted)
    {
      textCoordsSize += ((resolution._x-1) * (resolution._y-1) * 4) * 2;
    }
  
    IFloatBuffer textCoords = IFactory.instance().createFloatBuffer(textCoordsSize);
  
    int textCoordsIndex = 0;
  
    for (int j = 0; j < resolution._y; j++)
    {
      for (int i = 0; i < resolution._x; i++)
      {
        final int pos = j *resolution._x + i;
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
    }
  
    // create skirts
    if (_skirted)
    {
      // west side
      for (int j = 0; j < resolution._y-1; j++)
      {
        final int pos = j *resolution._x;
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // south side
      for (int i = 0; i < resolution._x-1; i++)
      {
        final int pos = (resolution._y-1) * resolution._x + i;
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // east side
      for (int j = resolution._y-1; j > 0; j--)
      {
        final int pos = j *resolution._x + resolution._x-1;
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // north side
      for (int i = resolution._x-1; i > 0; i--)
      {
        final int pos = i;
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
    }
  
    // free temp memory
    u = null;
    v = null;
  
    //  return textCoords.create();
    return textCoords;
  }

}