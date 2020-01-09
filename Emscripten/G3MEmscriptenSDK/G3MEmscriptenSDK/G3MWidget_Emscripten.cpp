
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


using namespace emscripten;

G3MWidget_Emscripten::G3MWidget_Emscripten() :
_canvas(val::null()),
_webGLContext(val::null())
{
  val document = val::global("document");
  
  _canvas = document.call<val>("createElement", "canvas");
  
  _canvas.call<void>("setId", "_g3m_canvas");
  
  val webGLContextArguments = val::object();
  //webGLContextArguments["preserveDrawingBuffer"]           = val(true);
  webGLContextArguments["alpha"]                           = val(false);
  webGLContextArguments["preferLowPowerToHighPerformance"] = val(true);
  webGLContextArguments["antialias"]                       = val(false);
  _webGLContext = _canvas.call<val>("getContext", "webgl", webGLContextArguments);
  
  // jsCanvas.addEventListener("webglcontextlost", function(
  // 		event) {
  // 	event.preventDefault();
  // 	$wnd.alert("webglcontextlost");
  // }, false);
  
  INativeGL* nativeGL = new NativeGL_Emscripten(_webGLContext);
  _gl = new GL(nativeGL);
  
}

G3MWidget_Emscripten::~G3MWidget_Emscripten() {
}

GL* G3MWidget_Emscripten::getGL() const {
  return _gl;
}

void G3MWidget_Emscripten::setG3MWidget(G3MWidget* g3mWidget) {
  _g3mWidget = g3mWidget;
}

void G3MWidget_Emscripten::startWidget() {
  if (_g3mWidget != NULL) {
    _motionEventProcessor = new MotionEventProcessor(this, _canvas.getCanvasElement());
    jsAddResizeHandler(_canvas.getCanvasElement());
    
    jsStartRenderLoop();
  }
}

void G3MWidget_Emscripten::initSingletons() {
  ILogger*         logger         = new Logger_Emscripten(LogLevel.InfoLevel);
  IFactory*        factory        = new Factory_Emscripten();
  IStringUtils*    stringUtils    = new StringUtils_Emscripten();
  IStringBuilder*  stringBuilder  = new StringBuilder_Emscripten(IStringBuilder::DEFAULT_FLOAT_PRECISION);
  IMathUtils*      mathUtils      = new MathUtils_Emscripten();
  IJSONParser*     jsonParser     = new JSONParser_Emscripten();
  ITextUtils*      textUtils      = new TextUtils_Emscripten();
  IDeviceAttitude* deviceAttitude = new DeviceAttitude_Emscripten();
  IDeviceLocation* deviceLocation = new DeviceLocation_Emscripten();
  
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

bool G3MWidget_Emscripten::isWebGLSupported() const {
  return (!_canvas.isNull() && !_webGLContext.isNull());
}