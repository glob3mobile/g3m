
#include "G3MWidget_Wasm.hpp"

#include "G3MWidget.hpp"
#include "Logger_WebGL.hpp"
#include "Factory_Wasm.hpp"
#include "StringUtils_Wasm.hpp"
#include "StringBuilder_Wasm.hpp"
#include "MathUtils_Wasm.hpp"
#include "JSONParser_Wasm.hpp"
#include "TextUtils_Wasm.hpp"
#include "DeviceAttitude_Wasm.hpp"
#include "DeviceLocation_Wasm.hpp"


G3MWidget_Wasm::G3MWidget_Wasm() {
}

G3MWidget_Wasm::~G3MWidget_Wasm() {
}

void G3MWidget_Wasm::initSingletons() {
  ILogger*         logger         = new Logger_Wasm(LogLevel.InfoLevel);
  IFactory*        factory        = new Factory_Wasm();
  IStringUtils*    stringUtils    = new StringUtils_Wasm();
  IStringBuilder*  stringBuilder  = new StringBuilder_Wasm(IStringBuilder::DEFAULT_FLOAT_PRECISION);
  IMathUtils*      mathUtils      = new MathUtils_Wasm();
  IJSONParser*     jsonParser     = new JSONParser_Wasm();
  ITextUtils*      textUtils      = new TextUtils_Wasm();
  IDeviceAttitude* deviceAttitude = new DeviceAttitude_Wasm();
  IDeviceLocation* deviceLocation = new DeviceLocation_Wasm();

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
