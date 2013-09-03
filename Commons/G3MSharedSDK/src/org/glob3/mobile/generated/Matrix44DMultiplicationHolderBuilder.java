package org.glob3.mobile.generated; 
public class Matrix44DMultiplicationHolderBuilder
{

  private final java.util.ArrayList<Matrix44DProvider> _providers = new java.util.ArrayList<Matrix44DProvider>();

  public void dispose()
  {
    final int size = _providers.size();
    for (int i = 0; i < size; i++)
    {
      _providers.get(i)._release();
    }
  }

  public final int size()
  {
    return _providers.size();
  }

  public final void add(Matrix44DProvider provider)
  {
    _providers.add(provider);
    provider._retain();
  }

  public final Matrix44DMultiplicationHolder create()
  {
    Matrix44DProvider[] ps = new Matrix44DProvider[_providers.size()];
    final int size = _providers.size();
    for (int i = 0; i < size; i++)
    {
      ps[i] = _providers.get(i);
    }

    return new Matrix44DMultiplicationHolder(ps, size);
  }

}