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

    // PageNationを初期化
    val pager: PageNation[Data] = PageNation[Data]("list - crud_scala", pageNum, 0, List.empty)

    // データ取得
    val resultTuple = Data.findFromTo(pager.pageNum * pager.maxListCount - pager.maxListCount, pager.maxListCount)

    // データリスト
    pager.dataList = resultTuple._1

    // 全体件数
    pager.totalRows = resultTuple._2.toInt

    Ok(html.index(pager))
  }

  def initData(pInx: Int) = Action { implicit request =>

    // 全件削除
    Data.deleteAll

    // Data作成
    if (Data.findAll.isEmpty) {
      for (i: Int <- 1 to pInx) {
        Data.create(Data(Id(i), "Name_" + i))
      }
    }

    Redirect(routes.Application.index(1))
  }

}