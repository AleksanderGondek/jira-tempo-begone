package jira_tempo_begone.jira

import scala.util.Either

import jira_tempo_begone.jira.JiraApi
import jira_tempo_begone.Config


object Api extends JiraApi[Either] {

  import scala.collection.immutable.Map;

  import sttp.client3._
  import sttp.client3.circe._
  import io.circe.generic.auto._

  import jira_tempo_begone.jira.ApiModel._
  import jira_tempo_begone.jira.AtlassianShenanigans

  val requestsBackend = HttpURLConnectionBackend()
  val requestHeaders: Map[String, String] = Map[String, String](
    // Using 'fake' Chrome in Windows 10 agent does not work
    // For now, using proper agent id
    "User-Agent" -> "Tempo-Begone",
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "X-Atlassian-Token" -> "no-check",
  )

  private def _getRequestHeaders(basicAuthToken: String): Map[String, String] = {
    requestHeaders + (
      "Authorization" -> f"Basic ${basicAuthToken}",
    )
  }

  private def _getIdsOfOpenSprints(cfg: Config)(boardId: Int): Either[String, List[Int]] = {
    val response = basicRequest
      .headers(_getRequestHeaders(cfg.basicAuthToken))
      .get(uri"${cfg.jiraApiUrl}/rest/agile/1.0/board/${boardId}/sprint?state=active")
      .response(asJson[ResponseActiveSprints])
      .send(requestsBackend)
    
    if (!response.is200) {
      return Left("Received non 2XX response from /board/<boardId>/sprint endpoint")
    }

    response.body match {
      case Right(response: ResponseActiveSprints) => {
        Right(response.values.map(z => z.id))
      }
      case Left(error) => {
        Left(error.getMessage())
      }
    }
  }

  private def _getUnresolvedIssues(cfg: Config)(boardId: Int)(sprintId: Int): Either[String, List[Issue]] = {
    val response = basicRequest
      .headers(_getRequestHeaders(cfg.basicAuthToken))
      .get(uri"${cfg.jiraApiUrl}/rest/agile/1.0/board/${boardId}/sprint/${sprintId}/issue?fields=assignee,status&maxResults=999")
      .response(asJson[ResponseIssuesInSprint])
      .send(requestsBackend)
    
    if (!response.is200) {
      return Left("Received non 2XX response from /board/<boardId>/sprint/<sprintId>issue endpoint")
    }

    response.body match {
      case Right(response: ResponseIssuesInSprint) => {
        Right(response.issues)
      }
      case Left(error) => {
        Left(error.getMessage())
      }
    }
  }

  private def _getIssues(cfg: Config)(boardId: Int)(sprintIds: List[Int]): Either[String, List[Issue]] = {
    val (errors, issues) = sprintIds
      .map(x => _getUnresolvedIssues(cfg)(boardId)(x))
      .partitionMap(identity)
    if (!errors.isEmpty) Left(errors.mkString(",")) else Right(issues.flatten)
  }

  def getUnresolvedIssues(cfg: Config)(boardId: Int): Either[String, List[Issue]] = {
      val issues = for {
        ids <- _getIdsOfOpenSprints(cfg)(boardId)
        is <- _getIssues(cfg)(boardId)(ids)
      } yield is
      issues.map(x => x.filterNot(z => z.fields.status.name == "Resolved"))
  }

  def updateWorklog(cfg: Config)(issueId: String, timeSpentSeconds: Int): Either[String, Boolean] = {
    val timestamp = AtlassianShenanigans.getTimestamp().fold(
      _ => "1970-01-01T00:00:00.001+0000",
      stamp => stamp
    )

    val response = basicRequest
      .headers(_getRequestHeaders(cfg.basicAuthToken))
      .body(IssueWorklogEntry(
        "Have been working on the issue",
        timestamp,
        timeSpentSeconds
      ))
      .post(uri"${cfg.jiraApiUrl}/rest/api/2/issue/${issueId}/worklog")
      .send(requestsBackend)

      response.body match {
        case Right(_) => Right(true)
        case Left(error) => {
          Left(error)
        }
      }
  }

}