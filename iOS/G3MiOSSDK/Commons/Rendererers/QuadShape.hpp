//
//  QuadShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__QuadShape__
#define __G3MiOSSDK__QuadShape__

#include "MeshShape.hpp"

class IImage;
class IGLTextureId;

class QuadShape : public MeshShape {
private:
  const std::string _textureFilename;
  IImage* _textureImage;
  const bool _autoDeleteTextureImage;

  const float _width;
  const float _height;

  const IGLTextureId* getTextureId(const RenderContext* rc);

protected:
  Mesh* createMesh(const RenderContext* rc);

public:
  QuadShape(const Geodetic3D& position,
            IImage* textureImage,
            bool autoDeleteTextureImage,
            const std::string textureFilename,
            float width,
            float height) :
  MeshShape(position),
  _textureFilename(textureFilename),
  _textureImage(textureImage),
  _autoDeleteTextureImage(autoDeleteTextureImage),
  _width(width),
  _height(height)
  {

  }

  virtual ~QuadShape() {

  }

};

#endif
