package controllers

import play.api.mvc._           // Controller で利用
// import play.api.Play.current // Actionで利用 ※DBActionを利用するのであれば不要
import play.api.db.slick._      // DBAction で利用

import play.api.data.Form
import play.api.data.Forms._

import models._ // models/Data で利用
import views._  // html.index で利用 ※views.html.indexと記述するのであれば不要

/** Application Controller */
object Application extends Controller {

  /** Home */
  def index(currentPageNum: Int) = DBAction { implicit request =>
    Ok(html.index(PageNation[Data](
         currentPageNum
        ,Data.count
        ,Data.findOffset(currentPageNum * PageNation.maxPageNum - PageNation.maxPageNum, PageNation.maxPageNum)
      )
     ,form)
    )
  }

  /** データ初期化 */
  val form = Form("number" -> number)
  def initData = DBAction { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => {
        Data.deleteAll
        for (i: Int <- 1 to 100) {
          Data.insert(Data(Option(i.toLong), "Data Name %03d%n".format(i)))
        }
      },
      number => {
        Data.deleteAll
        for (i: Int <- 1 to number) {
          Data.insert(Data(Option(i.toLong), "Data Name %03d%n".format(i)))
        }
      }
    )
    Redirect(routes.Application.index(1))
  }

  /** データ全件削除 */
  def deleteAll = DBAction { implicit request =>
    Data.deleteAll
    Redirect(routes.Application.index(1))
  }

  /** Page遷移先を指定 */
  def pageNation(currentPageNum: Int, pageName: String) = Action { implicit request =>
    pageName match {
      case "index" => Redirect(routes.Application.index(currentPageNum))
      // 新たにPageNationを利用する場合、ここに遷移先を追記する
      // 例） case "secondPage" => Redirect(routes.Application.secondPage(currentPageNum))
      case _       => Redirect(routes.Application.index(currentPageNum))
    }
  }
}