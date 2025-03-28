import sangria.schema._
import scala.concurrent.{Future, ExecutionContext}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import io.circe.generic.auto._
import io.circe.parser._

case class User(id: Int, name: String, email: String)

object GraphQLSchema {
    implicit val system: ActorSystem = ActorSystem("graphql-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher

    val UserType = ObjectType(
        "User",
        fields[Unit, User](
            Field("id", IntType, resolve = _.value.id),
            Field("name", StringType, resolve = _.value.name),
            Field("email", StringType, resolve = _.value.email)
        )
    )

    val QueryType = ObjectType("Query", fields[Unit, Unit](
        Field("user", OptionType(UserType),
            arguments = Argument("id", IntType) :: Nil,
            resolve = c => fetchUser(c.arg[Int]("id"))
        )
    ))

    val schema = Schema(QueryType)

    def fetchUser(id: Int): Future[Option[User]] = {
        val url = s"http://localhost:5118/api/user/$id"
        Http().singleRequest(HttpRequest(uri = url)).flatMap { response =>
            Unmarshal(response.entity).to[String].map { jsonString =>
                decode[User](jsonString).toOption
            }
        }
    }
}
