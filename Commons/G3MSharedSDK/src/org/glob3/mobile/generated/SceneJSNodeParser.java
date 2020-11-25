package org.glob3.mobile.generated;
//
//  SceneJSNodeParser.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

//
//  SceneJSNodeParser.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//



//class SGNode;
//class JSONBaseObject;
//class JSONObject;
//class SGRotateNode;
//class SGTranslateNode;
//class SGMaterialNode;
//class SGTextureNode;
//class SGGeometryNode;
//class SGLayerNode;
//class Color;
//class IByteBuffer;
//class JSONNumber;
//class SceneJSParserParameters;


public class SceneJSNodeParser
{

  public static class Statistics
  {
    private int _nodesCount;
    private int _materialsCount;
    private int _geometriesCount;
    private int _verticesCount;

    public Statistics()
    {
       _nodesCount = 0;
       _materialsCount = 0;
       _geometriesCount = 0;
       _verticesCount = 0;
    }

    public final void computeNode()
    {
      _nodesCount++;
    }

    public final int getNodesCount()
    {
      return _nodesCount;
    }

    public final void computeMaterial()
    {
      _materialsCount++;
    }

    public final int getMaterialsCount()
    {
      return _materialsCount;
    }

    public final void computeGeometry()
    {
      _geometriesCount++;
    }

    public final int getGeometriesCount()
    {
      return _geometriesCount;
    }

    public final void computeVertex()
    {
      _verticesCount++;
    }

    public final int getVerticesCount()
    {
      return _verticesCount;
    }

    public final String asLogString()
    {
      IStringBuilder statsSB = IStringBuilder.newStringBuilder();
    
      statsSB.addString("Nodes=");
      statsSB.addInt(getNodesCount());
      statsSB.addString("; Materials=");
      statsSB.addInt(getMaterialsCount());
      statsSB.addString("; Geometries=");
      statsSB.addInt(getGeometriesCount());
      statsSB.addString("; Vertices=");
      statsSB.addInt(getVerticesCount());
      statsSB.addString("; Vert/Geom=");
      statsSB.addFloat((float) getVerticesCount() / getGeometriesCount());
      statsSB.addString("; Vert/Mat=");
      statsSB.addFloat((float) getVerticesCount() / getMaterialsCount());
      statsSB.addString("; Vert/Nod=");
      statsSB.addFloat((float) getVerticesCount() / getNodesCount());
    
      final String stats = statsSB.getString();
      if (statsSB != null)
         statsSB.dispose();
    
      return stats;
    }

    public final void log()
    {
      if (ILogger.instance() != null)
      {
        ILogger.instance().logInfo("\nSceneJSNodeParser::Statistics: %s", asLogString());
      }
    }
  }



  private SceneJSNodeParser()
  {
  }

  private static SGNode toNode(JSONBaseObject jsonBaseObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
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
            result = createNode(jsonObject, statistics, parameters);
            statistics.computeNode();
          }
          else if (type.compareTo("rotate") == 0)
          {
            result = createRotateNode(jsonObject, statistics, parameters);
          }
          else if (type.compareTo("translate") == 0)
          {
            result = createTranslateNode(jsonObject, statistics, parameters);
          }
          else if (type.compareTo("material") == 0)
          {
            result = createMaterialNode(jsonObject, statistics, parameters);
            statistics.computeMaterial();
          }
          else if (type.compareTo("texture") == 0)
          {
            result = createTextureNode(jsonObject, statistics, parameters);
          }
          else if (type.compareTo("geometry") == 0)
          {
            result = createGeometryNode(jsonObject, statistics, parameters);
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

  private static SGNode createNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
    SGNode node = new SGNode(id, sID);
    parseChildren(jsonObject, node, statistics, parameters);
    return node;
  }

  private static SGRotateNode createRotateNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
  
    final double x = jsonObject.getAsNumber("x", 0);
    final double y = jsonObject.getAsNumber("y", 0);
    final double z = jsonObject.getAsNumber("z", 0);
  
    final double angle = jsonObject.getAsNumber("angle", 0);
  
    SGRotateNode node = new SGRotateNode(id, sID, x, y, z, angle);
  
    parseChildren(jsonObject, node, statistics, parameters);
  
