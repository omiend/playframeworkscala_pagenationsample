package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import anorm._
import models._
import views._


object Application extends Controller {

  /** トップ画面起動 */
  def index(pageNum: Int) = Action { implicit request =>

    // Pagingを初期化
    val pager: Paging[Parent] = Paging[Parent]("list - crud_scala", pageNum, 0, List.empty)

    // データ取得
    val resultTuple = Parent.findFromTo(pager.pageNum * pager.maxListCount - pager.maxListCount, pager.maxListCount)

    // データリスト
    pager.dataList = resultTuple._1

    // 全体件数
    pager.totalRows = resultTuple._2.toInt

    Ok(html.index(pager))
  }

  def initData(pInx: Int) = Action { implicit request =>

    // 現在日付作成（timestamp）
    def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd hh:MM:dd:ss.000").parse(str)
    def nowDate: java.util.Date = new java.util.Date

    // 全件削除
    Parent.deleteAll

    // Parent作成
    if (Parent.findAll.isEmpty) {
      for (i: Int <- 1 to pInx) {
        Parent.create(Parent(Id(i), "Name_" + i, Some(nowDate), Some(nowDate)))
      }
    }

    Redirect(routes.Application.index(1))
  }

}