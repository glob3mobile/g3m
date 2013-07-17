package org.glob3.mobile.generated; 
public class FloatArrayList {

}

public class FloatBufferBuilder
{
  protected final FloatArrayList _values = new FloatArrayList();

  public final IFloatBuffer create()
  {
    final int size = _values.size();
  
    IFloatBuffer result = IFactory.instance().createFloatBuffer(size);
  
    for (int i = 0; i < size; i++)
    {
      result.rawPut(i, _values[i]);
    }
  
    return result;
  }
}