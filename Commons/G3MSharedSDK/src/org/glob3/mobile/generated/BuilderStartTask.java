package org.glob3.mobile.generated; 
//class BuilderStartTask : public FrameTask {
//private:
//  TileTextureBuilder* _builder;
//
//public:
//  BuilderStartTask(TileTextureBuilder* builder) :
//  _builder(builder)
//  {
//    _builder->_retain();
//  }
//
//  virtual ~BuilderStartTask() {
//    _builder->_release();
//  }
//
//  void execute(const G3MRenderContext* rc) {
//    _builder->start();
//  }
//
//  bool isCanceled(const G3MRenderContext *rc){
//    return false;
//  }
//};

public class BuilderStartTask extends FrameTask
{
  private TileTextureBuilder _builder;

  public BuilderStartTask(TileTextureBuilder builder)
  {
     _builder = builder;
    _builder._retain();
  }

  public void dispose()
  {
    _builder._release();
  }

  public final void execute(G3MRenderContext rc)
  {
    _builder.start();
  }

  public final boolean isCanceled(G3MRenderContext rc)
  {
    return _builder.isCanceled();
  }
}