//
//  SceneJSParserParameters.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/19/18.
//

#include "SceneJSParserParameters.hpp"

SceneJSParserParameters::SceneJSParserParameters(const SceneJSParserParameters& that) :
_depthTest(that._depthTest),
_generateMipmap(that._generateMipmap),
_defaultWrapS(that._defaultWrapS),
_defaultWrapT(that._defaultWrapT)
{
  
}

SceneJSParserParameters::SceneJSParserParameters(const bool depthTest,
                                                 const bool generateMipmap,
                                                 const int  defaultWrapS,
                                                 const int  defaultWrapT) :
_depthTest(depthTest),
_generateMipmap(generateMipmap),
_defaultWrapS(defaultWrapS),
_defaultWrapT(defaultWrapT)
{
  
}
