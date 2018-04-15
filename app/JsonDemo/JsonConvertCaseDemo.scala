package JsonDemo

import play.api.libs.json._

/**
  * author: dulei
  * date: 18-4-2
  * desc: convert JSON object to an User
  */
case class User(username: String, friends: Int, enemies: Int, isAlive: Boolean)

object JsonConvertCaseDemo extends App {

  implicit val userJsonFormat = Json.format[User]
  //json字符串，切记Boolean类型的true,false不要加""
  val jsonString =  """{"username":"pedrorijo91","friends":100,"enemies":10,"isAlive":true}"""
  val jsonObject = Json.parse(jsonString)
  //json to case way1:
  val user = jsonObject.as[User]
  //json to case way2:
//  val user1 = Json.fromJson[User](jsonObject) match {
//    case p: JsSuccess[User] => println(p.value.username)
//    case e: JsError => println("Error: "+e.errors)
//  }
  println(user.isAlive)
}

