package how.about.it.view.feed.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FeedViewModel : ViewModel() {
    private val _toggleCategory = MutableStateFlow(0)
    val toggleCategory: StateFlow<Int> = _toggleCategory

    fun setToggleCategory(category: Int) {
        _toggleCategory.value = category
    }
}
