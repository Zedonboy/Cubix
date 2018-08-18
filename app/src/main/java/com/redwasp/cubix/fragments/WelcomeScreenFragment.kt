package com.redwasp.cubix.fragments

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.graphics.Palette
import android.text.TextUtils
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.redwasp.cubix.R
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.customViews.InterestItem
import kotlinx.android.synthetic.main.first_screen.*
import kotlinx.android.synthetic.main.first_window.*
import kotlinx.android.synthetic.main.login_window.*
import kotlinx.android.synthetic.main.sign_up_window.*
import kotlinx.android.synthetic.main.one_more_thing.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking

class WelcomeScreenFragment : Fragment(), IView {
    private lateinit var loginScene : Scene
    private lateinit var signUpScene : Scene
    private lateinit var oneMoreThingScene : Scene
    private lateinit var mTransition: Transition
    private var selectedItems : MutableList<Int> = mutableListOf()
    private val stack = mutableListOf<Scene>()
    private var tempUserObj = object {
        var email : String = ""
        var password : String = ""
        var discipline : String? = null
        var interests : Array<String> = emptyArray()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mTransition = TransitionInflater.from(activity).inflateTransition(R.transition.first_window_transition)
        return inflater.inflate(R.layout.first_screen,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Attaching To Scenes
        loginScene = Scene.getSceneForLayout(scene_root, R.layout.login_window, activity)
        signUpScene = Scene.getSceneForLayout(scene_root, R.layout.sign_up_window, activity)
        oneMoreThingScene = Scene.getSceneForLayout(scene_root, R.layout.one_more_thing, activity)
        initUI()
    }

    private fun initUI(){
        // Hooking callbacks and registering events
        // when clicked, transit to another scene
        login_activator?.setOnClickListener { _ ->
            TransitionManager.go(loginScene, mTransition)
        }
        // When clicked transit to Sign Up Scene
        sign_up_activator?.setOnClickListener { _ ->
            TransitionManager.go(signUpScene, mTransition)
        }

        login_btn?.setOnClickListener { _ ->
            //Make Network calls to login the user.(Firebase)
            // Save User to Global Application COntext
        }

        nxt_btn?.setOnClickListener { _ ->
            // get values from Text Input Widgets
            if (TextUtils.isEmpty(sign_up_btn?.text) or TextUtils.isEmpty(sign_up_password?.text)){
                // Tell User this field should not be empty
            } else {
                tempUserObj.email = sign_up_email?.text.toString()
                tempUserObj.password = sign_up_password?.text.toString()
                // Get to another scene
                TransitionManager.go(oneMoreThingScene, mTransition)
            }
        }

        one_more_thing_gridView?.apply {
            val gridAdapter = GridViewAdapter()
            adapter = gridAdapter
            setOnItemClickListener { _, view, pos, _ ->
                // check whether the item has been selected
                if (selectedItems.contains(pos)){
                    // Yes the element, is in the mutable, thus its has been selected

                    // Remove it then
                    selectedItems.remove(pos)

                    tempUserObj.interests.drop(tempUserObj.interests.indexOf(GridViewAdapter().namesOfInterests[pos]))
                    // show UI action of deselection
                    (view as InterestItem).deselect()
                } else {
                    //No the item wasn't found thus the user have not selected it
                    // Add it then
                    selectedItems.add(pos)
                    // Add it as what the user is interested with
                    tempUserObj.interests.plus(GridViewAdapter().namesOfInterests[pos])
                    (view as InterestItem).selected()
                }
            }
        }
    }

    override fun onBackBtnPressed() {

    }

    inner class GridViewAdapter : BaseAdapter(){
        val namesOfInterests = arrayOf("Tech", "Psychology", "Sport")
        private val imagesOfInterests = arrayOf(
                R.drawable.cpu,
                R.drawable.psych,
                R.drawable.sports
        )
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val view = if (p1 == null) InterestItem(context)
            else p1 as InterestItem
            val categoryTitle = view.findViewById<TextView>(R.id.interest_item_category_title)
            val categoryImage = view.findViewById<ImageView>(R.id.interest_item_category_image)
            categoryTitle.text = namesOfInterests[p0]
            categoryImage.setImageResource(imagesOfInterests[p0])
            // get the palette, gey the vibrant color and change the background color
            extractColorFromBitmap(imagesOfInterests[p0], view)
            return view
        }

        override fun getItem(p0: Int): Any {
            return namesOfInterests[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            /**
             * images of Interest and name of interest must be equal
             * if its not please, just return a threshold number*/
            return if(namesOfInterests.size == imagesOfInterests.size)
                namesOfInterests.size
            else 3
        }

        private fun extractColorFromBitmap(resID : Int, view: View) {
            val bitmap = BitmapFactory.decodeResource(resources, resID)
            Palette.from(bitmap).generate {
                val viewII = view.findViewById<ConstraintLayout>(R.id.interest_item_background)
                runBlocking(UI) {
                    viewII.setBackgroundColor(it.getVibrantColor(Color.BLUE))
                }
            }
        }

    }
}
