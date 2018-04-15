package Dao

import models.Userinfo

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-4-9
  * desc: 
  */
trait UserinfoDao {
  def add(user: Userinfo): Future[String]
  def delete(id: Long): Future[Int]
  def update(newUser: Userinfo): Future[String]
  def get(id: Long): Future[Option[Userinfo]]
  def listAll(args: String*): Future[Seq[Userinfo]]

}
