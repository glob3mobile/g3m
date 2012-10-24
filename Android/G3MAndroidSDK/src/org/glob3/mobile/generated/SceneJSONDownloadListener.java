package org.glob3.mobile.generated; 
/*#include "IStringBuilder.hpp"
#include "IMathUtils.hpp"
#include "IFactory.hpp"
#include "IJSONParser.hpp"
#include "ILogger.hpp"
#include "IStringUtils.hpp"
#include "IThreadUtils.hpp"
*/










public class SceneJSONDownloadListener implements IBufferDownloadListener
{

	private LayerSet _layerSet;
	private CompositeRenderer _composite;
	private TilesRenderParameters _parameters;
	private java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();
	private G3MJSONBuilder _g3mJsonBuilder;

	public SceneJSONDownloadListener(CompositeRenderer composite, LayerSet layerSet, TilesRenderParameters parameters, java.util.ArrayList<Renderer> renderers, G3MJSONBuilder g3mJSonBuilder)
	{
		_layerSet = layerSet;
		_composite = composite;
		_parameters = parameters;
		_renderers = renderers;
		_g3mJsonBuilder = g3mJSonBuilder;
	}

	public final void onDownload(URL url, IByteBuffer buffer)
	{
		String String = buffer.getAsString();
		_g3mJsonBuilder.fromSceneJSON(String, _composite, _layerSet, _parameters, _renderers);
	}

	public final void onError(URL url)
	{
		ILogger.instance().logError("The requested scene file could not be found!");
	}

	public final void onCancel(URL url)
	{
	}
	public final void onCanceledDownload(URL url, IByteBuffer data)
	{
	}

	public void dispose()
	{
	}
}