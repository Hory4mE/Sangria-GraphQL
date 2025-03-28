import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.util.Timeout
import sangria.execution.Executor
import sangria.marshalling.circe._
import io.circe.syntax._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import sangria.parser.QueryParser
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{complete, entity, path, post}
import io.circe.Json
import io.circe.parser._

object Server extends App {
  implicit val system: ActorSystem = ActorSystem("graphql-server")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = Materializer(system)
  implicit val timeout: Timeout = Timeout(5.seconds) // ตั้ง timeout

  val route: Route = path("graphql") {
     post {
    entity(as[String]) { queryJsonString =>
      parse(queryJsonString) match {
        case Right(json) =>
          json.hcursor.get[String]("query") match {
            case Right(query) =>
              QueryParser.parse(query) match {
                case scala.util.Success(queryAst) =>
                  complete {
                    Executor.execute(GraphQLSchema.schema, queryAst)
                      .map(_.asJson.noSpaces)
                  }
                case scala.util.Failure(error) =>
                  complete(StatusCodes.BadRequest, s"Invalid Query Syntax: ${error.getMessage}")
              }
            case Left(_) => complete(StatusCodes.BadRequest, "Missing 'query' field in JSON")
          }
        case Left(_) => complete(StatusCodes.BadRequest, "Invalid JSON format")
      }
    }
  }
  }

  Http().newServerAt("localhost", 8080).bind(route)
  println("GraphQL Server running at http://localhost:8080/graphql")
}
