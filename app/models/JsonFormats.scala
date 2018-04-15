package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * author: dulei
  * date: 18-4-7
  * desc: 
  */
object JsonFormats {
  //User Formats
//  implicit val userWrites: Writes[User] = (
//    (JsPath \ "name").write[String] and
//      (JsPath \ "password").write[String] and
//      (JsPath \ "id").writeNullable[Long]
//    ) (unlift(User.unapply))
//  implicit val userReads: Reads[User] = (
//    (JsPath \ "name").read[String] and
//      (JsPath \ "password").read[String] and
//      (JsPath \ "id").readNullable[Long]
//    )(User.apply _)

  implicit val userinfoFormt = Json.format[Userinfo]
}
