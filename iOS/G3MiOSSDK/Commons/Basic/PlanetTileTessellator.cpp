//
//  PlanetTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "PlanetTileTessellator.hpp"

#include "Tile.hpp"
#include "Context.hpp"
#include "IndexedMesh.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"
#include "FloatBufferBuilder.hpp"
#include "ShortBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "SimpleFloatBufferBuilder.hpp"
#include "GLConstants.hpp"
#include "Color.hpp"
#include "Planet.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "ElevationData.hpp"
#include "MercatorUtils.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"


#include "IndexedGeometryMesh.hpp"

#include "IShortBuffer.hpp"

PlanetTileTessellator::PlanetTileTessellator(const bool skirted, const Sector& sector):
_skirted(skirted),
_renderedSector(sector)
{
}

PlanetTileTessellator::~PlanetTileTessellator() {
//#ifdef C_CODE
//  for (std::map<OrderableVector2I, IShortBuffer*>::iterator it = _indicesMap.begin();
//       it != _indicesMap.end();
//       it++){
//    delete it->second;
//  }
//#endif
//#ifdef JAVA_CODE
//  java.util.Iterator it = _indicesMap.entrySet().iterator();
//  while (it.hasNext()) {
//    java.util.Map.Entry pairs = (java.util.Map.Entry)it.next();
//    IShortBuffer b = (IShortBuffer) pairs.getValue();
//    b.dispose();
//  }
//#endif

#ifdef JAVA_CODE
  super.dispose();
#endif

}

Vector2I PlanetTileTessellator::getTileMeshResolution(const Planet* planet,
                                                      const Vector2I& rawResolution,
                                                      const Tile* tile,
                                                      bool debug) const {
  Sector sector = getRenderedSectorForTile(tile); // tile->getSector();
  return calculateResolution(rawResolution, tile, sector);
}

