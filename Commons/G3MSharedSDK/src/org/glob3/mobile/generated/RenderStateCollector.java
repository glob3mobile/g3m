package org.glob3.mobile.generated;
// an error if any added state is an error (keeping all their errors), else busy if any is busy, else ready
public class RenderStateCollector
{
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();
  private boolean _error;
  private boolean _busy;

  public RenderStateCollector()
  {
     _error = false;
     _busy = false;
  }

  public void dispose()
  {
  }

  public final void reset()
  {
    _errors.clear();
    _error = false;
    _busy = false;
  }

  public final void add(RenderState state)
  {
    final RenderState_Type type = state._type;
    if (type == RenderState_Type.RENDER_ERROR)
    {
      _error = true;
  
      final java.util.ArrayList<String> errors = state.getErrors();
      _errors.addAll(errors);
    }
    else if (type == RenderState_Type.RENDER_BUSY)
    {
      _busy = true;
    }
  }

  public final RenderState getState()
  {
    if (_error)
    {
      return RenderState.error(_errors);
    }
    else if (_busy)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

}