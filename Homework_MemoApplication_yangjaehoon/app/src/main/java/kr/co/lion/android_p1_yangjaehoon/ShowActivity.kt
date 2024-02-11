package kr.co.lion.android_p1_yangjaehoon

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import kr.co.lion.android_p1_yangjaehoon.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {

    lateinit var activityShowBinding: ActivityShowBinding
    lateinit var modifyActivityLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowBinding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(activityShowBinding.root)

        initData()
        setToolbar()
        showContent()
    }

    fun initData(){
        val contract = ActivityResultContracts.StartActivityForResult()
        modifyActivityLauncher = registerForActivityResult(contract){
            val postData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("postData", PostData::class.java)
            } else {
                intent.getParcelableExtra<PostData>("postData")
            }
            Log.d("hello", "What!!!")

            if (it.resultCode == RESULT_OK) {
                val position = intent.getIntExtra("position", -1)
                val title1 = it.data?.getStringExtra("title")
                val body = it.data?.getStringExtra("body")
                Log.d("hello", body!!)
                Log.d("hello", "다왔다")
                postData?.title = title1
                postData?.body = body
                val intent = Intent()
                //intent.putExtra("position", position)
                //intent.putExtra("title", title)
                //intent.putExtra("body", body)
                setResult(RESULT_OK, intent)
                activityShowBinding.textViewTitle.text = "${postData?.title}"
                activityShowBinding.textViewDate.text = "${postData?.currentDate}"
                activityShowBinding.textViewBody.text = "${postData?.body}"

            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun setToolbar(){
        activityShowBinding.apply {
            toolbarShow.apply {
                title = "메모 보기"


                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    val position = intent.getIntExtra("position", -1)
                    val postData = intent.getParcelableExtra("postData", PostData::class.java)
                    val returnIntent = Intent()
                    returnIntent.putExtra("title", postData?.title)
                    returnIntent.putExtra("body", postData?.body)
                    returnIntent.putExtra("position", position)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }

                inflateMenu(R.menu.show_menu)

                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_modify -> {
                            //startActivity(Intent(this@MainActivity, ))
                            val newIntent = Intent(this@ShowActivity, ModifyActivity::class.java)
                            modifyActivityLauncher.launch(newIntent)

                        }

                        R.id.menu_delete -> {
                            val position = intent.getIntExtra("position", -1)
                            if (position != -1) {
                                val deleteIntent = Intent()
                                Log.d("my", position.toString() + "sfd")
                                deleteIntent.putExtra("position", position)
                                setResult(RESULT_OK, deleteIntent)
                                finish()
                            }

                        }
                    }
                    true
                }

            }
        }
    }

    fun showContent() {
        activityShowBinding.apply {

            val postData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("postData", PostData::class.java)
            } else {
                intent.getParcelableExtra<PostData>("postData")
            }

            textViewTitle.text = "${postData?.title}"
            textViewDate.text = "${postData?.currentDate}"
            textViewBody.text = "${postData?.body}"
        }

    }
}