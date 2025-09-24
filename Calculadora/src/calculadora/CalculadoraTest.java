package calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {

    private Calculadora calc;

    @BeforeEach
    void setUp() {
        calc = new Calculadora();
    }

    @Test
    void testSomaSimples() {
        String resultado = calc.evaluateExpression("2+3");
        assertEquals("5.0", resultado);
    }

    @Test
    void testSubtracao() {
        String resultado = calc.evaluateExpression("10-4");
        assertEquals("6.0", resultado);
    }

    @Test
    void testMultiplicacao() {
        String resultado = calc.evaluateExpression("5*6");
        assertEquals("30.0", resultado);
    }

    @Test
    void testDivisao() {
        String resultado = calc.evaluateExpression("20/4");
        assertEquals("5.0", resultado);
    }

    @Test
    void testOrdemDeOperacoes() {
        String resultado = calc.evaluateExpression("2+3*4");
        assertEquals("14.0", resultado);
    }

    @Test
    void testParenteses() {
        String resultado = calc.evaluateExpression("(2+3)*4");
        assertEquals("20.0", resultado);
    }

    @Test
    void testExpressaoInvalida() {
        String resultado = calc.evaluateExpression("2+*3");
        assertEquals("Erro", resultado);
    }

    @Test
    void testDivisaoPorZero() {
        String resultado = calc.evaluateExpression("5/0");
        assertEquals("Erro", resultado); // depende de como sua lógica trata isso
    }
}
