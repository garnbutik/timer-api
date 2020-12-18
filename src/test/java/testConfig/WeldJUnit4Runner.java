package testConfig;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldJUnit4Runner extends BlockJUnit4ClassRunner {
    public WeldJUnit4Runner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        final Class<?> test = getTestClass().getJavaClass();
        return WeldContext.INSTANCE.getBean(test);    }
}
