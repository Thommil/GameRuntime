# GameRuntime v0.1

Custom port/extension/framework based on GDX for 2D gaming with strong Physics support.

Based on LibGDX 1.8, Box2D 2.3.2 and LiquidFun 1.10

##Features :
* LiquidFun extension replacing Box2D (https://github.com/Thommil/gdx-liquidfun)
* Asynchronous physics/rendering loops
* Scene/Layers/Actors framework
* Dedicated Batch, Cache and Actors for simple texture/sprite rendering
* Full Profiler (Render, Physics, OpenGL, Memory)
* Full tests suite
* No HTML5/GWT Support

##Installation :
No installation needed, it's a standard LibGDX project with no extension. 

Gradle is your friend here : https://github.com/libgdx/libgdx/wiki/Project-Setup-Gradle

Screens Ratio and textures reminder :
	- preferred texture size : 1024x1024
	- Max image size :  
		-> 16/10 : 1024x640 (full zone)
		-> 16/9  : 1024x570
		-> 4/3   : 860x640
		-> 3/2   : 860x570 (safe zone)
	- Config :
		- Background : 1024x640 -> play only on centered 860x570
		- Space left for sprites : 1024x384
		- Viewport : 16nx10n (FILL)