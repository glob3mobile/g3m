package org.glob3.mobile.generated;import java.util.*;

public class GPUAttributeValueVecFloat extends GPUAttributeValue
{
  private final IFloatBuffer _buffer;
  private final int _timestamp;
  private long _id;

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public GPUAttributeValueVecFloat(IFloatBuffer buffer, int attributeSize, int arrayElementSize, int index, int stride, boolean normalized)
  {
	  super(GLType.glFloat(), attributeSize, arrayElementSize, index, stride, normalized);
	  _buffer = buffer;
	  _timestamp = buffer.timestamp();
	  _id = buffer.getID();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setAttribute(GL* gl, const int id) const
  public final void setAttribute(GL gl, int id)
  {
	if (_index != 0)
	{
	  //TODO: Change vertexAttribPointer
	  ILogger.instance().logError("INDEX NO 0");
	}

	gl.vertexAttribPointer(id, _arrayElementSize, _normalized, _stride, _buffer);
  }

	public final void replaceBuffer(IFloatBuffer buffer)
	{
		if (_buffer != buffer)
		{
			if (_buffer != null)
				_buffer.dispose();
			_buffer = buffer;
			_id = _buffer.getID();
		}
	}


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUAttributeValue* v) const
  public final boolean isEquals(GPUAttributeValue v)
  {

	if (!v._enabled)
	{
	  return false; //Is a disabled value
	}
	GPUAttributeValueVecFloat vecV = (GPUAttributeValueVecFloat)v;
	boolean equal = ((_id == vecV._buffer.getID()) && (_timestamp == vecV._timestamp) && (_type == v._type) && (_attributeSize == v._attributeSize) && (_stride == v._stride) && (_normalized == v._normalized));

	return equal;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {

	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Attribute Value Float.");
	isb.addString(" ArrayElementSize:");
	isb.addInt(_arrayElementSize);
	isb.addString(" AttributeSize:");
	isb.addInt(_attributeSize);
	isb.addString(" Index:");
	isb.addInt(_index);
	isb.addString(" Stride:");
	isb.addInt(_stride);
	isb.addString(" Normalized:");
	isb.addBool(_normalized);

	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}
