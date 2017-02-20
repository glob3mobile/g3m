package org.glob3.mobile.generated;
//
//  SceneJSNodeParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

//
//  SceneJSNodeParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//



//class JSONBaseObject;
//class JSONObject;
//class SceneJSParserStatistics;
//class SGNode;
//class SGRotateNode;
//class SGTranslateNode;
//class SGMaterialNode;
//class SGTextureNode;
//class SGGeometryNode;
//class SGLayerNode;
//class IByteBuffer;
//class Color;


public class SceneJSNodeParser
{

  private SceneJSNodeParser()
  {
  }

  private static SGNode toNode(JSONBaseObject jsonBaseObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    SGNode result = null;
  
    if (jsonBaseObject != null)
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject != null)
      {
        final JSONString jsType = jsonObject.getAsString("type");
        if (jsType != null)
        {
          final String type = jsType.value();
          if (type.compareTo("node") == 0)
          {
            result = createNode(jsonObject, statistics, depthTest);
            statistics.computeNode();
          }
          else if (type.compareTo("rotate") == 0)
          {
            result = createRotateNode(jsonObject, statistics, depthTest);
          }
          else if (type.compareTo("translate") == 0)
          {
            result = createTranslateNode(jsonObject, statistics, depthTest);
          }
          else if (type.compareTo("material") == 0)
          {
            result = createMaterialNode(jsonObject, statistics, depthTest);
            statistics.computeMaterial();
          }
          else if (type.compareTo("texture") == 0)
          {
            result = createTextureNode(jsonObject, statistics, depthTest);
          }
          else if (type.compareTo("geometry") == 0)
          {
            result = createGeometryNode(jsonObject, statistics, depthTest);
            statistics.computeGeometry();
          }
          else
          {
            ILogger.instance().logWarning("SceneJS: Unknown type \"%s\"", type);
          }
        }
      }
    }
  
    return result;
  }

  private static SGNode createNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 1; // "type" is already processed
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    SGNode node = new SGNode(id, sID);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static SGRotateNode createRotateNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 1; // "type" is already processed
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final JSONNumber jsX = jsonObject.getAsNumber("x");
    double x = 0.0;
    if (jsX != null)
    {
      x = jsX.value();
      processedKeys++;
    }
  
    final JSONNumber jsY = jsonObject.getAsNumber("y");
    double y = 0.0;
    if (jsY != null)
    {
      y = jsY.value();
      processedKeys++;
    }
  
    final JSONNumber jsZ = jsonObject.getAsNumber("z");
    double z = 0.0;
    if (jsZ != null)
    {
      z = jsZ.value();
      processedKeys++;
    }
  
    final JSONNumber jsAngle = jsonObject.getAsNumber("angle");
    double angle = 0;
    if (jsAngle != null)
    {
      angle = jsAngle.value();
      processedKeys++;
    }
  
    SGRotateNode node = new SGRotateNode(id, sID, x, y, z, angle);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static SGTranslateNode createTranslateNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 1; // "type" is already processed
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final JSONNumber jsX = jsonObject.getAsNumber("x");
    double x = 0.0;
    if (jsX != null)
    {
      x = jsX.value();
      processedKeys++;
    }
  
    final JSONNumber jsY = jsonObject.getAsNumber("y");
    double y = 0.0;
    if (jsY != null)
    {
      y = jsY.value();
      processedKeys++;
    }
  
    final JSONNumber jsZ = jsonObject.getAsNumber("z");
    double z = 0.0;
    if (jsZ != null)
    {
      z = jsZ.value();
      processedKeys++;
    }
  
    SGTranslateNode node = new SGTranslateNode(id, sID, x, y, z);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static SGMaterialNode createMaterialNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 1; // "type" is already processed
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final JSONObject jsBaseColor = jsonObject.getAsObject("baseColor");
    Color baseColor;
    if (jsBaseColor == null)
    {
      baseColor = Color.newFromRGBA(0, 0, 0, 1);
    }
    else
    {
      baseColor = parseColor(jsBaseColor);
      processedKeys++;
    }
  
    final JSONObject jsSpecularColor = jsonObject.getAsObject("specularColor");
    Color specularColor;
    if (jsSpecularColor == null)
    {
      specularColor = Color.newFromRGBA(0, 0, 0, 1);
    }
    else
    {
      specularColor = parseColor(jsSpecularColor);
      processedKeys++;
    }
  
    final JSONNumber jsShine = jsonObject.getAsNumber("shine");
    double shine = 10;
    if (jsShine != null)
    {
      shine = jsShine.value();
      processedKeys++;
    }
  
    final JSONNumber jsSpecular = jsonObject.getAsNumber("specular");
    double specular = 1.0;
    if (jsSpecular != null)
    {
      specular = jsSpecular.value();
      processedKeys++;
    }
  
    final JSONNumber jsAlpha = jsonObject.getAsNumber("alpha");
    double alpha = 1.0;
    if (jsAlpha != null)
    {
      alpha = jsAlpha.value();
      processedKeys++;
    }
  
    final JSONNumber jsEmit = jsonObject.getAsNumber("emit");
    double emit = 0.0;
    if (jsEmit != null)
    {
      emit = jsEmit.value();
      processedKeys++;
    }
  
    SGMaterialNode node = new SGMaterialNode(id, sID, baseColor, specularColor, specular, shine, alpha, emit);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static SGTextureNode createTextureNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 1; // "type" is already processed
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    SGTextureNode node = new SGTextureNode(id, sID);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    final JSONArray jsLayers = jsonObject.getAsArray("layers");
    if (jsLayers != null)
    {
      int layersCount = jsLayers.size();
      for (int i = 0; i < layersCount; i++)
      {
        final JSONObject jsLayer = jsLayers.getAsObject(i);
        if (jsLayer != null)
        {
          node.addLayer(createLayerNode(jsLayer, statistics, depthTest));
        }
      }
  
      processedKeys++;
    }
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static SGGeometryNode createGeometryNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 1; // "type" is already processed
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final JSONString jsPrimitive = jsonObject.getAsString("primitive");
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
      processedKeys++;
    }
  
    final JSONArray jsPositions = jsonObject.getAsArray("positions");
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
      statistics.computeVertex();
    }
  
    final JSONArray jsColors = jsonObject.getAsArray("colors");
    IFloatBuffer colors = null;
    if (jsColors != null)
    {
      final int colorsCount = jsColors.size();
      colors = IFactory.instance().createFloatBuffer(colorsCount);
      for (int i = 0; i < colorsCount; i++)
      {
        final float value = (float) jsColors.getAsNumber(i).value();
        colors.put(i, value);
      }
      processedKeys++;
    }
  
    final JSONArray jsUV = jsonObject.getAsArray("uv");
    IFloatBuffer uv = null;
    if (jsUV != null)
    {
      final int uvCount = jsUV.size();
      uv = IFactory.instance().createFloatBuffer(uvCount);
      boolean isY = false;
      for (int i = 0; i < uvCount; i++)
      {
        float value = (float) jsUV.getAsNumber(i).value();
        if (isY)
        {
          value = 1 - value;
        }
        isY = !isY;
        uv.put(i, value);
      }
      processedKeys++;
    }
  
    final JSONArray jsNormals = jsonObject.getAsArray("normals");
    IFloatBuffer normals = null;
    if (jsNormals != null)
    {
      final int normalsCount = jsNormals.size();
      normals = IFactory.instance().createFloatBuffer(normalsCount);
      for (int i = 0; i < normalsCount; i++)
      {
        float value = (float) jsNormals.getAsNumber(i).value();
        normals.put(i, value);
      }
      processedKeys++;
    }
  
    final JSONArray jsIndices = jsonObject.getAsArray("indices");
    if (jsIndices == null)
    {
      ILogger.instance().logError("Non indexed geometries not supported");
      return null;
    }
    int indicesOutOfRange = 0;
    int indicesCount = jsIndices.size();
    IShortBuffer indices = IFactory.instance().createShortBuffer(indicesCount);
    for (int i = 0; i < indicesCount; i++)
    {
      final long indice = (long) jsIndices.getAsNumber(i).value();
      if (indice > 32767)
      {
        indicesOutOfRange++;
      }
      indices.rawPut(i, (short) indice);
    }
    processedKeys++;
  
    if (indicesOutOfRange > 0)
    {
      ILogger.instance().logError("SceneJSShapesParser: There are %d (of %d) indices out of range.", indicesOutOfRange, indicesCount);
    }
  
    SGGeometryNode node = new SGGeometryNode(id, sID, primitive, vertices, colors, uv, normals, indices, depthTest);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static SGLayerNode createLayerNode(JSONObject jsonObject, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 0; // Layer has not "type"
  
  
    final String id = jsonObject.getAsString("id", "");
    if (id.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String sID = jsonObject.getAsString("sid", "");
    if (sID.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String uri = jsonObject.getAsString("uri", "");
    if (uri.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String applyTo = jsonObject.getAsString("applyTo", "");
    if (applyTo.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String blendMode = jsonObject.getAsString("blendMode", "");
    if (blendMode.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final JSONBoolean jsFlipY = jsonObject.getAsBoolean("flipY");
    boolean flipY = true;
    if (jsFlipY != null)
    {
      flipY = jsFlipY.value();
      processedKeys++;
    }
  
    final String magFilter = jsonObject.getAsString("magFilter", "");
    if (magFilter.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String minFilter = jsonObject.getAsString("minFilter", "");
    if (minFilter.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String wrapS = jsonObject.getAsString("wrapS", "");
    if (wrapS.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    final String wrapT = jsonObject.getAsString("wrapT", "");
    if (wrapT.compareTo("") != 0)
    {
      processedKeys++;
    }
  
    SGLayerNode node = new SGLayerNode(id, sID, uri, applyTo, blendMode, flipY, magFilter, minFilter, wrapS, wrapT);
  
    processedKeys += parseChildren(jsonObject, node, statistics, depthTest);
  
    checkProcessedKeys(jsonObject, processedKeys);
  
    return node;
  }

  private static Color parseColor(JSONObject jsColor)
  {
    final float r = (float) jsColor.getAsNumber("r", 0.0);
    final float g = (float) jsColor.getAsNumber("g", 0.0);
    final float b = (float) jsColor.getAsNumber("b", 0.0);
    final float a = (float) jsColor.getAsNumber("a", 1.0);
  
    return Color.newFromRGBA(r, g, b, a);
  }

  private static int parseChildren(JSONObject jsonObject, SGNode node, SceneJSParserStatistics statistics, boolean depthTest)
  {
    int processedKeys = 0;
  
    final JSONArray jsNodes = jsonObject.getAsArray("nodes");
    if (jsNodes != null)
    {
      final int nodesCount = jsNodes.size();
      for (int i = 0; i < nodesCount; i++)
      {
        final JSONObject child = jsNodes.getAsObject(i);
        if (child != null)
        {
          SGNode childNode = toNode(child, statistics, depthTest);
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

  private static void checkProcessedKeys(JSONObject jsonObject, int processedKeys)
  {
    java.util.ArrayList<String> keys = jsonObject.keys();
    if (processedKeys != keys.size())
    {
      //    for (int i = 0; i < keys.size(); i++) {
      //      printf("%s\n", keys.at(i).c_str());
      //    }
  
      ILogger.instance().logWarning("Not all keys processed in node, processed %i of %i", processedKeys, keys.size());
    }
  }


  public static SGNode parseFromJSONBaseObject(JSONBaseObject jsonObject, boolean depthTest)
  {
    SceneJSParserStatistics statistics = new SceneJSParserStatistics();
  
    SGNode result = toNode(jsonObject, statistics, depthTest);
  
    statistics.log();
  
    return result;
  }

  public static SGNode parseFromJSON(String json, boolean depthTest)
  {
    final JSONBaseObject jsonObject = IJSONParser.instance().parse(json);
    return parseFromJSONBaseObject(jsonObject, depthTest);
  }

  public static SGNode parseFromJSON(IByteBuffer json, boolean depthTest)
  {
    final JSONBaseObject jsonObject = IJSONParser.instance().parse(json);
    return parseFromJSONBaseObject(jsonObject, depthTest);
  }

  public static SGNode parseFromBSON(IByteBuffer bson, boolean depthTest)
  {
    final JSONBaseObject jsonObject = BSONParser.parse(bson);
    return parseFromJSONBaseObject(jsonObject, depthTest);
  }

}
