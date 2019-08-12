import java.util.UUID

import pn_challenge.utils.CountryCode.CountryCode

package object pn_challenge {

  case class Click(impressionId: UUID,
                   revenue: BigDecimal)

  case class Impression(appId: Long,
                        advertiserId: Long,
                        countryCode: Option[CountryCode] = None,
                        id: UUID)

  case class ClicksPerApp(appId: Long,
                          countryCode: CountryCode,
                          impressions: Long,
                          clicks: Long,
                          revenue: BigDecimal)

  case class FilteredImpression(appId: Long,
                                countryCode: CountryCode,
                                id: UUID)

  case class ImpressionAndRevenue(appId: Long,
                                  countryCode: CountryCode,
                                  revenue: BigDecimal)

  case class CalculatedRevenue(appId: Long,
                               countryCode: CountryCode,
                               impressions: Int,
                               clicks: Int,
                               revenue: BigDecimal)

  case class ClickCompatible(impressionId: String, revenue: BigDecimal)

  case class ImpressionCompatible(appId: Long, advertiserId: Long, countryCode: String, id: String)

  case class Recommendation(appId: Long, countryCode: String, recom: List[Long])

  type Key = (Long, CountryCode)

}
