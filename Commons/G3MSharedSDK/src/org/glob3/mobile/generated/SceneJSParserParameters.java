package org.glob3.mobile.generated;
//
//  SceneJSParserParameters.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/19/18.
//

//
//  SceneJSParserParameters.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/19/18.
//


public class SceneJSParserParameters
{
  public final boolean _depthTest;
  public final boolean _generateMipmap;
  public final int _defaultWrapS;
  public final int _defaultWrapT;

  public SceneJSParserParameters(SceneJSParserParameters that)
  {
     _depthTest = that._depthTest;
     _generateMipmap = that._generateMipmap;
     _defaultWrapS = that._defaultWrapS;
     _defaultWrapT = that._defaultWrapT;
  
  }

  public SceneJSParserParameters(boolean depthTest, boolean generateMipmap, int defaultWrapS, int defaultWrapT)
  {
     _depthTest = depthTest;
     _generateMipmap = generateMipmap;
     _defaultWrapS = defaultWrapS;
     _defaultWrapT = defaultWrapT;
  
  }

}