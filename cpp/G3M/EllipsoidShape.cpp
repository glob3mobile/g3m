//
//  EllipsoidShape.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//

#include "EllipsoidShape.hpp"

#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "CompositeMesh.hpp"
#include "Color.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "TexturesHandler.hpp"
#include "TexturedMesh.hpp"
#include "Sector.hpp"
#include "MercatorUtils.hpp"
#include "TextureIDReference.hpp"
#include "SimpleTextureMapping.hpp"
#include "G3MRenderContext.hpp"
#include "TimeInterval.hpp"
#include "IImage.hpp"
#include "EllipsoidalPlanet.hpp"
#include "IMathUtils.hpp"
#include "Geodetic3D.hpp"


EllipsoidShape::EllipsoidShape(Geodetic3D* position,
                               AltitudeMode altitudeMode,
                               const Vector3D& radius,
                               short resolution,
                               float borderWidth,
                               bool texturedInside,
                               bool mercator,
                               const Color& surfaceColor,
                               Color* borderColor,
                               bool withNormals) :
AbstractMeshShape(position, altitudeMode),
_radius(radius),
_oneOverRadiiSquared(Vector3D(1.0 / (radius._x * radius._x ),
                              1.0 / (radius._y * radius._y),
                              1.0 / (radius._z * radius._z))),
//  _quadric(Quadric::fromEllipsoid(_ellipsoid)),
_textureURL(URL("", false)),
_resolution(resolution < 3 ? 3 : resolution),
_borderWidth(borderWidth),
_texturedInside(texturedInside),
_mercator(mercator),
_surfaceColor(new Color(surfaceColor)),
_borderColor(borderColor),
_textureRequested(false),
_textureImage(NULL),
_withNormals(withNormals),
_texID(NULL)
{

}

EllipsoidShape::EllipsoidShape(Geodetic3D* position,
                               AltitudeMode altitudeMode,
                               const Planet* planet,
                               const URL& textureURL,
                               const Vector3D& radius,
                               short resolution,
                               float borderWidth,
                               bool texturedInside,
                               bool mercator,
                               bool withNormals) :
AbstractMeshShape(position, altitudeMode),
_radius(radius),
_oneOverRadiiSquared(Vector3D(1.0 / (radius._x * radius._x ),
                              1.0 / (radius._y * radius._y),
                              1.0 / (radius._z * radius._z))),
_textureURL(textureURL),
_resolution(resolution < 3 ? 3 : resolution),
_borderWidth(borderWidth),
_texturedInside(texturedInside),
_mercator(mercator),
_surfaceColor(NULL),
_borderColor(NULL),
_textureRequested(false),
_textureImage(NULL),
_withNormals(withNormals),
_texID(NULL)
{

}

void EllipsoidShape::setSurfaceColor(const Color& surfaceColor) {
  delete _surfaceColor;

  _surfaceColor = new Color(surfaceColor);

  cleanMesh();
}

EllipsoidShape::~EllipsoidShape() {
  delete _surfaceColor;
  delete _borderColor;

  delete _texID; //Releasing texture

#ifdef JAVA_CODE
  super.dispose();
#endif
}

const TextureIDReference* EllipsoidShape::getTextureID(const G3MRenderContext* rc) {

  if (_texID == NULL) {
    if (_textureImage == NULL) {
      return NULL;
    }

    _texID = rc->getTexturesHandler()->getTextureIDReference(_textureImage,
                                                             GLFormat::rgba(),
                                                             _textureURL._path,
                                                             false,
                                                             GLTextureParameterValue::clampToEdge(),
                                                             GLTextureParameterValue::clampToEdge());

    delete _textureImage;
    _textureImage = NULL;
  }

  if (_texID == NULL) {
    rc->getLogger()->logError("Can't load texture %s", _textureURL._path.c_str());
  }

  if (_texID == NULL) {
    return NULL;
  }

  return _texID->createCopy(); //The copy will be handle by the TextureMapping
}


