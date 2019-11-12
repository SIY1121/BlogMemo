package space.siy.blogmemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mRealm: Realm
    lateinit var adapter: MemoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //// Realm初期化
        Realm.init(this)
        mRealm = Realm.getDefaultInstance()
        ////

        //// RecyclerView準備
        adapter = MemoListAdapter(mRealm.where(Memo::class.java).findAll(), listener, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        ////

        /* 追加ボタンクリック時の動作 */
        floatingActionButton_add.setOnClickListener {
            val view = this@MainActivity.layoutInflater.inflate(R.layout.dialog_edit, null)
            val titleEditText: EditText = view.findViewById(R.id.editText_title)
            val contentEditText: EditText = view.findViewById(R.id.editText_content)
            // 作成ダイアログを表示
            AlertDialog.Builder(this@MainActivity)
                .setView(view)
                .setTitle("作成")
                .setPositiveButton("追加") { _, _ ->
                    mRealm.executeTransaction {
                        val memo = mRealm.createObject(Memo::class.java, UUID.randomUUID().toString()).apply {
                            title = titleEditText.text.toString()
                            content = contentEditText.text.toString()
                        }
                        it.copyToRealm(memo)
                    }
                    adapter.notifyItemInserted(adapter.data.size)
                }
                .setNegativeButton("キャンセル", null)
                .show()
        }
    }

    /**
     * 個々のアイテムをクリックしたときの処理
     */
    private val listener = object : MemoListAdapter.Listener {
        /**
         * 削除ボタンクリック時の処理
         */
        override fun onDelete(memo: Memo) {
            if (!memo.isValid) return // 削除ボタン連打対応

            val index = adapter.data.indexOf(memo)
            mRealm.executeTransaction {
                memo.deleteFromRealm()
            }
            adapter.notifyItemRemoved(index)
        }

        /**
         * 編集時の処理
         */
        override fun onEdit(memo: Memo) {
            val view = this@MainActivity.layoutInflater.inflate(R.layout.dialog_edit, null)
            val titleEditText: EditText = view.findViewById(R.id.editText_title)
            val contentEditText: EditText = view.findViewById(R.id.editText_content)
            titleEditText.setText(memo.title)
            contentEditText.setText(memo.content)

            AlertDialog.Builder(this@MainActivity)
                .setView(view)
                .setTitle("編集")
                .setPositiveButton("保存") { _, _ ->
                    val index = adapter.data.indexOf(memo)
                    mRealm.executeTransaction {
                        memo.title = titleEditText.text.toString()
                        memo.content = contentEditText.text.toString()
                    }
                    adapter.notifyItemChanged(index)
                }
                .setNegativeButton("キャンセル", null)
                .show()
        }
    }
}
