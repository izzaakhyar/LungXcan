package com.bangkit.lungxcan

import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.lungxcan.databinding.ActivityMainBinding
import com.bangkit.lungxcan.ml.TfliteModel
import com.bangkit.lungxcan.ui.setting.SettingFragment
import com.bangkit.lungxcan.ui.setting.SettingPreferences
import com.bangkit.lungxcan.ui.setting.SettingViewModel
import com.bangkit.lungxcan.ui.setting.SettingViewModelFactory
import com.bangkit.lungxcan.ui.setting.dataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {

        val fromSet = SettingFragment.fromSetting

        if (!fromSet) {
            val isDarkModeActive = intent.getBooleanExtra("isDarkModeActive", false)

            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_history, R.id.navigation_scan, R.id.navigation_article, R.id.navigation_setting
//            )
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.title = when (View.NO_ID) {
            R.id.navigation_article -> getString(R.string.title_article)
            else -> {
                getString(R.string.app_name)
            }
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        setThemeSetting(settingViewModel)

//        try {
//            tflite = Interpreter(loadModelFile())
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        loadImageFromUrlAndRunInference("https://picsum.photos/200/300.jpg")

    }

//    @Throws(IOException::class)
//    private fun loadModelFile(): MappedByteBuffer {
//        val fileDescriptor: AssetFileDescriptor = assets.openFd("tflite_model.tflite")
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel: FileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    private fun loadImageFromUrlAndRunInference(imageUrl: String) {
//        Glide.with(this)
//            .asBitmap()
//            .load(imageUrl)
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    runInference(resource)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // Handle if needed
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    Log.e("MainActivity", "Failed to load image from URL")
//                }
//            })
//    }
//
//    private fun runInference(bitmap: Bitmap) {
//        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
//
//        val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
//        for (y in 0 until 224) {
//            for (x in 0 until 224) {
//                val pixel = resizedBitmap.getPixel(x, y)
//                input[0][y][x][0] = (pixel shr 16 and 0xFF) / 255.0f
//                input[0][y][x][1] = (pixel shr 8 and 0xFF) / 255.0f
//                input[0][y][x][2] = (pixel and 0xFF) / 255.0f
//            }
//        }
//
//        // Update the output array shape to match the model's output tensor shape
//        val output = Array(1) { FloatArray(7) }
//
//        tflite.run(input, output)
//
//        // Process the output as needed
//    }


    private fun setThemeSetting(settingViewModel: SettingViewModel) {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}