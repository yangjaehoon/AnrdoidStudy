package kr.co.lion.android_p1_yangjaehoon

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class PostData(var title:String?, var currentDate: String?, var body:String?): Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(currentDate)
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostData> {
        override fun createFromParcel(parcel: Parcel): PostData {
            return PostData(parcel)
        }

        override fun newArray(size: Int): Array<PostData?> {
            return arrayOfNulls(size)
        }
    }
    fun delete(list: MutableList<PostData>): Boolean{
        return list.remove(this)
    }
}
