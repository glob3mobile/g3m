package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IShortBuffer;

public class ShortBufferBuilder
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ShortBufferBuilder(ShortBufferBuilder that);

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private java.util.ArrayList<Short> _values = new java.util.ArrayList<Short>();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE

  public final static class ShortArrayList
  {
	private internal[] _short _array;
	public int _size = new internal();

	public ShortArrayList()
	{
	  this(1024);
	}

	public ShortArrayList(final int initialCapacity)
	{
	  if (initialCapacity < 0)
	  {
		throw new IllegalArgumentException("Capacity can't be negative: " + initialCapacity);
	  }
	  _array = new short[initialCapacity];
	  _size = 0;
	}

	public int size()
	{
	  return _size;
	}

	public short get(final int index)
	{
	  return _array[index];
	}

	public void push_back(final short element)
	{
	  ensureCapacity(_size + 1);
	  _array[_size++] = element;
	}

	public void ensureCapacity(final int mincap)
	{
	  if (mincap > _array.length)
	  {
		final int newcap = ((_array.length * 3) >> 1) + 1;
		final[] short olddata = _array;
		_array = new short[newcap < mincap != null ? mincap : newcap];
		System.arraycopy(olddata, 0, _array, 0, _size);
	  }
	}

	public short[] toArray()
	{
	  final[] short result = new short[_size];
	  System.arraycopy(_array, 0, result, 0, _size);
	  return result;
	}

  }

  private ShortArrayList _protected final ShortArrayList _values = new ShortArrayList();
//#endif

  public ShortBufferBuilder()
  {
  }

  public final void add(short value)
  {
	_values.add(value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IShortBuffer* create() const
  public final IShortBuffer create()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int size = _values.size();
  
	IShortBuffer result = IFactory.instance().createShortBuffer(size);
  
	for (int i = 0; i < size; i++)
	{
	  result.rawPut(i, _values.get(i));
	}
  
	return result;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	//return IFactory.instance().createShortBuffer( _values.toArray() );
	return IFactory.instance().createShortBuffer(_values._array, _values._size);
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("ShortBufferBuilder: ");
	for (int i = 0; i < (int)_values.size(); i++)
	{

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  short v = _values.get(i);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  short v = _values.get(i);
//#endif
	  isb.addInt(v);
	  isb.addString(", ");
	}
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
