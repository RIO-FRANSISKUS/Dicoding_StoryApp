package com.example.storyapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.setPadding
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityUploadStoryBinding
import com.example.storyapp.ui.dashboard.DashboardActivity
import com.example.storyapp.util.createCustomTempFile
import com.example.storyapp.util.rotateImage
import com.example.storyapp.util.uriToFile
import com.example.storyapp.util.visible
import java.io.File
import java.io.FileOutputStream

class UploadStoryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUploadStoryBinding

    private lateinit var currentPhotoPath: String
    private val uploadStoryViewModel by viewModels<UploadStoryViewModel>()

    private var getFile: File? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                val bitmap = BitmapFactory.decodeFile(file.path)
                rotateImage(bitmap, currentPhotoPath).compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    FileOutputStream(file)
                )
                uploadStoryViewModel.setFile(file)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadStoryActivity)
                uploadStoryViewModel.setFile(myFile)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.uploadProgressBar.visible(false)

        binding.apply {
            btnCamera.setOnClickListener{
                startTakePhoto()
            }

            btnGallery.setOnClickListener {
                openGallery()
            }

            buttonAdd.setOnClickListener{
                edAddDescription.clearFocus()
                uploadStory()
            }
        }

        setUpViewModel()

    }

    private fun uploadStory() {
        when {
            getFile == null -> Toast.makeText(this, "Tolong masukkan gambar", Toast.LENGTH_SHORT).show()
            binding.edAddDescription.text.isNullOrBlank() -> {
                Toast.makeText(this, "Tolong masukkan deskripsi", Toast.LENGTH_SHORT).show()
                binding.edAddDescription.requestFocus()
            }
            else -> {
                val file = getFile as File
                val description = binding.edAddDescription.text.toString()
                uploadStoryViewModel.postStory(file, description)
            }
        }
    }

    private fun setUpViewModel(){
        uploadStoryViewModel.isPosted.observe(this) {
            if (it) {
                binding.uploadProgressBar.visible(false)
                Toast.makeText(this, "Story anda berhasil di upload", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                binding.uploadProgressBar.visible(false)
                Toast.makeText(this, "Story gagal di upload", Toast.LENGTH_SHORT).show()
            }
        }

        uploadStoryViewModel.hasUploaded.observe(this){
            if (it != null) {
                Log.d(TAG, "true - with file")
                getFile = it
                binding.ivImageStory.setImageBitmap(BitmapFactory.decodeFile(it.path))
                binding.ivImageStory.setPadding(0)
            }
            else {
                binding.ivImageStory.setImageResource(R.drawable.baseline_image_24)
                binding.ivImageStory.setPadding(32)
                Log.d(TAG, "true - without file")
            }
        }

        uploadStoryViewModel.isLoading.observe(this){
            binding.uploadProgressBar.visible(true)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoUri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "com.example.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "UploadStoryActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}