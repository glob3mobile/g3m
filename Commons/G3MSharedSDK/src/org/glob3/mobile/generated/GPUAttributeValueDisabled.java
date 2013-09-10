package org.glob3.mobile.generated; 
///////////

public class GPUAttributeValueDisabled extends GPUAttributeValue
{
  public GPUAttributeValueDisabled()
  {
     super(false);
  }

  public final void setAttribute(GL gl, int id)
  {
  }

  public final boolean isEquals(GPUAttributeValue v)
  {
    return (v.getEnabled() == false);
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
    if (oldAtt.getEnabled())
    {
      if (oldAtt != null)
         oldAtt.dispose();
      return new GPUAttributeValueDisabled();
    }
    return oldAtt;

  }

}