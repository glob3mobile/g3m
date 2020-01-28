
#include "G3MWidget_Emscripten.hpp"

#include "G3M/G3MWidget.hpp"
#include "G3M/GL.hpp"
#include "G3M/Vector2F.hpp"
#include "G3M/ErrorHandling.hpp"

#include "Logger_Emscripten.hpp"
#include "Factory_Emscripten.hpp"
#include "StringUtils_Emscripten.hpp"
#include "StringBuilder_Emscripten.hpp"
#include "MathUtils_Emscripten.hpp"
#include "JSONParser_Emscripten.hpp"
#include "TextUtils_Emscripten.hpp"
#include "DeviceAttitude_Emscripten.hpp"
#include "DeviceLocation_Emscripten.hpp"
#include "NativeGL_Emscripten.hpp"
#include "EMStorage.hpp"

#include <math.h>

#include <emscripten/emscripten.h>



using namespace emscripten;

G3MWidget_Emscripten::G3MWidget_Emscripten(const std::string& canvasContainerID) :
_canvas(val::null()),
_webGLContext(val::null()),
_width(0),
_height(0),
_physicalWidth(0),
_physicalHeight(0),
_resizerIntervalID(0),
_devicePixelRatio(1),
_mouseDown(false)
{
  val document = val::global("document");

  val canvasContainer = document.call<val>("getElementById", val(canvasContainerID));
  if ( !canvasContainer.as<bool>() ) {
    emscripten_console_error("Can't find canvasContainer");
    THROW_EXCEPTION("Can't find canvasContainer!");
  }

  _canvas = document.call<val>("createElement", val("canvas"));
  _canvas.set("id",    val("_g3m_canvas"));
  _canvas.set("style", val("width:0px; height:0px"));

  val webGLContextArguments = val::object();
  //webGLContextArguments.set("preserveDrawingBuffer",           true);
  webGLContextArguments.set("alpha",                           false);
  webGLContextArguments.set("preferLowPowerToHighPerformance", true);
  webGLContextArguments.set("antialias",                       false);
  _webGLContext = _canvas.call<val>("getContext", val("webgl"), webGLContextArguments);

  INativeGL* nativeGL = new NativeGL_Emscripten(_webGLContext);
  _gl = new GL(nativeGL);

  canvasContainer.call<void>("appendChild", _canvas);

  EM_ASM({
    var canvas = document.getElementById("_g3m_canvas");
    canvas.addEventListener("webglcontextlost", function(event) {
      event.preventDefault();
      alert("webglcontextlost");
    }, false);
  });
}

G3MWidget_Emscripten::~G3MWidget_Emscripten() {
}

GL* G3MWidget_Emscripten::getGL() const {
  return _gl;
}

void G3MWidget_Emscripten::setG3MWidget(G3MWidget* g3mWidget) {
  _g3mWidget = g3mWidget;
}

G3MWidget* G3MWidget_Emscripten::getG3MWidget() const {
  return _g3mWidget;
}

bool G3MWidget_Emscripten::isWebGLSupported() const {
  return (!_canvas.isNull() && !_webGLContext.isNull());
}

void G3MWidget_Emscripten_loopStep(void* userData) {
  G3MWidget_Emscripten* widget = (G3MWidget_Emscripten*) userData;
  widget->_loopStep();
}

void G3MWidget_Emscripten::_loopStep() {
  _g3mWidget->render(_physicalWidth, _physicalHeight);
}

void G3MWidget_Emscripten_resizerStep(void* userData) {
  G3MWidget_Emscripten* widget = (G3MWidget_Emscripten*) userData;
  widget->_resizerStep();
}

void G3MWidget_Emscripten::addResizeHandler() {
  _resizerIntervalID =  emscripten_set_interval(G3MWidget_Emscripten_resizerStep,
                                                200,  // double intervalMsecs
                                                this  // void *userData
                                                );
}

void G3MWidget_Emscripten::_resizerStep() {
  if (!_canvas.as<bool>()) {
    emscripten_console_warn("G3MWidget_Emscripten::_resizerStep() * NO _canvas *");
    return;
  }

  const int canvasWidth  = _canvas["clientWidth"].as<int>();
  const int canvasHeight = _canvas["clientHeight"].as<int>();

  const val canvasParent = _canvas["parentNode"];

  if (!canvasParent.as<bool>()) {
    emscripten_console_warn("G3MWidget_Emscripten::_resizerStep() * NO canvasParent *");
    return;
  }

  const int canvasParentWidth  = canvasParent["clientWidth"].as<int>();
  const int canvasParentHeight = canvasParent["clientHeight"].as<int>();

  if ((canvasWidth != canvasParentWidth) || (canvasHeight != canvasParentHeight)) {
    emscripten_console_log("G3MWidget_Emscripten::_resizerStep() ==> SizeChanged!!!!!!");
    onSizeChanged(canvasParentWidth, canvasParentHeight);
  }
}

