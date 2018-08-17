package org.glob3.mobile.generated;import java.util.*;

public class Matrix44DMultiplicationHolderBuilder
{
  private final java.util.ArrayList<Matrix44DProvider> _providers = new java.util.ArrayList<Matrix44DProvider>();

  public void dispose()
  {
	final int providersSize = _providers.size();
	for (int i = 0; i < providersSize; i++)
	{
	  _providers.get(i)._release();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _providers.size();
  }

  public final void add(Matrix44DProvider provider)
  {
	_providers.add(provider);
	provider._retain();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Matrix44DMultiplicationHolder* create() const
  public final Matrix44DMultiplicationHolder create()
  {
	final int providersSize = _providers.size();
	Matrix44DProvider[] ps = new Matrix44DProvider[providersSize];
	for (int i = 0; i < providersSize; i++)
	{
	  ps[i] = _providers.get(i);
	}

	return new Matrix44DMultiplicationHolder(ps, providersSize);
  }

}
