package org.glob3.mobile.generated; 
//
//  SceneJSShapesParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

//
//  SceneJSShapesParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Shape;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;


public class SceneJSShapesParser
{
  private Shape _rootShape;

  private SceneJSShapesParser(String json)
  {
	  _rootShape = null;
	pvtParse(json);
  }
  private SceneJSShapesParser(IByteBuffer json)
  {
	  _rootShape = null;
	pvtParse(json.getAsString());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Shape* getRootShape() const
  private Shape getRootShape()
  {
	return _rootShape;
  }

  private void pvtParse(String json)
  {
	JSONBaseObject jsonRootObject = IJSONParser.instance().parse(json);
  
	_rootShape = toShape(jsonRootObject);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Shape* toShape(JSONBaseObject* jsonObject) const
  private Shape toShape(JSONBaseObject jsonObject)
  {
	int ____DIEGO_AT_WORK;
  
	return null;
  }


  public static Shape parse(String json)
  {
	return new SceneJSShapesParser(json).getRootShape();
  }
  public static Shape parse(IByteBuffer json)
  {
	return new SceneJSShapesParser(json).getRootShape();
  }

}