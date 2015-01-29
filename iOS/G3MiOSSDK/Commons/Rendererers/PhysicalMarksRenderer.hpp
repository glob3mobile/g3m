//
//  PhysicalMarksRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/1/15.
//
//

#ifndef __G3MiOSSDK__PhysicalMarksRenderer__
#define __G3MiOSSDK__PhysicalMarksRenderer__

#include <vector>
#include "DefaultRenderer.hpp"

//#include "GPUProgramState.hpp"

#include "GLState.hpp"
#include "ShapesRenderer.hpp"

class Mark;
class Camera;
class MarkTouchListener;
class IFloatBuffer;

class PhysicalMarksRenderer : public DefaultRenderer {
private:
  const bool         _readyWhenMarksReady;
  std::vector<Mark*> _marks;
  
#ifdef C_CODE
  const Camera*     _lastCamera;
#endif
#ifdef JAVA_CODE
  private Camera     _lastCamera;
#endif
  
  MarkTouchListener* _markTouchListener;
  bool               _autoDeleteMarkTouchListener;
  
  long long _downloadPriority;
  
  GLState* _glState;
  
  void updateGLState(const G3MRenderContext* rc);
  IFloatBuffer* _billboardTexCoords;
  IFloatBuffer* getBillboardTexCoords();
  
  ShapesRenderer *_shapesRenderer;
  
  std::vector<Geodetic3D*> _anchors;
  std::vector<Vector2F*> _pixels;
  
public:
  
  PhysicalMarksRenderer(bool readyWhenMarksReady,
                        ShapesRenderer *shapesRenderer);
  
  void setMarkTouchListener(MarkTouchListener* markTouchListener,
                            bool autoDelete);
  
  virtual ~PhysicalMarksRenderer();
  
  virtual void onChangedContext();
  
  virtual void render(const G3MRenderContext* rc, GLState* glState);
  
  void addMark(Mark* mark);
  
  void removeMark(Mark* mark);
  
  void removeAllMarks();
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);
  
  RenderState getRenderState(const G3MRenderContext* rc);
  
  //TODO: WHY? VTP
  void onResume(const G3MContext* context) {
    _context = context;
  }
  
  /**
   Change the download-priority used by Marks (for downloading textures).
   
   Default value is 1000000
   */
  void setDownloadPriority(long long downloadPriority) {
    _downloadPriority = downloadPriority;
  }
  
  long long getDownloadPriority() const {
    return _downloadPriority;
  }
  
  bool isVisible(const G3MRenderContext* rc) {
    return true;
  }
  
  void modifiyGLState(GLState* state) {
    
  }
  void zRender(const G3MRenderContext* rc, GLState* glState){}
  
  std::vector<Mark*> getMarks() { return _marks; }
  
};

#endif /* defined(__G3MiOSSDK__PhysicalMarksRenderer__) */
