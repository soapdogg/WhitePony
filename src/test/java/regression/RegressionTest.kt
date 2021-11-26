package regression

import compiler.Compiler
import compiler.parser.ParserSingleton
import compiler.printer.PrinterSingleton
import compiler.tokenizer.TokenizerSingleton
import compiler.translator.TranslatorSingleton
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class RegressionTest {

    private val tokenizer = TokenizerSingleton.INSTANCE.tokenizer
    private val recursiveParser = ParserSingleton.INSTANCE.parser
    private val translator = TranslatorSingleton.INSTANCE.translator
    private val printer = PrinterSingleton.INSTANCE.printer
    private val recursiveCompiler = Compiler(
        tokenizer,
        recursiveParser,
        translator,
        printer
    )

    @ParameterizedTest
    @MethodSource(
        "inputData",
    )
    fun regressionTest(arguments: ArgumentsAccessor) {
        val triple = arguments.get(0) as Triple<*, *, *>
        val input = triple.first as String
        val intermediateCode = triple.second as String?

        val (parseTreeString, translatedTreeString) = recursiveCompiler.compile(input)
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

    @Test
    fun regressionIndividualTest() {

        val input = Program24
        val intermediateCode = null
        val (parseTreeString, translatedTreeString) = recursiveCompiler.compile(input)
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

    companion object {
        @JvmStatic
        fun inputData(): Stream<Triple<String, String?, Int>> {
            return Stream.of(
                Triple(Program1, IProgram1, 34),
                Triple(Program2, IProgram2, 37),
                Triple(Program3, IProgram3, 34),
                Triple(Program4, IProgram4, 28),
                Triple(Program5, IProgram5, 36),
                Triple(Program6, IProgram6, 44),
                Triple(Program7, IProgram7, 71),
                Triple(Program8, IProgram8, 30),
                Triple(Program9, null, 100),
                Triple(Program10, IProgram10, 22),
                Triple(Program11, IProgram11, 22),
                Triple(Program12, IProgram12, 32),
                Triple(Program13, IProgram13, 30),
                Triple(Program14, IProgram14, 37),
                Triple(Program15, IProgram15, 44),
                Triple(Program16, IProgram16, 42),
                Triple(Program17, IProgram17, 37),
                Triple(Program18, IProgram18, 42),
                Triple(Program19, IProgram19, 39),
                Triple(Program20, IProgram20, 39),
                Triple(Program21, null, 152),
                Triple(Program22, null, 367),
                Triple(Program23, IProgram23, 0),
                Triple(Program24, IProgram24, 0)
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