    return node;
  }

  private static SGTranslateNode createTranslateNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
  
    final double x = jsonObject.getAsNumber("x", 0);
    final double y = jsonObject.getAsNumber("y", 0);
    final double z = jsonObject.getAsNumber("z", 0);
  
    SGTranslateNode node = new SGTranslateNode(id, sID, x, y, z);
  
    parseChildren(jsonObject, node, statistics, parameters);
  
    return node;
  }

  private static SGMaterialNode createMaterialNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
  
    final Color baseColor = parseColor(jsonObject.getAsObject("baseColor"), 0, 0, 0, 1);
    final Color specularColor = parseColor(jsonObject.getAsObject("specularColor"), 0, 0, 0, 1);
  
    final double shine = jsonObject.getAsNumber("shine", 10);
    final double specular = jsonObject.getAsNumber("specular", 1);
    final double alpha = jsonObject.getAsNumber("alpha", 1);
    final double emit = jsonObject.getAsNumber("emit", 0);
  
    SGMaterialNode node = new SGMaterialNode(id, sID, baseColor, specularColor, specular, shine, alpha, emit);
  
    parseChildren(jsonObject, node, statistics, parameters);
  
    return node;
  }

  private static SGTextureNode createTextureNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
  
    SGTextureNode node = new SGTextureNode(id, sID);
  
    parseChildren(jsonObject, node, statistics, parameters);
  
    final JSONArray jsLayers = jsonObject.getAsArray("layers");
    if (jsLayers != null)
    {
      int layersCount = jsLayers.size();
      for (int i = 0; i < layersCount; i++)
      {
        final JSONObject jsLayer = jsLayers.getAsObject(i);
        if (jsLayer != null)
        {
          node.addLayer(createLayerNode(jsLayer, statistics, parameters));
        }
      }
    }
  
    return node;
  }

  private static SGGeometryNode createGeometryNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
  
    int primitive = GLPrimitive.triangles();
    {
      final String strPrimitive = jsonObject.getAsString("primitive", "triangles");
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
  
    final JSONArray jsPositions = jsonObject.getAsArray("positions");
    if (jsPositions == null)
    {
      ILogger.instance().logError("Mandatory positions are not present");
      return null;
    }
  
    final int verticesCount = jsPositions.size();
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
    }
  
    final JSONArray jsIndices = jsonObject.getAsArray("indices");
    if (jsIndices == null)
    {
      ILogger.instance().logError("Non indexed geometries not supported");
      return null;
    }
    int indicesOutOfRange = 0;
    final int indicesCount = jsIndices.size();
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
  
    if (indicesOutOfRange > 0)
    {
      ILogger.instance().logError("SceneJSShapesParser: There are %d (of %d) indices out of range.", indicesOutOfRange, indicesCount);
    }
  
    SGGeometryNode node = new SGGeometryNode(id, sID, primitive, vertices, colors, uv, normals, indices, parameters._depthTest);
  
    parseChildren(jsonObject, node, statistics, parameters);
  
    return node;
  }

  private static SGLayerNode createLayerNode(JSONObject jsonObject, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final String id = jsonObject.getAsString("id", "");
    final String sID = jsonObject.getAsString("sid", "");
    final String uri = jsonObject.getAsString("uri", "");
  
  //  const std::string applyTo = jsonObject->getAsString("applyTo", "baseColor");
  //
  //  const std::string blendMode = jsonObject->getAsString("blendMode", "add");
  //
  //  const bool flipY = jsonObject->getAsBoolean("flipY", true);
  //
  //  const std::string magFilter = jsonObject->getAsString("magFilter", "linear");
  //  const std::string minFilter = jsonObject->getAsString("minFilter", "linear");
  
  //  const std::string wrapS = jsonObject->getAsString("wrapS", "clampToEdge");
  //  const std::string wrapT = jsonObject->getAsString("wrapT", "clampToEdge");
  
  
  ////  const bool generateMipmap = false;
  //  const int wrapS = GLTextureParameterValue::repeat();
  //  const int wrapT = GLTextureParameterValue::mirroredRepeat();
  
    final int wrapS = parseInt(jsonObject.getAsNumber("wrapS"), parameters._defaultWrapS);
    final int wrapT = parseInt(jsonObject.getAsNumber("wrapT"), parameters._defaultWrapT);
  
    SGLayerNode node = new SGLayerNode(id, sID, uri, wrapS, wrapT, parameters._generateMipmap);
  
    parseChildren(jsonObject, node, statistics, parameters);
  
    return node;
  }

  private static Color parseColor(JSONObject jsColor, float defaultRed, float defaultGreen, float defaultBlue, float defaultAlpha)
  {
    if (jsColor == null)
    {
      return Color.newFromRGBA(defaultRed, defaultGreen, defaultBlue, defaultAlpha);
    }
  
    final float r = (float) jsColor.getAsNumber("r", defaultRed);
    final float g = (float) jsColor.getAsNumber("g", defaultGreen);
    final float b = (float) jsColor.getAsNumber("b", defaultBlue);
    final float a = (float) jsColor.getAsNumber("a", defaultAlpha);
  
    return Color.newFromRGBA(r, g, b, a);
  }

  private static void parseChildren(JSONObject jsonObject, SGNode node, SceneJSNodeParser.Statistics statistics, SceneJSParserParameters parameters)
  {
    final JSONArray jsNodes = jsonObject.getAsArray("nodes");
    if (jsNodes != null)
    {
      final int nodesCount = jsNodes.size();
      for (int i = 0; i < nodesCount; i++)
      {
        final JSONObject child = jsNodes.getAsObject(i);
        if (child != null)
        {
          SGNode childNode = toNode(child, statistics, parameters);
          if (childNode != null)
          {
            node.addNode(childNode);
          }
        }
      }
    }
  }

  private static int parseInt(JSONNumber jsonNumber, int defaultValue)
  {
    if (jsonNumber == null)
    {
      return defaultValue;
    }
    return (int) jsonNumber.value();
  }


  public static SGNode parseFromJSONBaseObject(JSONBaseObject jsonObject, SceneJSParserParameters parameters)
  {
    SceneJSNodeParser.Statistics statistics = new SceneJSNodeParser.Statistics();
    SGNode result = toNode(jsonObject, statistics, parameters);
    statistics.log();
    return result;
  }

  public static SGNode parseFromJSON(String json, SceneJSParserParameters parameters)
  {
    final JSONBaseObject jsonObject = IJSONParser.instance().parse(json);
    SGNode result = parseFromJSONBaseObject(jsonObject, parameters);
    if (jsonObject != null)
       jsonObject.dispose();
    return result;
  }

  public static SGNode parseFromJSON(IByteBuffer json, SceneJSParserParameters parameters)
  {
    final JSONBaseObject jsonObject = IJSONParser.instance().parse(json);
    SGNode result = parseFromJSONBaseObject(jsonObject, parameters);
    if (jsonObject != null)
       jsonObject.dispose();
    return result;
  }

  public static SGNode parseFromBSON(IByteBuffer bson, SceneJSParserParameters parameters)
  {
    final JSONBaseObject jsonObject = BSONParser.parse(bson);
    SGNode result = parseFromJSONBaseObject(jsonObject, parameters);
    if (jsonObject != null)
       jsonObject.dispose();
    return result;
  }

}