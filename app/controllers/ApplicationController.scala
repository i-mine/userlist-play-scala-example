package controllers

import javax.inject.{Inject, Singleton}

import models.{UserFormData, UserPage, Userinfo}
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.UserinfoService
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * author: dulei
  * date: 18-4-9
  * desc: 
  */
@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, userinfoService: UserinfoService, dbConfigProvider: DatabaseConfigProvider) extends AbstractController(cc) with HasDatabaseConfig[JdbcProfile] {
  override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.profile.api._

  def list = Action.async { implicit request =>
    import models.JsonFormats.userinfoFormt
    userinfoService.listAllUsers.map { res =>
      Ok(Json.toJson(res))
    }
  }

  def page = Action{
    implicit request =>
      Ok(views.html.users_page("layui_page"))
  }

  /**
    * 该Action用于为前端提供ajax post请求的分页数据
    * 数据格式为：json
    * @return ajax json
    */
  def pagedata = Action { implicit request =>
    import models.JsonFormats.pageinfoFormt
    println("ajax 成功进入")
    val ajaxForm = Form(tuple("start" -> number, "limit" -> number))
    val (start, limit) = ajaxForm.bindFromRequest().get

    val counts = Await.result(db.run(sql"""SELECT COUNT(1) FROM test""".as[Int]),Duration.Inf).headOption
    val res = Await.result(db.run(sql"""select id,username,description,password from test limit $start,$limit""".as[(Long,String, String, String)]), Duration.Inf)
    val data = res.map{
      user =>
        Userinfo(user._1, user._2, user._3, user._4)
    }.toList
    println(data)
    val page = UserPage(start,limit,data,counts.get)
    Ok(Json.toJson[UserPage](page))
  }

  def search() = Action.async {
    implicit request =>
      val name: Option[String] = request.body.asFormUrlEncoded
        .flatMap(m => m.get("username").flatMap(_.headOption))
      val username: String = name.getOrElse(0).toString
      println("Application Search query:" + username)
      userinfoService.search(username).map(res => Ok(views.html.users("User Query", res)))
  }

  def detail(id: Long) = Action.async { implicit request =>
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

  def add = Action.async {
    implicit request =>
      userForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest("No data")),
        data => {
          val newUser = Userinfo(0, data.username, data.description, data.password)
          println(data.username, " ", data.description, " ", data.password)
          userinfoService.add(newUser).map(res => Redirect("/users"))
        }
      )
  }

  def edit(id: Long) = Action.async {
    implicit request =>
      userForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest("No data")),
        data => {
          val newUser = Userinfo(id, data.username, data.description, data.password)
          userinfoService.update(newUser).map(res => Redirect("/users"))
        }
      )
  }

  def delete(id: Long) = Action.async {
    implicit request =>
      userinfoService.delete(id).map(res => Ok(Json.obj("delete" -> "success", "id" -> id)))
  }
}
