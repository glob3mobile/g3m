//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "EllipsoidalTileTessellator.hpp"

#include "Tile.hpp"
#include "Context.hpp"
#include "IndexedMesh.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"
#include "FloatBufferBuilder.hpp"
#include "ShortBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
//#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "SimpleFloatBufferBuilder.hpp"
#include "GLConstants.hpp"
#include "Color.hpp"
#include "Planet.hpp"

#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "ElevationData.hpp"

Vector2I EllipsoidalTileTessellator::getTileMeshResolution(const Planet* planet,
                                                           const Vector2I& rawResolution,
                                                           const Tile* tile,
                                                           bool debug) const {
  return calculateResolution(rawResolution, tile->getSector());
}

Vector2I EllipsoidalTileTessellator::calculateResolution(const Vector2I& rawResolution,
                                                         const Sector& sector) const {
//  return Vector2I(_resolutionX, _resolutionY);


  /* testing for dynamic latitude-resolution */
  const double cos = sector.getCenter().latitude().cosinus();

  int resolutionY = (int) (rawResolution._y * cos);
  if (resolutionY < 6) {
    resolutionY = 6;
  }

  int resolutionX = (int) (rawResolution._x * cos);
  if (resolutionX < 6) {
    resolutionX = 6;
  }

  return Vector2I(resolutionX, resolutionY);
}

Mesh* EllipsoidalTileTessellator::createTileMesh(const Planet* planet,
                                                 const Vector2I& rawResolution,
                                                 const Tile* tile,
                                                 const ElevationData* elevationData,
                                                 float verticalExaggeration,
                                                 bool debug) const {

  const Sector sector = tile->getSector();
  const Vector2I tileResolution = calculateResolution(rawResolution, sector);

  double minHeight = 0;
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
                                          planet,
                                          sector.getCenter());

  int unusedType;
  for (int j = 0; j < tileResolution._y; j++) {
    const double v = (double) j / (tileResolution._y-1);
    for (int i = 0; i < tileResolution._x; i++) {
      const double u = (double) i / (tileResolution._x-1);

      const Geodetic2D position = sector.getInnerPoint(u, v);
      
      double height = 0;
      if (elevationData != NULL) {
//        height = elevationData->getElevationAt(i, j) * verticalExaggeration;
        height = elevationData->getElevationAt(position,
                                               &unusedType) * verticalExaggeration;
        if (height < minHeight) {
          minHeight = height;
        }
      }

      vertices.add( position, height );
    }
  }


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
    // compute skirt height
    const Vector3D sw = planet->toCartesian(sector.getSW());
    const Vector3D nw = planet->toCartesian(sector.getNW());
    const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minHeight;

    int posS = tileResolution._x * tileResolution._y;
    indices.add((short) (posS-1));

    // east side
    for (int j = tileResolution._y-1; j > 0; j--) {
      vertices.add(sector.getInnerPoint(1, (double)j/(tileResolution._y-1)),
                   skirtHeight);

      indices.add((short) (j*tileResolution._x + (tileResolution._x-1)));
      indices.add((short) posS++);
    }

    // north side
    for (int i = tileResolution._x-1; i > 0; i--) {
      vertices.add(sector.getInnerPoint((double)i/(tileResolution._x-1), 0),
                   skirtHeight);

      indices.add((short) i);
      indices.add((short) posS++);
    }
    
    // west side
    for (int j = 0; j < tileResolution._y-1; j++) {
      vertices.add(sector.getInnerPoint(0, (double)j/(tileResolution._y-1)),
                   skirtHeight);
      
      indices.add((short) (j*tileResolution._x));
      indices.add((short) posS++);
    }
    
    // south side
    for (int i = 0; i < tileResolution._x-1; i++) {
      vertices.add(sector.getInnerPoint((double)i/(tileResolution._x-1), 1),
                   skirtHeight);
      
      indices.add((short) ((tileResolution._y-1)*tileResolution._x + i));
      indices.add((short) posS++);
    }

    // last triangle
    indices.add((short) ((tileResolution._x*tileResolution._y)-1));
    indices.add((short) (tileResolution._x*tileResolution._y));
  }

  Color* color = Color::newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);

  return new IndexedMesh(debug ? GLPrimitive::lineStrip() : GLPrimitive::triangleStrip(),
                         //GLPrimitive::triangleStrip(),
                         //GLPrimitive::lineStrip(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         1,
                         1,
                         color);
}


IFloatBuffer* EllipsoidalTileTessellator::createTextCoords(const Vector2I& rawResolution,
                                                           const Tile* tile,
                                                           bool mercator) const {

  const Vector2I tileResolution = calculateResolution(rawResolution, tile->getSector());

  float* u = new float[tileResolution._x * tileResolution._y];
  float* v = new float[tileResolution._x * tileResolution._y];

  for (int j = 0; j < tileResolution._y; j++) {
    for (int i = 0; i < tileResolution._x; i++) {
      const int pos = j*tileResolution._x + i;
      u[pos] = (float) i / (tileResolution._x-1);
      v[pos] = (float) j / (tileResolution._y-1);
    }
  }

  int textCoordsSize = (tileResolution._x * tileResolution._y) * 2;
  if (_skirted) {
    textCoordsSize += ((tileResolution._x-1) * (tileResolution._y-1) * 4) * 2;
  }

  IFloatBuffer* textCoords = IFactory::instance()->createFloatBuffer(textCoordsSize);

  int textCoordsIndex = 0;

  for (int j = 0; j < tileResolution._y; j++) {
    for (int i = 0; i < tileResolution._x; i++) {
      const int pos = j*tileResolution._x + i;
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }
  }

  // create skirts
  if (_skirted) {
    // east side
    for (int j = tileResolution._y-1; j > 0; j--) {
      const int pos = j*tileResolution._x + tileResolution._x-1;
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }
    
    // north side
    for (int i = tileResolution._x-1; i > 0; i--) {
      const int pos = i;
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // west side
    for (int j = 0; j < tileResolution._y-1; j++) {
      const int pos = j*tileResolution._x;
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // south side
    for (int i = 0; i < tileResolution._x-1; i++) {
      const int pos = (tileResolution._y-1) * tileResolution._x + i;
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }
  }

  // free temp memory
  delete[] u;
  delete[] v;

  //  return textCoords.create();
  return textCoords;
}


Mesh* EllipsoidalTileTessellator::createTileDebugMesh(const Planet* planet,
                                                      const Vector2I& rawResolution,
                                                      const Tile* tile) const {
  const Sector sector = tile->getSector();

  const int resolutionXMinus1 = rawResolution._x - 1;
  const int resolutionYMinus1 = rawResolution._y - 1;
  short posS = 0;

  // compute offset for vertices
  const Vector3D sw = planet->toCartesian(sector.getSW());
  const Vector3D nw = planet->toCartesian(sector.getNW());
  const double offset = nw.sub(sw).length() * 1e-3;

  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
                                          planet,
                                          sector.getCenter());

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

  Color *color = Color::newFromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0);

  return new IndexedMesh(GLPrimitive::lineLoop(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         1,
                         1,
                         color);
}
