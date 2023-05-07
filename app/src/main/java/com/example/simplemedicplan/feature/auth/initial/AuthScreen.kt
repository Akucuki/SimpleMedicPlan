package com.example.simplemedicplan.feature.auth.initial

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.BuildConfig
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.feature.common.PrimaryButton
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.receiveAsFlow

private const val LOGIN_STRING_TAG = "login"

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit,
    onNavigateEmailRegister: () -> Unit,
    onNavigateEmailLogin: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val alreadyHaveAnAccountAnnotatedString = buildAnnotatedString {
        pushStyle(SpanStyle(color = Color.White))
        append(stringResource(R.string.already_have_an_account))
        append(" ")
        withStyle(
            SpanStyle(color = YellowColor, textDecoration = TextDecoration.Underline)
        ) {
            pushStringAnnotation(
                tag = LOGIN_STRING_TAG,
                annotation = LOGIN_STRING_TAG
            )
            append(stringResource(R.string.login))
        }
    }
    val activity = LocalContext.current as ComponentActivity
    val googleSignUpLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            val token = account!!.idToken!!
            val credential = GoogleAuthProvider.getCredential(token, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnSuccessListener { viewModel.onAuthSuccess() }
                .addOnFailureListener { viewModel.onAuthFailure() }
        } catch (e: ApiException) {
            viewModel.onAuthFailure()
        }
    }
    val fbCallbackManager by lazy { CallbackManager.Factory.create() }

    fun registerFacebookCallback() {
        LoginManager.getInstance().registerCallback(
            fbCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    viewModel.onAuthFailure()
                }

                override fun onError(error: FacebookException) {
                    viewModel.onAuthFailure()
                }

                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken.token
                    val credential = FacebookAuthProvider.getCredential(token)
                    Firebase.auth.signInWithCredential(credential)
                        .addOnSuccessListener { viewModel.onAuthSuccess() }
                        .addOnFailureListener { viewModel.onAuthFailure() }
                }
            }
        )
    }

    fun authGoogle() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, signInOptions)
        googleSignUpLauncher.launch(googleSignInClient.signInIntent)
    }

    fun authFacebook() {
        FacebookSdk.fullyInitialize()
        registerFacebookCallback()
        LoginManager.getInstance().logInWithReadPermissions(
            activity,
            fbCallbackManager,
            listOf("email", "public_profile")
        )
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is AuthEvents.AuthWithGoogle -> {
                    authGoogle()
                }

                is AuthEvents.AuthWithFacebook -> {
                    authFacebook()
                }

                is AuthEvents.NavigateHome -> {
                    onNavigateHome()
                }

                is AuthEvents.ShowErrorToast -> {
                    Toast.makeText(
                        activity,
                        R.string.error_failed_to_authorize,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AuthEvents.NavigateEmailLogin -> {
                    onNavigateEmailLogin()
                }

                is AuthEvents.NavigateEmailRegister -> {
                    onNavigateEmailRegister()
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { LoginManager.getInstance().unregisterCallback(fbCallbackManager) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 150.dp),
            painter = painterResource(R.drawable.img_logo),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                text = stringResource(R.string.register_with_email),
                onClick = viewModel::onEmailRegisterClick
            )
            ClickableText(
                text = alreadyHaveAnAccountAnnotatedString,
                style = MaterialTheme.typography.bodySmall,
                onClick = { offset ->
                    alreadyHaveAnAccountAnnotatedString.getStringAnnotations(
                        tag = LOGIN_STRING_TAG,
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        viewModel.onEmailLoginClick()
                    }
                }
            )
            Box(
                modifier = Modifier.padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Divider(color = YellowColor)
                Text(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 10.dp),
                    text = stringResource(R.string.or),
                    style = MaterialTheme.typography.bodySmall,
                    color = YellowColor
                )
            }
            PrimaryButton(
                text = stringResource(R.string.authorize_with_google),
                onClick = viewModel::onAuthWithGoogleClick
            )
            PrimaryButton(
                text = stringResource(R.string.authorize_with_facebook),
                onClick = viewModel::onAuthWithFacebookClick
            )
        }
    }
}