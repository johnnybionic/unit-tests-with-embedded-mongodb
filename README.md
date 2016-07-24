"# unit-tests-with-embedded-mongodb" 

This project is a spin-off from a Coffee Shop app (see my other github projects), which itself was based on a MongoDB presentation here:

https://www.infoq.com/presentations/demo-java-javascript-mongodb

One of the issues with the demo app (and, keep in mind, it was a demo written in an hour), 
was that it required a running instance of MongoDB. That's fine for a deployed app, but not
so good for unit tests.

As always, there's various ways round that. One way is to fake the database (e.g. fongo),
another is to launch an instance of MongoDB when the tests run. That's what this project demonstrates. 

I'm up to step 2 in the list given in "development-process.text"