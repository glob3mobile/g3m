//
//  SimplePlanetRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SimplePlanetRenderer_h
#define G3MiOSSDK_SimplePlanetRenderer_h

#include "Renderer.hpp"

#include "IImage.hpp"

#include "IndexedMesh.hpp"
#include "TexturedMesh.hpp"

class IFloatBuffer;
class IIntBuffer;
class IGLTextureId;

class SimplePlanetRenderer: public Renderer {
private:
  
  const std::string _textureFilename;
  const int _texWidth, _texHeight;
  
  const int _latRes;
  const int _lonRes;
  
  Mesh * _mesh;
  
  
  IFloatBuffer* createVertices(const Planet& planet) const;
  IIntBuffer*  createMeshIndex() const;
  IFloatBuffer* createTextureCoordinates() const;
  
  bool initializeMesh(const RenderContext* rc);
  
public:
  SimplePlanetRenderer(const std::string textureFilename);
  ~SimplePlanetRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  void render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {
    
  }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  void start() {
    
  }
  
  void stop() {
    
  }

  void onResume(const InitializationContext* ic) {
    
  }
  
  void onPause(const InitializationContext* ic) {
    
  }

  
};



#endif
