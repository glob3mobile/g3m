//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_MarksRenderer
#define G3MiOSSDK_MarksRenderer

#include <vector>
#include "DefaultRenderer.hpp"

//#include "GPUProgramState.hpp"

#include "GLState.hpp"

class Mark;
class Camera;
class MarkTouchListener;
class IFloatBuffer;

class MarksRenderer : public DefaultRenderer {
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

public:

  MarksRenderer(bool readyWhenMarksReady);

  void setMarkTouchListener(MarkTouchListener* markTouchListener,
                            bool autoDelete);

  virtual ~MarksRenderer();

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
  
};

#endif
