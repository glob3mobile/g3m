
#ifndef G3MWidget_Emscripten_hpp
#define G3MWidget_Emscripten_hpp

#include "emscripten/val.h"

class GL;
class G3MWidget;

class G3MWidget_Emscripten {
private:
  emscripten::val _canvas;
  emscripten::val _webGLContext;

  GL* _gl;
  G3MWidget* _g3mWidget;

protected:

public:
  G3MWidget_Emscripten();
  
  ~G3MWidget_Emscripten();

  void initSingletons();

  bool isWebGLSupported() const;

  GL* getGL() const;

  void setG3MWidget(G3MWidget* g3mWidget);

  void startWidget();
  
};

#endif
