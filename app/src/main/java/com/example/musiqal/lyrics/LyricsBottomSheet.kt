package com.example.musiqal.lyrics

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import com.example.musiqal.R
import com.example.musiqal.databinding.LyricsBottomSheatLayoutBinding
import com.example.musiqal.ui.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView

class LyricsBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: LyricsBottomSheatLayoutBinding
    val ARG_LYRICS="args_key"
    private  val TAG = "LyricsBottomSheet"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LyricsBottomSheatLayoutBinding.inflate(layoutInflater)
        binding.bottomSheetTVLyrics.movementMethod=ScrollingMovementMethod()
        val parent=binding.bottomSheetLinearColors
        for(child in parent.children)
        {
            val img=(child as CircleImageView)
                img.setOnClickListener {
binding.bottomSheetRelativeLayout.setBackgroundColor((img.drawable as ColorDrawable).color)
                    mainActivtyCoordinator.setBackgroundColor((img.drawable as ColorDrawable).color)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.bottomSheetTVLyrics.text =it.getString(ARG_LYRICS)
        }

    }

    lateinit var mainActivtyCoordinator:CoordinatorLayout
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        mainActivtyCoordinator= requireActivity().findViewById<CoordinatorLayout>(R.id.MainActiviyt_coordinatorLayout)
        mainActivtyCoordinator.setBackgroundColor(Color.BLACK)
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: ")

        mainActivtyCoordinator.setBackgroundColor(
            (requireActivity() as MainActivity).viewsColorEvaluter.evaluatedColor
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            LyricsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_LYRICS, param1)
                }
            }
    }
}