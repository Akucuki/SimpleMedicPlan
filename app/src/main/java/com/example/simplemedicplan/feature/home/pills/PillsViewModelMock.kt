package com.example.simplemedicplan.feature.home.pills

import kotlinx.coroutines.channels.Channel

interface PillsViewModelMock {
    val events: Channel<PillsEvents>

    fun onAddButtonClick()
}

class PillsViewModelMockImpl : PillsViewModelMock {

    override val events: Channel<PillsEvents> = Channel(Channel.UNLIMITED)

    override fun onAddButtonClick() {}
}