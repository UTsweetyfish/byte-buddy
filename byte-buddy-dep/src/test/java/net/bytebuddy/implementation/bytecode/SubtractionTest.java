package net.bytebuddy.implementation.bytecode;

import net.bytebuddy.implementation.Implementation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Parameterized.class)
public class SubtractionTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Subtraction.INTEGER, StackSize.SINGLE, Opcodes.ISUB},
                {Subtraction.LONG, StackSize.DOUBLE, Opcodes.LSUB},
                {Subtraction.FLOAT, StackSize.SINGLE, Opcodes.FSUB},
                {Subtraction.DOUBLE, StackSize.DOUBLE, Opcodes.DSUB}
        });
    }

    private final StackManipulation stackManipulation;

    private final StackSize stackSize;

    private final int opcodes;

    public SubtractionTest(StackManipulation stackManipulation, StackSize stackSize, int opcodes) {
        this.stackManipulation = stackManipulation;
        this.stackSize = stackSize;
        this.opcodes = opcodes;
    }

    @Rule
    public MethodRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private MethodVisitor methodVisitor;

    @Mock
    private Implementation.Context implementationContext;

    @Test
    public void testSubtraction() {
        StackManipulation.Size size = stackManipulation.apply(methodVisitor, implementationContext);
        assertThat(size.getMaximalSize(), is(0));
        assertThat(size.getSizeImpact(), is(-stackSize.getSize()));
        verify(methodVisitor).visitInsn(opcodes);
        verifyNoMoreInteractions(methodVisitor);
        verifyNoMoreInteractions(implementationContext);
    }
}