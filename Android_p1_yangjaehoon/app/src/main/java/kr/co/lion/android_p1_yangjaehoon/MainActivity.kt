package kr.co.lion.android_p1_yangjaehoon

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.android_p1_yangjaehoon.databinding.ActivityMainBinding
import kr.co.lion.android_p1_yangjaehoon.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var writeActivityLauncher: ActivityResultLauncher<Intent>
    lateinit var showActivityLauncher: ActivityResultLauncher<Intent>


    val postList = mutableListOf<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initData()
        setToolBar()
        initView()

    }


    fun initData() {
        val contract1 = ActivityResultContracts.StartActivityForResult()
        writeActivityLauncher = registerForActivityResult(contract1) {
            if (it.resultCode == RESULT_OK) {
                if (it.data != null) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                        val postData = it.data?.getParcelableExtra("postData", PostData::class.java)
                        postList.add(postData!!)
                        activityMainBinding.recylcerViewResult.adapter?.notifyDataSetChanged()
                    } else {
                        val postData = it.data?.getParcelableExtra<PostData>("postData")
                        postList.add(postData!!)
                        activityMainBinding.recylcerViewResult.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showActivityLauncher = registerForActivityResult(contract2) {
            if (it.resultCode == RESULT_OK) {
                Log.d("my", "잘들어옴")
                val postIndex = it.data?.getIntExtra("position", -1)
                val title = it.data?.getStringExtra("title", )?: ""
                val body = it.data?.getStringExtra("body", )?: ""
                Log.d("my", postIndex.toString() + "잘들어옴")
                if (postIndex != -1 && title == null) {//삭제했을때 처리
                    postList.removeAt(postIndex!!)
                    activityMainBinding.recylcerViewResult.adapter?.notifyItemRemoved(
                        postIndex
                    )
                }
                else{//수정하고 돌아갈때 업데이트 해주기위한 처리
                    val postData = it.data?.getParcelableExtra<PostData>("postData")
                    postList[postIndex!!].title = title
                    postList[postIndex].body = body

                    activityMainBinding.recylcerViewResult.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    fun setToolBar() {
        activityMainBinding.apply {
            toolbar.apply {
                title = "메모 관리"
                inflateMenu(R.menu.main_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuItem1 -> {
                            val newIntent = Intent(this@MainActivity, WriteActivity::class.java)
                            writeActivityLauncher.launch(newIntent)
                        }
                    }
                    true

                }
            }
        }
    }

    fun initView() {
        activityMainBinding.apply {
            recylcerViewResult.apply {
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {

        inner class ViewHolderClass(rowMainBinding: RowMainBinding) :
            RecyclerView.ViewHolder(rowMainBinding.root) {
            val rowMainBinding: RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding

                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                this.rowMainBinding.root.setOnClickListener{
                    val showIntent = Intent(this@MainActivity, ShowActivity::class.java)

                    showIntent.putExtra("postData", postList[adapterPosition])
                    showIntent.putExtra("position", adapterPosition)
                    Log.d("my", adapterPosition.toString())
                    showActivityLauncher.launch(showIntent)

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            return ViewHolderClass(rowMainBinding)
        }

        override fun getItemCount(): Int {
            return postList.size
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.rowMainBinding.textView1.text = "${postList[position].title}"
            holder.rowMainBinding.textView2.text = "${postList[position].currentDate.toString()}"

        }
    }

}