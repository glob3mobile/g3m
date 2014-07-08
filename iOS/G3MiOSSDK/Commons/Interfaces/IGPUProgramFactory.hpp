//
//  IGPUProgramFactory.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/07/14.
//
//

#ifndef __G3MiOSSDK_IGPUProgramFactory__
#define __G3MiOSSDK_IGPUProgramFactory__

#include <string>
#include "ILogger.hpp"

class GPUProgram;
class GL;

class IGPUProgramFactory{
protected:
	static IGPUProgramFactory* _instance;

public:
	static void setInstance(IGPUProgramFactory* factory) {
		if (_instance != NULL) {
			ILogger::instance()->logWarning("IGPUProgramFactory instance already set!");
			delete _instance;
		}
		_instance = factory;
	}

	static IGPUProgramFactory* instance() {
		return _instance;
	}

	virtual GPUProgram* get(GL* gl, const std::string& name) const = 0;

	virtual ~IGPUProgramFactory(){};

};
#endif
