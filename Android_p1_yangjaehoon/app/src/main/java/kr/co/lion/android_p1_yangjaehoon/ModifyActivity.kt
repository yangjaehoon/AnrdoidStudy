package kr.co.lion.android_p1_yangjaehoon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.co.lion.android_p1_yangjaehoon.databinding.ActivityModifyBinding

class ModifyActivity : AppCompatActivity() {

    lateinit var activityModifyBinding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModifyBinding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(activityModifyBinding.root)


        activityModifyBinding.apply{
            toolbarModify.apply{
                title = "메모 수정"

                inflateMenu(R.menu.check_menu)
                setOnMenuItemClickListener{
                    when(it.itemId){
                        R.id.menuItem1 -> {
                            val newIntent = Intent(this@ModifyActivity, ShowActivity::class.java)
                            newIntent.putExtra("title", editTextTitle.text.toString())
                            newIntent.putExtra("body", editTextBody.text.toString())

                            Log.d("hello", editTextBody.text.toString())
                            setResult(RESULT_OK, newIntent)
                            finish()
                        }
                    }
                    true
                }
            }
        }
    }


}