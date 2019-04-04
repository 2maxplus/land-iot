package com.hyf.intelligence.iot.contract

import com.hyf.intelligence.iot.contract.base.ListContract
import com.hyf.intelligence.iot.domain.Event
import com.hyf.intelligence.iot.domain.Repository

/**
 * Created by L on 2017/9/14.
 */
interface EventsContract {

    interface IView : ListContract.IView<Event> {
        fun setRepos(repos: Repository?)
    }

    interface IPresenter : ListContract.IPresenter {
        fun getReposFromUrl(url: String)
    }
}