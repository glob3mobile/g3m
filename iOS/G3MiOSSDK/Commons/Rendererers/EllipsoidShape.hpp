//
//  EllipsoidShape.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//

#ifndef __G3MiOSSDK__EllipsoidShape__
#define __G3MiOSSDK__EllipsoidShape__

#include "AbstractMeshShape.hpp"
#include "Ellipsoid.hpp"
#include "Planet.hpp"
#include "Quadric.hpp"


class Color;
class FloatBufferBuilderFromGeodetic;
class FloatBufferBuilderFromCartesian2D;
class FloatBufferBuilderFromCartesian3D;
class TextureIDReference;

class IGLTextureId;

#include "URL.hpp"


class EllipsoidShape : public AbstractMeshShape {
private:
  
#ifdef C_CODE
  const Ellipsoid* _ellipsoid;
//  const Quadric    _quadric;
#endif
#ifdef JAVA_CODE
  private Ellipsoid _ellipsoid;
//  private final Quadric _quadric;
#endif

  URL _textureURL;

  /*const double _radiusX;
  const double _radiusY;
  const double _radiusZ;*/

  const short _resolution;

  const float _borderWidth;
  
  const bool _texturedInside;

  const bool _mercator;

  const bool _withNormals;

  Color* _surfaceColor;
  Color* _borderColor;

  Mesh* createBorderMesh(const G3MRenderContext* rc,
                         FloatBufferBuilderFromGeodetic *vertices);
  Mesh* createSurfaceMesh(const G3MRenderContext* rc,
                          FloatBufferBuilderFromGeodetic* vertices,
                          FloatBufferBuilderFromCartesian2D* texCoords,
                          FloatBufferBuilderFromCartesian3D* normals);

  bool _textureRequested;
  IImage* _textureImage;
  const TextureIDReference* getTextureId(const G3MRenderContext* rc);

#ifdef C_CODE
  const TextureIDReference* _texId;
#endif
#ifdef JAVA_CODE
  TextureIDReference _texId;
#endif

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  EllipsoidShape(Geodetic3D* position,
                 AltitudeMode altitudeMode,
                 const Vector3D& radius,
                 short resolution,
                 float borderWidth,
                 bool texturedInside,
                 bool mercator,
                 const Color& surfaceColor,
                 Color* borderColor = NULL,
                 bool withNormals = true) :
  AbstractMeshShape(position, altitudeMode),
  _ellipsoid(new Ellipsoid(Vector3D::zero, radius)),
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
  _texId(NULL)
  {

  }

  EllipsoidShape(Geodetic3D* position,
                 AltitudeMode altitudeMode,
                 const Planet* planet,
                 const URL& textureURL,
                 const Vector3D& radius,
                 short resolution,
                 float borderWidth,
                 bool texturedInside,
                 bool mercator,
                 bool withNormals = true) :
  AbstractMeshShape(position, altitudeMode),
  _ellipsoid(new Ellipsoid(Vector3D::zero, radius)),
//  _quadric(Quadric::fromEllipsoid(_ellipsoid)),
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
  _texId(NULL)
  {
    
  }


  ~EllipsoidShape();

  void imageDownloaded(IImage* image);
  
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const;
  
};

#endif
