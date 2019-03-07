import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._


object CProtocol {

  case class showCourse()

  case class joinToCourse(ar: ActorRef)

  case class Response(status: Int)

}

class Course(id: Int, name: String) extends Actor {

  import CProtocol._
  import DProtocol._

  implicit val timeout = Timeout(10 seconds)

  var students = ListBuffer[ActorRef]()
  val MAX = 10

  override def receive: Receive = {
    case showCourse() =>
      var courseName = "Course <" + id + ", " + name + ">"
      println(courseName + ", My parent : " + sender.path)
    case joinToCourse(ar) =>
      val f = context.parent ? studentAssociated(ar)
      val allowed = Await.result(f, timeout.duration).asInstanceOf[Boolean]
      if (allowed && students.size <= MAX) {
        students += ar
        sender ! "Student " + ar.path + " joined the course " + self.path
      } else {
        sender ! "Student " + ar.path + " can't join the course " + self.path
      }
    case _ =>
      println("Class Course")
  }
}