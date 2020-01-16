
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

#include <math.h>

#include <emscripten/emscripten.h>
#include <emscripten/html5.h>


using namespace emscripten;

G3MWidget_Emscripten::G3MWidget_Emscripten() :
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
  const int canvasWidth  = _canvas["clientWidth"].as<int>();
  const int canvasHeight = _canvas["clientHeight"].as<int>();

  const val canvasParent = _canvas["parentNode"];
  const int canvasParentWidth  = canvasParent["clientWidth"].as<int>();
  const int canvasParentHeight = canvasParent["clientHeight"].as<int>();

  if ((canvasWidth != canvasParentWidth) || (canvasHeight != canvasParentHeight)) {
    onSizeChanged(canvasParentWidth, canvasParentHeight);
  }
}

void G3MWidget_Emscripten::onSizeChanged(const int width,
                                         const int height) {
  if ((_width != width) || (_height != height)) {
    _width  = width;
    _height = height;
    //    setPixelSize(_width, _height);

    val window = val::global("window");
    val valDevicePixelRatio = window["devicePixelRatio"];
    _devicePixelRatio = valDevicePixelRatio.as<bool>() ? valDevicePixelRatio.as<float>() : 1;

    _physicalWidth  = round(_width  * _devicePixelRatio);
    _physicalHeight = round(_height * _devicePixelRatio);

    //    _canvas.setCoordinateSpaceWidth(_physicalWidth);
    //    _canvas.setCoordinateSpaceHeight(_physicalHeight);
    _canvas.set("width",  _physicalWidth);
    _canvas.set("height", _physicalHeight);
  }
}

void G3MWidget_Emscripten::startWidget() {
  if (_g3mWidget != NULL) {
    //    _motionEventProcessor = new MotionEventProcessor(this, _canvas);
    addResizeHandler();

    //    jsStartRenderLoop();
    emscripten_set_main_loop_arg(G3MWidget_Emscripten_loopStep, // em_arg_callback_func func
                                 (void*) this,                  // void *arg
                                 60,                            // int fps
                                 1                              // int simulate_infinite_loop
                                 );
  }
}

void G3MWidget_Emscripten::initSingletons() {
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

void G3MWidget_Emscripten::addInto(const std::string& containerID) {
  val document = val::global("document");

  val container = document.call<val>("getElementById", containerID);

  container.call<void>("appendChild", _canvas);
}
