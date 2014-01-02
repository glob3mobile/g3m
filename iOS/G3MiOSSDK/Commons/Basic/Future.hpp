//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/31/13.
//
//

#ifndef __G3MiOSSDK__Future__
#define __G3MiOSSDK__Future__

#include <stddef.h>
#include <string>

#include "ErrorHandling.hpp"

template <class T>
class Future {
private:
  T*   _value;
  bool _isDone;

  bool        _isDoneWithError;
  std::string _error;

  Future(const Future<T>& that);

protected:
  Future() :
  _value(NULL),
  _isDone(false),
  _isDoneWithError(false)
  {
  }

  void done(T* value) {
    _value  = value;
    _isDone = true;
    _isDoneWithError = false;
  }

  void error(const std::string& error) {
    _error = error;
    _isDone = true;
    _isDoneWithError = true;
  }

public:
  ~Future() {
    delete _value;
  }

  T* get() const {
    return _value;
  }

  bool isDone() const {
    return _isDone;
  }

  bool isDoneWithError() const {
    return _isDoneWithError;
  }

  const std::string getError() const {
    return _error;
  }
  
};


class IImage;
class FutureImage : public Future<IImage> {

};

#endif
