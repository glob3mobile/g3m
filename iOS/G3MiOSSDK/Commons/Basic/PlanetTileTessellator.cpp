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
_skirted(false),
_renderedSector(sector)
{
}

PlanetTileTessellator::~PlanetTileTessellator() {
#ifdef C_CODE
  for (std::map<OrderableVector2I, IShortBuffer*>::iterator it = _indicesMap.begin();
       it != _indicesMap.end();
       it++){
    delete it->second;
  }
#endif
#ifdef JAVA_CODE
  java.util.Iterator it = _indicesMap.entrySet().iterator();
  while (it.hasNext()) {
    java.util.Map.Entry pairs = (java.util.Map.Entry)it.next();
    IShortBuffer b = (IShortBuffer) pairs.getValue();
    b.dispose();
  }
#endif

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



  const Vector2I meshRes = Vector2I((int)(resolution._x / lonRatio) + 2,
                                    (int)(resolution._y / latRatio) + 2);

//  return Vector2I(4,4);

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

    int posS = tileResolution._x * tileResolution._y;
    indices.add((short) (posS-1));

    // east side
    for (int j = tileResolution._y-1; j > 0; j--) {
      indices.add((short) (j*tileResolution._x + (tileResolution._x-1)));
      indices.add((short) posS++);
    }

    // north side
    for (int i = tileResolution._x-1; i > 0; i--) {
      indices.add((short) i);
      indices.add((short) posS++);
    }

    // west side
    for (int j = 0; j < tileResolution._y-1; j++) {
      indices.add((short) (j*tileResolution._x));
      indices.add((short) posS++);
    }

    // south side
    for (int i = 0; i < tileResolution._x-1; i++) {
      indices.add((short) ((tileResolution._y-1)*tileResolution._x + i));
      indices.add((short) posS++);
    }

    // last triangle
    indices.add((short) ((tileResolution._x*tileResolution._y)-1));
    indices.add((short) (tileResolution._x*tileResolution._y));
  }

  return indices.create();
}

