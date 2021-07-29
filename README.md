
# specs2-http4s

specs2-http4s is a library of additional [specs2](https://etorreborre.github.io/specs2/) matchers for making test
assertions on [http4s](https://http4s.org/) data types.

## Usage

Add the library to your project's dependencies in its `build.sbt` file:

```sbt
libraryDependencies += "org.specs2" %% "specs2-http4s" % <latest version> % "test"
```

(or equivalent if you are not using sbt)

#### Available versions

* `1.1.x` is compatible with specs2 `4.x.x` and http4s `1.x`
* `1.0.x` is compatible with specs2 `4.x.x` and http4s `0.21.x`

#### Example usage

```scala
import cats.effect.IO
import io.circe.Json
import io.circe.literal._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.specs2.matcher.{Http4sMatchers, IOMatchers}
import org.specs2.mutable.Specification

class Http4sExamples extends Specification with Http4sMatchers[IO] with IOMatchers {

  private val exampleService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" => Ok("Hello, world!")

    case request @ POST -> Root / "echo-json" =>
      request.as[Json].flatMap(json => Ok(json))
  }

  "sending GET to /hello should return Ok with \"Hello, world!\"" in {
    val request = Request[IO](GET, uri"/hello")

    // `return*` matchers deal with `F[Message[F]]`s
    exampleService.orNotFound(request) must returnStatus(Ok)
    exampleService.orNotFound(request) must returnBody("Hello, world!")

    // `have*` matchers work with pure `Message[F]`s
    exampleService.orNotFound(request) must returnValue { (response: Response[IO]) =>
      response must haveStatus(Ok)
      response must haveBody("Hello, world!")
    }
  }

  "sending POST with JSON to /echo-json should return Ok with the same JSON" in {
    val exampleJson = json"""{ "foo": 1, "bar": true }"""
    val request = Request[IO](POST, uri"/echo-json")
      .withEntity(exampleJson)

    /* `returnBody` and `haveBody` work for any type `A` for which there' an
    `EntityDecoder[F, A]` in scope. In this case, we're using one for `Json` from
    http4s-circe */
    exampleService.orNotFound(request) must returnBody(exampleJson)
  }
}
```
