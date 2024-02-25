package com.teamdontbe.feature.signup

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.colorOf
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.context.openKeyboard
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivitySignUpProfileBinding
import com.teamdontbe.feature.mypage.bottomsheet.MyPageBottomSheet.Companion.MY_PAGE_PROFILE
import com.teamdontbe.feature.signup.SignUpAgreeActivity.Companion.SIGN_UP_AGREE
import com.teamdontbe.feature.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class SignUpProfileActivity :
    BindingActivity<ActivitySignUpProfileBinding>(R.layout.activity_sign_up_profile) {
    private val viewModel by viewModels<SignUpProfileViewModel>()
    private var photoUri: Uri? = null

    // Register ActivityResult handler
    // Permission request handler
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                // Permission granted, get images
                lifecycleScope.launch {
                    try {
                        selectImage()
                    } catch (e: Exception) {
                        // 오류 처리
                        Log.e("YourActivity", "Error fetching images: ${e.message}", e)
                    }
                }
            } else {
                // Permission denied
                // Handle permission denied case
            }
        }

    // 포토피커 사용하는 경우
    /*    private val getPictureLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri: Uri? ->
                imageUri?.let { uri ->
                    binding.ivSignUpProfile.setImageURI(uri)
    //                val requestBody = ContentUriRequestBody(this, uri)
    //                val multipartBody = requestBody.toFormData()
    //                photoUri = multipartBody.toString()
                    photoUri = uri
                    Log.d("SignUpProfileActivity", "uri: $uri")
                }
            }*/
    private val getPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                val imageUri = activityResult.data?.data
                imageUri?.let {
                    binding.ivSignUpProfile.setImageURI(it)
                    photoUri = it
                }
            }
        }

    private fun selectImage() {
        //        포토피커 사용하는 경우
        /*  getPictureLauncher.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
          )*/
        // 갤러리 사용하는 경우
        val getPictureIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        getPictureLauncher.launch(getPictureIntent)
    }

    override fun initView() {
        binding.vm = viewModel

        val flag = initMyPageProfileAppBarTitle()
        initUpdateErrorMessage(flag)
        initNickNameDoubleStateObserve()
        initNextBtnStateObserve(flag)
        initBackBtnClickListener(flag)
        initKeyboardSetting()
        initImagePlusBtnClickListener()
    }

    private fun initImagePlusBtnClickListener() {
        binding.btnSignUpProfilePlus.setOnClickListener {
            // 갤러리 이미지 가져오기
            getGalleryPermission()
        }
    }

    private fun getGalleryPermission() {
        // Permission request logic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(
                arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_VIDEO,
                    READ_MEDIA_VISUAL_USER_SELECTED,
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    private fun initMyPageProfileAppBarTitle(): String {
        return when {
            intent.getStringExtra(MY_PAGE_PROFILE) != null -> {
                initializeMyPageProfile()
                MY_PAGE_PROFILE
            }

            else -> {
                initializeSignUpAgree()
                SIGN_UP_AGREE
            }
        }
    }

    private fun initializeMyPageProfile() = with(binding) {
        setUpInitMyPageProfileUi()
        setUpMyPageProfileViewModelUi()
    }

    private fun setUpInitMyPageProfileUi() = with(binding) {
        val myPageAppBarTitle =
            intent.getStringExtra(MY_PAGE_PROFILE) ?: getString(R.string.my_page_profile_edit)
        appbarSignUp.tvAppbarTitle.text = myPageAppBarTitle
        btnSignUpAgreeNext.text = getString(R.string.my_page_profile_edit_completed)
        etSignUpProfileNickname.setText(viewModel.getUserNickName() ?: "")
    }

    private fun setUpMyPageProfileViewModelUi() {
        viewModel.apply {
            getUserProfileIntroduce()
            checkOnMyPageInitialNickName()
            updateProfileImage()
        }
    }

    private fun updateProfileImage() {
        viewModel.myPageUserInfo.observe(this) {
            loadImage(binding.ivSignUpProfile, it.memberProfileUrl)
        }
    }

    private fun initializeSignUpAgree() {
        binding.appbarSignUp.tvAppbarTitle.text = getString(R.string.sign_up_appbar_title)
        binding.groupSignUpIntroduce.visibility = View.INVISIBLE
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    // 닉네임 중복 체크 메세지
    private fun initUpdateErrorMessage(flag: String) {
        viewModel.isNickNameValid.observe(this) {
            val messageResId =
                if (it) R.string.sign_up_profile_check_text else R.string.sign_up_profile_correct_check
            val textColorResId = if (it) R.color.gray_8 else R.color.error

            updateAgreeText(messageResId, textColorResId)
            initDoubleBtnClickListener(flag, it)
        }
    }

    private fun updateAgreeText(messageResId: Int, textColorResId: Int) {
        binding.tvSignUpAgreeMessage.apply {
            text = context.getString(messageResId)
            setTextColor(colorOf(textColorResId))
        }
    }

    // 중복 확인 버튼 클릭
    private fun initDoubleBtnClickListener(flag: String, btnAvailable: Boolean) {
        binding.btnSignUpProfileDoubleCheck.setOnClickListener {
            viewModel.getNickNameDoubleCheck(binding.etSignUpProfileNickname.text.toString())
            handleFlag(flag, btnAvailable)
        }
    }

    // 프로필 편집, 마이페이지 프로필 편집 분기처리
    private fun handleFlag(flag: String, btnAvailable: Boolean) {
        if (btnAvailable) {
            when (flag) {
                SIGN_UP_AGREE -> hideKeyboard(binding.root)
                MY_PAGE_PROFILE -> focusOnIntroduceEditText()
            }
        }
    }

    // editText 자동 포커스
    private fun focusOnIntroduceEditText() {
        binding.etSignUpAgreeIntroduce.apply {
            requestFocus()
            setSelection(this.text.length)
        }
    }

    private fun initNickNameDoubleStateObserve() {
        viewModel.nickNameDoubleState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> updateErrorMessage(it.data)
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun updateErrorMessage(doubleCheck: Boolean) {
        val messageResId =
            if (doubleCheck) R.string.sign_up_profile_use_posssible
            else R.string.sign_up_profile_use_impossible
        val textColorResId = if (doubleCheck) R.color.primary else R.color.error

        updateAgreeText(messageResId, textColorResId)
    }

    private fun initNextBtnStateObserve(flag: String) {
        binding.btnSignUpAgreeNext.setOnClickListener {
            handleSelectedButton(flag)
        }
    }

    private fun handleSelectedButton(flag: String) {
        val nickName = viewModel.nickName.value.orEmpty()
        val optionalAgreementInSignUp = intent.getBooleanExtra(SIGN_UP_AGREE, false)
        val introduceText = viewModel.introduceText.value.orEmpty()
        val imgUrl = makeImageFile(photoUri)
        Log.d("imgUrl", imgUrl.toString())
        Log.d("photoUri", photoUri.toString())

        viewModel.saveUserNickNameInLocal(nickName)

        handleSignUpAgree(
            nickName = nickName,
            optionalAgreement = optionalAgreementInSignUp,
            introduce = introduceText,
            imgUrl = imgUrl
        )

        /*    when (flag) {
                SIGN_UP_AGREE -> handleSignUpAgree(
                    nickName = nickName,
                    optionalAgreement = optionalAgreementInSignUp,
                    introduce = introduceText,
                    imgUrl = imgUrl
                )
                // 마이페이지 인 경우 선택 동의 null
                MY_PAGE_PROFILE -> handleMyPageProfile(
                    nickName = nickName,
                    introduce = introduceText,
                    imgUrl = imgUrl
                )
            }*/
    }

    private fun makeImageFile(uri: Uri?): File? {
        // 파일 스트림으로 uri로 접근해 비트맵을 디코딩
        if (uri == null) return null
        val bitmap = contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it)
        }

        // 캐시 파일 생성
        val tempFile =
            File.createTempFile("file", ".jpg", cacheDir)

        // 파일 스트림을 통해 파일에 비트맵 저장
        FileOutputStream(tempFile).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return tempFile
    }

    private fun handleSignUpAgree(
        nickName: String,
        optionalAgreement: Boolean,
        introduce: String,
        imgUrl: File?
    ) {
        viewModel.patchUserProfileUri(
            ProfileEditInfoEntity(
                nickName,
                optionalAgreement,
                introduce,
            ),
            imgUrl
        )
        finish()

//        navigateToMainActivity(setUpUserProfile(nickName, optionalAgreement, introduce, null))
    }

    /*  private fun handleMyPageProfile(nickName: String, introduce: String, imgUrl: String?) {
          Log.d("uir", imgUrl.toString())
          viewModel.patchUserProfileUri(
              ProfileEditInfoEntity(
                  nickName,
                  null,
                  introduce,
              ),
              imgUrl
          )
          finish()
      }*/

    private fun Uri.getFilePath(): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = applicationContext.contentResolver.query(this, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }

    private fun navigateToMainActivity(userProfile: UserProfileModel) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(SIGN_UP_AGREE, userProfile)
        startActivity(intent)
        finish()
    }

    private fun setUpUserProfile(
        inputNickName: String,
        allowedCheck: Boolean,
        introduce: String,
        imgUrl: String?
    ): UserProfileModel {
        return UserProfileModel(
            inputNickName,
            allowedCheck,
            introduce,
            imgUrl
        )
    }

    private fun initBackBtnClickListener(flag: String) {
        when (flag) {
            SIGN_UP_AGREE -> {
                binding.appbarSignUp.btnAppbarBack.setOnClickListener {
                    finish()
                }
            }

            MY_PAGE_PROFILE -> {
                binding.appbarSignUp.btnAppbarBack.setOnClickListener {
                    // 이전 프레그먼트로 돌아가는 코드
                    onBackPressedDispatcher.onBackPressed()
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun initKeyboardSetting() {
        hideKeyboardOnClickBackground()
        requestFocusIntroduceEditText()
    }

    private fun hideKeyboardOnClickBackground() {
        binding.clSignUpProfileRoot.setOnClickListener {
            hideKeyboard(binding.root)
        }
    }

    private fun requestFocusIntroduceEditText() = with(binding) {
        etSignUpAgreeIntroduce.setOnClickListener {
            openKeyboard(etSignUpAgreeIntroduce)
        }
    }
}
