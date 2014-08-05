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

//#include "GPUProgramFactory.hpp"
#include "IGPUProgram.hpp"
#include "GL.hpp"

class GPUProgramManager {

  std::map<std::string, IGPUProgram*> _programs;

  //GPUProgramFactory *_factory;

  IGPUProgram* getCompiledProgram(const std::string& name);

  IGPUProgram* compileProgramWithName(GL* gl, const std::string& name);

  IGPUProgram* getNewProgram(GL* gl, int uniformsCode, int attributesCode);

  IGPUProgram* getCompiledProgram(int uniformsCode, int attributesCode);
public:

  //GPUProgramManager(GPUProgramFactory *factory):_factory(factory) {}

  ~GPUProgramManager();

  IGPUProgram* getProgram(GL* gl, int uniformsCode, int attributesCode);

  void removeUnused();
};

#endif
