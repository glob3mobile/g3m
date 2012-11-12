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
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGNode;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGRotateNode;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGMaterialNode;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGTextureNode;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGGeometryNode;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGTranslateNode;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGLayerNode;

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
  
	//  _rootShape = toShape(jsonRootObject);
  
	SGNode node = toNode(jsonRootObject);
  
	_rootShape = new SGShape(node);
  
	if (jsonRootObject != null)
		jsonRootObject.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGNode* toNode(JSONBaseObject* jsonBaseObject) const
  private SGNode toNode(JSONBaseObject jsonBaseObject)
  {
  
	if (jsonBaseObject == null)
	{
	  return null;
	}
  
	int ____DIEGO_AT_WORK;
	JSONObject jsonObject = jsonBaseObject.asObject();
  
	SGNode result = null;
  
	if (jsonObject != null)
	{
	  JSONString jsType = jsonObject.getAsString("type");
	  if (jsType != null)
	  {
		final String type = jsType.value();
		if (type.compareTo("node") == 0)
		{
		  result = createNode(jsonObject);
		}
		else if (type.compareTo("rotate") == 0)
		{
		  result = createRotateNode(jsonObject);
		}
		else if (type.compareTo("translate") == 0)
		{
		  result = createTranslateNode(jsonObject);
		}
		else if (type.compareTo("material") == 0)
		{
		  result = createMaterialNode(jsonObject);
		}
		else if (type.compareTo("texture") == 0)
		{
		  result = createTextureNode(jsonObject);
		}
		else if (type.compareTo("geometry") == 0)
		{
		  result = createGeometryNode(jsonObject);
		}
		else
		{
		  ILogger.instance().logWarning("Unknown type \"%s\"", type);
		}
	  }
	}
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int parseCommons(JSONObject* jsonObject, SGNode* node) const
  private int parseCommons(JSONObject jsonObject, SGNode node)
  {
	int processedKeys = 0;
  
	JSONString jsId = jsonObject.getAsString("id");
	if (jsId != null)
	{
	  node.setId(jsId.value());
	  processedKeys++;
	}
  
	JSONString jsSId = jsonObject.getAsString("sid");
	if (jsSId != null)
	{
	  node.setSId(jsSId.value());
	  processedKeys++;
	}
  
	JSONArray jsNodes = jsonObject.getAsArray("nodes");
	if (jsNodes != null)
	{
	  final int nodesCount = jsNodes.size();
	  for (int i = 0; i < nodesCount; i++)
	  {
		JSONObject child = jsNodes.getAsObject(i);
		if (child != null)
		{
		  SGNode childNode = toNode(child);
		  if (childNode != null)
		  {
			node.addNode(childNode);
		  }
		}
	  }
	  processedKeys++;
	}
  
	return processedKeys;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void checkProcessedKeys(JSONObject* jsonObject, int processedKeys) const
  private void checkProcessedKeys(JSONObject jsonObject, int processedKeys)
  {
	java.util.ArrayList<String> keys = jsonObject.keys();
	if (processedKeys != keys.size())
	{
	  for (int i = 0; i < keys.size(); i++)
	  {
		System.out.printf("%s\n", keys.get(i));
	  }
  
	  ILogger.instance().logWarning("Not all keys processed in node, processed %i of %i", processedKeys, keys.size());
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGNode* createNode(JSONObject* jsonObject) const
  private SGNode createNode(JSONObject jsonObject)
  {
  
	int processedKeys = 1; // "type" is already processed
  
	SGNode node = new SGNode();
  
	processedKeys += parseCommons(jsonObject, node);
  
	//  std::vector<std::string> keys = jsonObject->keys();
	//  if (processedKeys != keys.size()) {
	//    for (int i = 0; i < keys.size(); i++) {
	//      printf("%s\n", keys.at(i).c_str());
	//    }
	//
	////    ILogger::instance()->logWarning("Not all keys processed in node of type \"%s\"", type.c_str());
	//    ILogger::instance()->logWarning("Not all keys processed in node");
	//  }
	//
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGRotateNode* createRotateNode(JSONObject* jsonObject) const
  private SGRotateNode createRotateNode(JSONObject jsonObject)
  {
	int processedKeys = 1; // "type" is already processed
  
	SGRotateNode node = new SGRotateNode();
  
	processedKeys += parseCommons(jsonObject, node);
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGTranslateNode* createTranslateNode(JSONObject* jsonObject) const
  private SGTranslateNode createTranslateNode(JSONObject jsonObject)
  {
	int processedKeys = 1; // "type" is already processed
  
	SGTranslateNode node = new SGTranslateNode();
  
	processedKeys += parseCommons(jsonObject, node);
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGMaterialNode* createMaterialNode(JSONObject* jsonObject) const
  private SGMaterialNode createMaterialNode(JSONObject jsonObject)
  {
	int processedKeys = 1; // "type" is already processed
  
	SGMaterialNode node = new SGMaterialNode();
  
	processedKeys += parseCommons(jsonObject, node);
  
	JSONObject jsSpecularColor = jsonObject.getAsObject("specularColor");
	if (jsSpecularColor != null)
	{
	  final double r = jsSpecularColor.getAsNumber("r").value();
	  final double g = jsSpecularColor.getAsNumber("g").value();
	  final double b = jsSpecularColor.getAsNumber("b").value();
	  final double a = jsSpecularColor.getAsNumber("a").value();
	  node.setSpecularColor(Color.newFromRGBA((float) r, (float) g, (float) b, (float) a));
	  processedKeys++;
	}
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGTextureNode* createTextureNode(JSONObject* jsonObject) const
  private SGTextureNode createTextureNode(JSONObject jsonObject)
  {
	int processedKeys = 1; // "type" is already processed
  
	SGTextureNode node = new SGTextureNode();
  
	processedKeys += parseCommons(jsonObject, node);
  
	JSONArray jsLayers = jsonObject.getAsArray("layers");
	if (jsLayers != null)
	{
	  int layersCount = jsLayers.size();
	  for (int i = 0; i < layersCount; i++)
	  {
		JSONObject jsLayer = jsLayers.getAsObject(i);
		if (jsLayer != null)
		{
		  node.addLayer(createLayerNode(jsLayer));
		}
	  }
  
	  processedKeys++;
	}
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGGeometryNode* createGeometryNode(JSONObject* jsonObject) const
  private SGGeometryNode createGeometryNode(JSONObject jsonObject)
  {
	int processedKeys = 1; // "type" is already processed
  
	JSONString jsPrimitive = jsonObject.getAsString("primitive");
	int primitive = GLPrimitive.triangles(); // triangles is the default
	if (jsPrimitive != null)
	{
	  final String strPrimitive = jsPrimitive.value();
  
	  if (strPrimitive.compareTo("points") == 0)
	  {
		primitive = GLPrimitive.points();
	  }
	  else if (strPrimitive.compareTo("lines") == 0)
	  {
		primitive = GLPrimitive.lines();
	  }
	  else if (strPrimitive.compareTo("line-loop") == 0)
	  {
		primitive = GLPrimitive.lineLoop();
	  }
	  else if (strPrimitive.compareTo("line-strip") == 0)
	  {
		primitive = GLPrimitive.lineStrip();
	  }
	  else if (strPrimitive.compareTo("triangles") == 0)
	  {
		primitive = GLPrimitive.triangles();
	  }
	  else if (strPrimitive.compareTo("triangle-strip") == 0)
	  {
		primitive = GLPrimitive.triangleStrip();
	  }
	  else if (strPrimitive.compareTo("triangle-fan") == 0)
	  {
		primitive = GLPrimitive.triangleFan();
	  }
	}
  
	JSONArray jsPositions = jsonObject.getAsArray("positions");
	if (jsPositions == null)
	{
	  ILogger.instance().logError("Mandatory positions are not present");
	  return null;
	}
	processedKeys++;
	int verticesCount = jsPositions.size();
	IFloatBuffer vertices = IFactory.instance().createFloatBuffer(verticesCount);
	for (int i = 0; i < verticesCount; i++)
	{
	  vertices.put(i, (float) jsPositions.getAsNumber(i).value());
	}
  
	JSONArray jsColors = jsonObject.getAsArray("colors");
	IFloatBuffer colors = null;
	if (jsColors != null)
	{
	  final int colorsCount = jsColors.size();
	  colors = IFactory.instance().createFloatBuffer(colorsCount);
	  for (int i = 0; i < colorsCount; i++)
	  {
		colors.put(i, (float) jsColors.getAsNumber(i).value());
	  }
	  processedKeys++;
	}
  
	JSONArray jsUV = jsonObject.getAsArray("uv");
	IFloatBuffer uv = null;
	if (jsUV != null)
	{
	  final int uvCount = jsUV.size();
	  uv = IFactory.instance().createFloatBuffer(uvCount);
	  for (int i = 0; i < uvCount; i++)
	  {
		uv.put(i, (float) jsUV.getAsNumber(i).value());
	  }
	  processedKeys++;
	}
  
	JSONArray jsNormals = jsonObject.getAsArray("normals");
	IFloatBuffer normals = null;
	if (jsNormals != null)
	{
	  processedKeys++;
	}
  
	JSONArray jsIndices = jsonObject.getAsArray("indices");
	if (jsIndices == null)
	{
	  ILogger.instance().logError("Non indexed geometries not supported");
	  return null;
	}
	int indicesCount = jsIndices.size();
	IIntBuffer indices = IFactory.instance().createIntBuffer(indicesCount);
	for (int i = 0; i < indicesCount; i++)
	{
	  indices.put(i, (int) jsIndices.getAsNumber(i).value());
	}
	processedKeys++;
  
	SGGeometryNode node = new SGGeometryNode(primitive, vertices, colors, uv, normals, indices);
  
	processedKeys += parseCommons(jsonObject, node);
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGLayerNode* createLayerNode(JSONObject* jsonObject) const
  private SGLayerNode createLayerNode(JSONObject jsonObject)
  {
	int processedKeys = 1; // "type" is already processed
  
	SGLayerNode node = new SGLayerNode();
  
	processedKeys += parseCommons(jsonObject, node);
  
	int ____DIEGO_AT_WORK;
  //  JSONString* jsUri = jsonObject->getAsString("uri");
  //  if (jsUri != NULL) {
  //    node->setUri( jsUri->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONString* jsApplyTo = jsonObject->getAsString("applyTo");
  //  if (jsApplyTo != NULL) {
  //    node->setApplyTo( jsApplyTo->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONString* jsBlendMode = jsonObject->getAsString("blendMode");
  //  if (jsBlendMode != NULL) {
  //    node->setBlendMode( jsBlendMode->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONBoolean* jsFlipY = jsonObject->getAsBoolean("flipY");
  //  if (jsFlipY != NULL) {
  //    node->setFlipY( jsFlipY->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONString* jsMagFilter = jsonObject->getAsString("magFilter");
  //  if (jsMagFilter != NULL) {
  //    node->setMagFilter( jsMagFilter->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONString* jsMinFilter = jsonObject->getAsString("minFilter");
  //  if (jsMinFilter != NULL) {
  //    node->setMinFilter( jsMinFilter->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONString* jsWrapS = jsonObject->getAsString("wrapS");
  //  if (jsWrapS != NULL) {
  //    node->setWrapS( jsWrapS->value() );
  //    processedKeys++;
  //  }
  //
  //  JSONString* jsWrapT = jsonObject->getAsString("wrapT");
  //  if (jsWrapT != NULL) {
  //    node->setWrapT( jsWrapT->value() );
  //    processedKeys++;
  //  }
  
	checkProcessedKeys(jsonObject, processedKeys);
  
	return node;
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