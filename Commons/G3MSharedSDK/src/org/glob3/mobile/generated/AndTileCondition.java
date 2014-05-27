package org.glob3.mobile.generated; 
//
//  AndTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/13.
//
//

//
//  AndTileCondition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/13.
//
//




public class AndTileCondition extends LayerCondition
{
  private java.util.ArrayList<LayerCondition> _children = new java.util.ArrayList<LayerCondition>();

  private AndTileCondition(java.util.ArrayList<LayerCondition> children)
  {
     _children = children;
  }


  public AndTileCondition(LayerCondition child1, LayerCondition child2)
  {
    _children.add(child1);
    _children.add(child2);
  }

  public void dispose()
  {
    int size = _children.size();
    for (int i = 0; i < size; i++)
    {
      LayerCondition child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  }

//  bool isAvailable(const G3MRenderContext* rc,
//                   const Tile* tile) const;
//
//  bool isAvailable(const G3MEventContext* ec,
//                   const Tile* tile) const;


  //bool AndTileCondition::isAvailable(const G3MRenderContext* rc,
  //                                   const Tile* tile) const {
  //  int size = _children.size();
  //  for (int i = 0; i < size; i++) {
  //    LayerCondition* child = _children[i];
  //    if (!child->isAvailable(rc, tile)) {
  //      return false;
  //    }
  //  }
  //  return true;
  //}
  //
  //bool AndTileCondition::isAvailable(const G3MEventContext* ec,
  //                                   const Tile* tile) const {
  //  int size = _children.size();
  //  for (int i = 0; i < size; i++) {
  //    LayerCondition* child = _children[i];
  //    if (!child->isAvailable(ec, tile)) {
  //      return false;
  //    }
  //  }
  //  return true;
  //}
  
  public final boolean isAvailable(Tile tile)
  {
    int size = _children.size();
    for (int i = 0; i < size; i++)
    {
      LayerCondition child = _children.get(i);
      if (!child.isAvailable(tile))
      {
        return false;
      }
    }
    return true;
  }

  public final LayerCondition copy()
  {
    return new AndTileCondition(_children);
  }

}