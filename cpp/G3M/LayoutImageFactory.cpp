//
//  LayoutImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "LayoutImageFactory.hpp"

#include "ErrorHandling.hpp"

#include "ImageBackground.hpp"
#include "NullImageBackground.hpp"

LayoutImageFactory::LayoutImageFactory(const std::vector<IImageFactory*>& children,
                                       const ImageBackground*             background) :
_children(children),
_background((background == NULL) ? new NullImageBackground() : background)
{
}

LayoutImageFactory::LayoutImageFactory(IImageFactory*         child0,
                                       IImageFactory*         child1,
                                       const ImageBackground* background) :
_background((background == NULL) ? new NullImageBackground() : background)
{
  _children.push_back(child0);
  _children.push_back(child1);
}

LayoutImageFactory::LayoutImageFactory(IImageFactory*         child0,
                                       const ImageBackground* background) :
_background((background == NULL) ? new NullImageBackground() : background)
{
  _children.push_back(child0);
}


LayoutImageFactory::~LayoutImageFactory() {
  const size_t childrenSize = _children.size();
  for (size_t i = 0; i < childrenSize; i++) {
    IImageFactory* child = _children[i];
    delete child;
  }
  
  delete _background;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool LayoutImageFactory::isMutable() const {
  //TODO: #warning TODO: make mutable if any children is
  return false;
}

void LayoutImageFactory::create(const G3MContext* context,
                               IImageFactoryListener* listener,
                               bool deleteListener) {
  const size_t childrenSize = _children.size();
  if (childrenSize > 0) {
    ChildrenResult* childrenResult = new ChildrenResult(this,
                                                        childrenSize,
                                                        context,
                                                        listener,
                                                        deleteListener);
    for (int i = 0; i < childrenSize; i++) {
      IImageFactory* child = _children[i];
      
      child->create(context,
                   new LayoutImageFactoryChildListener(childrenResult, i),
                   true);
    }
    
    childrenResult->_release();
  }
}

void LayoutImageFactory::ChildrenResult::childImageCreated(const IImage*      image,
                                                           const std::string& imageName,
                                                           const size_t       childIndex) {
  if (_childrenResult[childIndex] != NULL) {
    THROW_EXCEPTION("Logic error");
  }
  
  _childrenResult[childIndex] = new ChildResult(image, imageName);
  if (--_childrenResultPendingCounter == 0) {
    _layoutImageFactory->doLayout(_context,
                                  _listener,
                                  _deleteListener,
                                  _childrenResult);
  }
}

void LayoutImageFactory::ChildrenResult::childError(const std::string& error,
                                                    const size_t       childIndex) {
  if (_childrenResult[childIndex] != NULL) {
    THROW_EXCEPTION("Logic error");
  }
  
  _childrenResult[childIndex] = new ChildResult(error);
  if (--_childrenResultPendingCounter == 0) {
    _layoutImageFactory->doLayout(_context,
                                  _listener,
                                  _deleteListener,
                                  _childrenResult);
  }
}
