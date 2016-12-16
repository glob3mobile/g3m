package org.glob3.mobile.generated;
public class PlanetTileTessellator extends TileTessellator
{
  private final boolean _skirted;
  private Sector _renderedSector;

  private Vector2S calculateResolution(PlanetRenderContext prc, Tile tile, Sector renderedSector)
  {
    final Sector sector = tile._sector;
    final Vector2S resolution = prc._layerTilesRenderParameters._tileMeshResolution;
  
    final double latRatio = sector._deltaLatitude._degrees / renderedSector._deltaLatitude._degrees;
    final double lonRatio = sector._deltaLongitude._degrees / renderedSector._deltaLongitude._degrees;
  
    final IMathUtils mu = IMathUtils.instance();
  
    short resX = (short) mu.ceil((resolution._x / lonRatio));
    if (resX < 2)
    {
      resX = 2;
    }
  
    short resY = (short) mu.ceil((resolution._y / latRatio));
    if (resY < 2)
    {
      resY = 2;
    }
  
    return new Vector2S(resX, resY);
  }

  private boolean needsEastSkirt(Sector tileSector)
  {
    if (_renderedSector == null)
    {
      return true;
    }
    return _renderedSector._upper._longitude.greaterThan(tileSector._upper._longitude);
  }

  private boolean needsNorthSkirt(Sector tileSector)
  {
    if (_renderedSector == null)
    {
      return true;
    }
    return _renderedSector._upper._latitude.greaterThan(tileSector._upper._latitude);
  }

  private boolean needsWestSkirt(Sector tileSector)
  {
    if (_renderedSector == null)
    {
      return true;
    }
    return _renderedSector._lower._longitude.lowerThan(tileSector._lower._longitude);
  }

  private boolean needsSouthSkirt(Sector tileSector)
  {
    if (_renderedSector == null)
    {
      return true;
    }
    return _renderedSector._lower._latitude.lowerThan(tileSector._lower._latitude);
  }

  private Sector getRenderedSectorForTile(Tile tile)
  {
    if (_renderedSector == null)
    {
      return tile._sector;
    }
    return tile._sector.intersection(_renderedSector);
  }


  private double createSurfaceVertices(Vector2S meshResolution, Sector meshSector, ElevationData elevationData, float verticalExaggeration, FloatBufferBuilderFromGeodetic vertices, TileTessellatorMeshData tileTessellatorMeshData)
  {
  
    final IMathUtils mu = IMathUtils.instance();
    double minElevation = mu.maxDouble();
    double maxElevation = mu.minDouble();
    double sumElevation = 0;
  
    for (int j = 0; j < meshResolution._y; j++)
    {
      final double v = (double) j / (meshResolution._y - 1);
  
      for (int i = 0; i < meshResolution._x; i++)
      {
        final double u = (double) i / (meshResolution._x - 1);
        final Geodetic2D position = meshSector.getInnerPoint(u, v);
        double elevation = 0;
  
        if (elevationData != null)
        {
          final double rawElevation = elevationData.getElevationAt(position);
  
          elevation = (rawElevation != rawElevation)? 0 : rawElevation * verticalExaggeration;
  
          if (elevation < minElevation)
          {
            minElevation = elevation;
          }
  
          if (elevation > maxElevation)
          {
            maxElevation = elevation;
          }
  
          sumElevation += elevation;
        }
  
        vertices.add(position, elevation);
      }
    }
  
    if (minElevation == mu.maxDouble())
    {
      minElevation = 0;
    }
    if (maxElevation == mu.minDouble())
    {
      maxElevation = 0;
    }
  
    tileTessellatorMeshData._minHeight = minElevation;
    tileTessellatorMeshData._maxHeight = maxElevation;
    tileTessellatorMeshData._averageHeight = sumElevation / (meshResolution._x * meshResolution._y);
  
    return minElevation;
  }

  private double createSurface(Sector tileSector, Sector meshSector, Vector2S meshResolution, ElevationData elevationData, float verticalExaggeration, boolean mercator, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords, TileTessellatorMeshData tileTessellatorMeshData)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final double minElevation = createSurfaceVertices(new Vector2S(meshResolution._x, meshResolution._y), meshSector, elevationData, verticalExaggeration, vertices, tileTessellatorMeshData);
  
  
    //TEX COORDINATES////////////////////////////////////////////////////////////////
  
