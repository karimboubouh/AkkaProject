import akka.actor.{Actor, ActorRef, Props}
import scala.collection.mutable.ListBuffer

object DProtocol {
  case class createCourse(id: Int, name: String)
  case class Response(status: Int)
}

class Department(dName: String) extends Actor {
  import DProtocol._
  import CProtocol._
  var courses = ListBuffer[ActorRef]()

  override def receive: Receive = {
    case createCourse(id, name) =>
      val course = context.actorOf(Props(classOf[Course], id, name), "Course" + id + "Actor")
      courses ++= List(course)
      course ! showCourse()
    case _ =>
      println("Class Department")
  }
}