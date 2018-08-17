package org.glob3.mobile.generated;//
//  FrameTask.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

//
//  FrameTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;

public abstract class FrameTask
{

	public final boolean _repeatUntilCancellation;

	public FrameTask()
	{
		this(false);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: FrameTask(boolean repeatUntilCancellation = false): _repeatUntilCancellation(repeatUntilCancellation)
	public FrameTask(boolean repeatUntilCancellation)
	{
		_repeatUntilCancellation = repeatUntilCancellation;
	}

  public void dispose()
  {

  }

  public abstract boolean isCanceled(G3MRenderContext rc);

  public abstract void execute(G3MRenderContext rc);

}
