package jira_tempo_begone.jira

object ApiModel {
  case class IssueAssignee(
    emailAddress: String,
  )

  case class IssueStatus(
    name: String,
    id: Int,
  )

  case class IssueFields(
    assignee: IssueAssignee,
    status: IssueStatus
  )

  case class Issue(
    key: String,
    fields: IssueFields
  )

  case class Sprint(
    id: Int
  )

  case class ResponseActiveSprints(
    isLast: Boolean,
    values: List[Sprint]
  )

  case class ResponseIssuesInSprint(
    issues: List[Issue]
  )

  case class IssueWorklogEntry(
    comment: String,
    started: String,
    timeSpentSeconds: Int
  )
}
