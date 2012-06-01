package org.glob3.mobile.generated; 
//ORIGINAL LINE: class RenderContext: private Context
//C++ TO JAVA CONVERTER TODO TASK: Java has no concept of 'private' inheritance:
public class RenderContext extends Context
{
  private IGL _gl;

  public RenderContext(IFactory factory, IGL gl)
  {
	  super(factory);
	  _gl = gl;

  }

  public final IGL getGL()
  {
	return _gl;
  }
}