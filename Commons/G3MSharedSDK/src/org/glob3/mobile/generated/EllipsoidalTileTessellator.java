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

///#include "MutableVector3D.hpp"
///#include "Planet.hpp"

public class EllipsoidalTileTessellator extends TileTessellator
{

  private final int _resolution;
  private final boolean _skirted;

  private short calculateResolution(Sector sector)
  {
    final short resolution = (short) _resolution;
  
  //  /* testing for dynamic latitude-resolution */
  //  double cos = sector.getCenter().latitude().cosinus();
  //  if (cos < 0) {
  //    cos *= -1;
  //  }
  //  short resolution = (short) (_resolution * cos);
  //  if (resolution % 2 == 1) {
  //    resolution += 1;
  //  }
  //  if (resolution < 6) {
  //    resolution = 6;
  //  }
  
    return resolution;
  }


  public EllipsoidalTileTessellator(int resolution, boolean skirted)
  {
     _resolution = resolution;
     _skirted = skirted;
    //    int __TODO_width_and_height_resolutions;
  }

  public void dispose()
  {
  }


  ///#include "FloatBufferBuilderFromCartesian2D.hpp"
  
  
  public final Vector2I getTileMeshResolution(Planet planet, Tile tile, boolean debug)
  {
    final short resolution = calculateResolution(tile.getSector());
    return new Vector2I(resolution, resolution);
  }


  public final Mesh createTileMesh(Planet planet, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean debug)
  {
  
    final Sector sector = tile.getSector();
  
  //  if (elevationData != NULL) {
  //    ILogger::instance()->logInfo("Elevation data for sector=%s", sector.description().c_str());
  //    ILogger::instance()->logInfo("%s", elevationData->description().c_str());
  //  }
  
    //const short resolution = (short) _resolution;
    final short resolution = calculateResolution(sector);
  
    final short resolutionMinus1 = (short)(resolution - 1);
  
  
    double minHeight = 0;
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
    for (int j = 0; j < resolution; j++)
    {
      final double v = (double) j / resolutionMinus1;
      for (int i = 0; i < resolution; i++)
      {
        final double u = (double) i / resolutionMinus1;
  
        float height = 0F;
        if (elevationData != null)
        {
          height = elevationData.getElevationAt(i, j) * verticalExaggeration;
          if (height < minHeight)
          {
            minHeight = height;
          }
        }
  
        vertices.add(sector.getInnerPoint(u, v), height);
      }
    }
  
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
    for (short j = 0; j < resolutionMinus1; j++)
    {
      final short jTimesResolution = (short)(j *resolution);
      if (j > 0)
      {
        indices.add(jTimesResolution);
      }
      for (short i = 0; i < resolution; i++)
      {
        indices.add((short)(jTimesResolution + i));
        indices.add((short)(jTimesResolution + i + resolution));
      }
      indices.add((short)(jTimesResolution + 2 *resolution - 1));
    }
  
  
    // create skirts
    if (_skirted)
    {
      // compute skirt height
      final Vector3D sw = planet.toCartesian(sector.getSW());
      final Vector3D nw = planet.toCartesian(sector.getNW());
      final double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minHeight;
  
      indices.add((short) 0);
      int posS = resolution * resolution;
  
      // west side
      for (int j = 0; j < resolutionMinus1; j++)
      {
        vertices.add(sector.getInnerPoint(0, (double)j/resolutionMinus1), skirtHeight);
  
        indices.add((short)(j *resolution));
        indices.add((short) posS++);
      }
  
      // south side
      for (int i = 0; i < resolutionMinus1; i++)
      {
        vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 1), skirtHeight);
  
        indices.add((short)(resolutionMinus1 *resolution + i));
        indices.add((short) posS++);
      }
  
      // east side
      for (int j = resolutionMinus1; j > 0; j--)
      {
        vertices.add(sector.getInnerPoint(1, (double)j/resolutionMinus1), skirtHeight);
  
        indices.add((short)(j *resolution + resolutionMinus1));
        indices.add((short) posS++);
      }
  
      // north side
      for (int i = resolutionMinus1; i > 0; i--)
      {
        vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 0), skirtHeight);
  
        indices.add((short) i);
        indices.add((short) posS++);
      }
  
      // last triangle
      indices.add((short) 0);
      indices.add((short)(resolution *resolution));
    }
  
    Color color = Color.newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);
  
    return new IndexedMesh(debug ? GLPrimitive.lineStrip() : GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color);
  }

  public final Mesh createTileDebugMesh(Planet planet, Tile tile)
  {
    final Sector sector = tile.getSector();
  
    final int resolutionMinus1 = _resolution - 1;
    short posS = 0;
  
    // compute offset for vertices
    final Vector3D sw = planet.toCartesian(sector.getSW());
    final Vector3D nw = planet.toCartesian(sector.getNW());
    final double offset = nw.sub(sw).length() * 1e-3;
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
  
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    // west side
    for (int j = 0; j < resolutionMinus1; j++)
    {
      vertices.add(sector.getInnerPoint(0, (double)j/resolutionMinus1), offset);
      indices.add(posS++);
    }
  
    // south side
    for (int i = 0; i < resolutionMinus1; i++)
    {
      vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 1), offset);
      indices.add(posS++);
    }
  
    // east side
    for (int j = resolutionMinus1; j > 0; j--)
    {
      vertices.add(sector.getInnerPoint(1, (double)j/resolutionMinus1), offset);
      indices.add(posS++);
    }
  
    // north side
    for (int i = resolutionMinus1; i > 0; i--)
    {
      vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 0), offset);
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
  
  //  const int resolution       = _resolution;
    final short resolution = calculateResolution(tile.getSector());
    final int resolutionMinus1 = resolution - 1;
  
    float[] u = new float[resolution * resolution];
    float[] v = new float[resolution * resolution];
  
    for (int j = 0; j < resolution; j++)
    {
      for (int i = 0; i < resolution; i++)
      {
        final int pos = j *resolution + i;
        u[pos] = (float) i / resolutionMinus1;
        v[pos] = (float) j / resolutionMinus1;
      }
    }
  
    //FloatBufferBuilderFromCartesian2D textCoords;
    int textCoordsSize = (resolution * resolution) * 2;
    if (_skirted)
    {
      textCoordsSize += (resolutionMinus1 * 4) * 2;
    }
    IFloatBuffer textCoords = IFactory.instance().createFloatBuffer(textCoordsSize);
    int textCoordsIndex = 0;
  
    for (int j = 0; j < resolution; j++)
    {
      for (int i = 0; i < resolution; i++)
      {
        final int pos = j *resolution + i;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
    }
  
    // create skirts
    if (_skirted)
    {
      // west side
      for (int j = 0; j < resolutionMinus1; j++)
      {
        final int pos = j *resolution;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // south side
      for (int i = 0; i < resolutionMinus1; i++)
      {
        final int pos = resolutionMinus1 * resolution + i;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // east side
      for (int j = resolutionMinus1; j > 0; j--)
      {
        final int pos = j *resolution + resolutionMinus1;
        //textCoords.add( u[pos], v[pos] );
        textCoords.rawPut(textCoordsIndex++, u[pos]);
        textCoords.rawPut(textCoordsIndex++, v[pos]);
      }
  
      // north side
      for (int i = resolutionMinus1; i > 0; i--)
      {
        final int pos = i;
        //textCoords.add( u[pos], v[pos] );
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