void G3MWidget_Emscripten::onSizeChanged(const int width,
                                         const int height) {
  if ((_width != width) || (_height != height)) {
    _width  = width;
    _height = height;

    val valDevicePixelRatio = val::global("window")["devicePixelRatio"];
    _devicePixelRatio = valDevicePixelRatio.as<bool>() ? valDevicePixelRatio.as<float>() : 1;

    _physicalWidth  = round(_width  * _devicePixelRatio);
    _physicalHeight = round(_height * _devicePixelRatio);

    _canvas.set("width",  _physicalWidth);
    _canvas.set("height", _physicalHeight);

    EM_ASM({
      var canvas = document.getElementById("_g3m_canvas");
      canvas.style.width  = $0 + "px";
      canvas.style.height = $1 + "px";
    }, _width, _height);
  }
}

EM_BOOL G3MWidget_Emscripten_onMouseEvent(int eventType,
                                          const EmscriptenMouseEvent* e,
                                          void* userData) {
  G3MWidget_Emscripten* widget = (G3MWidget_Emscripten*) userData;
  return widget->_onMouseEvent(eventType, e);
}

EM_BOOL G3MWidget_Emscripten_onMouseWheelEvent(int eventType,
                                               const EmscriptenWheelEvent* e,
                                               void* userData) {
  G3MWidget_Emscripten* widget = (G3MWidget_Emscripten*) userData;
  return widget->_onMouseWheelEvent(eventType, e);
}

EM_BOOL G3MWidget_Emscripten_onTouchEvent(int eventType,
                                          const EmscriptenTouchEvent* e,
                                          void* userData) {
  G3MWidget_Emscripten* widget = (G3MWidget_Emscripten*) userData;
  return widget->_onTouchEvent(eventType, e);
}

static inline const char *emscripten_event_type_to_string(int eventType) {
  const char *events[] = { "(invalid)", "(none)", "keypress", "keydown", "keyup", "click", "mousedown", "mouseup", "dblclick", "mousemove", "wheel", "resize",
    "scroll", "blur", "focus", "focusin", "focusout", "deviceorientation", "devicemotion", "orientationchange", "fullscreenchange", "pointerlockchange",
    "visibilitychange", "touchstart", "touchend", "touchmove", "touchcancel", "gamepadconnected", "gamepaddisconnected", "beforeunload",
    "batterychargingchange", "batterylevelchange", "webglcontextlost", "webglcontextrestored", "(invalid)" };
  ++eventType;
  if (eventType < 0) eventType = 0;
  if (eventType >= sizeof(events)/sizeof(events[0])) eventType = sizeof(events)/sizeof(events[0])-1;
  return events[eventType];
}

EM_JS(int, getAbsoluteLeft, (int elemID), {
      var elem = document.EMStorage.take(elemID);

      var left = 0;
      var curr = elem;
      // This intentionally excludes body which has a null offsetParent.
      while (curr.offsetParent) {
      left -= curr.scrollLeft;
      curr = curr.parentNode;
      }
      while (elem) {
      left += elem.offsetLeft;
      elem = elem.offsetParent;
      }
      return left;
      });

EM_JS(int, getAbsoluteTop, (int elemID), {
      var elem = document.EMStorage.take(elemID);

      var top = 0;
      var curr = elem;
      // This intentionally excludes body which has a null offsetParent.
      while (curr.offsetParent) {
      top -= curr.scrollTop;
      curr = curr.parentNode;
      }
      while (elem) {
      top += elem.offsetTop;
      elem = elem.offsetParent;
      }
      return top;
      });

const Vector2F G3MWidget_Emscripten::createPosition(const EmscriptenMouseEvent* e) const {
  const int canvasAbsoluteLeft = getAbsoluteLeft( EMStorage::put(_canvas) );
  const int canvasAbsoluteTop  = getAbsoluteTop ( EMStorage::put(_canvas) );
  return Vector2F((e->clientX - canvasAbsoluteLeft) * _devicePixelRatio,
                  (e->clientY - canvasAbsoluteTop ) * _devicePixelRatio);
}

