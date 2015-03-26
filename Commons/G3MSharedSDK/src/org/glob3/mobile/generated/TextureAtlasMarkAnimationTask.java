package org.glob3.mobile.generated; 
public class TextureAtlasMarkAnimationTask extends PeriodicalTask
{

  private static class TextureAtlasMarkAnimationGTask extends GTask
  {
    private Mark _mark;
    private int _cols;
    private int _rows;
    private int _nFrames;

    private int _currentFrame;

    private float _scaleX;
    private float _scaleY;

    public void dispose()
    {
    }

    public TextureAtlasMarkAnimationGTask(Mark mark, int nColumn, int nRows, int nFrames)
    {
       _mark = mark;
       _currentFrame = 0;
       _cols = nColumn;
       _rows = nRows;
       _nFrames = nFrames;
      //    _mark->setOnScreenSize(Vector2F(100,100));

      _scaleX = 1.0f / _cols;
      _scaleY = 1.0f / _rows;
    }


    public void run(G3MContext context)
    {
      int row = _currentFrame / _cols;
      int col = _currentFrame % _cols;

      float transX = col * (1.0f / _cols);
      float transY = row * (1.0f / _rows);
      //        printf("FRAME:%d, R:%d, C:%d -> %f %f\n", _currentFrame, row, col, transX, transY);

      _mark.setTextureCoordinatesTransformation(new Vector2F(transX,transY), new Vector2F(_scaleX, _scaleY));
      _currentFrame = (_currentFrame+1) % _nFrames;
    }

  }


  public TextureAtlasMarkAnimationTask(Mark mark, int nColumn, int nRows, int nFrames, TimeInterval frameTime)
  {
     super(frameTime, new TextureAtlasMarkAnimationGTask(mark, nColumn, nRows, nFrames));
  }
}