package org.specs2.matcher

import cats.MonadError
import cats.implicits._
import org.http4s._
import org.http4s.headers.{`Content-Encoding`, `Content-Type`}

trait Http4sMatchers[F[_]] extends Matchers with RunTimedMatchers[F] {

  def haveStatus(expected: Status): Matcher[Response[F]] =
    be_===(expected) ^^ { r: Response[F] => r.status.aka("the response status") }

  def returnStatus(s: Status): Matcher[F[Response[F]]] =
    returnValue(haveStatus(s)) ^^ { (m: F[Response[F]]) => m.aka("the returned response status") }

  def haveBody[A](a: ValueCheck[A])(
      implicit F: MonadError[F, Throwable],
      ee: EntityDecoder[F, A]
  ): Matcher[Message[F]] =
    returnValue(a) ^^ { (m: Message[F]) => m.as[A].aka("the message body") }

  def returnBody[A](a: ValueCheck[A])(
      implicit F: MonadError[F, Throwable],
      ee: EntityDecoder[F, A]
  ): Matcher[F[Message[F]]] =
    returnValue(a) ^^ { (m: F[Message[F]]) => m.flatMap(_.as[A]).aka("the returned message body") }

  def haveHeaders(hs: Headers): Matcher[Message[F]] =
    be_===(hs) ^^ { (m: Message[F]) => m.headers.aka("the headers") }

  def returnHeaders(hs: Headers): Matcher[F[Message[F]]] =
    returnValue(haveHeaders(hs)) ^^ { (m: F[Message[F]]) => m.aka("the returned headers")}

  def containHeader[H](hs: H)(implicit H: Header.Select[H]): Matcher[Message[F]] =
    beSome(hs) ^^ { (m: Message[F]) =>
      m.headers.get[H](H).asInstanceOf[Option[H]].aka("the particular header")
    }

  def returnContainingHeader[H](hs: H)(implicit H: Header.Select[H]): Matcher[F[Message[F]]] =
    returnValue(containHeader(hs)) ^^ { (m: F[Message[F]]) =>
      m.aka("the returned particular header")
    }

  def haveMediaType(mt: MediaType): Matcher[Message[F]] =
    beSome(mt) ^^ { (m: Message[F]) =>
      m.headers.get[`Content-Type`].map(_.mediaType).aka("the media type header")
    }

  def returnMediaType(mt: MediaType): Matcher[F[Message[F]]] =
    returnValue(haveMediaType(mt)) ^^ { (m: F[Message[F]]) =>
      m.aka("the returned media type header")
    }

  def haveContentCoding(c: ContentCoding): Matcher[Message[F]] =
    beSome(c) ^^ { (m: Message[F]) =>
      m.headers.get[`Content-Encoding`].map(_.contentCoding).aka("the content encoding header")
    }

  def returnContentCoding(c: ContentCoding): Matcher[F[Message[F]]] =
    returnValue(haveContentCoding(c)) ^^ { (m: F[Message[F]]) =>
      m.aka("the returned content encoding header")
    }

}
