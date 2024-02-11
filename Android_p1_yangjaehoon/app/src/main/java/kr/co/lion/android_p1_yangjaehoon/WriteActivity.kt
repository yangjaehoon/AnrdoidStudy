package kr.co.lion.android_p1_yangjaehoon

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_p1_yangjaehoon.databinding.ActivityWriteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.concurrent.thread

class WriteActivity : AppCompatActivity() {

    lateinit var activityWriteBinding: ActivityWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityWriteBinding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(activityWriteBinding.root)

        setToolbar()
        setView()


    }

    fun setToolbar() {
        activityWriteBinding.apply {
            toolbarWrite.apply {
                title = "메모 작성"

                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                inflateMenu(R.menu.check_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuItem1 -> {
//                            val newIntent = Intent(this@WriteActivity, MainActivity::class.java)
//                            startActivity(newIntent)
                            processWriteDone()
                        }
                    }
                    true
                }
            }
        }
    }

    fun setView() {
        activityWriteBinding.apply {
            textFieldInputTitle.requestFocus()
            showSoftInput(textFieldInputTitle)


            textFieldInputBody.setOnEditorActionListener { v, actionId, event ->
                processWriteDone()
                true
            }
        }
    }

    fun showSoftInput(focusView: TextInputEditText) {
        thread {
            SystemClock.sleep(1000)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(focusView, 0)
        }
    }

    fun processWriteDone() {
        activityWriteBinding.apply {
            val title = textFieldInputTitle.text.toString()
            val body = textFieldInputBody.text.toString()
            val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormat.timeZone = koreaTimeZone
            val currentDate = dateFormat.format(Date())


            if (title.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요", textFieldInputTitle)
                return
            }
            if (body.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요", textFieldInputBody)
                return
            }

            val postData = PostData(title, currentDate, body)
            Snackbar.make(activityWriteBinding.root, "등록이 완료되었습니다", Snackbar.LENGTH_SHORT).show()
            // 이전으로 돌아간다.
            val resultIntent = Intent()
            resultIntent.putExtra("postData", postData)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    fun showDialog(title: String, message: String, focusView: TextInputEditText) {
        val builder = MaterialAlertDialogBuilder(this@WriteActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                focusView.setText("")
                focusView.requestFocus()
                showSoftInput(focusView)
            }
        }
        builder.show()
    }

}