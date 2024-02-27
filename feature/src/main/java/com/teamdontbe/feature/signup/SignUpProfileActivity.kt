package com.teamdontbe.feature.signup

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.colorOf
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.context.openKeyboard
import com.teamdontbe.core_ui.util.intent.navigateTo
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.feature.ErrorActivity.Companion.navigateToErrorPage
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivitySignUpProfileBinding
import com.teamdontbe.feature.mypage.bottomsheet.MyPageBottomSheet.Companion.MY_PAGE_PROFILE
import com.teamdontbe.feature.signup.SignUpAgreeActivity.Companion.SIGN_UP_AGREE
import com.teamdontbe.feature.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class SignUpProfileActivity :
    BindingActivity<ActivitySignUpProfileBinding>(R.layout.activity_sign_up_profile) {
    private val viewModel by viewModels<SignUpProfileViewModel>()
    private var photoUri: Uri? = null

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
                    }
                }
            }
        }

    // 포토피커 사용하는 경우
    /*    private val getPictureLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri: Uri? ->
                imageUri?.let { uri ->
                    binding.ivSignUpProfile.setImageURI(uri)
                    photoUri = uri
                }
            }*/
    private val getPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                val imageUri = activityResult.data?.data
                imageUri?.let {
                    binding.ivSignUpProfile.load(it)
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

        lifecycleScope.launch {
            val imgUrl = uriToTempFileWithCoil(photoUri)

            when (flag) {
                SIGN_UP_AGREE -> handleSignUpAgree(
                    nickName = nickName,
                    optionalAgreement = optionalAgreementInSignUp,
                    introduce = introduceText,
                    imgUrl = imgUrl
                )

                MY_PAGE_PROFILE -> handleMyPageProfile(
                    nickName = nickName,
                    introduce = introduceText,
                    imgUrl = imgUrl
                )
            }
        }
    }

    private suspend fun uriToTempFileWithCoil(uri: Uri?): File? = withContext(Dispatchers.IO) {
        if (uri == null) return@withContext null

        // Coil 이미지 로더 초기화
        val imageLoader = ImageLoader(this@SignUpProfileActivity)

        // 이미지 요청 생성
        val request = ImageRequest.Builder(this@SignUpProfileActivity)
            .data(uri)
            .build()

        // 이미지 로드
        val result = (imageLoader.execute(request) as SuccessResult).drawable

        // 비트맵을 ByteArray로 압축
        val bitmap = (result as BitmapDrawable).bitmap
        var stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var byteArr = stream.toByteArray()

        // 이미지 크기가 3MB를 초과하는 경우 압축하여 크기 줄이기
        var quality = 100
        while (byteArr.size > 3 * 1024 * 1024) {
            stream = ByteArrayOutputStream()

            // 퀄리티를 10% 감소
            quality = (quality * 0.9).toInt()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            byteArr = stream.toByteArray()
        }

        // 캐시 파일 생성
        val tempFile = File.createTempFile("file", ".jpg", this@SignUpProfileActivity.cacheDir)

        // 압축된 이미지를 파일에 저장
        FileOutputStream(tempFile).use {
            it.write(byteArr)
        }

        return@withContext tempFile
    }

    private suspend fun handleSignUpAgree(
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
        // Profile edit 성공 여부를 대기하고, timeout을 설정하여 처리
        val success = withTimeoutOrNull(10_000) {
            viewModel.profileEditSuccess.first { it }
        }

        // Profile edit이 성공했을 경우에만 UI 업데이트 및 네비게이션 수행
        if (success == true) {
            finish()
            navigateTo<MainActivity>(this@SignUpProfileActivity)
        } else {
            // Profile edit이 실패한 경우에 대한 처리
            navigateToErrorPage(this@SignUpProfileActivity)
        }
    }

    private suspend fun handleMyPageProfile(nickName: String, introduce: String, imgUrl: File?) {
        viewModel.patchUserProfileUri(
            ProfileEditInfoEntity(
                nickName,
                null,
                introduce,
            ),
            imgUrl
        )
        val success = withTimeoutOrNull(10_000) {
            viewModel.profileEditSuccess.first { it }
        }

        if (success == true) {
            finish()
        } else {
            navigateToErrorPage(this@SignUpProfileActivity)
        }
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

    private fun deleteCache() {
        // 캐시 파일을 삭제
        cacheDir.listFiles()?.forEach {
            it.delete()
        }
        // 삭제할 폴더 경로
        val imageCacheDir = File(cacheDir, "image_cache")

        // 해당 폴더가 존재하고 디렉토리인지 확인
        if (imageCacheDir.exists() && imageCacheDir.isDirectory) {
            // 폴더 내의 모든 파일을 반복하면서 삭제
            imageCacheDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }

    override fun onDestroy() {
        deleteCache()
        super.onDestroy()
    }
}
