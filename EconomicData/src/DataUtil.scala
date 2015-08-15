import scala.io.Source
/**
 * Created by jessenelson on 4/5/15.
 */
object DataUtil extends App {
  private final val INDEX_HEADER = "#Geograhical area index"
  private final val TITLE_HEADER = "%Title"
  private final val FORMAT_HEADER = "%Format%"
  private final val COLUMN_HEADER = "%Header"
  private final val DATA_HEADER = "%Data"
  private final val CHOICES =
    List("1. Find top 5 exporting countries for a given year",
      "2. Find the worst 5 exporting countries for a given year",
      "3. Find the 5 countries with the best balance of trade for a given year",
      "4. Find the 5 countries with the worst balance of trade for a given year",
      "5. Find the 5 countries with the best ratio of exports to balance of trade for a given year",
      "6. Find the 5 countries with the worst ratio of exports to balance of trade for a given year",
      "7. Find the sum for exports, trade balance, and population for a given year."
    )

  case class CountryObj(
                         country: String,
                         Exports: Int,
                         Trade_Balance: Int,
                         Population: Int,
                         Geographical_Area: String
                         )

  private val fileIterator =
    Source.fromFile("/Users/jessenelson/Development/CS3210/EconomicData/data.txt").getLines()

  verifyFileFormat(INDEX_HEADER)
  private val geoIndex = handleGeoIndexs(fileIterator, List[String](), fileIterator.next())

  verifyFileFormat(TITLE_HEADER)
  private val title = fileIterator.next()

  verifyFileFormat(FORMAT_HEADER)
  verifyFileFormat(COLUMN_HEADER)
  val columnDefs = fileIterator.next().split(",")

  verifyFileFormat(DATA_HEADER)
  val map = buildEntries(Map(), fileIterator)

  var yearStr = ""
  for (key <- map.keySet) {
    yearStr += key + "\n"
  }

  while (true) {
    val userChoice =
      getChoice("\nPlease choose a number from the list to perform action, or 0 to exit\n")
    if (userChoice == 0) System.exit(0)
    var year = getInput("Please choose a year from list\n" + yearStr)
    while (!map.contains(year)) {
      year =
        getInput("You have not selected a valid year. \nPlease choose a year from list\n " + yearStr)
    }

    userChoice match {
      case 1 =>
        val sorted = map.get(year).get.sortBy(ctry => ctry.Exports)
        val top5Exports = sorted.drop(sorted.length - 5)

        top5Exports.foreach(ctry => println("Country: " + ctry.country +
                                            ", Exports: " + ctry.Exports))
      case 2 =>
        val sorted = map.get(year).get.sortBy(ctry => ctry.Exports)
        val worst5Exports = sorted.take(5)

        worst5Exports.foreach(ctry => println("Country: " + ctry.country +
                                              ", Exports: " + ctry.Exports))
      case 3 =>
        val sorted = map.get(year).get.sortBy(ctry => ctry.Trade_Balance)
        val top5TradeBalance = sorted.drop(sorted.length - 5)

        top5TradeBalance.foreach(ctry => println("Country: " + ctry.country +
                                                 ", Exports: " + ctry.Trade_Balance))
      case 4 =>
        val sorted = map.get(year).get.sortBy(ctry => ctry.Trade_Balance)
        val worst5TradeBalance = sorted.take(5)

        worst5TradeBalance.foreach(ctry => println("Country: " + ctry.country +
                                                   ", Exports: " + ctry.Trade_Balance))
      case 5 =>
        val sorted = map.get(year).get.sortBy(ctry => ctry.Exports / ctry.Trade_Balance)
        val top5ExportsToTrade = sorted.drop (sorted.length - 5)

        top5ExportsToTrade.foreach (ctry => println ("Country: " + ctry.country +
                                                     ", Exports: " + ctry.Trade_Balance))
      case 6 =>
        val sorted = map.get(year).get.sortBy(ctry => ctry.Exports / ctry.Trade_Balance)
        val worst5ExportsToTrade = sorted.take(5)

        worst5ExportsToTrade.foreach (ctry => println ("Country: " + ctry.country +
                                                       ", Exports: " + ctry.Trade_Balance))
      case 7 =>
        val values = map.get(year).get
        val exportsTotal = values.map(_.Exports).sum
        val tradeBalanceTotal = values.map(_.Trade_Balance).sum
        val populationTotal = values.map(_.Population).sum

        println("The sum for exports, trade balance, and population for " + year +
                " is:\nExports: " + exportsTotal + ", Trade Balances: " + tradeBalanceTotal +
                ", Population: " + populationTotal)
    }
  }

  def handleGeoIndexs(source: Iterator[String],
                      list: List[String],
                      line: String): List[String] = {
    if (line.equals("#")) {
      return list.reverse
    }
    return handleGeoIndexs(source,
                          line.substring(1, line.length) :: list,
                          source.next())
  }

  def buildEntries(map: Map[String, List[CountryObj]],
                   source: Iterator[String]): Map[String, List[CountryObj]] = {
    if (source.hasNext) {
      val line = source.next()
      val lineAsArray = line.split(",")
      if (lineAsArray.length < columnDefs.length) {
        println(lineAsArray + " Doesn't match the format of " + columnDefs)
        System.exit(-1)
      }
      val country = lineAsArray(columnDefs.indexOf("Country"))
      val exports = lineAsArray(columnDefs.indexOf("Exports"))
      val tradeBalance = lineAsArray(columnDefs.indexOf("Trade Balance"))
      val year = lineAsArray(columnDefs.indexOf("Year"))
      val population = lineAsArray(columnDefs.indexOf("Population"))
      val geoArea = lineAsArray(columnDefs.indexOf("Geographical Area"))
      val countryMap =
        CountryObj(country,
                   Integer.parseInt(exports),
                   Integer.parseInt(tradeBalance),
                   Integer.parseInt(population),
                   geoIndex(Integer.parseInt(geoArea) - 1))

      if (map.contains(year)) {
        return buildEntries(map + (year -> (countryMap :: map.get(year).get)), source)
      }
      return buildEntries(map + (year -> List[CountryObj](countryMap)), source)
    }
    return map
  }

  def getInput(msg: (String)): String = {
    println(msg)
    return Console.readLine()
  }

  def getChoice(msg: (String)): Int = {
    println(msg)
    CHOICES.foreach(println)

    var input = 0
    try {
      input = Console.readInt()
    } catch {
      case e: NumberFormatException =>
        getChoice("You have not entered a valid choice." +
          "Please rechoose from the list or type 0 to quit")
    }

    return input
  }

  def verifyFileFormat(expected: (String)) = {
    if(fileIterator.next() != expected) {
      System.out.println("You haven't provided a valid file format. Expected: " + expected)
      System.exit(-1)
    }
  }
}
