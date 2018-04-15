package models

import javax.inject.Inject

import Dao.UserinfoDao
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-4-8
  * desc: 
  */

case class Userinfo(id: Long, usename: String, description: String, password: String)
case class UserFormData(username: String, description: String, password: String)
trait UserinfoSchema {
  protected val driver: JdbcProfile
  import driver.api._
  class UserinfoTable(tag: Tag) extends Table[Userinfo](tag, "test"){
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")
    def description = column[String]("description")
    def * = (id, username, description, password) <> (Userinfo.tupled, Userinfo.unapply)
  }
}


class UserinfoDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfig[JdbcProfile]
    with UserinfoSchema
    with UserinfoDao{
  override protected val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.profile.api._
  val userInfos = TableQuery[UserinfoTable]

  override def add(user: Userinfo): Future[String] = {
    db.run(userInfos += user).map(res => "User sucessfully added").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Long): Future[Int] = {
    db.run(userInfos.filter(_.id === id).delete)
  }

  override def update(newUser: Userinfo): Future[String] = {
    db.run(userInfos.filter(_.id === newUser.id).update(newUser)).map(
      res => "User sucessfully added"
    ).recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def get(id: Long): Future[Option[Userinfo]] = {
    db.run(userInfos.filter(_.id === id).result.headOption)
  }

  override def listAll(args: String*): Future[Seq[Userinfo]] = {
    if(args.length == 1){
      //实现查询search的逻辑,arg在这里最多只有一个
      for(arg <- args){
       return db.run(userInfos.filter(
         _.username.like(s"%$arg%")).result)
      }
    }
    db.run(userInfos.result)
  }
}