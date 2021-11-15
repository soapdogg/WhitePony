package regression

import compiler.Compiler
import compiler.parser.ParserSingleton
import compiler.tokenizer.TokenizerSingleton
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CompilerTest {

    private val tokenizer = TokenizerSingleton.INSTANCE.tokenizer
    private val recursiveParser = ParserSingleton.INSTANCE.recursiveParser
    private val recursiveCompiler = Compiler(
        tokenizer,
        recursiveParser
    )

    @ParameterizedTest
    @MethodSource(
        "inputData",
    )
    fun regressionTest(arguments: ArgumentsAccessor) {
        val pair = arguments.get(0) as Pair<*, *>
        val input = pair.first as String
        val expectedSize = pair.second as Int
        val result = tokenizer.tokenize(input)

        Assertions.assertEquals(expectedSize, result.size)

        Assertions.assertDoesNotThrow {
            recursiveCompiler.compile(input)
        }
    }

    @Test
    fun regressionIndividualTest() {

        Assertions.assertDoesNotThrow {
            recursiveCompiler.compile(Program12)
        }
    }

    companion object {
        @JvmStatic
        fun inputData(): Stream<Pair<String, Int>> {
            return Stream.of(
                //Pair(Program1, 34),
                Pair(Program2, 37),
                Pair(Program3, 34),
                Pair(Program4, 28),
                Pair(Program5, 36),
                Pair(Program6, 44),
                Pair(Program7, 71),
                Pair(Program8, 30),
//                Pair(Program9, 100),
                Pair(Program10, 22),
                Pair(Program11, 22),
                Pair(Program12, 32),
                Pair(Program13, 30),
                Pair(Program14, 37),
                Pair(Program15, 44),
                Pair(Program16, 42),
                Pair(Program17, 37),
//                Pair(Program18, 42),
                Pair(Program19, 39),
                Pair(Program20, 39),
//                Pair(Program21, 152),
//                Pair(Program22, 367)
            )
        }


        private const val Program1 = """
        int test1(){
            int a ;
            {
                int b = 5 ;
                {
                    int c = 10 ;
                    {
                        a = b + c ;
                    }
                }
            }
            return a ;
        }
    """

        private const val Program2 = """
        int test2() {
            int d = 1;
            d = 1 | 2;
            d = d ^ 4;
            d = d & 7;
            d = ~d;
            return d;
        }
    """

        private const val Program3 = """
            int test3(){
                int e = 4;
                e <<= 3;
                e >>= 2;
                e &= 7 ;
                e |= 15 ;
                e &= 4 ;
                return e ;
            }
        """

        private const val Program4 = """
            int test4(){
                int x;
                int y = 2;
                int z = 3;
                x = y + z;
                return x;
            }
        """

        private const val Program5 = """
            int test5(){
                int w, x, q;
                int y = 4;
                int z = 10;
                w = x  = q = y + z;
                return w;
            }
        """

        private const val Program6 = """
            int test6(){
                int a[10];
                int x = 9;
                int y = 1;
                int z = 0;
                a[x=y+z] = x;
                return a[x];
            }
        """

        private const val Program7 = """
            int test7(){
                int a[10];
                int i;
                for(i = 0; i < 10; ++i){
                    a[i] = 0;
                }
                int x = 0;
                int y = 1;
                int z = 5;
	            x = y + z;
	            a[x] = x;
                return a[x];
            }
        """

        private const val Program8 = """
            int test8(){
                int x = 102;
                int y = 34;
                int z = 1232;
                x += y + z;
                return x;
            }
        """

        private const val Program9 = """
            int test9(){
                int i = 0;
                int j = 0;
                do {
                    if(i == 1){
                        j += i;
                    }
                    else if(i % 2 == 0) j += i;
                    else{
                        if(i == 9){
                            i++;
                        } 
		                else if(i != 6){
                            --i;
                            i += j;
                        }
                    }
                    ++i;
                }
	            while(i < 10);

                i = 0;
                while(i < 10) i++;
                return j;
            }
        """

        private const val Program10 = """
            int test10(){
                int x;
                int y  = 10;
                x = ++y;
                return x;
            }
        """

        private const val Program11 = """
            int test11(){
                int x;
                int y = 19;
                x = y++;
                return x;
            }
        """

        private const val Program12 = """
            int test12(){
                int a = 3;
                int b = 945;
                int c = 93483;
                return a + b * b + c - 23;
            }
        """

        private const val Program13 = """
            int test13(){
                int x = 2;
                int y = 4;
                int z;
                z = (x + y);
                return z;
            }
        """

        private const val Program14 = """
            int test14(){
                int a[10];
                a[1] = 9;
                int y = 1;
                int x;
                x = a[y];
                return x;
            }
        """

        private const val Program15 = """
            int test15(){
                int a[10];
                int x = 34;
                int y = 2;
                a[0] = 45;
                a[0] += x * y;
                return a[0];
            }
        """

        private const val Program16 = """
            int test16(){
                int x[10];
                int y = 0;
                x[0] = 34;
                ++x[y];
                x[y]++;
                return x[y];
            }
        """

        private const val Program17 = """
            int test17(){
                int x = -2;
                int y = 35653;
                if(x + 1 < 0) y = 1;
                else y = 2; 
                return y;
            }
        """

        private const val Program18 = """
            int test18(){
                int x = -1;
                int y = 0;
                int z = 234;
                if(!(x < 0 && y < 1)) z = 2;
                return z;
            }
        """

        private const val Program19 = """
            int test19(){
                int x = -1;
                int y = 0;
                int z = 234132;
                if(x < 0 || y < 1) z = 2;
                return z;
            }
        """

        private const val Program20 = """
            int test20(){
                int x = -1; 
                int y = 0;
                int z = 234245;
                if(x < 0 && y >= 1) z = 6;
                return z;
            }
        """

        private const val Program21 = """
            int test21(){
                int x = 0;
                int y = 1;
                int z = 7;
                int i;
                int j, k;
                k = 2;
                while (k < 345){
                    for(j = 0; j < 100; ++j){
                        if (x > 1000 || z % 2 == 1) z++;
                        else if(y == 1 && z == 7) z += 34;
                        else z = 3;


                        i = 54;
                        do{
                            x += z * y;
                            y++;
                            ++i;
                        }	
        	            while(i  < 100);
                    }
                    if(k % 2 == 1 && !(z > 1194 || 697 < k)) k += i * j;
                    else k += i + j;
                }
                return x;
            }
        """

        private const val Program22 = """
            double data_real[1024], data_imag[1024];
            double coef_real[1024];
            double coef_imag[1024];

            double fft()
            {
                int i, j, k;

                double temp_real;
                double temp_imag;
                double Wr;
                double Wi;

                double ir = 0.0;

                for (i=0 ; i<1024 ; i++){
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
                        Wr = coef_real[(1<<i)-1+j];
                        Wi = coef_imag[(1<<i)-1+j];
                        for (k = 0; k < buttersPerGroup; ++k) {
                            temp_real = Wr * data_real[2*j*buttersPerGroup+buttersPerGroup+k] -
                                Wi * data_imag[2*j*buttersPerGroup+buttersPerGroup+k];
                            temp_imag = Wi * data_real[2*j*buttersPerGroup+buttersPerGroup+k] +
                                Wr * data_imag[2*j*buttersPerGroup+buttersPerGroup+k];
                            data_real[2*j*buttersPerGroup+buttersPerGroup+k] =
                                data_real[2*j*buttersPerGroup+k] - temp_real;
                            data_real[2*j*buttersPerGroup+k] += temp_real;
                            data_imag[2*j*buttersPerGroup+buttersPerGroup+k] =
                                data_imag[2*j*buttersPerGroup+k] - temp_imag;
                            data_imag[2*j*buttersPerGroup+k] += temp_imag;
                        }
                    }
                    groupsPerStage <<= 1;
                    buttersPerGroup >>= 1;
                }

                double sum = 0.0;
                for (i=0 ; i<1023 ; i++) sum += 11.1 * data_real[i]; 
                return sum;
            }
        """
    }
}