#include "D3DRenderer.hpp"
#include "G3MWidget_win8.hpp"
#include "G3MBuilder_win8.hpp"


#include "GInitializationTask.hpp"
class RueditaTask :public GInitializationTask{
	bool isDone(const G3MContext* context){
		return false;
	}

	void run(const G3MContext* context){

	}
};




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
		_builder->setInitializationTask(new RueditaTask(), TRUE);
		_widget = _builder->createWidget();
		renderer = _widget->getRenderer();
	}


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
int main(Platform::Array<Platform::String^>^) {
	Windows::ApplicationModel::Core::CoreApplication::Run(ref new G3MViewSource());
	return 0;

};