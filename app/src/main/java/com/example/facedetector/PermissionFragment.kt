package com.example.facedetector

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)


class PermissionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions(requireContext())) {
            permissionsResultCallback.launch(PERMISSIONS_REQUIRED)
        } else {
            // If permissions have already been granted, proceed
            navigateToCamera()
        }
    }

    private fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all {
            it.value
        }
        if (granted) {
            navigateToCamera()
        } else {
            context?.run {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert)
                builder.setMessage(R.string.permission_alert_msg)
                builder.setPositiveButton(R.string.ok) { dialog, which ->
                    activity?.finish()
                    showPermRationale()
                }
                builder.show()
            }
        }
    }

    private fun showPermRationale() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package", requireActivity().packageName, null
        )
        intent.data = uri
        requireActivity().startActivity(intent)
    }


    private fun navigateToCamera() {
        findNavController().navigate(R.id.action_permissionFragment_to_cameraFragment)
    }

    companion object {
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}