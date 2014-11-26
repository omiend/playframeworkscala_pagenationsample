package controllers

import play.api.mvc._           // Controller で利用
// import play.api.Play.current // Actionで利用 ※DBActionを利用するのであれば不要
import play.api.db.slick._      // DBAction で利用

import models._ // models/Data で利用
import views._  // html.index で利用 ※views.html.indexと記述するのであれば不要

/** Application Controller */
object Application extends Controller {
  /** Home */
  def index(pageNum: Int) = DBAction { implicit request =>
    val pageNation: PageNation[Data] = PageNation[Data]("Page Nation Sample", pageNum, Data.count, List.empty)
    val resultTuple = 
    pageNation.dataList = Data.findOffset(pageNation.pageNum * pageNation.maxListCount - pageNation.maxListCount, pageNation.maxListCount)
    Ok(html.index(pageNation))
  }
  /** データ初期化 */
  def initData = DBAction { implicit request =>
    Data.deleteAll
    for (i: Int <- 1 to 100) {
      Data.insert(Data(Option(i.toLong), "Data Name %03d%n".format(i)))
    }
    Redirect(routes.Application.index(1))
  }
  /** データ全件削除 */
  def deleteAll = DBAction { implicit request =>
    Data.deleteAll
    Redirect(routes.Application.index(1))
  }
}