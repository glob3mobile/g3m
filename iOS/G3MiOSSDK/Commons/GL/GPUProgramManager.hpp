//
//  GPUProgramManager.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#ifndef __G3MiOSSDK__GPUProgramManager__
#define __G3MiOSSDK__GPUProgramManager__

#include <map>
#include <string>

#include "GPUProgramFactory.hpp"
#include "GPUProgram.hpp"
#include "GL.hpp"

class GPUProgramManager {

  struct GPUProgramData{
    GPUProgram* _program;
    bool _usedSinceLastCleanUp;
  };
  
  std::map<std::string, GPUProgramData> _programs;
  
  GPUProgramFactory *_factory;

  GPUProgram* getCompiledProgram(const std::string& name);

  GPUProgram* compileProgramWithName(GL* gl, const std::string& name);

  GPUProgram* getNewProgram(GL* gl, int uniformsCode, int attributesCode);

  GPUProgram* getCompiledProgram(int uniformsCode, int attributesCode);
public:
  
  GPUProgramManager(GPUProgramFactory *factory):_factory(factory) {}
  
  ~GPUProgramManager();

  GPUProgram* getProgram(GL* gl, int uniformsCode, int attributesCode);

  //Remove all shaders that have not been used since last call
  void deleteUnusedPrograms();
};

#endif
