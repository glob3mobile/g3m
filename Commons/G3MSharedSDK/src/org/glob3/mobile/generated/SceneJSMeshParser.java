package org.glob3.mobile.generated;
//
//  SceneJSMeshParser.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

//
//  SceneJSMeshParser.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//



//class SceneJSParserParameters;
//class IByteBuffer;
//class SGMesh;


public class SceneJSMeshParser
{
  private SceneJSMeshParser()
  {
  }


  public static SGMesh parseFromJSON(String json, String uriPrefix, boolean isTransparent, SceneJSParserParameters parameters)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, parameters);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

  public static SGMesh parseFromJSON(IByteBuffer json, String uriPrefix, boolean isTransparent, SceneJSParserParameters parameters)
  {
    SGNode node = SceneJSNodeParser.parseFromJSON(json, parameters);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

  public static SGMesh parseFromBSON(IByteBuffer bson, String uriPrefix, boolean isTransparent, SceneJSParserParameters parameters)
  {
    SGNode node = SceneJSNodeParser.parseFromBSON(bson, parameters);
    return (node == null) ? null : new SGMesh(node, uriPrefix, isTransparent);
  }

}