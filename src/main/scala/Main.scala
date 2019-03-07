import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Main extends App {

  import DProtocol._
  import UProtocol._
  import CProtocol._

  implicit val timeout = Timeout(10 seconds)

  var departments = List[ActorRef]()
  var students = List[ActorRef]()

  var as = ActorSystem("UM6PApp")
  val ua = as.actorOf(Props[University], "UniversityActor")

  // create departments
  val df = for {
    x <- ua ? createDepartment("Informatics")
    y <- ua ? createDepartment("Physics")
    z <- ua ? createDepartment("Chemistry")
  } yield List(x, y, z)
  departments = departments ++ Await.result(df, timeout.duration).asInstanceOf[List[ActorRef]]
  println(departments.length + " departments created.")

  println()

  // create students
  val sf = for {
    x <- ua ? registerStudent("Abdelhak")
    y <- ua ? registerStudent("Karim")
    z <- ua ? registerStudent("Younuss")
  } yield List(x, y, z)
  students = students ++ Await.result(sf, timeout.duration).asInstanceOf[List[ActorRef]]
  println(students.length + " students registered to university " + ua.path)

  println()

  // associate student Abdelhak/Karim to department Informatics
  var af = departments(0) ? associateStudent(students(0))
  println(Await.result(af, timeout.duration).asInstanceOf[String])
  var af1 = departments(0) ? associateStudent(students(1))
  println(Await.result(af1, timeout.duration).asInstanceOf[String])

  println()

  // get Computer science courses
  var cf = departments(0) ? "getCourses"
  var courses = Await.result(cf, timeout.duration).asInstanceOf[ListBuffer[ActorRef]]

  // associate student Abdelhak to the course Course{id}Actor
  var jf = courses(0) ? joinToCourse(students(0))
  println(Await.result(jf, timeout.duration).asInstanceOf[String])


}
