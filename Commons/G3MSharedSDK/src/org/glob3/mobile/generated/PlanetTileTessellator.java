package org.glob3.mobile.generated; 
public class PlanetTileTessellator extends TileTessellator
{
  private final boolean _skirted;
  private Sector _renderedSector;

  private Vector2I calculateResolution(PlanetRenderContext prc, Tile tile, Sector renderedSector)
  {
    Sector sector = tile._sector;
    final Vector2I resolution = prc._layerTilesRenderParameters._tileMeshResolution;
  
    final double latRatio = sector._deltaLatitude._degrees / renderedSector._deltaLatitude._degrees;
    final double lonRatio = sector._deltaLongitude._degrees / renderedSector._deltaLongitude._degrees;
  
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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Geodetic3D getGeodeticOnPlanetSurface(IMathUtils mu, Planet planet, ElevationData elevationData, float verticalExaggeration, Geodetic2D g);

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


  private double createSurfaceVertices(int rx, int ry, Sector meshSector, ElevationData elevationData, float verticalExaggeration, FloatBufferBuilderFromGeodetic vertices, TileTessellatorMeshData data)
  {
  
    final IMathUtils mu = IMathUtils.instance();
    double minElevation = mu.maxDouble();
    double maxElevation = mu.minDouble();
    double averageElevation = 0;
  
    for (int j = 0; j < ry; j++)
    {
      final double v = (double) j / (ry - 1);
  
      for (int i = 0; i < rx; i++)
      {
        final double u = (double) i / (rx - 1);
        final Geodetic2D position = meshSector.getInnerPoint(u, v);
        double elevation = 0;
  
        if (elevationData != null)
        {
          final double rawElevation = elevationData.getElevationAt(position);
  
          elevation = (rawElevation != rawElevation)? 0 : rawElevation * verticalExaggeration;
  
          //MIN
          if (elevation < minElevation)
          {
            minElevation = elevation;
          }
  
          //MAX
          if (elevation > maxElevation)
          {
            maxElevation = elevation;
          }
  
          //AVERAGE
          averageElevation += elevation;
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
  
    data._minHeight = minElevation;
    data._maxHeight = maxElevation;
    data._averageHeight = averageElevation / (rx * ry);
  
    return minElevation;
  }

  private double createSurface(Sector tileSector, Sector meshSector, Vector2I meshResolution, ElevationData elevationData, float verticalExaggeration, boolean mercator, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords, TileTessellatorMeshData data)
  {
  
    final int rx = meshResolution._x;
    final int ry = meshResolution._y;
  
    //VERTICES///////////////////////////////////////////////////////////////
    final double minElevation = createSurfaceVertices(rx, ry, meshSector, elevationData, verticalExaggeration, vertices, data);
  
  
    //TEX COORDINATES////////////////////////////////////////////////////////////////
  
    if (mercator) //Mercator
    {
  
      final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(tileSector._lower._latitude);
      final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(tileSector._upper._latitude);
      final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
      for (int j = 0; j < ry; j++)
      {
        final double v = (double) j / (ry - 1);
  
        for (int i = 0; i < rx; i++)
        {
          final double u = (double) i / (rx - 1);
          final Geodetic2D position = meshSector.getInnerPoint(u, v);
          //U
          final double m_u = tileSector.getUCoordinate(position._longitude);
  
          //V
          final double mercatorGlobalV = MercatorUtils.getMercatorV(position._latitude);
          final double m_v = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
  
          textCoords.add((float)m_u, (float)m_v);
        }
      }
  
    } //No mercator
    else
    {
  
      for (int j = 0; j < ry; j++)
      {
        final double v = (double) j / (ry - 1);
        for (int i = 0; i < rx; i++)
        {
          final double u = (double) i / (rx - 1);
          textCoords.add((float)u, (float)v);
        }
      }
  
    }
  
    //INDEX///////////////////////////////////////////////////////////////
    for (short j = 0; j < (ry-1); j++)
    {
      final short jTimesResolution = (short)(j *rx);
      if (j > 0)
      {
        indices.add(jTimesResolution);
      }
      for (short i = 0; i < rx; i++)
      {
        indices.add((short)(jTimesResolution + i));
        indices.add((short)(jTimesResolution + i + rx));
      }
      indices.add((short)(jTimesResolution + 2 *rx - 1));
    }
  
    return minElevation;
  }

  private void createEastSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2I meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short rx = (short) meshResolution._x;
    final short ry = (short) meshResolution._y;
  
    final short southEastCorner = (short)((rx * ry) - 1);
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = southEastCorner;
  
    // east side
    for (int j = ry-1; j >= 0; j--)
    {
      final double x = 1;
      final double y = (double)j/(ry-1);
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add(uv);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add(skirtIndex);
  
      skirtIndex++;
      surfaceIndex -= rx;
    }
  
    indices.add((short)(surfaceIndex + rx));
    indices.add((short)(surfaceIndex + rx));
  }

  private void createNorthSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2I meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short rx = (short) meshResolution._x;
    //  const short ry = (short) meshResolution._y;
  
    final short northEastCorner = (short)(rx - 1);
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = northEastCorner;
  
    indices.add(surfaceIndex);
  
    for (int i = rx-1; i >= 0; i--)
    {
      final double x = (double)i/(rx-1);
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

  private void createWestSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2I meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short rx = (short) meshResolution._x;
    final short ry = (short) meshResolution._y;
  
    final short northWestCorner = (short)0;
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = northWestCorner;
  
    indices.add(surfaceIndex);
  
    for (int j = 0; j < ry; j++)
    {
      final double x = 0;
      final double y = (double)j/(ry-1);
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add(uv);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add(skirtIndex);
  
      skirtIndex++;
      surfaceIndex += rx;
    }
  
    indices.add((short)(surfaceIndex - rx));
    indices.add((short)(surfaceIndex - rx));
  }

  private void createSouthSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2I meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    //VERTICES///////////////////////////////////////////////////////////////
    final short firstSkirtVertex = (short)(vertices.size() / 3);
  
    final short rx = (short) meshResolution._x;
    final short ry = (short) meshResolution._y;
  
    final short southWestCorner = (short)(rx * (ry-1));
  
    short skirtIndex = firstSkirtVertex;
    short surfaceIndex = southWestCorner;
  
    indices.add(surfaceIndex);
  
    for (int i = 0; i < rx; i++)
    {
      final double x = (double)i/(rx-1);
      final double y = 1;
      final Geodetic2D g = meshSector.getInnerPoint(x, y);
      vertices.add(g, skirtHeight);
  
      //TEXTURE COORDS/////////////////////////////
      Vector2D uv = textCoords.getVector2D(surfaceIndex);
      textCoords.add((float)uv._x, (float)uv._y);
  
      //INDEX///////////////////////////////////////////////////////////////
      indices.add(surfaceIndex);
      indices.add((short) skirtIndex++);
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
     _renderedSector = sector.isEquals(Sector.fullSphere())? null : new Sector(sector);
  }

  public void dispose()
  {
    _renderedSector = null;
    super.dispose();
  
  }

  public final Vector2I getTileMeshResolution(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    Sector sector = getRenderedSectorForTile(tile);
    return calculateResolution(prc, tile, sector);
  }


  public final Mesh createTileMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, ElevationData elevationData, TileTessellatorMeshData data)
  {
  
    final Sector tileSector = tile._sector;
    final Sector meshSector = getRenderedSectorForTile(tile); // tile->getSector();
    final Vector2I meshResolution = calculateResolution(prc, tile, meshSector);
  
    final Planet planet = rc.getPlanet();
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, meshSector._center);
    ShortBufferBuilder indices = new ShortBufferBuilder();
    FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
    final double minElevation = createSurface(tileSector, meshSector, meshResolution, elevationData, prc._verticalExaggeration, tile._mercator, vertices, indices, textCoords, data);
  
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
    tile.setTessellatorData(new PlanetTileTessellatorData(textCoords));
  
    IFloatBuffer verticesB = vertices.create();
    IShortBuffer indicesB = indices.create();
    IFloatBuffer normals = null;
    ///#warning Testing_Terrain_Normals;
    //  IFloatBuffer* normals = NormalsUtils::createTriangleStripSmoothNormals(verticesB, indicesB);
  
    Mesh result = new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), verticesB, true, normals, true, indicesB, true);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  public final Mesh createTileDebugMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
  
  
  
  
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO for JM!
    final Sector meshSector = getRenderedSectorForTile(tile);
    final Vector2I meshResolution = calculateResolution(prc, tile, meshSector);
    final short rx = (short)meshResolution._x;
    final short ry = (short)meshResolution._y;
  
