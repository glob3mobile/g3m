//
//  GPUProgramFactory_OGL.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 06/07/14.
//
//

#ifndef __G3MiOSSDK_GPUProgramFactory_OGL__
#define __G3MiOSSDK_GPUProgramFactory_OGL__

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
		const std::string& fragmentSource) :
		_name(name),
		_vertexSource(vertexSource),
		_fragmentSource(fragmentSource)
	{
	}

	GPUProgramSources(const GPUProgramSources& that) :
		_name(that._name),
		_vertexSource(that._vertexSource),
		_fragmentSource(that._fragmentSource)
	{
	}
};

#include "IGPUProgramFactory.hpp"



class GPUProgramFactory_OGL : public IGPUProgramFactory{
private:
	std::vector<GPUProgramSources> _sources;
	void addGPUProgramSources(const GPUProgramSources& s);
	const GPUProgramSources* getSource(const std::string& name) const;
	

public:
	GPUProgramFactory_OGL();
	IGPUProgram* get(GL* gl, const std::string& name);
	
};

#endif