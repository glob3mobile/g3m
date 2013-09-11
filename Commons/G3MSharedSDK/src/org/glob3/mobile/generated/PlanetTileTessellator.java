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
//class FloatBufferBuilderFromGeodetic;
//class ShortBufferBuilder;

public class PlanetTileTessellator extends TileTessellator
{
  private final boolean _skirted;
  private final Sector _renderedSector ;

///#ifdef C_CODE
//  class OrderableVector2I: public Vector2I{
//  public:
//    OrderableVector2I(const Vector2I v): Vector2I(v){}
//    bool operator<(const Vector2I& that) const{
//      return _x < that._x;
//    }
//  };
//  mutable std::map<OrderableVector2I, IShortBuffer*> _indicesMap; //Resolution vs Indices
///#endif
///#ifdef JAVA_CODE
//  private java.util.HashMap<Vector2I, IShortBuffer> _indicesMap = new java.util.HashMap<Vector2I, IShortBuffer>();
///#endif

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

//  IShortBuffer* createTileIndices(const Planet* planet,
//                                  const Sector& sector,
//                                  const Vector2I& tileResolution) const;

//  IShortBuffer* getTileIndices(const Planet* planet, const Sector& sector,
//                               const Vector2I& tileResolution, bool reusableIndices) const;

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

