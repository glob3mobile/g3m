package org.glob3.mobile.generated; 
///////////

public class GPUAttributeValueVecFloat extends GPUAttributeValue
{
  private IFloatBuffer _buffer;
  private int _timeStamp;
  public GPUAttributeValueVecFloat(IFloatBuffer buffer, int attributeSize, int arrayElementSize, int index, int stride, boolean normalized)
  {
     _buffer = buffer;
     _timeStamp = buffer.timestamp();
     super(GLType.glFloat(), attributeSize, arrayElementSize, index, stride, normalized);
  }

  public final void setAttribute(GL gl, int id)
  {
    if (_index != 0)
    {
      //TODO: Change vertexAttribPointer
      ILogger.instance().logError("INDEX NO 0");
    }

    gl.vertexAttribPointer(id, _arrayElementSize, _normalized, _stride, _buffer);
  }

  public final boolean isEqualsTo(GPUAttributeValue v)
  {
    return (_buffer == ((GPUAttributeValueVecFloat)v)._buffer) && (_timeStamp == ((GPUAttributeValueVecFloat)v)._timeStamp) && (_type == v.getType()) && (_attributeSize == v.getAttributeSize()) && (_stride == v.getStride()) && (_normalized == v.getNormalized());
  }

  public final GPUAttributeValue shallowCopy()
  {
    GPUAttributeValueVecFloat v = new GPUAttributeValueVecFloat(_buffer, _attributeSize, _arrayElementSize, _index, _stride, _normalized);
    v._timeStamp = _timeStamp;
    return v;
  }

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