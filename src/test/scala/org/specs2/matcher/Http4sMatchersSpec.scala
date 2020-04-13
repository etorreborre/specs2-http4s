package org.specs2.matcher

import cats.effect.IO
import org.http4s._
import org.http4s.headers.{Accept, Host, `Content-Encoding`, `Content-Type`}
import org.specs2.Spec


class Http4sMatchersSpec extends Spec with Http4sMatchers[IO] with IOMatchers { def is = s2"""
    
    haveStatus checks that a Response's HTTP status is the expected value
    ${Response[IO](Status.Ok) must haveStatus(Status.Ok)}
    ${Response[IO](Status.NotFound) must not(haveStatus(Status.Ok))}

    returnStatus checks that an effectful Response's Http status is the expected value
    ${IO.pure(Response[IO](Status.Ok)) must returnStatus(Status.Ok)}
    ${IO.pure(Response[IO](Status.NotFound)) must not(returnStatus(Status.Ok))}

    haveBody checks that the body of a Message decodes to the expected value
    ${Request[IO](method = Method.POST).withEntity("abc") must haveBody("abc")}
    ${Response[IO]().withEntity("def") must not(haveBody("abc"))}

    returnBody checks that the body of an effectful Message decodes to the expected value
    ${IO.pure(Response[IO]().withEntity("abc")) must returnBody("abc")}
    ${IO.pure(Response[IO]().withEntity("def")) must not(returnBody("abc"))}

    haveHeaders checks that the Message's headers equal the expected Headers
    ${Request[IO]().putHeaders(Host("specs2.org")) must haveHeaders(Headers.of(Host("specs2.org")))}
    ${Request[IO]().putHeaders(Host("specs2.org")) must not(haveHeaders(Headers.of(
      Host("specs2.org"),
      Accept(MediaType.text.html)
    )))}

    returnHeaders checks that the effectful Message's headers equal the expected Headers
    ${IO.pure(Request[IO]().putHeaders(Host("specs2.org"))) must
      returnHeaders(Headers.of(Host("specs2.org")))}
    ${IO.pure(Request[IO]().putHeaders(Host("specs2.org"))) must not(returnHeaders(Headers.of(
      Host("specs2.org"),
      Accept(MediaType.text.html)
    )))}

    containHeader checks that the Message contains the expected header
    ${Request[IO]().putHeaders(Host("specs2.org")) must containHeader(Host("specs2.org"))}
    ${Request[IO]().putHeaders(Host("specs2.org")) must not(containHeader(Host("example.com")))}
    ${Request[IO]().putHeaders(Host("specs2.org"), Accept(MediaType.text.html)) must containHeader(
      Host("specs2.org"))}

    returnContainingHeader checks that the effectul Message contains the expected header
    ${IO.pure(Request[IO]().putHeaders(Host("specs2.org"))) must
      returnContainingHeader(Host("specs2.org"))}
    ${IO.pure(Request[IO]().putHeaders(Host("specs2.org"))) must
      not(returnContainingHeader(Host("example.com")))}
    ${IO.pure(Request[IO]().putHeaders(Host("specs2.org"), Accept(MediaType.text.html))) must
      returnContainingHeader(Host("specs2.org"))}

    haveMediaType checks that the Message has a Content-Type header with the expected MediaType
    ${Request[IO](method = Method.POST).putHeaders(`Content-Type`(MediaType.text.html)) must
      haveMediaType(MediaType.text.html)}
    ${Request[IO](method = Method.POST).putHeaders(`Content-Type`(MediaType.text.html,
      Charset.`UTF-8`)) must haveMediaType(MediaType.text.html)}
    ${Request[IO](method = Method.POST).putHeaders(`Content-Type`(MediaType.text.html)) must
      not(haveMediaType(MediaType.application.json))}

    returnMediaType checks that the effectful Message has a Content-Type header with the expected
    MediaType
    ${IO.pure(Request[IO](method = Method.POST).putHeaders(`Content-Type`(MediaType.text.html))) must
      returnMediaType(MediaType.text.html)}
    ${IO.pure(Request[IO](method = Method.POST).putHeaders(`Content-Type`(MediaType.text.html,
      Charset.`UTF-8`))) must returnMediaType(MediaType.text.html)}
    ${IO.pure(Request[IO](method = Method.POST).putHeaders(`Content-Type`(MediaType.text.html))) must
      not(returnMediaType(MediaType.application.json))}

    haveContentCoding checks that the Message has a Content-Encoding header with the expected
    ContentCoding
    ${Request[IO](method = Method.POST).putHeaders(`Content-Encoding`(ContentCoding.gzip)) must
      haveContentCoding(ContentCoding.gzip)}
    ${Request[IO](method = Method.POST).putHeaders(`Content-Encoding`(ContentCoding.gzip)) must
      not(haveContentCoding(ContentCoding.zstd))}

    returnContentCoding checks that the effectful Message has a Content-Encoding header with the
    expected ContentCoding
    ${IO.pure(Request[IO](method = Method.POST).putHeaders(`Content-Encoding`(ContentCoding.gzip))) must
      returnContentCoding(ContentCoding.gzip)}
    ${IO.pure(Request[IO](method = Method.POST).putHeaders(`Content-Encoding`(ContentCoding.gzip))) must
      not(returnContentCoding(ContentCoding.zstd))}

  """

}
