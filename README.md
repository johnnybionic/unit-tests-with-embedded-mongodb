"# unit-tests-with-embedded-mongodb" 

This project is a spin-off from a Coffee Shop app (see my other github projects), which itself was based on a MongoDB presentation here:

https://www.infoq.com/presentations/demo-java-javascript-mongodb

One of the issues with the demo app (and, keep in mind, it was a demo written in an hour), 
was that it required a running instance of MongoDB. That's fine for a deployed app, but not
so good for unit tests. 

As always, there's various ways round that. One way is to fake the database (e.g. fongo),
another is to launch an instance of MongoDB when the tests run. That's what this project demonstrates. 

See "profile-problems.txt" for some issues encountered along the way!

The project was developed entirely using unit tests, from the bottom up. As the controller was the last unit to
be written, the app wasn't even started as a Spring Boot app until very late in the process. I know that's not
normal practise, but it's interesting to see how much progress can be made just with tests.

Running the app
---------------

- without any parameters for local database, using the MongoDB driver directly
- '-Dspring.profiles.active=production' to use remote database (application-production.yaml) and MongoDB driver
- '-Dspring.profiles.active=mongotemplate' to use local database, using the MongoTemplate