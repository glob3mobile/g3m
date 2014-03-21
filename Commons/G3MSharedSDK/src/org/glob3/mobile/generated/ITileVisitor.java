package org.glob3.mobile.generated; 
//
//  ITileVisitor.hpp
//  G3MiOSSDK
//
//  Created by Vidal on 1/22/13.
//
//



public interface ITileVisitor
{
  void dispose();

  void visitTile(java.util.ArrayList<Layer> layers, Tile tile);
}