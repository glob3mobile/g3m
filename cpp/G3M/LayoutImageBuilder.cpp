//
//  LayoutImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "LayoutImageBuilder.hpp"

#include "ErrorHandling.hpp"

#include "ImageBackground.hpp"
#include "NullImageBackground.hpp"

LayoutImageBuilder::LayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                                       const ImageBackground*             background) :
_children(children),
_background((background == NULL) ? new NullImageBackground() : background)
{
}

LayoutImageBuilder::LayoutImageBuilder(IImageBuilder*         child0,
                                       IImageBuilder*         child1,
                                       const ImageBackground* background) :
_background((background == NULL) ? new NullImageBackground() : background)
{
  _children.push_back(child0);
  _children.push_back(child1);
}

LayoutImageBuilder::LayoutImageBuilder(IImageBuilder*         child0,
                                       const ImageBackground* background) :
_background((background == NULL) ? new NullImageBackground() : background)
{
  _children.push_back(child0);
}


LayoutImageBuilder::~LayoutImageBuilder() {
  const size_t childrenSize = _children.size();
  for (size_t i = 0; i < childrenSize; i++) {
    IImageBuilder* child = _children[i];
    delete child;
  }
  
  delete _background;
}

bool LayoutImageBuilder::isMutable() const {
  //TODO: #warning TODO: make mutable if any children is
  return false;
}

void LayoutImageBuilder::build(const G3MContext* context,
                               IImageBuilderListener* listener,
                               bool deleteListener) {
  const size_t childrenSize = _children.size();
  if (childrenSize > 0) {
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
}

void LayoutImageBuilder::ChildrenResult::childImageCreated(const IImage*      image,
                                                           const std::string& imageName,
                                                           const size_t       childIndex) {
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
                                                    const size_t       childIndex) {
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
