package org.glob3.mobile.generated;//
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
  private final java.util.ArrayList<Info> _info = new java.util.ArrayList<Info>();


  public ChildRenderer(Renderer renderer)
  {
	  _renderer = renderer;

  }

  public ChildRenderer(Renderer renderer, java.util.ArrayList<const Info> info)
  {
	  _renderer = renderer;
	setInfo(info);
  }

  public void dispose()
  {
	_renderer = null;
	_info.clear();
  }

  public final void addInfo(Info inf)
  {
	_info.add(inf);
  }

  public final void setInfo(java.util.ArrayList<const Info> info)
  {
	_info.clear();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_info.insert(_info.end(), info.iterator(), info.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_info.addAll(info);
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Renderer* getRenderer() const
  public final Renderer getRenderer()
  {
	return _renderer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<const Info*> getInfo() const
  public final java.util.ArrayList<Info> getInfo()
  {
	return _info;
  }

}