  //  AbstractGeometryMesh* mesh = NULL;// ((AbstractGeometryMesh*)tile->getTessellatorMesh());
  //  const IFloatBuffer* vertices = mesh->getVertices();
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(rc.getPlanet());
    TileTessellatorMeshData data = new TileTessellatorMeshData();
    createSurfaceVertices((int)rx, (int)ry, meshSector, tile.getElevationData(), prc._verticalExaggeration, vertices, data);
  
    //INDEX OF BORDER///////////////////////////////////////////////////////////////
    ShortBufferBuilder indicesBorder = new ShortBufferBuilder();
    for (short j = 0; j < rx; j++)
    {
      indicesBorder.add(j);
    }
  
    for (short i = 2; i < ry+1; i++)
    {
      indicesBorder.add((short)((i * rx)-1));
    }
  
    for (short j = (short)(rx *ry-2); j >= (short)(rx*(ry-1)); j--)
    {
      indicesBorder.add(j);
    }
  
    for (short j = (short)(rx*(ry-1)-rx); j >= 0; j-=rx)
    {
      indicesBorder.add(j);
    }
  
    //INDEX OF GRID
    ShortBufferBuilder indicesGrid = new ShortBufferBuilder();
    for (short i = 0; i < ry-1; i++)
    {
      short rowOffset = (short)(i * rx);
  
      for (short j = 0; j < rx; j++)
      {
        indicesGrid.add((short)(rowOffset + j));
        indicesGrid.add((short)(rowOffset + j+rx));
      }
      for (short j = (short)((2 *rx)-1); j >= rx; j--)
      {
        indicesGrid.add((short)(rowOffset + j));
      }
  
    }
  
