//
//  GPUProgramManager.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#ifndef __G3MiOSSDK__GPUProgramManager__
#define __G3MiOSSDK__GPUProgramManager__

#include <iostream>

#include <map>
#include <string>

#include "GPUProgramFactory.hpp"

#include "GPUProgram.hpp"
#include "GL.hpp"
#include "GPUProgramState.hpp"

class GPUProgramManager{
  
  std::map<std::string, GPUProgram*> _programs;
  
  GPUProgramFactory *_factory;
  GL* _gl;
public:
  
  GPUProgramManager(GL* gl, GPUProgramFactory *factory):_gl(gl), _factory(factory){}
  
  GPUProgramManager(){
    delete _factory;
    
    for (std::map<std::string, GPUProgram*>::iterator it = _programs.begin(); it != _programs.end(); ++it){
      delete it->second;
    }
  }
  
  GPUProgram* getProgram(const std::string& name){
    
    std::map<std::string, GPUProgram*>::iterator it = _programs.find(name);
    if (it != _programs.end()){
      return it->second;
    } else{
      const GPUProgramSources* ps = _factory->get(name);
      GPUProgram* prog = NULL;
      
      //Compile new Program
      if (ps != NULL){
        prog = GPUProgram::createProgram(_gl,
                                         ps->_name,
                                         ps->_vertexSource,
                                         ps->_fragmentSource);
        if (prog == NULL){
          ILogger::instance()->logError("Problem at creating program named %s.", name.c_str());
          return NULL;
        }
        
        //_programs[prog->getName()] = prog;
        
        _programs.insert ( std::pair<std::string, GPUProgram*>(prog->getName(),prog) );
      }
      return prog;
    }
  }
  
  GPUProgram* getProgram(const GPUProgramState& state) {
    int WORKING_JM;
    
    std::vector<std::string> us = state.getUniformsNames();
    for (int i = 0; i < us.size(); i++) {
      if (us[i].compare("ScaleTexCoord") == 0){
        return getProgram("DefaultProgram");
      }
    }
    
    return getProgram("Billboard");
  }
  
  
};

#endif /* defined(__G3MiOSSDK__GPUProgramManager__) */
