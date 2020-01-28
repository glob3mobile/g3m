
#ifndef G3MWidget_Emscripten_hpp
#define G3MWidget_Emscripten_hpp

#include <emscripten/val.h>
#include <emscripten/html5.h>

#include "G3M/TouchEvent.hpp"
#include <map>

class GL;
class G3MWidget;
class Vector2F;


class G3MWidget_Emscripten {
private:
  struct XY {
    float _x;
    float _y;

    const Vector2F asVector2F() const {
      return Vector2F(_x, _y);
    }
  };

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

  const TouchEvent* createTouchFromMouseEvent(const TouchEventType& type,
                                              const EmscriptenMouseEvent* e) const;

  std::map<long, XY> _previousTouchesPositions;

  std::vector<const Touch*> createPointers(const EmscriptenTouchEvent* e);


protected:
  
public:
  static void initSingletons();

  G3MWidget_Emscripten(const std::string& canvasContainerID);
  
  ~G3MWidget_Emscripten();

  bool isWebGLSupported() const;
  
  GL* getGL() const;
  
  void setG3MWidget(G3MWidget* g3mWidget);

  G3MWidget* getG3MWidget() const;

  void startWidget();


  void _loopStep();
  void _resizerStep();

  EM_BOOL _onMouseEvent(int eventType,
                        const EmscriptenMouseEvent* e);

  EM_BOOL _onMouseWheelEvent(int eventType,
                             const EmscriptenWheelEvent* e);

  EM_BOOL _onTouchEvent(int eventType,
                        const EmscriptenTouchEvent* e);

};

#endif