Mesh* EllipsoidShape::createBorderMesh(const G3MRenderContext* rc,
                                       FloatBufferBuilderFromGeodetic* vertices) {

  // create border indices for horizontal lines
  ShortBufferBuilder indices;
  short delta = (short) (2*_resolution - 1);
  for (short j=1; j<_resolution-1; j++) {
    for (short i=0; i<2*_resolution-2; i++) {
      indices.add((short) (i+j*delta));
      indices.add((short) (i+1+j*delta));
    }
  }

  // create border indices for vertical lines
  for (short i=0; i<2*_resolution-2; i++) {
    for (short j=0; j<_resolution-1; j++) {
      indices.add((short) (i+j*delta));
      indices.add((short) (i+(j+1)*delta));
    }
  }

  Color* borderColor;
  if (_borderColor != NULL) {
    borderColor = new Color(*_borderColor);
  }
  else {
    if (_surfaceColor != NULL) {
      borderColor = new Color(*_surfaceColor);
    }
    else {
      borderColor = Color::newFromRGBA(1, 0, 0, 1);
    }
  }

  return new IndexedMesh(GLPrimitive::lines(),
                         vertices->getCenter(),
                         vertices->create(),
                         true,
                         indices.create(),
                         true,
                         (_borderWidth < 1) ? 1 : _borderWidth,
                         1,
                         borderColor);
}

Mesh* EllipsoidShape::createSurfaceMesh(const G3MRenderContext* rc,
                                        FloatBufferBuilderFromGeodetic* vertices,
                                        FloatBufferBuilderFromCartesian2D* texCoords,
                                        FloatBufferBuilderFromCartesian3D* normals) {

  // create surface indices
  ShortBufferBuilder indices;
  short delta = (short) (2*_resolution - 1);

  // create indices for textupe mapping depending on the flag _texturedInside
  if (!_texturedInside) {
    for (short j=0; j<_resolution-1; j++) {
      if (j>0) indices.add((short) (j*delta));
      for (short i=0; i<2*_resolution-1; i++) {
        indices.add((short) (i+j*delta));
        indices.add((short) (i+(j+1)*delta));
      }
      indices.add((short) ((2*_resolution-2)+(j+1)*delta));
    }
  }
  else {
    for (short j=0; j<_resolution-1; j++) {
      if (j>0) indices.add((short) ((j+1)*delta));
      for (short i=0; i<2*_resolution-1; i++) {
        indices.add((short) (i+(j+1)*delta));
        indices.add((short) (i+j*delta));
      }
      indices.add((short) ((2*_resolution-2)+j*delta));
    }
  }

  // create mesh
  Color* surfaceColor = (_surfaceColor == NULL) ? NULL : new Color(*_surfaceColor);
  Mesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
                             vertices->getCenter(),
                             vertices->create(),
                             true,
                             indices.create(),
                             true,
                             (_borderWidth < 1) ? 1 : _borderWidth,
                             1,
                             surfaceColor,
                             NULL,
                             true,
                             _withNormals? normals->create() : NULL);

  const TextureIDReference* texID = getTextureID(rc);
  if (texID == NULL) {
    return im;
  }

  TextureMapping* texMap = new SimpleTextureMapping(texID,
                                                    texCoords->create(),
                                                    true,
                                                    true);

  return new TexturedMesh(im, true, texMap, true, true);

}

class EllipsoidShape_IImageDownloadListener : public IImageDownloadListener {
private:
  EllipsoidShape* _ellipsoidShape;

public:

  EllipsoidShape_IImageDownloadListener(EllipsoidShape* ellipsoidShape) :
  _ellipsoidShape(ellipsoidShape)
  {

  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired)  {
    _ellipsoidShape->imageDownloaded(image);
  }

  void onError(const URL& url) {

  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired)  {

  }
};

