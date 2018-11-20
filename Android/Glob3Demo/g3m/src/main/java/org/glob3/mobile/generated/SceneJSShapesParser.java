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




//class SceneJSParserParameters;
//class Geodetic3D;
//class IByteBuffer;
//class SGShape;


public class SceneJSShapesParser
{
  private SceneJSShapesParser()
  {

  }


  public static SGShape parseFromJSON(String json, String uriPrefix, boolean isTransparent, SceneJSParserParameters parameters, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, parameters);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, new Geodetic3D(position), altitudeMode);
  }

  public static SGShape parseFromJSON(IByteBuffer json, String uriPrefix, boolean isTransparent, SceneJSParserParameters parameters, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, parameters);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, new Geodetic3D(position), altitudeMode);
  }

  public static SGShape parseFromBSON(IByteBuffer bson, String uriPrefix, boolean isTransparent, SceneJSParserParameters parameters, Geodetic3D position, AltitudeMode altitudeMode)
  {
    SGNode node = SceneJSNodeParser.parseFromBSON(bson, parameters);
    return (node == null) ? null : new SGShape(node, uriPrefix, isTransparent, new Geodetic3D(position), altitudeMode);
  }

}
