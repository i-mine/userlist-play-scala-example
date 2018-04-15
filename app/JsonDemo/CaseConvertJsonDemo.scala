package JsonDemo
import play.api.libs.json._
/**
  * author: dulei
  * date: 18-4-3
  * desc: covert case class to a Json String
  */
case class Address(province: String, city: String)
case class Person(name: String, emails: List[String], address: Address)
object CaseParseJsonDemo extends App{
  implicit val addressFormat = Json.format[Address]
  implicit val personFormat = Json.format[Person]
  val person = Person("dulei", List("lei.du@mobvista.com"), Address("shanxi", "taiyuan"))
  val json = Json.toJson[Person](person)
  println(json.toString())
}
