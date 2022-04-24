package com.pnx.momassignment.fragment

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.pnx.momassignment.R
import com.pnx.momassignment.activity.ActivityViewModel
import com.pnx.momassignment.livedata.ActivityDataSource
import com.pnx.momassignment.network.NetworkService
import com.pnx.momassignment.room.RoomDBClient
import com.pnx.momassignment.room.data.LoadDiv
import com.pnx.momassignment.room.data.UserItem
import com.pnx.momassignment.room.data.UserItemPaginSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

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


    // API 검색 유져
    private val _searchUserList = MutableLiveData<PagingData<UserItem>>()
    val searchUserList: LiveData<PagingData<UserItem>> get() = _searchUserList

    // 로컬 검색 유져
    private val _localSearchUserList = MutableLiveData<PagingData<UserItem>>()
    val localSearchUserList: LiveData<PagingData<UserItem>> get() = _localSearchUserList

    // 선택한 유저 정보
    private val _selectUser = MutableLiveData<UserItem>()
    val selectUser: LiveData<UserItem> get() = _selectUser

    var perPage = 100               // 조회 사용자 수
    var apiSearchName = ""          // 사용자 검색 이름
    var locakSearchName = ""        // 즐겨찾기 사용자 검색 이름

    /**
     * 선택 된 메뉴 저장
     */
    fun setSelectedMenuId(selectedMenuId: Int) {
        _selectedMainTabMenu.value = selectedMenuId
    }

    fun setUserMemo(memo:String) {
        _selectUser.value?.memo = memo
    }

    fun getLocalUserListApi(context:Context){
        viewModelScope.launch {
            val dao = RoomDBClient.getUserDatabase(context).userDao()
            Pager(
                config = PagingConfig(
                    pageSize = perPage,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { UserItemPaginSource(dao,apiSearchName,perPage,LoadDiv.API) }
            ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                _searchUserList.value = pagingData
            }
        }
    }

    fun getUserDetail(login:String,block: suspend () -> Unit){
        dataSource.launchNetworkLoad(viewModelScope) {
            val result = NetworkService().getApiService().userDetail(
                login = login
            )
            _selectUser.value = result
            block()
        }
    }

    fun getLocalUserList(context:Context){
        viewModelScope.launch {
            val dao = RoomDBClient.getUserDatabase(context).userDao()
            Pager(
                config = PagingConfig(
                    pageSize = perPage,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { UserItemPaginSource(dao,locakSearchName,perPage) }
            ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                var strGroup = ""
                var data = pagingData.map {
                    var userItem = it
                    val firstStr = it.login.substring(0,1).uppercase()
                    if (strGroup != firstStr) {
                        strGroup = firstStr
                        userItem.strGroup = firstStr
                    }
                    userItem
                }
                _localSearchUserList.value = data
            }
        }
    }

    suspend fun getLocalChackUserId(context:Context,id:Int,block:suspend (Boolean)->Unit){
        val db = RoomDBClient.getUserDatabase(context).userDao()
        val localId = db.getUserId(id = id)
        block(localId != null)
    }

    fun getLocalUser(context:Context,id: Int):UserItem?{
        val dao = RoomDBClient.getUserDatabase(context).userDao()
        return dao.getUser(id)
    }
    fun insertLocalUser(context:Context,userItem: UserItem){
        val dao = RoomDBClient.getUserDatabase(context).userDao()
        dao.insert(userItem)
    }
    fun deleteLocalUser(context:Context,userItem: UserItem){
        val dao = RoomDBClient.getUserDatabase(context).userDao()
        dao.delete(userItem)
    }
    fun updateLocalUser(context:Context,userItem: UserItem){
        val dao = RoomDBClient.getUserDatabase(context).userDao()
        dao.update(userItem)
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