package springbook.learningtest.template;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        int a = calculator.calcSum(this.numFilepath);
        System.out.println(a);
        assertThat(a, is(130));
    }
    @Test
    public void multiplyOfNumbers() throws IOException {
        int a = calculator.calcMultiply(this.numFilepath);
        System.out.println(a);
        assertThat(a, is(2904));
    }
    @Test
    public void sumOfStrings() throws IOException {
        String a = calculator.Concatenate(this.numFilepath);
        System.out.println(a);
        assertThat(a, is("121234"));
    }

}

