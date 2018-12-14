# Android Starter
A starter template for Android app project that implements Clean Architecture using RxJava.
Dagger2 is used for dependency injection and lambdas are enabled with Retrolambda.
You are free to implement any design pattern such as MVP or MVVM.

Prior knowledge with Clean Architecture and RxJava is required to use this starter project.

Following the Clean Architecture guidelines, the project is divided
into 3 modules: *domain, data, and app (presentation)*

For further reading about [Clean Architecture](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## Setup
* Rename packages from `com.leondzn.starter` to a name of your choice. Make sure the changes are
  applied to all three layers (domain, data, app)
* Change the `package` attribute on the *AndroidManifest.xml* file
* Change the `applicationId` property on *app/build.gradle*

## Usage
* Interactors *(or Use Cases)* can be created by implementing the interfaces found in 
  `domain/interactors/types`.
* Dependencies are managed inside the *dependencies.gradle* file for easier tracking of
  libraries across multiple modules
  
  
## TODO:
- Make a demo app
- More detailed guide
