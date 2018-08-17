package org.glob3.mobile.generated;import java.util.*;

///////////

public class GPUAttributeValueDisabled extends GPUAttributeValue
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public GPUAttributeValueDisabled()
  {
	  super(false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setAttribute(GL* gl, const int id) const
  public final void setAttribute(GL gl, int id)
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUAttributeValue* v) const
  public final boolean isEquals(GPUAttributeValue v)
  {
	return (v._enabled == false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUAttributeValue* shallowCopy() const
  public final GPUAttributeValue shallowCopy()
  {
	return new GPUAttributeValueDisabled();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	return "Attribute Disabled.";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUAttributeValue* copyOrCreate(GPUAttributeValue* oldAtt) const
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
