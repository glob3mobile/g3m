//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_MarksRenderer_hpp
#define G3MiOSSDK_MarksRenderer_hpp

#include <vector>
#include "LeafRenderer.hpp"

class Mark;
class Camera;
class MarkTouchListener;

class MarksRenderer : public LeafRenderer {
private:
  const bool         _readyWhenMarksReady;
  std::vector<Mark*> _marks;

#ifdef C_CODE
  const G3MContext* _context;
  const Camera*     _lastCamera;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
  private Camera     _lastCamera;
#endif

  MarkTouchListener* _markTouchListener;
  bool               _autoDeleteMarkTouchListener;
  long long    _downloadPriority;

public:

  MarksRenderer(bool readyWhenMarksReady);

  void setMarkTouchListener(MarkTouchListener* markTouchListener,
                            bool autoDelete);
  
  void setDownloadPriority(long long downloadPriority);

  virtual ~MarksRenderer();

  virtual void initialize(const G3MContext* context);

  virtual void render(const G3MRenderContext* rc,
                      const GLState& parentState);

  void addMark(Mark* mark);

  void removeMark(Mark* mark);

  void removeAllMarks();

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }

  bool isReadyToRender(const G3MRenderContext* rc);

  void start() {
  }

  void stop() {
  }

  void onResume(const G3MContext* context) {
    _context = context;
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }

};

#endif
