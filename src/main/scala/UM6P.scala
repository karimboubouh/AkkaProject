import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await


object UM6P extends App {
  import UProtocol._
  implicit val timeout = Timeout(10 seconds)

  var departments = ListBuffer[ActorRef]()

  var as = ActorSystem("UM6PApp")
  val ua = as.actorOf(Props[University], "UniversityActor")
  println(ua.path)
//  ua ! createDepartment("Informatics")
//
//

  val uFuture1 =
  val result = Await.result(uFuture1, timeout.duration).asInstanceOf[ActorRef]
  println(result)

  val fagent = for {
    x <- ua ? createDepartment("Informatics")
    y <- ua ? createDepartment("Physics")
    z <- ua ? createDepartment("Chemistry")
  } yield (x + y)

}
