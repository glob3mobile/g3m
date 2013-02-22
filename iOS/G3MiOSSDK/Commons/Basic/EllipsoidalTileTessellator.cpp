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
                                                           const Tile* tile,
                                                           bool debug) const {
  //const short resolution = calculateResolution(tile->getSector());
  //return Vector2I(resolution, resolution);
  return calculateResolution(tile->getSector());
}

Vector2I EllipsoidalTileTessellator::calculateResolution(const Sector& sector) const {
  //const short resolution = (short) _resolutionX;

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

  return Vector2I(_resolutionX, _resolutionY);
}

Mesh* EllipsoidalTileTessellator::createTileMesh(const Planet* planet,
                                                 const Tile* tile,
                                                 const ElevationData* elevationData,
                                                 float verticalExaggeration,
                                                 bool debug) const {

  const Sector sector = tile->getSector();

//  if (elevationData != NULL) {
//    ILogger::instance()->logInfo("Elevation data for sector=%s", sector.description().c_str());
//    ILogger::instance()->logInfo("%s", elevationData->description().c_str());
//  }

  //const short resolution = (short) _resolution;
  const Vector2I resolution = calculateResolution(sector);

  //const short resolutionMinus1 = (short) (resolution - 1);


  int unusedType = 0;
  double minHeight = 0;
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
                                          planet,
                                          sector.getCenter());
  for (int j = 0; j < resolution._y; j++) {
    const double v = (double) j / (resolution._y-1);
    for (int i = 0; i < resolution._x; i++) {
      const double u = (double) i / (resolution._x-1);

      const Geodetic2D position = sector.getInnerPoint(u, v);
      double height = 0;
      if (elevationData != NULL) {
//        height = elevationData->getElevationAt(i, j, &unusedType) * verticalExaggeration;
        height = elevationData->getElevationAt(position.latitude(),
                                               position.longitude(),
                                               &unusedType) * verticalExaggeration;
        if (height < minHeight) {
          minHeight = height;
        }
      }

      vertices.add( position, height );
    }
  }


  ShortBufferBuilder indices;
  for (short j = 0; j < (resolution._y-1); j++) {
    const short jTimesResolution = (short) (j*resolution._x);
    if (j > 0) {
      indices.add(jTimesResolution);
    }
    for (short i = 0; i < resolution._x; i++) {
      indices.add((short) (jTimesResolution + i));
      indices.add((short) (jTimesResolution + i + resolution._x));
    }
    indices.add((short) (jTimesResolution + 2*resolution._x - 1));
  }


  // create skirts
  if (_skirted) {
    // compute skirt height
    const Vector3D sw = planet->toCartesian(sector.getSW());
    const Vector3D nw = planet->toCartesian(sector.getNW());
//    const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minHeight;
    const double skirtHeight = (nw.sub(sw).length() * 0.1 * -1) + minHeight;

    indices.add((short) 0);
    int posS = resolution._x * resolution._y;

    // west side
    for (int j = 0; j < resolution._y-1; j++) {
      vertices.add(sector.getInnerPoint(0, (double)j/(resolution._y-1)),
                   skirtHeight);

      indices.add((short) (j*resolution._x));
      indices.add((short) posS++);
    }

    // south side
    for (int i = 0; i < resolution._x-1; i++) {
      vertices.add(sector.getInnerPoint((double)i/(resolution._x-1), 1),
                   skirtHeight);

      indices.add((short) ((resolution._y-1)*resolution._x + i));
      indices.add((short) posS++);
    }

    // east side
    for (int j = resolution._y-1; j > 0; j--) {
      vertices.add(sector.getInnerPoint(1, (double)j/(resolution._y-1)),
                   skirtHeight);

      indices.add((short) (j*resolution._x + (resolution._x-1)));
      indices.add((short) posS++);
    }

    // north side
    for (int i = resolution._x-1; i > 0; i--) {
      vertices.add(sector.getInnerPoint((double)i/(resolution._x-1), 0),
                   skirtHeight);

      indices.add((short) i);
      indices.add((short) posS++);
    }

    // last triangle
    indices.add((short) 0);
    indices.add((short) (resolution._x*resolution._y));
  }

  Color* color = Color::newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);

  return new IndexedMesh(//debug ? GLPrimitive::lineStrip() : GLPrimitive::triangleStrip(),
                         GLPrimitive::triangleStrip(),
                         //GLPrimitive::lineStrip(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         1,
                         1,
                         color);
}


IFloatBuffer* EllipsoidalTileTessellator::createUnitTextCoords(const Tile* tile) const {

//  const int resolution       = _resolution;
  Vector2I resolution = calculateResolution(tile->getSector());
  //const int resolutionMinus1 = resolution - 1;

  float* u = new float[resolution._x * resolution._y];
  float* v = new float[resolution._x * resolution._y];

  for (int j = 0; j < resolution._y; j++) {
    for (int i = 0; i < resolution._x; i++) {
      const int pos = j*resolution._x + i;
      u[pos] = (float) i / (resolution._x-1);
      v[pos] = (float) j / (resolution._y-1);
    }
  }

  //FloatBufferBuilderFromCartesian2D textCoords;
  int textCoordsSize = (resolution._x * resolution._y) * 2;
  if (_skirted) {
    //textCoordsSize += (resolutionMinus1 * 4) * 2;
    textCoordsSize += ((resolution._x-1)*(resolution._y-1) * 4) * 2;
  }
  IFloatBuffer* textCoords = IFactory::instance()->createFloatBuffer(textCoordsSize);
  int textCoordsIndex = 0;

  for (int j = 0; j < resolution._y; j++) {
    for (int i = 0; i < resolution._x; i++) {
      const int pos = j*resolution._x + i;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }
  }

  // create skirts
  if (_skirted) {
    // west side
    for (int j = 0; j < resolution._y-1; j++) {
      const int pos = j*resolution._x;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // south side
    for (int i = 0; i < resolution._x-1; i++) {
      const int pos = (resolution._y-1) * resolution._x + i;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // east side
    for (int j = resolution._y-1; j > 0; j--) {
      const int pos = j*resolution._x + resolution._x-1;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // north side
    for (int i = resolution._x-1; i > 0; i--) {
      const int pos = i;
      //textCoords.add( u[pos], v[pos] );
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
                                                      const Tile* tile) const {
  const Sector sector = tile->getSector();

  const int resolutionXMinus1 = _resolutionX - 1;
  const int resolutionYMinus1 = _resolutionY - 1;
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
