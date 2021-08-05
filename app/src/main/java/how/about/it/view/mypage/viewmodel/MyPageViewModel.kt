package how.about.it.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyPageViewModel : ViewModel() {
    private val _category = MutableStateFlow(0)
    val category: StateFlow<Int> = _category

    fun setCategory(category: Int) {
        _category.value = category
    }
}