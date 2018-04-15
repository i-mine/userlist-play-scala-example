package controllers

import javax.inject.{Inject, Singleton}

import models.{UserFormData, Userinfo}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.UserinfoService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-4-9
  * desc: 
  */
@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, userinfoService: UserinfoService) extends AbstractController(cc) {
  def list = Action.async { implicit request =>
    import models.JsonFormats.userinfoFormt
      userinfoService.listAllUsers.map { res =>
        Ok(Json.toJson(res))
      }
  }

  def search() = Action.async{
    implicit request =>
      val body = request.body
      val jsonBody: Option[JsValue] = body.asJson
      val  name: Option[String] = request.body.asFormUrlEncoded
        .flatMap(m => m.get("username").flatMap(_.headOption))
      val username: String = name.getOrElse(0).toString
      println("Application Search query:"+ username)
      userinfoService.search(username).map(res => Ok(views.html.users("User Query",res)))
  }

  def detail(id: Long) = Action.async{implicit request =>
    import models.JsonFormats.userinfoFormt
    userinfoService.detail(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  
  val userForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "description" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserFormData.apply)(UserFormData.unapply)
  )
  def add = Action.async{
    implicit request =>
      userForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest("No data")),
        data =>{
          val newUser = Userinfo(0, data.username, data.description, data.password)
          println(data.username," ", data.description, " ", data.password)
          userinfoService.add(newUser).map(res => Redirect("/users"))
        }
      )
  }

  def edit(id: Long) = Action.async{
    implicit request =>
      userForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest("No data")),
        data =>{
          val newUser = Userinfo(id, data.username, data.description, data.password)
          userinfoService.update(newUser).map(res => Redirect("/users"))
        }
      )
  }

  def delete(id: Long) = Action.async{
    implicit request =>
      userinfoService.delete(id).map(res =>Ok(Json.obj("delete"->"success","id"->id)))
  }
}
