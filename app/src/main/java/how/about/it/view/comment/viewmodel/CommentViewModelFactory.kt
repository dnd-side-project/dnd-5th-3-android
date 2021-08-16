package how.about.it.view.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import how.about.it.view.comment.repository.CommentRepository

class CommentViewModelFactory(
    private val commentRepository: CommentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentViewModel(commentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}