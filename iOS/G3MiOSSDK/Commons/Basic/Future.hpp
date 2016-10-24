//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/24/16.
//
//

#ifndef Future_hpp
#define Future_hpp

template <class T>
class Future {
private:
  T*   _value;
  bool _isResolved;
  bool _isCanceled;

public:
  Future();

  Future(T* value);

  T* get() const;

  bool isResolved() const;
  bool isCanceled() const;

};

#endif
