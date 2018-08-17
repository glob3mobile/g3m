package org.glob3.mobile.generated;import java.util.*;

//
//  GPUAttribute.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  Attribute.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//







//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttribute;

public abstract class GPUAttributeValue extends RCObject
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final boolean _enabled;
  public final int _type;
  public final int _attributeSize;
  public final int _index;
  public final int _stride;
  public final boolean _normalized;
  public final int _arrayElementSize;

  public GPUAttributeValue(boolean enabled)
  {
	  _enabled = enabled;
	  _type = 0;
	  _attributeSize = 0;
	  _index = 0;
	  _stride = 0;
	  _normalized = false;
	  _arrayElementSize = 0;
  }

  public GPUAttributeValue(int type, int attributeSize, int arrayElementSize, int index, int stride, boolean normalized)
  {
	  _enabled = true;
	  _type = type;
	  _attributeSize = attributeSize;
	  _index = index;
	  _stride = stride;
	  _normalized = normalized;
	  _arrayElementSize = arrayElementSize;
  }

  //  int getType() const { return _type;}
  //  int getAttributeSize() const { return _attributeSize;}
  //  int getIndex() const { return _index;}
  //  int getStride() const { return _stride;}
  //  bool getNormalized() const { return _normalized;}
  //  bool getEnabled() const { return _enabled;}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setAttribute(GL* gl, const int id) const = 0;
  public abstract void setAttribute(GL gl, int id);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEquals(const GPUAttributeValue* v) const = 0;
  public abstract boolean isEquals(GPUAttributeValue v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String description() const = 0;
  public abstract String description();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
