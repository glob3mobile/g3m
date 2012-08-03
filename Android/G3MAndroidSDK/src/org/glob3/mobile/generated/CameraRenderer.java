package org.glob3.mobile.generated; 
public abstract class CameraRenderer extends Renderer
{

  //static const Planet* _planet;
  //static const ILogger * _logger;
  //static IGL *gl;

  //static Camera _camera0;                //Initial Camera saved on Down event
  //static Camera* _camera;         // Camera used at current frame

  protected MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  protected MutableVector3D _initialPixel = new MutableVector3D(); //Initial pixel at start of gesture

  protected static Gesture _currentGesture = Gesture.None; //Gesture the user is making at the moment

  protected double _initialFingerSeparation;
  protected double _initialFingerInclination;



  public CameraRenderer()
  {
	  _initialPoint = new MutableVector3D(0,0,0);
	  _initialPixel = new MutableVector3D(0,0,0);
  }

  public abstract void initialize(InitializationContext ic);

  public abstract int render(RenderContext rc);

  public abstract boolean onTouchEvent(TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(int width, int height);

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }


}