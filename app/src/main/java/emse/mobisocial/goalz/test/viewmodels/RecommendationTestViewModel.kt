package emse.mobisocial.goalz.test.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import emse.mobisocial.goalz.GoalzApp
import emse.mobisocial.goalz.dal.DalResponse
import emse.mobisocial.goalz.dal.IRecommendationRepository
import emse.mobisocial.goalz.model.Recommendation
import emse.mobisocial.goalz.model.RecommendationTemplate

private const val NEW_RECOMMENDATION_RESOURCE_ID = "-LANYpGONoMJnkHUzbM2"
private const val NEW_RECOMMENDATION_GOAL_ID = "-LANc6UguMrohxmJ37Ud"
private const val NEW_RECOMMENDATION_USER_ID = "to2mdRC0eXUvPRVTHL1QJk57Aro2"
private const val NEW_RECOMMENDATION_TITLE = "New recommendation"
private const val NEW_RECOMMENDATION_DESCRIPTION = "New description"
private const val NEW_RECOMMENDATION_REQ_TIME = 30

private const val UPDATED_RATING = 4.5


/**
 * Created by MobiSocial EMSE Team on 3/29/2018.
 */
class RecommendationTestViewModel  (application: Application) : AndroidViewModel(application){

    private val recommendationRepository : IRecommendationRepository
            = (application as GoalzApp).recommendationRepository
    private var recommendationListDb: MutableLiveData<LiveData<List<Recommendation>>>
            = MutableLiveData<LiveData<List<Recommendation>>>()
    val recommendationList: LiveData<List<Recommendation>>
            = Transformations.switchMap(recommendationListDb){ it }

    var selectedRecommendation : Recommendation? = null

    fun searchForUser(userId : String){
        recommendationListDb.postValue(recommendationRepository.getRecommendationsForUser(userId))
    }

    fun searchForAuthor(userId : String){
        recommendationListDb.postValue(recommendationRepository.getRecommendationsForAuthor(userId))
    }

    fun searchForGoal(goalId : String){
        recommendationListDb.postValue(recommendationRepository.getRecommendationsForGoal(goalId))
    }

    fun insertRecommendation() : LiveData<DalResponse> {
        return recommendationRepository.addRecommendation(
                RecommendationTemplate(
                        NEW_RECOMMENDATION_RESOURCE_ID,
                        NEW_RECOMMENDATION_GOAL_ID,
                        NEW_RECOMMENDATION_USER_ID,
                        NEW_RECOMMENDATION_TITLE,
                        NEW_RECOMMENDATION_DESCRIPTION,
                        NEW_RECOMMENDATION_REQ_TIME
                )
        )
    }

    fun deleteSelectedRecommendation() : LiveData<DalResponse>? {
        val x = selectedRecommendation
        if(x != null) {
            recommendationRepository.deleteRecommendation(x.id)
        }

        return null
    }

    fun updateSelectedRecommendation() : LiveData<DalResponse>? {
        val recommendation = selectedRecommendation
        if (recommendation != null) {
            recommendationRepository.rateRecommendation(recommendation.id, UPDATED_RATING)
        }

        return null
    }

}