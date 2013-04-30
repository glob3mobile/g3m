package org.glob3.mobile.generated; 
public class FloatBufferBuilder
{
  protected java.util.ArrayList<Float> _values = new java.util.ArrayList<Float>();

  public final IFloatBuffer create()
  {
    final int size = _values.size();
  
    IFloatBuffer result = IFactory.instance().createFloatBuffer(size);
  
    for (int i = 0; i < size; i++)
    {
      result.rawPut(i, _values.get(i));
    }
  
    return result;
  }
}