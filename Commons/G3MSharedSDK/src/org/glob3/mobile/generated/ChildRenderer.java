package org.glob3.mobile.generated; 
//
//  ChildRenderer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 22/04/14.
//
//

//
//  ChildRenderer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 22/04/14.
//
//



public class ChildRenderer
{
  private Renderer _renderer;
  private java.util.ArrayList<String> _info = new java.util.ArrayList<String>();


  public ChildRenderer(Renderer renderer)
  {
     _renderer = renderer;

  }

  public ChildRenderer(Renderer renderer, java.util.ArrayList<String> info)
  {
     _renderer = renderer;
     _info = info;

  }

  public void dispose()
  {
    _renderer = null;
    _info.clear();
  }

  public final void addInfo(String inf)
  {
    _info.add(inf);
  }

  public final void setInfo(java.util.ArrayList<String> info)
  {
    _info.clear();
    _info.addAll(info);
  }

  public final Renderer getRenderer()
  {
    return _renderer;
  }

  public final java.util.ArrayList<String> getInfo()
  {
    return _info;
  }

}