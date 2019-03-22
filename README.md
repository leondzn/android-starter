# Android Starter
A starter template for Android app project that implements Clean Architecture.
Dagger2 is used for dependency injection.
Base classes for implementing MVVM are also included in this starter project although you are free to implement
other design patterns if needed.

Prior knowledge with Clean Architecture is required to use this starter project.

Following the Clean Architecture guidelines, the project is divided
into 3 modules: *domain, data, and app (presentation)*

For further reading about [Clean Architecture](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## Setup
* Rename packages from `com.snekbyte.starter` to a name of your choice. Make sure the changes are
  applied to all three layers (domain, data, app)
* Change the `package` attribute on the *AndroidManifest.xml* file.
  This should not be exactly the same as the app layer's package attribute
  or else the project will build but will fail to resolve the R class properly.
* Change the `applicationId` property on *app/build.gradle*

## Usage
* Use Cases can be created by implementing the UseCase interface
* Dependencies are managed inside the *dependencies.gradle* file for easier tracking of
  libraries across multiple modules
  
  
## TODO:
- Make a demo app
- More detailed guide