IShortBuffer* PlanetTileTessellator::getTileIndices(const Planet* planet,
                                                    const Sector& sector,
                                                    const Vector2I& tileResolution) const{
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

Mesh* PlanetTileTessellator::createTileMesh(const Planet* planet,
                                            const Vector2I& rawResolution,
                                            const Tile* tile,
                                            const ElevationData* elevationData,
                                            float verticalExaggeration,
                                            bool mercator,
                                            bool renderDebug) const {

  const Sector sector = getRenderedSectorForTile(tile);// tile->getSector();
  const Vector2I tileResolution = calculateResolution(rawResolution, tile, sector);

  double minElevation = 0;
  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
  //                                          planet,
  //                                          sector._center);
  FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(planet, sector._center);

  const IMathUtils* mu = IMathUtils::instance();

  for (int j = 0; j < tileResolution._y; j++) {
    const double v = (double) j / (tileResolution._y - 1);

    for (int i = 0; i < tileResolution._x; i++) {
      const double u = (double) i / (tileResolution._x - 1);

      const Geodetic2D position = sector.getInnerPoint(u, v);

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

      printf("V: %s\n", position.description().c_str()  );

      vertices.add( position, elevation );
    }
  }

  // create skirts
  if (_skirted) {
    // compute skirt height
    const Vector3D sw = planet->toCartesian(sector.getSW());
    const Vector3D nw = planet->toCartesian(sector.getNW());
    const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minElevation;

    // east side
    for (int j = tileResolution._y-1; j > 0; j--) {
      vertices.add(sector.getInnerPoint(1, (double)j/(tileResolution._y-1)),
                   skirtHeight);
    }

    // north side
    for (int i = tileResolution._x-1; i > 0; i--) {
      vertices.add(sector.getInnerPoint((double)i/(tileResolution._x-1), 0),
                   skirtHeight);
    }

    // west side
    for (int j = 0; j < tileResolution._y-1; j++) {
      vertices.add(sector.getInnerPoint(0, (double)j/(tileResolution._y-1)),
                   skirtHeight);
    }

    // south side
    for (int i = 0; i < tileResolution._x-1; i++) {
      vertices.add(sector.getInnerPoint((double)i/(tileResolution._x-1), 1),
                   skirtHeight);
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

  return new IndexedGeometryMesh(GLPrimitive::triangleStrip(),
                                 vertices.getCenter(),
                                 vertices.create(), true,
                                 getTileIndices(planet, sector, tileResolution), false,
                                 1,
                                 1);
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

  const Sector tileSector = tile->getSector();
  const Sector sector = getRenderedSectorForTile(tile);
  const Vector2I tileResolution = calculateResolution(rawResolution, tile, sector);

  float* u = new float[tileResolution._x * tileResolution._y];
  float* v = new float[tileResolution._x * tileResolution._y];

  printf("RES: %s\n", tileResolution.description().c_str()  );

//  if (sector.isEqualsTo(tile->getSector())){
//
//    const double mercatorLowerGlobalV = MercatorUtils::getMercatorV(sector._lower._latitude);
//    const double mercatorUpperGlobalV = MercatorUtils::getMercatorV(sector._upper._latitude);
//    const double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
//
//    for (int j = 0; j < tileResolution._y; j++) {
//      for (int i = 0; i < tileResolution._x; i++) {
//        const int pos = j*tileResolution._x + i;
//
//        u[pos] = (float) i / (tileResolution._x-1);
//
//        const double linearV = (double) j / (tileResolution._y-1);
//        if (mercator) {
//          const Angle latitude = sector.getInnerPointLatitude(linearV);
//          const double mercatorGlobalV = MercatorUtils::getMercatorV(latitude);
//          const double mercatorLocalV  = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
//          v[pos] = (float) mercatorLocalV;
//        }
//        else {
//          v[pos] = (float) linearV;
//        }
//      }
//    }
//
//  } else{
//
    for (int j = 0; j < tileResolution._y; j++) {
      for (int i = 0; i < tileResolution._x; i++) {
        const int pos = j*tileResolution._x + i;

        const double meshSectorU = (double)i / tileResolution._x;
        const double meshSectorV = (double)j / tileResolution._y;
        
        Geodetic2D g = sector.getInnerPoint(meshSectorU,meshSectorV);

        if (!tileSector.contains(g)){
          int a = 0;a++;
        }

        Vector2D uv = tileSector.getUVCoordinates(g);
        if (mercator){
          //TODO....
        }

        u[pos] = (float)uv._x;
        v[pos] = (float)uv._y;


        printf("UV: %d -> %f, %f\n", pos, u[pos], v[pos]  );
      }
    }

//  printf("---------------------------------------");

//    const double mercatorLowerGlobalV = MercatorUtils::getMercatorV(sector._lower._latitude);
//    const double mercatorUpperGlobalV = MercatorUtils::getMercatorV(sector._upper._latitude);
//    const double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
//
//    for (int j = 0; j < tileResolution._y; j++) {
//      for (int i = 0; i < tileResolution._x; i++) {
//        const int pos = j*tileResolution._x + i;
//
//        u[pos] = (float) i / (tileResolution._x-1);
//
//        const double linearV = (double) j / (tileResolution._y-1);
//        if (mercator) {
//          const Angle latitude = sector.getInnerPointLatitude(linearV);
//          const double mercatorGlobalV = MercatorUtils::getMercatorV(latitude);
//          const double mercatorLocalV  = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
//          v[pos] = (float) mercatorLocalV;
//        }
//        else {
//          v[pos] = (float) linearV;
//        }
//
//        printf("UV: %d -> %f, %f\n", pos, u[pos], v[pos]);
//      }
//    }

//  }

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

  //  return textCoords.create();
  return textCoords.create();
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

  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
  //                                          planet,
  //                                          sector._center);
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
