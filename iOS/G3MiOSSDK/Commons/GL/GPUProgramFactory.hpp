//
//  GPUProgramFactory.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#ifndef __G3MiOSSDK__GPUProgramFactory__
#define __G3MiOSSDK__GPUProgramFactory__

#include <iostream>

#include <map>
#include <string>

class GPUProgramSources{
public:
  std::string _name;
  std::string _vertexSource;
  std::string _fragmentSource;
  
  GPUProgramSources(){}
  
  GPUProgramSources(const std::string& n, const std::string& v, const std::string& f):
  _name(n),_vertexSource(v),_fragmentSource(f){}
  
  GPUProgramSources(const GPUProgramSources& g):
  _name(g._name), _vertexSource(g._vertexSource), _fragmentSource(g._fragmentSource){}
};


class GPUProgramFactory{
  
  std::map<std::string, GPUProgramSources> _sources;
public:
  
  GPUProgramFactory(){}
  
  void add(const GPUProgramSources& ps){
    _sources[ps._name] = ps;
  }
  
  const GPUProgramSources* get(const std::string& name) const{
#ifdef C_CODE
    std::map<std::string, GPUProgramSources>::const_iterator it = _sources.find(name);
    if (it != _sources.end()){
      return &it->second;
    } else{
      return NULL;
    }
#endif
#ifdef JAVA_CODE
    return _sources.get(name);
#endif
  }


};

#endif /* defined(__G3MiOSSDK__GPUProgramFactory__) */
