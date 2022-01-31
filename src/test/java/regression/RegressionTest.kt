package regression

import compiler.Compiler
import compiler.frontend.parser.ParserSingleton
import compiler.frontend.printer.PrinterSingleton
import compiler.frontend.tokenizer.TokenizerSingleton
import compiler.frontend.translator.TranslatorSingleton
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class RegressionTest {

    private val tokenizer = TokenizerSingleton.INSTANCE.tokenizer
    private val parser = ParserSingleton.INSTANCE.parser
    private val translator = TranslatorSingleton.INSTANCE.translator
    private val printer = PrinterSingleton.INSTANCE.printer
    private val compiler = Compiler(
        tokenizer,
        parser,
        translator,
        printer
    )

    @ParameterizedTest
    @MethodSource(
        "inputData",
    )
    fun regressionTest(arguments: ArgumentsAccessor) {
        val testInput = arguments.get(0) as TestInput


        val (parseTreeString, translatedTreeString) = compiler.compile(
            testInput.expectedParsedProgram,
        )
        Assertions.assertEquals(testInput.expectedParsedProgram, parseTreeString)
        Assertions.assertEquals(testInput.expectedTranslatedProgram, translatedTreeString)
    }

    @Test
    fun regressionIndividualTest() {
        val input = Program6
        val intermediateCode = null
        val (parseTreeString, translatedTreeString) = compiler.compile(
            input,
        )
        Assertions.assertEquals(input, parseTreeString)
        if (intermediateCode != null) {
            Assertions.assertEquals(intermediateCode, translatedTreeString)
        } else {
            println(parseTreeString)
            println()
            println(translatedTreeString)
            println()
        }
    }

    data class TestInput(
        val expectedParsedProgram: String,
        val expectedTranslatedProgram: String
    )

    companion object {
        @JvmStatic
        fun inputData(): Stream<TestInput> {
            return Stream.of(
                TestInput(
                    Program1,
                    IProgram1
                ),
                TestInput(
                    Program2,
                    IProgram2
                ),
                TestInput(
                    Program3,
                    IProgram3
                ),
                TestInput(
                    Program4,
                    IProgram4
                ),
                TestInput(
                    Program5,
                    IProgram5
                ),
                TestInput(
                    Program6,
                    IProgram6
                ),
                TestInput(
                    Program7,
                    IProgram7
                ),
                TestInput(
                    Program8,
                    IProgram8
                ),
                TestInput(
                    Program9,
                    IProgram9
                ),
                TestInput(
                    Program10,
                    IProgram10
                ),
                TestInput(
                    Program11,
                    IProgram11
                ),
                TestInput(
                    Program12,
                    IProgram12
                ),
                TestInput(
                    Program13,
                    IProgram13
                ),
                TestInput(
                    Program14,
                    IProgram14
                ),
                TestInput(
                    Program15,
                    IProgram15
                ),
                TestInput(
                    Program16,
                    IProgram16
                ),
                TestInput(
                    Program17,
                    IProgram17
                ),
                TestInput(
                    Program18,
                    IProgram18
                ),
                TestInput(
                    Program19,
                    IProgram19
                ),
                TestInput(
                    Program20,
                    IProgram20
                ),
                TestInput(
                    Program21,
                    IProgram21
                ),
                TestInput(
                    Program22,
                    IProgram22
                ),
                TestInput(
                    Program23,
                    IProgram23
                ),
                TestInput(
                    Program24,
                    IProgram24
                )
            )
        }

        private const val Program1 =
"""int test1() {
    int a;
    {
        int b = 5;
        {
            int c = 10;
            {
                a = b + c;
            }
        }
    }
    return a;
}"""
        private const val IProgram1 =
"""int test1() {
    int a;
    int b = 5;
    int c = 10;
    int _t0 = b;
    int _t1 = c;
    int _t2 = _t0 + _t1;
    a = _t2;
    int _t3 = a;
    return _t3;
}"""

        private const val Program2 =
"""int test2() {
    int d = 1;
    d = 1 | 2;
    d = d ^ 4;
    d = d & 7;
    d = ~d;
    return d;
}"""
        private const val IProgram2 =
"""int test2() {
    int d = 1;
    int _t0 = 1 | 2;
    d = _t0;
    int _t1 = d;
    int _t2 = _t1 ^ 4;
    d = _t2;
    int _t3 = d;
    int _t4 = _t3 & 7;
    d = _t4;
    int _t5 = d;
    int _t6 = ~_t5;
    d = _t6;
    int _t7 = d;
    return _t7;
}"""

        private const val Program3 =
"""int test3() {
    int e = 4;
    e <<= 3;
    e >>= 2;
    e &= 7;
    e |= 15;
    e &= 4;
    return e;
}"""
        private const val IProgram3 =
"""int test3() {
    int e = 4;
    int _t0 = e << 3;
    e = _t0;
    int _t1 = e >> 2;
    e = _t1;
    int _t2 = e & 7;
    e = _t2;
    int _t3 = e | 15;
    e = _t3;
    int _t4 = e & 4;
    e = _t4;
    int _t5 = e;
    return _t5;
}"""

        private const val Program4 =
"""int test4() {
    int x;
    int y = 2;
    int z = 3;
    x = y + z;
    return x;
}"""
        private const val IProgram4 =
"""int test4() {
    int x;
    int y = 2;
    int z = 3;
    int _t0 = y;
    int _t1 = z;
    int _t2 = _t0 + _t1;
    x = _t2;
    int _t3 = x;
    return _t3;
}"""

        private const val Program5 =
"""int test5() {
    int w, x, q;
    int y = 4;
    int z = 10;
    w = x = q = y + z;
    return w;
}"""
        private const val IProgram5 =
"""int test5() {
    int w, x, q;
    int y = 4;
    int z = 10;
    int _t0 = y;
    int _t1 = z;
    int _t2 = _t0 + _t1;
    q = _t2;
    x = _t2;
    w = _t2;
    int _t3 = w;
    return _t3;
}"""

        private const val Program6 =
"""int test6() {
    int a[10];
    int x = 9;
    int y = 1;
    int z = 0;
    a[x = y + z] = x;
    return a[x];
}"""
        private const val IProgram6 =
"""int test6() {
    int a[10];
    int x = 9;
    int y = 1;
    int z = 0;
    int _t0 = y;
    int _t1 = z;
    int _t2 = _t0 + _t1;
    x = _t2;
    int _t3 = x;
    a[_t2] = _t3;
    int _t4 = x;
    int _t5 = a[_t4];
    return _t5;
}"""

        private const val Program7 =
"""int test7() {
    int a[10];
    int i;
    for (i = 0; i < 10; ++i) {
        a[i] = 0;
    }
    int x = 0;
    int y = 1;
    int z = 5;
    x = y + z;
    a[x] = x;
    return a[x];
}"""
        private const val IProgram7 =
"""int test7() {
    int a[10];
    int i;
    i = 0;
    _l1: ;
    int _t0 = i;
    if (_t0 < 10) goto _l2;
    goto _l0;
    _l2: ;
    int _t1 = i;
    a[_t1] = 0;
    i = i + 1;
    goto _l1;
    _l0: ;
    int x = 0;
    int y = 1;
    int z = 5;
    int _t2 = y;
    int _t3 = z;
    int _t4 = _t2 + _t3;
    x = _t4;
    int _t5 = x;
    int _t6 = x;
    a[_t5] = _t6;
    int _t7 = x;
    int _t8 = a[_t7];
    return _t8;
}"""

        private const val Program8 =
"""int test8() {
    int x = 102;
    int y = 34;
    int z = 1232;
    x += y + z;
    return x;
}"""
        private const val IProgram8 =
"""int test8() {
    int x = 102;
    int y = 34;
    int z = 1232;
    int _t0 = y;
    int _t1 = z;
    int _t2 = _t0 + _t1;
    int _t3 = x + _t2;
    x = _t3;
    int _t4 = x;
    return _t4;
}"""

        private const val Program9 =
"""int test9() {
    int i = 0;
    int j = 0;
    do {
        if(i == 1) {
            j += i;
        }
        else if(i % 2 == 0) j += i;
        else {
            if(i == 9) {
                i++;
            }
            else if(i != 6) {
                --i;
                i += j;
            }
        }
        ++i;
    } while (i < 10);
    i = 0;
    while (i < 10) i++;
    return j;
}"""
        private const val IProgram9 =
"""int test9() {
    int i = 0;
    int j = 0;
    _l1: ;
    int _t0 = i;
    if (_t0 == 1) goto _l3;
    goto _l4;
    _l3: ;
    int _t1 = i;
    int _t2 = j + _t1;
    j = _t2;
    goto _l2;
    _l4: ;
    int _t3 = i;
    int _t4 = _t3 % 2;
    if (_t4 == 0) goto _l6;
    goto _l7;
    _l6: ;
    int _t5 = i;
    int _t6 = j + _t5;
    j = _t6;
    goto _l5;
    _l7: ;
    int _t7 = i;
    if (_t7 == 9) goto _l9;
    goto _l10;
    _l9: ;
    int _t8 = i;
    i = i + 1;
    goto _l8;
    _l10: ;
    int _t9 = i;
    if (_t9 != 6) goto _l12;
    goto _l11;
    _l12: ;
    i = i - 1;
    int _t10 = j;
    int _t11 = i + _t10;
    i = _t11;
    _l11: ;
    _l8: ;
    _l5: ;
    _l2: ;
    i = i + 1;
    int _t12 = i;
    if (_t12 < 10) goto _l1;
    goto _l0;
    _l0: ;
    i = 0;
    _l14: ;
    int _t13 = i;
    if (_t13 < 10) goto _l15;
    goto _l13;
    _l15: ;
    int _t14 = i;
    i = i + 1;
    goto _l14;
    _l13: ;
    int _t15 = j;
    return _t15;
}"""

        private const val Program10 =
"""int test10() {
    int x;
    int y = 10;
    x = ++y;
    return x;
}"""
        private const val IProgram10 =
"""int test10() {
    int x;
    int y = 10;
    y = y + 1;
    x = y;
    int _t0 = x;
    return _t0;
}"""

        private const val Program11 =
"""int test11() {
    int x;
    int y = 19;
    x = y++;
    return x;
}"""
        private const val IProgram11 =
"""int test11() {
    int x;
    int y = 19;
    int _t0 = y;
    y = y + 1;
    x = _t0;
    int _t1 = x;
    return _t1;
}"""

        private const val Program12 =
"""int test12() {
    int a = 3;
    int b = 945;
    int c = 93483;
    return a + b * b + c - 23;
}"""
        private const val IProgram12 =
"""int test12() {
    int a = 3;
    int b = 945;
    int c = 93483;
    int _t0 = a;
    int _t1 = b;
    int _t2 = b;
    int _t3 = _t1 * _t2;
    int _t4 = _t0 + _t3;
    int _t5 = c;
    int _t6 = _t4 + _t5;
    int _t7 = _t6 - 23;
    return _t7;
}"""

        private const val Program13 =
"""int test13() {
    int x = 2;
    int y = 4;
    int z;
    z = (x + y);
    return z;
}"""
        private const val IProgram13 =
"""int test13() {
    int x = 2;
    int y = 4;
    int z;
    int _t0 = x;
    int _t1 = y;
    int _t2 = _t0 + _t1;
    z = _t2;
    int _t3 = z;
    return _t3;
}"""

        private const val Program14 =
"""int test14() {
    int a[10];
    a[1] = 9;
    int y = 1;
    int x;
    x = a[y];
    return x;
}"""
        private const val IProgram14 =
"""int test14() {
    int a[10];
    a[1] = 9;
    int y = 1;
    int x;
    int _t0 = y;
    int _t1 = a[_t0];
    x = _t1;
    int _t2 = x;
    return _t2;
}"""

        private const val Program15 =
"""int test15() {
    int a[10];
    int x = 34;
    int y = 2;
    a[0] = 45;
    a[0] += x * y;
    return a[0];
}"""
        private const val IProgram15 =
"""int test15() {
    int a[10];
    int x = 34;
    int y = 2;
    a[0] = 45;
    int _t3 = a[0];
    int _t0 = x;
    int _t1 = y;
    int _t2 = _t0 * _t1;
    _t3 = _t3 + _t2;
    a[0] = _t3;
    int _t4 = a[0];
    return _t4;
}"""

        private const val Program16 =
"""int test16() {
    int x[10];
    int y = 0;
    x[0] = 34;
    ++x[y];
    x[y]++;
    return x[y];
}"""
        private const val IProgram16 =
"""int test16() {
    int x[10];
    int y = 0;
    x[0] = 34;
    int _t0 = y;
    int _t1 = x[_t0];
    _t1 = _t1 + 1;
    x[_t0] = _t1;
    int _t2 = y;
    int _t3 = x[_t2];
    _t3 = _t3 + 1;
    x[_t2] = _t3;
    _t3 = _t3 - 1;
    int _t4 = y;
    int _t5 = x[_t4];
    return _t5;
}"""

        private const val Program17 =
"""int test17() {
    int x = -2;
    int y = 35653;
    if(x + 1 < 0) y = 1;
    else y = 2;
    return y;
}"""
        private const val IProgram17 =
"""int test17() {
    int x = -2;
    int y = 35653;
    int _t0 = x;
    int _t1 = _t0 + 1;
    if (_t1 < 0) goto _l1;
    goto _l2;
    _l1: ;
    y = 1;
    goto _l0;
    _l2: ;
    y = 2;
    _l0: ;
    int _t2 = y;
    return _t2;
}"""

        private const val Program18 =
"""int test18() {
    int x = -1;
    int y = 0;
    int z = 234;
    if(!(x < 0 && y < 1)) z = 2;
    return z;
}"""
        private const val IProgram18 =
"""int test18() {
    int x = -1;
    int y = 0;
    int z = 234;
    int _t0 = x;
    if (_t0 < 0) goto _l2;
    goto _l1;
    _l2: ;
    int _t1 = y;
    if (_t1 < 1) goto _l0;
    goto _l1;
    _l1: ;
    z = 2;
    _l0: ;
    int _t2 = z;
    return _t2;
}"""

        private const val Program19 =
"""int test19() {
    int x = -1;
    int y = 0;
    int z = 234132;
    if(x < 0 || y < 1) z = 2;
    return z;
}"""
        private const val IProgram19 =
"""int test19() {
    int x = -1;
    int y = 0;
    int z = 234132;
    int _t0 = x;
    if (_t0 < 0) goto _l1;
    goto _l2;
    _l2: ;
    int _t1 = y;
    if (_t1 < 1) goto _l1;
    goto _l0;
    _l1: ;
    z = 2;
    _l0: ;
    int _t2 = z;
    return _t2;
}"""

        private const val Program20 =
"""int test20() {
    int x = -1;
    int y = 0;
    int z = 234245;
    if(x < 0 && y >= 1) z = 6;
    return z;
}"""
        private const val IProgram20 =
"""int test20() {
    int x = -1;
    int y = 0;
    int z = 234245;
    int _t0 = x;
    if (_t0 < 0) goto _l2;
    goto _l0;
    _l2: ;
    int _t1 = y;
    if (_t1 >= 1) goto _l1;
    goto _l0;
    _l1: ;
    z = 6;
    _l0: ;
    int _t2 = z;
    return _t2;
}"""

        private const val Program21 =
"""int test21() {
    int x = 0;
    int y = 1;
    int z = 7;
    int i;
    int j, k;
    k = 2;
    while (k < 345) {
        for (j = 0; j < 100; ++j) {
            if(x > 1000 || z % 2 == 1) z++;
            else if(y == 1 && z == 7) z += 34;
            else z = 3;
            i = 54;
            do {
                x += z * y;
                y++;
                ++i;
            } while (i < 100);
        }
        if(k % 2 == 1 && !(z > 1194 || 697 < k)) k += i * j;
        else k += i + j;
    }
    return x;
}"""
        private const val IProgram21 =
"""int test21() {
    int x = 0;
    int y = 1;
    int z = 7;
    int i;
    int j, k;
    k = 2;
    _l1: ;
    int _t0 = k;
    if (_t0 < 345) goto _l2;
    goto _l0;
    _l2: ;
    j = 0;
    _l4: ;
    int _t1 = j;
    if (_t1 < 100) goto _l5;
    goto _l3;
    _l5: ;
    int _t2 = x;
    if (_t2 > 1000) goto _l7;
    goto _l9;
    _l9: ;
    int _t3 = z;
    int _t4 = _t3 % 2;
    if (_t4 == 1) goto _l7;
    goto _l8;
    _l7: ;
    int _t5 = z;
    z = z + 1;
    goto _l6;
    _l8: ;
    int _t6 = y;
    if (_t6 == 1) goto _l13;
    goto _l12;
    _l13: ;
    int _t7 = z;
    if (_t7 == 7) goto _l11;
    goto _l12;
    _l11: ;
    int _t8 = z + 34;
    z = _t8;
    goto _l10;
    _l12: ;
    z = 3;
    _l10: ;
    _l6: ;
    i = 54;
    _l15: ;
    int _t9 = z;
    int _t10 = y;
    int _t11 = _t9 * _t10;
    int _t12 = x + _t11;
    x = _t12;
    int _t13 = y;
    y = y + 1;
    i = i + 1;
    int _t14 = i;
    if (_t14 < 100) goto _l15;
    goto _l14;
    _l14: ;
    j = j + 1;
    goto _l4;
    _l3: ;
    int _t15 = k;
    int _t16 = _t15 % 2;
    if (_t16 == 1) goto _l19;
    goto _l18;
    _l19: ;
    int _t17 = z;
    if (_t17 > 1194) goto _l18;
    goto _l20;
    _l20: ;
    int _t18 = k;
    if (697 < _t18) goto _l18;
    goto _l17;
    _l17: ;
    int _t19 = i;
    int _t20 = j;
    int _t21 = _t19 * _t20;
    int _t22 = k + _t21;
    k = _t22;
    goto _l16;
    _l18: ;
    int _t23 = i;
    int _t24 = j;
    int _t25 = _t23 + _t24;
    int _t26 = k + _t25;
    k = _t26;
    _l16: ;
    goto _l1;
    _l0: ;
    int _t27 = x;
    return _t27;
}"""

        private const val Program22 =
"""double data_real[1024], data_imag[1024];
double coef_real[1024];
double coef_imag[1024];
double fft() {
    int i, j, k;
    double temp_real;
    double temp_imag;
    double Wr;
    double Wi;
    double ir = 0.0;
    for (i = 0; i < 1024; i++) {
        data_real[i] = ir;
        data_imag[i] = 1.0;
        coef_real[i] = 1.0;
        coef_imag[i] = 1.0;
        ir += 0.33;
    }
    int groupsPerStage = 1;
    int buttersPerGroup = 1024 / 2;
    for (i = 0; i < 10; ++i) {
        for (j = 0; j < groupsPerStage; ++j) {
            Wr = coef_real[(1 << i) - 1 + j];
            Wi = coef_imag[(1 << i) - 1 + j];
            for (k = 0; k < buttersPerGroup; ++k) {
                temp_real = Wr * data_real[2 * j * buttersPerGroup + buttersPerGroup + k] - Wi * data_imag[2 * j * buttersPerGroup + buttersPerGroup + k];
                temp_imag = Wi * data_real[2 * j * buttersPerGroup + buttersPerGroup + k] + Wr * data_imag[2 * j * buttersPerGroup + buttersPerGroup + k];
                data_real[2 * j * buttersPerGroup + buttersPerGroup + k] = data_real[2 * j * buttersPerGroup + k] - temp_real;
                data_real[2 * j * buttersPerGroup + k] += temp_real;
                data_imag[2 * j * buttersPerGroup + buttersPerGroup + k] = data_imag[2 * j * buttersPerGroup + k] - temp_imag;
                data_imag[2 * j * buttersPerGroup + k] += temp_imag;
            }
        }
        groupsPerStage <<= 1;
        buttersPerGroup >>= 1;
    }
    double sum = 0.0;
    for (i = 0; i < 1023; i++) sum += 11.1 * data_real[i];
    return sum;
}"""
        private const val IProgram22 =
"""double data_real[1024], data_imag[1024];
double coef_real[1024];
double coef_imag[1024];
double fft() {
    int i, j, k;
    double temp_real;
    double temp_imag;
    double Wr;
    double Wi;
    double ir = 0.0;
    i = 0;
    _l1: ;
    int _t0 = i;
    if (_t0 < 1024) goto _l2;
    goto _l0;
    _l2: ;
    int _t2 = i;
    double _t3 = ir;
    data_real[_t2] = _t3;
    int _t4 = i;
    data_imag[_t4] = 1.0;
    int _t5 = i;
    coef_real[_t5] = 1.0;
    int _t6 = i;
    coef_imag[_t6] = 1.0;
    double _t7 = ir + 0.33;
    ir = _t7;
    int _t1 = i;
    i = i + 1;
    goto _l1;
    _l0: ;
    int groupsPerStage = 1;
    int buttersPerGroup = 1024 / 2;
    i = 0;
    _l4: ;
    int _t8 = i;
    if (_t8 < 10) goto _l5;
    goto _l3;
    _l5: ;
    j = 0;
    _l7: ;
    int _t9 = j;
    int _t10 = groupsPerStage;
    if (_t9 < _t10) goto _l8;
    goto _l6;
    _l8: ;
    int _t11 = i;
    int _t12 = 1 << _t11;
    int _t13 = _t12 - 1;
    int _t14 = j;
    int _t15 = _t13 + _t14;
    double _t16 = coef_real[_t15];
    Wr = _t16;
    int _t17 = i;
    int _t18 = 1 << _t17;
    int _t19 = _t18 - 1;
    int _t20 = j;
    int _t21 = _t19 + _t20;
    double _t22 = coef_imag[_t21];
    Wi = _t22;
    k = 0;
    _l10: ;
    int _t23 = k;
    int _t24 = buttersPerGroup;
    if (_t23 < _t24) goto _l11;
    goto _l9;
    _l11: ;
    double _t25 = Wr;
    int _t26 = j;
    int _t27 = 2 * _t26;
    int _t28 = buttersPerGroup;
    int _t29 = _t27 * _t28;
    int _t30 = buttersPerGroup;
    int _t31 = _t29 + _t30;
    int _t32 = k;
    int _t33 = _t31 + _t32;
    double _t34 = data_real[_t33];
    double _t35 = _t25 * _t34;
    double _t36 = Wi;
    int _t37 = j;
    int _t38 = 2 * _t37;
    int _t39 = buttersPerGroup;
    int _t40 = _t38 * _t39;
    int _t41 = buttersPerGroup;
    int _t42 = _t40 + _t41;
    int _t43 = k;
    int _t44 = _t42 + _t43;
    double _t45 = data_imag[_t44];
    double _t46 = _t36 * _t45;
    double _t47 = _t35 - _t46;
    temp_real = _t47;
    double _t48 = Wi;
    int _t49 = j;
    int _t50 = 2 * _t49;
    int _t51 = buttersPerGroup;
    int _t52 = _t50 * _t51;
    int _t53 = buttersPerGroup;
    int _t54 = _t52 + _t53;
    int _t55 = k;
    int _t56 = _t54 + _t55;
    double _t57 = data_real[_t56];
    double _t58 = _t48 * _t57;
    double _t59 = Wr;
    int _t60 = j;
    int _t61 = 2 * _t60;
    int _t62 = buttersPerGroup;
    int _t63 = _t61 * _t62;
    int _t64 = buttersPerGroup;
    int _t65 = _t63 + _t64;
    int _t66 = k;
    int _t67 = _t65 + _t66;
    double _t68 = data_imag[_t67];
    double _t69 = _t59 * _t68;
    double _t70 = _t58 + _t69;
    temp_imag = _t70;
    int _t71 = j;
    int _t72 = 2 * _t71;
    int _t73 = buttersPerGroup;
    int _t74 = _t72 * _t73;
    int _t75 = buttersPerGroup;
    int _t76 = _t74 + _t75;
    int _t77 = k;
    int _t78 = _t76 + _t77;
    int _t79 = j;
    int _t80 = 2 * _t79;
    int _t81 = buttersPerGroup;
    int _t82 = _t80 * _t81;
    int _t83 = k;
    int _t84 = _t82 + _t83;
    double _t85 = data_real[_t84];
    double _t86 = temp_real;
    double _t87 = _t85 - _t86;
    data_real[_t78] = _t87;
    int _t88 = j;
    int _t89 = 2 * _t88;
    int _t90 = buttersPerGroup;
    int _t91 = _t89 * _t90;
    int _t92 = k;
    int _t93 = _t91 + _t92;
    double _t95 = data_real[_t93];
    double _t94 = temp_real;
    _t95 = _t95 + _t94;
    data_real[_t93] = _t95;
    int _t96 = j;
    int _t97 = 2 * _t96;
    int _t98 = buttersPerGroup;
    int _t99 = _t97 * _t98;
    int _t100 = buttersPerGroup;
    int _t101 = _t99 + _t100;
    int _t102 = k;
    int _t103 = _t101 + _t102;
    int _t104 = j;
    int _t105 = 2 * _t104;
    int _t106 = buttersPerGroup;
    int _t107 = _t105 * _t106;
    int _t108 = k;
    int _t109 = _t107 + _t108;
    double _t110 = data_imag[_t109];
    double _t111 = temp_imag;
    double _t112 = _t110 - _t111;
    data_imag[_t103] = _t112;
    int _t113 = j;
    int _t114 = 2 * _t113;
    int _t115 = buttersPerGroup;
    int _t116 = _t114 * _t115;
    int _t117 = k;
    int _t118 = _t116 + _t117;
    double _t120 = data_imag[_t118];
    double _t119 = temp_imag;
    _t120 = _t120 + _t119;
    data_imag[_t118] = _t120;
    k = k + 1;
    goto _l10;
    _l9: ;
    j = j + 1;
    goto _l7;
    _l6: ;
    int _t121 = groupsPerStage << 1;
    groupsPerStage = _t121;
    int _t122 = buttersPerGroup >> 1;
    buttersPerGroup = _t122;
    i = i + 1;
    goto _l4;
    _l3: ;
    double sum = 0.0;
    i = 0;
    _l13: ;
    int _t123 = i;
    if (_t123 < 1023) goto _l14;
    goto _l12;
    _l14: ;
    int _t125 = i;
    double _t126 = data_real[_t125];
    double _t127 = 11.1 * _t126;
    double _t128 = sum + _t127;
    sum = _t128;
    int _t124 = i;
    i = i + 1;
    goto _l13;
    _l12: ;
    double _t129 = sum;
    return _t129;
}"""


        private const val Program23 =
"""int test23() {
    int a[10];
    int i = 0;
    while (i < 10) {
        a[i] = 1;
        a[i] = 0;
        ++i;
    }
    int x = 0;
    int y = 1;
    int z = 5;
    x = y + z;
    a[x] = x;
    return a[0];
}"""
        private const val IProgram23 =
"""int test23() {
    int a[10];
    int i = 0;
    _l1: ;
    int _t0 = i;
    if (_t0 < 10) goto _l2;
    goto _l0;
    _l2: ;
    int _t1 = i;
    a[_t1] = 1;
    int _t2 = i;
    a[_t2] = 0;
    i = i + 1;
    goto _l1;
    _l0: ;
    int x = 0;
    int y = 1;
    int z = 5;
    int _t3 = y;
    int _t4 = z;
    int _t5 = _t3 + _t4;
    x = _t5;
    int _t6 = x;
    int _t7 = x;
    a[_t6] = _t7;
    int _t8 = a[0];
    return _t8;
}"""

        private const val Program24 =
"""int test24() {
    int a[10];
    int i = 0;
    do {
        a[i] = 1;
        a[i] = i;
        ++i;
    } while (i < 10);
    int x = 0;
    int y = 1;
    int z = 5;
    x = y + z;
    a[x] = x;
    return a[x - 1];
}"""
        private const val IProgram24 =
"""int test24() {
    int a[10];
    int i = 0;
    _l1: ;
    int _t0 = i;
    a[_t0] = 1;
    int _t1 = i;
    int _t2 = i;
    a[_t1] = _t2;
    i = i + 1;
    int _t3 = i;
    if (_t3 < 10) goto _l1;
    goto _l0;
    _l0: ;
    int x = 0;
    int y = 1;
    int z = 5;
    int _t4 = y;
    int _t5 = z;
    int _t6 = _t4 + _t5;
    x = _t6;
    int _t7 = x;
    int _t8 = x;
    a[_t7] = _t8;
    int _t9 = x;
    int _t10 = _t9 - 1;
    int _t11 = a[_t10];
    return _t11;
}"""
    }
}