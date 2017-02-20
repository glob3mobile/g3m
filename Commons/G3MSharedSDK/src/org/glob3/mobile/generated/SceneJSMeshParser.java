package org.glob3.mobile.generated;
//
//  SceneJSMeshParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

//
//  SceneJSMeshParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//



//class SGMesh;
//class JSONBaseObject;
//class IByteBuffer;


public class SceneJSMeshParser
{
  private SceneJSMeshParser()
  {
  }


  public static SGMesh parseFromJSONBaseObject(JSONBaseObject jsonObject, String uriPrefix, boolean isTransparent, boolean depthTest)
  {
    SGNode node = SceneJSNodeParser.parseFromJSONBaseObject(jsonObject, depthTest);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

  public static SGMesh parseFromJSON(String json, String uriPrefix, boolean isTransparent, boolean depthTest)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, depthTest);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

  public static SGMesh parseFromJSON(IByteBuffer json, String uriPrefix, boolean isTransparent, boolean depthTest)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, depthTest);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

  public static SGMesh parseFromBSON(IByteBuffer bson, String uriPrefix, boolean isTransparent, boolean depthTest)
  {
    SGNode node = SceneJSNodeParser.parseFromBSON(bson, depthTest);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

}
