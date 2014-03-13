package org.glob3.mobile.generated; 
//
//  ShortBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//

//
//  ShortBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//



//class IShortBuffer;

public class ShortBufferBuilder
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ShortBufferBuilder(ShortBufferBuilder that);


  public final class ShortArrayList {
    private short[] _array;
    private int     _size;

    public ShortArrayList() {
      this(1024);
    }

    public ShortArrayList(final int initialCapacity) {
      if (initialCapacity < 0) {
        throw new IllegalArgumentException("Capacity can't be negative: " + initialCapacity);
      }
      _array = new short[initialCapacity];
      _size = 0;
    }

    public int size() {
      return _size;
    }

    public short get(final int index) {
      return _array[index];
    }

    public void push_back(final short element) {
      ensureCapacity(_size + 1);
      _array[_size++] = element;
    }

    private void ensureCapacity(final int mincap) {
      if (mincap > _array.length) {
        final int newcap = ((_array.length * 3) >> 1) + 1;
        final short[] olddata = _array;
        _array = new short[newcap < mincap ? mincap : newcap];
        System.arraycopy(olddata, 0, _array, 0, _size);
      }
    }

    public short[] toArray() {
      final short[] result = new short[_size];
      System.arraycopy(_array, 0, result, 0, _size);
      return result;
    }

  }

  protected final ShortArrayList _values = new ShortArrayList();

  public ShortBufferBuilder()
  {
  }

  public final void add(short value)
  {
    _values.push_back(value);
  }

  public final IShortBuffer create()
  {
    //return IFactory.instance().createShortBuffer( _values.toArray() );
    return IFactory.instance().createShortBuffer( _values._array, _values._size );
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("ShortBufferBuilder: ");
    for (int i = 0; i < (int)_values.size(); i++)
    {

      short v = _values.get(i);
      isb.addInt(v);
      isb.addString(", ");
    }
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}