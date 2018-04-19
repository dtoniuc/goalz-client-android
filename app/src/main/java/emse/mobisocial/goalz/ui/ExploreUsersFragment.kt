package emse.mobisocial.goalz.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import emse.mobisocial.goalz.R
import emse.mobisocial.goalz.model.UserMinimal
import emse.mobisocial.goalz.ui.viewModels.ExploreUsersViewModel

class ExploreUsersFragment : Fragment() {

    private lateinit var model : ExploreUsersViewModel
    private lateinit var recyclerView: RecyclerView
    // An array can be used to hold the data temporarily. Maybe needed later
    // private lateinit var usersList : ArrayList<UserMinimal>
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_explore_users, container, false)

        // usersList = ArrayList<UserMinimal>()

        model = ViewModelProviders.of(this).get(ExploreUsersViewModel::class.java)

        recyclerView = view.findViewById(R.id.explore_users_recycler_view) as RecyclerView

        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = RecyclerViewAdapter(ArrayList<UserMinimal>())
        recyclerView.adapter = recyclerViewAdapter

        initializeObservers()

        return view
    }

    private fun initializeObservers() {
        model.usersList.observe(this, Observer<List<UserMinimal>> { users ->
            if (users != null) {
                recyclerViewAdapter.addItems(users)
            }
        })
    }

    inner class RecyclerViewAdapter(usersParam: ArrayList<UserMinimal>) : RecyclerView.Adapter<RecyclerViewAdapter.UserViewHolder>() {
        private var mUsers: List<UserMinimal> = usersParam

        override fun getItemCount(): Int {
            return mUsers.size
        }

        fun addItems(newUsersList: List<UserMinimal>) {
            this.mUsers = newUsersList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UserViewHolder {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_card, viewGroup, false)
            return UserViewHolder(v)
        }

        override fun onBindViewHolder(userViewHolder: UserViewHolder, i: Int) {
            // The data from the user model is retrieved and bound to the card View here.
            userViewHolder.userNickname.text = mUsers[i].nickname
            userViewHolder.userRating.text = mUsers[i].rating.toString()
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            super.onAttachedToRecyclerView(recyclerView)
        }

        inner class UserViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var userCard: CardView
            internal var userNickname: TextView
            internal var userRating: TextView

            init {
                userCard = itemView.findViewById<View>(R.id.user_card_view) as CardView
                userNickname = itemView.findViewById<View>(R.id.user_nickname) as TextView
                userRating = itemView.findViewById<View>(R.id.user_rating) as TextView
            }
        }

    }
}