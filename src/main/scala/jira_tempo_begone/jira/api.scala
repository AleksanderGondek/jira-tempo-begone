package jira_tempo_begone.jira

import jira_tempo_begone.Config
import jira_tempo_begone.jira.ApiModel._


trait JiraApi[F[_,_]] {
  def getUnresolvedIssues(cfg: Config)(boardId: Int): F[String, List[Issue]]
  def updateWorklog(cfg: Config)(issueId: String, timeSpentSeconds: Int): F[String, Boolean]
}
