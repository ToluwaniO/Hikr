package com.tpad.hikr.DataClasses

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by oguns on 7/18/2017.
 */
data class HikeLocationData(var name: String, var placeId: String, var address: String, var latitude : Double, var longitude: Double,
            var email: String, var phoneNumber: String, var rating: Double, var city: String, var distance: Int = 0)
    : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(placeId)
        parcel.writeString(address)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeDouble(rating)
        parcel.writeString(city)
        parcel.writeInt(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HikeLocationData> {
        override fun createFromParcel(parcel: Parcel): HikeLocationData {
            return HikeLocationData(parcel)
        }

        override fun newArray(size: Int): Array<HikeLocationData?> {
            return arrayOfNulls(size)
        }
    }

    constructor() : this("", "", "", 0.0, 0.0,
    "", "", 0.0, "", 0)
    constructor(city: String) : this("", "", "", 0.0, 0.0,
            "", "", 0.0, city, 0)

}