void EllipsoidShape::imageDownloaded(IImage* image) {
  _textureImage = image;

  cleanMesh();
}


Mesh* EllipsoidShape::createMesh(const G3MRenderContext* rc) {
  if (!_textureRequested) {
    _textureRequested = true;
    if (_textureURL._path.length() != 0) {
      rc->getDownloader()->requestImage(_textureURL,
                                        1000000,
                                        TimeInterval::fromDays(30),
                                        true,
                                        new EllipsoidShape_IImageDownloadListener(this),
                                        true);
    }
  }

  const EllipsoidalPlanet ellipsoid(Ellipsoid(Vector3D::ZERO, _radius));
  const Sector sector(Sector::FULL_SPHERE);

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(&ellipsoid, Vector3D::ZERO);
  FloatBufferBuilderFromCartesian2D texCoords;

  FloatBufferBuilderFromCartesian3D* normals = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  const short resolution2Minus2 = (short) (2*_resolution-2);
  const short resolutionMinus1  = (short) (_resolution-1);

  for (int j = 0; j < _resolution; j++) {
    for (int i = 0; i < 2*_resolution-1; i++) {
      const double u = (double) i / resolution2Minus2;
      const double v = (double) j / resolutionMinus1;

      const Geodetic2D innerPoint = sector.getInnerPoint(u, v);

      vertices->add(innerPoint);

      if (_withNormals) {
        Vector3D n = ellipsoid.geodeticSurfaceNormal(innerPoint);
        normals->add(n);
      }

      const double vv = _mercator ? MercatorUtils::getMercatorV(innerPoint._latitude) : v;

      texCoords.add((float) u, (float) vv);
    }
  }


  Mesh* surfaceMesh = createSurfaceMesh(rc, vertices, &texCoords, normals);

  Mesh* resultMesh;
  if (_borderWidth > 0) {
    CompositeMesh* compositeMesh = new CompositeMesh();
    compositeMesh->addMesh(surfaceMesh);
    compositeMesh->addMesh(createBorderMesh(rc, vertices));
    resultMesh = compositeMesh;
  }
  else {
    resultMesh = surfaceMesh;
  }

  delete vertices;
  delete normals;

  return resultMesh;
}


std::vector<double> EllipsoidShape::intersectionsDistances(const Planet* planet,
                                                           const Vector3D& origin,
                                                           const Vector3D& direction) const {
  const Vector3D m = origin.sub( planet->toCartesian(getPosition()) );

  std::vector<double> result;

  // By laborious algebraic manipulation....
  const double a = (direction._x * direction._x * _oneOverRadiiSquared._x +
                    direction._y * direction._y * _oneOverRadiiSquared._y +
                    direction._z * direction._z * _oneOverRadiiSquared._z);

  const double b = 2.0 * (m._x * direction._x * _oneOverRadiiSquared._x +
                          m._y * direction._y * _oneOverRadiiSquared._y +
                          m._z * direction._z * _oneOverRadiiSquared._z);

  const double c = (m._x * m._x * _oneOverRadiiSquared._x +
                    m._y * m._y * _oneOverRadiiSquared._y +
                    m._z * m._z * _oneOverRadiiSquared._z - 1.0);

  // Solve the quadratic equation: ax^2 + bx + c = 0.
  // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
  // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
  const double discriminant = b * b - 4 * a * c;
  if (discriminant < 0.0) {
    // no intersections
  }
  else if (discriminant == 0.0) {
    // one intersection at a tangent point
    result.push_back(-0.5 * b / a);
  }
  else {
    const double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils::instance()->sqrt(discriminant));
    const double root1 = t / a;
    const double root2 = c / t;

    // Two intersections - return the smallest first.
    if (root1 < root2) {
      result.push_back(root1);
      result.push_back(root2);
    }
    else {
      result.push_back(root2);
      result.push_back(root1);
    }
  }

  return result;
}

