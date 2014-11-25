package models

import java.util.Date
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Data(id: Pk[Long] = NotAssigned, name: String)

object Data {

  val simple = {
    get[Pk[Long]]("data.id") ~
    get[String]("data.name") map {
      case id~name => Data(id, name)
    }
  }

  def findAll: List[Data] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select *
          from data
        """
      ).as(
        Data.simple *
      )
    }
  }

  def findFromTo(offset: Int, maxPageCount: Int) = {
    DB.withConnection { implicit connection =>

      // 親テーブル取得
      val dataList: List[Data] = SQL(
        """
        select *
          from data
          limit {maxPageCount} offset {offset}
        """
      ).on(
        'offset -> offset,
        'maxPageCount -> maxPageCount
      ).as(
        Data.simple *
      )

      // 件数取得
      val totalRows = SQL(
        """
        select count(*)
          from data
        """
      ).as(scalar[Long].single)

      // 返却
      (dataList, totalRows)

    }

  }

  def create(data: Data): Data = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into data values (
              {id}
             ,{name}
          )
        """
      ).on(
         'id   -> data.id
        ,'name -> data.name
      ).executeUpdate()
      data
    }
  }

  def deleteAll = {
    DB.withConnection { implicit connection =>
      SQL("delete from data").executeUpdate()
    }
  }
}
