//
//  GPUProgramFactory_OGL.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 06/07/14.
//
//

#include "GPUProgramFactory_OGL.hpp"
#include "BasicShadersGL2.hpp"
#include "GL.hpp"


GPUProgramFactory_OGL::GPUProgramFactory_OGL(){
	BasicShadersGL2 basicShaders;
	for (int i = 0; i < basicShaders.size(); i++) {
		addGPUProgramSources(basicShaders.get(i));
	}
}

const GPUProgram* GPUProgramFactory_OGL::get(GL* gl, const std::string& name) const{
	//Get the source code for the shader
	const GPUProgramSources* ps = getSource(name);
	GPUProgram* prog;
	//Compile if the sources exist
	if (ps != NULL) {
		prog = GPUProgram::createProgram(gl, ps->_name, ps->_vertexSource, ps->_fragmentSource);

		if (prog == NULL) {
			ILogger::instance()->logError("Problem at creating program named %s.", name.c_str());
			return NULL;
		}
	}
	else{
		ILogger::instance()->logError("No shader sources for program named %s.", name.c_str());
	}
}


void GPUProgramFactory_OGL::addGPUProgramSources(const GPUProgramSources& s) {
	_sources.push_back(s);
}

const GPUProgramSources* GPUProgramFactory_OGL::getSource(const std::string& name) const{
	const int size = _sources.size();
	for (int i = 0; i < size; i++) {
		if (_sources[i]._name.compare(name) == 0) {
			return &(_sources[i]);
		}
	}
	return NULL;
}