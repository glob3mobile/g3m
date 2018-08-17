//
//  ProtoRenderer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 18/03/14.
//
//

#ifndef G3MiOSSDK_ProtoRenderer
#define G3MiOSSDK_ProtoRenderer

class G3MContext;
class G3MRenderContext;
class GLState;
class G3MEventContext;

class ProtoRenderer {
public:
#ifdef C_CODE
  virtual ~ProtoRenderer() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
  virtual void initialize(const G3MContext* context) = 0;
  
  
  virtual void render(const G3MRenderContext* rc,
                      GLState* glState) = 0;
  
  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height) = 0;
  
  virtual void start(const G3MRenderContext* rc) = 0;
  
  virtual void stop(const G3MRenderContext* rc) = 0;
  
  // Android activity lifecyle
  virtual void onResume(const G3MContext* context) = 0;
  
  virtual void onPause(const G3MContext* context) = 0;
  
  virtual void onDestroy(const G3MContext* context) = 0;
  
};



#endif
