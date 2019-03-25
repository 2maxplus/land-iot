package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.contract.base.ListContract
import com.hyf.intelligence.kotlin.domain.Repository

/**
 * Created by L on 2017/9/27.
 */
interface RepositoriesContract {

    interface IView : ListContract.IView<Repository>

    interface IPresenter : ListContract.IPresenter
}