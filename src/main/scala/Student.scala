import akka.actor.Actor

class Student(name: String) extends Actor{

  override def receive: Receive = {
    case _ =>
      println("Class Student")
  }
}
