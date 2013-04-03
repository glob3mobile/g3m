//
//  G3MError.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#ifndef G3MiOSSDK_G3MError_hpp
#define G3MiOSSDK_G3MError_hpp

#include <string>

#ifdef C_CODE
class G3MError{
  
private:
  std::string _message;
public:

  G3MError(const std::string& message): _message(message){}
  std::string getMessage() const { return _message;}
  
};
#endif
#ifdef JAVA_CODE
class G3MError extends java.lang.RuntimeException{
	private static final long serialVersionUID = 1L;
}
#endif



#endif
