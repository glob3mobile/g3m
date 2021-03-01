package org.glob3.mobile.generated;
//
//  ChildRenderer.cpp
//  G3M
//
//  Created by Vidal Toboso on 22/04/14.
//
//

//
//  ChildRenderer.hpp
//  G3M
//
//  Created by Vidal Toboso on 22/04/14.
//
//



public class ChildRenderer
{
  private Renderer _renderer;
  private final java.util.ArrayList<Info> _info = new java.util.ArrayList<Info>();


  public ChildRenderer(Renderer renderer)
  {
     _renderer = renderer;

  }

  public ChildRenderer(Renderer renderer, java.util.ArrayList<Info> info)
  {
     _renderer = renderer;
    setInfo(info);
  }

  public void dispose()
  {
    if (_renderer != null)
       _renderer.dispose();
    _info.clear();
  }

  public final void addInfo(Info inf)
  {
    _info.add(inf);
  }

  public final void setInfo(java.util.ArrayList<Info> info)
  {
    _info.clear();
    _info.addAll(info);
  }

  public final Renderer getRenderer()
  {
    return _renderer;
  }

  public final java.util.ArrayList<Info> getInfo()
  {
    return _info;
  }

}