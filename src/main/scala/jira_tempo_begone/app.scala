package jira_tempo_begone


object App {

  import scala.util.Either

  import jira_tempo_begone.jira.Api
  import jira_tempo_begone.jira.ApiModel

  def _calculateTimeSpentOnIssues(cfg: Config)(issuesByAssignee: Map[String, List[ApiModel.Issue]]): Either[String, Unit] = {
    val hoursWorkedPerDay = 8.0
    val (errors, _) = issuesByAssignee.filter{ case (email: String, _) => cfg.userEmails.contains(email.toLowerCase())}
      .filterNot{ case (_, issues: List[ApiModel.Issue]) => issues.isEmpty}
      .values.flatMap(issues => {
        val timeSpentPerIssue = ((hoursWorkedPerDay * 60.0) / issues.length.toFloat).ceil.toInt
        issues.map(issue => Api.updateWorklog(cfg)(issue.key, timeSpentPerIssue * 60))
      })
      .partitionMap(identity)
      if (errors.isEmpty) Right(()) else Left(errors.mkString(","))
  }

  def updateTimeSpent(cfg: Config): Either[String, Unit] = {
    for {
      issues <- Api.getUnresolvedIssues(cfg)(cfg.jiraBoardId)
      issuesByAssignee <- Right(issues.groupBy(x => x.fields.assignee.emailAddress.toLowerCase()))
      results <- _calculateTimeSpentOnIssues(cfg)(issuesByAssignee)
    } yield results
  }
}