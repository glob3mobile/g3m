//
//  FrameTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#ifndef __G3MiOSSDK__FrameTask__
#define __G3MiOSSDK__FrameTask__

class G3MRenderContext;

class FrameTask {
public:
  virtual ~FrameTask() {

  }

  virtual bool isCanceled(const G3MRenderContext* rc) = 0;

  virtual void execute(const G3MRenderContext* rc) = 0;
  
};

#endif
