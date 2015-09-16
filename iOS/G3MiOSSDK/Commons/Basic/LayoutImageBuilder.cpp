//
//  LayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "LayoutImageBuilder.hpp"

#include "ErrorHandling.hpp"

LayoutImageBuilder::~LayoutImageBuilder() {
  const size_t childrenSize = _children.size();
  for (size_t i = 0; i < childrenSize; i++) {
    IImageBuilder* child = _children[i];
    delete child;
  }
}

bool LayoutImageBuilder::isMutable() const {
  //TODO: #warning TODO: make mutable if any children is
  return false;
}

void LayoutImageBuilder::build(const G3MContext* context,
                               IImageBuilderListener* listener,
                               bool deleteListener) {
  const size_t childrenSize = _children.size();
  ChildrenResult* childrenResult = new ChildrenResult(this,
                                                      childrenSize,
                                                      context,
                                                      listener,
                                                      deleteListener);
  for (int i = 0; i < childrenSize; i++) {
    IImageBuilder* child = _children[i];

    child->build(context,
                 new LayoutImageBuilderChildListener(childrenResult, i),
                 true);
  }

  childrenResult->_release();
}

void LayoutImageBuilder::ChildrenResult::childImageCreated(const IImage*      image,
                                                           const std::string& imageName,
                                                           size_t             childIndex) {
  if (_childrenResult[childIndex] != NULL) {
    THROW_EXCEPTION("Logic error");
  }

  _childrenResult[childIndex] = new ChildResult(image, imageName);
  if (--_childrenResultPendingCounter == 0) {
    _layoutImageBuilder->doLayout(_context,
                                  _listener,
                                  _deleteListener,
                                  _childrenResult);
  }
}

void LayoutImageBuilder::ChildrenResult::childError(const std::string& error,
                                                    size_t             childIndex) {
  if (_childrenResult[childIndex] != NULL) {
    THROW_EXCEPTION("Logic error");
  }

  _childrenResult[childIndex] = new ChildResult(error);
  if (--_childrenResultPendingCounter == 0) {
    _layoutImageBuilder->doLayout(_context,
                                  _listener,
                                  _deleteListener,
                                  _childrenResult);
  }
}
