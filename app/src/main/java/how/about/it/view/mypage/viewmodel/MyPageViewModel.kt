package how.about.it.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {
    private val _category = MutableStateFlow(0)
    val category: StateFlow<Int> = _category

    fun setCategory(category: Int) {
        _category.value = category
    }
}