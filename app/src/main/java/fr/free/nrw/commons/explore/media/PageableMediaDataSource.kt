package fr.free.nrw.commons.explore.media

import fr.free.nrw.commons.Media
import fr.free.nrw.commons.auth.SessionManager
import fr.free.nrw.commons.explore.depictions.search.LoadFunction
import fr.free.nrw.commons.explore.paging.LiveDataConverter
import fr.free.nrw.commons.explore.paging.PageableBaseDataSource
import fr.free.nrw.commons.media.MediaClient
import javax.inject.Inject

class PageableMediaDataSource
@Inject
constructor(
    liveDataConverter: LiveDataConverter,
    private val mediaClient: MediaClient,
    private val sessionManager: SessionManager // Added injection
) : PageableBaseDataSource<Media>(liveDataConverter) {

    var ownFiles: Boolean = false

    override val loadFunction: LoadFunction<Media> = { loadSize: Int, startPosition: Int ->
        if (ownFiles) {
            val userName = sessionManager.userName ?: ""
            // Call the renamed function here
            mediaClient.getMediaListForUserSearch(query, userName, loadSize, startPosition).blockingGet()
        } else {
            mediaClient.getMediaListFromSearch(query, loadSize, startPosition).blockingGet()
        }
    }
}