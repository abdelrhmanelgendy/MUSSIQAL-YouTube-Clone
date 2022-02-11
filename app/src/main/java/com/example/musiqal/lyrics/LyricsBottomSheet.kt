package com.example.musiqal.lyrics

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import com.example.musiqal.R
import com.example.musiqal.databinding.LyricsBottomSheatLayoutBinding
import com.example.musiqal.ui.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView


import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.ImageView
import androidx.core.text.set
import com.example.musiqal.util.MakingToast
import java.util.regex.Matcher
import java.util.regex.Pattern



class LyricsBottomSheet(val resourceOfCoordinatorLayoutId:Int) : BottomSheetDialogFragment() {
    lateinit var binding: LyricsBottomSheatLayoutBinding
    val ARG_LYRICS = "args_key"
    private val TAG = "LyricsBottomSheet"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LyricsBottomSheatLayoutBinding.inflate(layoutInflater)
        binding.bottomSheetTVLyrics.movementMethod = ScrollingMovementMethod()


        val parent = binding.bottomSheetLinearColors
        for (child in parent.children) {

            if (child.id != R.id.bottomSheet_imgCopy) {
                val img = (child as CircleImageView)
                img.setOnClickListener {

                    binding.bottomSheetRelativeLayout.setBackgroundColor((img.drawable as ColorDrawable).color)
                    mainActivtyCoordinator.setBackgroundColor((img.drawable as ColorDrawable).color)
                }
            } else {
                val imgCopy = child as ImageView
                imgCopy.setOnClickListener {
                    copyTextViewContent()
                }
            }
        }

        return binding.root
    }

    private fun copyTextViewContent() {
        val lyrics = binding.bottomSheetTVLyrics.text.toString()
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("lyrics", lyrics)
        clipboardManager.setPrimaryClip(clipData)
        MakingToast(requireContext()).toast("Lyrics Copied", MakingToast.LENGTH_SHORT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {

            coloringTextCiew(it.getString(ARG_LYRICS)!!)
        }

    }

    private fun coloringTextCiew(lyrics: String) {
        val spannableString = SpannableString(lyrics)
        val pattern = Pattern.compile("\\[.*\\n", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(lyrics)
        while (matcher.find())
        {
            val foregroundColorSpan = ForegroundColorSpan(Color.YELLOW)
            spannableString.setSpan(
                foregroundColorSpan,
                matcher.start(),
                matcher.end(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }



        binding.bottomSheetTVLyrics.text = spannableString

    }

    lateinit var mainActivtyCoordinator: CoordinatorLayout
    override fun onAttach(context: Context) {
        super.onAttach(context)


        Log.d(TAG, "onAttach: ")
        mainActivtyCoordinator =
            requireActivity().findViewById<CoordinatorLayout>(resourceOfCoordinatorLayoutId)
        mainActivtyCoordinator.setBackgroundColor(Color.BLACK)
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: ")

        if (requireActivity() is MainActivity)
        {
            mainActivtyCoordinator.setBackgroundColor(
                (requireActivity() as MainActivity).viewsColorEvaluter.evaluatedColor
            )

        }
        else
        {
            mainActivtyCoordinator.setBackgroundColor(Color.BLACK)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String,resourceOfCoordinatorLayoutId:Int) =
            LyricsBottomSheet(resourceOfCoordinatorLayoutId).apply {
                arguments = Bundle().apply {
                    putString(ARG_LYRICS, param1)
                }
            }
    }
}