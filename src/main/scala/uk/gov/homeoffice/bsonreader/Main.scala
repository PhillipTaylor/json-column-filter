package uk.gov.homeoffice.bsonreader

import cats.data.*
import cats.effect.*
import cats.effect.implicits.{*, given}
import cats.effect.unsafe.implicits.global
import cats.implicits.{*, given}

import com.typesafe.config.*

import fs2.io.file.*
import java.nio.file.Paths
import scala.jdk.CollectionConverters._

import _root_.io.circe.*
import _root_.io.circe.syntax.*
import _root_.io.circe.parser.*
import scala.util.*
import scopt.OParser

object MainApp extends IOApp:

  def columnPrinter(textLine :String, columnList :List[String]) :IO[Unit] = {
    parse(textLine) match {
      case Left(_) => IO(())
      case Right(json) if json.asObject.isDefined =>
        val jsObj :JsonObject = json.asObject.get
        val newObj = JsonObject.fromIterable(
          jsObj
          .toIterable
          .filter { case (k, v) => columnList.contains(k) }
        )

        IO.println(newObj.asJson.noSpaces)
      case Right(_) => IO(())
    }
  }

  def run(args: List[String]): IO[ExitCode] = {
    val inputFile = args(0)
    val columnListFile = args(1)

    val columnList :List[String] = java.nio.file.Files.readAllLines(Paths.get(columnListFile)).asScala.toList

    readAll[IO](Paths.get(inputFile), 4096)
     .through(fs2.text.utf8Decode)
     .through(fs2.text.lines)
     .evalTap(textLine => columnPrinter(textLine, columnList))
     .compile
     .drain
     .as(ExitCode(0))
  }

