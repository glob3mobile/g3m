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

#include "URL.hpp"

class QuadShape : public AbstractMeshShape {
private:
  URL _textureURL;
  const float _width;
  const float _height;
  const Color* _color;

  bool _textureRequested;
  IImage* _textureImage;
  const IGLTextureId* getTextureId(const G3MRenderContext* rc);

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  QuadShape(Geodetic3D* position,
            const URL& textureURL,
            float width,
            float height) :
  AbstractMeshShape(position),
  _textureURL(textureURL),
  _width(width),
  _height(height),
  _textureRequested(false),
  _textureImage(NULL),
  _color(NULL)
  {

  }

  QuadShape(Geodetic3D* position,
            IImage* textureImage,
            float width,
            float height) :
  AbstractMeshShape(position),
  _textureURL(URL("", false)),
  _width(width),
  _height(height),
  _textureRequested(true),
  _textureImage(textureImage),
  _color(NULL)
  {

  }


  QuadShape(Geodetic3D* position,
            float width,
            float height,
            Color* color) :
  AbstractMeshShape(position),
  _textureURL(URL("", false)),
  _width(width),
  _height(height),
  _textureRequested(false),
  _textureImage(NULL),
  _color(color)
  {

  }
  virtual ~QuadShape();

  void imageDownloaded(IImage* image);
  
};

#endif
