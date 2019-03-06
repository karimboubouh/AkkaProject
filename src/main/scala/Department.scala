import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._


object DProtocol {

  case class createCourse(id: Int, name: String)

  case class associateStudent(ar: ActorRef)

  case class Response(status: Int)

}

class Department(dName: String) extends Actor {

  import CProtocol._
  import DProtocol._
  import UProtocol._

  implicit val timeout = Timeout(10 seconds)

  var courses = ListBuffer[ActorRef]()
  var students = ListBuffer[ActorRef]()

  override def receive: Receive = {
    case createCourse(id, name) =>
      val course = context.actorOf(Props(classOf[Course], id, name), "Course" + id + "Actor")
      courses ++= List(course)
      course ! showCourse()
    case associateStudent(ar) =>
      val f = context.parent ? studentRegistred(ar)
      val registered = Await.result(f, timeout.duration).asInstanceOf[Boolean]
      if (registered) {
        sender ! "Student " + ar.path + " associated to " + self.path
        students ++= List(ar)
      } else {
        sender ! "Student " + ar.path + " not registered into " + context.parent.path
      }


    case _ =>
      println("Class Department")
  }
}