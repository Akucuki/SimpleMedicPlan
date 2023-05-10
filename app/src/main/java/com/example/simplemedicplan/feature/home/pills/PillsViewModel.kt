package com.example.simplemedicplan.feature.home.pills

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class PillsViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel(), PillsViewModelMock {

    override val events = Channel<PillsEvents>(Channel.UNLIMITED)

    override fun onAddButtonClick() {

    }
}