package com.ifam.talkhub.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe de aplicativo principal que estende a
 * classe `Application` e é anotada
 * com `@HiltAndroidApp` para
 * habilitar a injeção de
 * dependência com Hilt.
 */
@HiltAndroidApp
class MyApp: Application()