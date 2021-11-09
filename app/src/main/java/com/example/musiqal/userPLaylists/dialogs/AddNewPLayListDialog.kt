package com.example.musiqal.userPLaylists.dialogs

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.musiqal.databinding.AddNewPlaylistDialogItemBinding

class AddNewPLayListDialog(private val context: Context) {
    private val TAG = "AddNewPLayListDialog11"
     private var dialog: Dialog
     private var binding: AddNewPlaylistDialogItemBinding

    init {
        dialog = Dialog(context)
        val layoutInlater = LayoutInflater.from(context)
        binding = AddNewPlaylistDialogItemBinding.inflate(layoutInlater)
        dialog.setContentView(binding.root)
        maximizeDialogWidth()

    }

    fun createDialog(addNewPLayListDialogClickLister: AddNewPLayListDialogClickLister) {
        disableButtons()
        addWatcherOnET()
        addOETImoOptions()
        binding.addNewPlayListDialogBtnOK.setOnClickListener {

            val playListName = binding.addNewPlayListDialogETListName.text.toString()
            addNewPLayListDialogClickLister.onOkButtonClick(playListName)
        }
        binding.addNewPlayListDialogBtnCancel.setOnClickListener {
            val playListName = binding.addNewPlayListDialogETListName.text.toString()
            addNewPLayListDialogClickLister.onCancelButtonClick(playListName)
            dismissDialog()
        }



    }

    private fun maximizeDialogWidth() {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams


    }

    private fun addOETImoOptions() {
        binding.addNewPlayListDialogETListName.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeys(binding.root)
                    return true
                }
                return false
            }
        })
    }

    private fun hideSoftKeys(view: View) {

        kotlin.runCatching {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            var currentview = (context as AppCompatActivity).currentFocus
            if (currentview == null) {
                currentview = View(context)
            }
            inputMethodManager.hideSoftInputFromWindow(currentview.windowToken, 0)

        }.onFailure {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        }

    }

    private fun addWatcherOnET() {
        binding.addNewPlayListDialogETListName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 0) {
                    enableButtons()
                } else {
                    disableButtons()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    fun showDialog(cancellable: Boolean) {
        dialog.setCancelable(cancellable)
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

    private fun disableButtons() {
        binding.addNewPlayListDialogBtnOK.isEnabled = false
    }

    private fun enableButtons() {
        binding.addNewPlayListDialogBtnOK.isEnabled = true
    }
}