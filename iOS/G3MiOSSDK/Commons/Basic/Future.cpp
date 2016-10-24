//
//  Future.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/24/16.
//
//

#include "Future.hpp"

#include "ErrorHandling.hpp"


template <class T>
Future<T>::Future() :
_value(NULL),
_isResolved(false),
_isCanceled(false)
{
}

template <class T>
Future<T>::Future(T* value) :
_value(value),
_isResolved(true),
_isCanceled(true)
{
}

template <class T>
T* Future<T>::get() const
{
  if (_isResolved) {
    return _value;
  }
  if (_isCanceled) {
    THROW_EXCEPTION("The future was canceled");
  }
  THROW_EXCEPTION("Can't get the future's value before it gets resolved");
}

template <class T>
bool Future<T>::isResolved() const {
  return _isResolved;
}

template <class T>
bool Future<T>::isCanceled() const {
  return _isCanceled;
}
