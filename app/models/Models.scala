package models

import scala.slick.driver.MySQLDriver.simple._ // Session 等で利用

case class Data(id: Option[Long] = None, name: String)
class DataTable(tag: Tag) extends Table[Data](tag, "data") {
  def id   = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def *    = (id, name) <> ((Data.apply _).tupled, Data.unapply)
}
object Data {
  // DDL  
  lazy val query = TableQuery[DataTable]
  // 全件取得
  def findAll()(implicit s: Session): List[Data] = query.list
  // 全体件数取得
  def count()(implicit s: Session): Int = query.list.size
  // Offsetを指定して取得
  def findOffset(offset: Int, limit: Int)(implicit s: Session): List[Data] = query.drop(offset).take(limit).list
  // 登録
  def insert(data: Data)(implicit s: Session) = query.insert(data)
  // 条件無しの全件削除
  def deleteAll()(implicit s: Session) = query.delete
}