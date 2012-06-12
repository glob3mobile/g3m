package org.glob3.mobile.generated; 
//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class MarksRenderer extends Renderer
{
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();


  public void initialize(InitializationContext ic)
  {
  
  }

  public int render(RenderContext rc)
  {
  //  rc.getLogger()->logInfo("MarksRenderer::render()");
  
  //  IGL* gl = rc->getGL();
  
	int __dgd_at_work;
  
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
	}
  
	return 9999;
  }

  public boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public void dispose()
  {
	int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  if (_marks.get(i) != null)
		  _marks.get(i).dispose();
	}
  }

  public final void addMark(Mark mark)
  {
	_marks.add(mark);
  }

  public final boolean onResizeViewportEvent(int width, int height)
  {
	  return false;
  }

}