package com.example.musiqal.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.musiqal.R
import com.example.musiqal.databinding.YesOrNoItemLayoutDialogBinding
import com.example.musiqal.dialogs.util.OnDialogButtonsClickListener
import android.view.WindowManager


class SimpleYesOrNoDialog(private val context: Context) {
  private  var yesOrNoItemLayoutDialogBinding: YesOrNoItemLayoutDialogBinding
    private var dialog: Dialog
    private lateinit var onDialogButtonsClickLister: OnDialogButtonsClickListener

    init {

        if (context is OnDialogButtonsClickListener) {
            onDialogButtonsClickLister = context
        } else {
//            throw Throwable("must implement OnDialogButtonsClickListener")
        }

        val inflater =
            LayoutInflater.from(context).inflate(R.layout.yes_or_no_item_layout_dialog, null)

        yesOrNoItemLayoutDialogBinding = YesOrNoItemLayoutDialogBinding.bind(inflater)
        dialog = Dialog(context)
        dialog.setContentView(yesOrNoItemLayoutDialogBinding.root)



    }

    public fun intialize(
        mainTextView: String,
        subTextView: String,
        btnPositiveText: String,
        btnNegativeText: String,
        position: Int,
        isButtonsVisible: Boolean = false,
    ) {

        val layoutParams=WindowManager.LayoutParams()
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height =WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes=layoutParams


        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofBtnOk.text = btnPositiveText
        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofBtnCancel.text = btnNegativeText
        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofTVMainText.text = mainTextView
        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofTVSubText.text = subTextView

        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofBtnOk.visibility=if (isButtonsVisible) View.VISIBLE else View.GONE
        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofBtnCancel.visibility=if (isButtonsVisible) View.VISIBLE else View.GONE
        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofProgressBar.visibility=if (isButtonsVisible) View.GONE else View.VISIBLE

        initButtonsListeners(position)
    }

    private fun initButtonsListeners(position: Int) {
        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofBtnOk.setOnClickListener {
            onDialogButtonsClickLister.onPositiveButtonClick(position)
        }

        yesOrNoItemLayoutDialogBinding.simpleYesNoDialofBtnCancel.setOnClickListener {
            onDialogButtonsClickLister.onNegativeButtonClick(position)
        }
    }

    fun show(cancelable: Boolean) {
        dialog.setCancelable(cancelable)
try {
    dialog.show()

}
catch (e:Exception)
{
    dismis()
}

    }

    fun dismis() {
        dialog.dismiss()
    }

}