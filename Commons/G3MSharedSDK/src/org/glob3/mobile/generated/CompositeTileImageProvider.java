package org.glob3.mobile.generated; 
//
//  CompositeTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

//
//  CompositeTileImageProvider.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//



//class CompositeTileImageContribution;


public class CompositeTileImageProvider extends CanvasTileImageProvider
{

  private static class ChildContribution
  {
    public final boolean _isError;
    public final boolean _isCanceled;
    public final IImage _image;
    public final String _imageId;
    public final TileImageContribution _contribution;
    public final String _error;

    public static CompositeTileImageProvider.ChildContribution image(IImage image, String imageId, TileImageContribution contribution)
    {
      return new CompositeTileImageProvider.ChildContribution(false, false, image, imageId, contribution, ""); // error -  isCanceled -  isError
    }

    public static CompositeTileImageProvider.ChildContribution error(String error)
    {
      return new CompositeTileImageProvider.ChildContribution(true, false, null, "", null, error); // contribution -  imageId -  image -  isCanceled -  isError
    }

    public static CompositeTileImageProvider.ChildContribution cancelation()
    {
      return new CompositeTileImageProvider.ChildContribution(false, true, null, "", null, ""); // error -  contribution -  imageId -  image -  isCanceled -  isError
    }


    private ChildContribution(boolean isError, boolean isCanceled, IImage image, String imageId, TileImageContribution contribution, String error)
    {
       _isError = isError;
       _isCanceled = isCanceled;
       _image = image;
       _imageId = imageId;
       _contribution = contribution;
       _error = error;
    }
  }

  private static class Composer
  {
    private final String _tileId;
    private TileImageListener _listener;
    private final boolean _deleteListener;
    private final CompositeTileImageContribution _compositeContribution;
    private final java.util.ArrayList<ChildContribution> _contributions = new java.util.ArrayList<ChildContribution>();
    private final int _contributionsSize;

    private int _stepsDone;
    private void stepDone()
    {
      _stepsDone++;
      if (_stepsDone == _contributionsSize)
      {
        done();
      }
    }
    private void done()
    {
    
      if (_contributionsSize == 1)
      {
        final ChildContribution singleContribution = _contributions.get(0);
    
        if (singleContribution._isError)
        {
          _listener.imageCreationError(_tileId, singleContribution._error);
        }
        else if (singleContribution._isCanceled)
        {
          _listener.imageCreationCanceled(_tileId);
        }
        else
        {
          _listener.imageCreated(singleContribution._imageId, singleContribution._image, singleContribution._imageId, singleContribution._contribution);
        }
        if (_deleteListener)
        {
          if (_listener != null)
             _listener.dispose();
          _listener = null;
        }
      }
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODODODODODODO
    }

    private boolean _anyError;
    private boolean _anyCancelation;
    public void dispose()
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
      for (int i = 0; i < _contributionsSize; i++)
      {
        final ChildContribution contribution = _contributions.get(i);
        if (contribution != null)
           contribution.dispose();
      }
    }

    public Composer(String tileId, TileImageListener listener, boolean deleteListener, CompositeTileImageContribution compositeContribution)
    {
       _tileId = tileId;
       _listener = listener;
       _deleteListener = deleteListener;
       _compositeContribution = compositeContribution;
       _contributionsSize = compositeContribution.size();
       _stepsDone = 0;
       _anyError = false;
       _anyCancelation = false;
      for (int i = 0; i < _contributionsSize; i++)
      {
        _contributions.add(null);
      }
    }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution, int index);

    public final void imageCreationError(String error, int index)
    {
      if (_contributions.get(index) != null)
      {
        System.out.print("Logic error 2\n");
      }
      _contributions.set(index, ChildContribution.error(error));
      _anyError = true;
      stepDone();
    }

    public final void imageCreationCanceled(int index)
    {
      if (_contributions.get(index) != null)
      {
        System.out.print("Logic error 3\n");
      }
      _contributions.set(index, ChildContribution.cancelation());
      _anyCancelation = true;
      stepDone();
    }

  }


  private static class ChildTileImageListener extends TileImageListener
  {
    private Composer _composer;
    private final int _index;

    public ChildTileImageListener(Composer composer, int index)
    {
       _composer = composer;
       _index = index;
    }

    public final void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution)
    {
      _composer.imageCreated(tileId, image, imageId, contribution, _index);
    }

    public final void imageCreationError(String tileId, String error)
    {
      _composer.imageCreationError(error, _index);
    }

    public final void imageCreationCanceled(String tileId)
    {
      _composer.imageCreationCanceled(_index);
    }

  }


  private java.util.ArrayList<TileImageProvider> _children = new java.util.ArrayList<TileImageProvider>();
  private int _childrenSize;

  public void dispose()
  {
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
      //    delete child;
      child._release();
    }
    super.dispose();
  }

  public CompositeTileImageProvider()
  {
     _childrenSize = 0;
  }

  public final void addProvider(TileImageProvider child)
  {
    _children.add(child);
    _childrenSize = _children.size();
  }

  public final TileImageContribution contribution(Tile tile)
  {
    java.util.ArrayList<CompositeTileImageContribution.ChildContribution> childrenContributions = new java.util.ArrayList<CompositeTileImageContribution.ChildContribution>();
  
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
      final TileImageContribution childContribution = child.contribution(tile);
  //    if (childContribution->isNone()) {
  //      TileImageContribution::deleteContribution( childContribution );
  //    }
  //    else {
      if (childContribution != null)
      {
        // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
        final int childrenContributionsSize = childrenContributions.size();
        if ((childrenContributionsSize > 0) && childContribution.isFullCoverageAndOpaque())
        {
          for (int j = 0; j < childrenContributionsSize; j++)
          {
            CompositeTileImageContribution.ChildContribution previousContribution = childrenContributions.get(j);
            if (previousContribution != null)
               previousContribution.dispose();
          }
          childrenContributions.clear();
        }
  
        childrenContributions.add(new CompositeTileImageContribution.ChildContribution(i, childContribution));
      }
    }
  
    return CompositeTileImageContribution.create(childrenContributions);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
  
    final CompositeTileImageContribution compositeContribution = (CompositeTileImageContribution) contribution;
  
    final int contributionsSize = compositeContribution.size();
  
    Composer composer = new Composer(tile._id, listener, deleteListener, compositeContribution);
    for (int i = 0; i < contributionsSize; i++)
    {
      final CompositeTileImageContribution.ChildContribution singleContribution = compositeContribution.get(i);
  
      TileImageProvider child = _children.get(singleContribution._childIndex);
      final TileImageContribution contribution = singleContribution._contribution;
  
      child.create(tile, contribution, resolution, tileDownloadPriority, logDownloadActivity, new ChildTileImageListener(composer, i), true);
    }
  
  
    /*
  
  //  if (contributionsSize == 1) {
  //    const CompositeTileImageContribution::ChildContribution* singleContribution = compositeContribution->get(0);
  //
  //    TileImageProvider* child = _children[ singleContribution->_childIndex ];
  //    const TileImageContribution* contribution = singleContribution->_contribution;
  //
  //    child->create(tile,
  //                  contribution,
  //                  resolution,
  //                  tileDownloadPriority,
  //                  logDownloadActivity,
  //                  listener,
  //                  deleteListener);
  //  }
  //  else {
      // temporary error code
      TileImageContribution::deleteContribution( contribution );
      listener->imageCreationError(tile->_id, "Not yet implemented");
      if (deleteListener) {
        delete listener;
      }
  //  }
     */
  }

  public final void cancel(Tile tile)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Diego at work!
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO cancel the (posible) single request
  
  }

}