import akka.actor.{Actor, Props, ActorRef}
import scala.collection.mutable.ListBuffer
import scala.util.Random


object UProtocol {
  case class createDepartment(name: String)
  case class UResponse(actorRef: ActorRef)
}

class University extends Actor {
  import UProtocol._
  import DProtocol._
  var departments = ListBuffer[ActorRef]()

  override def receive: Receive = {
    case  createDepartment(name) =>
      var department = context.actorOf(Props(classOf[Department], name), name+"Actor")
//      println()
//      context.children.foreach(println)
      department ! createCourse(Random.nextInt(10), name)
      departments ++= List(department)
      sender ! department
    case _ =>
      println("Class University")
  }
}