*Please note*: Presentable is a work in progress and there may be some code flux at this time.
Most of the fundamentals have been setup however.


# Presentable
Presentable is a high level framework to implement a Model View Presenter (MVP) and Clean inspired architecture for Java, Android and iOS applications.

Presentable aims to achieve this by producing reusable and translatable domain model and presentation layer code across a variety of platforms, with Java as the base language.

This tool weighs up the learnings from cross platform tools like Xamarin.

TODO

## Concepts
TODO

### Strong contracts between layers
TODO

Interface between UI <--> Presenter <--> InterActor


### High level lifecycle binding

There are only 2 lifecycle events exposed at the Presenter layer from the UI: binding and unbinding.
These events are intentionally generalised and as bare bones as possible.
It's too complex to shoehorn different UI components and platforms into following the same lifecycle.


## Framework and design decisions


### Contract
This is a conglomeration of all things required to represent a UI component or a screen.
This includes 'base' interfaces for all components:
 - User Interface (UI)
 - Presenter
 - InterActors


### UI
TODO

### Presenter
TODO

### InterActor

## Naming choices
When using generalised naming, there can be some confusion and some lost in translation moments.
Here's a breakdown of some naming choices


### InterActor vs Interactor
This is a clean interactor.
Decomposing the words an 'inter-actor' sits in between two components.
I've found that this term is generally confusing for developers new to or unfamiliar with clean architecture and this naming.
Hence the naming `InterActor`

### Repositories
It stores and retrieves something.
This is inclusive of APIs, epiphermal stores, etc. and not just databases.


## Modules

### presentable
The core, pure java implementation of the framework

### presentable-replayable
TODO

### presentable-android
Android specific implementations

### presentable-ios?
To be implemented???


## Inspirations
The framework takes inspiration from and is based on
 - Model View Presenter (MVP) architecture
 - Clean architecture
 - Square coordinators (https://github.com/square/coordinators) general purpose binding
 - Google/Square Dagger (https://github.com/google/dagger) and Jake Wharton's ButterKnife (https://github.com/JakeWharton/butterknife) Code generation
 - Dagger Non-configuration scope
 - Commander Pattern