//
//  QuadShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__QuadShape__
#define __G3MiOSSDK__QuadShape__

#include "Shape.hpp"
class Mesh;
class IImage;
class IGLTextureId;

class QuadShape : public Shape {
private:
  Mesh* createMesh(const RenderContext* rc);
  Mesh* getMesh(const RenderContext* rc);

  Mesh* _mesh;

  const std::string _textureFilename;
  IImage* _textureImage;
  const bool _autoDeleteTextureImage;
  
  const int _width;
  const int _height;

  
#ifdef C_CODE
  const IGLTextureId*
#endif
#ifdef JAVA_CODE
  IGLTextureId
#endif
  getTextureId(const RenderContext* rc);


public:
  QuadShape(const Geodetic3D& position,
            IImage* textureImage,
            bool autoDeleteTextureImage,
            const std::string textureFilename,
            int width,
            int height) :
  Shape(position),
  _mesh(NULL),
  _textureFilename(textureFilename),
  _textureImage(textureImage),
  _autoDeleteTextureImage(autoDeleteTextureImage),
  _width(width),
  _height(height)
  {

  }

  ~QuadShape();
  
  void render(const RenderContext* rc);

};

#endif
