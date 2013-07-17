package org.glob3.mobile.generated; 
public class FloatBufferBuilder
{
  public final class FloatArrayList {
    private float[] _array;
    private int     _size;
    
    public FloatArrayList() {
      this(10);
    }

    public FloatArrayList(final int initialCapacity) {
      if (initialCapacity < 0) {
        throw new IllegalArgumentException("Capacity can't be negative: " + initialCapacity);
      }
      _array = new float[initialCapacity];
      _size = 0;
    }

    public int size() {
      return _size;
    }

    public float at(final int index) {
      return _array[index];
    }

    public void push_back(final float element) {
      ensureCapacity(_size + 1);
      _array[_size++] = element;
    }

    public void ensureCapacity(final int mincap) {
      if (mincap > _array.length) {
        final int newcap = ((_array.length * 3) >> 1) + 1;
        final float[] olddata = _array;
        _array = new float[newcap < mincap ? mincap : newcap];
        System.arraycopy(olddata, 0, _array, 0, _size);
      }
    }

  }

  protected final FloatArrayList _values = new FloatArrayList();

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