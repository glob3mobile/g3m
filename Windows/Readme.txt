Windows 8.1 / Windows Phone 8.1 Version of G3M

Requirements:
- Windows 8.1
- Visual Studio 2013 with Update 2 or later (should include D3D 11.2 SDK and Win Phone 8.1 SDK)

Build and Run:
- Check out the "Windows" branch from GIT-Repository. You should now have a "Windows" subfolder in the G3M repository.
- In Visual Studio, load the solution "G3MWindows8App" from G3M/Windows/G3MWindows8App. This solution contains two universal projects, G3MWindows8App and G3MWindowsSDK. The first one is a Windows 8.1/Windows Phone 8.1 app, the second compiles to a static lib

Additional settings/dependencies:
- Shader reflection:
	- Right-click on G3MWindowsSDK (Windows 8.1) and select Properties->Librarian->Additional Dependencies. Add windowscodecs.lib to the list. Repeat the same step for G3MWindowsSDK (Windows Phone 8.1)

- SQLite:
	In Visual Studio-> Tools-> NuGet Package Manager -> Manage NuGet Packages for solution, find and install:
	- SQLite for Windows Runtime (Windows 8.1)
	- SQLite (should also install SQlite redistributable)
	Now check in Tools->Extensions and updates. Both packages should appear in the list of installed sdks. If not, click on "online" in the left hand side of the window, find the two packages and install them.
	Now SQLite has to added as reference to both G3MWindowsSDK projects. To do so:
	- Right-click on WindowsSDK (Windows 8.1)->properties->Common Properties->Add new reference. Select SQlite for Windows Runtime 8.1
	- Right-click on WindowsSDK (Windows Phone 8.1)->properties->Common Properties->Add new reference. Select SQlite for Windows Phone 8.1
	Add dependencies for SQLite:
	- Right-click on G3MWindowsSDK (Windows 8.1) and select Properties->Librarian->Additional Dependencies. Add sqlite3.lib to the list. Repeat the same step for G3MWindowsSDK (Windows Phone 8.1)
	You should now be able to build and run G3MWindows8App (Windows 8.1). The Windows Phone version has not been tested.
	
Current State of development:
- Downloader, BusyMeshRenderer, and PlanetRenderer work (the last one only to some degree). There still are problems when mapping the textures to the globe, as it appears that the geometry is not drawn correctly.
- JPEG textures are not drawn correctly.
- Loading and using shaders according to features set by renderers works, however only some basic shaders have been translated to HLSL (ColorMesh, NoColorMesh, TexturedMesh, TransformedTexCoorTexturedMesh). When porting shaders, make sure to name them the same way as the GLSL shaders. As HLSL does not distinguish names between vertex and pixel shaders, add an upper case V or P to the name (e.g. ColorMeshV.hlsl and ColorMeshP.hlsl).
- There is no UI yet, the globe cannot be moved with the mouse.

