
#include "G3MWidget_Emscripten.hpp"

#include "G3M/G3MWidget.hpp"
#include "G3M/GL.hpp"
#include "G3M/Vector2F.hpp"
#include "G3M/ErrorHandling.hpp"
#include "G3M/ILogger.hpp"

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
#include <algorithm>

#include <emscripten/emscripten.h>
using namespace emscripten;


G3MWidget_Emscripten::G3MWidget_Emscripten() :
_canvas(val::null()),
_webGLContext(val::null()),
_gl(NULL),
_g3mWidget(NULL),
_width(0),
_height(0),
_physicalWidth(0),
_physicalHeight(0),
_resizerIntervalID(0),
_devicePixelRatio(1),
_mouseDown(false)
{
  val document = val::global("document");

  _canvas = document.call<val>("createElement", val("canvas"));
  _canvas.set("id",    val("_g3m_canvas"));
  _canvas.set("style", val("width:0px; height:0px"));

  val webGLContextArguments = val::object();
  //webGLContextArguments.set("preserveDrawingBuffer",           true);
  webGLContextArguments.set("alpha",                           false);
  webGLContextArguments.set("preferLowPowerToHighPerformance", true);
  webGLContextArguments.set("antialias",                       false);
  _webGLContext = _canvas.call<val>("getContext", val("webgl"), webGLContextArguments);

  if (isWebGLSupported()) {
    INativeGL* nativeGL = new NativeGL_Emscripten(_webGLContext);
    _gl = new GL(nativeGL);
  }
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

    _physicalWidth  = (int) round(_width  * _devicePixelRatio);
    _physicalHeight = (int) round(_height * _devicePixelRatio);

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

//static inline const char *emscripten_event_type_to_string(int eventType) {
//  const char *events[] = { "(invalid)", "(none)", "keypress", "keydown", "keyup", "click", "mousedown", "mouseup", "dblclick", "mousemove", "wheel", "resize",
//"scroll", "blur", "focus", "focusin", "focusout", "deviceorientation", "devicemotion", "orientationchange", "fullscreenchange", "pointerlockchange",
//"visibilitychange", "touchstart", "touchend", "touchmove", "touchcancel", "gamepadconnected", "gamepaddisconnected", "beforeunload",
//"batterychargingchange", "batterylevelchange", "webglcontextlost", "webglcontextrestored", "(invalid)" };
//  ++eventType;
//  if (eventType < 0) eventType = 0;
//  if (eventType >= sizeof(events)/sizeof(events[0])) eventType = sizeof(events)/sizeof(events[0])-1;
//  return events[eventType];
//}

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

// separation of the three touches simulated with shift+mouse, as MotionEventProcessor.DELTA in WebGL
static const Vector2F SHIFT_TOUCHES_DELTA(10, 0);

const Vector2F G3MWidget_Emscripten::createPosition(const EmscriptenMouseEvent* e) const {
  const int canvasAbsoluteLeft = getAbsoluteLeft( EMStorage::put(_canvas) );
  const int canvasAbsoluteTop  = getAbsoluteTop ( EMStorage::put(_canvas) );
  return Vector2F((e->clientX - canvasAbsoluteLeft) * _devicePixelRatio,
                  (e->clientY - canvasAbsoluteTop ) * _devicePixelRatio);
}

std::vector<const Touch*> G3MWidget_Emscripten::createMouseTouches(const EmscriptenMouseEvent* e) const {
  const Vector2F position = createPosition(e);
  const unsigned char tapCount = 1;

  std::vector<const Touch*> touches;
  if (e->shiftKey) {
    touches.push_back( new Touch(position.sub(SHIFT_TOUCHES_DELTA), tapCount) );
    touches.push_back( new Touch(position,                          tapCount) );
    touches.push_back( new Touch(position.add(SHIFT_TOUCHES_DELTA), tapCount) );
  }
  else {
    touches.push_back( new Touch(position, tapCount) );
  }
  return touches;
}

void G3MWidget_Emscripten::dispatchTouchEvent(const TouchEvent* event) {
  _g3mWidget->onTouchEvent(event);
  delete event;
}

EM_BOOL G3MWidget_Emscripten::_onMouseWheelEvent(int eventType,
                                                 const EmscriptenWheelEvent* e) {
  const Vector2F position = createPosition(&(e->mouse));
  // same value as WebGL's wheelDelta clamped to [-1, 1]; wheelDelta has the opposite sign of deltaY
  const double delta = std::max(-1.0, std::min(1.0, -e->deltaY));

  dispatchTouchEvent( TouchEvent::create(TouchEventType::MouseWheel,
                                         new Touch(position, (unsigned char) 0),
                                         delta) );

  return EM_TRUE;
}

EM_BOOL G3MWidget_Emscripten::_onMouseEvent(int eventType,
                                            const EmscriptenMouseEvent* e)
{
  if (eventType == EMSCRIPTEN_EVENT_MOUSEDOWN) {
    _mouseDown = true;

    dispatchTouchEvent( TouchEvent::create(TouchEventType::Down, createMouseTouches(e)) );

    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_MOUSEMOVE) {
    if (_mouseDown) {
      dispatchTouchEvent( TouchEvent::create(TouchEventType::Move, createMouseTouches(e)) );
    }

    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_MOUSEUP) {
    _mouseDown = false;

    const bool isSecondaryButton = !e->shiftKey && e->ctrlKey && (e->button == 0);
    const TouchEventType type = isSecondaryButton ? TouchEventType::LongPress : TouchEventType::Up;
    dispatchTouchEvent( TouchEvent::create(type, createMouseTouches(e)) );

    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_DBLCLICK) {
    dispatchTouchEvent( TouchEvent::create(TouchEventType::Down,
                                           new Touch(createPosition(e), (unsigned char) 2)) );

    return EM_TRUE;
  }
  else {
    return EM_FALSE;
  }
}

std::vector<const Touch*> G3MWidget_Emscripten::createTouches(const EmscriptenTouchEvent* e,
                                                               const bool onlyChanged) const {
  const int canvasAbsoluteLeft = getAbsoluteLeft( EMStorage::put(_canvas) );
  const int canvasAbsoluteTop  = getAbsoluteTop ( EMStorage::put(_canvas) );
  const unsigned char tapCount = 1;

  std::vector<const Touch*> touches;
  for (int i = 0; i < e->numTouches; i++) {
    const EmscriptenTouchPoint& point = e->touches[i];
    if (onlyChanged && !point.isChanged) {
      continue;
    }
    const Vector2F position((point.clientX - canvasAbsoluteLeft) * _devicePixelRatio,
                            (point.clientY - canvasAbsoluteTop ) * _devicePixelRatio);
    touches.push_back( new Touch(position, tapCount) );
  }
  return touches;
}

EM_BOOL G3MWidget_Emscripten::_onTouchEvent(int eventType,
                                            const EmscriptenTouchEvent* e)
{
  std::vector<const Touch*> touches;
  TouchEventType type;

  switch (eventType) {
    case EMSCRIPTEN_EVENT_TOUCHSTART:
      type    = TouchEventType::Down;
      touches = createTouches(e, false);
      break;

    case EMSCRIPTEN_EVENT_TOUCHMOVE:
      type    = TouchEventType::Move;
      touches = createTouches(e, false);
      break;

    case EMSCRIPTEN_EVENT_TOUCHEND:
      // as WebGL: an Up carries the touches that ended (changedTouches)
      type    = TouchEventType::Up;
      touches = createTouches(e, true);
      break;

    default:
      // touchcancel: as WebGL, no event
      return EM_TRUE;
  }

  if (touches.empty()) {
    ILogger::instance()->logError("G3MWidget_Emscripten: touch event type %d without touches", eventType);
    return EM_TRUE;
  }

  dispatchTouchEvent( TouchEvent::create(type, touches) );

  return EM_TRUE;
}

void G3MWidget_Emscripten::startWidget(const std::string& canvasContainerID) {
  val document = val::global("document");

  val canvasContainer = document.call<val>("getElementById", val(canvasContainerID));
  if ( !canvasContainer.as<bool>() ) {
    emscripten_console_error("Can't find canvasContainer");
    THROW_EXCEPTION("Can't find canvasContainer!");
  }

  if (!isWebGLSupported()) {
    // as G3MWidget_WebGL, the container shows why there is no globe
    const char* message = "Your browser does not support WebGL.";
    emscripten_console_error(message);
    val messageElement = document.call<val>("createElement", val("div"));
    messageElement.set("textContent", val(message));
    canvasContainer.call<void>("appendChild", messageElement);
    return;
  }

  if (_g3mWidget != NULL) {
    canvasContainer.call<void>("appendChild", _canvas);

    EM_ASM({
      var canvas = document.getElementById("_g3m_canvas");
      canvas.addEventListener("webglcontextlost", function(event) {
        event.preventDefault();
        alert("webglcontextlost");
      }, false);
    });

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
                                 0,                             // int fps: 0 = requestAnimationFrame, as WebGL
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
