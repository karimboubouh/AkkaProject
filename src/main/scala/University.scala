import akka.actor.{Actor, Props, ActorRef}
import scala.collection.mutable.ListBuffer
import scala.util.Random


object UProtocol {
  case class createDepartment(name: String)
  case class UResponse(ar: ActorRef)
  case class registerStudent(name: String)
  case class studentRegistred(actorRef: ActorRef)
}

class University extends Actor {
  import UProtocol._
  import DProtocol._
  var departments = ListBuffer[ActorRef]()
  var students = ListBuffer[ActorRef]()

  override def receive: Receive = {
    case  createDepartment(name) =>
      val department = context.actorOf(Props(classOf[Department], name), name+"Actor")
//      context.children.foreach(println)
      department ! createCourse(Random.nextInt(10), name)
      departments ++= List(department)
      sender ! department
    case registerStudent(name) =>
      val student = context.actorOf(Props(classOf[Student], name), name+"Actor")
      students ++= List(student)
      sender ! student
    case studentRegistred(ar) =>
      sender ! students.contains(ar)
    case _ =>
      println("Class University")
  }
}