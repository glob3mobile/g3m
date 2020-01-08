
#include "G3MWidget_Emscripten.hpp"

#include "G3MWidget.hpp"
#include "Logger_WebGL.hpp"
#include "Factory_Emscripten.hpp"
#include "StringUtils_Emscripten.hpp"
#include "StringBuilder_Emscripten.hpp"
#include "MathUtils_Emscripten.hpp"
#include "JSONParser_Emscripten.hpp"
#include "TextUtils_Emscripten.hpp"
#include "DeviceAttitude_Emscripten.hpp"
#include "DeviceLocation_Emscripten.hpp"


G3MWidget_Emscripten::G3MWidget_Emscripten() {
}

G3MWidget_Emscripten::~G3MWidget_Emscripten() {
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
