package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.contract.base.ListContract
import com.hyf.intelligence.kotlin.domain.Starred

/**
 * Created by L on 2017/9/21.
 */
interface StarredContract {

    interface IView : ListContract.IView<Starred>

    interface IPresenter : ListContract.IPresenter
}