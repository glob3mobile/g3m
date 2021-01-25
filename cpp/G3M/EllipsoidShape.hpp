//
//  EllipsoidShape.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//

#ifndef __G3M__EllipsoidShape__
#define __G3M__EllipsoidShape__

#include "AbstractMeshShape.hpp"

#include "Vector3D.hpp"
#include "URL.hpp"

class FloatBufferBuilderFromGeodetic;
class FloatBufferBuilderFromCartesian2D;
class FloatBufferBuilderFromCartesian3D;
class TextureIDReference;
class G3MEventContext;


class EllipsoidShape : public AbstractMeshShape {
private:
  const Vector3D _radius;
  const Vector3D _oneOverRadiiSquared;

  const URL _textureURL;

  const short _resolution;

  const float _borderWidth;
  
  const bool _texturedInside;

  const bool _mercator;

  const bool _withNormals;

  Color* _surfaceColor;
  Color* _borderColor;

  Mesh* createBorderMesh(const G3MRenderContext* rc,
                         FloatBufferBuilderFromGeodetic* vertices);
  Mesh* createSurfaceMesh(const G3MRenderContext* rc,
                          FloatBufferBuilderFromGeodetic* vertices,
                          FloatBufferBuilderFromCartesian2D* texCoords,
                          FloatBufferBuilderFromCartesian3D* normals);

  bool _textureRequested;
  IImage* _textureImage;
  const TextureIDReference* getTextureID(const G3MRenderContext* rc);

#ifdef C_CODE
  const TextureIDReference* _texID;
#endif
#ifdef JAVA_CODE
  TextureIDReference _texID;
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
                 bool withNormals = true);

  EllipsoidShape(Geodetic3D* position,
                 AltitudeMode altitudeMode,
                 const Planet* planet,
                 const URL& textureURL,
                 const Vector3D& radius,
                 short resolution,
                 float borderWidth,
                 bool texturedInside,
                 bool mercator,
                 bool withNormals = true);

  ~EllipsoidShape();

  void imageDownloaded(IImage* image);

  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Vector3D& origin,
                                             const Vector3D& direction) const;

  void setSurfaceColor(const Color& surfaceColor);

};

#endif
