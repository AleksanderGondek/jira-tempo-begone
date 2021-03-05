package jira_tempo_begone

object Main extends App {

  import jira_tempo_begone.getConfig
  import jira_tempo_begone.App

  getConfig() match {
    case Left(_) => {}
    case Right(config) => {
      App.updateTimeSpent(config)      
    }
  }
}