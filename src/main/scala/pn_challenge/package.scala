import java.util.UUID

import pn_challenge.utils.CountryCode

package object pn_challenge {

  // represents click
  case class Click(impressionId: UUID,
                   revenue: BigDecimal)

  // represents impression
  case class Impression(appId: Long,
                        advertiserId: Long,
                        countryCode: Option[CountryCode] = None,
                        id: UUID)

  // represents calculated metrics
  case class ClicksPerApp(appId: Long,
                          countryCode: CountryCode,
                          impressions: Long,
                          clicks: Long,
                          revenue: BigDecimal)

  // represents impressions without advertiser_id to get rid of extra data
  case class FilteredImpression(appId: Long,
                                countryCode: Option[CountryCode] = None,
                                id: UUID)

  case class ImpressionAndRevenue(appId: Long,
                                  countryCode: Option[CountryCode] = None,
                                  revenue: BigDecimal)

  type Key = (Long, Option[CountryCode])


}
