package jira_tempo_begone.jira

import scala.util.Try


object AtlassianShenanigans {

  import java.time.format.DateTimeFormatter
  import java.time.ZoneId
  import java.time.Instant

  def getTimestamp(): Try[String] = {
    // For some reason Jira API does not accept timestamps in ISO format with 'Z'
    Try {
      val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSSZ")
        .withZone(ZoneId.of("UTC"))

      formatter.format(
        Instant.now()
      )
    }
  }
}