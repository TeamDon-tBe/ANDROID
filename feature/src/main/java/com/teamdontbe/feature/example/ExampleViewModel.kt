package com.teamdontbe.feature.example

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.domain.repository.ExampleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel
    @Inject
    constructor(
        private val exampleRepository: ExampleRepository,
    ) : ViewModel() {
        fun getRecyclerviewTest(page: Int): Flow<PagingData<UserEntity>> = exampleRepository.getExample(page)
    }