    final Color levelColor = Color.blue().wheelStep(5, tile._level % 5);
    final float gridLineWidth = tile.isElevationDataSolved() || (tile.getElevationData() == null) ? 1.0f : 3.0f;
  
  
    IndexedMesh border = new IndexedMesh(GLPrimitive.lineStrip(), vertices.getCenter(), vertices.create(), true, indicesBorder.create(), true, 2.0f, 1.0f, Color.newFromRGBA(1.0f, 0.0f, 0.0f, 1.0f), null, 1.0f, false, null, true, 1.0f, 1.0f);
  
    IndexedMesh grid = new IndexedMesh(GLPrimitive.lineStrip(), vertices.getCenter(), vertices.create(), true, indicesGrid.create(), true, gridLineWidth, 1.0f, new Color(levelColor), null, 1.0f, false, null, true, 1.0f, 1.0f);
  
    if (vertices != null)
       vertices.dispose();
  
    CompositeMesh c = new CompositeMesh();
    c.addMesh(grid);
    c.addMesh(border);
  
    return c;
  }

  public final IFloatBuffer createTextCoords(Vector2I rawResolution, Tile tile)
  {
  
    PlanetTileTessellatorData data = (PlanetTileTessellatorData) tile.getTessellatorData();
    if (data == null || data._textCoords == null)
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

      if (sector.isEquals(Sector.fullSphere()))
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