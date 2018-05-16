package controllers

import javax.inject.{Inject, Singleton}

import models.Userinfo
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * author: dulei
  * date: 18-4-11
  * desc: 
  */
@Singleton
class UserInfoController @Inject()(cc: ControllerComponents, ws: WSClient) extends AbstractController(cc) {
  def list = Action.async {implicit request: Request[AnyContent] =>
    import models.JsonFormats._
    val url = s"http://localhost:9000/api/users"
    ws.url(url).get().map {
      response =>
        val json = Json.parse(response.body)
        val jsonSeq = json.as[Seq[Userinfo]]
        Ok(views.html.users("User list", jsonSeq))
    }
  }


  def detail(id: Long) = Action.async{
    import models.JsonFormats._
    val url = s"http://localhost:9000/api/users/detail/$id"
    ws.url(url).get().map {
      response =>
        val json = Json.parse(response.body)
        val jsonObject = json.as[Userinfo]
        Ok(views.html.details("User details",jsonObject))
    }
  }

  def add = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.userForm("Add User"))
  }

  def edit(id: Long) = Action.async{
    import models.JsonFormats._
    val url = s"http://localhost:9000/api/users/detail/$id"
    ws.url(url).get().map {
      response =>
        val json = Json.parse(response.body)
        val jsonObject = json.as[Userinfo]
        Ok(views.html.edit("User Update",jsonObject))
    }
  }

  def delete(id: Long)= Action{
    val url = s"http://localhost:9000/api/users/delete/$id"
    ws.url(url).get()
    Ok("删除成功")
    Redirect("/users")
  }
}
