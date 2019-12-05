//
//  Matrix44DProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

#include "Matrix44DProvider.hpp"
#include "ILogger.hpp"
#include "MutableMatrix44D.hpp"

Matrix44DMultiplicationHolder::Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[],
                                                             size_t matricesSize):
_matricesSize(matricesSize),
_result(NULL)
{
#ifdef C_CODE
    _providers = new const Matrix44DProvider*[_matricesSize];
#endif
#ifdef JAVA_CODE
    _providers = new Matrix44DProvider[_matricesSize];
#endif
    for (int i = 0; i < _matricesSize; i++) {
        _providers[i] = providers[i];
        _providers[i]->_retain();
    }
}

Matrix44DMultiplicationHolder::~Matrix44DMultiplicationHolder() {
    for (int j = 0; j < _matricesSize; j++) {
        _providers[j]->_release();
    }
    
#ifdef C_CODE
    delete[] _providers;
#endif
    
    if (_result != NULL) {
        _result->_release();
    }
    
#ifdef JAVA_CODE
    super.dispose();
#endif
}

Matrix44D* Matrix44DMultiplicationHolder::getMatrix() const {
    long long newSumID = 0;
    const Matrix44D* matrices[_matricesSize];
    for (int i = 0; i < _matricesSize; i++) {
        matrices[i] = _providers[i]->getMatrix();
        if (matrices[i] != NULL){
            newSumID += matrices[i]->_id;
        } else{
            ILogger::instance()->logWarning("Provider returned NULL matrix.");
        }
    }
    
    if (newSumID != _sumMatrixIDs || _result == NULL){
        if (_result != NULL){
            _result->_release();
            _result = NULL;
        }
        
        MutableMatrix44D m = MutableMatrix44D::identity();
        for (int i = 0; i < _matricesSize; i++) {
            const Matrix44D* m1 = matrices[i];
            if (m1 != NULL){
                m.copyValueOfMultiplicationWithMatrix44D(m, *m1);
            }
        }
        
        _result = m.asMatrix44D();
        _result->_retain();
        _sumMatrixIDs = newSumID;
    }
    if (_result == NULL){
        THROW_EXCEPTION("Provider returned NULL matrix.");
    }
    return _result;
}
