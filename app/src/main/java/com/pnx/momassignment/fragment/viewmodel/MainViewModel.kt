package com.pnx.momassignment.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.pnx.momassignment.R
import com.pnx.momassignment.activity.ActivityViewModel
import com.pnx.momassignment.livedata.ActivityDataSource
import com.pnx.momassignment.network.NetworkService
import com.pnx.momassignment.room.RoomDBClient
import com.pnx.momassignment.room.model.UserItem

/**
 * MainViewModel
 * @property dataSource IDataSource : ActivityViewModel interface
 * @constructor
 */
class MainViewModel(private val dataSource: ActivityDataSource) : ViewModel() {
    private val TAG = this::class.simpleName

    // 메인에서 하단 네비게이션 바 선택된 메뉴 TAG
    private val _selectedMainTabMenu = MutableLiveData(R.id.main_tab1)
    val selectedMainTabMenu: LiveData<Int> = _selectedMainTabMenu


    // 마케팅 동의여부
    private val _searchUserList = MutableLiveData<MutableList<UserItem>>()
    val searchUserList: LiveData<MutableList<UserItem>> get() = _searchUserList



    /**
     * 선택 된 메뉴 저장ㅅㄷㄴㅅ
     */
    fun setSelectedMenuId(selectedMenuId: Int) {
        _selectedMainTabMenu.value = selectedMenuId
    }

    fun getUserList(name:String){
        dataSource.launchNetworkLoad(viewModelScope) {
            val result = NetworkService().getApiService().userInfo(
                q = name,
                sort = "",
                order = "",
                per_page = 100,
                page = 1
            )
            _searchUserList.value = result.items
        }
    }
    fun saveUser(context: Context){
        dataSource.launchNetworkLoad(viewModelScope) {
            searchUserList.value?.let {
                it.map { userItem ->
                    Log.d("TEST", "userItem.id = ${userItem.id}")
                    Log.d("TEST", "userItem.login = ${userItem.login}")
                    val db = RoomDBClient.getUserDatabase(context)
                    val localId = db.userDao().getUserId(id = userItem.id)
                    Log.d("TEST", "localId = $localId")
                    if (localId == null) {
                        db.userDao().insert(userItem)
                    } else {
                        db.userDao().update(userItem)
                    }
                }
            }
        }
    }
    fun getUser(context: Context, block:suspend (ArrayList<UserItem>)-> Unit) {
        dataSource.launchNetworkLoad(viewModelScope) {
            val db = RoomDBClient.getUserDatabase(context)
            block(ArrayList(db.userDao().getAll()))
        }
    }
}

/**
 * 상세보기 ViewModel 생성
 *
 * @property activiyModel ActivityViewModel : Activity에서 사용되고 있는 ViewModel progress등 공통 영역을 담당 한다.
 * @constructor
 */
class MainVMFactory(private val activiyModel: ActivityViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(ActivityDataSource(activiyModel)) as T
    }
}

//class BaseActivityViewModelFactory(private val activiyModel: ActivityViewModel) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ActivityDataSource::class.java)) {
////            val resource = DataSource.getDataSource(context.resources)
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel(ActivityDataSource(activiyModel) ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}