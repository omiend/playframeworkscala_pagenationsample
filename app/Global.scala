import models._

import play.api._            // extends GlobalSetting で利用
import play.api.Play.current // Application で利用
import play.api.db.slick._   // DB.withSession で利用

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    InitialData.insert()
  }
}

object InitialData {
  def insert(): Unit = {
    DB.withSession { implicit s: Session =>
      if (Data.count() == 0) {
        for (i: Int <- 1 to 100) {
          Data.insert(Data(Option(i.toLong), "Data Name %03d%n".format(i)))
        }
      }
    }
  }
}