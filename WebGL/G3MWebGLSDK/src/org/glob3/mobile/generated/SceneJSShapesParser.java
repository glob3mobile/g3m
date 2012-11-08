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
//ORIGINAL LINE: Shape* toShape(JSONBaseObject* jsonBaseObject) const
  private Shape toShape(JSONBaseObject jsonBaseObject)
  {
  //  int ____DIEGO_AT_WORK;
	JSONObject jsonObject = jsonBaseObject.asObject();
  
	java.util.ArrayList<String> keys = jsonObject.keys();
  
	for (int i = 0; i < keys.size(); i++)
	{
	  String key = keys.get(i);
	  String value = jsonObject.get(key).description();
	  System.out.printf("%s=%s", key, value);
	}
  
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