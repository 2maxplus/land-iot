package com.hyf.iot.contract

import com.hyf.iot.contract.base.ListContract
import com.hyf.iot.domain.Repository

interface RepositoriesContract {

    interface IView : ListContract.IView<Repository>

    interface IPresenter : ListContract.IPresenter
}