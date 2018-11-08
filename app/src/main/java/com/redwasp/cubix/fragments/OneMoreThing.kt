package com.redwasp.cubix.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.redwasp.cubix.DiscoverActivity
import com.redwasp.cubix.R
import com.redwasp.cubix.customViews.InterestItem
import kotlinx.android.synthetic.main.one_more_thing.*

class OneMoreThing : Fragment() {
    private val choosenTag = mutableListOf<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.one_more_thing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI(){
        one_more_thing_gridView?.apply {
            adapter = ImageAdapter(context)
            setOnItemClickListener { _, view, _, _ ->
               val img = view.findViewById<ImageView>(R.id.interest_item_check)
                val visible = img.visibility
                if(visible == View.VISIBLE){
                    choosenTag.remove(view.findViewById<TextView>(R.id.interest_item_category_title).text.toString())
                    img.visibility = View.INVISIBLE
                } else if (visible == View.INVISIBLE){
                    choosenTag.add(view.findViewById<TextView>(R.id.interest_item_category_title).text.toString())
                    img.visibility = View.VISIBLE
                }
            }
        }

        sign_up_btn?.setOnClickListener { _ ->
            // check Autocomplete widget, if its empty
            val text = one_more_thing_autocomplete?.text?.toString()?: return@setOnClickListener
            if (text.isEmpty()) return@setOnClickListener
            val sharedPref = activity?.getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE)?:return@setOnClickListener
            with(sharedPref.edit()){
                putStringSet(getString(R.string.user_preferences), choosenTag.toSet())
                putString(getString(R.string.user_discipline), text)
                apply()
                return@with
            }
            val activityIntent = Intent(activity, DiscoverActivity::class.java)
            activity?.startActivity(activityIntent)
            activity?.finish()
        }
    }

    inner class ImageAdapter(private val context : Context) : BaseAdapter(){
        private val images = listOf(R.drawable.cpu, R.drawable.psych, R.drawable.sports)
        private val titles = listOf("Tech", "Psychology", "Sports")
        override fun getView(p0: Int, view: View?, p2: ViewGroup?): View {
            return if(view == null){
                val option = LayoutInflater.from(context).inflate(R.layout.interest_item,p2, false)
                val img = option.findViewById<ImageView>(R.id.interest_item_category_image)
                val title = option.findViewById<TextView>(R.id.interest_item_category_title)
                title.text = titles[p0]
                img.setImageResource(images[p0])
                option
            } else {
                val img = view.findViewById<ImageView>(R.id.interest_item_category_image)
                val title = view.findViewById<TextView>(R.id.interest_item_category_title)
                img.setImageResource(images[p0])
                title.text = titles[p0]
                view
            }
        }

        override fun getItem(p0: Int): Any? = null

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getCount(): Int = images.size

    }
}