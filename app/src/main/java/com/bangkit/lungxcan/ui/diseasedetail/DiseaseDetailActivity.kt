package com.bangkit.lungxcan.ui.diseasedetail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.request.DiseaseRequest
import com.bangkit.lungxcan.databinding.ActivityDiseaseDetailBinding
import com.bumptech.glide.Glide

class DiseaseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val disease = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_DISEASE, DiseaseRequest::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY_DISEASE)
        }

        if (disease != null) {
            binding.apply {
                tvDefinition.text = "What is ${disease.disease}"
                tvSymptom.text = "Symptoms of ${disease.disease}"
                tvTreatment.text = "Treating ${disease.disease}"
                tvPreventing.text = "Preventing ${disease.disease}"
                tvDetailDefinition.text = disease.definition
                tvDetailPreventing.text = disease.preventive
                tvDetailSymptom.text = disease.symptom
                tvDetailTreatment.text = disease.treatment
                tvDiseaseName.text = disease.disease
                tvReady.text = disease.availability
                btnSeeMore.setOnClickListener {
                    webview.visibility = View.VISIBLE
                    disease.linkRead.let { it1 -> webview.loadUrl(it1) }
                }
                Glide.with(this@DiseaseDetailActivity)
                    .load(disease.image)
                    .into(ivDiseaseDetail)

                if (disease.availability == "NOT READY")
                    tvReady.setTextColor(resources.getColor(R.color.colorError))
            }
        }
    }

    companion object {
        const val KEY_DISEASE = "key_disease"
    }
}