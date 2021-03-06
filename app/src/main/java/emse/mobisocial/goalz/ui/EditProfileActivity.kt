package emse.mobisocial.goalz.ui

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.content.res.AppCompatResources
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import emse.mobisocial.goalz.R
import emse.mobisocial.goalz.dal.DalResponse
import emse.mobisocial.goalz.dal.DalResponseStatus
import emse.mobisocial.goalz.model.User
import emse.mobisocial.goalz.ui.viewModels.UserProfileViewModel
import emse.mobisocial.goalz.util.Gender
import emse.mobisocial.goalz.util.getAvatarImageResource
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*

private const val GENDER_MALE = "MALE"
private const val GENDER_FEMALE = "FEMALE"
private const val GENDER_UNDEFINED = "NOT SPECIFIED"

class EditProfileActivity : AppCompatActivity() {
    private lateinit var model : UserProfileViewModel
    private lateinit var mDateListener:DatePickerDialog.OnDateSetListener
    private lateinit var mSnackbar: Snackbar
    private var gender :Gender? = null
    private var birthYear:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        model = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: finish()

        model.initialize(userId.toString())

        initializeObservers()

        birthdate_edit_text.isEnabled = false
        initializeDatePicker()

        change_password_button.setOnClickListener {
            Toast.makeText(this, getString(R.string.unavailable_function_toast),
                    Toast.LENGTH_LONG).show()
            /*val intent = Intent(this, ChangeEmailPasswordActivity::class.java)
            intent.putExtra("user_id", userId.toString())
            startActivity(intent)*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save_edit_profile -> {
                updateUserData()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeObservers() {
        model.userData.observe(this, Observer<User> { user ->
            if (user != null) {
                setUserData(user)
                birthYear = user.age!!.toInt()
            }
        })
    }

    private fun setUserData(user:User){

        nickname_edit_text.setText(user.nickname)
        firstname_edit_text.setText(user.firstName)
        lastname_edit_text.setText(user.lastName)
        website_edit_text.setText(user.website)
        user_profile_picture.setImageResource(getAvatarImageResource(user.avatar))
        
        val spinnerArray = arrayOfNulls<String>(3)

        spinnerArray[0] = GENDER_FEMALE
        spinnerArray[1] = GENDER_MALE
        spinnerArray[2] = GENDER_UNDEFINED

        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pick_gender.adapter = adapter
        pick_gender.setSelection(getGenderPosition(user.gender))
    }

    private fun updateUserData() {

        val website = website_edit_text.text.toString()
        val nickname = nickname_edit_text.text.toString().trim()
        val firstName = firstname_edit_text.text.toString().trim()
        val lastName = lastname_edit_text.text.toString().trim()
        gender = getGender(pick_gender.selectedItemPosition)

        if(checkBirthYear(birthYear!!)) {
            if (!areValidFields(nickname, firstName, lastName)) {
                launchSnackbar(getString(R.string.edit_profile_activity_invalid_fields_snackbar))
            } else {
                model.modifyUserData(nickname, firstName, lastName, birthYear!!, website, gender!!)?.observe(this, Observer<DalResponse> { response ->
                    if (response?.status == DalResponseStatus.SUCCESS) {
                        Toast.makeText(application, application.getString(R.string.edit_profile_update_profile_success_toast),
                                Toast.LENGTH_LONG).show()
                        finish()
                    } else if (response?.status == DalResponseStatus.FAIL) {
                        launchSnackbar(getString(R.string.edit_profile_update_profile_failed_snackbar))
                    }
                })
            }
        }
    }


    private fun checkBirthYear(birthYear:Int):Boolean{
        if(birthYear < 12) {
            launchSnackbar(getString(R.string.edit_profile_activity_minimum_age_snackbar))
            return false
        }else return true
    }

    private fun getGender(position:Int) :Gender{
        when (position) {
            0 -> return Gender.FEMALE
            1 -> return Gender.MALE
            2 -> return Gender.UNDEFINED
        }
        return Gender.UNDEFINED
    }

    private fun getAge(Year:Int):Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR) - Year
    }

    private fun getGenderPosition(gender: Gender): Int{
        return when (gender){
            Gender.FEMALE -> 0
            Gender.MALE -> 1
            Gender.UNDEFINED -> 2
        }
    }

    private fun initializeDatePicker(){
        pickbirthdate_edit.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val year:Int = calendar.get(Calendar.YEAR)
            val month:Int = calendar.get(Calendar.MONTH)
            val day:Int = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog (
                    this, R.style.ThemeOverlay_AppCompat_Dialog, mDateListener, year, month, day)
            dialog.datePicker.maxDate = System.currentTimeMillis()-1000
            dialog.show()
        }
        mDateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year:Int, month:Int, day:Int ->
            birthYear = getAge(year)
            val datePicked = day.toString()+"/"+month.toString()+"/"+year.toString()
            birthdate_edit_text.setText(datePicked)
        }
    }

    private fun launchSnackbar(title: String) {
        mSnackbar = Snackbar.make(edit_profile_layout, title, Snackbar.LENGTH_SHORT)
        mSnackbar.view.background = AppCompatResources.getDrawable(this, R.color.snackbarErrorColor)
        mSnackbar.show()
    }

    private fun areValidFields(nickname : String, firstname : String, lastname: String) : Boolean{
        return nickname != "" && firstname != "" && lastname != ""
    }
}
