#include "D3DRenderer.hpp"
#include "G3MWidget_win8.hpp"
#include "G3MBuilder_win8.hpp"




/////////////////////////////////////
//Testing
#include "PlanetRendererBuilder.hpp"
#include "GInitializationTask.hpp"
#include "IDownloader.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"
#include "LevelTileCondition.hpp"



//Non-finishing init-task
class RueditaTask :public GInitializationTask{
	bool isDone(const G3MContext* context){
		return false;
	}

	void run(const G3MContext* context){

	}
};
//Dummy-Downloader
class DummyDownloader :public IDownloader{
	void initialize(const G3MContext* context, FrameTasksExecutor* frameTasksExecutor){}
	void onResume(const G3MContext* context){}
	void onPause(const G3MContext* context){}
	void onDestroy(const G3MContext* context){}
	void start(){}
	void stop(){}
	long long requestBuffer(const URL& url,
		long long priority,
		const TimeInterval& timeToCache,
		bool readExpired,
		IBufferDownloadListener* listener,
		bool deleteListener){
		return NULL;
	}
	long long requestImage(const URL& url,
		long long priority,
		const TimeInterval& timeToCache,
		bool readExpired,
		IImageDownloadListener* listener,
		bool deleteListener){
		return NULL;
	}
	void cancelRequest(long long requestId){}
	const std::string statistics(){
		return NULL;
	}
};
//Ende Testing
//////////////////////////////////////////



ref class G3MView sealed : public Windows::ApplicationModel::Core::IFrameworkView
{
	D3DRenderer* renderer;
	G3MWidget_win8* _widget;
public:
	

	//Inherited from IFrameWorkView. A method for app view initialization, which is called when an app object is launched.
	virtual void Initialize(Windows::ApplicationModel::Core::CoreApplicationView^ view){
		view->Activated += ref new Windows::Foundation::TypedEventHandler<Windows::ApplicationModel::Core::CoreApplicationView ^, Windows::ApplicationModel::Activation::IActivatedEventArgs^ >(this, &G3MView::OnActivated);
	};

	//Inherited from IFrameWorkView. A method that loads or activates any external resources used by the app view before Run is called.
	virtual void Load(Platform::String^ entryPoint){};

	//Inherited from IFrameWorkView. The actual application
	virtual void Run(){
		
		while (true) {
			if (renderer->isInitialized()){
				renderer->render();
			}
			
		} //nasty way to create a game loop
	};

	//Inherited from IFrameWorkView. A method that sets the current window for the app object's view. Also, UI-listeners (keyboard, mouse, touch) are declared here.
	virtual void SetWindow(Windows::UI::Core::CoreWindow^ window){};

	//Inherited from IFrameWorkView. A method that uninitializes the app view. A good place to free ressources.
	virtual void Uninitialize(){};

	void OnActivated(Windows::ApplicationModel::Core::CoreApplicationView^ view, Windows::ApplicationModel::Activation::IActivatedEventArgs^ args){
		Windows::UI::Core::CoreWindow::GetForCurrentThread()->Activate();

		G3MBuilder_win8* _builder = new G3MBuilder_win8();
		//_builder->setInitializationTask(new RueditaTask(), TRUE);
		//_builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
		//PlanetRendererBuilder* pbuilder = _builder->
		//_builder->setDownloader(new DummyDownloader());
		_builder->getPlanetRendererBuilder()->setLayerSet(createLayerset());
		_builder->getPlanetRendererBuilder()->setForceFirstLevelTilesRenderOnStart(false);

		_widget = _builder->createWidget();
		renderer = _widget->getRenderer();
	}

	////////////////////////////////
	///Testing
private:
	LayerSet* createLayerset(){
		LayerSet* ls = new LayerSet();

		WMSLayer* layer = new WMSLayer("bmng200405", URL::nullURL(), WMSServerVersion::WMS_1_1_0,
			Sector::fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0, 1),
			TimeInterval::fromDays(30), true);

		ls->addLayer(layer);
		return ls;
	}
	/////////////////////////////
	//End Testing





};

ref class G3MViewSource sealed : public Windows::ApplicationModel::Core::IFrameworkViewSource
{
public:
	virtual Windows::ApplicationModel::Core::IFrameworkView^ CreateView(){
		return ref new G3MView();
	};
};


// Main.cpp
// main entry point for application
[Platform::MTAThread]
int main(Platform::Array<Platform::String^>^) {
	Windows::ApplicationModel::Core::CoreApplication::Run(ref new G3MViewSource());
	return 0;

};