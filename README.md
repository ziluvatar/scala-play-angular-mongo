Vessels Management Tool
=================================

Focus
---------------------------

### Code readability

* In server side, separation between controller and persistence layer make the classes/objects/methods shorter.
* In client side, separate files by view, javascript and template
* Partial RESTful api. POST and CREATE methods don't return the object as well, because we don't need it, 
with that the code in the controller is nicer without any lack of functionality.

### Website usability

* Keeping in mind the user most of the times will want to create or update a Vessel, the main entry point is
the screen to create a Vessel, but the user can always search or choose a vessel to edit in that page as well.
* Delete operation probably is less needed, that is why the user has to do it through the edit screen instead of
having it in the vessel list.
* Search box. The user can filter by vessel name to find easily which one he wants to edit, this list is updated
automatically every time the user modify/create/delete a vessel.
* Google maps for location. It is pretty handy for adding or modifying a location just clicking the map.
* Error/Info messages. The user gets information about if every operation has been done right or with failures.
* Validation error messages. User can know and learn how the form should be filled with the right information.
* The website is responsive to different screen sizes. It uses internally twitter bootstrap as base.


Technical Decisions
---------------------------
* Avoid to merge the controller with mongo. Even if I have not done any Dependency Injection I have prepared the
separation of controller and persistence layer. So we could mock the persistence layer for testing or use another
different database without change the controller.
* IE8 and below are not fully supported by JQuery version used and angular version.
* The app manages its own id, instead of using the mongodb one (_id). The id in this case is the resource id of the API,
instead of the id of a record in one database. You can migrate to another database easily.

Future improvements
---------------------------
Due to the time restrictions we could go further with some improvements for following versions:

* Add tests for javascript code and fully tests browser with rest service
* Avoid double submit of the form if the user presses twice the button
* Remove info/error messages after few seconds
* Reuse vessel html form common parts
* Give more feedback of the actions to the user: loadings, disabling buttons, etc
* Add server validations
* Manage exceptions. It is only developed the "happy path"
* Add log traces to make the life easier for production support
* Use nicer ids instead of hexadecimal
* Use GeoJson for longitude/latitude data, so that later we could apply spacial queries over it.
* Add Dependency Injection if needed (cake pattern, scaldi, guice, ...)


Useful Information
---------------------------

### Angular
#### API
* https://code.angularjs.org/1.3.3/docs/guide
* https://code.angularjs.org/1.3.3/docs/api

#### Getting Started
* https://docs.angularjs.org/tutorial/step_00
* http://www.ng-newsletter.com/posts/beginner2expert-scopes.html
* http://www.toptal.com/angular-js/a-step-by-step-guide-to-your-first-angularjs-app
* http://www.ng-newsletter.com/posts/validations.html
* http://toddmotto.com/all-about-angulars-emit-broadcast-on-publish-subscribing/

#### Promises
* http://markdalgleish.com/2013/06/using-promises-in-angularjs-views/
* http://weblog.west-wind.com/posts/2014/Oct/24/AngularJs-and-Promises-with-the-http-Service

#### Seeds
* https://github.com/mariussoutier/play-angular-require-seed
* https://github.com/angular/angular-seed
* https://github.com/paulyoder/angular-bootstrap-show-errors
* https://github.com/lashford/modern-web-template

### Play
* https://www.playframework.com/documentation/2.3.x/ScalaHome
* https://www.playframework.com/documentation/2.3.x/ScalaFunctionalTestingWithScalaTest
* https://www.playframework.com/documentation/2.3.x/ScalaDependencyInjection

#### Seeds
* https://github.com/knoldus/playing-reactive-mongo

### MongoDB
* https://github.com/ReactiveMongo/Play-ReactiveMongo
* http://reactivemongo.org/releases/0.10.5/documentation/tutorial/write-documents.html


