package org.glob3.mobile.generated;public class GPUProgramFactory
{
  private java.util.ArrayList<GPUProgramSources> _sources = new java.util.ArrayList<GPUProgramSources>();


  public GPUProgramFactory()
  {
  }

  public final void add(GPUProgramSources ps)
  {
	_sources.add(ps);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GPUProgramSources* get(const String& name) const
  public final GPUProgramSources get(String name)
  {
	final int size = _sources.size();
	for (int i = 0; i < size; i++)
	{
	  if (_sources.get(i)._name.compareTo(name) == 0)
	  {
		return (_sources.get(i));
	  }
	}
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _sources.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUProgramSources get(int i) const
  public final GPUProgramSources get(int i)
  {
	return _sources.get(i);
  }
}