Vector2I PlanetTileTessellator::calculateResolution(const Vector2I& resolution,
                                                    const Tile* tile,
                                                    const Sector& renderedSector) const {
  Sector sector = tile->getSector();

  const double latRatio = sector.getDeltaLatitude()._degrees / renderedSector.getDeltaLatitude()._degrees;
  const double lonRatio = sector.getDeltaLongitude()._degrees / renderedSector.getDeltaLongitude()._degrees;

  const IMathUtils* mu = IMathUtils::instance();

  int resX = (int) mu->ceil((resolution._x / lonRatio));
  if (resX < 2){
    resX = 2;
  }

  int resY = (int) mu->ceil((resolution._y / latRatio) );
  if (resY < 2){
    resY = 2;
  }

  const Vector2I meshRes = Vector2I(resX, resY);
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

Mesh* PlanetTileTessellator::createTileMesh(const Planet* planet,
                                            const Vector2I& rawResolution,
                                            Tile* tile,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            bool renderDebug) const {

  const Sector tileSector = tile->getSector();
  const Sector meshSector = getRenderedSectorForTile(tile);// tile->getSector();
  const Vector2I meshResolution = calculateResolution(rawResolution, tile, meshSector);

  FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(planet, meshSector._center);
  ShortBufferBuilder indices;
  FloatBufferBuilderFromCartesian2D* textCoords = new FloatBufferBuilderFromCartesian2D();

  double minElevation = createSurface(tileSector,
                                      meshSector,
                                      meshResolution,
                                      elevationData,
                                      verticalExaggeration,
                                      mercator,
                                      vertices,
                                      indices,
                                      *textCoords);

  if (_skirted){

    const Vector3D sw = planet->toCartesian(tileSector.getSW());
    const Vector3D nw = planet->toCartesian(tileSector.getNW());
    //    const double offset = nw.sub(sw).length() * 1e-3;
    const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minElevation;
    //    const double skirtHeight = -1e5; //TODO: CHECK

    if (needsEastSkirt(tileSector)){
      createEastSkirt(planet,
                      tileSector,
                      meshSector,
                      meshResolution,
                      elevationData,
                      verticalExaggeration,
                      mercator,
                      skirtHeight,
                      vertices,
                      indices,
                      *textCoords);
    }

    if (needsNorthSkirt(tileSector)){
      createNorthSkirt(planet,
                       tileSector,
                       meshSector,
                       meshResolution,
                       elevationData,
                       verticalExaggeration,
                       mercator,
                       skirtHeight,
                       vertices,
                       indices,
                       *textCoords);
    }

    if (needsWestSkirt(tileSector)){
      createWestSkirt(planet,
                      tileSector,
                      meshSector,
                      meshResolution,
                      elevationData,
                      verticalExaggeration,
                      mercator,
                      skirtHeight,
                      vertices,
                      indices,
                      *textCoords);
    }

    if (needsSouthSkirt(tileSector)){
      createSouthSkirt(planet,
                       tileSector,
                       meshSector,
                       meshResolution,
                       elevationData,
                       verticalExaggeration,
                       mercator,
                       skirtHeight,
                       vertices,
                       indices,
                       *textCoords);
    }
  }

  //Storing textCoords in Tile
  tile->setTessellatorData(new PlanetTileTessellatorData(textCoords));

  return new IndexedGeometryMesh(GLPrimitive::triangleStrip(),
                                 vertices.getCenter(),
                                 vertices.create(), true,
                                 indices.create(), true,
                                 1,
                                 1);

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

const Vector2D PlanetTileTessellator::getTextCoord(const Tile* tile,
                                                   const Angle& latitude,
                                                   const Angle& longitude,
                                                   bool mercator) const {
  const Sector sector = tile->getSector();

  const Vector2D linearUV = sector.getUVCoordinates(latitude, longitude);
  if (!mercator) {
    return linearUV;
  }

  const double lowerGlobalV = MercatorUtils::getMercatorV(sector._lower._latitude);
  const double upperGlobalV = MercatorUtils::getMercatorV(sector._upper._latitude);
  const double deltaGlobalV = lowerGlobalV - upperGlobalV;

  const double globalV = MercatorUtils::getMercatorV(latitude);
  const double localV  = (globalV - upperGlobalV) / deltaGlobalV;

  return Vector2D(linearUV._x, localV);
}

IFloatBuffer* PlanetTileTessellator::createTextCoords(const Vector2I& rawResolution,
                                                      const Tile* tile,
                                                      bool mercator) const {

  PlanetTileTessellatorData* data = (PlanetTileTessellatorData*) tile->getTessellatorData();
  if (data == NULL || data->_textCoords == NULL){
    ILogger::instance()->logError("Logic error on PlanetTileTessellator::createTextCoord");
    return NULL;
  }
  return data->_textCoords->create();

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


Mesh* PlanetTileTessellator::createTileDebugMesh(const Planet* planet,
                                                 const Vector2I& rawResolution,
                                                 const Tile* tile) const {
  const Sector sector = getRenderedSectorForTile(tile); // tile->getSector();

  const int resolutionXMinus1 = rawResolution._x - 1;
  const int resolutionYMinus1 = rawResolution._y - 1;
  short posS = 0;

  // compute offset for vertices
  const Vector3D sw = planet->toCartesian(sector.getSW());
  const Vector3D nw = planet->toCartesian(sector.getNW());
  const double offset = nw.sub(sw).length() * 1e-3;

  FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(planet, sector._center);
  ShortBufferBuilder indices;

  // west side
  for (int j = 0; j < resolutionYMinus1; j++) {
    vertices.add(sector.getInnerPoint(0, (double)j/resolutionYMinus1),
                 offset);
    indices.add(posS++);
  }

  // south side
  for (int i = 0; i < resolutionXMinus1; i++) {
    vertices.add(sector.getInnerPoint((double)i/resolutionXMinus1, 1),
                 offset);
    indices.add(posS++);
  }

  // east side
  for (int j = resolutionYMinus1; j > 0; j--) {
    vertices.add(sector.getInnerPoint(1, (double)j/resolutionYMinus1),
                 offset);
    indices.add(posS++);
  }

  // north side
  for (int i = resolutionXMinus1; i > 0; i--) {
    vertices.add(sector.getInnerPoint((double)i/resolutionXMinus1, 0),
                 offset);
    indices.add(posS++);
  }

  Color *color = Color::newFromRGBA((float) 1.0, (float) 0.0, (float) 0, (float) 1.0);

  return new IndexedMesh(GLPrimitive::lineLoop(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         1,
                         1,
                         color,
                         NULL, // colors
                         0, // colorsIntensity
                         false);
}

Sector PlanetTileTessellator::getRenderedSectorForTile(const Tile* tile) const{
  return tile->getSector().intersection(_renderedSector);
}

double PlanetTileTessellator::getHeight(const Geodetic2D& g, const ElevationData* elevationData, double verticalExaggeration) const{
  if (elevationData == NULL){
    return 0;
  }
  const double h = elevationData->getElevationAt(g);
  if (IMathUtils::instance()->isNan(h)){
    return 0;
  }

  return h;
}

double PlanetTileTessellator::createSurface(const Sector& tileSector,
                                            const Sector& meshSector,
                                            const Vector2I& meshResolution,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            FloatBufferBuilderFromGeodetic& vertices,
                                            ShortBufferBuilder& indices,
                                            FloatBufferBuilderFromCartesian2D& textCoords) const{

  //CREATING TEXTURE COORDS////////////////////////////////////////////////////////////////
  const double mercatorLowerGlobalV = MercatorUtils::getMercatorV(meshSector._lower._latitude);
  const double mercatorUpperGlobalV = MercatorUtils::getMercatorV(meshSector._upper._latitude);
  const double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;

  for (int j = 0; j < meshResolution._y; j++) {

    double linearV = (float) j / (meshResolution._y-1);

    if (mercator){
      const Angle latitude = meshSector.getInnerPointLatitude(linearV);
      const double mercatorGlobalV = MercatorUtils::getMercatorV(latitude);
      const double mercatorLocalV  = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
      linearV = mercatorLocalV;
    }

    for (int i = 0; i < meshResolution._x; i++) {
      const double linearU = (float) i / (meshResolution._x-1);
      Geodetic2D g = meshSector.getInnerPoint(linearU,linearV);
      Vector2D uv = tileSector.getUVCoordinates(g);
      textCoords.add(uv);
    }
  }

  //VERTICES///////////////////////////////////////////////////////////////
  const IMathUtils* mu = IMathUtils::instance();
  double minElevation = 0;
  for (int j = 0; j < meshResolution._y; j++) {
    const double v = (double) j / (meshResolution._y - 1);

    for (int i = 0; i < meshResolution._x; i++) {
      const double u = (double) i / (meshResolution._x - 1);
      const Geodetic2D position = meshSector.getInnerPoint(u, v);
      double elevation = 0;

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

  //INDEX///////////////////////////////////////////////////////////////
  for (short j = 0; j < (meshResolution._y-1); j++) {
    const short jTimesResolution = (short) (j*meshResolution._x);
    if (j > 0) {
      indices.add(jTimesResolution);
    }
    for (short i = 0; i < meshResolution._x; i++) {
      indices.add((short) (jTimesResolution + i));
      indices.add((short) (jTimesResolution + i + meshResolution._x));
    }
    indices.add((short) (jTimesResolution + 2*meshResolution._x - 1));
  }

  return minElevation;
}

void PlanetTileTessellator::createEastSkirt(const Planet* planet,
                                            const Sector& tileSector,
                                            const Sector& meshSector,
                                            const Vector2I& meshResolution,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            double skirtHeight,
                                            FloatBufferBuilderFromGeodetic& vertices,
                                            ShortBufferBuilder& indices,
                                            FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices.size() / 3);

  const short rx = (short) meshResolution._x;
  const short ry = (short) meshResolution._y;

  const short southEastCorner = (rx * ry) - 1;

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = southEastCorner;

  // east side
  for (int j = ry-1; j >= 0; j--) {
    const double x = 1;
    const double y = (double)j/(ry-1);
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
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

  indices.add(surfaceIndex + rx);
  indices.add(surfaceIndex + rx);
}

void PlanetTileTessellator::createNorthSkirt(const Planet* planet,
                                             const Sector& tileSector,
                                             const Sector& meshSector,
                                             const Vector2I& meshResolution,
                                             const ElevationData* elevationData,
                                             float verticalExaggeration,
                                             bool mercator,
                                             double skirtHeight,
                                             FloatBufferBuilderFromGeodetic& vertices,
                                             ShortBufferBuilder& indices,
                                             FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices.size() / 3);

  const short rx = (short) meshResolution._x;
  //  const short ry = (short) meshResolution._y;

  const short northEastCorner = rx - 1;

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = northEastCorner;

  indices.add(surfaceIndex);

  for (int i = rx-1; i >= 0; i--) {
    const double x = (double)i/(rx-1);
    const double y = 0;
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
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

  indices.add(surfaceIndex + 1);
  indices.add(surfaceIndex + 1);
}

void PlanetTileTessellator::createWestSkirt(const Planet* planet,
                                            const Sector& tileSector,
                                            const Sector& meshSector,
                                            const Vector2I& meshResolution,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            double skirtHeight,
                                            FloatBufferBuilderFromGeodetic& vertices,
                                            ShortBufferBuilder& indices,
                                            FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices.size() / 3);

  const short rx = (short) meshResolution._x;
  const short ry = (short) meshResolution._y;

  const short northWestCorner = 0;

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = northWestCorner;

  indices.add(surfaceIndex);

  for (int j = 0; j < ry; j++) {
    const double x = 0;
    const double y = (double)j/(ry-1);
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
    vertices.add(g, skirtHeight);

    //TEXTURE COORDS/////////////////////////////
    Vector2D uv = textCoords.getVector2D(surfaceIndex);
    textCoords.add(uv);

    if (skirtIndex != (textCoords.size() / 2) - 1){
      printf("WR");
    }

    //INDEX///////////////////////////////////////////////////////////////
    indices.add(surfaceIndex);
    indices.add(skirtIndex);

    skirtIndex++;
    surfaceIndex += rx;
  }

  indices.add(surfaceIndex - rx);
  indices.add(surfaceIndex - rx);
}

void PlanetTileTessellator::createSouthSkirt(const Planet* planet,
                                             const Sector& tileSector,
                                             const Sector& meshSector,
                                             const Vector2I& meshResolution,
                                             const ElevationData* elevationData,
                                             float verticalExaggeration,
                                             bool mercator,
                                             double skirtHeight,
                                             FloatBufferBuilderFromGeodetic& vertices,
                                             ShortBufferBuilder& indices,
                                             FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices.size() / 3);

  const short rx = (short) meshResolution._x;
  const short ry = (short) meshResolution._y;

  const short southWestCorner = rx * (ry-1);

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = southWestCorner;

  indices.add(surfaceIndex);

  for (int i = 0; i < rx; i++) {
    const double x = (double)i/(rx-1);
    const double y = 1;
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
    vertices.add(g, skirtHeight);
    
    //TEXTURE COORDS/////////////////////////////
    Vector2D uv = textCoords.getVector2D(surfaceIndex);
    textCoords.add((float)uv._x, (float)uv._y);
    
    //INDEX///////////////////////////////////////////////////////////////
    indices.add(surfaceIndex);
    indices.add((short) skirtIndex++);
    surfaceIndex += 1;
  }
  
  indices.add(surfaceIndex - 1);
  indices.add(surfaceIndex - 1);
}
