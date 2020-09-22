package com.android.util.uiparse

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/9/22
 */
class ParseDialog private constructor() : DialogFragment() {

    companion object {
        fun newInstance() = ParseDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_ui_parse, container, false)
        initView(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    private fun initView(view: View) {
        activity?.let {
            val uiStructure = ParseUtil.getUIStructure(it)
            view.findViewById<TextView>(R.id.package_name_tv).text = uiStructure.packageName
            view.findViewById<TextView>(R.id.activity_name_tv).text = uiStructure.activityName
            view.findViewById<TextView>(R.id.activity_path_tv).text = uiStructure.activityPath
            if (uiStructure.fragmentList.isNotEmpty()) {
                view.findViewById<LinearLayout>(R.id.fragment_ll).visibility = View.VISIBLE
                view.findViewById<RecyclerView>(R.id.fragment_rv).adapter = FragmentAdapter(uiStructure.fragmentList)
                view.findViewById<TextView>(R.id.fragment_tv).text = uiStructure.getTopFragmentInfo()
            } else {
                view.findViewById<LinearLayout>(R.id.fragment_ll).visibility = View.GONE
            }
        }
    }

    private fun initParams() {
        dialog?.window?.let {
            val params = it.attributes
            params.dimAmount = 0.3f
            params.gravity = Gravity.CENTER
            params.width = SizeUtil.dp2px(280f).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.attributes = params
        }
        isCancelable = true
    }

    fun show(manager: FragmentManager) {
        super.show(manager, ParseDialog::class.java.name)
    }

}