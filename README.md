**Please note**: Presentable is a work in progress and there may be some code flux at this time.
Most of the fundamentals have been setup and the tool is in a usable state.


# Presentable
Presentable is a high level framework to implement a Model View Presenter (MVP) and Clean inspired architecture for Java, Android and iOS applications.

Presentable aims to achieve this by producing reusable and translatable domain models, core functionality and business logic across a variety of platforms, with Java as the base language.

This tool weighs up the learnings from cross platform tools like Xamarin.

## Concepts

### Strong contracts between layers
Interface between UI <--> Presenter <--> InterActor

Developers can make the choice to replace the `InterActor` with a `Model` and follow MVP strictly.
It is your choice though.


### High level lifecycle binding

There are only 2 lifecycle events exposed to the Presentation layer: binding and unbinding.
These events are intentionally generalised and are as simple as possible.
This prevents shoehorning different lifecycles provided by different UI components and platforms.
If there are other lifecycle events that need to be used, these can simply be exposed as a method through your contracts. 


## Framework and design decisions


### Contract
An interface that provides all contracts between application layers for your UI component or screen.
This keeps all interfaces between application layers in a single place.
Base interfaces have been provided for all layers.


### Ui
Info coming soon

### Presenter
Info coming soon

### InterActor
Info coming soon

## Naming choices
Naming is hard.
Quite often things are lost in translation when there's different understandings of words.
Here's a breakdown of some naming choices:


### InterActor vs Interactor
An 'inter-actor' sits in between two components, as with the Clean architecture interactor.
The term can be confused and the intentional uppercasing of the 'A' in actor helps emphasise the meaning.

### Repositories
It stores and retrieves something.
This is not restricted to databases, and can include data provided by APIs, epiphermal stores, preference stores, etc.


## Modules

### presentable
The core, pure java implementation of the framework

### presentable-replayable
Info coming soon

### presentable-android
Android specific implementations

### presentable-ios?
Info coming soon, to be developed


## Replayable code generation
Info coming soon


## Inspirations
The framework is inspired by and based on:

* Model View Presenter (MVP) architecture
* (Uncle Bob's) Clean architecture
* The Mysterious CJ (you know who you are! Thanks for your feedback and mentoring)
* [Square Coordinators](https://github.com/square/coordinators) general purpose binding
* [Google/Square Dagger](https://github.com/google/dagger) and [Jake Wharton's ButterKnife](https://github.com/JakeWharton/butterknife) Code generation
* Dagger Non-configuration scope
* Commander Pattern