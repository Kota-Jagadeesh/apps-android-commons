package fr.free.nrw.commons.explore.media

import fr.free.nrw.commons.Media
import fr.free.nrw.commons.di.CommonsApplicationModule
import fr.free.nrw.commons.explore.paging.BasePagingPresenter
import fr.free.nrw.commons.explore.paging.PagingContract
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

interface SearchMediaFragmentPresenter : PagingContract.Presenter<Media>

class SearchMediaFragmentPresenterImpl
@Inject
constructor(
    @Named(CommonsApplicationModule.MAIN_THREAD) mainThreadScheduler: Scheduler,
    private val dataSourceFactory: PageableMediaDataSource, // Remove 'val' to local if needed, but we need access
) : BasePagingPresenter<Media>(mainThreadScheduler, dataSourceFactory),
    SearchMediaFragmentPresenter {

    fun setSearchType(isOwnMedia: Boolean) {
        dataSourceFactory.ownFiles = isOwnMedia
    }
}