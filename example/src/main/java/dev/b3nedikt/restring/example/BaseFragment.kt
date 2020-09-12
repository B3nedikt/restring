package dev.b3nedikt.restring.example

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import dev.b3nedikt.restring.Restring
import dev.b3nedikt.viewpump.ViewPump
import dev.b3nedikt.viewpump.ViewPumpContextWrapper

abstract class BaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()

        ViewPump.setOverwriteContext(Restring.wrapContext(requireContext()))
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val wrappedContext = ViewPumpContextWrapper.wrap(Restring.wrapContext(requireContext()))
        return wrappedContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}