package org.glob3.mobile.generated; 
public class PlanetTileTessellator extends TileTessellator
{
  private final boolean _skirted;
  private Sector _renderedSector;

  private Vector2I calculateResolution(Vector2I resolution, Tile tile, Sector renderedSector)
  {
    Sector sector = tile._sector;
  
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

  private double createSurface(Sector tileSector, Sector meshSector, Vector2I meshResolution, ElevationData elevationData, float verticalExaggeration, boolean mercator, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords, TileTessellatorMeshData data)
  {
  
    final int rx = meshResolution._x;
    final int ry = meshResolution._y;
  
    final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(tileSector._lower._latitude);
    final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(tileSector._upper._latitude);
    final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
    //VERTICES///////////////////////////////////////////////////////////////
    IMathUtils mu = IMathUtils.instance();
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
          if (!(rawElevation != rawElevation))
          {
            elevation = rawElevation * verticalExaggeration;
  
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
        }
        vertices.add(position, elevation);
  
        //TEXT COORDS
        if (mercator)
        {
          //U
          final double m_u = tileSector.getUCoordinate(position._longitude);
  
          //V
          final double mercatorGlobalV = MercatorUtils.getMercatorV(position._latitude);
          final double m_v = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
  
          textCoords.add((float)m_u, (float)m_v);
        }
        else
        {
          Vector2D uv = tileSector.getUVCoordinates(position);
          textCoords.add(uv);
        }
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



  //
  //  Vector3F.cpp
  //  G3MiOSSDK
  //
  //  Created by Diego Gomez Deck on 2/9/13.
  //
  //
  
  
  
  
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

  public final Vector2I getTileMeshResolution(Planet planet, Vector2I rawResolution, Tile tile, boolean debug)
  {
    Sector sector = getRenderedSectorForTile(tile); // tile->getSector();
    return calculateResolution(rawResolution, tile, sector);
  }


  public final Mesh createTileMesh(Planet planet, Vector2I rawResolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean mercator, boolean renderDebug, TileTessellatorMeshData data)
  {
  
    final Sector tileSector = tile._sector;
    final Sector meshSector = getRenderedSectorForTile(tile); // tile->getSector();
    final Vector2I meshResolution = calculateResolution(rawResolution, tile, meshSector);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, meshSector._center);
    ShortBufferBuilder indices = new ShortBufferBuilder();
    FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
    double minElevation = createSurface(tileSector, meshSector, meshResolution, elevationData, verticalExaggeration, mercator, vertices, indices, textCoords, data);
  
    if (_skirted)
    {
      final Vector3D se = planet.toCartesian(tileSector.getSE());
      final Vector3D nw = planet.toCartesian(tileSector.getNW());
      final double diagonalLength = nw.sub(se).length();
      //0.707 = 1 / SQRT(2) -> diagonalLength => estimated side length
      final double relativeSkirtHeight = (diagonalLength * -0.05 * 0.70710678118) + minElevation;
  
      double absoluteSkirtHeight = 0;
      if (_renderedSector != null)
      {
        final Vector3D ase = planet.toCartesian(_renderedSector.getSE());
        final Vector3D anw = planet.toCartesian(_renderedSector.getNW());
        //0.707 = 1 / SQRT(2) -> diagonalLength => estimated side length
        absoluteSkirtHeight = (anw.sub(ase).length() * -0.05 * 0.70710678118);
      }
  
      createEastSkirt(planet, tileSector, meshSector, meshResolution, needsEastSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createNorthSkirt(planet, tileSector, meshSector, meshResolution, needsNorthSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createWestSkirt(planet, tileSector, meshSector, meshResolution, needsWestSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createSouthSkirt(planet, tileSector, meshSector, meshResolution, needsSouthSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
    }
  
    //Storing textCoords in Tile
    tile.setTessellatorData(new PlanetTileTessellatorData(textCoords));
  
  ///#warning Testing_Terrain_Normals;
    IFloatBuffer verticesB = vertices.create();
    IShortBuffer indicesB = indices.create();
    //IFloatBuffer* normals = NormalsUtils::createTriangleStripSmoothNormals(verticesB, indicesB);
    //IFloatBuffer* normals = NormalsUtils::createTriangleSmoothNormals(verticesB, indicesB);
    IFloatBuffer normals = null;
  
    Mesh result = new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), verticesB, true, normals, true, indicesB, true);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
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
  
    Mesh result = new IndexedMesh(GLPrimitive.lineLoop(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color, null, 0, false); // colorsIntensity -  colors
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  public final boolean isReady(G3MRenderContext rc)
  {
    return true;
  }

  public final IFloatBuffer createTextCoords(Vector2I rawResolution, Tile tile, boolean mercator)
  {
  
    PlanetTileTessellatorData data = (PlanetTileTessellatorData) tile.getTessellatorData();
    if (data == null || data._textCoords == null)
    {
      ILogger.instance().logError("Logic error on PlanetTileTessellator::createTextCoord");
      return null;
    }
    return data._textCoords.create();
  }

  public final Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude, boolean mercator)
  {
    final Sector sector = tile._sector;
  
    final Vector2F linearUV = sector.getUVCoordinatesF(latitude, longitude);
    if (!mercator)
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