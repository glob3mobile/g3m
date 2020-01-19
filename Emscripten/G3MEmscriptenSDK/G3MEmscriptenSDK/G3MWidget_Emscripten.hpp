
#ifndef G3MWidget_Emscripten_hpp
#define G3MWidget_Emscripten_hpp

#include <emscripten/val.h>
#include <emscripten/html5.h>

class GL;
class G3MWidget;
class Vector2F;


class G3MWidget_Emscripten {
private:
  emscripten::val _canvas;
  emscripten::val _webGLContext;
  
  GL* _gl;
  G3MWidget* _g3mWidget;

  int _width;
  int _height;
  int _physicalWidth;
  int _physicalHeight;

  long _resizerIntervalID;

  float _devicePixelRatio;

  bool _mouseDown;
  
  void addResizeHandler();

  void onSizeChanged(const int width,
                     const int height);

  const Vector2F createPosition(const EmscriptenMouseEvent* e) const;

protected:
  
public:
  G3MWidget_Emscripten(const std::string& canvasContainerID);
  
  ~G3MWidget_Emscripten();
  
  void initSingletons();
  
  bool isWebGLSupported() const;
  
  GL* getGL() const;
  
  void setG3MWidget(G3MWidget* g3mWidget);
  
  void startWidget();


  void _loopStep();
  void _resizerStep();

  EM_BOOL _onMouseEvent(int eventType,
                        const EmscriptenMouseEvent* e);

};

#endif
