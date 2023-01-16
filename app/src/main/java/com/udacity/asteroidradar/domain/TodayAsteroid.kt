package com.udacity.asteroidradar.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "toady_asteroid")
@Parcelize
class TodayAsteroid(
    @PrimaryKey()
    val date: String,
    val url: String,
    val title: String

) : Parcelable {

}