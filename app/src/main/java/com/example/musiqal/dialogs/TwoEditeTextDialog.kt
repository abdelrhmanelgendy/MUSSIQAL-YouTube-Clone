package com.example.musiqal.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.musiqal.R
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.musiqal.databinding.TwoEditeTextItemLayoutDialogBinding
import com.example.musiqal.dialogs.util.OnTwoEditesViewsButtonsClickListener
import com.example.musiqal.lyrics.model.SongAndSingerName
import com.example.musiqal.lyrics.mvi.LyricsViewModel


class TwoEditeTextDialog(private val context: Context) {

    private var binding: TwoEditeTextItemLayoutDialogBinding
    private var dialog: Dialog


    init {


        val inflater =
            LayoutInflater.from(context).inflate(R.layout.two_edite_text_item_layout_dialog, null)

        binding = TwoEditeTextItemLayoutDialogBinding.bind(inflater)
        dialog = Dialog(context)
        dialog.setContentView(binding.root)


    }

    public fun intialize(
        mainEditeText: String,
        subEditeText: String,
        btnPositiveText: String,
        btnNegativeText: String,
        onDialogButtonsClickLister: OnTwoEditesViewsButtonsClickListener
    ) {

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams


        binding.twoETDialogEtMainET.setText(removeSpaces(mainEditeText))
        binding.twoETDialogEtSubET.setText(removeSpaces(subEditeText))

        binding.twoETDialogBtnNegativeButton.setText(btnNegativeText)
        binding.twoETDialogBtnPositiveButton.setText(btnPositiveText)



        initButtonsListeners(onDialogButtonsClickLister)
    }

    private fun removeSpaces(mainEditeText: String): String {
   try {
       var name = mainEditeText
       if (name.get(name.length - 1).toString().equals(" ")) {
           name = name.replace(" ", "")
       }
       if (name.get(0).toString().equals(" ")) {
           name = name.replace(" ", "")
       }
       return name

   }
   catch (e:Exception)
   {
       return mainEditeText
   }

    }

    private fun initButtonsListeners(
        onDialogButtonsClickLister: OnTwoEditesViewsButtonsClickListener
    ) {


        binding.twoETDialogBtnPositiveButton.setOnClickListener {
            val mainText = binding.twoETDialogEtMainET.text.toString()
            val subText = binding.twoETDialogEtSubET.text.toString()
            onDialogButtonsClickLister.onPositiveButtonClick(SongAndSingerName(mainText, subText))
        }

        binding.twoETDialogBtnNegativeButton.setOnClickListener {
            val mainText = binding.twoETDialogEtMainET.text.toString()
            val subText = binding.twoETDialogEtSubET.text.toString()
            onDialogButtonsClickLister.onNegativeButtonClick(SongAndSingerName(mainText, subText))
        }
    }

    fun show(cancelable: Boolean) {
        dialog.setCancelable(cancelable)
        dialog.show()
    }

    fun dismis() {
        dialog.dismiss()
    }

    fun hideButtons(isButtonsVisible: Boolean) {
        if (isButtonsVisible) {
            binding.twoETDialogBtnNegativeButton.visibility = View.VISIBLE
            binding.twoETDialogBtnPositiveButton.visibility = View.VISIBLE
            binding.twoETDialogBtnProgressBar.visibility = View.GONE
        } else {
            binding.twoETDialogBtnNegativeButton.visibility = View.GONE
            binding.twoETDialogBtnPositiveButton.visibility = View.GONE
            binding.twoETDialogBtnProgressBar.visibility = View.VISIBLE
        }
    }
}