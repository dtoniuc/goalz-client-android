package emse.mobisocial.goalz.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.media.Image
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.view.*
import android.widget.SearchView
import android.widget.TextView

import emse.mobisocial.goalz.R
import emse.mobisocial.goalz.model.UserMinimal
import emse.mobisocial.goalz.ui.viewModels.ExploreUsersViewModel

class ExploreUsersFragment : Fragment() {

    private lateinit var model : ExploreUsersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_explore_users, container, false)

        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_explore, menu)
        val searchItem = menu!!.findItem(R.id.exploreSearch)
        searchView = searchItem.actionView as SearchView
        searchView.setIconified(false)
        searchView.setOnCloseListener(object: SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                searchItem.collapseActionView()
                return true
            }
        })
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                model.searchUsers(searchQuery!!)
                return true
            }

            override fun onQueryTextChange(searchQuery: String?): Boolean {
                model.searchUsers(searchQuery!!)
                return true
            }
        })
        super.onCreateOptionsMenu(menu,inflater)
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
            val rating = mUsers[i].rating
            when(rating){
                in 0..10 -> userViewHolder.ratingImage.setImageResource(R.drawable.level_1)
                in 11..50 -> userViewHolder.ratingImage.setImageResource(R.drawable.level_2)
                in 51..200 -> userViewHolder.ratingImage.setImageResource(R.drawable.level_3)
                else -> userViewHolder.ratingImage.setImageResource(R.drawable.level_4)
            }
            userViewHolder.rating.text = rating.toString()
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            super.onAttachedToRecyclerView(recyclerView)
        }

        inner class UserViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var userCard: CardView
            internal var userNickname: TextView
            internal var ratingImage:ImageView
            internal var rating:TextView


            init {
                userCard = itemView.findViewById<View>(R.id.user_card_view) as CardView
                userNickname = itemView.findViewById<View>(R.id.user_nickname) as TextView
                ratingImage = itemView.findViewById(R.id.rating_icon) as ImageView
                rating = itemView.findViewById<View>(R.id.rating_text) as TextView


            }
        }

    }
}