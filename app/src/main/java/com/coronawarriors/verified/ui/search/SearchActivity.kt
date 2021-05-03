package com.coronawarriors.verified.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coronawarriors.verified.R
import com.coronawarriors.verified.data.DataRepositoryImpl
import com.coronawarriors.verified.databinding.ActivitySearchBinding
import com.coronawarriors.verified.enums.SearchUIListener.ON_BACK
import com.coronawarriors.verified.enums.SearchUIListener.ON_CLEAR
import com.coronawarriors.verified.util.Constants
import com.coronawarriors.verified.util.Constants.CITY
import com.coronawarriors.verified.util.Constants.COUNTRY
import com.coronawarriors.verified.util.Constants.EMPTY_STRING
import com.coronawarriors.verified.util.Constants.REQUIREMENT
import com.coronawarriors.verified.util.Constants.SEARCH_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_CITY_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_COUNTRY_TYPE
import com.coronawarriors.verified.util.Constants.SELECTED_SEARCH_RESULT
import com.coronawarriors.verified.util.Constants.SELECTED_STATE_TYPE
import com.coronawarriors.verified.util.Constants.STATE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), OnResultSelectedClickListener {

    private lateinit var mBinding: ActivitySearchBinding
    private val mViewModel: SearchViewModel by viewModels()

    private lateinit var searchResultAdapter: SearchResultAdapter

    @Inject
    lateinit var repository: DataRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        getIntentData()
        subscribeToClickListeners()
        subscribeToResultListener()
    }

    private fun subscribeToResultListener() {
        mViewModel.searchResult.observe(this, { result ->
            mBinding.loading.visibility = GONE
            if (!result.isNullOrEmpty()) {
                searchResultAdapter.updateList(result)
            } else showErrorStatus(message = resources.getString(R.string.error_getting_data))
        })
    }

    private fun showErrorStatus(
        message: String = resources.getString(R.string.error_getting_data),
        isVisible: Boolean = true
    ) {
        mBinding.vgStatus.visibility = if (isVisible) VISIBLE else GONE
        mBinding.rvSearchSuggestion.visibility = if (!isVisible) VISIBLE else GONE
        mBinding.tvStatusMessage.text = message
    }

    private fun getIntentData() {
        if (intent != null) {
            if (intent.hasExtra(SEARCH_TYPE)) {
                mViewModel.setSearchType(intent.getStringExtra(SEARCH_TYPE) ?: EMPTY_STRING)
            }
            if (mViewModel.getSearchType().isNotBlank()) {
                if (mViewModel.getSearchType() == REQUIREMENT) mViewModel.fetchRequirementTypes()
                else mViewModel.fetchPlacesResult()
            }
        }
    }

    private fun subscribeToClickListeners() {
        mViewModel.clickListener.observe(this, { uiListener ->
            when (uiListener) {
                ON_BACK -> onBackPressed()
                ON_CLEAR -> mBinding.etSearch.setText(EMPTY_STRING)
            }
        })
    }

    private fun initialize() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        mBinding.viewModel = mViewModel
        searchResultAdapter = SearchResultAdapter(this)
        mBinding.rvSearchSuggestion.adapter = searchResultAdapter
    }

    override fun onItemSelected(result: String) {
        val intent = Intent()
        val key: String = when (mViewModel.getSearchType()) {
            COUNTRY -> SELECTED_COUNTRY_TYPE
            CITY -> SELECTED_CITY_TYPE
            STATE -> SELECTED_STATE_TYPE
            REQUIREMENT -> Constants.SELECTED_REQUIREMENT_TYPE
            else -> SELECTED_SEARCH_RESULT
        }
        intent.putExtra(key, result)
        setResult(RESULT_OK, intent)
        finish()
    }
}