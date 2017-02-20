package com.marionete.pi

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by agapito on 20/12/2016.
  */
class Master extends Actor {

  // Me
  val address = "akka.tcp://SlavePi@192.168.255.92:5150/user/SlaveDoFilipe"

  val slaveList = 1.to(100).map(n => context.actorSelection(address + n.toString)).toList
  println(s"slaveList = $slaveList")

  def receive = {
    case MsgStartPi(pi) =>
      println(s"Sending message MsgCalcPi($pi)")
      for( slave <- slaveList){
        slave ! MsgCalcPi(pi)
      }
    case MsgPi(pi) =>
      println(s"pi = $pi")
  }
}

object Main extends App {
  val system = ActorSystem("Pi")
  val master = system.actorOf(Props[Master], name="MasterDoFilipe")

  val nActors = 100
  master ! MsgStartPi(nActors)

}

@SerialVersionUID(1L)
sealed trait Msg
@SerialVersionUID(1L)
case class MsgCalcPi(n:Int) extends Msg
@SerialVersionUID(1L)
case class MsgPi(pi:Double) extends Msg
@SerialVersionUID(1L)
case class MsgStartPi(n:Int) extends Msg
