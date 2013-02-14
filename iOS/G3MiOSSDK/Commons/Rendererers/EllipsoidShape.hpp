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

class Color;
class FloatBufferBuilderFromCartesian3D;
class FloatBufferBuilderFromCartesian2D;
class IGLTextureId;

#include "URL.hpp"


class EllipsoidShape : public AbstractMeshShape {
private:
  const URL _textureURL;

  const double _radiusX;
  const double _radiusY;
  const double _radiusZ;

  const short _resolution;

  const float _borderWidth;

  const bool _cozzi;

  Color* _surfaceColor;
  Color* _borderColor;

  Mesh* createBorderMesh(const G3MRenderContext* rc,
                         FloatBufferBuilderFromCartesian3D *vertices);
  Mesh* createSurfaceMesh(const G3MRenderContext* rc,
                          FloatBufferBuilderFromCartesian3D* vertices,
                          FloatBufferBuilderFromCartesian2D* texCoords);

  bool _textureRequested;
  IImage* _textureImage;
  const IGLTextureId* getTextureId(const G3MRenderContext* rc);

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  EllipsoidShape(Geodetic3D* position,
                 const Vector3D& radius,
                 short resolution,
                 float borderWidth,
                 bool cozzi,
                 Color* surfaceColor,
                 Color* borderColor = NULL) :
  AbstractMeshShape(position),
  _textureURL(URL("", false)),
  _radiusX(radius.x()),
  _radiusY(radius.y()),
  _radiusZ(radius.z()),
  _resolution(resolution < 3 ? 3 : resolution),
  _borderWidth(borderWidth),
  _cozzi(cozzi),
  _surfaceColor(surfaceColor),
  _borderColor(borderColor),
  _textureRequested(false),
  _textureImage(NULL)
  {

  }

  EllipsoidShape(Geodetic3D* position,
                 const URL& textureURL,
                 const Vector3D& radius,
                 short resolution,
                 float borderWidth,
                 bool cozzi) :
  AbstractMeshShape(position),
  _textureURL(textureURL),
  _radiusX(radius.x()),
  _radiusY(radius.y()),
  _radiusZ(radius.z()),
  _resolution(resolution < 3 ? 3 : resolution),
  _borderWidth(borderWidth),
  _cozzi(cozzi),
  _surfaceColor(NULL),
  _borderColor(NULL),
  _textureRequested(false),
  _textureImage(NULL)
  {

  }


  ~EllipsoidShape();

  void imageDownloaded(IImage* image);

};

#endif
