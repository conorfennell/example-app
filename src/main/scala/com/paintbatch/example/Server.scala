package com.paintbatch

import com.paintbatch.routes.{HealthcheckRoutesImpl}
import fs2._
import cats.effect._
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.blaze.BlazeServerBuilder

import cats.implicits._
import org.http4s.implicits._

object ExampleAppServer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    ExampleApp.stream[IO].compile.drain.as(ExitCode.Success)
}

object ExampleApp {
  val bindHost = "0.0.0.0"
  val bindPort = 8080

  def httpRoutes[F[_]: ConcurrentEffect]: HttpRoutes[F] =
    new HealthcheckRoutesImpl[F].service()

  def httpApp[F[_]: ConcurrentEffect: ContextShift: Timer]: HttpApp[F] =
    httpRoutes.orNotFound

  def stream[F[_]: ConcurrentEffect: Timer: ContextShift]: Stream[F, ExitCode] = {
    BlazeServerBuilder[F]
      .bindHttp(bindPort, bindHost)
      .withHttpApp(httpApp[F])
      .serve
  }
}