package fr.free.nrw.commons.explore.media

import android.os.Bundle // Added
import android.view.View   // Added
import javax.inject.Inject

/**
 * Displays the image search screen.
 */
class SearchMediaFragment : PageableMediaFragment() {
    @Inject
    lateinit var presenter: SearchMediaFragmentPresenter

    override val injectedPresenter
        get() = presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if this fragment instance is for "My Media" or "All Media"
        val isOwnMedia = arguments?.getBoolean("isOwnMedia", false) ?: false
        (presenter as? SearchMediaFragmentPresenterImpl)?.setSearchType(isOwnMedia)
    }
}