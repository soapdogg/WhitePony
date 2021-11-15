package compiler.translator

import compiler.translator.impl.Translator

enum class TranslatorSingleton {
    INSTANCE;

    val translator: ITranslator = Translator()
}