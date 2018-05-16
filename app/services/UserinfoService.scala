package services

import javax.inject.{Inject, Singleton}

import models._

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-4-9
  * desc: 
  */
@Singleton
class UserinfoService @Inject()(userinfoDaoImpl: UserinfoDaoImpl){
  def listAllUsers: Future[Seq[Userinfo]] = {
    userinfoDaoImpl.listAll()
  }

  def search(username: String):Future[Seq[Userinfo]] = {
    userinfoDaoImpl.listAll(username)
  }

  def detail(id: Long): Future[Option[Userinfo]] = {
    userinfoDaoImpl.get(id)
  }

  def add(userinfo: Userinfo): Future[String] = {
    println(userinfo.usename,userinfo.description,userinfo.password)
    userinfoDaoImpl.add(userinfo)
  }

  def update(userinfo: Userinfo): Future[String] = {
    userinfoDaoImpl.update(userinfo)
  }

  def delete(id: Long):Future[Int] = {
    userinfoDaoImpl.delete(id)
  }


}
