package org.glob3.mobile.generated;//
//  FloatBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  FloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;

public class FloatBufferBuilder
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FloatBufferBuilder(FloatBufferBuilder that);

  protected enum CenterStrategy
  {
	NO_CENTER,
	FIRST_VERTEX,
	GIVEN_CENTER;

	  public int getValue()
	  {
		  return this.ordinal();
	  }

	  public static CenterStrategy forValue(int value)
	  {
		  return values()[value];
	  }
  }


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected java.util.ArrayList<Float> _values = new java.util.ArrayList<Float>();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE

  public final static class FloatArrayList
  {
	private internal[] _float _array;
	public int _size = new internal();

	public FloatArrayList()
	{
	  this(1024);
//      _array = IFactory.instance().getThreadLocalFloatArray();
//      _size = 0;
	}

	public FloatArrayList(final int initialCapacity)
	{
	  if (initialCapacity < 0)
	  {
		throw new IllegalArgumentException("Capacity can't be negative: " + initialCapacity);
	  }
	  _array = new float[initialCapacity];
	  _size = 0;
	}

	public int size()
	{
	  return _size;
	}

	public float get(final int index)
	{
	  return _array[index];
	}

	public void push_back(final float element)
	{
	  ensureCapacity(_size + 1);
	  _array[_size++] = element;
	}

	public void ensureCapacity(final int mincap)
	{
	  if (mincap > _array.length)
	  {
		final int newcap = ((_array.length * 3) >> 1) + 1;
		final[] float olddata = _array;
		_array = new float[newcap < mincap != null ? mincap : newcap];
		//IFactory.instance().setThreadLocalFloatArray(_array);
		System.arraycopy(olddata, 0, _array, 0, _size);
	  }
	}

	public float[] toArray()
	{
	  final[] float result = new float[_size];
	  System.arraycopy(_array, 0, result, 0, _size);
	  return result;
	}

  }

  protected FloatArrayList _protected final FloatArrayList _values = new FloatArrayList();
//#endif

  protected FloatBufferBuilder()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* create() const
  public final IFloatBuffer create()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int size = _values.size();
  
	IFloatBuffer result = IFactory.instance().createFloatBuffer(size);
  
	for (int i = 0; i < size; i++)
	{
	  result.rawPut(i, _values.get(i));
	}
  
  //  ILogger::instance()->logInfo("Created FloatBuffer with %d floats", result->size());
	return result;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	//return IFactory.instance().createFloatBuffer( _values.toArray() );
	return IFactory.instance().createFloatBuffer(_values._array, _values._size);
//#endif
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _values.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getVector2D(int i) const
  public final Vector2D getVector2D(int i)
  {
	int pos = i * 2;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	return new Vector2D(_values.get(pos), _values.get(pos + 1));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return new Vector2D(_values.get(pos), _values.get(pos + 1));
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getVector3D(int i) const
  public final Vector3D getVector3D(int i)
  {
	int pos = i * 3;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	return new Vector3D(_values.get(pos), _values.get(pos + 1), _values.get(pos+2));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return new Vector3D(_values.get(pos), _values.get(pos + 1), _values.get(pos + 2));
//#endif
  }
}
