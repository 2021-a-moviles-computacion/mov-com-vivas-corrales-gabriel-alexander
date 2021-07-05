package com.example.moviles_comp_2021_p1

import android.os.Parcel
import android.os.Parcelable

class DLiga(val nombre: String?,val descripcion: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<DLiga> {
        override fun createFromParcel(parcel: Parcel): DLiga {
            return DLiga(parcel)
        }

        override fun newArray(size: Int): Array<DLiga?> {
            return arrayOfNulls(size)
        }
    }
}