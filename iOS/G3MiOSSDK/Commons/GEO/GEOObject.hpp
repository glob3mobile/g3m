//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOObject__
#define __G3MiOSSDK__GEOObject__

class G3MContext;
class G3MRenderContext;
class GLState;
class GEOSymbolizer;
class GPUProgramState;

class GEOObject {
public:
  virtual ~GEOObject() {

  }

  virtual void initialize(const G3MContext* context) {

  }

  virtual bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  virtual void render(const G3MRenderContext* rc,
                      const GLState& parentState, const GPUProgramState* parentProgramState,
                      const GEOSymbolizer* symbolizer) = 0;

};

#endif
