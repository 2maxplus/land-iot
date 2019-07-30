package com.hyf.iot.contract

import com.hyf.iot.contract.base.ListContract
import com.hyf.iot.domain.Event
import com.hyf.iot.domain.Repository


interface EventsContract {

    interface IView : ListContract.IView<Event> {
        fun setRepos(repos: Repository?)
    }

    interface IPresenter : ListContract.IPresenter {
        fun getReposFromUrl(url: String)
    }
}