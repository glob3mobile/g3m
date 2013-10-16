//
//  QuadShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__QuadShape__
#define __G3MiOSSDK__QuadShape__

#include "AbstractMeshShape.hpp"

class IImage;
class IGLTextureId;
class Color;
class TextureIDReference;

#include "URL.hpp"

class QuadShape : public AbstractMeshShape {
private:
  URL _textureURL;
  const float _width;
  const float _height;
  const Color* _color;

  bool _textureRequested;
#ifdef C_CODE
  const IImage* _textureImage;
#endif
#ifdef JAVA_CODE
  private IImage _textureImage;
#endif

  const TextureIDReference* getTextureId(const G3MRenderContext* rc);

  const bool _withNormals;

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  QuadShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            const URL& textureURL,
            float width,
            float height,
            bool withNormals) :
  AbstractMeshShape(position, altitudeMode),
  _textureURL(textureURL),
  _width(width),
  _height(height),
  _textureRequested(false),
  _textureImage(NULL),
  _color(NULL),
  _withNormals(withNormals)
  {

  }

  QuadShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            const IImage* textureImage,
            float width,
            float height,
            bool withNormals) :
  AbstractMeshShape(position, altitudeMode),
  _textureURL(URL("", false)),
  _width(width),
  _height(height),
  _textureRequested(true),
  _textureImage(textureImage),
  _color(NULL),
  _withNormals(withNormals)
  {

  }


  QuadShape(Geodetic3D* position,
            AltitudeMode altitudeMode,
            float width,
            float height,
            Color* color,
            bool withNormals) :
  AbstractMeshShape(position, altitudeMode),
  _textureURL(URL("", false)),
  _width(width),
  _height(height),
  _textureRequested(false),
  _textureImage(NULL),
  _color(color),
  _withNormals(withNormals)
  {

  }
  virtual ~QuadShape();

  void imageDownloaded(IImage* image);
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const {
    std::vector<double> intersections;
    return intersections;
  }

};

#endif
