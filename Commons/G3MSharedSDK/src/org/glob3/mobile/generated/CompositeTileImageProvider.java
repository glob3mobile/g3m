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

  private static class ChildResult
  {
    public final boolean _isError;
    public final boolean _isCanceled;
    public final IImage _image;
    public final String _imageId;
    public final TileImageContribution _contribution;
    public final String _error;

    public static CompositeTileImageProvider.ChildResult image(IImage image, String imageId, TileImageContribution contribution)
    {
      return new CompositeTileImageProvider.ChildResult(false, false, image, imageId, contribution, ""); // error -  isCanceled -  isError
    }

    public static CompositeTileImageProvider.ChildResult error(String error)
    {
      return new CompositeTileImageProvider.ChildResult(true, false, null, "", null, error); // contribution -  imageId -  image -  isCanceled -  isError
    }

    public static CompositeTileImageProvider.ChildResult cancelation()
    {
      return new CompositeTileImageProvider.ChildResult(false, true, null, "", null, ""); // error -  contribution -  imageId -  image -  isCanceled -  isError
    }

    public void dispose()
    {
      if (_image != null)
         _image.dispose();
    }


    private ChildResult(boolean isError, boolean isCanceled, IImage image, String imageId, TileImageContribution contribution, String error)
    {
       _isError = isError;
       _isCanceled = isCanceled;
       _image = image;
       _imageId = imageId;
       _contribution = contribution;
       _error = error;
    }

  }


  private static class Composer extends RCObject
  {
    private CompositeTileImageProvider _compositeTileImageProvider;
    private TileImageListener _listener;
    private final boolean _deleteListener;
    private CompositeTileImageContribution _compositeContribution;
    private final java.util.ArrayList<ChildResult> _results = new java.util.ArrayList<ChildResult>();
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
      if (_canceled)
      {
        cleanUp();
        return;
      }
    
      if (_contributionsSize == 1)
      {
        final ChildResult singleResult = _results.get(0);
    
        if (singleResult._isError)
        {
          _listener.imageCreationError(_tileId, singleResult._error);
        }
        else if (singleResult._isCanceled)
        {
          _listener.imageCreationCanceled(_tileId);
        }
        else
        {
          _listener.imageCreated(singleResult._imageId, singleResult._image.shallowCopy(), singleResult._imageId, singleResult._contribution);
        }
    
        cleanUp();
      }
      else
      {
        if (_anyError)
        {
          String composedError = "";
          for (int i = 0; i < _contributionsSize; i++)
          {
            final ChildResult childResult = _results.get(i);
            if (childResult._isError)
            {
              composedError += childResult._error + " ";
            }
          }
    
          _listener.imageCreationError(_tileId, composedError);
    
          cleanUp();
        }
        else if (_anyCancelation)
        {
          _listener.imageCreationCanceled(_tileId);
          cleanUp();
        }
        else
        {
          ICanvas canvas = IFactory.instance().createCanvas();
    
          canvas.initialize(_width, _height);
    
          String imageId = "";
    
          for (int i = 0; i < _contributionsSize; i++)
          {
           final ChildResult result = _results.get(i);
    
            imageId += result._imageId + "|";
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning JM: consider sector && transparency
            canvas.drawImage(result._image, 0, 0);
          }
          _imageId = imageId;
    
          canvas.createImage(new ComposerImageListener(this), true);
    
          if (canvas != null)
             canvas.dispose();
        }
      }
    }

    private boolean _anyError;
    private boolean _anyCancelation;
    private boolean _canceled;

    private void cleanUp()
    {
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
        _listener = null;
      }
    
      _compositeTileImageProvider.composerDone(this);
    }

    private final int _width;
    private final int _height;

    private String _imageId;

    private FrameTasksExecutor _frameTasksExecutor;

    public void dispose()
    {
    ///#warning remove debug printf
    //  printf("**** deleted CompositeTileImageProvider::Composer %p (_stepsDone=%d, _anyError=%s, _anyCancelation=%s, _canceled=%s, _compositeContribution=%p)\n",
    //         this,
    //         _stepsDone,
    //         _anyError       ? "true" : "false",
    //         _anyCancelation ? "true" : "false",
    //         _canceled       ? "true" : "false",
    //         _compositeContribution
    //         );
      for (int i = 0; i < _contributionsSize; i++)
      {
        final ChildResult result = _results.get(i);
        if (result != null)
           result.dispose();
      }
      _compositeContribution = null;
    }

    public final String _tileId;

    public Composer(int width, int height, CompositeTileImageProvider compositeTileImageProvider, String tileId, TileImageListener listener, boolean deleteListener, CompositeTileImageContribution compositeContribution, FrameTasksExecutor frameTasksExecutor)
    {
       _width = width;
       _height = height;
       _compositeTileImageProvider = compositeTileImageProvider;
       _tileId = tileId;
       _listener = listener;
       _deleteListener = deleteListener;
       _compositeContribution = compositeContribution;
       _contributionsSize = compositeContribution.size();
       _frameTasksExecutor = frameTasksExecutor;
       _stepsDone = 0;
       _anyError = false;
       _anyCancelation = false;
       _canceled = false;
      for (int i = 0; i < _contributionsSize; i++)
      {
        _results.add(null);
      }
    }


    public final void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution, int index)
    {
    //  if (_results[index] != NULL) {
    //    printf("Logic error 1\n");
    //  }
      _results.set(index, ChildResult.image(image, imageId, contribution));
      stepDone();
    }

    public final void imageCreationError(String error, int index)
    {
    //  if (_results[index] != NULL) {
    //    printf("Logic error 2\n");
    //  }
      _results.set(index, ChildResult.error(error));
      _anyError = true;
      stepDone();
    }

    public final void imageCreationCanceled(int index)
    {
    //  if (_results[index] != NULL) {
    //    printf("Logic error 3\n");
    //  }
      _results.set(index, ChildResult.cancelation());
      _anyCancelation = true;
      stepDone();
    }

    public final void cancel(Tile tile)
    {
      _canceled = true;
      _compositeTileImageProvider.cancelChildren(tile, _compositeContribution);
    }

    public final void imageCreated(IImage image)
    {
      _listener.imageCreated(_tileId, image, _imageId, _compositeContribution);
      _compositeContribution = null; // the _compositeContribution ownership moved to the listener
      cleanUp();
    }

  }


  private static class ComposerImageListener extends IImageListener
  {
    private Composer _composer;
    public ComposerImageListener(Composer composer)
    {
       _composer = composer;
      _composer._retain();
    }

    public void dispose()
    {
      _composer._release();
    }

    public final void imageCreated(IImage image)
    {
      _composer.imageCreated(image);
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

  private final java.util.HashMap<String, Composer> _composers = new java.util.HashMap<String, Composer>();

  public void dispose()
  {
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
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
    final java.util.ArrayList<CompositeTileImageContribution.ChildContribution> childrenContributions = new java.util.ArrayList<CompositeTileImageContribution.ChildContribution>();
  
    for (int i = 0; i < _childrenSize; i++)
    {
      TileImageProvider child = _children.get(i);
      final TileImageContribution childContribution = child.contribution(tile);
      if (childContribution != null)
      {
        // ignore previous contributions, they are covered by the current fullCoverage & Opaque contribution.
        final int childrenContributionsSize = childrenContributions.size();
        if ((childrenContributionsSize > 0) && childContribution.isFullCoverageAndOpaque())
        {
          for (int j = 0; j < childrenContributionsSize; j++)
          {
            final CompositeTileImageContribution.ChildContribution previousContribution = childrenContributions.get(j);
            previousContribution.dispose();
          }
          childrenContributions.clear();
        }
  
        childrenContributions.add(new CompositeTileImageContribution.ChildContribution(i, childContribution));
      }
    }
  
    return CompositeTileImageContribution.create(childrenContributions);
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
  
    final CompositeTileImageContribution compositeContribution = (CompositeTileImageContribution) contribution;
  
    final String tileId = tile._id;
  
    Composer composer = new Composer(resolution._x, resolution._y, this, tileId, listener, deleteListener, compositeContribution, frameTasksExecutor);
  
    _composers.put(tileId, composer);
  
    final int contributionsSize = compositeContribution.size();
    for (int i = 0; i < contributionsSize; i++)
    {
      final CompositeTileImageContribution.ChildContribution childContribution = compositeContribution.get(i);
  
      TileImageProvider child = _children.get(childContribution._childIndex);
  
      child.create(tile, childContribution._contribution, resolution, tileDownloadPriority, logDownloadActivity, new ChildTileImageListener(composer, i), true, frameTasksExecutor);
    }
  }

  public final void cancel(Tile tile)
  {
    final String tileId = tile._id;
    final Composer composer = _composers.remove(tileId);
    if (composer != null) {
      composer.cancel(tile);
    }
  }

  public final void composerDone(Composer composer)
  {
    _composers.remove(composer._tileId);
  //  delete composer;
    composer._release();
  }

  public final void cancelChildren(Tile tile, CompositeTileImageContribution compositeContribution)
  {
    final int contributionsSize = compositeContribution.size();
  
    // store all the indexes before calling child->cancel().
    // child->cancel() can force the deletion of the builder (and in order the deletion of compositeContribution)
    int[] indexes = new int[contributionsSize];
    for (int i = 0; i < contributionsSize; i++)
    {
      indexes[i] = compositeContribution.get(i)._childIndex;
    }
  
    for (int i = 0; i < contributionsSize; i++)
    {
      TileImageProvider child = _children.get(indexes[i]);
      child.cancel(tile);
    }
  
    indexes = null;
  }
}