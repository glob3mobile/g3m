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

class IFloatBuffer;
class IGLTextureId;

class Mark {
private:
  const std::string  _name;
  const std::string  _textureFilename;
  const Geodetic3D   _position;
  
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

public:
  Mark(const std::string name,
       const std::string textureFilename,
       const Geodetic3D  position) :
  _name(name),
  _textureFilename(textureFilename),
  _position(position),
  _textureId(NULL),
  _cartesianPosition(NULL),
  _vertices(NULL)
  {
    
  }
  
  ~Mark();
  
  const std::string getName() const {
    return _name;
  }
  
  const Geodetic3D getPosition() const {
    return _position;
  }
  
  void render(const RenderContext* rc,
              const double minDistanceToCamera);
  
};

#endif
