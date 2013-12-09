package org.glob3.mobile.generated; 
public class GPUProgramFactory
{
  //std::map<std::string, GPUProgramSources> _sources;
  private java.util.ArrayList<GPUProgramSources> _sources = new java.util.ArrayList<GPUProgramSources>();


  public GPUProgramFactory()
  {
  }

  public final void add(GPUProgramSources ps)
  {
    //_sources[ps._name] = ps;
    _sources.add(ps);
  }

  public final GPUProgramSources get(String name)
  {
    ///#ifdef C_CODE
    //    std::map<std::string, GPUProgramSources>::const_iterator it = _sources.find(name);
    //    if (it != _sources.end()) {
    //      return &it->second;
    //    } else{
    //      return NULL;
    //    }
    ///#endif
    ///#ifdef JAVA_CODE
    //    return _sources.get(name);
    ///#endif
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

  public final int size()
  {
    return _sources.size();
  }

  public final GPUProgramSources get(int i)
  {
    return _sources.get(i);
  }



}