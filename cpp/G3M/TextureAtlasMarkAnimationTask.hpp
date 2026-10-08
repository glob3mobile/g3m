//
//  TextureAtlasMarkAnimationTask.hpp
//  G3M
//
//  Extracted from Mark.hpp on 10/8/26.
//

#ifndef G3M_TextureAtlasMarkAnimationTask
#define G3M_TextureAtlasMarkAnimationTask

#include "GTask.hpp"
#include "PeriodicalTask.hpp"

#include "Mark.hpp"


class TextureAtlasMarkAnimationTask: public PeriodicalTask{
  
  class TextureAtlasMarkAnimationGTask: public GTask{
    Mark* _mark;

    const int _cols;
    const int _rows;
    const int _nFrames;
    const float _scaleX;
    const float _scaleY;

    int _currentFrame;
    
  public:
    
    ~TextureAtlasMarkAnimationGTask() {}
    
    TextureAtlasMarkAnimationGTask(Mark* mark,
                                   int cols,
                                   int rows,
                                   int nFrames):
    _mark(mark),
    _cols(cols),
    _rows(rows),
    _nFrames(nFrames),
    _scaleX(1.0f / cols),
    _scaleY(1.0f / rows),
    _currentFrame(0)
    {
      //    _mark->setOnScreenSize(Vector2F(100,100));
    }
    
    
    virtual void run(const G3MContext* context) {
      const int row = _currentFrame / _cols;
      const int col = _currentFrame % _cols;
      
      const float translationX = col * (1.0f / _cols);
      const float translationY = row * (1.0f / _rows);

      _mark->setTextureCoordinatesTransformation(translationX,
                                                 translationY,
                                                 _scaleX,
                                                 _scaleY);
      _currentFrame = (_currentFrame+1) % _nFrames;
    }
    
  };
  
  
public:
  TextureAtlasMarkAnimationTask(Mark* mark, int nColumn, int nRows, int nFrames, const TimeInterval& frameTime):
  PeriodicalTask(frameTime, new TextureAtlasMarkAnimationGTask(mark, nColumn, nRows, nFrames))
  {
  }
};

#endif
