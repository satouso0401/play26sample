package controllers.api

import javax.inject._

import domain.QuestionService
import models.originator._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class QuestionController @Inject()(cc: ControllerComponents)(qs: QuestionService)(
  dc: DatabaseConfigProvider)
  extends AbstractController(cc) {

  def postQuestion() = Action(parse.json).async { request =>
    val placeResult = request.body.validate[QuestionRequest]
    val createId: Future[Long] = placeResult.map(j => qs.createQuestion(j.req)).getOrElse(Future.successful(0))
    createId
      .map(x => QuestionResult(SaQuestionModel(id = Some(x))))
      .map(y => Ok(Json.toJson(y)))
  }

  def putQuestion() = Action(parse.json).async { request =>
    val placeResult = request.body.validate[QuestionRequest]
    val updateCount: Future[Int] = placeResult.map(j => qs.updateQuestion(j.req)).getOrElse(Future.successful(0))
    updateCount
      .map(x => QuestionResult(SaQuestionModel(id = Some(x)))) // TODO 成功、失敗を返すようにする
      .map(y => Ok(Json.toJson(y)))
  }

  def getQuestionBySurveyId(surveyId: Long) = Action.async {
    qs.readQuestion(None, Some(surveyId)).map(x => QuestionListResult(x)).map(y => Ok(Json.toJson(y)))
  }

  def getQuestion(id: Long) = Action.async {
    qs.readQuestion(Some(id), None).map(x => QuestionListResult(x)).map(y => Ok(Json.toJson(y)))
  }

  def deleteQuestion(qtype: String, qid: Long) = Action(parse.json).async {
    val deleteCount: Future[Int] = qs.deleteQuestion(qtype, qid)
    deleteCount
      .map(x => QuestionResult(SaQuestionModel(id = Some(x)))) // TODO 成功、失敗を返すようにする
      .map(y => Ok(Json.toJson(y)))
  }
}
