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
#include "ShortBufferBuilder.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
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

#include "NormalsUtils.hpp"


PlanetTileTessellator::PlanetTileTessellator(const bool skirted, const Sector& sector):
_skirted(skirted),
_renderedSector(sector.isEquals(Sector::fullSphere())? NULL : new Sector(sector))
{
}

PlanetTileTessellator::~PlanetTileTessellator() {
  delete _renderedSector;
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
  Sector sector = tile->_sector;

  const double latRatio = sector._deltaLatitude._degrees  / renderedSector._deltaLatitude._degrees;
  const double lonRatio = sector._deltaLongitude._degrees / renderedSector._deltaLongitude._degrees;

  const IMathUtils* mu = IMathUtils::instance();

  int resX = (int) mu->ceil((resolution._x / lonRatio));
  if (resX < 2) {
    resX = 2;
  }

  int resY = (int) mu->ceil((resolution._y / latRatio) );
  if (resY < 2) {
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

double PlanetTileTessellator::skirtDepthForSector(const Planet* planet, const Sector& sector){

  const Vector3D se = planet->toCartesian(sector.getSE());
  const Vector3D nw = planet->toCartesian(sector.getNW());
  const double diagonalLength = nw.sub(se).length();
  const double sideLength = diagonalLength * 0.70710678118;
  //0.707 = 1 / SQRT(2) -> diagonalLength => estimated side length
  return sideLength / 20.0;
}


Mesh* PlanetTileTessellator::createTileMesh(const Planet* planet,
                                            const Vector2I& rawResolution,
                                            Tile* tile,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            bool renderDebug,
                                            TileTessellatorMeshData& data) const {

  const Sector tileSector = tile->_sector;
  const Sector meshSector = getRenderedSectorForTile(tile);// tile->getSector();
  const Vector2I meshResolution = calculateResolution(rawResolution, tile, meshSector);

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(planet, meshSector._center);
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
                                      *textCoords,
                                      data);

  if (_skirted) {
    const double relativeSkirtHeight = minElevation - skirtDepthForSector(planet, tileSector);

    double absoluteSkirtHeight = 0;
    if (_renderedSector != NULL) {
#ifdef C_CODE
      absoluteSkirtHeight = - skirtDepthForSector(planet, *_renderedSector);
#endif
#ifdef JAVA_CODE
      absoluteSkirtHeight = - skirtDepthForSector(planet, _renderedSector);
#endif
    }

    createEastSkirt(planet,
                    tileSector,
                    meshSector,
                    meshResolution,
                    needsEastSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight,
                    vertices,
                    indices,
                    *textCoords);

    createNorthSkirt(planet,
                     tileSector,
                     meshSector,
                     meshResolution,
                     needsNorthSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight,
                     vertices,
                     indices,
                     *textCoords);

    createWestSkirt(planet,
                    tileSector,
                    meshSector,
                    meshResolution,
                    needsWestSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight,
                    vertices,
                    indices,
                    *textCoords);

    createSouthSkirt(planet,
                     tileSector,
                     meshSector,
                     meshResolution,
                     needsSouthSkirt(tileSector)? relativeSkirtHeight : absoluteSkirtHeight,
                     vertices,
                     indices,
                     *textCoords);
  }

  //Storing textCoords in Tile
  tile->setTessellatorData(new PlanetTileTessellatorData(textCoords));

  IFloatBuffer* verticesB = vertices->create();
  IShortBuffer* indicesB  = indices.create();
  IFloatBuffer* normals = NULL;
//#warning Testing_Terrain_Normals;
//  IFloatBuffer* normals = NormalsUtils::createTriangleStripSmoothNormals(verticesB, indicesB);

  Mesh* result = new IndexedGeometryMesh(GLPrimitive::triangleStrip(),
                                         vertices->getCenter(),
                                         verticesB, true,
                                         normals,   true,
                                         indicesB,  true);

  delete vertices;

  return result;
}

const Vector2F PlanetTileTessellator::getTextCoord(const Tile* tile,
                                                   const Angle& latitude,
                                                   const Angle& longitude,
                                                   bool mercator) const {
  const Sector sector = tile->_sector;

  const Vector2F linearUV = sector.getUVCoordinatesF(latitude, longitude);
  if (!mercator) {
    return linearUV;
  }

  const double lowerGlobalV = MercatorUtils::getMercatorV(sector._lower._latitude);
  const double upperGlobalV = MercatorUtils::getMercatorV(sector._upper._latitude);
  const double deltaGlobalV = lowerGlobalV - upperGlobalV;

  const double globalV = MercatorUtils::getMercatorV(latitude);
  const double localV  = (globalV - upperGlobalV) / deltaGlobalV;

  return Vector2F(linearUV._x, (float) localV);
}

IFloatBuffer* PlanetTileTessellator::createTextCoords(const Vector2I& rawResolution,
                                                      const Tile* tile,
                                                      bool mercator) const {

  PlanetTileTessellatorData* data = (PlanetTileTessellatorData*) tile->getTessellatorData();
  if (data == NULL || data->_textCoords == NULL) {
    ILogger::instance()->logError("Logic error on PlanetTileTessellator::createTextCoord");
    return NULL;
  }
  return data->_textCoords->create();
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

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(planet, sector._center);
  ShortBufferBuilder indices;

  // west side
  for (int j = 0; j < resolutionYMinus1; j++) {
    vertices->add(sector.getInnerPoint(0, (double)j/resolutionYMinus1),
                  offset);
    indices.add(posS++);
  }

  // south side
  for (int i = 0; i < resolutionXMinus1; i++) {
    vertices->add(sector.getInnerPoint((double)i/resolutionXMinus1, 1),
                  offset);
    indices.add(posS++);
  }

  // east side
  for (int j = resolutionYMinus1; j > 0; j--) {
    vertices->add(sector.getInnerPoint(1, (double)j/resolutionYMinus1),
                  offset);
    indices.add(posS++);
  }

  // north side
  for (int i = resolutionXMinus1; i > 0; i--) {
    vertices->add(sector.getInnerPoint((double)i/resolutionXMinus1, 0),
                  offset);
    indices.add(posS++);
  }

  Color *color = Color::newFromRGBA((float) 1.0, (float) 0.0, (float) 0, (float) 1.0);

  Mesh* result = new IndexedMesh(GLPrimitive::lineLoop(),
                                 true,
                                 vertices->getCenter(),
                                 vertices->create(),
                                 indices.create(),
                                 1,
                                 1,
                                 color,
                                 NULL, // colors
                                 0, // colorsIntensity
                                 false);

  delete vertices;

  return result;
}

Sector PlanetTileTessellator::getRenderedSectorForTile(const Tile* tile) const{
  if (_renderedSector == NULL) {
    return tile->_sector;
  }
#ifdef C_CODE
  return tile->_sector.intersection(*_renderedSector);
#endif
#ifdef JAVA_CODE
  return tile._sector.intersection(_renderedSector);
#endif
}

double PlanetTileTessellator::createSurface(const Sector& tileSector,
                                            const Sector& meshSector,
                                            const Vector2I& meshResolution,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            FloatBufferBuilderFromGeodetic* vertices,
                                            ShortBufferBuilder& indices,
                                            FloatBufferBuilderFromCartesian2D& textCoords,
                                            TileTessellatorMeshData& data) const{

  const int rx = meshResolution._x;
  const int ry = meshResolution._y;

  const double mercatorLowerGlobalV = MercatorUtils::getMercatorV(tileSector._lower._latitude);
  const double mercatorUpperGlobalV = MercatorUtils::getMercatorV(tileSector._upper._latitude);
  const double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;

  //VERTICES///////////////////////////////////////////////////////////////
  IMathUtils* mu = IMathUtils::instance();
  double minElevation = mu->maxDouble();
  double maxElevation = mu->minDouble();
  double averageElevation = 0;
  for (int j = 0; j < ry; j++) {
    const double v = (double) j / (ry - 1);

    for (int i = 0; i < rx; i++) {
      const double u = (double) i / (rx - 1);
      const Geodetic2D position = meshSector.getInnerPoint(u, v);
      double elevation = 0;

      if (elevationData != NULL) {
        const double rawElevation = elevationData->getElevationAt(position);

        elevation = ISNAN(rawElevation)? 0 : rawElevation * verticalExaggeration;

        //MIN
        if (elevation < minElevation) {
          minElevation = elevation;
        }

        //MAX
        if (elevation > maxElevation) {
          maxElevation = elevation;
        }

        //AVERAGE
        averageElevation += elevation;
      }

      vertices->add( position, elevation );

      //TEXT COORDS
      if (mercator) {
        //U
        const double m_u = tileSector.getUCoordinate(position._longitude);

        //V
        const double mercatorGlobalV = MercatorUtils::getMercatorV(position._latitude);
        const double m_v = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;

        textCoords.add((float)m_u, (float)m_v);
      }
      else {
        Vector2D uv = tileSector.getUVCoordinates(position);
        textCoords.add(uv);
      }
    }
  }

  if (minElevation == mu->maxDouble()) {
    minElevation = 0;
  }
  if (maxElevation == mu->minDouble()) {
    maxElevation = 0;
  }

  data._minHeight = minElevation;
  data._maxHeight = maxElevation;
  data._averageHeight = averageElevation / (rx * ry);

  //INDEX///////////////////////////////////////////////////////////////
  for (short j = 0; j < (ry-1); j++) {
    const short jTimesResolution = (short) (j*rx);
    if (j > 0) {
      indices.add(jTimesResolution);
    }
    for (short i = 0; i < rx; i++) {
      indices.add((short) (jTimesResolution + i));
      indices.add((short) (jTimesResolution + i + rx));
    }
    indices.add((short) (jTimesResolution + 2*rx - 1));
  }

  return minElevation;
}

void PlanetTileTessellator::createEastSkirt(const Planet* planet,
                                            const Sector& tileSector,
                                            const Sector& meshSector,
                                            const Vector2I& meshResolution,
                                            double skirtHeight,
                                            FloatBufferBuilderFromGeodetic* vertices,
                                            ShortBufferBuilder& indices,
                                            FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices->size() / 3);

  const short rx = (short) meshResolution._x;
  const short ry = (short) meshResolution._y;

  const short southEastCorner = (short)((rx * ry) - 1);

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = southEastCorner;

  // east side
  for (int j = ry-1; j >= 0; j--) {
    const double x = 1;
    const double y = (double)j/(ry-1);
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
    vertices->add(g, skirtHeight);

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

void PlanetTileTessellator::createNorthSkirt(const Planet* planet,
                                             const Sector& tileSector,
                                             const Sector& meshSector,
                                             const Vector2I& meshResolution,
                                             double skirtHeight,
                                             FloatBufferBuilderFromGeodetic* vertices,
                                             ShortBufferBuilder& indices,
                                             FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices->size() / 3);

  const short rx = (short) meshResolution._x;
  //  const short ry = (short) meshResolution._y;

  const short northEastCorner = (short) (rx - 1);

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = northEastCorner;

  indices.add(surfaceIndex);

  for (int i = rx-1; i >= 0; i--) {
    const double x = (double)i/(rx-1);
    const double y = 0;
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
    vertices->add(g, skirtHeight);

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

void PlanetTileTessellator::createWestSkirt(const Planet* planet,
                                            const Sector& tileSector,
                                            const Sector& meshSector,
                                            const Vector2I& meshResolution,
                                            double skirtHeight,
                                            FloatBufferBuilderFromGeodetic* vertices,
                                            ShortBufferBuilder& indices,
                                            FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices->size() / 3);

  const short rx = (short) meshResolution._x;
  const short ry = (short) meshResolution._y;

  const short northWestCorner = (short)0;

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = northWestCorner;

  indices.add(surfaceIndex);

  for (int j = 0; j < ry; j++) {
    const double x = 0;
    const double y = (double)j/(ry-1);
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
    vertices->add(g, skirtHeight);

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

void PlanetTileTessellator::createSouthSkirt(const Planet* planet,
                                             const Sector& tileSector,
                                             const Sector& meshSector,
                                             const Vector2I& meshResolution,
                                             double skirtHeight,
                                             FloatBufferBuilderFromGeodetic* vertices,
                                             ShortBufferBuilder& indices,
                                             FloatBufferBuilderFromCartesian2D& textCoords) const{

  //VERTICES///////////////////////////////////////////////////////////////
  const short firstSkirtVertex = (short) (vertices->size() / 3);

  const short rx = (short) meshResolution._x;
  const short ry = (short) meshResolution._y;

  const short southWestCorner = (short) (rx * (ry-1));

  short skirtIndex = firstSkirtVertex;
  short surfaceIndex = southWestCorner;

  indices.add(surfaceIndex);

  for (int i = 0; i < rx; i++) {
    const double x = (double)i/(rx-1);
    const double y = 1;
    const Geodetic2D g = meshSector.getInnerPoint(x, y);
    vertices->add(g, skirtHeight);

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
