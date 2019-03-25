package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.contract.base.ListContract
import com.hyf.intelligence.kotlin.domain.Event
import com.hyf.intelligence.kotlin.domain.Repository

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