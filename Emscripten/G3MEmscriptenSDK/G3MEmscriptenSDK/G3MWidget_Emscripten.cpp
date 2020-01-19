
#include "G3MWidget_Emscripten.hpp"

#include "G3MWidget.hpp"
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
#include "GL.hpp"
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
_devicePixelRatio(1)
{
  val document = val::global("document");
  
  _canvas = document.call<val>("createElement", val("canvas"));
  
  _canvas.set("id", val("_g3m_canvas"));

  _canvas.set("style", val("width:0px; height:0px"));

  val webGLContextArguments = val::object();
  //webGLContextArguments.set("preserveDrawingBuffer",           true);
  webGLContextArguments.set("alpha",                           false);
  webGLContextArguments.set("preferLowPowerToHighPerformance", true);
  webGLContextArguments.set("antialias",                       false);
  _webGLContext = _canvas.call<val>("getContext", val("webgl"), webGLContextArguments);
  
  // jsCanvas.addEventListener("webglcontextlost", function(
  // 		event) {
  // 	event.preventDefault();
  // 	$wnd.alert("webglcontextlost");
  // }, false);
  
  INativeGL* nativeGL = new NativeGL_Emscripten(_webGLContext);
  _gl = new GL(nativeGL);

  //  jsDefineG3MBrowserObjects();
  //
  //  sinkEvents(Event.TOUCHEVENTS | Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
  //
  //  exportJSFunctions();


  val canvasContainer = document.call<val>("getElementById", val(canvasContainerID));
  if ( canvasContainer.as<bool>() ) {
    canvasContainer.call<void>("appendChild", _canvas);
  }
  else {
    emscripten_console_error("Can't find canvasContainer");
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

EM_BOOL G3MWidget_Emscripten::_onMouseEvent(int eventType,
                                            const EmscriptenMouseEvent* e)
{
  if (eventType == EMSCRIPTEN_EVENT_MOUSEDOWN) {
    printf("==> MOUSEDOWN\n");
    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_MOUSEMOVE) {
    printf("==> MOUSEMOVE\n");
    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_MOUSEUP) {
    printf("==> MOUSEUP\n");
    return EM_TRUE;
  }
  else if (eventType == EMSCRIPTEN_EVENT_DBLCLICK) {
    printf("==> DBLCLICK\n");
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

void G3MWidget_Emscripten::startWidget() {
  if (_g3mWidget != NULL) {
    {
      // mouse events
      emscripten_set_mousedown_callback("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
      emscripten_set_mousemove_callback("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
      emscripten_set_mouseup_callback  ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
      emscripten_set_dblclick_callback ("#_g3m_canvas", this, 1, G3MWidget_Emscripten_onMouseEvent);
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