const TouchEvent* G3MWidget_Emscripten::createTouchFromMouseEvent(const TouchEventType& type,
                                                                  const EmscriptenMouseEvent* e) const {
  const Vector2F currentPos = createPosition(e);
  const float previousX = currentPos._x - e->movementX;
  const float previousY = currentPos._y - e->movementY;

  if (e->shiftKey) {
    std::vector<const Touch*> touches;
    touches.reserve(3);
    touches.push_back( new Touch(Vector2F(currentPos._x - 10, currentPos._y), Vector2F(previousX - 10, previousY) ) );
    touches.push_back( new Touch(currentPos,                                  Vector2F(previousX     , previousY) ) );
    touches.push_back( new Touch(Vector2F(currentPos._x + 10, currentPos._y), Vector2F(previousX + 10, previousY) ) );
    return TouchEvent::create(type, touches);
  }

  Touch* touch = new Touch(currentPos, Vector2F(previousX, previousY));
  return TouchEvent::create(type, touch);
}

EM_BOOL G3MWidget_Emscripten::_onMouseWheelEvent(int eventType,
                                                 const EmscriptenWheelEvent* e) {
  if ((e->deltaY > 0) || (e->deltaY < 0)) {
    const int canvasAbsoluteLeft = getAbsoluteLeft( EMStorage::put(_canvas) );
    const int canvasAbsoluteTop  = getAbsoluteTop ( EMStorage::put(_canvas) );

    const float x = (e->mouse.clientX - canvasAbsoluteLeft) * _devicePixelRatio;
    const float y = (e->mouse.clientY - canvasAbsoluteTop ) * _devicePixelRatio;
    const float delta = e->deltaY / 16;

    const Vector2F beginFirstPosition (x - 10, y - 10);
    const Vector2F beginSecondPosition(x + 10, y + 10);

    {
      std::vector<const Touch*> beginTouches;
      beginTouches.reserve(2);
      beginTouches.push_back(new Touch(beginFirstPosition,  beginFirstPosition));
      beginTouches.push_back(new Touch(beginSecondPosition, beginSecondPosition));

      const TouchEvent* event = TouchEvent::create(TouchEventType::Down, beginTouches);
      _g3mWidget->onTouchEvent(event);
    }

    {
      const Vector2F endFirstPosition (beginFirstPosition._x  - delta, beginFirstPosition._y  - delta);
      const Vector2F endSecondPosition(beginSecondPosition._x + delta, beginSecondPosition._y + delta);

      std::vector<const Touch*> endTouches;
      endTouches.reserve(2);
      endTouches.push_back(new Touch(endFirstPosition,  beginFirstPosition ));
      endTouches.push_back(new Touch(endSecondPosition, beginSecondPosition));

      _g3mWidget->onTouchEvent( TouchEvent::create(TouchEventType::Move, endTouches) );
      _g3mWidget->onTouchEvent( TouchEvent::create(TouchEventType::Up,   endTouches) );
    }

    //    if (e->deltaMode == DOM_DELTA_PIXEL) {
    //      printf("*** onMouseWheelEvent: DOM_DELTA_PIXEL %f\n", e->deltaY);
    //    }
    //    else if (e->deltaMode == DOM_DELTA_LINE) {
    //      printf("*** onMouseWheelEvent: DOM_DELTA_LINE %f\n", e->deltaY);
    //    }
    //    else if (e->deltaMode == DOM_DELTA_PAGE) {
    //      printf("*** onMouseWheelEvent: DOM_DELTA_PAGE %f\n", e->deltaY);
    //    }
    //    else {
    //      printf("*** onMouseWheelEvent: unknown delta page %f\n", e->deltaY);
    //    }
  }

  return EM_TRUE;
}

EM_BOOL G3MWidget_Emscripten::_onMouseEvent(int eventType,
                                            const EmscriptenMouseEvent* e)
{
  if (eventType == EMSCRIPTEN_EVENT_MOUSEDOWN) {
    _mouseDown = true;

    const TouchEvent* event = createTouchFromMouseEvent(TouchEventType::Down, e);
    _g3mWidget->onTouchEvent(event);

    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_MOUSEMOVE) {
    if (_mouseDown) {
      const TouchEvent* event = createTouchFromMouseEvent(TouchEventType::Move, e);
      _g3mWidget->onTouchEvent(event);
    }

    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_MOUSEUP) {
    if (_mouseDown) {
      _mouseDown = false;

      const TouchEvent* event = createTouchFromMouseEvent(TouchEventType::Up, e);
      _g3mWidget->onTouchEvent(event);
    }

    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_DBLCLICK) {
    const Vector2F currentPos = createPosition(e);
    const Touch* touch = new Touch(currentPos, currentPos, (unsigned char) 2);
    const TouchEvent* event = TouchEvent::create(TouchEventType::Down, touch);
    _g3mWidget->onTouchEvent(event);
    
    return EM_TRUE;
  }
  else {
    printf("%s, screen: (%ld,%ld), client: (%ld,%ld),%s%s%s%s button: %hu, buttons: %hu, movement: (%ld,%ld), canvas: (%ld,%ld), target: (%ld, %ld)\n",
           emscripten_event_type_to_string(eventType), e->screenX, e->screenY, e->clientX, e->clientY,
           e->ctrlKey ? " CTRL" : "", e->shiftKey ? " SHIFT" : "", e->altKey ? " ALT" : "", e->metaKey ? " META" : "",
           e->button, e->buttons, e->movementX, e->movementY, e->canvasX, e->canvasY, e->targetX, e->targetY);
    return EM_FALSE;
  }
}


