package com.hyf.iot.contract

import com.hyf.iot.contract.base.ListContract
import com.hyf.iot.domain.Repository

/**
 * Created by L on 2017/9/27.
 */
interface RepositoriesContract {

    interface IView : ListContract.IView<Repository>

    interface IPresenter : ListContract.IPresenter
}