//
//  Mark.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Mark_hpp
#define G3MiOSSDK_Mark_hpp

#include <string>
#include "Geodetic3D.hpp"
#include "Context.hpp"

#include "Vector3D.hpp"
#include "URL.hpp"
#include "Vector2I.hpp"

class IImage;
class IFloatBuffer;
class IGLTextureId;

class Mark {
private:
  
  const std::string _name;
  URL               _textureURL;
  const Geodetic3D  _position;
  
#ifdef C_CODE
  const IGLTextureId* _textureId;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _textureId;
#endif
  
  Vector3D* _cartesianPosition;
  Vector3D* getCartesianPosition(const Planet* planet);
  
  IFloatBuffer* _vertices;
  IFloatBuffer* getVertices(const Planet* planet);
  
  bool    _textureSolved;
  IImage* _textureImage;
  int _textureWidth;
  int _textureHeight;

  bool    _renderedMark;
  
public:
  Mark(const std::string name,
       const URL         textureURL,
       const Geodetic3D  position) :
  _name(name),
  _textureURL(textureURL),
  _position(position),
  _textureId(NULL),
  _cartesianPosition(NULL),
  _vertices(NULL),
  _textureSolved(false),
  _textureImage(NULL),
  _renderedMark(false),
  _textureWidth(0),
  _textureHeight(0)
  {
    
  }
  
  ~Mark();
  
  const std::string getName() const {
    return _name;
  }
  
  const Geodetic3D getPosition() const {
    return _position;
  }
  
  void initialize(const InitializationContext* ic);
  
  void render(const RenderContext* rc,
              const double minDistanceToCamera);
  
  bool isReady() const;
  
  bool isRendered() const {
    return _renderedMark;
  }
  
  void onTextureDownloadError();
  
  void onTextureDownload(const IImage* image);
  
  int getTextureWidth() const;
  int getTextureHeight() const;
  Vector2I getTextureExtent() const;
  
};

#endif
