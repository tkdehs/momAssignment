package com.pnx.momassignment.room.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pnx.momassignment.network.NetworkService
import com.pnx.momassignment.room.dao.UserDao

class UserItemPaginSource(
    private val dao: UserDao,
    private val searchName: String,
    private val perPage:Int,
    private val loadDiv: LoadDiv = LoadDiv.LOCAL
): PagingSource<Int, UserItem>() {
    override fun getRefreshKey(state: PagingState<Int, UserItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserItem> {
        val page = params.key ?: 1
        return try {
            when(loadDiv) {
                LoadDiv.API -> {
                    val items = NetworkService().getApiService().userList(
                        q = searchName,
                        per_page = perPage,
                        page = page
                    )
                    LoadResult.Page(
                        data = items.items,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (items.items.isEmpty()) null else page + 1
                    )
                }
                else ->{
                    val items = dao.getUserListByPaging(page, perPage, searchName)
                    LoadResult.Page(
                        data = items,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (items.isEmpty()) null else page + 1
                    )
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}

enum class LoadDiv {
    API, LOCAL
}