package com.ifam.talkhub.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ifam.talkhub.constants.Constants
import com.ifam.talkhub.databinding.TelaLoginBinding
import com.ifam.talkhub.ui.alertdialog.LoadingAlertDialog
import com.ifam.talkhub.ui.presenter.FirebaseAuthLoginPresenter
import com.ifam.talkhub.ui.view.LoginView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment responsável pela autenticação do usuário no TalkHub.
 */
@AndroidEntryPoint
class LoginFragment : Fragment(), FirebaseAuthLoginPresenter {
    private lateinit var binding: TelaLoginBinding
    private val loginView: LoginView by viewModels()
    private lateinit var loadingAlertDialog: LoadingAlertDialog
    private val currentUser = Firebase.auth.currentUser

    /**
     * Infla o layout da tela de login.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TelaLoginBinding.inflate(inflater, container, false)

        // Configura o presenter para esta classe.
        loginView.repo.firebaseAuthentication.firebaseAuthLoginPresenter = this

        // Inicializa o alert dialog de carregamento.
        loadingAlertDialog = LoadingAlertDialog(requireContext())

        return binding.root
    }

    /**
     * Configura os cliques de botões e lida com a inicialização da tela.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navega para o fragmento de registro ao clicar no texto "Usuário sem conta".
        binding.usuarioSemContaTextview.setOnClickListener {
            goToRegisterFragment()
        }

        // Tenta fazer login ao clicar no botão de login.
        binding.loginButton.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edSenha.text.toString()

            // Validação dos dados do usuário antes do login.
            val validation = verifyDataFromUser(email, password)

            if (validation) {
                loginView.singInWithEmailPassword(email, password)
                loadingAlertDialog.showLoadingAlertDialog()
            }
        }
    }

    /**
     * Verifica se o usuário já está logado e sua conta está verificada ao iniciar a tela.
     */
    override fun onStart() {
        super.onStart()

        // Se o usuário estiver logado e a conta estiver verificada, navega para o fragmento de mensagens.
        if (currentUser != null && currentUser.isEmailVerified) {
            goToLatestMessagesFragment()
        }
    }

    /**
     * Valida os dados do usuário antes do login.
     *
     * @param email Email fornecido pelo usuário.
     * @param password Senha fornecida pelo usuário.
     * @return `true` se os dados forem válidos, `false` caso contrário.
     */
    private fun verifyDataFromUser(email: String, password: String): Boolean {

        if (email.isEmpty()) {
            binding.edEmail.error = Constants.REQUIRED_FIELD
        }
        if (password.isEmpty()) {
            binding.edSenha.error = Constants.REQUIRED_FIELD
        }

        return (email.isNotEmpty() && password.isNotEmpty())
    }

    /**
     * Navega para o fragmento de registro.
     */
    private fun goToRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    /**
     * Exibe um Snackbar com a opção de reenviar a verificação por email.
     */
    private fun showSnackBarSendVerify() {
        val snackBar = Snackbar.make(
            requireContext(),
            requireView(),
            "Você deve verificar sua conta para poder entrar no aplicativo TalkHub",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Enviar verificação") {
            loginView.sendEmailVerificationLogin()
            loadingAlertDialog.showLoadingAlertDialog()
        }
        snackBar.show()
    }

    /**
     * Navega para o fragmento de mensagens mais recentes.
     */
    private fun goToLatestMessagesFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    /**
     * Trata o resultado do login, verificação de email e reenvio de verificação por email.
     */
    override fun ifSingInWithEmailPasswordComplete(ifComplete: Boolean, state: String) {
        if (ifComplete) {
            loginView.checkEmailVerification()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    /**
     * Trata o resultado da verificação de email após o login.
     */
    override fun ifEmailVerificationLogin(ifVerified: Boolean) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (ifVerified) {
            goToLatestMessagesFragment()
        } else {
            showSnackBarSendVerify()
        }
    }

    /**
     * Trata o resultado do envio de verificação por email.
     */
    override fun ifSendEmailVerificationSentSuccessLogin(ifSuccess: Boolean, state: String) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (ifSuccess) {
            Toast.makeText(requireContext(), "enviado, verifique seu e-mail", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
    }
}
