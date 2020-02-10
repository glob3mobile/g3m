//
//  LayoutImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3M__LayoutImageBuilder__
#define __G3M__LayoutImageBuilder__

#include <vector>

#include "AbstractImageBuilder.hpp"
#include "IImageBuilderListener.hpp"
#include "IImage.hpp"
#include "RCObject.hpp"

class ImageBackground;


class LayoutImageBuilder : public AbstractImageBuilder {
protected:


  class ChildResult {
  public:
    const IImage*     _image;
    const std::string _imageName;
    const std::string _error;

    ChildResult(const IImage*      image,
                const std::string& imageName) :
    _image(image),
    _imageName(imageName),
    _error("")
    {
    }

    ChildResult(const std::string& error) :
    _image(NULL),
    _imageName(""),
    _error(error)
    {
    }

    ~ChildResult() {
      delete _image;
    }
  };


  class ChildrenResult : public RCObject {
  private:
    LayoutImageBuilder*    _layoutImageBuilder;
    const G3MContext*      _context;
    IImageBuilderListener* _listener;
    bool                   _deleteListener;

    size_t _childrenResultPendingCounter;

  protected:
    ~ChildrenResult() {
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

  public:
    std::vector<ChildResult*> _childrenResult;

    ChildrenResult(LayoutImageBuilder* layoutImageBuilder,
                   const size_t childrenSize,
                   const G3MContext* context,
                   IImageBuilderListener* listener,
                   bool deleteListener) :
    _layoutImageBuilder(layoutImageBuilder),
    _context(context),
    _listener(listener),
    _deleteListener(deleteListener)
    {
      _childrenResultPendingCounter = childrenSize;
      for (size_t i = 0; i < childrenSize; i++) {
        _childrenResult.push_back(NULL); // make space for the result
      }
    }

    void childImageCreated(const IImage*      image,
                           const std::string& imageName,
                           const size_t       childIndex);

    void childError(const std::string& error,
                    const size_t childIndex);
  };


  class LayoutImageBuilderChildListener : public IImageBuilderListener {
  private:
    ChildrenResult* _childrenResult;
    const size_t    _childIndex;

  public:
    LayoutImageBuilderChildListener(ChildrenResult* childrenResult,
                                    const size_t    childIndex) :
    _childrenResult(childrenResult),
    _childIndex(childIndex)
    {
      _childrenResult->_retain();
    }

    ~LayoutImageBuilderChildListener() {
      _childrenResult->_release();
    }

    void imageCreated(const IImage*      image,
                      const std::string& imageName) {
      _childrenResult->childImageCreated(image, imageName, _childIndex);
    }

    void onError(const std::string& error) {
      _childrenResult->childError(error, _childIndex);
    }
  };


  std::vector<IImageBuilder*> _children;

  const ImageBackground* _background;

  /*
   | margin
   |   border
   |     padding
   |       content
   */


  LayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                     const ImageBackground*             background);

  LayoutImageBuilder(IImageBuilder*         child0,
                     IImageBuilder*         child1,
                     const ImageBackground* background);

  LayoutImageBuilder(IImageBuilder*         child0,
                     const ImageBackground* background);

  virtual void doLayout(const G3MContext* context,
                        IImageBuilderListener* listener,
                        bool deleteListener,
                        const std::vector<ChildResult*>& results) = 0;

protected:
  ~LayoutImageBuilder();

public:

  bool isMutable() const;

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
