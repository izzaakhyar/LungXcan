package com.bangkit.lungxcan.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiseaseRequest(
    val disease: String,
    val image: String,
    val availability: String,
    val linkRead: String,
    val definition: String,
    val symptom: String,
    val treatment: String,
    val preventive: String,
    val diseaseIcon: String
): Parcelable