  private double createSurface(Sector tileSector, Sector meshSector, Vector2I meshResolution, ElevationData elevationData, float verticalExaggeration, boolean mercator, FloatBufferBuilderFromGeodetic vertices, ShortBufferBuilder indices, FloatBufferBuilderFromCartesian2D textCoords)
  {
  
    final int rx = meshResolution._x;
    final int ry = meshResolution._y;
  
    //CREATING TEXTURE COORDS////////////////////////////////////////////////////////////////
    final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(tileSector._lower._latitude);
    final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(tileSector._upper._latitude);
    final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
    //VERTICES///////////////////////////////////////////////////////////////
    final IMathUtils mu = IMathUtils.instance();
    double minElevation = 0;
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

  private static class PlanetTileTessellatorData implements ITessellatorData
  {
    public FloatBufferBuilderFromCartesian2D _textCoords;
    public PlanetTileTessellatorData(FloatBufferBuilderFromCartesian2D textCoords)
    {
       _textCoords = textCoords;
    }

    public void dispose()
    {
    }
  }


  public PlanetTileTessellator(boolean skirted, Sector sector)
  {
     _skirted = skirted;
     _renderedSector = new Sector(sector);
  }

  public void dispose()
  {
    ///#ifdef C_CODE
    //  for (std::map<OrderableVector2I, IShortBuffer*>::iterator it = _indicesMap.begin();
    //       it != _indicesMap.end();
    //       it++){
    //    delete it->second;
    //  }
    ///#endif
    ///#ifdef JAVA_CODE
    //  java.util.Iterator it = _indicesMap.entrySet().iterator();
    //  while (it.hasNext()) {
    //    java.util.Map.Entry pairs = (java.util.Map.Entry)it.next();
    //    IShortBuffer b = (IShortBuffer) pairs.getValue();
    //    b.dispose();
    //  }
    ///#endif
  
    super.dispose();
  
  }

  public final Vector2I getTileMeshResolution(Planet planet, Vector2I rawResolution, Tile tile, boolean debug)
  {
    Sector sector = getRenderedSectorForTile(tile); // tile->getSector();
    return calculateResolution(rawResolution, tile, sector);
  }



  /*
   IShortBuffer* PlanetTileTessellator::createTileIndices(const Planet* planet,
   const Sector& sector,
   const Vector2I& tileResolution) const{
  
   ShortBufferBuilder indices;
   for (short j = 0; j < (tileResolution._y-1); j++) {
   const short jTimesResolution = (short) (j*tileResolution._x);
   if (j > 0) {
   indices.add(jTimesResolution);
   }
   for (short i = 0; i < tileResolution._x; i++) {
   indices.add((short) (jTimesResolution + i));
   indices.add((short) (jTimesResolution + i + tileResolution._x));
   }
   indices.add((short) (jTimesResolution + 2*tileResolution._x - 1));
   }
  
   // create skirts
   if (_skirted) {
  
   int skirtIndexCursor = tileResolution._x * tileResolution._y;
   indices.add((short) (skirtIndexCursor-1));
  
   // east side
   for (int j = tileResolution._y-1; j > 0; j--) {
   indices.add((short) (j*tileResolution._x + (tileResolution._x-1)));
   indices.add((short) skirtIndexCursor++);
   }
  
   // north side
   for (int i = tileResolution._x-1; i > 0; i--) {
   indices.add((short) i);
   indices.add((short) skirtIndexCursor++);
   }
  
   // west side
   for (int j = 0; j < tileResolution._y-1; j++) {
   indices.add((short) (j*tileResolution._x));
   indices.add((short) skirtIndexCursor++);
   }
  
   // south side
   for (int i = 0; i < tileResolution._x-1; i++) {
   indices.add((short) ((tileResolution._y-1)*tileResolution._x + i));
   indices.add((short) skirtIndexCursor++);
   }
  
   // last triangle
   indices.add((short) ((tileResolution._x*tileResolution._y)-1));
   indices.add((short) (tileResolution._x*tileResolution._y));
   }
  
   return indices.create();
   }
   */
  /*
   IShortBuffer* PlanetTileTessellator::getTileIndices(const Planet* planet,
   const Sector& sector,
   const Vector2I& tileResolution,
   bool reusableIndices) const{
   if (reusableIndices){
   return createTileIndices(planet, sector, tileResolution);
   }
  
   #ifdef C_CODE
   std::map<OrderableVector2I, IShortBuffer*>::iterator it = _indicesMap.find(OrderableVector2I(tileResolution));
   if (it != _indicesMap.end()){
   return it->second;
   }
  
   IShortBuffer* indices = createTileIndices(planet, sector, tileResolution);
   _indicesMap[tileResolution] = indices;
  
   return indices;
   #endif
   #ifdef JAVA_CODE
   IShortBuffer indices = _indicesMap.get(tileResolution);
   if (indices == null){
   indices = createTileIndices(planet, sector, tileResolution);
   _indicesMap.put(tileResolution, indices);
   }
   return indices;
   #endif
  
   }
   */
  
  public final Mesh createTileMesh(Planet planet, Vector2I rawResolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean mercator, boolean renderDebug)
  {
  
    final Sector tileSector = tile.getSector();
    final Sector meshSector = getRenderedSectorForTile(tile); // tile->getSector();
    final Vector2I meshResolution = calculateResolution(rawResolution, tile, meshSector);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, meshSector._center);
    ShortBufferBuilder indices = new ShortBufferBuilder();
    FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
    double minElevation = createSurface(tileSector, meshSector, meshResolution, elevationData, verticalExaggeration, mercator, vertices, indices, textCoords);
  
    if (_skirted)
    {
  
      final Vector3D sw = planet.toCartesian(tileSector.getSW());
      final Vector3D nw = planet.toCartesian(tileSector.getNW());
      //    const double offset = nw.sub(sw).length() * 1e-3;
      final double relativeSkirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minElevation;
      final double absoluteSkirtHeight = -1e5; //TODO: CHECK
  
      createEastSkirt(planet, tileSector, meshSector, meshResolution, needsEastSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createNorthSkirt(planet, tileSector, meshSector, meshResolution, needsNorthSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createWestSkirt(planet, tileSector, meshSector, meshResolution, needsWestSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
  
      createSouthSkirt(planet, tileSector, meshSector, meshResolution, needsSouthSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, textCoords);
    }
  
    //Storing textCoords in Tile
    tile.setTessellatorData(new PlanetTileTessellatorData(textCoords));
  
    return new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, indices.create(), true, 1, 1);
  
    ///////////////////////////////////////////////
    /*
     double minElevation = 0;
  
     const IMathUtils* mu = IMathUtils::instance();
  
     for (int j = 0; j < meshResolution._y; j++) {
     const double v = (double) j / (meshResolution._y - 1);
  
     for (int i = 0; i < meshResolution._x; i++) {
     const double u = (double) i / (meshResolution._x - 1);
  
     const Geodetic2D position = meshSector.getInnerPoint(u, v);
  
     double elevation = 0;
  
     //TODO: MERCATOR!!!
  
     if (elevationData != NULL) {
     const double rawElevation = elevationData->getElevationAt(position);
     if ( !mu->isNan(rawElevation) ) {
     elevation = rawElevation * verticalExaggeration;
  
     if (elevation < minElevation) {
     minElevation = elevation;
     }
     }
     }
     vertices.add( position, elevation );
     }
     }
  
     // create skirts
     if (_skirted) {
     // compute skirt height
     const Vector3D sw = planet->toCartesian(meshSector.getSW());
     const Vector3D nw = planet->toCartesian(meshSector.getNW());
     const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minElevation;
  
     // east side
     bool hasSkirt = needsEastSkirt(tileSector);
     for (int j = meshResolution._y-1; j > 0; j--) {
  
     const double x = 1;
     const double y = (double)j/(meshResolution._y-1);
     const Geodetic2D g = meshSector.getInnerPoint(x, y);
  
     double h = skirtHeight;
     if (!hasSkirt){
     h = getHeight(g, elevationData, verticalExaggeration);
     }
  
     vertices.add(g, h);
     }
  
     // north side
     hasSkirt = needsNorthSkirt(tileSector);
     for (int i = meshResolution._x-1; i > 0; i--) {
     const double x = (double)i/(meshResolution._x-1);
     const double y = 0;
     const Geodetic2D g = meshSector.getInnerPoint(x, y);
  
     double h = skirtHeight;
     if (!hasSkirt){
     h = getHeight(g, elevationData, verticalExaggeration);
     }
  
     vertices.add(g, h);
     }
  
     // west side
     hasSkirt = needsWestSkirt(tileSector);
     for (int j = 0; j < meshResolution._y-1; j++) {
     const double x = 0;
     const double y = (double)j/(meshResolution._y-1);
     const Geodetic2D g = meshSector.getInnerPoint(x, y);
  
     double h = skirtHeight;
     if (!hasSkirt){
     h = getHeight(g, elevationData, verticalExaggeration);
     }
  
     vertices.add(g, h);
     }
  
     // south side
     hasSkirt = needsSouthSkirt(tileSector);
     for (int i = 0; i < meshResolution._x-1; i++) {
     const double x = (double)i/(meshResolution._x-1);
     const double y = 1;
     const Geodetic2D g = meshSector.getInnerPoint(x, y);
  
     double h = skirtHeight;
     if (!hasSkirt){
     h = getHeight(g, elevationData, verticalExaggeration);
     }
  
     vertices.add(g, h);
     }
     }
  
     const bool reusableMeshIndex = _renderedSector.fullContains(meshSector);
  
     return new IndexedGeometryMesh(GLPrimitive::triangleStrip(),
     vertices.getCenter(),
     vertices.create(), true,
     getTileIndices(planet, meshSector, meshResolution, reusableMeshIndex), !reusableMeshIndex,
     1,
     1);
     */
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
  
    PlanetTileTessellatorData data = (PlanetTileTessellatorData) tile.getTessellatorData();
    if (data == null || data._textCoords == null)
    {
      ILogger.instance().logError("Logic error on PlanetTileTessellator::createTextCoord");
      return null;
    }
    return data._textCoords.create();
  
    ////////////////////////////////////////////////////////////
    /*
     const Sector tileSector = tile->getSector();
     const Sector meshSector = getRenderedSectorForTile(tile);
     const Vector2I tileResolution = calculateResolution(rawResolution, tile, meshSector);
  
     float* u = new float[tileResolution._x * tileResolution._y];
     float* v = new float[tileResolution._x * tileResolution._y];
  
     const double mercatorLowerGlobalV = MercatorUtils::getMercatorV(meshSector._lower._latitude);
     const double mercatorUpperGlobalV = MercatorUtils::getMercatorV(meshSector._upper._latitude);
     const double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
     for (int j = 0; j < tileResolution._y; j++) {
  
     double linearV = (float) j / (tileResolution._y-1);
  
     if (mercator){
     const Angle latitude = meshSector.getInnerPointLatitude(linearV);
     const double mercatorGlobalV = MercatorUtils::getMercatorV(latitude);
     const double mercatorLocalV  = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
     linearV = mercatorLocalV;
     }
  
     for (int i = 0; i < tileResolution._x; i++) {
     const int pos = j*tileResolution._x + i;
  
     const double linearU = (float) i / (tileResolution._x-1);
     Geodetic2D g = meshSector.getInnerPoint(linearU,linearV);
  
     Vector2D uv = tileSector.getUVCoordinates(g);
     u[pos] = (float)uv._x;
     v[pos] = (float)uv._y;
     }
     }
  
     FloatBufferBuilderFromCartesian2D textCoords;
  
     for (int j = 0; j < tileResolution._y; j++) {
     for (int i = 0; i < tileResolution._x; i++) {
     const int pos = j*tileResolution._x + i;
     textCoords.add(u[pos], v[pos]);
     }
     }
  
     // create skirts
     if (_skirted) {
     // east side
     for (int j = tileResolution._y-1; j > 0; j--) {
     const int pos = j*tileResolution._x + tileResolution._x-1;
     textCoords.add(u[pos], v[pos]);
     }
  
     // north side
     for (int i = tileResolution._x-1; i > 0; i--) {
     const int pos = i;
     textCoords.add(u[pos], v[pos]);
     }
  
     // west side
     for (int j = 0; j < tileResolution._y-1; j++) {
     const int pos = j*tileResolution._x;
     textCoords.add(u[pos], v[pos]);
     }
  
     // south side
     for (int i = 0; i < tileResolution._x-1; i++) {
     const int pos = (tileResolution._y-1) * tileResolution._x + i;
     textCoords.add(u[pos], v[pos]);
     }
     }
  
     // free temp memory
     delete[] u;
     delete[] v;
  
     return textCoords.create();
  
     */
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