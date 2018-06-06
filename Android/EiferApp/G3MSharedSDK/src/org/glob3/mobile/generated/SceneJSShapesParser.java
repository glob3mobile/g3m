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




//class SGShape;
//class JSONBaseObject;
//class Geodetic3D;
//class IByteBuffer;

public class SceneJSShapesParser
{
  private SceneJSShapesParser(){}

  public static SGShape parseFromJSONBaseObject(JSONBaseObject jsonObject, String uriPrefix, boolean isTransparent, boolean depthTest, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromJSONBaseObject(jsonObject, depthTest);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
  }

  public static SGShape parseFromJSON(String json, String uriPrefix, boolean isTransparent, boolean depthTest, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, depthTest);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
  }

  public static SGShape parseFromJSON(IByteBuffer json, String uriPrefix, boolean isTransparent, boolean depthTest, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, depthTest);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
  }

  public static SGShape parseFromBSON(IByteBuffer bson, String uriPrefix, boolean isTransparent, boolean depthTest, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromBSON(bson, depthTest);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
  }

}