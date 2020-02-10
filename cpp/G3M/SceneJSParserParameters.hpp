//
//  SceneJSParserParameters.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/19/18.
//

#ifndef SceneJSParserParameters_hpp
#define SceneJSParserParameters_hpp

class SceneJSParserParameters {
public:
  const bool _depthTest;
  const bool _generateMipmap;
  const int  _defaultWrapS;
  const int  _defaultWrapT;

  SceneJSParserParameters(const SceneJSParserParameters& that);

  SceneJSParserParameters(const bool depthTest,
                          const bool generateMipmap,
                          const int  defaultWrapS,
                          const int  defaultWrapT);
  
};

#endif
