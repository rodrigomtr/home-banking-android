package com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth.tasks.MatrixAuthTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import java.util.Random

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

/**
 * @author Rodrigo Rodrigues on 05/02/2018
 */

@RequiresApi(api = Build.VERSION_CODES.M)
class TransactionAuthPresenter internal constructor(private val view: TransactionAuthContract.View, fpManager: FingerprintManager, kgManager: KeyguardManager)
    : FingerprintManager.AuthenticationCallback(), TransactionAuthContract.Presenter {

    /**
     * 0: FingerPrint Auth
     * 1: Matrix Auth
     */
    private var state: Int = 0
    private val fingerprintManager: FingerprintManager = fpManager
    private val keyguardManager: KeyguardManager = kgManager
    private var cipher: Cipher? = null
    private var keyStore: KeyStore? = null
    private val keyName = "HomeBankingFingerPrint"
    private var l1: Int = 0
    private var c1: Int = 0
    private var l2: Int = 0
    private var c2: Int = 0
    private var l3: Int = 0
    private var c3: Int = 0
    private var l4: Int = 0
    private var c4: Int = 0

    init {
        setAuthMethod()
    }

    private fun setAuthMethod() {
        if (!fingerprintManager.isHardwareDetected
                || !fingerprintManager.hasEnrolledFingerprints()
                || !keyguardManager.isKeyguardSecure) {
            authWithMatrix()
        } else {
            authWithFingerPrint()
        }
    }

    /*
    *
    * Finger Print Auth
    *
     */
    private fun authWithFingerPrint() {
        state = 0
        view.setFingerPrintView()
        generateKey()
        if (cipherInit()) {
            val cryptoObject = FingerprintManager.CryptoObject(cipher!!)
            startAuthentication(fingerprintManager, cryptoObject)
        }
    }


    private fun cipherInit(): Boolean {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }

        try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(keyName, null) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e1: IOException) {
            e1.printStackTrace()
            return false
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
            return false
        } catch (e1: CertificateException) {
            e1.printStackTrace()
            return false
        } catch (e1: UnrecoverableKeyException) {
            e1.printStackTrace()
            return false
        } catch (e1: KeyStoreException) {
            e1.printStackTrace()
            return false
        } catch (e1: InvalidKeyException) {
            e1.printStackTrace()
            return false
        }

    }

    @SuppressLint("NewApi")
    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

        var keyGenerator: KeyGenerator? = null
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        }

        try {
            keyStore!!.load(null)
            if (keyGenerator != null) {
                keyGenerator.init(KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build())
                keyGenerator.generateKey()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }

    }

    private fun startAuthentication(fingerprintManager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject) {
        val cancellationSignal = CancellationSignal()
        if(view.checkFPPermission)
            return
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        view.setFingerPrintError()
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        view.fingerPrintSuccess()
    }

    /*
    *
    * Matrix Auth
    *
     */
    private fun authWithMatrix() {
        state = 1
        view.setMatrixView()
        setRequest()
    }

    private fun setRequest() {
        val rand = Random()
        l1 = rand.nextInt(5)
        c1 = rand.nextInt(5)
        l2 = rand.nextInt(5)
        c2 = rand.nextInt(5)
        l3 = rand.nextInt(5)
        c3 = rand.nextInt(5)
        l4 = rand.nextInt(5)
        c4 = rand.nextInt(5)
        val req1 = (65 + l1).toChar() + c1.toString()
        val req2 = (65 + l2).toChar() + c2.toString()
        val req3 = (65 + l3).toChar() + c3.toString()
        val req4 = (65 + l4).toChar() + c4.toString()
        view.requestPassword(req1, req2, req3, req4)
    }

    override fun matrixAuth() {
        val matrixId = App.instance!!.user!!.matrix!!.id
        val password = view.password
        try {
            val request = (matrixId.toString() + "%" + l1 + ":" + c1 + ":" + password[0]
                    + "!" + l2 + ":" + c2 + ":" + password[1]
                    + "!" + l3 + ":" + c3 + ":" + password[2]
                    + "!" + l4 + ":" + c4 + ":" + password[3])
            view.showLoadingProgressDialog()
            launch(UI) {
                view.showLoadingProgressDialog()
                val authResult = MatrixAuthTask().auth(request)
                view.dismissProgressDialog()
                if (authResult) view.matrixSuccess() else view.matrixError()
            }
        } catch (e: StringIndexOutOfBoundsException) {
            view.matrixError()
        }
    }

    override fun onPositiveButtonClicked() {
        if (state == 0) {
            authWithMatrix()
        } else {
            matrixAuth()
        }
    }

    override fun startMatrixAuth() {
        setRequest()
    }
}
