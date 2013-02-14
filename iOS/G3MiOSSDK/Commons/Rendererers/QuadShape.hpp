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
#include "URL.hpp"

class QuadShape : public AbstractMeshShape {
private:
  const URL _textureURL;
  const float _width;
  const float _height;

  bool _textureRequested;
  bool _texturedSolved;
  IImage* _textureImage;
  const IGLTextureId* getTextureId(const G3MRenderContext* rc);

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  QuadShape(Geodetic3D* position,
            URL textureURL,
            float width,
            float height) :
  AbstractMeshShape(position),
  _textureURL(textureURL),
  _width(width),
  _height(height),
  _textureRequested(false),
  _texturedSolved(false),
  _textureImage(NULL)
  {

  }

  virtual ~QuadShape() {

  }

  void imageDownloaded(IImage* image);
  
};

#endif
