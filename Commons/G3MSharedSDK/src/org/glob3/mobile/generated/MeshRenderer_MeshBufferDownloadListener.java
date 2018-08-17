package org.glob3.mobile.generated;import java.util.*;

public class MeshRenderer_MeshBufferDownloadListener implements IBufferDownloadListener
{
	private MeshRenderer _meshRenderer;
	private final float _pointSize;
	private final double _deltaHeight;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final Color _color;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public Color _color = new internal();
//#endif
	private MeshLoadListener _listener;
	private boolean _deleteListener;
	private final IThreadUtils _threadUtils;
	private boolean _isBSON;
	private final MeshType _meshType;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final G3MContext _context;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public G3MContext _context = new internal();
//#endif


	public MeshRenderer_MeshBufferDownloadListener(MeshRenderer meshRenderer, float pointSize, double deltaHeight, Color color, MeshLoadListener listener, boolean deleteListener, IThreadUtils threadUtils, boolean isBSON, MeshType meshType, G3MContext context)
	{
		_meshRenderer = meshRenderer;
		_pointSize = pointSize;
		_deltaHeight = deltaHeight;
		_color = color;
		_listener = listener;
		_deleteListener = deleteListener;
		_threadUtils = threadUtils;
		_isBSON = isBSON;
		_meshType = meshType;
		_context = context;
	}

	public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
	{
		ILogger.instance().logInfo("Downloaded Mesh buffer from \"%s\" (%db)", url._path.c_str(), buffer.size());

		_threadUtils.invokeAsyncTask(new MeshRenderer_MeshParserAsyncTask(_meshRenderer, url, buffer, _pointSize, _deltaHeight, _color, _listener, _deleteListener, _isBSON, _meshType, _context), true);
		_color = null;
	}

	public final void onError(URL url)
	{
		ILogger.instance().logError("Error downloading \"%s\"", url._path.c_str());

		if (_listener != null)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onError(url);
			_listener.onError(new URL(url));
		}

		if (_deleteListener)
		{
			if (_listener != null)
				_listener.dispose();
		}
	}

	public final void onCancel(URL url)
	{
		ILogger.instance().logInfo("Canceled download of \"%s\"", url._path.c_str());

		if (_deleteListener)
		{
			if (_listener != null)
				_listener.dispose();
		}
	}

	public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
	{
		// do nothing
	}

	public void dispose()
	{
		if (_color != null)
			_color.dispose();
		_color = null;
	}

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff
