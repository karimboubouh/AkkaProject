import DProtocol.createCourse
import akka.actor.{Actor, Props}

object CProtocol {
  case class showCourse()
  case class Response(status: Int)
}

class Course(id: Int, name: String) extends Actor {
  import CProtocol._
  override def receive: Receive = {
    case showCourse() =>
      var courseName = "Course <" + id + ", " + name + ">"
      println(courseName + ", My parent : " + sender.path)
    case _ =>
      println("Class Course")
  }
}