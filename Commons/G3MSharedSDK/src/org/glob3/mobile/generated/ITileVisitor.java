package org.glob3.mobile.generated;
//
//  ITileVisitor.hpp
//  G3M
//
//  Created by Vidal on 1/22/13.
//
//


//class Layer;
//class Tile;

public interface ITileVisitor
{
  void dispose();

  void visitTile(java.util.ArrayList<Layer> layers, Tile tile);
}