    if (mercator)
    {
      final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(tileSector._lower._latitude);
      final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(tileSector._upper._latitude);
      final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
      for (int j = 0; j < meshResolution._y; j++)
      {
        final double v = (double) j / (meshResolution._y - 1);
  
        for (int i = 0; i < meshResolution._x; i++)
        {
          final double u = (double) i / (meshResolution._x - 1);
  
          final Angle lat = Angle.linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 1.0 - v);
          final Angle lon = Angle.linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, u);
  
          //U
          final double m_u = tileSector.getUCoordinate(lon);
  
          //V
          final double mercatorGlobalV = MercatorUtils.getMercatorV(lat);
          final double m_v = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
  
          textCoords.add((float)m_u, (float)m_v);
        }
      }
    }
    else
    {
      for (int j = 0; j < meshResolution._y; j++)
      {
        final double v = (double) j / (meshResolution._y - 1);
        for (int i = 0; i < meshResolution._x; i++)
        {
          final double u = (double) i / (meshResolution._x - 1);
          textCoords.add((float)u, (float)v);
        }
      }
    }
  
    //INDEX///////////////////////////////////////////////////////////////
    for (short j = 0; j < (meshResolution._y-1); j++)
    {
      final short jTimesResolution = (short)(j *meshResolution._x);
      if (j > 0)
      {
        indices.add(jTimesResolution);
      }
      for (short i = 0; i < meshResolution._x; i++)
      {
        indices.add((short)(jTimesResolution + i));
        indices.add((short)(jTimesResolution + i + meshResolution._x));
      }
      indices.add((short)(jTimesResolution + 2 *meshResolution._x - 1));
    }
  
    return minElevation;
  }

  private void createEastSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short southEastCorner = (short)((meshResolution._x * meshResolution._y) - 1);
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = southEastCorner;
  
    // east side
    for (short j = (short)(meshResolution._y-1); j >= 0; j--)
    {
      final double x = 1;
      final double y = (double)j/(meshResolution._y-1);
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add(uv);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add(skirtIndex);
  
      skirtIndex++;
      surfaceIndex -= meshResolution._x;
    }
    //Short casts are needed due to widening primitive conversions in java
    //http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
    indices.add((short)(surfaceIndex + meshResolution._x));
    indices.add((short)(surfaceIndex + meshResolution._x));
  }

  private void createNorthSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
    final short northEastCorner = (short)(meshResolution._x - 1);
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = northEastCorner;
  
    indices.add(surfaceIndex);
  
    for (short i = (short)(meshResolution._x-1); i >= 0; i--)
    {
      final double x = (double)i/(meshResolution._x-1);
      final double y = 0;
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add(uv);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add(skirtIndex);
  
      skirtIndex++;
      surfaceIndex -= 1;
    }
  
    indices.add((short)(surfaceIndex + 1));
    indices.add((short)(surfaceIndex + 1));
  }

  private void createWestSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short northWestCorner = (short)0;
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = northWestCorner;
  
    indices.add(surfaceIndex);
  
    for (short j = 0; j < meshResolution._y; j++)
    {
      final double x = 0;
      final double y = (double)j/(meshResolution._y-1);
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add(uv);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add(skirtIndex);
  
      skirtIndex++;
      surfaceIndex += meshResolution._x;
    }
  
    indices.add((short)(surfaceIndex - meshResolution._x));
    indices.add((short)(surfaceIndex - meshResolution._x));
  }

  private void createSouthSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short southWestCorner = (short)(meshResolution._x * (meshResolution._y-1));
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = southWestCorner;
  
    indices.add(surfaceIndex);
  
    for (short i = 0; i < meshResolution._x; i++)
    {
      final double x = (double)i/(meshResolution._x-1);
      final double y = 1;
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add((float)uv._x, (float)uv._y);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add(skirtIndex++);
      surfaceIndex += 1;
    }
  
    indices.add((short)(surfaceIndex - 1));
    indices.add((short)(surfaceIndex - 1));
  }

  private static double skirtDepthForSector(Planet planet, Sector sector)
  {
    final Vector3D se = planet.toCartesian(sector.getSE());
    final Vector3D nw = planet.toCartesian(sector.getNW());
    final double diagonalLength = nw.sub(se).length();
    final double sideLength = diagonalLength * 0.70710678118;
    //0.707 = 1 / SQRT(2) -> diagonalLength => estimated side length
    return sideLength / 20.0;
  }


  public PlanetTileTessellator(boolean skirted, Sector sector)
  {
     _skirted = skirted;
     _renderedSector = sector.isEquals(Sector.FULL_SPHERE)? null : new Sector(sector);
  }

  public void dispose()
  {
    _renderedSector = null;
    super.dispose();
  
  }

  public final Vector2S getTileMeshResolution(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    Sector sector = getRenderedSectorForTile(tile);
    return calculateResolution(prc, tile, sector);
  }

  public final Mesh createTileMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, ElevationData elevationData, DEMGrid grid, TileTessellatorMeshData tileTessellatorMeshData)
  {
  
    if (grid != null)
    {
      final Vector3D minMaxAverageElevations = DEMGridUtils.getMinMaxAverageElevations(grid);
      tileTessellatorMeshData._minHeight = minMaxAverageElevations._x;
      tileTessellatorMeshData._maxHeight = minMaxAverageElevations._y;
      // tileTessellatorMeshData._averageHeight = minMaxAverageElevations._z;
      tileTessellatorMeshData._averageHeight = 0;
  
      return DEMGridUtils.createDebugMesh(grid, rc.getPlanet(), prc._verticalExaggeration, Geodetic3D.zero(), -11000, 9000, 15); // pointSize -  maxElevation -  minElevation -  offset
    }
  
    final Sector tileSector = tile._sector;
    final Sector meshSector = getRenderedSectorForTile(tile);
    final Vector2S meshResolution = calculateResolution(prc, tile, meshSector);
  
    final Planet planet = rc.getPlanet();
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, meshSector._center);
    ShortBufferBuilder indices = new ShortBufferBuilder();
    FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
    final double minElevation = createSurface(tileSector, meshSector, meshResolution, elevationData, prc._verticalExaggeration, tile._mercator, vertices, indices, textCoords, tileTessellatorMeshData);
  
    if (_skirted)
    {
      final double relativeSkirtHeight = minElevation - skirtDepthForSector(planet, tileSector);
  
      double absoluteSkirtHeight = 0;
      if (_renderedSector != null)
      {
        absoluteSkirtHeight = -skirtDepthForSector(planet, _renderedSector);
      }
  
      createEastSkirt(planet, tileSector, meshSector, meshResolution, needsEastSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createNorthSkirt(planet, tileSector, meshSector, meshResolution, needsNorthSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createWestSkirt(planet, tileSector, meshSector, meshResolution, needsWestSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createSouthSkirt(planet, tileSector, meshSector, meshResolution, needsSouthSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
    }
  
    //Storing textCoords in Tile
    tile.setPlanetTileTessellatorData(new PlanetTileTessellatorData(textCoords));
  
    IFloatBuffer verticesB = vertices.create();
    IShortBuffer indicesB = indices.create();
    IFloatBuffer normals = null;
    ///#warning Testing_Terrain_Normals;
    //  IFloatBuffer* normals = NormalsUtils::createTriangleStripSmoothNormals(verticesB, indicesB);
  
    Mesh result = new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), verticesB, true, normals, true, indicesB, true);
  
    if (vertices != null)
       vertices.dispose();
  
  //  if (grid != NULL) {
  //    CompositeMesh* compositeMesh = new CompositeMesh();
  //    compositeMesh->addMesh(result);
  //
  //    Mesh* pointsMesh = DEMGridUtils::createDebugMesh(grid,
  //                                                     rc->getPlanet(),
  //                                                     prc->_verticalExaggeration,
  //                                                     Geodetic3D::zero(), // offset
  //                                                     -11000,             // minElevation
  //                                                     9000,               // maxElevation
  //                                                     15                  // pointSize
  //                                                     );
  //    compositeMesh->addMesh(pointsMesh);
  //
  //    return compositeMesh;
  //  }
  
    return result;
  }

  public final Mesh createTileDebugMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    final Sector meshSector = getRenderedSectorForTile(tile);
    final Vector2S meshResolution = calculateResolution(prc, tile, meshSector);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(rc.getPlanet());
    TileTessellatorMeshData tileTessellatorMeshData = new TileTessellatorMeshData();
    createSurfaceVertices(meshResolution, meshSector, tile.getElevationData(), prc._verticalExaggeration, vertices, tileTessellatorMeshData);
  
    //INDEX OF BORDER///////////////////////////////////////////////////////////////
    ShortBufferBuilder indicesBorder = new ShortBufferBuilder();
    for (short j = 0; j < meshResolution._x; j++)
    {
      indicesBorder.add(j);
    }
  
    for (short i = 2; i < meshResolution._y+1; i++)
    {
      indicesBorder.add((short)((i * meshResolution._x)-1));
    }
  
    for (short j = (short)(meshResolution._x *meshResolution._y-2); j >= (meshResolution._x*(meshResolution._y-1)); j--)
    {
      indicesBorder.add(j);
    }
  
    for (short j = (short)(meshResolution._x*(meshResolution._y-1)-meshResolution._x); j >= 0; j-=meshResolution._x)
    {
      indicesBorder.add(j);
    }
  
    //INDEX OF GRID
    ShortBufferBuilder indicesGrid = new ShortBufferBuilder();
    for (short i = 0; i < meshResolution._y-1; i++)
    {
      short rowOffset = (short)(i * meshResolution._x);
  
      for (short j = 0; j < meshResolution._x; j++)
      {
        indicesGrid.add((short)(rowOffset + j));
        indicesGrid.add((short)(rowOffset + j+meshResolution._x));
      }
      for (short j = (short)((2 *meshResolution._x)-1); j >= meshResolution._x; j--)
      {
        indicesGrid.add((short)(rowOffset + j));
      }
  
    }
  
    final Color levelColor = Color.BLUE.wheelStep(5, tile._level % 5);
    final float gridLineWidth = tile.isElevationDataSolved() || (tile.getElevationData() == null) ? 1.0f : 3.0f;
  
    IndexedMesh border = new IndexedMesh(GLPrimitive.lineStrip(), vertices.getCenter(), vertices.create(), true, indicesBorder.create(), true, 2.0f, 1.0f, Color.newFromRGBA(1.0f, 0.0f, 0.0f, 1.0f), null, false, null, true, 1.0f, 1.0f);
  
    IndexedMesh grid = new IndexedMesh(GLPrimitive.lineStrip(), vertices.getCenter(), vertices.create(), true, indicesGrid.create(), true, gridLineWidth, 1.0f, new Color(levelColor), null, false, null, true, 1.0f, 1.0f);
  
    if (vertices != null)
       vertices.dispose();
  
    CompositeMesh c = new CompositeMesh();
    c.addMesh(grid);
    c.addMesh(border);
  
    return c;
  }

  public final IFloatBuffer createTextCoords(Vector2S rawResolution, Tile tile)
  {
    PlanetTileTessellatorData data = tile.getPlanetTileTessellatorData();
    if ((data == null) || (data._textCoords == null))
    {
      ILogger.instance().logError("Logic error on PlanetTileTessellator::createTextCoord");
      return null;
    }
    return data._textCoords.create();
  }

  public final Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude)
  {
    final Sector sector = tile._sector;
  
    final Vector2F linearUV = sector.getUVCoordinatesF(latitude, longitude);
    if (!tile._mercator)
    {
      return linearUV;
    }
  
    final double lowerGlobalV = MercatorUtils.getMercatorV(sector._lower._latitude);
    final double upperGlobalV = MercatorUtils.getMercatorV(sector._upper._latitude);
    final double deltaGlobalV = lowerGlobalV - upperGlobalV;
  
    final double globalV = MercatorUtils.getMercatorV(latitude);
    final double localV = (globalV - upperGlobalV) / deltaGlobalV;
  
    return new Vector2F(linearUV._x, (float) localV);
  }

  public final void setRenderedSector(Sector sector)
  {
    if (_renderedSector == null || !_renderedSector.isEquals(sector))
    {
      _renderedSector = null;

      if (sector.isEquals(Sector.FULL_SPHERE))
      {
        _renderedSector = null;
      }
      else
      {
        _renderedSector = new Sector(sector);
      }
    }
  }

}
