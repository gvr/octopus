# octopus
## demo to create docker container with sbt
Usage:

* Clone the project
* Have the docker daemon running.
* Run `sbt docker`
* Note the docker image-id in the output
* Run `docker run -dit -p 8080:8080 <image-id>`
* Note the container-id in the output (or use `docker ps`)
* Use `docker logs <container-id>` to check if the application started
* Do a GET /hello on localhost:8080 with your favorite tool (*e.g.* with HTTPie: http -v GET http://localhost:8080/hello)
