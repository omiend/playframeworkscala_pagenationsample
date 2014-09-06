package models

import java.util.Date
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Parent(id: Pk[Long] = NotAssigned, name: String, var createDate: Option[Date], var updateDate: Option[Date]) {
}

object Parent {

  // -- Parsers
  /**
   * Parse a Parent from a ResultSet
   */
  val simple = {
    get[Pk[Long]]("parent.id") ~
    get[String]("parent.name") ~
    get[Date]("parent.create_date") ~
    get[Date]("parent.update_date") map {
      case id~name~createDate~updateDate => Parent(id, name, Option(createDate), Option(updateDate))
    }
  }

  // -- Queries

  /**
   * Retrieve a child from the id.
   */
  def findAll: List[Parent] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select *
          from parent
        """
      ).as(
        Parent.simple *
      )
    }
  }

  /**
   * Retrieve a child from the id.
   */
  def findFromTo(offset: Int, maxPageCount: Int) = {
    DB.withConnection { implicit connection =>

      // 親テーブル取得
      val parentList: List[Parent] = SQL(
        """
        select *
          from parent
          limit {maxPageCount} offset {offset}
        """
      ).on(
        'offset -> offset,
        'maxPageCount -> maxPageCount
      ).as(
        Parent.simple *
      )

      // 件数取得
      val totalRows = SQL(
        """
        select count(*)
          from parent
        """
      ).as(scalar[Long].single)

      // 返却
      (parentList, totalRows)

    }

  }

  /**
   * Create a User.
   */
  def create(parent: Parent): Parent = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into parent values (
              {id}
             ,{name}
             ,{create_date}
             ,{update_date}
          )
        """
      ).on(
        'id -> parent.id,
        'name -> parent.name,
        'create_date -> parent.createDate,
        'update_date -> parent.updateDate
      ).executeUpdate()
      parent
    }
  }

  def deleteAll = {
    DB.withConnection { implicit connection =>
      SQL("delete from parent").executeUpdate()
    }
  }
}
