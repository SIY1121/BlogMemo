package space.siy.blogmemo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

/**
 * メモを保持するためのオブジェクト
 */
open class Memo (
    @PrimaryKey
    open var id: String = UUID.randomUUID().toString(),
    @Required
    open var title: String = "",
    @Required
    open var content: String = ""
): RealmObject()
