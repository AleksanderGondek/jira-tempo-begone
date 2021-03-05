package jira_tempo_begone


case class Config(
  basicAuthToken: String,
  jiraApiUrl: String,
  jiraBoardId: Int,
  userEmails: Set[String]
)

object getConfig {
  import scala.util.Either

  import pureconfig.error.ConfigReaderFailures
  import pureconfig.generic.auto._

  def apply(): Either[ConfigReaderFailures, Config] = {
    pureconfig.ConfigSource.file("application.conf").load[Config]
  }
}
