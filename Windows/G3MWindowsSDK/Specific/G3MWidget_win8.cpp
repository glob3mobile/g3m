
#include "G3MWidget_win8.hpp"
#include "Logger_win8.hpp"
#include "MathUtils_win8.hpp"
#include "StringUtils_win8.hpp"
#include "StringBuilder_win8.hpp"
#include "Factory_win8.hpp"
#include "GPUProgramFactory_D3D.hpp"

#include "G3MWidget.hpp"
#include "D3DRenderer.hpp"
#include "NativeGL_win8.hpp"

//#include "G3MWidget.hpp"

#include "IJSONParser.hpp"
#include "ITextUtils.hpp"


G3MWidget_win8::G3MWidget_win8():
_genericWidget(NULL)
{
	
	_renderer = new D3DRenderer();
	initSingletons((NativeGL_win8*)_renderer->getGL()->getNative());

}

GL* G3MWidget_win8::getGL() {
	return _renderer->getGL();
}

D3DRenderer* G3MWidget_win8::getRenderer(){
	return _renderer;
}


void G3MWidget_win8::setWidget(G3MWidget* genericWidget){
	_genericWidget = genericWidget;
	_renderer->setWidget(_genericWidget);
}


void G3MWidget_win8::initSingletons(NativeGL_win8* ngl) const{
	ILogger* logger = new Logger_win8(InfoLevel);
	IFactory* factory = new Factory_win8(ngl);
	IStringUtils* stringUtils = new StringUtils_win8();
	IStringBuilder* stringBuilder = new StringBuilder_win8();
	IMathUtils* mathUtils = new MathUtils_win8();
	IJSONParser* jsonParser = NULL;
	ITextUtils* textUtils = NULL;
	IGPUProgramFactory* shaderFactory = new GPUProgramFactory_D3D();

	G3MWidget::initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils, shaderFactory);
}