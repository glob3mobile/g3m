package org.glob3.mobile.generated; 
public class GPUProgramFactory
{

  private java.util.HashMap<String, GPUProgramSources> _sources = new java.util.HashMap<String, GPUProgramSources>();

  public GPUProgramFactory()
  {
  }

  public final void add(GPUProgramSources ps)
  {
    _sources.put(ps._name, ps);
  }

  public final GPUProgramSources get(String name)
  {
    java.util.Iterator<String, GPUProgramSources> it = _sources.indexOf(name);
    if (it.hasNext())
    {
      return it.next().getValue();
    }
    else
    {
      return null;
    }
  }


}