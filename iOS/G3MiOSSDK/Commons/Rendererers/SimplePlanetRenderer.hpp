//
//  SimplePlanetRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SimplePlanetRenderer_h
#define G3MiOSSDK_SimplePlanetRenderer_h

#include "LeafRenderer.hpp"

#include "IImage.hpp"

#include "IndexedMesh.hpp"
#include "TexturedMesh.hpp"

class IFloatBuffer;
class IShortBuffer;
class IGLTextureId;

class SimplePlanetRenderer: public LeafRenderer {
private:
  
//  const std::string _textureFilename;
//  const int _texWidth, _texHeight;
  IImage* _image;

  const int _latRes;
  const int _lonRes;
  
  Mesh* _mesh;
  
  
  IFloatBuffer* createVertices(const Planet* planet) const;
  IShortBuffer*  createMeshIndex() const;
  IFloatBuffer* createTextureCoordinates() const;
  
  Mesh* createMesh(const G3MRenderContext* rc);
  
public:
  SimplePlanetRenderer(IImage* image);
  
  ~SimplePlanetRenderer();
  
  void initialize(const G3MContext* context);  
  
  void render(const G3MRenderContext* rc);
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    
  }
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }
  
  void start() {
    
  }
  
  void stop() {
    
  }

  void onResume(const G3MContext* context) {
    
  }
  
  void onPause(const G3MContext* context) {
    
  }

  void onDestroy(const G3MContext* context) {

  }

};



#endif