std::vector<const Touch*> G3MWidget_Emscripten::createPointers(const EmscriptenTouchEvent* e) {
  const int canvasAbsoluteLeft = getAbsoluteLeft( EMStorage::put(_canvas) );
  const int canvasAbsoluteTop  = getAbsoluteTop ( EMStorage::put(_canvas) );

  const int numTouches = e->numTouches;

  std::map<long, XY> currentPositions;

  std::vector<const Touch*> pointers;

  pointers.reserve(numTouches);

  for (int i = 0; i < numTouches; i++) {
    const EmscriptenTouchPoint point = e->touches[i];
    const long id = point.identifier;

    const float x = (point.clientX - canvasAbsoluteLeft) * _devicePixelRatio;
    const float y = (point.clientY - canvasAbsoluteTop ) * _devicePixelRatio;
    const Vector2F currentPosition(x, y);

    const Vector2F previousPosition = (_previousTouchesPositions.find(id) == _previousTouchesPositions.end()) ? Vector2F(x, y) : _previousTouchesPositions[id].asVector2F();

    currentPositions[id] = {x, y};

    pointers.push_back(new Touch(currentPosition, previousPosition));
  }

  _previousTouchesPositions = currentPositions;

  return pointers;
}

EM_BOOL G3MWidget_Emscripten::_onTouchEvent(int eventType,
                                            const EmscriptenTouchEvent* e)
{
  TouchEvent* event = NULL;

  switch (eventType) {
    case EMSCRIPTEN_EVENT_TOUCHSTART:
      event = TouchEvent::create(TouchEventType::Down, createPointers(e));
      break;

    case EMSCRIPTEN_EVENT_TOUCHEND:
      event = TouchEvent::create(TouchEventType::Up, createPointers(e));
      break;

    case EMSCRIPTEN_EVENT_TOUCHMOVE:
      event = TouchEvent::create(TouchEventType::Move, createPointers(e));
      break;

    case EMSCRIPTEN_EVENT_TOUCHCANCEL:
      _previousTouchesPositions.clear();
      break;
  }

  if (event) {
    _g3mWidget->onTouchEvent(event);
  }

  return EM_TRUE;
}

void G3MWidget_Emscripten::startWidget() {
  if (_g3mWidget != NULL) {
    {
      // mouse events
      emscripten_set_mousedown_callback("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
      emscripten_set_mousemove_callback("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
      emscripten_set_mouseup_callback  ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
      emscripten_set_dblclick_callback ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);

      emscripten_set_wheel_callback    ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseWheelEvent);
    }

    {
      // touch events
      emscripten_set_touchstart_callback ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onTouchEvent);
      emscripten_set_touchend_callback   ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onTouchEvent);
      emscripten_set_touchmove_callback  ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onTouchEvent);
      emscripten_set_touchcancel_callback("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onTouchEvent);
    }

    addResizeHandler();

    // render loop
    emscripten_set_main_loop_arg(G3MWidget_Emscripten_loopStep, // em_arg_callback_func func
                                 (void*) this,                  // void *arg
                                 60,                            // int fps
                                 1                              // int simulate_infinite_loop
                                 );
  }
}

void G3MWidget_Emscripten::initSingletons() {
  EMStorage::initialize();

  ILogger*         logger         = new Logger_Emscripten(LogLevel::InfoLevel);
  IFactory*        factory        = new Factory_Emscripten();
  IStringUtils*    stringUtils    = new StringUtils_Emscripten();
  IStringBuilder*  stringBuilder  = new StringBuilder_Emscripten(IStringBuilder::DEFAULT_FLOAT_PRECISION);
  IMathUtils*      mathUtils      = new MathUtils_Emscripten();
  IJSONParser*     jsonParser     = new JSONParser_Emscripten();
  ITextUtils*      textUtils      = new TextUtils_Emscripten();
  IDeviceAttitude* deviceAttitude = NULL; //new DeviceAttitude_Emscripten();
  IDeviceLocation* deviceLocation = NULL; //new DeviceLocation_Emscripten();
  
  G3MWidget::initSingletons(logger,
                            factory,
                            stringUtils,
                            stringBuilder,
                            mathUtils,
                            jsonParser,
                            textUtils,
                            deviceAttitude,
                            deviceLocation);
}
