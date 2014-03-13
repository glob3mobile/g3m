//
//  GPUProgramFactory.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#ifndef __G3MiOSSDK__GPUProgramFactory__
#define __G3MiOSSDK__GPUProgramFactory__

#include <vector>
#include <string>

class GPUProgramSources {
public:
  std::string _name;
  std::string _vertexSource;
  std::string _fragmentSource;

  GPUProgramSources() {}

  GPUProgramSources(const std::string& name,
                    const std::string& vertexSource,
                    const std::string& fragmentSource):
  _name(name),
  _vertexSource(vertexSource),
  _fragmentSource(fragmentSource)
  {
  }

  GPUProgramSources(const GPUProgramSources& that):
  _name(that._name),
  _vertexSource(that._vertexSource),
  _fragmentSource(that._fragmentSource)
  {
  }
};


class GPUProgramFactory {
private:
  //std::map<std::string, GPUProgramSources> _sources;
  std::vector<GPUProgramSources> _sources;

public:

  GPUProgramFactory() {}

  void add(const GPUProgramSources& ps) {
    //_sources[ps._name] = ps;
    _sources.push_back(ps);
  }

  const GPUProgramSources* get(const std::string& name) const{
    //#ifdef C_CODE
    //    std::map<std::string, GPUProgramSources>::const_iterator it = _sources.find(name);
    //    if (it != _sources.end()) {
    //      return &it->second;
    //    } else{
    //      return NULL;
    //    }
    //#endif
    //#ifdef JAVA_CODE
    //    return _sources.get(name);
    //#endif
    const int size = _sources.size();
    for (int i = 0; i < size; i++) {
      if (_sources[i]._name.compare(name) == 0) {
        return &(_sources[i]);
      }
    }
    return NULL;
  }

  int size() const{
    return _sources.size();
  }

  GPUProgramSources get(int i) const{
    return _sources[i];
  }
  
  
  
};

#endif
