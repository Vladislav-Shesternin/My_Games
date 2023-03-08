package togle.plinko.mega.sigma.dominicanos.utils

import androidx.core.view.isVisible
import togle.plinko.mega.sigma.dominicanos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class Lottie(private val binding: ActivityMainBinding) {

    private val coroutineLottie = CoroutineScope(Dispatchers.Main)

    fun showLoader() {
        coroutineLottie.launch {
            binding.loader.apply {
                if (isVisible.not()) {
                    isVisible = true
                    playAnimation()
                }
            }
        }
    }

    fun hideLoader() {
        coroutineLottie.launch {
            binding.loader.apply {
                if (isVisible) {
                    isVisible = false
                    coroutineLottie.coroutineContext.cancelChildren()
                }
            }
        }
    }

    fun showNotNetwork() {
        coroutineLottie.launch {
            binding.lottieNetwork.apply {
                if (isVisible.not()) {
                    hideLoader()
                    isVisible = true
                    playAnimation()
                }
            }
        }
    }

}