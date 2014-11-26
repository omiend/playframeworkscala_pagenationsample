package models

/** ページ処理で利用するクラス */
case class PageNation(currentPageNum: Int     // 現在のページ番号
                        ,totalCount    : Int     // 全体件数
                        ,dataList      : List[A] // 一覧するデータリスト
                        ) {

  /** 取得したデータの件数からページ数を計算 */
  def maxPageNumber: Int = {
    // 端数が存在する場合１ページ追加
    if (totalCount % PageNation.maxListCount > 0) {
      totalCount / PageNation.maxListCount + 1
    } else {
      totalCount / PageNation.maxListCount
    }
  }

  /**
   * ページ番号リスト作成起点を取得
   */
  def startIndex: Int = {
    var ret: Int = 0
    // 現在のページ番号
    //  7ページ目＝ 11    10 - (3 * 2) =  4
    //  8ページ目＝ 12    10 - (3 * 2) =  4
    //  9ページ目＝ 13    10 - (3 * 2) =  4
    // 10ページ目＝ 14    10 - (3 * 2) =  4
    if (currentPageNum + PageNation.behindAndFrontCount + 1 > maxPageNumber) {
      ret = maxPageNumber - (PageNation.behindAndFrontCount * 2)
    //  1ページ目＝  5     1 - 3 = -2(1)
    //  2ページ目＝  6     2 - 3 = -1(1)
    //  3ページ目＝  7     3 - 3 = -0(1)
    //  4ページ目＝  8     4 - 3 =  1
    //  5ページ目＝  9     5 - 3 =  2
    //  6ページ目＝ 10     6 - 3 =  3
    } else {
      ret = currentPageNum - PageNation.behindAndFrontCount
    }
    if (ret <= 0) {
      ret = 1
    }
    ret
  }

  /** ページ番号リストを返却する */
  def pageNumList: List[Int] = {
    // 返却用
    var pageNumList: List[Int] = List.empty
    // ページ番号リストを作成
    for (i: Int <- startIndex to maxPageNumber if pageNumList.size < (PageNation.behindAndFrontCount * 2 + 1) && pageNumList.size < maxPageNumber) {
      if (i >= 1 && maxPageNumber >= i) {
        pageNumList = i :: pageNumList
      }
      i + 1
    }
    pageNumList.reverse
  }
}

object PageNation {
  /** １ページに表時するデータの件数 */
  val maxListCount: Int = 5
  /** 現在ページの両脇ページ番号を表示する件数 */
  val behindAndFrontCount: Int = 5
}