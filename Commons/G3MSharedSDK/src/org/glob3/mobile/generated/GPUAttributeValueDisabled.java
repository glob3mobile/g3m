package org.glob3.mobile.generated; 
///////////

public class GPUAttributeValueDisabled extends GPUAttributeValue
{
  public void dispose()
  {
    super.dispose();
  }

  public GPUAttributeValueDisabled()
  {
     super(false);
  }

  public final void setAttribute(GL gl, int id)
  {
  }

  public final boolean isEquals(GPUAttributeValue v)
  {
    return (v._enabled == false);
  }

  public final GPUAttributeValue shallowCopy()
  {
    return new GPUAttributeValueDisabled();
  }

  public final String description()
  {
    return "Attribute Disabled.";
  }

  public final GPUAttributeValue copyOrCreate(GPUAttributeValue oldAtt)
  {
    if (oldAtt == null)
    {
      return new GPUAttributeValueDisabled();
    }
    if (oldAtt._enabled)
    {
      oldAtt._release();
      return new GPUAttributeValueDisabled();
    }
    return oldAtt;
  }

}