package org.glob3.mobile.generated; 
//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class HUDErrorRenderer extends HUDImageRenderer, ErrorRenderer
{

  public HUDErrorRenderer()
  {
     super(new HUDErrorRenderer_ImageFactory());

  }

  public void dispose()
  {
    super.dispose();
  }

  public final void setErrors(java.util.ArrayList<String> errors)
  {
    ((HUDErrorRenderer_ImageFactory) getImageFactory()).setErrors(errors);
  
    recreateImage();
  }

}