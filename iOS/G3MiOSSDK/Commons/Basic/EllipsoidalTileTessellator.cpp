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
  const short resolution = calculateResolution(tile->getSector());
  return Vector2I(resolution, resolution);
}

short EllipsoidalTileTessellator::calculateResolution(const Sector& sector) const {
  const short resolution = (short) _resolution;

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
  const short resolution = calculateResolution(sector);

  const short resolutionMinus1 = (short) (resolution - 1);


  double minHeight = 0;
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
                                          planet,
                                          sector.getCenter());
  for (int j = 0; j < resolution; j++) {
    const double v = (double) j / resolutionMinus1;
    for (int i = 0; i < resolution; i++) {
      const double u = (double) i / resolutionMinus1;

      float height = 0;
      if (elevationData != NULL) {
        height = elevationData->getElevationAt(i, j) * verticalExaggeration;
        if (height < minHeight) {
          minHeight = height;
        }
      }

      vertices.add( sector.getInnerPoint(u, v), height );
    }
  }


  ShortBufferBuilder indices;
  for (short j = 0; j < resolutionMinus1; j++) {
    const short jTimesResolution = (short) (j*resolution);
    if (j > 0) {
      indices.add(jTimesResolution);
    }
    for (short i = 0; i < resolution; i++) {
      indices.add((short) (jTimesResolution + i));
      indices.add((short) (jTimesResolution + i + resolution));
    }
    indices.add((short) (jTimesResolution + 2*resolution - 1));
  }


  // create skirts
  if (_skirted) {
    // compute skirt height
    const Vector3D sw = planet->toCartesian(sector.getSW());
    const Vector3D nw = planet->toCartesian(sector.getNW());
    const double skirtHeight = (nw.sub(sw).length() * 0.05 * -1) + minHeight;

    indices.add((short) 0);
    int posS = resolution * resolution;

    // west side
    for (int j = 0; j < resolutionMinus1; j++) {
      vertices.add(sector.getInnerPoint(0, (double)j/resolutionMinus1),
                   skirtHeight);

      indices.add((short) (j*resolution));
      indices.add((short) posS++);
    }

    // south side
    for (int i = 0; i < resolutionMinus1; i++) {
      vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 1),
                   skirtHeight);

      indices.add((short) (resolutionMinus1*resolution + i));
      indices.add((short) posS++);
    }

    // east side
    for (int j = resolutionMinus1; j > 0; j--) {
      vertices.add(sector.getInnerPoint(1, (double)j/resolutionMinus1),
                   skirtHeight);

      indices.add((short) (j*resolution + resolutionMinus1));
      indices.add((short) posS++);
    }

    // north side
    for (int i = resolutionMinus1; i > 0; i--) {
      vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 0),
                   skirtHeight);

      indices.add((short) i);
      indices.add((short) posS++);
    }

    // last triangle
    indices.add((short) 0);
    indices.add((short) (resolution*resolution));
  }

  Color* color = Color::newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0);

  return new IndexedMesh(debug ? GLPrimitive::lineStrip() : GLPrimitive::triangleStrip(),
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
  const short resolution = calculateResolution(tile->getSector());
  const int resolutionMinus1 = resolution - 1;

  float* u = new float[resolution * resolution];
  float* v = new float[resolution * resolution];

  for (int j = 0; j < resolution; j++) {
    for (int i = 0; i < resolution; i++) {
      const int pos = j*resolution + i;
      u[pos] = (float) i / resolutionMinus1;
      v[pos] = (float) j / resolutionMinus1;
    }
  }

  //FloatBufferBuilderFromCartesian2D textCoords;
  int textCoordsSize = (resolution * resolution) * 2;
  if (_skirted) {
    textCoordsSize += (resolutionMinus1 * 4) * 2;
  }
  IFloatBuffer* textCoords = IFactory::instance()->createFloatBuffer(textCoordsSize);
  int textCoordsIndex = 0;

  for (int j = 0; j < resolution; j++) {
    for (int i = 0; i < resolution; i++) {
      const int pos = j*resolution + i;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }
  }

  // create skirts
  if (_skirted) {
    // west side
    for (int j = 0; j < resolutionMinus1; j++) {
      const int pos = j*resolution;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // south side
    for (int i = 0; i < resolutionMinus1; i++) {
      const int pos = resolutionMinus1 * resolution + i;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // east side
    for (int j = resolutionMinus1; j > 0; j--) {
      const int pos = j*resolution + resolutionMinus1;
      //textCoords.add( u[pos], v[pos] );
      textCoords->rawPut(textCoordsIndex++, u[pos]);
      textCoords->rawPut(textCoordsIndex++, v[pos]);
    }

    // north side
    for (int i = resolutionMinus1; i > 0; i--) {
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

  const int resolutionMinus1 = _resolution - 1;
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
  for (int j = 0; j < resolutionMinus1; j++) {
    vertices.add(sector.getInnerPoint(0, (double)j/resolutionMinus1),
                 offset);
    indices.add(posS++);
  }

  // south side
  for (int i = 0; i < resolutionMinus1; i++) {
    vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 1),
                 offset);
    indices.add(posS++);
  }

  // east side
  for (int j = resolutionMinus1; j > 0; j--) {
    vertices.add(sector.getInnerPoint(1, (double)j/resolutionMinus1),
                 offset);
    indices.add(posS++);
  }

  // north side
  for (int i = resolutionMinus1; i > 0; i--) {
    vertices.add(sector.getInnerPoint((double)i/resolutionMinus1, 0),
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
