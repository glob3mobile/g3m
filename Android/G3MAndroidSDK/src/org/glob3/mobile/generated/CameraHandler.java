package org.glob3.mobile.generated; 
public abstract class CameraHandler extends Renderer
{

  protected final Planet _planet;
  protected ILogger _logger;
  protected static IGL gl;

  protected static Camera _camera0 ; //Initial Camera saved on Down event
  protected static Camera _camera = null; // Camera used at current frame

  protected MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  protected MutableVector3D _initialPixel = new MutableVector3D(); //Initial pixel at start of gesture

  protected static Gesture _currentGesture = Gesture.None; //Gesture the user is making at the moment

  protected double _initialFingerSeparation;
  protected double _initialFingerInclination;



  public CameraHandler()
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
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
Camera CameraHandler._camera0(null, 0, 0);



