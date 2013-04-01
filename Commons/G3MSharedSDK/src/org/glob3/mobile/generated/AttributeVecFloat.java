package org.glob3.mobile.generated; 
public class AttributeVecFloat extends Attribute
{
  protected IFloatBuffer _buffer;
  protected int _timeStamp;
  protected int _index;
  protected int _stride;
  protected boolean _normalized;

  protected int _size;


  protected final boolean equalsTo(IFloatBuffer buffer, int index, int stride, boolean normalized)
  {
    return !(_buffer != buffer || _index != index || _stride != stride || _normalized != normalized || _timeStamp != _buffer.timestamp());
  }

  protected final void save(IFloatBuffer buffer, int index, int stride, boolean normalized)
  {
    _buffer = buffer;
    _index = index;
    _stride = stride;
    _normalized = normalized;
    _timeStamp = _buffer.timestamp();
  }

  public AttributeVecFloat(String name, int id, int size)
  {
     super(name, id);
     _buffer = null;
     _timeStamp = -1;
     _index = -1;
     _stride = -1;
     _normalized = false;
     _size = size;
  }


  public final void set(INativeGL _nativeGL, IFloatBuffer buffer, int index, int stride, boolean normalized)
  {

    if (equalsTo(buffer, index, stride, normalized))
    {
      _nativeGL.vertexAttribPointer(index, _size, normalized, stride, buffer); //Stride - Normalized - Size - Index

      save(buffer, index, stride, normalized);
    }
  }

}