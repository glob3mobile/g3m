//
//  MarksRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/06/12.
//

#ifndef G3M_MarksRenderer
#define G3M_MarksRenderer

#include <vector>
#include "DefaultRenderer.hpp"
#include "MarkTransitionMode.hpp"

class Mark;
class Camera;
class Planet;
class TimeInterval;
class IImageFactory;
class IImage;
class MarksRenderer_HintListener;
class MutableVector3D;
class MarkTouchListener;
class IFloatBuffer;
class ITimer;
class MarkFilter;
class Vector2F;


class MarksRenderer : public DefaultRenderer {
private:
  const bool         _readyWhenMarksReady;
  std::vector<Mark*> _marks;

#ifdef C_CODE
  const Camera*     _lastCamera;
#endif
#ifdef JAVA_CODE
  private Camera     _lastCamera;
#endif

  MarkTouchListener* _markTouchListener;
  bool               _autoDeleteMarkTouchListener;

  long long _downloadPriority;

  GLState* _glState;

  void updateGLState(const G3MRenderContext* rc);
  IFloatBuffer* _billboardTexCoords;
  IFloatBuffer* getBillboardTexCoords();

  bool _renderInReverse;
  bool _progressiveInitialization;
  ITimer* _initializationTimer;

  bool      _declutter;
  bool      _horizonBand;
  float     _declutterMargin;
  long long          _transitionMS;
  long long          _delayMS;
  MarkTransitionMode _transitionMode;

  // the default hint: one image, shared by every mark without a hint of its own
  IImageFactory*              _hintImageFactory;
  MarksRenderer_HintListener* _hintListener;
#ifdef C_CODE
  const IImage*               _hintImage;
#endif
#ifdef JAVA_CODE
  private IImage              _hintImage;
#endif
  std::string                 _hintImageName;

  void startHintImage();
  void attachHint(Mark* mark);
  // reused between frames: the marks in placing order and the screen rectangles taken
  std::vector<Mark*> _declutterCandidates;
  std::vector<Mark*> _declutterOrder;
  std::vector<float> _takenLeft;
  std::vector<float> _takenTop;
  std::vector<float> _takenRight;
  std::vector<float> _takenBottom;

  void declutter(const Camera* camera,
                 const Planet* planet,
                 const MutableVector3D& cameraPosition,
                 double cameraHeight);

  bool isFree(float left, float top, float right, float bottom) const;

  void removeMarksThatLeft(bool evenLeaving);

  bool hasOutfitImage(const Mark* mark,
                      size_t outfitIndex) const;

  bool outfitFits(const Mark* mark,
                  const Vector2F& markPixel,
                  size_t outfitIndex,
                  float margin) const;

  void takeOutfitSpace(const Mark* mark,
                       const Vector2F& markPixel,
                       size_t outfitIndex);

  int chooseOutfit(const Mark* mark,
                   const Vector2F& markPixel) const;

public:

  MarksRenderer(bool readyWhenMarksReady,
                bool renderInReverse = false,
                bool progressiveInitialization = true);

  void setRenderInReverse(bool renderInReverse) {
    _renderInReverse = renderInReverse;
  }

  /**
   * Each frame, every visible mark takes its largest outfit that does not
   * overlap the marks placed before it, or hides when none fits. Marks with
   * priority go first, the highest first; the rest in the order they are drawn
   * on top.
   */
  void setDeclutter(bool declutter);

  bool getDeclutter() const {
    return _declutter;
  }

  /** the free space a mark needs around it to grow or come back; it keeps its place with no margin, so it does not blink */
  void setDeclutterMargin(float marginInPixels) {
    _declutterMargin = marginInPixels;
  }

  /** the hint of the marks without their own: drawn centred on the position when nothing else fits; the renderer owns the factory; NULL: no hint for the marks added from now on */
  void setHint(IImageFactory* hintImageFactory);

  void onHintImageCreated(const IImage* image,
                          const std::string& imageName);

  void onHintImageCreationError();

  /** the marks shrink while they sink behind the horizon, over their own apparent height, instead of vanishing at once; on by default */
  void setHorizonBand(bool horizonBand) {
    _horizonBand = horizonBand;
  }

  bool getHorizonBand() const {
    return _horizonBand;
  }

  /** how long an outfit takes to come in, and the one it replaces to go away, both at once; 250ms by default */
  void setDeclutterTransitionDuration(const TimeInterval& duration);

  /** how long a mark's new outfit must hold before the mark changes, so changes that come and go are not seen; marks may overlap meanwhile; 250ms by default */
  void setDeclutterDelay(const TimeInterval& delay);

  /** how outfits come in and go away: SCALE_AND_ALPHA by default; WIDTH suits outfits that move the label to the other side of the icon */
  void setDeclutterTransitionMode(MarkTransitionMode mode) {
    _transitionMode = mode;
  }

  MarkTransitionMode getDeclutterTransitionMode() const {
    return _transitionMode;
  }

  bool getRenderInReverse() const {
    return _renderInReverse;
  }

  void setMarkTouchListener(MarkTouchListener* markTouchListener,
                            bool autoDelete);

  virtual ~MarksRenderer();

  virtual void onChangedContext();

  virtual void render(const G3MRenderContext* rc, GLState* glState);

  const bool hasMarks() const;

  void addMark(Mark* mark);

  void removeMark(Mark* mark);

  void removeAllMarks(bool deleteMarks = true);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  RenderState getRenderState(const G3MRenderContext* rc);

  //TODO: WHY? VTP
  void onResume(const G3MContext* context) {
    _context = context;
  }

  /**
   Change the download-priority used by Marks (for downloading textures).

   Default value is 1000000
   */
  void setDownloadPriority(long long downloadPriority) {
    _downloadPriority = downloadPriority;
  }

  long long getDownloadPriority() const {
    return _downloadPriority;
  }

  bool isVisible(const G3MRenderContext* rc) {
    return true;
  }

  void modifiyGLState(GLState* state) {

  }

  size_t removeAllMarks(const MarkFilter& filter,
                        bool animated,
                        bool deleteMarks);

  const std::vector<Mark*> getAllMarks(const MarkFilter& filter) const;

};

#endif
