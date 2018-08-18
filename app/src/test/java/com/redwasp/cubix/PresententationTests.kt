package com.redwasp.cubix

import com.redwasp.cubix.arch.IPresenter
import com.redwasp.cubix.arch.IView
import com.redwasp.cubix.archComponents_Presenters.*
import junit.framework.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class PresententationTests {
    private val presenter = DiscoverActivityPresenter()
    private val appPresenter = ApplicationPresenter()
    private val feedListPresenter = FeedListPresenter()
    private val imageUploadPresenter = ImageUploadPresenter()
    private val mainActivityPresenter = MainActivityPresenter()
    private val materialFragmentPresenter = MaterialFragmentPresenter()
    private val profileNotifyPresenter = ProfileNotifyPresenter()
    private val readingFragmentPresenter = ReadingFragmentPresenter()
    private val searchableActivityPresenter = SearchableActivityPresenter()

    private val mockView : IView = mock(IView::class.java)
    private val mockFragment = mock(IView::class.java)
    private val mockPresenter = mock(IPresenter::class.java)
    private val mockGlobalPresenter = mock(ApplicationPresenter::class.java)

    @Test
    fun testDiscoverPresenter(){
        `when`(mockView.navigateToAnotherView(ArgumentMatchers.any(IView::class.java)))
                .then { _ ->
                    print("Navigation is called")
                }
        presenter.init(mockView)
        presenter.setGlobalPresenter(mockGlobalPresenter)
        presenter.navigate(mockFragment)
    }

    fun testappPresenter(){}
    fun testfeedListPresenter(){}
    fun testimageUploadPresenter(){}
    fun testmainActivitypresenter(){}
    fun testmaterialFragmentpresent(){}
    fun testprofileFragmentpresenter(){}
    fun testreadingFragmentPresenter(){}
    fun testSearchableActivityPresenter(){}
}